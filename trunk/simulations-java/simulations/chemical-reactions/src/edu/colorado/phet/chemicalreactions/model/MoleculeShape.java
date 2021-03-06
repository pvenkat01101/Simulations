// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.chemicalreactions.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.colorado.phet.chemicalreactions.ChemicalReactionsResources.Strings;
import edu.colorado.phet.chemistry.model.Element;
import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;

import static edu.colorado.phet.chemistry.model.Element.C;
import static edu.colorado.phet.chemistry.model.Element.Cl;
import static edu.colorado.phet.chemistry.model.Element.H;
import static edu.colorado.phet.chemistry.model.Element.N;
import static edu.colorado.phet.chemistry.model.Element.O;

/**
 * Specifies the shape of a molecule
 */
public class MoleculeShape {

    public final List<AtomSpot> spots;
    public final String name;

    // angles of rotation that are completely equivalent.
    public final double[] symmetryAngles;

    private MoleculeShape( String name, List<AtomSpot> spots, double[] symmetryAngles ) {
        this.name = name;
        this.spots = Collections.unmodifiableList( spots );
        this.symmetryAngles = symmetryAngles;
    }

    public static class AtomSpot {
        public final Element element;
        public final Vector2D position;

        public AtomSpot( Element element, Vector2D position ) {
            this.element = element;
            this.position = position;
        }
    }

    public double getBoundingCircleRadius() {
        double result = 0;
        for ( AtomSpot spot : spots ) {
            result = Math.max( result, spot.position.magnitude() + spot.element.getRadius() );
        }
        return result;
    }

    /*---------------------------------------------------------------------------*
    * various molecule shapes
    *----------------------------------------------------------------------------*/

    public static final MoleculeShape H2 = new MoleculeShape( Strings.BUCKET___H_2, Arrays.asList(
            new AtomSpot( H, new Vector2D( -H.getRadius(), 0 ) ),
            new AtomSpot( H, new Vector2D( H.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );


    public static final MoleculeShape O2 = new MoleculeShape( Strings.BUCKET___O_2, Arrays.asList(
            new AtomSpot( O, new Vector2D( -O.getRadius(), 0 ) ),
            new AtomSpot( O, new Vector2D( O.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );

    public static final MoleculeShape Cl2 = new MoleculeShape( Strings.BUCKET___CL_2, Arrays.asList(
            new AtomSpot( Cl, new Vector2D( -Cl.getRadius(), 0 ) ),
            new AtomSpot( Cl, new Vector2D( Cl.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );

    public static final MoleculeShape N2 = new MoleculeShape( Strings.BUCKET___N_2, Arrays.asList(
            new AtomSpot( N, new Vector2D( -N.getRadius(), 0 ) ),
            new AtomSpot( N, new Vector2D( N.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );

    public static final MoleculeShape H2O = new MoleculeShape( Strings.BUCKET___H_2_O, Arrays.asList(
            new AtomSpot( O, new Vector2D( 0, 0 ) ),
            new AtomSpot( H, new Vector2D( ( O.getRadius() + H.getRadius() ), 0 ) ),
            new AtomSpot( H, new Vector2D( -( O.getRadius() + H.getRadius() ), 0 ) )
    ), new double[]{0, Math.PI} );

    public static final MoleculeShape HCl = new MoleculeShape( Strings.BUCKET___H_CL, Arrays.asList(
            new AtomSpot( H, new Vector2D( -Cl.getRadius(), 0 ) ),
            new AtomSpot( Cl, new Vector2D( H.getRadius(), 0 ) )
    ), new double[]{0} );

    public static final MoleculeShape NH3 = new MoleculeShape( Strings.BUCKET___NH_3, Arrays.asList(
            new AtomSpot( N, new Vector2D( 0, 0 ) ),

            // top
            new AtomSpot( H, new Vector2D( 0, N.getRadius() + H.getRadius() ) ),

            // left
            new AtomSpot( H, new Vector2D( -N.getRadius() - H.getRadius(), 0 ) ),

            // right
            new AtomSpot( H, new Vector2D( N.getRadius() + H.getRadius(), 0 ) )
    ), new double[]{0} );

    public static final MoleculeShape ClNO2 = new MoleculeShape( Strings.BUCKET___CL_NO_2, Arrays.asList(
            new AtomSpot( N, new Vector2D( 0, 0 ) ),

            // top
            new AtomSpot( Cl, new Vector2D( 0, N.getRadius() + Cl.getRadius() ) ),

            // left
            new AtomSpot( O, new Vector2D( -N.getRadius() - O.getRadius(), 0 ) ),

            // right
            new AtomSpot( O, new Vector2D( N.getRadius() + O.getRadius(), 0 ) )
    ), new double[]{0} );

    public static final MoleculeShape NO = new MoleculeShape( Strings.BUCKET___NO, Arrays.asList(
            new AtomSpot( N, new Vector2D( -O.getRadius(), 0 ) ),
            new AtomSpot( O, new Vector2D( N.getRadius(), 0 ) )
    ), new double[]{0} );

    public static final MoleculeShape NO2 = new MoleculeShape( Strings.BUCKET___NO_2, Arrays.asList(
            new AtomSpot( O, new Vector2D( 0, 0 ) ),
            new AtomSpot( N, new Vector2D( -O.getRadius() - N.getRadius(), 0 ) ),
            new AtomSpot( N, new Vector2D( O.getRadius() + N.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );

    public static final MoleculeShape ClNO = new MoleculeShape( Strings.BUCKET___CL_NO, new ArrayList<AtomSpot>() {{
        double halfWidth = Cl.getRadius() + N.getRadius() + O.getRadius();
        add( new AtomSpot( Cl, new Vector2D( -halfWidth + Cl.getRadius(), 0 ) ) );
        add( new AtomSpot( N, new Vector2D( -halfWidth + Cl.getRadius() * 2 + N.getRadius(), 0 ) ) );
        add( new AtomSpot( O, new Vector2D( halfWidth - O.getRadius(), 0 ) ) );
    }}, new double[]{0} );

    public static final MoleculeShape CH4 = new MoleculeShape( Strings.BUCKET___CH_4, Arrays.asList(
            new AtomSpot( C, new Vector2D( 0, 0 ) ),
            new AtomSpot( H, new Vector2D( H.getRadius() + C.getRadius(), 0 ) ),
            new AtomSpot( H, new Vector2D( -H.getRadius() - C.getRadius(), 0 ) ),
            new AtomSpot( H, new Vector2D( 0, H.getRadius() + C.getRadius() ) ),
            new AtomSpot( H, new Vector2D( 0, -H.getRadius() - C.getRadius() ) )
    ), new double[]{0, Math.PI / 2, Math.PI, 3 * Math.PI / 2} );

    public static final MoleculeShape CO2 = new MoleculeShape( Strings.BUCKET___CO_2, Arrays.asList(
            new AtomSpot( C, new Vector2D( 0, 0 ) ),
            new AtomSpot( O, new Vector2D( O.getRadius() + C.getRadius(), 0 ) ),
            new AtomSpot( O, new Vector2D( -O.getRadius() - C.getRadius(), 0 ) )
    ), new double[]{0, Math.PI} );

}
