// Copyright 2002-2012, University of Colorado
package edu.colorado.phet.sugarandsaltsolutions.common.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import edu.colorado.phet.common.phetcommon.math.vector.Vector2D;
import edu.colorado.phet.common.phetcommon.util.Option;
import edu.colorado.phet.common.phetcommon.util.function.Function0;
import edu.colorado.phet.common.phetcommon.util.function.Function1;
import edu.colorado.phet.common.phetcommon.util.logging.LoggingUtils;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.DivisionResult;
import edu.colorado.phet.sugarandsaltsolutions.micro.model.OpenSite;

import static edu.colorado.phet.common.phetcommon.math.vector.Vector2D.ZERO;
import static java.util.Collections.min;

/**
 * Marker class to signify which compounds are crystals vs noncrystals.
 *
 * @author Sam Reid
 */
public abstract class Crystal<T extends Particle> extends Compound<T> {

    //The chemical formula such as NaCl or CaCl2 for purposes of maintaining the formula ratio so the crystal can't become unbalanced
    public final Formula formula;

    //The spacing between components on the lattice
    public final double spacing;

    //Direction vectors (non-unit vectors) in the coordinate frame of the lattice, at the right spacing and angle for generating the lattice topology
    protected final Vector2D northUnitVector;
    protected final Vector2D southUnitVector;
    protected final Vector2D eastUnitVector;
    protected final Vector2D westUnitVector;

    //Randomness for growing the crystal
    public final Random random = new Random();

    private static final Logger LOGGER = LoggingUtils.getLogger( Crystal.class.getCanonicalName() );

    //Construct the compound from the specified lattice
    public Crystal( Formula formula, Vector2D position, double spacing, double angle ) {
        super( position, angle );
        this.formula = formula;
        this.spacing = spacing;

        //Update positions so the lattice position overwrites constituent particle positions
        updateConstituentLocations();

        northUnitVector = new Vector2D( 0, 1 ).times( spacing ).getRotatedInstance( angle );
        southUnitVector = new Vector2D( 0, -1 ).times( spacing ).getRotatedInstance( angle );
        eastUnitVector = new Vector2D( 1, 0 ).times( spacing ).getRotatedInstance( angle );
        westUnitVector = new Vector2D( -1, 0 ).times( spacing ).getRotatedInstance( angle );
    }

    //Create an instance that could bond with the specified original particle for purposes of growing crystals from scratch
    public abstract T createPartner( T original );

    //Grow the crystal for the specified number of formula ratios
    //Returns "true" if successful
    public boolean grow( int numberFormulaRatios ) {

        //There is a random aspect to crystal growth and in some cases (particularly for the more constrained case of CaCl2's lattice topology)
        //The growth can run into a dead end where it is impossible to add a full formula unit.
        //To handle this problem, try many times to generate a crystal and keep the first one that doesn't run into a dead end
        //If the sim fails to grow a lattice, then the Crystal is cleared and the sim continues to run
        int numTries = 10000;
        for ( int tryIndex = 0; tryIndex < numTries; tryIndex++ ) {
            //Grow the full crystal
            boolean success = growRandomly( numberFormulaRatios );
            if ( success ) {
                return true;
            }
            else {
                //If there was a dead end, clear this crystal and try again
                System.out.println( "crystal growth failed: tryIndex = " + tryIndex );
                while ( numberConstituents() > 0 ) {
                    removeConstituent( getConstituent( 0 ) );
                }
            }
        }

        //No success after numTries tries, so signify a failure (shouldn't happen unless you have an unusual lattice structure)
        return false;
    }

    //Grow the entire crystal for the specified number of formula ratios, fails if it runs into a dead end.  In that case it should be re-run
    //Returns "true" if successful
    private boolean growRandomly( int numberFormulaRatios ) {
        for ( int i = 0; i < numberFormulaRatios; i++ ) {
            boolean success = growByOneFormulaUnit();
            if ( !success ) {

                //Indicate a failure immediately if we couldn't even grow by one formula unit
                return false;
            }
        }

        //All steps completed, so signify successful
        return true;
    }

