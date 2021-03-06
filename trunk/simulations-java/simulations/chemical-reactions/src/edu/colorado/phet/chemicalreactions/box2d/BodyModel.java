// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.chemicalreactions.box2d;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import edu.colorado.phet.chemicalreactions.ChemicalReactionsConstants;
import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.model.property.ObservableProperty;
import edu.colorado.phet.common.phetcommon.model.property.Property;
import edu.colorado.phet.common.phetcommon.util.SimpleObserver;
import edu.colorado.phet.common.phetcommon.view.graphics.transforms.ModelViewTransform;
import edu.colorado.phet.jamaphet.collision.Collidable2D;

/**
 * Wrapper for a box2d body that gives us more convenient properties in a possibly different frame of reference
 */
public class BodyModel implements Collidable2D {

    public final Property<Boolean> userControlled = new Property<Boolean>( false );

    private final Property<Vector2D> _position = new Property<Vector2D>( new Vector2D() );
    private final Property<Vector2D> _velocity = new Property<Vector2D>( new Vector2D() );
    private final Property<Float> _angle = new Property<Float>( 0f );
    private final Property<Float> _angularVelocity = new Property<Float>( 0f ); // may have issues based on shears or scales

    private final List<FixtureDef> fixtureDefs = new ArrayList<FixtureDef>();

    // public read-only copies
    public final ObservableProperty<Vector2D> position = _position;
    public final ObservableProperty<Vector2D> velocity = _velocity;
    public final ObservableProperty<Float> angle = _angle;
    public final ObservableProperty<Float> angularVelocity = _angularVelocity;

    private final BodyDef bodyDef;
    private final ModelViewTransform bodyToModelTransform;
    private Body body;

    public BodyModel( BodyDef bodyDef, ModelViewTransform bodyToModelTransform ) {
        this.bodyDef = bodyDef;
        this.bodyToModelTransform = bodyToModelTransform;

        userControlled.addObserver( new SimpleObserver() {
            public void update() {
                if ( hasBody() ) {
                    Fixture fixture = body.getFixtureList();
                    while ( fixture != null ) {
                        fixture.setDensity( userControlled.get() ? 0 : ChemicalReactionsConstants.BOX2D_DENSITY );

                        // yes, this is actually how you iterate through fixtures. essentially a hard-coded linked list
                        fixture = fixture.m_next;
                    }
                }
            }
        } );
    }

    public void addFixtureDef( FixtureDef fixtureDef ) {
        fixtureDefs.add( fixtureDef );
    }

    public Body getBody() {
        return body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBody( Body body ) {
        this.body = body;
        if ( body != null ) {
            for ( FixtureDef fixtureDef : fixtureDefs ) {
                body.createFixture( fixtureDef );
            }
        }
        setPosition( position.get() );
        setVelocity( velocity.get() );
        setAngle( angle.get() );
        setAngularVelocity( angularVelocity.get() );
    }

    public boolean hasBody() {
        return body != null;
    }

    public void setPosition( Vector2D p ) {
        // TODO: consider not changing this until postStep()? (rounding issues from double->float?)
        if ( !p.equals( _position.get() ) ) { // check required since Property only checks for equality with "="
            _position.set( p );
        }
        if ( hasBody() ) {
            Vector2D boxPosition = bodyToModelTransform.viewToModel( p );
            body.setTransform( new Vec2( (float) boxPosition.getX(), (float) boxPosition.getY() ), body.getAngle() );
            body.setAwake( true );
        }
    }

    public void setVelocity( Vector2D v ) {
        // TODO: consider not changing this until postStep()? (rounding issues from double->float?)
        if ( !v.equals( _velocity.get() ) ) { // check required since Property only checks for equality with "="
            _velocity.set( v );
        }
        if ( hasBody() ) {
            Vector2D boxVelocity = bodyToModelTransform.viewToModelDelta( v );
            body.setLinearVelocity( new Vec2( (float) boxVelocity.getX(), (float) boxVelocity.getY() ) );
            body.setAwake( true );
        }
    }

    public void setAngle( float theta ) {
        _angle.set( theta );
        if ( hasBody() ) {
            float boxAngle = inverseTransformAngle( theta );
            body.setTransform( body.getPosition(), boxAngle );
            body.setAwake( true );
        }
    }

    public void setAngularVelocity( float omega ) {
        _angularVelocity.set( omega );
        if ( hasBody() ) {
            body.setAngularVelocity( omega );
            body.setAwake( true );
        }
    }

    // called within a frame in-between steps, so that we can fix user-controlled bodies in position (also done in density-and-buoyancy)
    public void intraStep() {
        if ( userControlled.get() ) {
            updateUserControlledBox2d();
        }
    }

    public void postStep() {
        assert body != null;
        if ( userControlled.get() ) {
            updateUserControlledBox2d();
        }
        else {
            _position.set( bodyToModelTransform.modelToView( new Vector2D( body.getPosition().x, body.getPosition().y ) ) );
            _velocity.set( bodyToModelTransform.modelToViewDelta( new Vector2D( body.getLinearVelocity().x, body.getLinearVelocity().y ) ) );
            _angle.set( transformAngle( body.getAngle() ) );
            _angularVelocity.set( body.getAngularVelocity() * ( isTransformReflected() ? -1 : 1 ) ); // reverse angular velocity in reflection case
        }
    }

    private void updateUserControlledBox2d() {
        setPosition( position.get() );
        setVelocity( new Vector2D( 0, 0 ) );
        setAngle( angle.get() );
        setAngularVelocity( 0 );
    }

    private boolean isTransformReflected() {
        return bodyToModelTransform.getTransform().getScaleX() * bodyToModelTransform.getTransform().getScaleX() < 0;
    }

    private float transformAngle( float angle ) {
        Vector2D newVector = bodyToModelTransform.modelToViewDelta( new Vector2D( Math.cos( angle ), Math.sin( angle ) ) ).normalized();
        return (float) Math.atan2( newVector.getY(), newVector.getX() );
    }

    private float inverseTransformAngle( float angle ) {
        Vector2D newVector = bodyToModelTransform.viewToModelDelta( new Vector2D( Math.cos( angle ), Math.sin( angle ) ) ).normalized();
        return (float) Math.atan2( newVector.getY(), newVector.getX() );
    }

    public Vector2D getPosition() {
        return position.get();
    }

    public Vector2D getVelocity() {
        return velocity.get();
    }

    public double getAngle() {
        return angle.get();
    }

    public double getAngularVelocity() {
        return angularVelocity.get();
    }
}
