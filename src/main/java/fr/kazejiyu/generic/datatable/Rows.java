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
 * A collection of {@link Row} that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Rows extends Iterable <Row> {

	/** @return the number of rows in the table */
	public int size();
	
	/**
	 * Adds a new row to the table.
	 * 
	 * @param row
	 * 			The new row.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Rows add(Row row);
	
	/**
	 * Inserts a new row at the specified location.
	 * 
	 * @param row
	 * 			The new row.
	 * @param position
	 * 			The location of the new row.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Rows insert(Row row, int position);
	
	/**
	 * Removes a row from the table.
	 * 
	 * @param row
	 * 			The row to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public default Rows remove(Row row) {
		return remove(row.id());
	}
	
	/**
	 * Removes a row from the table.
	 * 
	 * @param id
	 * 			The id of the row to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	public Rows remove(int id);
	
}
