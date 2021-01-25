package org.wahlzeit.model.landscape;

import java.util.concurrent.atomic.AtomicInteger;

public class LandscapeId {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Returns the next unique landscape id
     */
    public static int getUniqueId() {
        return nextId.getAndIncrement();
    }

    /**
     * Returns the smallest id that is not yet in use
     */
    public static int getNextLandscapeId() {
        return nextId.get();
    }

    /**
     * Restores the smallest id that can be returned by getUniqueId()
     */
    public static void setNextLandscapeId(int id) {
        nextId.set(Math.max(getNextLandscapeId(), id));
    }
}
