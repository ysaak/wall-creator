package info.seravee;

import java.awt.*;

import info.seravee.wallmanager.beans.ScalingAlgorithm;

/**
 * Created by ysaak on 02/02/15.
 */
public final class DefaultConfiguration {
    /**
     * Default image background color 
     */
    public static final Color BACKGROUND_COLOR = Color.WHITE;

    /**
     * Default scaling algorithm
     */
    public static final ScalingAlgorithm SCALING_ALGORITHM = ScalingAlgorithm.STRETCH_KEEP_PROPORTION_WITH_CROP;
    
    
    public static final int FOLDER_LIST_WIDTH = 200;
    public static final int FOLDER_LIST_HEIGHT = 300;
    
    public static final double THUMBNAIL_RATIO = 16.0 / 9.0;
    public static final int THUMBNAIL_HEIGHT = 70;
    public static final int THUMBNAIL_WIDTH = (int) Math.round(THUMBNAIL_HEIGHT * THUMBNAIL_RATIO);
    public static final int THUMBNAIL_PER_LINE = 5;
    public static final int THUMBNAIL_LINES = 5;
}
