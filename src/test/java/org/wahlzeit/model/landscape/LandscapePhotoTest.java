package org.wahlzeit.model.landscape;

import org.junit.Test;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.landscape.LandscapePhoto;
import org.wahlzeit.model.landscape.LandscapePhotoFactory;
import org.wahlzeit.model.landscape.LandscapePhotoManager;
import org.wahlzeit.model.location.Location;

import static org.junit.Assert.*;

public class LandscapePhotoTest {

    /**
     * Tests weather the two singletons PhotoManager and PhotoFactory
     * are now objects of the subclasses LandscapePhotoManager and LandscapePhotoFactory.
     */
    @Test
    public void testPhotoManagerAndPhotoFactory()  {
        /*
         * Both singletons of PhotoManager and PhotoFactory are created inside ModelMain.startUp() by the following two calls:
         */
        LandscapePhotoFactory.initialize();
        LandscapePhotoManager.initialize();

        // PhotoManager contains the singleton of LandscapePhotoManager
        assertTrue(PhotoManager.getInstance() instanceof LandscapePhotoManager);

        // PhotoManager contains the singleton of LandscapePhotoFactory
        assertTrue(PhotoFactory.getInstance() instanceof LandscapePhotoFactory);
    }

    @Test
    public void testCreatePhoto(){
        /*
         * Both singletons of PhotoManager and PhotoFactory are created inside ModelMain.startUp() by the following two calls:
         */
        LandscapePhotoFactory.initialize();
        LandscapePhotoManager.initialize();

        Photo photo = PhotoFactory.getInstance().createPhoto();

        assertTrue(photo instanceof LandscapePhoto);
    }

    @Test
    public void testDefaultLocation(){
        /*
         * Both singletons of PhotoManager and PhotoFactory are created inside ModelMain.startUp() by the following two calls:
         */
        LandscapePhotoFactory.initialize();
        LandscapePhotoManager.initialize();

        Photo photo = PhotoFactory.getInstance().createPhoto();
        Location location = photo.getLocation();

        assertEquals(Location.DEFAULT_COORDINATE, location.getCoordinate());
    }
}
