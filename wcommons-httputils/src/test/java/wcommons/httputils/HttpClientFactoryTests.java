/**
 * 
 */
package wcommons.httputils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:26:08 AM May 7, 2014
 */
public class HttpClientFactoryTests {

	@Test
	public void testHttpContentCharset() {
		final HttpClientFactory factory = new HttpClientFactory();

		Assert.assertEquals(factory.httpContentCharset(), HttpStatics.DEFAULT_CHARSET);
	}
}
