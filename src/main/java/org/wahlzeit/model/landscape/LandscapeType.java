package org.wahlzeit.model.landscape;

import org.wahlzeit.contract.AssertArgument;
import org.wahlzeit.utils.Immutable;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LandscapeType implements Immutable, Comparable<LandscapeType>{
    protected static final Path ROOT = Paths.get("/landscape");

    private final String typeName;
    private final LandscapeType superType;
//    private final TreeSet<LandscapeType> subTypes = new TreeSet<>();

    protected LandscapeType() {
        this.typeName = ROOT.getFileName().toString();
        this.superType = null;
    }

    protected LandscapeType(String typeName, LandscapeType superType) {
        this.typeName = typeName;
        this.superType = superType;
    }

    protected Landscape createInstance(TimeOfDay time, Season season){
        return new Landscape(this, time, season);
    }

    protected Landscape createInstance(int landscapeId, TimeOfDay time, Season season){
        return new Landscape(landscapeId, this, time, season);
    }


    Path asPath() {
        if(!hasSuperType()){
            return ROOT;
        }

        Path parent = getSuperType().asPath();
        return parent.resolve(typeName);
    }

    public boolean hasSuperType() {
        return superType != null;
    }

    public LandscapeType getSuperType() {
        return superType;
    }


    /**
     * Tests whether one type is a subtype of another. Any type is considered to be a subtype of itself.
     */
    public boolean isSubtype(LandscapeType superType){
        AssertArgument.notNull(superType);
        return doIsSubtype(superType);
    }

    private boolean doIsSubtype(LandscapeType superType){
        return asPath().startsWith(superType.asPath());
    }


    @Override
    public int compareTo(LandscapeType o) {
        AssertArgument.notNull(o);
        if(this==o) return 0;
        return asPath().compareTo(o.asPath());
    }

    @Override
    public String toString() {
        return asPath().toString();
    }
}
