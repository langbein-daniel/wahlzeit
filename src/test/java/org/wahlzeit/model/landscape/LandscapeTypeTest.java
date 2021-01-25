package org.wahlzeit.model.landscape;

import org.junit.Test;

import static org.junit.Assert.*;

public class LandscapeTypeTest {
    @Test
    public void testAsPath(){
        LandscapeType generalLandscape = LandscapeManager.getLandscapeType();

        LandscapeType mountains = LandscapeManager.getLandscapeType("mountains");
        LandscapeType alps = LandscapeManager.getLandscapeType("alps", mountains);
        LandscapeType zugspitze = LandscapeManager.getLandscapeType("zugspitze", alps);

        assertEquals("/landscape", generalLandscape.asPath().toString());
        assertEquals("/landscape/mountains", mountains.asPath().toString());
        assertEquals("/landscape/mountains/alps/zugspitze", zugspitze.asPath().toString());
    }

    @Test
    public void testIsSubtype() {
        LandscapeType generalLandscape = LandscapeManager.getLandscapeType();

        LandscapeType mountains = LandscapeManager.getLandscapeType("mountains");
        LandscapeType alps = LandscapeManager.getLandscapeType("alps", mountains);
        LandscapeType zugspitze = LandscapeManager.getLandscapeType("zugspitze", alps);


        assertTrue(alps.isSubtype(mountains));
        assertTrue(zugspitze.isSubtype(mountains));
        assertTrue(zugspitze.isSubtype(alps));

        assertFalse(mountains.isSubtype(alps));
        assertFalse(mountains.isSubtype(zugspitze));
        assertFalse(alps.isSubtype(zugspitze));
    }

    @Test
    public void testSharedValue(){
        LandscapeType t1 = LandscapeManager.getLandscapeType();
        LandscapeType t2 = LandscapeManager.getLandscapeType();

        LandscapeType t11 = LandscapeManager.getLandscapeType("subtype");
        LandscapeType t12 = LandscapeManager.getLandscapeType("subtype");

        assertSame(t1,t2);
        assertSame(t11,t12);
    }

    @Test
    public void testIsSame(){
        LandscapeType t1 = LandscapeManager.getLandscapeType("mountains");
        LandscapeType t2 = LandscapeManager.getLandscapeType("mountains", LandscapeManager.getLandscapeType());

        assertSame(t1,t2);
    }
}
