package ca.quadrilateral.jua.game;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameStats implements IGameStats {
    public static final long NANOS_PER_SECOND = 1000000000;

    int backgroundCacheAttempts = 0;
    int wallCacheAttempts = 0;
    int borderCacheAttempts = 0;

    int backgroundCacheHits = 0;
    int wallCacheHits = 0;
    int borderCacheHits = 0;

    long handleGameTime = 0;
    long renderTime = 0;
    long loopTime = 0;

    long textRenderTime = 0;
    long imageRenderTime = 0;
    long optionRenderTime = 0;
    long viewRenderTime = 0;

    long[] generalRendererTime = new long[10];

    public GameStats() {
        for(int i = 0; i < generalRendererTime.length; i++) {
            generalRendererTime[i] = 0;
        }
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this).append("Background Cache Hit Rate", this.getBackgroundCacheHitPercentage())
        .append("Wall Cache Hit Rate", this.getWallCacheHitPercentage())
        .append("Border Cache Hit Rate", this.getBorderCacheHitPercentage())
        .append("Handle Game Time", ((this.handleGameTime * 1.0) / NANOS_PER_SECOND))
        .append("Render Time", ((this.renderTime * 1.0) / NANOS_PER_SECOND))
        .append("Loop Time", ((this.loopTime * 1.0) / NANOS_PER_SECOND))

        .append("View Render Time", ((this.viewRenderTime * 1.0) / NANOS_PER_SECOND))
        .append("Image Render Time", ((this.imageRenderTime * 1.0) / NANOS_PER_SECOND))
        .append("Text Render Time", ((this.textRenderTime * 1.0) / NANOS_PER_SECOND))
        .append("Options Render Time", ((this.optionRenderTime * 1.0) / NANOS_PER_SECOND));

        for(int i = 0; i < generalRendererTime.length; i++) {
            builder.append("General Renderer Time " + i, ((generalRendererTime[i] * 1.0) / NANOS_PER_SECOND));
        }


        return builder.toString();
    }

    @Override
    public void addGeneralRendererTime(int rendererIndex, long time) {
        generalRendererTime[rendererIndex] += time;
    }

    @Override
    public void addTextRenderTime(long time) {
        this.textRenderTime += time;
    }

    @Override
    public void addImageRenderTime(long time) {
        this.imageRenderTime += time;
    }

    @Override
    public void addOptionRenderTime(long time) {
        this.optionRenderTime += time;
    }

    @Override
    public void addViewRenderTime(long time) {
        this.viewRenderTime += time;
    }


    @Override
    public void addLoopTime(long loopTime) {
        this.loopTime += loopTime;
    }

    @Override
    public void addHandleGameTime(long gameTime) {
        this.handleGameTime += handleGameTime;
    }

    @Override
    public void addRenderTime(long renderTime) {
        this.renderTime += renderTime;
    }

    @Override
    public double getBackgroundCacheHitPercentage() {
        if (backgroundCacheAttempts > 0) {
            return (backgroundCacheHits * 1.0) / backgroundCacheAttempts * 100;
        } else {
            return 0;
        }
    }

    @Override
    public double getBorderCacheHitPercentage() {
        if (borderCacheAttempts > 0) {
            return (borderCacheHits * 1.0) / borderCacheAttempts * 100;
        } else {
            return 0;
        }

    }

    @Override
    public double getWallCacheHitPercentage() {
        if (wallCacheAttempts > 0) {
            return (wallCacheHits * 1.0) / wallCacheAttempts * 100;
        } else {
            return 0;
        }

    }

    @Override
    public void registerBackgroundCacheLookup(boolean isHit) {
        backgroundCacheAttempts++;
        if (isHit) {
            backgroundCacheHits++;
        }
    }

    @Override
    public void registerBorderCacheLookup(boolean isHit) {
        borderCacheAttempts++;
        if (isHit) {
            borderCacheHits++;
        }
    }

    @Override
    public void registerWallCacheLookup(boolean isHit) {
        wallCacheAttempts++;
        if (isHit) {
            wallCacheHits++;
        }
    }

}