    //Grow the crystal randomly at one of the open sites by adding a full formula (such as 1 Ca and 2Cl for CaCl2)
    //Returns "true" if successful
    //If it is impossible to continue growing the crystal then exit by returning false
    public boolean growByOneFormulaUnit() {
        for ( final Class<? extends Particle> type : formula.getFormulaUnit() ) {
            if ( constituents.size() == 0 ) {
                addConstituent( new Constituent<T>( createConstituentParticle( type ), ZERO ) );
            }
            else {

                //find any particle that has open bonds
                ItemList<OpenSite<T>> openSites = getOpenSites().filter( new Function1<OpenSite<T>, Boolean>() {
                    public Boolean apply( OpenSite<T> site ) {
                        return site.matches( type );
                    }
                } );

                if ( openSites.size() > 0 ) {
                    addConstituent( openSites.get( random.nextInt( openSites.size() ) ).toConstituent() );
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    //Determine all of the available locations where an existing particle could be added
    public ItemList<OpenSite<T>> getOpenSites() {
        ItemList<OpenSite<T>> bondingSites = new ItemList<OpenSite<T>>();
        for ( final Constituent<T> constituent : new ArrayList<Constituent<T>>( constituents ) ) {
            for ( Vector2D direction : getPossibleDirections( constituent ) ) {
                Vector2D relativePosition = constituent.relativePosition.plus( direction );
                if ( !isOccupied( relativePosition ) ) {
                    T opposite = createPartner( constituent.particle );
                    Vector2D absolutePosition = relativePosition.plus( getPosition() );
                    opposite.setPosition( absolutePosition );
                    bondingSites.add( new OpenSite<T>( relativePosition, opposite.getShape(), new Function0<T>() {
                        public T apply() {
                            return createPartner( constituent.particle );
                        }
                    }, absolutePosition ) );
                }
            }
        }

        return bondingSites;
    }

    //This method gets the possible directions in which the crystal can be grown from the given constituent.
    //The default implementation here is a dense square lattice, like a checkerboard
    //This method is overrideable so that other crystal types like CaCl2 can specify their own topology
    //This may not generalize to non-square lattice topologies, but is sufficient for all currently requested crystal types for sugar and salt solutions
    public Vector2D[] getPossibleDirections( Constituent<T> constituent ) {
        return new Vector2D[] { northUnitVector, southUnitVector, eastUnitVector, westUnitVector };
    }

    //Determine whether the specified location is available for bonding or already occupied by another particle
    public boolean isOccupied( final Vector2D location ) {
        return getConstituentAtLocation( location ).isSome();
    }

    //Find the constituent at the specified location, if any
    public Option<Constituent<T>> getConstituentAtLocation( final Vector2D location ) {

        final ItemList<Constituent<T>> atLocation = constituents.filter( new Function1<Constituent<T>, Boolean>() {
            public Boolean apply( Constituent<T> constituent ) {
                return constituent.relativePosition.minus( location ).magnitude() < spacing / 100;
            }
        } );

        //Check to make sure there weren't too many at the specified location.  If so, this was an error during crystal growth.
        if ( atLocation.size() > 1 ) {

            //It has been difficult to identify the cause of this case, so we also overrode addConstituent to check for errors during the build process
            new RuntimeException( "Too many particles at the same location, getting one of them randomly" ).printStackTrace();
            int index = random.nextInt( atLocation.size() );
            return new Option.Some<Constituent<T>>( atLocation.get( index ) );
        }
        else if ( atLocation.size() == 0 ) {
            return new Option.None<Constituent<T>>();
        }
        else {
            return new Option.Some<Constituent<T>>( atLocation.get( 0 ) );
        }
    }

    //Overriden to check for errors during the assembly process
    @Override public void addConstituent( Constituent<T> constituent ) {
        super.addConstituent( constituent );

        //Make sure the constituent at the location is the one and only one we just added, otherwise it will cause an error by putting two constituents at the same lattice site
        Option<Constituent<T>> atLocation = getConstituentAtLocation( constituent.relativePosition );
        if ( atLocation.isSome() && atLocation.get() == constituent ) {
            //All is well
        }
        else {

            //Error during the build process, print an exception with the stack trace so we can find out which step in crystal constriction caused the problem
            new RuntimeException( "Wrong constituent during add process" ).printStackTrace();
        }
    }

    //Create the first constituent particle in a crystal
    protected abstract T createConstituentParticle( Class<? extends Particle> type );

    //Choose a set of particles to dissolve from the crystal according to the formula ratio (e.g. NaCl = 1 Na + 1 Cl or CaCl2 = 1 Ca + 2 Cl) so the crystal and solution will remain balanced
    public Option<ArrayList<Constituent<T>>> getConstituentsToDissolve( final Rectangle2D waterBounds ) {

        //For each type, get as many components as are specified in the formula
        ArrayList<Constituent<T>> toDissolve = new ArrayList<Constituent<T>>();
        for ( Class<? extends Particle> type : formula.getFormulaUnit() ) {
            final Constituent<T> constituentToDissolve = getConstituentToDissolve( type, waterBounds, toDissolve );
            if ( constituentToDissolve == null ) {
                //If couldn't dissolve all elements of formula, then don't dissolve any
                return new Option.None<ArrayList<Constituent<T>>>();
            }
            else {
                toDissolve.add( constituentToDissolve );
            }
        }
        return new Option.Some<ArrayList<Constituent<T>>>( toDissolve );
    }

    //Choose a particle of the specified type, to be dissolved off the crystal used by getConstituentsToDissolve to ensure it dissolves according to the formula ratio
    public Constituent<T> getConstituentToDissolve( final Class<? extends Particle> type, final Rectangle2D waterBounds, final ArrayList<Constituent<T>> ignore ) {

        //Only consider particles that are completely submerged because it would be incorrect for particles outside of the fluid to suddenly disassociate from the crystal
        //Also make sure it is the requested type (to match formula ratio), and make sure it hasn't already been flagged for removal
        ItemList<Constituent<T>> c = constituents.
                filter( new Function1<Constituent<T>, Boolean>() {
                    public Boolean apply( Constituent<T> constituent ) {
                        return waterBounds.contains( constituent.particle.getShape().getBounds2D() );
                    }
                } ).
                filter( new Function1<Constituent<T>, Boolean>() {
                    public Boolean apply( Constituent<T> constituent ) {
                        return type.isInstance( constituent.particle );
                    }
                } ).
                filter( new Function1<Constituent<T>, Boolean>() {
                    public Boolean apply( Constituent<T> constituent ) {
                        return !ignore.contains( constituent );
                    }
                } );

        //Make sure list not empty after filtering based on desired type
        if ( c.size() > 0 ) {

            //Find the smallest number of bonds of any of the particles
            final int minBonds = getNumBonds( min( c, new Comparator<Constituent<T>>() {
                public int compare( Constituent<T> o1, Constituent<T> o2 ) {
                    return Double.compare( getNumBonds( o1 ), getNumBonds( o2 ) );
                }
            } ) );

            //Only consider particles with the smallest number of bonds since they are closest to the edge
            c = c.filter( new Function1<Constituent<T>, Boolean>() {
                public Boolean apply( Constituent<T> constituent ) {
                    return getNumBonds( constituent ) == minBonds;
                }
            } );

            //Sort by y-value so that particles are taken from near the top instead of from the bottom (which would cause the crystal to fall)
            Collections.sort( c, new Comparator<Constituent>() {
                public int compare( Constituent o1, Constituent o2 ) {
                    return Double.compare( o1.particle.getPosition().getY(), o2.particle.getPosition().getY() );
                }
            } );

            LOGGER.fine( "Crystal num components = " + c.size() );
            for ( int i = 0; i < c.size(); i++ ) {
                LOGGER.fine( "" + i + ": " + getNumBonds( c.get( i ) ) );
            }
            LOGGER.fine( "END crystal" );

            //Return the highest item
            if ( c.size() > 0 ) {
                return c.get( c.size() - 1 );
            }
        }
        return null;
    }

    //Determine whether all constituents in the crystal can be reached from all others.  If not, there is a hole in the lattice and the crystal should be dissolved.
    public boolean isConnected() {

        //Empty crystals are not a problem
        if ( constituents.isEmpty() ) {
            return true;
        }

        //Start with an arbitrary node and try to visit all others
        List<Constituent<T>> toVisit = new ArrayList<Constituent<T>>() {{ add( constituents.get( 0 ) ); }};
        List<Constituent<T>> visited = new ArrayList<Constituent<T>>();

        //Traverse any seen nodes and try to visit the entire graph
        while ( !toVisit.isEmpty() ) {

            //Choose an arbitrary node to visit next
            Constituent<T> c = toVisit.get( 0 );
            toVisit.remove( c );
            visited.add( c );

            //Signify that we should visit each unvisited neighbor
            for ( Constituent<T> neighbor : getNeighbors( c ) ) {
                if ( !toVisit.contains( neighbor ) && !visited.contains( neighbor ) ) {
                    toVisit.add( neighbor );
                }
            }
        }

        //If we visited the entire graph, the graph is connected
        return visited.size() == constituents.size();
    }

    //Find the neighbors for the specified constituent
    private ItemList<Constituent<T>> getNeighbors( Constituent<T> constituent ) {
        ItemList<Constituent<T>> neighbors = new ItemList<Constituent<T>>();
        for ( Vector2D direction : new Vector2D[] { northUnitVector, southUnitVector, eastUnitVector, westUnitVector } ) {
            Option<Constituent<T>> option = getConstituentAtLocation( constituent.relativePosition.plus( direction ) );
            if ( option.isSome() ) {
                neighbors.add( option.get() );
            }
        }
        return neighbors;
    }

    //Count the number of bonds by which the constituent is attached, so that particles near the edges (instead of near the centers) will be selected for dissolving
    private int getNumBonds( Constituent<T> constituent ) {
        return getNeighbors( constituent ).size();
    }

    //Check to see if the crystal matches the formula ratio by dividing each constituent count by getting the division results for each, making sure they are the same, and making sure there is no remainder
    public boolean matchesFormulaRatio() {
        LOGGER.fine( "Crystal.matchesFormulaRatio" );
        HashSet<DivisionResult> result = new HashSet<DivisionResult>();
        for ( Class type : formula.getTypes() ) {
            int count = new ItemList<T>( constituents.map( new Function1<Constituent<T>, T>() {
                public T apply( Constituent<T> constituent ) {
                    return constituent.particle;
                }
            } ) ).filter( type ).size();
            int factor = formula.getFactor( type );
            final DivisionResult e = new DivisionResult( count, factor );
            result.add( e );
            LOGGER.fine( e.toString() );
        }
        return result.size() == 1 && result.iterator().next().remainder == 0;
    }
}