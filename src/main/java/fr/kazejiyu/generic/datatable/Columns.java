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

/**
 * A collection of {@link Column} that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Columns extends Iterable <Column<?>> {

	/** @return the number of columns in the table */
	public int size();
	
	/**
	 * Adds a new column to the table.
	 * 
	 * @param column
	 * 			The new column.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Columns add(Column <?> column);
	
	/**
	 * Inserts a new column at the specified location.
	 * 
	 * @param column
	 * 			The new column.
	 * @param position
	 * 			The location of the new column.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Columns insert(Column <?> column, int position);
	
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
	public Columns remove(String header);

}
