package ca.quadrilateral.jua.displayengine.renderer.impl;

import ca.quadrilateral.jua.displayengine.impl.IRendererQueue;
import ca.quadrilateral.jua.displayengine.renderer.ILevelRenderer;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

public class RendererQueue extends LinkedList<ILevelRenderer> implements IRendererQueue {
    public RendererQueue() {
        super();
    }

    public RendererQueue(List<ILevelRenderer> queue) {
        this.addAll(queue);
    }

    @Override
    public void render(Graphics2D graphics) {
        for(ILevelRenderer renderer : this) {
            renderer.render(graphics, null);
        }
    }
}
