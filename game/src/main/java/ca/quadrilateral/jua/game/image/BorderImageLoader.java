package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ca.quadrilateral.jua.game.IGameCache;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStats;

public class BorderImageLoader implements IBorderImageLoader {
    //@Autowired @Qualifier("gameAssetCache")
    @Autowired @Qualifier("levelStructureCache")
    private IGameCache cache;

    @Autowired
    private IGameContext gameContext;

    @Autowired
    private IGameStats gameStats;

    @Override
    public IGameImage getBorderImage(UUID borderId, String filename) {
        final String key = constructBorderKeyString(borderId, filename);

        if (cache.containsKey(key)) {
            gameStats.registerBorderCacheLookup(true);
            return cache.get(key);
        } else {
            gameStats.registerBorderCacheLookup(false);
            final IGameImage gameImage = new ImageLoader().loadImage(gameContext.getBordersPath() + filename);
            cache.put(key, gameImage);
            return gameImage;
        }
    }

    private String constructBorderKeyString(UUID borderId, String filename) {
        return "border-" + borderId + "-" + filename;
    }

}
