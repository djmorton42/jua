package ca.quadrilateral.jua.game.impl;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.quadrilateral.jua.game.GameConstants;
import ca.quadrilateral.jua.game.IGameConfiguration;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.FacingEnum;

public class GameConfiguration implements IGameConfiguration {
    private long textDelay = GameConstants.NANOS_PER_MILLISECOND * 5;
    private List<String> authors = null;
    private String adventureName = null;
    private String adventureVersion = null;
    private UUID startLevelId = null;
    private IPartyPosition initialPartyPosition = null;
    private UUID defaultBorderId = null;
    private Font gameFont = new Font("LucidaSansRegular", Font.PLAIN, 20);
    private Font boldGameFont = new Font("LucidaSansRegular", Font.BOLD, 20);
    private Boolean useGradientRender = null;

    public GameConfiguration() {
        authors = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("Adventure Name", this.adventureName)
            .append("Adventure Version", this.adventureVersion)
            .append("UUID Start Level", this.startLevelId)
            .append("Start Party Position", this.initialPartyPosition)
            .append("Auhors", this.authors)
            .append("Default Border ID", this.defaultBorderId)
            .toString();
    }

    @Override
    public boolean useGradientRender() {
        if (this.useGradientRender == null) {
            this.useGradientRender = Boolean.parseBoolean(System.getProperty("gradientRender", "true"));
        }
        return this.useGradientRender;
    }

    @Override
    public UUID getDefaultBorderId() {
        return this.defaultBorderId;
    }

    @Override
    public void setDefaultBorderId(UUID defaultBorderId) {
        this.defaultBorderId = defaultBorderId;
    }

    @Override
    public long getTextDelay() {
        return this.textDelay;
    }

    @Override
    public void setTextDelay(long textDelay) {
        this.textDelay = textDelay;
    }

    @Override
    public void addAuthor(String author) {
        this.authors.add(author);
    }

    @Override
    public String getAdventureName() {
        return this.adventureName;
    }

    @Override
    public String getAdventureVersion() {
        return this.adventureVersion;
    }

    @Override
    public List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public UUID getStartLevelId() {
        return this.startLevelId;
    }

    @Override
    public IPartyPosition getStartingPartyPosition() {
        return this.initialPartyPosition;
    }

    @Override
    public void setAdventureName(String adventureName) {
        this.adventureName = adventureName;
    }

    @Override
    public void setAdventureVersion(String adventureVersion) {
        this.adventureVersion = adventureVersion;
    }

    @Override
    public void setInitialPartyPosition(int x, int y, FacingEnum facing) {
        this.initialPartyPosition = new PartyPosition(facing, x, y);
    }

    @Override
    public void setStartLevelId(UUID startLevelId) {
        this.startLevelId = startLevelId;
    }

    @Override
    public Font getGameFont() {
        return this.gameFont;
    }

    @Override
    public Font getBoldGameFont() {
        return this.boldGameFont;
    }

}
