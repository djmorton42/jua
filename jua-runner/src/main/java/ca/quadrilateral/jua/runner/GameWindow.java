package ca.quadrilateral.jua.runner;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.quadrilateral.jua.displayengine.IFirstPersonDisplayEngine;
import ca.quadrilateral.jua.displayengine.impl.IRendererQueue;
import ca.quadrilateral.jua.game.IGameStateMachine;
import ca.quadrilateral.jua.game.IGameStats;
import ca.quadrilateral.jua.game.IImageRenderer;
import ca.quadrilateral.jua.game.IOptionRenderer;
import ca.quadrilateral.jua.game.ITextRenderer;
import ca.quadrilateral.jua.game.enums.GameMode;

public class GameWindow extends JFrame implements IGameWindow {
    private static final long serialVersionUID = 1L;

    @Autowired @Qualifier("displayEngine3DView")
    private IFirstPersonDisplayEngine engine3DView;

    @Autowired @Qualifier("displayEngineAreaView")
    private IFirstPersonDisplayEngine engineAreaView;

    @Autowired
    private ITextRenderer textRenderer;

    @Autowired
    private IImageRenderer imageRenderer;

    @Autowired
    private IGameStats gameStats;

    @Autowired
    private IOptionRenderer optionRenderer;

    @Autowired
    private IGameStateMachine gameStateMachine;

    @Autowired @Qualifier(value="characterInfoViewRendererQueue")
    private IRendererQueue characterInfoViewRendererQueue;

    @Autowired @Qualifier(value="characterInventoryViewRenderQueue")
    private IRendererQueue characterInventoryViewRendererQueue;

    @Autowired
    public GameWindow(KeyListener keyHandler) {
        this.addKeyListener(keyHandler);
    }

    @Override
    public void render(Graphics2D graphics) {
        if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInfoView)) {
            characterInfoViewRendererQueue.render(graphics);
            optionRenderer.render(graphics);
        } else if (gameStateMachine.getCurrentGameMode().equals(GameMode.CharacterInventoryView)) {
            characterInventoryViewRendererQueue.render(graphics);
            optionRenderer.render(graphics);
        } else {
            long textRenderTicks = 0;
            long imageRenderTicks = 0;
            long optionRenderTicks = 0;
            long viewRenderTicks = 0;

            viewRenderTicks = System.nanoTime();
            engine3DView.renderPartyPosition(graphics);
            gameStats.addViewRenderTime(System.nanoTime() - viewRenderTicks);

            textRenderTicks = System.nanoTime();
            textRenderer.render(graphics);
            gameStats.addTextRenderTime(System.nanoTime() - textRenderTicks);

            imageRenderTicks = System.nanoTime();
            imageRenderer.render(graphics);
            gameStats.addImageRenderTime(System.nanoTime() - imageRenderTicks);

            optionRenderTicks = System.nanoTime();
            optionRenderer.render(graphics);
            gameStats.addOptionRenderTime(System.nanoTime() - optionRenderTicks);
        }
    }
}
