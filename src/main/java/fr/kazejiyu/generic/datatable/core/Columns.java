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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentColumnSizeException;

/**
 * A collection of {@link Column} that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Columns extends Iterable <Column<?>> {
	
	/** @return whether the collection is empty */
	boolean isEmpty();

	/** @return the number of columns in the table */
	int size();
	
	/** @return the headers of the columns */
	LinkedHashSet <String> headers();
	
	/** @return whether {@code header} is a valid header */
	boolean hasHeader(String header);
	
	/** @return the first column of the table */
	default Column<?> first() {
		return get(0);
	}
	
	/** @return the last column of the table */
	default Column<?> last() {
		return get(size() - 1);
	}
	
	/**
	 * Returns the {@code Column} identified by the given header.
	 * 
	 * @param header
	 * 			Identify the column to return.
	 * 
	 * @return the column identified by the given header
	 */
	default Column<?> get(String header) {
		return get(indexOf(header));
	}
	
	/**
	 * Returns the {@code Column} located at the specified index.
	 * 
	 * @param index
	 * 			The index of the column to get.
	 * 
	 * @return the column located at the specified index
	 */
	Column<?> get(int index);
	
	/**
	 * Returns the index of the column corresponding to the given header.
	 * 
	 * @param header
	 * 			The header of the column.
	 * 
	 * @return the index of the column corresponding to the given header.
	 * 
	 * @throws HeaderNotFoundException if ! hasHeader(header)
	 */
	int indexOf(String header);
	
	default Columns create(Class<?> type, String header) {
		return create(type, header, Collections.emptyList());
	}
	
	/**
	 * Creates a new {@code Column} from a given iterable.
	 * 
	 * @param type
	 * 			The type of the elements stored in the column.
	 * 			Must not be {@code null}.
	 * @param header
	 * 			The column's header.
	 * 			Must not be {@code null}.
	 * @param column
	 * 			The elements of the column.
	 * 			Must not be {@code null}.
	 * 
	 * @return the new column containing {@code elements}.
	 * 
	 * @param <N> The type of the elements in the new column
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}
	 * @throws InconsistentColumnSizeException if {@code column.length} != {@code rows.size()}
	 */
	@SuppressWarnings("unchecked")
	default <N> Columns create(Class<N> type, String header, N... column) {
		return create(type, header, Arrays.asList(column));
	}

	/**
	 * Creates a new {@code Column} from a given iterable.
	 * 
	 * @param type
	 * 			The type of the elements in the new column.
	 * 			Must not be {@code null}. 
	 * @param header
	 * 			The column's header.
	 * 			Must not be {@code null}. 
	 * @param column
	 * 			The elements of the column.
	 * 			Must not be {@code null}. 
	 * 
	 * @return the new column containing {@code elements}.
	 * 
	 * @param <N> The type of the elements in the new column
	 * 
	 * @throws NullPointerException if any of the parameters is {@code null}.
	 * @throws InconsistentColumnSizeException if {@code column.size()} != {@code this.size()}
	 */
	<N> Columns create(Class<N> type, String header, Iterable<N> column);
	
	/**
	 * Removes a column from the table.
	 * 
	 * @param header
	 * 			The header of the column to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws HeaderNotFoundException if ! hasHeader(header)
	 */
	default Columns remove(String header) {
		return remove(indexOf(header));
	}
	
	/**
	 * Removes a column from the table.
	 * 
	 * @param index
	 * 			The index of the column to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || (index < 0 || size <= index)
	 */
	Columns remove(int index);
}
