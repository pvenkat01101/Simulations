// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.nuclearphysics.common;

import java.awt.Color;

import edu.colorado.phet.nuclearphysics.NuclearPhysicsConstants;
import edu.colorado.phet.nuclearphysics.NuclearPhysicsStrings;
import edu.colorado.phet.nuclearphysics.common.model.AtomicNucleus;

/**
 * This class encapsulates the information that is used to display each of the
 * nuclei used in this simulation.  The information includes textual strings
 * like the chemical symbol and the textual name (e.g. carbon-14) as well as
 * other display information such as the color to use for the text when
 * placing a label on a nucleus.
 * 
 * @author John Blanco
 */
public class NucleusDisplayInfo {
	
    //------------------------------------------------------------------------
    // Class Data
    //------------------------------------------------------------------------

	public static final NucleusDisplayInfo HYDROGEN_3_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.HYDROGEN_3_LEGEND_LABEL,
			NuclearPhysicsStrings.HYDROGEN_3_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.HYDROGEN_3_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.HYDROGEN_3_LABEL_COLOR,
			NuclearPhysicsConstants.HYDROGEN_COLOR, 
			"hydrogen-nucleus.png" );

	public static final NucleusDisplayInfo HELIUM_3_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.HELIUM_3_LEGEND_LABEL,
			NuclearPhysicsStrings.HELIUM_3_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.HELIUM_3_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.HELIUM_3_LABEL_COLOR,
			NuclearPhysicsConstants.HELIUM_COLOR, 
			"helium-nucleus.png" );

	public static final NucleusDisplayInfo CARBON_14_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.CARBON_14_LEGEND_LABEL,
			NuclearPhysicsStrings.CARBON_14_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.CARBON_14_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.CARBON_14_LABEL_COLOR,
			NuclearPhysicsConstants.CARBON_COLOR, 
			"atomic_nucleus_with_around_15_nucleons.png" );

	public static final NucleusDisplayInfo NITROGEN_14_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.NITROGEN_14_LEGEND_LABEL,
			NuclearPhysicsStrings.NITROGEN_14_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.NITROGEN_14_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.NITROGEN_14_LABEL_COLOR,
			NuclearPhysicsConstants.NITROGEN_COLOR, 
			"atomic_nucleus_with_around_15_nucleons.png" );

	public static final NucleusDisplayInfo LIGHT_CUSTOM_NUCLEUS_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_LEGEND_LABEL,
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_CHEMICAL_SYMBOL,
			"",  // No isotope number for the custom nucleus.
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_LABEL_COLOR,
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_PRE_DECAY_COLOR, 
			"atomic_nucleus_with_around_15_nucleons.png" );

	public static final NucleusDisplayInfo DECAYED_LIGHT_CUSTOM_NUCLEUS_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_LEGEND_LABEL,
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_CHEMICAL_SYMBOL,
			"",  // No isotope number for the custom nucleus.
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_POST_DECAY_LABEL_COLOR,
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_POST_DECAY_COLOR, 
			"atomic_nucleus_with_around_15_nucleons.png" );

	public static final NucleusDisplayInfo URANIUM_235_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.URANIUM_235_LEGEND_LABEL,
			NuclearPhysicsStrings.URANIUM_235_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.URANIUM_235_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.URANIUM_235_LABEL_COLOR,
			NuclearPhysicsConstants.URANIUM_COLOR, 
			"polonium-nucleus-small.png" );
	
	public static final NucleusDisplayInfo URANIUM_236_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.URANIUM_236_LEGEND_LABEL,
			NuclearPhysicsStrings.URANIUM_236_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.URANIUM_236_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.URANIUM_236_LABEL_COLOR,
			NuclearPhysicsConstants.URANIUM_COLOR, 
			"polonium-nucleus-small.png" );
	
	public static final NucleusDisplayInfo URANIUM_238_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.URANIUM_238_LEGEND_LABEL,
			NuclearPhysicsStrings.URANIUM_238_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.URANIUM_238_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.URANIUM_238_LABEL_COLOR,
			NuclearPhysicsConstants.URANIUM_COLOR, 
			"polonium-nucleus-small.png" );
	
	public static final NucleusDisplayInfo URANIUM_239_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.URANIUM_239_LEGEND_LABEL,
			NuclearPhysicsStrings.URANIUM_239_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.URANIUM_239_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.URANIUM_239_LABEL_COLOR,
			NuclearPhysicsConstants.URANIUM_COLOR, 
			"polonium-nucleus-small.png" );
	
	public static final NucleusDisplayInfo POLONIUM_211_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.POLONIUM_211_LEGEND_LABEL,
			NuclearPhysicsStrings.POLONIUM_211_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.POLONIUM_211_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.POLONIUM_LABEL_COLOR,
			NuclearPhysicsConstants.POLONIUM_COLOR, 
			"polonium-nucleus-small.png" );

	public static final NucleusDisplayInfo LEAD_206_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.LEAD_206_LEGEND_LABEL,
			NuclearPhysicsStrings.LEAD_206_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.LEAD_206_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.LEAD_LABEL_COLOR,
			NuclearPhysicsConstants.LEAD_COLOR, 
			"polonium-nucleus-small.png" );

	public static final NucleusDisplayInfo LEAD_207_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.LEAD_207_LEGEND_LABEL,
			NuclearPhysicsStrings.LEAD_207_CHEMICAL_SYMBOL,
			NuclearPhysicsStrings.LEAD_207_ISOTOPE_NUMBER,
			NuclearPhysicsConstants.LEAD_LABEL_COLOR,
			NuclearPhysicsConstants.LEAD_COLOR, 
			"polonium-nucleus-small.png" );

	public static final NucleusDisplayInfo HEAVY_CUSTOM_NUCLEUS_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_LEGEND_LABEL,
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_CHEMICAL_SYMBOL,
			"",  // No isotope number for the custom nucleus.
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_LABEL_COLOR,
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_PRE_DECAY_COLOR, 
			"polonium-nucleus-small.png" );

	public static final NucleusDisplayInfo DECAYED_HEAVY_CUSTOM_NUCLEUS_DISPLAY_INFO = new NucleusDisplayInfo(
			NuclearPhysicsStrings.DECAYED_CUSTOM_NUCLEUS_LEGEND_LABEL,
			NuclearPhysicsStrings.CUSTOM_NUCLEUS_CHEMICAL_SYMBOL,
			"",  // No isotope number for the custom nucleus.
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_POST_DECAY_LABEL_COLOR,
			NuclearPhysicsConstants.CUSTOM_NUCLEUS_POST_DECAY_COLOR, 
			"polonium-nucleus-small.png" );

	public static final NucleusDisplayInfo DEFAULT_DISPLAY_INFO = new NucleusDisplayInfo(
			"XX",
			"XX",
			"XX",
			Color.WHITE,
			Color.GRAY, 
			"polonium-nucleus-small.png" );
	
    //------------------------------------------------------------------------
    // Instance Data
    //------------------------------------------------------------------------
	
	private final String longName;            // Mostly used in control panels.
	private final String chemicalSymbol;      // Used all over, but especially when labeling a nucleus.
	private final String isotopeNumberString; // Mostly used when labeling a nucleus.
	private final Color labelColor;           // Mostly used when labeling a nucleus.
	private final Color displayColor;         // Color used when portraying nucleus as a sphere.
	private final String imageName;           // Used when representing this nucleus as an image.

    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------
	
	/**
	 * Constructor.  Not intended to be instantiated outside of this class,
	 * but this could change if for some reason more dynamic display info is
	 * needed in the future.
	 */
	private NucleusDisplayInfo(
			String name, 
			String chemicalSymbol, 
			String isotopeNumberString, 
			Color labelColor,
			Color displayColor,
			String imageName) {
		this.longName = name;
		this.chemicalSymbol = chemicalSymbol;
		this.isotopeNumberString = isotopeNumberString;
		this.labelColor = labelColor;
		this.displayColor = displayColor;
		this.imageName = imageName;
	}

    //------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------

	public String getName() {
		return longName;
	}

	public String getChemicalSymbol() {
		return chemicalSymbol;
	}

	public String getIsotopeNumberString() {
		return isotopeNumberString;
	}

	public Color getLabelColor() {
		return labelColor;
	}

	public Color getDisplayColor() {
		return displayColor;
	}

	public String getImageName() {
		return imageName;
	}

	public static NucleusDisplayInfo getDisplayInfoForNucleusType(NucleusType nucleusType){
		
		if (nucleusType == null){
			return null;
		}
		
		NucleusDisplayInfo displayInfo = null;

		switch (nucleusType){
		case HYDROGEN_3:
			displayInfo = HYDROGEN_3_DISPLAY_INFO;
			break;

		case HELIUM_3:
			displayInfo = HELIUM_3_DISPLAY_INFO;
			break;
		
		case CARBON_14:
			displayInfo = CARBON_14_DISPLAY_INFO;
			break;

		case NITROGEN_14:
			displayInfo = NITROGEN_14_DISPLAY_INFO;
			break;
			
		case LIGHT_CUSTOM:
			displayInfo = LIGHT_CUSTOM_NUCLEUS_DISPLAY_INFO;
			break;
			
		case LIGHT_CUSTOM_POST_DECAY:
			displayInfo = DECAYED_LIGHT_CUSTOM_NUCLEUS_DISPLAY_INFO;
			break;
			
		case LEAD_206:
			displayInfo = LEAD_206_DISPLAY_INFO;
			break;
			
		case LEAD_207:
			displayInfo = LEAD_207_DISPLAY_INFO;
			break;
			
		case POLONIUM_211:
			displayInfo = POLONIUM_211_DISPLAY_INFO;
			break;
			
		case URANIUM_235:
			displayInfo = URANIUM_235_DISPLAY_INFO;
			break;
			
		case URANIUM_236:
			displayInfo = URANIUM_236_DISPLAY_INFO;
			break;
			
		case URANIUM_238:
			displayInfo = URANIUM_238_DISPLAY_INFO;
			break;
			
		case URANIUM_239:
			displayInfo = URANIUM_239_DISPLAY_INFO;
			break;
			
		case HEAVY_CUSTOM:
			displayInfo = HEAVY_CUSTOM_NUCLEUS_DISPLAY_INFO;
			break;
			
		case HEAVY_CUSTOM_POST_DECAY:
			displayInfo = DECAYED_HEAVY_CUSTOM_NUCLEUS_DISPLAY_INFO;
			break;
			
		default:
			System.err.println("Warning: No display information available for selected nucleus " + nucleusType + ", using default");
			assert false;  // Add the needed information if you hit this while debugging.
		    displayInfo = DEFAULT_DISPLAY_INFO;
		    break;
		}
		
		return displayInfo;
	}
	
	public static NucleusDisplayInfo getDisplayInfoForNucleusConfig( int numProtons, int numNeutrons ){
		
		return getDisplayInfoForNucleusType(AtomicNucleus.identifyNucleus(numProtons, numNeutrons));
		
	}
}
