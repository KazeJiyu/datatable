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

import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

/**
 * A row that belongs to a {@link Table}. <br>
 * <br>
 * A row is an ordered collection of heterogeneous elements.
 * The type of its elements depends on the type expected by the {@link Column}s
 * of the table.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Row extends Iterable <Object> {

	/** @return the id of the row */
	int id();
	
	/** @return the number of elements in the row */
	int size();
	
	/** @return whether the row is empty */
	boolean isEmpty();
	
	/**
	 * Returns the element of the row located in {@code column}.
	 * 
	 * @param column
	 * 			The id of the column that contains the element to retrieve.
	 * 
	 * @return the element of the row located in {@code column}.
	 * 
	 * @param <T> The runtime type of the element
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || column < 0 || size <= column)
	 * @throws ClassCastException if the element cannot be casted to {@code T}.
	 */
	Object get(int column);
	
	/**
	 * Returns the element located at the column identified by the given id. <br>
	 * <br>
	 * This method is type-safe and cannot throw a {@code ClassCastException}.
	 * 
	 * @param id
	 * 			Identifies the element's column. Must not be {@code null}.
	 * 
	 * @return the element located at the column identified by the given id.
	 * 
	 * @throws ColumnIdNotFoundException if id does not match any column
	 * @throws NullPointerException if id == null
	 */
	<T> T get(ColumnId<T> id);
	
	/**
	 * Returns the element of the row located in the column called {@code header}.
	 * 
	 * @param header
	 * 			The header of the column of the element to retrieve.
	 * 
	 * @return the element of the row located in the column called {@code header}.
	 * 
	 * @param <T> The runtime type of the element
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || column < 0 || size <= column)
	 * @throws ClassCastException if the element cannot be casted to {@code T}.
	 */
	Object get(String header);

	/**
	 * Sets the element located at {@code index}.
	 * 
	 * @param column
	 * 			The index of the element to set.
	 * @param element
	 * 			The new value.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || (0 < index <= size())
	 * @throws ClassCastException if {@code element} is not of the type expected by the column
	 */
	void set(int column, Object element);

	/**
	 * Sets the element located at the column identified by {@code id}.
	 * 
	 * @param id
	 * 			Identify the column of the element to set.
	 * @param element
	 * 			The new value.
	 * 
	 * @throws ColumnIdNotFoundException if {@code id} does not match any column
	 */
	<T> void set(ColumnId<T> id, T element);

	/**
	 * Sets the element located at {@code index}.
	 * 
	 * @param index
	 * 			The index of the element to set.
	 * @param element
	 * 			The new value.
	 * 
	 * @throws HeaderNotFoundException if {@code header} does not match any header in the column
	 */
	void set(String header, Object element);
}
