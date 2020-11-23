package org.wahlzeit.model;

import org.junit.Test;

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
}
