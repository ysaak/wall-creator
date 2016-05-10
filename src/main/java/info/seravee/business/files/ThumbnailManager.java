package info.seravee.business.files;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

public class ThumbnailManager {
	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage getThumbnail(File file) throws Exception {
		final String fileHash = getHash(file.getAbsolutePath());

		Path thumbnail = getThumbnailPath().resolve(fileHash + ".png");
		if (Files.exists(thumbnail)) {
			
			final FileTime thumbnailMT = Files.getLastModifiedTime(thumbnail);
			final FileTime imageMT = Files.getLastModifiedTime(Paths.get(file.getAbsolutePath()));
			
			// Return thumbnail only if more recent
			if (imageMT.compareTo(thumbnailMT) <= 0) 
				return ImageIO.read(thumbnail.toFile());
		}

		return null;
	}

	/**
	 * 
	 * @param fileHash
	 * @param wallpaper
	 * @throws Exception
	 */
	public static void writeThumbnailFile(File file, BufferedImage image) throws Exception {

		Path tbPath = getThumbnailPath();
		String fileHash = getHash(file.getAbsolutePath());

		if (Files.notExists(tbPath)) {
			Files.createDirectories(tbPath);
		}

		try {
			ImageIO.write(image, "PNG", tbPath.resolve(fileHash + ".png").toFile());
		} catch (IOException e) {
			System.err.println("Error while generating thumbnail");
			e.printStackTrace();
		}
	}

	public static void purgeThumbnails() {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getThumbnailPath(), "*.{png}")) {
			for (Path entry : stream) {
				Files.deleteIfExists(entry);
			}
		} catch (IOException x) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can // only be thrown by newDirectoryStream.
			System.err.println(x);
		}
	}

	/**
	 * 
	 * @return
	 */
	private static Path getThumbnailPath() {
		
		/*
		return Platforms.get().getAppDirectory().resolve("thumbnails");
		*/
		return null;
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private static String getHash(String text) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
		byte[] digest = md.digest();

		return String.format("%064x", new java.math.BigInteger(1, digest));
	}
}
