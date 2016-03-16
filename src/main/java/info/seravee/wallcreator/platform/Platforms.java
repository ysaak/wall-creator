package info.seravee.wallcreator.platform;

/**
 * Created by ysaak on 31/01/15.
 */
public class Platforms {
	
	private static Platform platform = null;
	

    public static Platform get() {
    	if (platform == null) {
    		platform = new FakePlatform();
    	}

    	return platform;
    }
}
