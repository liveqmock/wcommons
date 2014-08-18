/**
 * 
 */
package wcommons.beanutils.dao;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:34:03 AM Nov 25, 2013
 */
public class ColumnUtils {

	/**
	 * 数据库字段名 转换成 对象属性名
	 * 
	 * <pre>
	 * user_name >> userName
	 * password >> password
	 * i_phone >> iPhone
	 * q_q_qd >> qQQd
	 * </pre>
	 * 
	 * @param tableColumn
	 * @return
	 */
	public static String toObjectProperty(String tableColumn) {
		if (tableColumn == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append(tableColumn.charAt(0));

		// i_i__i_pa_i >> iI_iPaI
		for (int i = 1; i < tableColumn.length(); i++) {
			char ch = tableColumn.charAt(i);
			if (ch != '_') {
				buffer.append(ch);
			}
			else {
				if (++i > tableColumn.length() - 1) {
					buffer.append(ch);
					break;
				}
				buffer.append(Character.toUpperCase(tableColumn.charAt(i)));
			}
		}
		return buffer.toString();
	}

	/**
	 * 对象属性名 转换成 数据库字段名
	 * 
	 * <pre>
	 * userName >> user_name
	 * password >> password
	 * qQQd >> q_q_qd
	 * </pre>
	 * 
	 * @param objectProperty
	 * @return
	 */
	public static String toTableColumn(String objectProperty) {
		if (objectProperty == null) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < objectProperty.length(); i++) {
			char ch = objectProperty.charAt(i);
			if (ch >= 65 && ch <= 90) {
				buffer.append('_').append(Character.toLowerCase(ch));
			}
			else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}
}