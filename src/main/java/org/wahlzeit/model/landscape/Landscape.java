package org.wahlzeit.model.landscape;

import org.wahlzeit.utils.Immutable;

/**
 * Types of Landscapes:
 * <p>
 * Mountains,
 * Desert,
 * Lake,
 * Ocean,
 * River,
 * Grassland,
 * Forest,
 * Rainforest,
 * Polar,
 * Farmland,
 * City
 * <p>
 * Attributes of a Landscape photo:
 * <p>
 * Time of day
 * Season
 */
public class Landscape implements Immutable {
    private final LandscapeType type;
    private final int id;

    private final TimeOfDay time;
    private final Season season;


    protected Landscape(LandscapeType type, TimeOfDay time, Season season) {
        this(LandscapeId.getUniqueId(), type, time, season);
    }

    protected Landscape(int landscapeId, LandscapeType type, TimeOfDay time, Season season) {
        this.id = landscapeId;
        this.type = type;

        this.time = time;
        this.season = season;
    }

    public LandscapeType getType() {
        return type;
    }

    public int getId() {
        return this.id;
    }

    public TimeOfDay getTime() {
        return time;
    }

    public Season getSeason() {
        return season;
    }

    @Override
    public String toString() {
        return "Landscape{" +
                "type=" + type +
                ", id=" + id +
                ", time=" + time +
                ", season=" + season +
                '}';
    }
}
