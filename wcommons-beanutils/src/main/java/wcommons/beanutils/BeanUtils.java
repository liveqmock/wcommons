/**
 * 
 */
package wcommons.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.util.ReflectionUtils;

import wcommons.beanutils.dao.ColumnUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:33:59 AM Nov 25, 2013
 */
public class BeanUtils {
	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.不进行类型转换
	 * 2.拷贝source中所有的属性
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 */
	public static void copyProperties(Object target, Object source) {
		copyProperties(target, source, null);
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.不进行类型转换
	 * 2.忽略properties中的属性
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 * @param properties 忽略的属性名
	 */
	public static void copyProperties(Object target, Object source, String[] properties) {
		copyProperties(target, source, properties, true);
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.不进行类型转换
	 * 2.
	 * 	a.ignore=true,忽略properties中包含的属性
	 *  b.ignore=false,拷贝properties中包含的属性
	 * </pre>
	 * 
	 * @param dest 目标对象
	 * @param source
	 * @param properties 属性名数组
	 * @param ignore
	 */
	public static void copyProperties(Object target, Object source, String[] properties, boolean ignore) {
		if (source == null || target == null) {
			return;
		}
		PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(target.getClass());

		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() == null || (ignore && ArrayUtils.contains(properties, targetPd.getName()))
					|| (!ignore && !ArrayUtils.contains(properties, targetPd.getName()))) {
				continue;
			}
			PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(),
					targetPd.getName());
			if (sourcePd != null && sourcePd.getReadMethod() != null) {
				try {
					Method readMethod = sourcePd.getReadMethod();
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object value = readMethod.invoke(source);
					Method writeMethod = targetPd.getWriteMethod();
					if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
						writeMethod.setAccessible(true);
					}
					writeMethod.invoke(target, value);
				}
				catch (Throwable ex) {
					throw new FatalBeanException("Could not copy properties from source to target", ex);
				}
			}
		}
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.不进行类型转换
	 * 2.
	 * 	a.ignore=true,忽略properties中包含的属性
	 *  b.ignore=false,拷贝properties中包含的属性
	 * </pre>
	 * 
	 * @param target
	 * @param source
	 * @param properties
	 * @param ignore true-忽略properties中的属性; false-仅拷贝properties中的属性
	 */
	public static void copyProperties(Map<String, Object> target, Object source, String[] properties, boolean ignore) {
		if (source == null) {
			return;
		}
		PropertyDescriptor[] sourcePds = org.springframework.beans.BeanUtils.getPropertyDescriptors(source.getClass());

		for (PropertyDescriptor sourcePd : sourcePds) {
			if (sourcePd.getReadMethod() == null || (ignore && ArrayUtils.contains(properties, sourcePd.getName()))
					|| (!ignore && !ArrayUtils.contains(properties, sourcePd.getName()))) {
				continue;
			}
			try {
				Method readMethod = sourcePd.getReadMethod();
				if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
					readMethod.setAccessible(true);
				}
				Object value = readMethod.invoke(source);
				target.put(sourcePd.getName(), value);
			}
			catch (Throwable ex) {
				throw new FatalBeanException("Could not copy properties from source to target", ex);
			}
		}
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.拷贝properties中包含的属性
	 * 2.不进行类型转换
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 * @param properties 忽略的属性名
	 */
	public static void copyProperties(Map<String, Object> target, Object source, String[] properties) {
		copyProperties(target, source, properties, true);
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.拷贝source中的所有属性
	 * 2.不进行类型转换
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 */
	public static void copyProperties(Map<String, Object> target, Object source) {
		copyProperties(target, source, null);
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.source中不存在的属性则跳过(不拷贝)
	 * 2.不进行类型转换
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 * @param properties
	 * @param ignore true-忽略properties中的属性; false-仅拷贝properties中的属性
	 */
	public static void copyProperties(Object target, Map<String, Object> source, String[] properties, boolean ignore) {
		if (source == null || source.isEmpty()) {
			return;
		}
		PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(target.getClass());

		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() == null || (ignore && ArrayUtils.contains(properties, targetPd.getName()))
					|| (!ignore && !ArrayUtils.contains(properties, targetPd.getName()))) {
				continue;
			}
			if (!source.containsKey(targetPd.getName())) {
				continue;
			}
			try {
				Object value = source.get(targetPd.getName());
				Method writeMethod = targetPd.getWriteMethod();
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				writeMethod.invoke(target, value);
			}
			catch (Throwable ex) {
				throw new FatalBeanException("Could not copy properties from source to target", ex);
			}
		}
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.忽略properties中的属性
	 * 2.不进行类型转换
	 * </pre>
	 * 
	 * @param target 目标对象
	 * @param source
	 * @param properties 忽略的属性名
	 */
	public static void copyProperties(Object target, Map<String, Object> source, String[] properties) {
		copyProperties(target, source, properties, true);
	}

	/**
	 * 值拷贝
	 * 
	 * <pre>
	 * 1.拷贝source中的所有属性
	 * 2.不进行类型转换
	 * </pre>
	 * 
	 * @param target
	 * @param source
	 */
	public static void copyProperties(Object target, Map<String, Object> source) {
		copyProperties(target, source, null);
	}

	/**
	 * 把指定字段的值拷贝至bean对象中
	 * 
	 * @param dest
	 * @param rs
	 * @param columns <property's columnName,ResultSet's columnName>
	 * @param properties
	 * @param ignore
	 */
	public static void copyResultSetToBeanByColumn(final Object dest, final ResultSet rs,
			final Map<String, String> columns, final String[] properties, final boolean ignore) {
		if (columns == null || columns.isEmpty()) {
			return;
		}
		Class<?> propertyType;
		String propertyName, rowSetColumnName, setMethodName, startWith;
		Object propertyValue;
		Method setMethod;
		Field field;
		try {
			final boolean emptyProperties = ArrayUtils.isEmpty(properties);
			final Map<String, Map<String, String>> innerColumns = new HashMap<String, Map<String, String>>();
			for (String propertyColumnName : columns.keySet()) {
				int index = propertyColumnName.indexOf(".");
				// 当前对象内部对象的属性
				if (index >= 0) {
					propertyName = propertyColumnName.substring(0, index);
					if (!innerColumns.containsKey(propertyName)) {
						innerColumns.put(propertyName, new HashMap<String, String>());
					}
					innerColumns.get(propertyName).put(propertyColumnName.substring(index + 1),
							columns.get(propertyColumnName));
					continue;
				}

				propertyName = ColumnUtils.toObjectProperty(propertyColumnName);

				if (emptyProperties && !ignore) {
					continue;
				}
				rowSetColumnName = columns.get(propertyColumnName);
				if (!emptyProperties) {
					int ix = rowSetColumnName.indexOf(".");
					if (ix < 0) {
						startWith = StringUtils.EMPTY;
					}
					else {
						startWith = rowSetColumnName.substring(0, ix + 1);
					}
					boolean ignoreProperty = false;
					for (String property : properties) {
						if ((ignore && (startWith + propertyName).equals(property))
								|| (!ignore && !(startWith + propertyName).equals(property))) {
							ignoreProperty = true;
							break;
						}
					}
					if (ignoreProperty) {
						continue;
					}
				}

				field = ReflectionUtils.findField(dest.getClass(), propertyName);
				if (field == null) {
					continue;
				}

				propertyType = field.getType();

				if (propertyType.getName().equals("java.lang.String")) {
					propertyValue = rs.getString(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.util.Date")) {
					propertyValue = rs.getDate(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.sql.Timestamp")) {
					propertyValue = rs.getTimestamp(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Byte")) {
					propertyValue = rs.getByte(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Short")) {
					propertyValue = rs.getShort(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Integer")) {
					propertyValue = rs.getInt(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Long")) {
					propertyValue = rs.getLong(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Float")) {
					propertyValue = rs.getFloat(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Double")) {
					propertyValue = rs.getDouble(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Boolean")) {
					propertyValue = rs.getBoolean(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.math.BigDecimal")) {
					propertyValue = rs.getBigDecimal(rowSetColumnName);
				}
				else {
					propertyValue = rs.getObject(rowSetColumnName);
				}

				setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

				setMethod = dest.getClass().getDeclaredMethod(setMethodName, propertyType);
				if (setMethod == null) {
					continue;
				}

				setMethod.invoke(dest, propertyValue);
			}

			// 填充内部对象的属性
			if (!innerColumns.isEmpty()) {
				for (String column : innerColumns.keySet()) {
					propertyType = dest.getClass().getDeclaredField(column).getType();
					// 需要提供默认的构造方法 ，标准的POJO
					propertyValue = Class.forName(propertyType.getName()).newInstance();

					setMethodName = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);

					setMethod = dest.getClass().getDeclaredMethod(setMethodName, propertyType);
					if (setMethod == null) {
						continue;
					}
					setMethod.invoke(dest, propertyValue);
					//
					copyResultSetToBeanByColumn(propertyValue, rs, innerColumns.get(column), properties, ignore);
				}
			}
		}
		catch (Exception e) {
			throw new IllegalOperateBeanPropertyException(e);
		}
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 * @param properties 属性名数组
	 * @param ignore true-忽略properties中的属性; false-仅拷贝properties中的属性
	 */
	public static void copyResultSetToBean(final Object dest, final ResultSet rs, final String[] properties,
			final boolean ignore) {
		try {
			final ResultSetMetaData rsmd = rs.getMetaData();
			final int colNum = rsmd.getColumnCount();
			final Map<String, String> columns = new HashMap<String, String>();
			for (int i = 1; i <= colNum; i++) {
				columns.put(rsmd.getColumnLabel(i), rsmd.getColumnLabel(i));
			}
			copyResultSetToBeanByColumn(dest, rs, columns, properties, ignore);
		}
		catch (Exception e) {
			throw new IllegalOperateBeanPropertyException(e);
		}
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 * @param properties 忽略的属性名数组
	 */
	public static void copyResultSetToBean(final Object dest, final ResultSet rs, final String[] properties) {
		copyResultSetToBean(dest, rs, properties, true);
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 */
	public static void copyResultSetToBean(final Object dest, final ResultSet rs) {
		copyResultSetToBean(dest, rs, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	/**
	 * 把指定字段的值拷贝至bean对象中
	 * 
	 * @param dest
	 * @param rs
	 * @param columns <property's columnName,RowSet's columnName>
	 * @param properties
	 * @param ignore
	 */
	public static void copySqlRowSetToBeanByColumn(final Object dest, final SqlRowSet rs,
			final Map<String, String> columns, final String[] properties, final boolean ignore) {
		if (columns == null || columns.isEmpty()) {
			return;
		}
		Class<?> propertyType;
		String propertyName, rowSetColumnName, setMethodName, startWith;
		Object propertyValue;
		Method setMethod;
		Field field;
		try {
			final boolean emptyProperties = ArrayUtils.isEmpty(properties);
			final Map<String, Map<String, String>> innerColumns = new HashMap<String, Map<String, String>>();
			for (String propertyColumnName : columns.keySet()) {
				int index = propertyColumnName.indexOf(".");
				// 当前对象内部对象的属性
				if (index >= 0) {
					propertyName = propertyColumnName.substring(0, index);
					if (!innerColumns.containsKey(propertyName)) {
						innerColumns.put(propertyName, new HashMap<String, String>());
					}
					innerColumns.get(propertyName).put(propertyColumnName.substring(index + 1),
							columns.get(propertyColumnName));
					continue;
				}

				propertyName = ColumnUtils.toObjectProperty(propertyColumnName);

				if (emptyProperties && !ignore) {
					continue;
				}
				rowSetColumnName = columns.get(propertyColumnName);
				if (!emptyProperties) {
					int ix = rowSetColumnName.indexOf(".");
					if (ix < 0) {
						startWith = StringUtils.EMPTY;
					}
					else {
						startWith = rowSetColumnName.substring(0, ix + 1);
					}
					boolean ignoreProperty = false;
					for (String property : properties) {
						if ((ignore && (startWith + propertyName).equals(property))
								|| (!ignore && !(startWith + propertyName).equals(property))) {
							ignoreProperty = true;
							break;
						}
					}
					if (ignoreProperty) {
						continue;
					}
				}

				field = ReflectionUtils.findField(dest.getClass(), propertyName);
				if (field == null) {
					continue;
				}

				propertyType = field.getType();

				if (propertyType.getName().equals("java.lang.String")) {
					propertyValue = rs.getString(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.util.Date")) {
					propertyValue = rs.getDate(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.sql.Timestamp")) {
					propertyValue = rs.getTimestamp(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Byte")) {
					propertyValue = rs.getByte(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Short")) {
					propertyValue = rs.getShort(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Integer")) {
					propertyValue = rs.getInt(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Long")) {
					propertyValue = rs.getLong(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Float")) {
					propertyValue = rs.getFloat(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Double")) {
					propertyValue = rs.getDouble(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.lang.Boolean")) {
					propertyValue = rs.getBoolean(rowSetColumnName);
				}
				else if (propertyType.getName().equals("java.math.BigDecimal")) {
					propertyValue = rs.getBigDecimal(rowSetColumnName);
				}
				else {
					propertyValue = rs.getObject(rowSetColumnName);
				}

				setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

				setMethod = dest.getClass().getDeclaredMethod(setMethodName, propertyType);
				if (setMethod == null) {
					continue;
				}

				setMethod.invoke(dest, propertyValue);
			}

			// 填充内部对象的属性
			if (!innerColumns.isEmpty()) {
				for (String column : innerColumns.keySet()) {
					propertyType = dest.getClass().getDeclaredField(column).getType();
					// 需要提供默认的构造方法 ，标准的POJO
					propertyValue = Class.forName(propertyType.getName()).newInstance();

					setMethodName = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);

					setMethod = dest.getClass().getDeclaredMethod(setMethodName, propertyType);
					if (setMethod == null) {
						continue;
					}
					setMethod.invoke(dest, propertyValue);
					//
					copySqlRowSetToBeanByColumn(propertyValue, rs, innerColumns.get(column), properties, ignore);
				}
			}
		}
		catch (Exception e) {
			throw new IllegalOperateBeanPropertyException(e);
		}
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 * @param properties 属性名数组
	 * @param ignore true-忽略properties中的属性; false-仅拷贝properties中的属性
	 */
	public static void copySqlRowSetToBean(final Object dest, final SqlRowSet rs, final String[] properties,
			final boolean ignore) {
		final SqlRowSetMetaData rsmd = rs.getMetaData();
		final int colNum = rsmd.getColumnCount();
		final Map<String, String> columns = new HashMap<String, String>();
		for (int i = 1; i <= colNum; i++) {
			columns.put(rsmd.getColumnLabel(i), rsmd.getColumnLabel(i));
		}
		copySqlRowSetToBeanByColumn(dest, rs, columns, properties, ignore);
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 * @param properties 忽略的属性名数组
	 */
	public static void copySqlRowSetToBean(final Object dest, final SqlRowSet rs, final String[] properties) {
		copySqlRowSetToBean(dest, rs, properties, true);
	}

	/**
	 * 将数据集游标所在位置的行数据复制到以驼峰风格对应的JavaBean对象中
	 * 
	 * @param dest 目标JavaBean对象
	 * @param rs 行数据，调用前需要将游标移到需要复制的行数据处
	 */
	public static void copySqlRowSetToBean(final Object dest, final SqlRowSet rs) {
		copySqlRowSetToBean(dest, rs, ArrayUtils.EMPTY_STRING_ARRAY);
	}

	/**
	 * @author Wayne.Wang<5waynewang@gmail.com>
	 * @version 11:52:22 AM Dec 19, 2012
	 */
	private static class IllegalOperateBeanPropertyException extends RuntimeException {

		private static final long serialVersionUID = -1386582009017456751L;

		public IllegalOperateBeanPropertyException(Exception e) {
			super(e);
		}
	}
}