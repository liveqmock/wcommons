/**
 * 
 */
package wcommons.httputils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

@SuppressWarnings("rawtypes")
public class ArrayEnumeration implements Enumeration {

	//
	// Data
	//

	/** Array. */
	private Object[] array;

	/** Index. */
	private int index;

	//
	// Constructors
	//

	/** Constructs an array enumeration. */
	public ArrayEnumeration(Object[] array) {
		this.array = array;
	} // <init>(Object[])

	//
	// Enumeration methods
	//

	/**
	 * Tests if this enumeration contains more elements.
	 * 
	 * @return <code>true</code> if this enumeration contains more elements; <code>false</code>
	 *         otherwise.
	 * @since JDK1.0
	 */
	public boolean hasMoreElements() {
		return index < array.length;
	} // hasMoreElement():boolean

	/**
	 * Returns the next element of this enumeration.
	 * 
	 * @return the next element of this enumeration.
	 * @exception NoSuchElementException if no more elements exist.
	 * @since JDK1.0
	 */
	public Object nextElement() {
		if (index < array.length) {
			return array[index++];
		}
		throw new NoSuchElementException();
	} // nextElement():Object
}