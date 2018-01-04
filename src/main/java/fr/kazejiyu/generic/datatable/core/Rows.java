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

import java.util.List;

/**
 * A collection of {@link Row} that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Rows extends Iterable <Row> {
	
	/** @return whether the collection is empty */
	public default boolean isEmpty() {
		return size() == 0;
	}

	/** @return the number of rows in the table */
	public int size();
	
	/**
	 * Returns the row located at {@code index}.
	 * 
	 * @param index
	 * 			The index of the row to get.
	 * 
	 * @return the row located at {@code index}.
	 */
	public Row get(int index);

	/** @return the first row of the table */
	public default Row first() {
		return get(0);
	}
	
	/** @return the last row of the table */
	public default Row last() {
		return get(size() - 1);
	}
	
	/**
	 * Creates then adds a new row.
	 * 
	 * @param elements
	 * 			The elements of the new row. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws NullPointerException if {@code elements} is {@code null}.
	 * 
	 * @see #add(Row)
	 */
	public Rows create(List <Object> elements);
	
	/**
	 * Adds a new row to the table.
	 * 
	 * @param row
	 * 			The new row.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @see #create(List)
	 */
	public Rows add(Row row);
	
	/**
	 * Inserts a new row at the specified location.
	 * 
	 * @param position
	 * 			The location of the new row.
	 * @param row
	 * 			The new row. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws NullPointerException if {@code row} is {@code null}.
	 */
	public Rows insert(int position, Row row);
	
	/**
	 * Removes a row from the table.
	 * 
	 * @param row
	 * 			The row to remove. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws NullPointerException if {@code row} is {@code null}.
	 */
	public default Rows remove(Row row) {
		return remove(row.id());
	}
	
	/**
	 * Removes a row from the table.
	 * 
	 * @param index
	 * 			The index of the row to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Rows remove(int index);
	
	/**
	 * Removes all the rows.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Rows clear();	
}