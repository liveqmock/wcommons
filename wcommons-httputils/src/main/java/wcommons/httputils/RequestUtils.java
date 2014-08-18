/**
 * 
 */
package wcommons.httputils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wcommons.lang.ArrayUtils;
import wcommons.lang.DateUtils;
import wcommons.lang.ExceptionUtils;
import wcommons.lang.NumberUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:04:38 AM Nov 25, 2013
 */
public class RequestUtils {

	private static final Log LOG = LogFactory.getLog(RequestUtils.class);

	/**
	 * 根据Request中的参数值创建Bean对象
	 * 
	 * <pre>
	 * clazz必须提供可访问的默认构造方法，通过调用对象的set方法设置其属性值
	 * </pre>
	 * 
	 * @param req
	 * @param clazz
	 * @return
	 */
	public static <T> T createBeanByParams(final HttpServletRequest req, final Class<T> clazz) {
		// clazz不能为null
		ExceptionUtils.throwIllegalArgumentExceptionIfNull(clazz, "arg1[clazz] is required");

		T bean;
		try {
			bean = clazz.newInstance();
		}
		catch (Exception e) {
			// 必选一提供默认构造
			throw new IllegalArgumentException(
					"please check the arg1[clazz], it must provide assignable default constructor");
		}

		return initBeanByParams(req, bean);
	}

