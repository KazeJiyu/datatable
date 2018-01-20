/*
 * 		Copyright 2017 Emmanuel CHEBBI
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.kazejiyu.generic.datatable.core;

/**
 * A column that belongs to a {@link Table}. <br>
 * <br>
 * A column is an ordered collection of elements of a same type. It is also
 * identified by its header : a string that should be unique across the table.
 * 
 * <pre><code class="java">String truc = new String("hello");</code></pre>
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the elements stored by the column
 */
public interface Column <T> extends Iterable <T> {

	/** @return the header of the column */
	String header();
	
	/** @return the type of the column's elements */
	Class <T> type();
	
	/** @return the number of elements in the column */
	int size();
	
	/** @return whether the column is empty */
	boolean isEmpty();
	
	/**
	 * Returns whether {@code object} could be accepted into the column. <br>
	 * <br>
	 * Typically, this method checks {@code object}'s type to ensure that it
	 * is a subtype of {@link #type()}.
	 * 
	 * @param object
	 * 			The object that could be into the column.
	 * 
	 * @return whether {@code object} could be accepted into the row.
	 */
	boolean accepts(Object object);
	
	/**
	 * Returns the element of the column located in {@code row}.
	 * 
	 * @param row
	 * 			The id of the row that contains the element to retrieve.
	 * 
	 * @return the element of the column located in {@code row}.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || row &lt; 0 || size &lt;= row)
	 * @throws ClassCastException if the element cannot be casted to {@code T}.
	 */
	T get(int row);
	
	/**
	 * Sets the element at {@code row}.
	 * 
	 * @param row
	 * 			The index of the row where the new element is set.
	 * @param element
	 * 			The element to put in the column.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || row &lt; 0 || size &lt;= row
	 */
	void set(int row, T element);
}
