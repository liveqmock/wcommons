/**
 * 
 */
package wcommons.httputils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import wcommons.httputils.RequestUtils;

import com.alibaba.fastjson.JSON;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:19:26 AM Nov 25, 2013
 */
public class RequestUtilsTests {

	@Test
	public void testInitBeanByParams() {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "342425");
		params.put("userName", "waynewang");
		params.put("age", "25");
		params.put("birthday", "1986-05-30");
		params.put("ids", new String[] { "1", "2", "4" });
		params.put("interests", new String[] { "nba", "war3" });
		params.put("createTime", "2013-08-08 08:08:08");
		params.put("T", "123456789");
		params.put("group.name", "admin");
		params.put("c", "20120808");
		params.put("money", "287");

		final HttpServletRequest req = new DefaultHttpServletRequest(params);

		final User user = RequestUtils.createBeanByParams(req, User.class);

		System.out.println(JSON.toJSONString(user));
	}
}