	private static <T> void initBeanProperty(final T bean, final String propertyName,
			final String[] values, final String value) {
		final Class<?> beanClass = bean.getClass();
		final int index = propertyName.indexOf(".");
		if (index != -1) {
			final String childPropertyName = propertyName.substring(0, index);
			final String readMethodName = "get"
					+ Character.toUpperCase(childPropertyName.charAt(0))
					+ childPropertyName.substring(1);
			final String writeMethodName = "set"
					+ Character.toUpperCase(childPropertyName.charAt(0))
					+ childPropertyName.substring(1);
			final PropertyDescriptor pd;
			try {
				pd = new PropertyDescriptor(childPropertyName, beanClass, readMethodName,
						writeMethodName);
			}
			catch (Exception e) {
				LOG.warn("can not create PropertyDescriptor(" + childPropertyName + ", "
						+ beanClass.getName() + ", " + readMethodName + ", " + writeMethodName
						+ "), \r\n", e);
				// ignore
				return;
			}
			Object childBean;
			try {
				childBean = pd.getReadMethod().invoke(bean);
				if (childBean == null) {
					childBean = pd.getPropertyType().newInstance();
					pd.getWriteMethod().invoke(bean, childBean);
				}
			}
			catch (Exception e) {
				LOG.error("error to invoke " + beanClass.getName() + "." + readMethodName + " or ."
						+ writeMethodName + ", \r\n", e);
				return;
			}
			// 递归初始化
			if (childBean != null) {
				initBeanProperty(childBean, propertyName.substring(index + 1), values, value);
			}
			return;
		}

		final String readMethodName = "get" + Character.toUpperCase(propertyName.charAt(0))
				+ propertyName.substring(1);
		final String writeMethodName = "set" + Character.toUpperCase(propertyName.charAt(0))
				+ propertyName.substring(1);
		final PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, beanClass, readMethodName, writeMethodName);
		}
		catch (Exception e) {
			LOG.warn(
					"can not create PropertyDescriptor(" + propertyName + ", "
							+ beanClass.getName() + ", " + readMethodName + ", " + writeMethodName
							+ "), \r\n", e);
			// ignore
			return;
		}
		final Class<?> propertyType = pd.getPropertyType();
		final Method writeMethod = pd.getWriteMethod();
		if (writeMethod == null) {
			return;
		}
		if (propertyType == String[].class) {
			invokeWriteMethod(writeMethod, bean, values);
		}
		else if (propertyType == Byte[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toByteObjectArray(values));
		}
		else if (propertyType == Short[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toShortObjectArray(values));
		}
		else if (propertyType == Integer[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toIntegerObjectArray(values));
		}
		else if (propertyType == Long[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toLongObjectArray(values));
		}
		else if (propertyType == Float[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toFloatObjectArray(values));
		}
		else if (propertyType == Double[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toDoubleObjectArray(values));
		}
		else if (propertyType == String[].class) {
			invokeWriteMethod(writeMethod, bean, values);
		}
		else if (propertyType == Character[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toCharacterObjectArray(values));
		}
		else if (propertyType == byte[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toByteArray(values));
		}
		else if (propertyType == short[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toShortArray(values));
		}
		else if (propertyType == int[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toIntArray(values));
		}
		else if (propertyType == long[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toLongArray(values));
		}
		else if (propertyType == float[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toFloatArray(values));
		}
		else if (propertyType == double[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toDoubleArray(values));
		}
		else if (propertyType == char[].class) {
			invokeWriteMethod(writeMethod, bean, ArrayUtils.toCharArray(values));
		}
		else if (propertyType == String.class) {
			invokeWriteMethod(writeMethod, bean, value);
		}
		else if (propertyType == Byte.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toByteObject(value));
		}
		else if (propertyType == Short.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toShortObject(value));
		}
		else if (propertyType == Integer.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toIntegerObject(value));
		}
		else if (propertyType == Long.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toLongObject(value));
		}
		else if (propertyType == Float.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toFloatObject(value));
		}
		else if (propertyType == Double.class) {
			invokeWriteMethod(writeMethod, bean, NumberUtils.toDoubleObject(value));
		}
		else if (propertyType == Character.class) {
			if (value == null) {
				invokeWriteMethod(writeMethod, bean, null);
			}
			else if (value.length() != 1) {
				LOG.error("invalid param of " + propertyName + ", can not convert " + value
						+ " to " + Character.class.getName());
			}
			else {
				invokeWriteMethod(writeMethod, bean, Character.valueOf(value.charAt(0)));
			}
		}
		else if (propertyType == byte.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toByte(value, (byte) 0));
		}
		else if (propertyType == short.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toShort(value, (short) 0));
		}
		else if (propertyType == int.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toInt(value, 0));
		}
		else if (propertyType == long.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toLong(value, 0));
		}
		else if (propertyType == float.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toFloat(value, 0));
		}
		else if (propertyType == double.class) {
			invokeWriteMethod(writeMethod, bean,
					org.apache.commons.lang3.math.NumberUtils.toDouble(value, 0));
		}
		else if (propertyType == char.class) {
			if (value == null) {
				LOG.error("invalid param of " + propertyName + ", can not convert null to char");
			}
			else if (value.length() != 1) {
				LOG.error("invalid param of " + propertyName + ", can not convert " + value
						+ " to char");
			}
			else {
				invokeWriteMethod(writeMethod, bean, Character.valueOf(value.charAt(0)));
			}
		}
		else if (propertyType == java.sql.Date.class) {
			if (StringUtils.isBlank(value)) {
				invokeWriteMethod(writeMethod, bean, null);
			}
			else {
				final java.util.Date date = DateUtils.parseDate(value);
				if (date == null) {
					invokeWriteMethod(writeMethod, bean, null);
				}
				else {
					invokeWriteMethod(writeMethod, bean, new java.sql.Date(date.getTime()));
				}
			}
		}
		else if (propertyType == java.util.Date.class) {
			if (StringUtils.isBlank(value)) {
				invokeWriteMethod(writeMethod, bean, null);
			}
			else {
				invokeWriteMethod(writeMethod, bean, DateUtils.parseDate(value));
			}
		}
		else if (propertyType == java.sql.Timestamp.class) {
			if (StringUtils.isBlank(value)) {
				invokeWriteMethod(writeMethod, bean, null);
			}
			else {
				final java.util.Date date = DateUtils.parseDate(value);
				if (date == null) {
					invokeWriteMethod(writeMethod, bean, null);
				}
				else {
					invokeWriteMethod(writeMethod, bean, new java.sql.Timestamp(date.getTime()));
				}
			}
		}
		else if (propertyType == java.util.Calendar.class) {
			if (StringUtils.isBlank(value)) {
				invokeWriteMethod(writeMethod, bean, null);
			}
			else {
				final java.util.Date date = DateUtils.parseDate(value);
				if (date == null) {
					invokeWriteMethod(writeMethod, bean, null);
				}
				else {
					final Calendar c = java.util.Calendar.getInstance();
					c.setTime(date);
					invokeWriteMethod(writeMethod, bean, c);
				}
			}
		}
	}

	/**
	 * 根据Request中的参数值初始化Bean对象
	 * 
	 * <pre>
	 * 通过调用对象的set方法设置其属性值
	 * </pre>
	 * 
	 * @param req
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initBeanByParams(final HttpServletRequest req, final T bean) {
		// bean不能为null
		ExceptionUtils.throwIllegalArgumentExceptionIfNull(bean, "arg1[bean] is required");

		final Enumeration<String> names = req.getParameterNames();

		while (names.hasMoreElements()) {
			final String propertyName = names.nextElement();
			final String[] values = req.getParameterValues(propertyName);
			final String value = req.getParameter(propertyName);
			initBeanProperty(bean, propertyName, values, value);
		}

		return bean;
	}

	private static <T> void invokeWriteMethod(Method method, T bean, Object value) {
		try {
			method.invoke(bean, value);
		}
		catch (Exception e) {
			// ignore
			LOG.warn("error to call " + bean.getClass().getName() + "." + method.getName() + "("
					+ value + "), \r\n", e);
		}
	}

	/**
	 * 获取请求IP
	 * 
	 * @param req
	 * @return
	 */
	public static String getRemoteIp(final HttpServletRequest req) {
		final String remoteIP = req.getHeader("X-Real-IP");
		return StringUtils.defaultIfBlank(remoteIP, req.getRemoteAddr());
	}

	/**
	 * 获取请求报文
	 * 
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getRequestInfo(final HttpServletRequest req) {
		final StringBuilder result = new StringBuilder();

		// header
		final Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String name = headerNames.nextElement();
			result.append(name).append(": ").append(req.getHeader(name));
		}

		return result.toString();
	}

}