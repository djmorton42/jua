package ca.quadrilateral.jua.game.impl;

import java.util.UUID;
import ca.quadrilateral.jua.game.IMap;
import ca.quadrilateral.jua.game.enums.LevelType;
import ca.quadrilateral.jua.game.level.ILevel;

public class DefaultLevel implements ILevel {
    private UUID levelId = null;
    private LevelType levelType = null;
    private IMap levelMap = null;
    private String levelName = null;

    @Override
    public UUID getLevelId() {
        return this.levelId;
    }
    public void setLevelId(UUID levelId) {
        this.levelId = levelId;
    }
    @Override
    public LevelType getLevelType() {
        return this.levelType;
    }
    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }
    @Override
    public IMap getLevelMap() {
        return this.levelMap;
    }
    public void setLevelMap(IMap levelMap) {
        this.levelMap = levelMap;
    }
    @Override
    public String getLevelName() {
    	return this.levelName;
    }
    public void setLevelName(String levelName) {
    	this.levelName = levelName;
    }
}
