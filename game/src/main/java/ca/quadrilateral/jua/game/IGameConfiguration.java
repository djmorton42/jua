package ca.quadrilateral.jua.game;

import java.awt.Font;
import java.util.List;
import java.util.UUID;

import ca.quadrilateral.jua.game.enums.FacingEnum;

public interface IGameConfiguration {
    //TODO Move text delay to save game
    long getTextDelay();
    UUID getStartLevelId();
    IPartyPosition getStartingPartyPosition();
    String getAdventureName();
    String getAdventureVersion();
    List<String> getAuthors();
    UUID getDefaultBorderId();
    Font getGameFont();
    Font getBoldGameFont();

    void setDefaultBorderId(UUID defaultBorderId);
    void setTextDelay(long textDelay);
    void setStartLevelId(UUID startLevelId);
    void setInitialPartyPosition(int x, int y, FacingEnum facing);
    void setAdventureName(String adventureName);
    void setAdventureVersion(String adventureVersion);
    void addAuthor(String author);
    boolean useGradientRender();
}
