package ca.quadrilateral.jua.game.border;

import ca.quadrilateral.jua.game.enums.BorderStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BorderDefinitionItem implements IBorderDefinitionItem {
    private BorderStyle borderStyle = null;
    private String filePath = null;
    private int xOffset = 0;
    private int yOffset = 0;
    private int zOrder = 0;

    public BorderDefinitionItem(BorderStyle borderStyle,
                                int xOffset,
                                int yOffset,
                                int zOrder,
                                String filePath) {
        this.borderStyle = borderStyle;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOrder = zOrder;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                        .append("Border Style", borderStyle)
                        .append("File Path", filePath)
                        .append("xOffset", xOffset)
                        .append("yOffset", yOffset)
                        .append("zOrder", zOrder)
                        .toString();
    }

    @Override
    public BorderStyle getBorderStyle() {
        return this.borderStyle;
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public int getXOffset() {
        return this.xOffset;
    }

    @Override
    public int getYOffset() {
        return this.yOffset;
    }

    @Override
    public int getZOrder() {
        return this.zOrder;
    }

    public void setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    @Override
    public int compareTo(IBorderDefinitionItem o) {
        final int otherZOrder = o.getZOrder();

        if (this.zOrder < otherZOrder) {
            return -1;
        }
        if (this.zOrder > otherZOrder) {
            return 1;
        }
        return 0;
    }

}
