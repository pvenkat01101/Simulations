// Preferences.as
//
// Handles loading of stored client-side preferences data,
// and allows other entities within the simulation to check
// the preferences.
//
// Author: Jonathan Olson


class Preferences {
	
	// current preferences version
	// this SHOULD NOT CHANGE after development, and is an
	// aid for development purposes.
	public static var CURRENT_PREF_VERSION : Number = 1.2;
	
	// current privacy agreement version
	// this should be changed when a new agreement would need to be
	// accepted by people who have accepted an old agreement
	public static var CURRENT_PRIVACY_VERSION : Number = 1.0;
	
	// reference to the shared object used to store preferences
	public var sharedObject : SharedObject;
	
	// "constant"s to refer to fields that change sim-to-sim but
	// are otherwise constant
	public var FIELD_SKIPPED_UPDATE : String;
	public var FIELD_ASK_LATER : String;
	public var FIELD_VISITS_SINCE : String;
	public var FIELD_VISITS_EVER : String;
	
	// shorthand for debugging function
	public function debug(str : String) : Void {
		_level0.debug(str);
	}
	
	// constructor
	public function Preferences() {
		debug("Preferences initializing\n");
		
		// make this object accessible from _level0.preferences
		// should only be one copy of Preferences (singleton-like)
		_level0.preferences = this;
		
		// load the shared object into sharedObject
		load();
		
		// set "constant" strings
		FIELD_SKIPPED_UPDATE = _level0.simName + "_skippedUpdate";
		FIELD_ASK_LATER = _level0.simName + "_askLater";
		FIELD_VISITS_SINCE = _level0.simName + "_visitsSince";
		FIELD_VISITS_EVER = _level0.simName + "_visitsEver";
		
		/////////////////////////////////////////
		// TEMPORARY FOR DEVELOPMENT PURPOSES
		/////////////////////////////////////////
		// if they don't have the newest field, reset their shared object!
		//if(!sharedObject.data.userId) {
			//debug("Preferences: DEVELOPMENT: resetting shared object, new information to be stored\n");
			//reset();
		//}
		/////////////////////////////////////////
		if(!sharedObject.data.dataVersion || sharedObject.data.dataVersion != CURRENT_PREF_VERSION) {
			debug("Preferences: DEVELOPMENT: resetting shared object, new information to be stored\n");
			reset();
		}
		/////////////////////////////////////////
		// for resetting sharedobject data
		Key.addListener(this);
		/////////////////////////////////////////
		
		// if it is the first time simulations have been run from
		// their domain, we need to fill in default values.
		// we set data.exists, so in the future we can tell whether
		// SharedObject.getLocal() reads preferences or creates the
		// object.
		if(!sharedObject.data.exists) {
			debug("Preferences do not exist! Creating, and filling with defaults.\n");
			sharedObject.data.exists = true;
			sharedObject.data.allowTracking = true;
			sharedObject.data.checkForUpdates = true;
			sharedObject.data.dataVersion = CURRENT_PREF_VERSION;
			sharedObject.data.userPreferencesFileCreationTime = (new Date()).valueOf();
			sharedObject.data.userTotalSessions = 0;
			sharedObject.data.latestPrivacyAgreementVersion = 0;
		} else {
			debug("Found preferences\n");
		}
		
		// increment the number of times the current sim has been run
		incrementVisit();
		
		// conditionally add update information if it doesn't exist
		initUpdateInfo();
		
		// save the shared object (preferences) to filesystem.
		save();
		
		// for debugging: print out each piece of data stored in the shared object
		for(var i : String in sharedObject.data) {
			debug("    pref: " + i + " = " + String(sharedObject.data[i]) + "\n");
		}
		
		// if privacy is not up-to-snuff (and user is running from a non-phet-website
		// location), present the user with a dialog
		if(!isPrivacyOK() && !_level0.common.fromPhetWebsite()) {
			_level0.privacyDialog = new PrivacyDialog();
		} else {
			// do everything else once it has been verified
			_level0.common.postAgreement();
		}
		
		// unload the sharedObject from memory. this prevents an out-of-date version of
		// the preferences data to be saved when the sim is closed.
		unload();
	}
	
	// load the preferences data into sharedObject
	public function load() : Void {
		//debug("Preferences: Loading shared object\n");
		sharedObject = SharedObject.getLocal("phetPrefs", "/");
	}
	
	// unload the preferences data from sharedObject. this prevents Flash from saving
	// a possibly out-of-date version when the sim is closed
	public function unload() : Void {
		//debug("Preferences: Unloading shared object\n");
		delete sharedObject;
	}
	
	// returns whether the user has accepted the latest privacy agreement needed for this sim
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function isPrivacyOK() : Boolean {
		return CURRENT_PRIVACY_VERSION <= sharedObject.data.latestPrivacyAgreementVersion;
	}
	
	// saves the user's acceptance of the privacy agreement to preferences
	public function agreeToPrivacy() : Void {
		load();
		sharedObject.data.latestPrivacyAgreementVersion = CURRENT_PRIVACY_VERSION;
		save();
		unload();
	}
	
