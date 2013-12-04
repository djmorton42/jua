package ca.quadrilateral.jua.game;

public interface IGameStats {
    double getWallCacheHitPercentage();
    double getBackgroundCacheHitPercentage();
    double getBorderCacheHitPercentage();

    void registerWallCacheLookup(boolean isHit);
    void registerBackgroundCacheLookup(boolean isHit);
    void registerBorderCacheLookup(boolean isHit);

    void addHandleGameTime(long gameTime);
    void addRenderTime(long renderTime);
    void addLoopTime(long renderTime);

    void addTextRenderTime(long time);
    void addImageRenderTime(long time);
    void addOptionRenderTime(long time);
    void addViewRenderTime(long time);

    void addGeneralRendererTime(int rendererIndex, long time);
}
