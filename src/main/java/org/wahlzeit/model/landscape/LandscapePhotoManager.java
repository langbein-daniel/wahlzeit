package org.wahlzeit.model.landscape;

import org.wahlzeit.contract.PatternInstance;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.services.SysLog;

@PatternInstance(
        patternName = "Singleton",
        participants = LandscapePhotoManager.class
)
public class LandscapePhotoManager extends PhotoManager {
    /*
     * Note on Inheritance:
     * PhotoFactory.getInstance() returns a LandscapePhotoFactory as
     * LandscapePhotoFactory.initialize() was called earlier
     */

    /**
     * @cw11 1. Get the PhotoManager/LandscapePhotoManager singleton. Depending on if PhotoManager.initialize()
     * or LandscapePhotoManager.initialize() was initially called, this will return a reference to the corresponding
     * manager singleton.
     * <p>
     * Public singleton access method.
     */
    public static synchronized PhotoManager getInstance() {
        if (instance == null) {
            SysLog.logSysInfo("setting LandscapePhotoManager");
            instance = new LandscapePhotoManager();
        }

        return instance;
    }

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    public static void initialize() {
        LandscapePhotoManager.getInstance(); // drops result due to getInstance() side-effects
    }

    public LandscapePhotoManager() {
        super();
    }
}
