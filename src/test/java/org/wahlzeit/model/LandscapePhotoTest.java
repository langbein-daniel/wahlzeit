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
         * Both singletons are created inside ModelMain.startUp() by the following two calls:
         */
        LandscapePhotoFactory.initialize();
        LandscapePhotoManager.initialize();

        // PhotoManager contains the singleton of LandscapePhotoManager
        assertTrue(PhotoManager.getInstance() instanceof LandscapePhotoManager);
        assertEquals(PhotoManager.getInstance().getClass(), LandscapePhotoManager.class);
        assertNotEquals(PhotoManager.getInstance().getClass(), PhotoManager.class);

        // PhotoManager contains the singleton of LandscapePhotoFactory
        assertTrue(PhotoFactory.getInstance() instanceof LandscapePhotoFactory);
        assertEquals(PhotoFactory.getInstance().getClass(), LandscapePhotoFactory.class);
        assertNotEquals(PhotoFactory.getInstance().getClass(), PhotoFactory.class);
    }
}
