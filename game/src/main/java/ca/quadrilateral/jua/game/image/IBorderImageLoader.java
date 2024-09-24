package ca.quadrilateral.jua.game.image;

import java.util.UUID;

public interface IBorderImageLoader {
    IGameImage getBorderImage(UUID borderId, String filename);
}
