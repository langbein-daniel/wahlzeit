package org.wahlzeit.model;

import org.wahlzeit.contract.PatternInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {PhotoFactory.class, LandscapePhotoFactory.class, Photo.class, LandscapePhoto.class}
)
public class LandscapePhoto extends Photo {
    public LandscapePhoto(){
        super();
    }
    public LandscapePhoto(PhotoId myId){
        super(myId);
    }
    public LandscapePhoto(ResultSet rset) throws SQLException{
        super(rset);
    }
}
