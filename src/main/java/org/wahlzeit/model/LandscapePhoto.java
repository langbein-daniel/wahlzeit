package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

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