	// returns whether the user allows messages to be sent
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function userAllowsTracking() : Boolean {
		return sharedObject.data.allowTracking;
	}
	
	// allow other common code/simulation to check whether
	// tracking messages can be sent
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function isTrackingAllowed() : Boolean {
		if(_level0.common.fromPhetWebsite()) {
			debug("From PhET website: no tracking allowed\n");
			return false;
		}
		return userAllowsTracking();
	}
	
	// returns whether the user allows checking for updates
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function userAllowsUpdates() : Boolean {
		return sharedObject.data.checkForUpdates;
	}
	
	// allow other common code/simulation to check whether
	// checking for updates is allowed
	public function areUpdatesAllowed() : Boolean {
		if(_level0.common.fromPhetWebsite()) {
			debug("From PhET website: no updates allowed (or needed)\n");
			return false;
		}
		load();
		var ret : Boolean = userAllowsUpdates();
		unload();
		return ret;
	}
	
	// returns latest skipped update version as [major, minor]
	public function getLatestSkippedUpdate() : Array {
		load();
		return sharedObject.data.skippedUpdate;
		unload();
	}
	
	// set latest skipped update version
	public function setSkippedUpdate(major : Number, minor : Number) : Void {
		load();
		sharedObject.data[FIELD_SKIPPED_UPDATE] = [major, minor];
		save();
		unload();
	}
	
	// set ask me later time
	public function setAskLater() : Void {
		load();
		sharedObject.data[FIELD_ASK_LATER] = (new Date()).valueOf();
		save();
		unload();
	}
	
	// allow other code to set the tracking and updates values
	public function setTracking(updates : Boolean, tracking : Boolean) : Void {
		load();
		sharedObject.data.allowTracking = tracking;
		sharedObject.data.checkForUpdates = updates;
		debug("setting tracking to " + tracking.toString() + "\n");
		debug("setting updates to " + updates.toString() + "\n");
		save();
		unload();
	}
	
	// resets (clears) any data stored on disk
	// (also resets the data in the local copy)
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function reset() : Void {
		debug("Preferences: resetting\n");
		sharedObject.clear();
	}
	
	// saves the shared object (preferences data) to the
	// user's hard drive.
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function save() : Void {
		debug("Preferences: Saving shared object\n");
		sharedObject.flush();
	}
	
	// creates or increments a preferences attribute specifying
	// how many times the current simulation has been run.
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function incrementVisit() : Void {
		debug("Preferences: Incrementing number of visits\n");
		
		// increment total visits
		sharedObject.data.userTotalSessions = sharedObject.data.userTotalSessions + 1;
		
		// check whether property exists first. might be a new sim
		// or one the user hasn't seen yet.
		if(sharedObject.data.hasOwnProperty(FIELD_VISITS_EVER)) {
			sharedObject.data[FIELD_VISITS_EVER] = sharedObject.data[FIELD_VISITS_EVER] + 1;
		} else {
			sharedObject.data[FIELD_VISITS_EVER] = 1;
		}
		
		if(sharedObject.data.hasOwnProperty(FIELD_VISITS_SINCE)) {
			sharedObject.data[FIELD_VISITS_SINCE] = sharedObject.data[FIELD_VISITS_SINCE] + 1;
		} else {
			sharedObject.data[FIELD_VISITS_SINCE] = 1;
		}
	}
	
	// add sim-specific update information
	public function initUpdateInfo() : Void {
		if(!sharedObject.data.hasOwnProperty(FIELD_SKIPPED_UPDATE)) {
			sharedObject.data[FIELD_SKIPPED_UPDATE] = [0, 0];
		}
		
		if(!sharedObject.data.hasOwnProperty(FIELD_ASK_LATER)) {
			sharedObject.data[FIELD_ASK_LATER] = 0;
		}
	}
	
	// resets the number of #'s since sent
	public function resetSince() : Void {
		load();
		sharedObject.data[FIELD_VISITS_SINCE] = 0;
		unload();
	}
	
	// how many times the current simulation has ever been run (according to preferences)
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function visitsEver() : Number {
		return sharedObject.data[FIELD_VISITS_EVER];
	}
	
	// how many times the current simulation has been run since last message sent (according to preferences)
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function visitsSince() : Number {
		return sharedObject.data[FIELD_VISITS_SINCE];
	}
	
	// returns when the preferences file was created
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function getUserTime() : Number {
		return sharedObject.data.userPreferencesFileCreationTime;
	}
	
	// returns how many total times the user has run any simulation
	// NOTE: make sure preferences are loaded before calling, and unloaded sometime soon after
	public function getUserTotalSessions() : Number {
		return sharedObject.data.userTotalSessions;
	}
	
	// return number of milliseconds elapsed since ask-later was selected
	public function askLaterElapsed() : Number {
		load();
		var time : Number = sharedObject.data[FIELD_ASK_LATER];
		unload();
		return (new Date()).valueOf() - time;
	}
	
	/////////////////////////////////////////
	// for resetting preferences data
	public function onKeyDown() : Void {
		if(Key.getCode() == 119) {
			// F8 was pressed
			debug("Preferences: Manually resetting shared data\n");
			load();
			reset();
			unload();
		}
	}
}

