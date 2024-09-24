package ca.quadrilateral.jua.game.level;

import java.util.UUID;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.enums.LevelType;

public interface ILevel {
    IMap getLevelMap();
    LevelType getLevelType();
    UUID getLevelId();
    String getLevelName();
}
