package info.seravee.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ysaak on 27/01/15.
 */
public class FileUtils {

    public static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpeg", "jpg", "png", "gif", "tiff", "tif", "bmp");

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
