package ca.quadrilateral.jua.game.image;

import java.util.UUID;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ca.quadrilateral.jua.game.IGameCache;
import ca.quadrilateral.jua.game.IGameContext;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.ILevelContext;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.background.IBackgroundDefinition;
import ca.quadrilateral.jua.game.background.IBackgroundDefinitionItem;
import ca.quadrilateral.jua.game.background.IBackgroundDefinitionManager;
import ca.quadrilateral.jua.game.enums.TimeOfDay;

public class BackgroundImageLoader implements IBackgroundImageLoader {
	private static final Logger log = Logger.getLogger(BackgroundImageLoader.class);

	@Autowired @Qualifier("levelStructureCache")
	private IGameCache globalCache;

	@Autowired
	private IGameContext gameContext;

	@Autowired
	private IBackgroundDefinitionManager backgroundDefinitionManager;

	@Autowired
	private IGameStats gameStats;

	@Override
	public void cachePosition(ILevelContext levelContext, IPartyPosition partyPosition) {

	}

	//TODO Handle missing file errors
	@Override
	public IGameImage getBackgroundImage(UUID backgroundId, TimeOfDay timeOfDay) {
		final String backgroundKey = constructBackgroundKeyString(backgroundId, timeOfDay);

		if (globalCache.containsKey(backgroundKey)) {
		    gameStats.registerBackgroundCacheLookup(true);
			return globalCache.get(backgroundKey);
		} else {
		    gameStats.registerBackgroundCacheLookup(false);
			final IBackgroundDefinition backgroundDefinition = backgroundDefinitionManager.getBackgroundDefinition(backgroundId);
			final IBackgroundDefinitionItem backgroundDefinitionItem = backgroundDefinition.getBackgroundDefinitionItem(timeOfDay);
			final IGameImage gameImage = new ImageLoader().loadImage(gameContext.getBackgroundsPath() + backgroundDefinitionItem.getFilePath());
			if (gameImage == null) {
				log.error("Failed to load background image: " + gameContext.getBackgroundsPath() + backgroundDefinitionItem.getFilePath());
				System.exit(1);
			}
			globalCache.put(constructBackgroundKeyString(backgroundId, timeOfDay), gameImage);
			return gameImage;
		}
	}

	private String constructBackgroundKeyString(UUID backgroundId, TimeOfDay timeOfDay) {
		return "background-" + backgroundId + "-" + timeOfDay.getText();
	}


}
