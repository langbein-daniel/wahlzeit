package org.wahlzeit.model;

import org.wahlzeit.contract.PatternInstance;
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
     *
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
