package ca.quadrilateral.jua.game;

import java.util.UUID;
import ca.quadrilateral.jua.game.enums.FacingEnum;

public interface IMapCell {
    IWall getWall(FacingEnum facing);
    UUID getBackgroundDefinitionId();
    void setBackgroundDefinitionId(UUID backgroundDefinitionId);
    int getCellX();
    int getCellY();
    IEvent getEventChain();
    void setEventChain(IEvent event);
}
