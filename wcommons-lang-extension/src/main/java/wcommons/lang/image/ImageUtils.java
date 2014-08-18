/**
 * 
 */
package wcommons.lang.image;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 10:58:23 AM Nov 25, 2013
 */
public class ImageUtils {

	private static final Log LOG = LogFactory.getLog(ImageUtils.class);

	/**
	 * 获取远程图片的尺寸
	 * 
	 * @param url
	 * @return
	 */
	public static int[] getRemoteImageSize(String url) {
		try {
			final BufferedImage bi = ImageIO.read(new URL(url));
			final int[] result = new int[2];
			result[0] = bi.getWidth();
			result[1] = bi.getHeight();
			return result;
		}
		catch (Exception e) {
			LOG.warn("Error to get remote image size\r\n", e);
		}
		return null;
	}

}
