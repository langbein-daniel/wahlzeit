package org.wahlzeit.model.landscape;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class LandscapeIdTest {
    @Test
    public void testUniqueId(){
        List<Integer> allIds = new ArrayList<>();
        TreeSet<Integer> uniqueIds = new TreeSet<>();

        for(int i = 0; i<20; i++){
            Landscape l = LandscapeManager.createLandscape("lake", TimeOfDay.GOLDEN_HOUR, Season.SPRING);
            allIds.add(l.getId());
            uniqueIds.add(l.getId());
        }

        /**
         * If some of the 20 photos have the same id, then the List allIds will contain duplicates.
         * As the TreeSet dos not contain duplicates it would thus be smaller in size and the assertion will fail
         */
        Assert.assertEquals(allIds.size(), uniqueIds.size());
    }

    @Test
    public void testRestoreId(){
        int nextId = 100;

        LandscapeId.setNextLandscapeId(nextId);
        Landscape l1 = LandscapeManager.createLandscape("desert", TimeOfDay.NIGHT, Season.SUMMER);

        Assert.assertEquals(nextId, l1.getId());
    }
}
