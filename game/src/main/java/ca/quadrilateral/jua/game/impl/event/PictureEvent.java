package ca.quadrilateral.jua.game.impl.event;

import ca.quadrilateral.jua.game.enums.EventType;
import ca.quadrilateral.jua.game.image.IGameImage;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PictureEvent extends AbstractEvent {
	private IGameImage initialImage = null;
	private IGameImage finalImage = null;
	private int initialOpacity = 100;
	private long blendDuration = 0;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendToString(super.toString())
                .append("Initial Opacity", initialOpacity)
                .append("Blend Duration", blendDuration)
                .append("Initial Image", initialImage)
                .append("Final Image", finalImage)
                .toString();
    }

	public IGameImage getInitialImage() {
		return this.initialImage;
	}

	public void setInitialImage(IGameImage initialImage) {
		this.initialImage = initialImage;
	}

	public IGameImage getFinalImage() {
		return this.finalImage;
	}

	public void setFinalImage(IGameImage finalImage) {
		this.finalImage = finalImage;
	}

	public int getInitialOpacity() {
		return this.initialOpacity;
	}

	public void setInitialOpacity(int initialOpacity) {
		this.initialOpacity = initialOpacity;
	}

	public long getBlendDuration() {
		return this.blendDuration;
	}

	public void setBlendDuration(long blendDuration) {
		this.blendDuration = blendDuration;
	}

	@Override
	public EventType getEventType() {
		return EventType.PictureEvent;
	}
}
