/**
 * 
 */
package wcommons.httputils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:19:22 AM Nov 25, 2013
 */
public class HttpUtilsTests {

	@Test
	public void testGet() {
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("source", "2707485026");
//		params.put("access_token", "2.00t84uCC1X1OxCd44a77b5a9FRFhvC");
//		params.put("status", "今天");

		Collection<Header> headers = new ArrayList<Header>();

		headers.add(new BasicHeader("Content-Type", "multipart/form-data;boundary=----------"
				+ System.currentTimeMillis()));

		System.out.println(HttpUtils.postForString("https://api.weibo.com/2/statuses/update.json", params, 0, headers));
	}

	@Test
	public void testGetNotFound() {
		try {
			HttpUtils.getForString("http://m.clhk.cn");
		}
		catch (RequestException e) {
			Assert.assertEquals(e.getErrorCode(), 404);
		}
	}
}
