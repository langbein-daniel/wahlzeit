package org.wahlzeit.model.landscape;

import org.wahlzeit.contract.PatternInstance;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;
import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {PhotoFactory.class, LandscapePhotoFactory.class, Photo.class, LandscapePhoto.class}
)
public class LandscapePhotoFactory extends PhotoFactory {
    /**
     * Public singleton access method.
     */
    public static synchronized PhotoFactory getInstance() {
        if (instance == null) {
            SysLog.logSysInfo("setting LandscapePhotoFactory");
            setInstance(new LandscapePhotoFactory());
        }

        return instance;
    }

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    public static void initialize() {
        LandscapePhotoFactory.getInstance(); // drops result due to getInstance() side-effects
    }

    protected LandscapePhotoFactory() {
        super();
    }

    /**
     * @methodtype factory
     */
    @Override
    public LandscapePhoto createPhoto() {
        return new LandscapePhoto();
    }

    /**
     * @cw11 2.1.2. The factory instantiates a new photo object in-code.
     */
    @Override
    public LandscapePhoto createPhoto(PhotoId id) {
        return new LandscapePhoto(id);
    }

    /**
     *
     */
    @Override
    public LandscapePhoto createPhoto(ResultSet rs) throws SQLException {
        return new LandscapePhoto(rs);
    }
}
