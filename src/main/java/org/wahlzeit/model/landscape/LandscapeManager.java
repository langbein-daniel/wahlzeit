package org.wahlzeit.model.landscape;

import org.wahlzeit.contract.AssertArgument;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LandscapeManager {
    /**
     * Contains all Landscape objects by mapping their IDs to the Landscape objects
     */
    private static final Map<Integer, Landscape> landscapes = new HashMap<>();
    /**
     * A lot of Landscape objects will contain references to their LandscapeTypes
     * thus the latter are shared value objects.
     */
    private static final Map<Path, LandscapeType> landscapeTypes = new HashMap<>();

    /**
     * Creates a Landscape of type /landscape/"typeName"
     */
    public static Landscape createLandscape(String typeName, TimeOfDay time, Season season) {
        return createLandscape(getLandscapeType(typeName), time, season);
    }

    /**
     * Creates a Landscape of type "superType"/"typeName"
     */
    public static Landscape createLandscape(String typeName, LandscapeType superType, TimeOfDay time, Season season) {
        return createLandscape(getLandscapeType(typeName, superType), time, season);
    }

    /**
     * Creates a Landscape of type "lt"
     */
    public static Landscape createLandscape(LandscapeType lt, TimeOfDay time, Season season) {
        AssertArgument.notNull(lt);
        AssertArgument.notNull(time);
        AssertArgument.notNull(season);

        return doCreateLandscape(lt, time, season);
    }


    private static Landscape doCreateLandscape(LandscapeType lt, TimeOfDay time, Season season) {
        Landscape result = lt.createInstance(time, season);
        landscapes.put(result.getId(), result);
        return result;
    }

    private static Landscape doCreateLandscape(int landscapeId, LandscapeType lt, TimeOfDay time, Season season) {
        Landscape result = lt.createInstance(landscapeId, time, season);
        landscapes.put(result.getId(), result);
        return result;
    }

    /**
     * @return The top-level LandscapeType "/landscape" | The most general LandscapeType
     */
    public static LandscapeType getLandscapeType() {
        LandscapeType result = landscapeTypes.get(LandscapeType.ROOT);

        if (result == null) {
            result = new LandscapeType();
            landscapeTypes.put(result.asPath(), result);
        }

        return result;
    }

    /**
     * @return The LandscapeType /landscape/"typeName"
     */
    public static LandscapeType getLandscapeType(String typeName){
        /* typeName is checked inside next getLandscapeType */
        return getLandscapeType(typeName, getLandscapeType());
    }

    /**
     * @return The LandscapeType "superType"/"typeName"
     */
    public static LandscapeType getLandscapeType(String typeName, LandscapeType superType) {
        assertIsValidTypeName(typeName);
        AssertArgument.notNull(superType);

        Path path = superType.asPath().resolve(typeName);
        LandscapeType result = landscapeTypes.get(path);

        if (result == null) {
            result = new LandscapeType(typeName, superType);
            landscapeTypes.put(result.asPath(), result);
        }

        return result;
    }

    private static LandscapeType getLandscapeTypeFromPath(Path path) {
        if (path.equals(LandscapeType.ROOT)) {
            return getLandscapeType();
        }
        return getLandscapeType(path.getFileName().toString(), getLandscapeTypeFromPath(path.getParent()));
    }


    // === Persistence ===

    protected static Landscape fromRset(ResultSet rset) throws SQLException {
        String typeAsPath = rset.getString("landscape_type_path");
        if(typeAsPath.equals("")) return null;  // no landscape was saved

        int landscapeId = rset.getInt("landscape_id");
        TimeOfDay time = TimeOfDay.valueOf(rset.getString("landscape_time"));
        Season season = Season.valueOf(rset.getString("landscape_season"));

        LandscapeType lt = getLandscapeTypeFromPath(Paths.get(typeAsPath));
        return doCreateLandscape(landscapeId, lt, time, season);
    }

    protected static void toRset(ResultSet rset, Landscape landscape) throws SQLException {
        if(landscape==null){
            rset.updateString("landscape_type_path", "");
            rset.updateInt("landscape_id", 0);
            rset.updateString("landscape_time", "");
            rset.updateString("landscape_season", "");
            return;
        }

        rset.updateString("landscape_type_path", landscape.getType().asPath().toString());
        rset.updateInt("landscape_id", landscape.getId());
        rset.updateString("landscape_time", landscape.getTime().toString());
        rset.updateString("landscape_season", landscape.getSeason().toString());
    }

    // === Assertions ===


    private static void assertIsValidTypeName(String typeName) {
        if (typeName == null || typeName.contains("\n") || containsWhitespaces(typeName) || typeName.contains("/")) {
            throw new IllegalArgumentException("typeName must not contain line breaks, whitespaces or '/'");
        }
    }

    private static boolean containsWhitespaces(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
