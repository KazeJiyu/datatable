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
package fr.kazejiyu.generic.datatable;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * A collection of {@link Column} that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Columns extends Iterable <Column<?>> {
	
	/** @return whether the collection is empty */
	public default boolean isEmpty() {
		return size() == 0;
	}

	/** @return the number of columns in the table */
	public int size();
	
	/** @return the headers of the columns */
	public LinkedHashSet <String> headers();
	
	/**
	 * Returns the {@code Column} identified by the given header.
	 * 
	 * @param header
	 * 			Identify the column to return.
	 * 
	 * @return the column identified by the given header
	 */
	public default Column<?> get(String header) {
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
	public Column<?> get(int index);
	
	/**
	 * Adds a new column to the table.
	 * 
	 * @param column
	 * 			The new column.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public default Columns add(Column <?> column) {
		return insert(size(), column);
	}
	
	/**
	 * Inserts a new column at the specified location.
	 * @param position
	 * 			The location of the new column.
	 * @param column
	 * 			The new column.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Columns insert(int position, Column <?> column);
	
	/**
	 * Removes a column from the table.
	 * 
	 * @param column
	 * 			The column to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public default Columns remove(Column <?> column) {
		return remove(column.header());
	}
	
	/**
	 * Removes a column from the table.
	 * 
	 * @param header
	 * 			The header of the column to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public default Columns remove(String header) {
		return remove(indexOf(header));
	}
	
	/**
	 * Removes a column from the table.
	 * 
	 * @param index
	 * 			The index of the column to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Columns remove(int index);
	
	/**
	 * Returns the index of the column corresponding to the given header.
	 * 
	 * @param header
	 * 			The header of the column.
	 * 
	 * @return the index of the column corresponding to the given header.
	 */
	public int indexOf(String header);
	
	public default Columns create(Class<?> type, String header) {
		return create(type, header, Collections.emptyList());
	}
	
	/**
	 * Creates a new {@code Column} from a given iterable.
	 * 
	 * @param header
	 * 			The column's header
	 * @param elements
	 * 			The elements of the column
	 * 
	 * @return the new column containing {@code elements}.
	 * 
	 * @param <N> The type of the elements in the new column
	 */
	@SuppressWarnings("unchecked")
	public default <N> Columns create(Class<N> type, String header, N... elements) {
		return create(type, header, Arrays.asList(elements));
	}

	/**
	 * Creates a new {@code Column} from a given iterable.
	 * 
	 * @param header
	 * 			The column's header
	 * @param elements
	 * 			The elements of the column
	 * 
	 * @return the new column containing {@code elements}.
	 * 
	 * @param <N> The type of the elements in the new column
	 */
	public <N> Columns create(Class<N> type, String header, Iterable<N> elements);
}
