package org.wahlzeit.model.landscape;

import org.wahlzeit.contract.PatternInstance;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;

import java.sql.ResultSet;
import java.sql.SQLException;

@PatternInstance(
        patternName = "Abstract Factory",
        participants = {PhotoFactory.class, LandscapePhotoFactory.class, Photo.class, LandscapePhoto.class}
)
public class LandscapePhoto extends Photo {
    protected Landscape landscape;

    public LandscapePhoto() {
        super();
    }

    public LandscapePhoto(PhotoId myId) {
        super(myId);
    }

    public LandscapePhoto(ResultSet rset) throws SQLException {
        super(rset);
    }


    public Landscape getLandscape() {
        return landscape;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
        incWriteCount();
    }

    // === persistence ===

    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        this.landscape = LandscapeManager.fromRset(rset);
    }

    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        LandscapeManager.toRset(rset, landscape);
    }
}