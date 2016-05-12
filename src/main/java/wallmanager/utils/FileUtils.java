package wallmanager.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
    
	public static void exportResource(String resourceName, File outputFile) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        
        try {
            stream = FileUtils.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            
            resStreamOut = new FileOutputStream(outputFile);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } 
        catch (Exception ex) {
            throw ex;
        } 
        finally {
            stream.close();
            resStreamOut.close();
        }
    }
}
