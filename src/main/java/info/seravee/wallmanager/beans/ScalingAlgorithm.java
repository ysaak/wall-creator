package info.seravee.wallmanager.beans;

/**
 * Created by ysaak on 27/01/15.
 */
public enum ScalingAlgorithm {
    CENTER,
    STRETCH,
    STRETCH_KEEP_PROPORTION_NO_CROP,
    STRETCH_KEEP_PROPORTION_WITH_CROP;
    
    public static int indexOf(ScalingAlgorithm algorithm) {
        for (int i=0; i<values().length; i++) {
            if (values()[i] == algorithm) {
                return i;
            }
        }
        
        throw new IllegalArgumentException("Unknow algorithm");
    }
}
