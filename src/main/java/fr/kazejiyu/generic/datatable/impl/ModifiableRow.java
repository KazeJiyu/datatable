package fr.kazejiyu.generic.datatable.impl;

import fr.kazejiyu.generic.datatable.Row;

/**
 * A {@link Row} that can be modified.
 * 
 * @author Emmanuel CHEBBI
 */
abstract class ModifiableRow implements Row {

	/**
	 * Appends {@code element} at the end of the row.
	 * 
	 * @param element
	 * 			The value to append.
	 * 
	 * @return a reference to the instance to enable method chaining
	 */
	Row add(final Object element) {
		return insert(size(), element);
	}
	
	/**
	 * Inserts {@code element} at the specified {@code position}.
	 * 
	 * @param position
	 * 			The position at which insert the element.
	 * @param element
	 * 			The value to insert.
	 * 
	 * @return a reference to the instance to enable method chaining
	 */
	abstract Row insert(int position, Object element);
	
	/**
	 * Removes the element located at the specified {@code position}.
	 * 
	 * @param position
	 * 			The position of the element to remove.
	 * 
	 * @return a reference to the instance to enable method chaining
	 */
	abstract Row remove(int position);
		
}
