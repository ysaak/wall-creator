package info.seravee.utils;

import info.seravee.data.ScalingAlgorithm;

import java.awt.*;

/**
 * Created by ysaak on 27/01/15.
 */
public class ImageScalerUtils {

    public static Dimension getScaledImage(ScalingAlgorithm algorithm, Dimension original, Dimension target) {

        if (algorithm == ScalingAlgorithm.CENTER) {
            return original;
        }
        //else if (algorithm == ScalingAlgorithm.STRETCH) {
        //    return target;
        //}
        else if (algorithm == ScalingAlgorithm.STRETCH_KEEP_PROPORTION_NO_CROP) {
            return getScaledDimension(
                    original,
                    getScaleFactorToFit(original, target)
            );
        }
        else if (algorithm == ScalingAlgorithm.STRETCH_KEEP_PROPORTION_WITH_CROP) {
            return getScaledDimension(
                    original,
                    getScaleFactorToFill(original, target)
            );
        }
        else {
            // Default value is stretch
            return target;
        }
    }

    private static Dimension getScaledDimension(Dimension original, double scaleFactor) {
        int scaleWidth = (int) Math.round(original.width * scaleFactor);
        int scaleHeight = (int) Math.round(original.height * scaleFactor);
        return new Dimension(scaleWidth, scaleHeight);
    }

    /**
     * Returns the scale factor for the image to fit the available area
     * @param original Original dimension
     * @param target Available dimension
     * @return Scale factor
     */
    private static double getScaleFactorToFit(Dimension original, Dimension target) {
        double dScale = 1d;

        if (original != null && target != null) {
            double dScaleWidth = getScaleFactor(original.width, target.width);
            double dScaleHeight = getScaleFactor(original.height, target.height);
            dScale = Math.min(dScaleHeight, dScaleWidth);
        }
        return dScale;
    }

    /**
     * Returns the scale factor for the image to fill the available area
     * @param original Original dimension
     * @param target Available dimension
     * @return Scale factor
     */
    private static double getScaleFactorToFill(Dimension original, Dimension target) {
        double dScale = 1d;

        if (original != null && target != null) {
            double dScaleWidth = getScaleFactor(original.width, target.width);
            double dScaleHeight = getScaleFactor(original.height, target.height);
            dScale = Math.max(dScaleHeight, dScaleWidth);
        }

        return dScale;
    }

    /**
     * Returns the scaling factor for a particular size
     * @param iMasterSize Master size
     * @param iTargetSize Target size
     * @return scaling factor
     */
    private static double getScaleFactor(int iMasterSize, int iTargetSize) {
        double dScale;
        if (iMasterSize > iTargetSize) {
            dScale = (double) iTargetSize / (double) iMasterSize;
        } else {
            dScale = (double) iTargetSize / (double) iMasterSize;
        }
        return dScale;
    }
}
