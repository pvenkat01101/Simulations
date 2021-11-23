// Copyright 2002-2011, University of Colorado

/*
 * CVS Info -
 * Filename : $Source$
 * Branch : $Name$
 * Modified by : $Author: samreid $
 * Revision : $Revision: 47772 $
 * Date modified : $Date: 2011-01-08 01:23:07 +0530 (Sat, 08 Jan 2011) $
 */
package edu.colorado.phet.reactionsandrates.model;

/**
 * Selectable
 *
 * @author Ron LeMaster
 * @version $Revision: 47772 $
 */
public interface Selectable {
    static class Selection {
        private Selection() {
        }

        ;
    }

    static Selection NOT_SELECTED = new Selection();
    static Selection SELECTED = new Selection();
    static Selection NEAREST_TO_SELECTED = new Selection();

    void setSelectionStatus( Selection selection );

    Selection getSelectionStatus();
}
