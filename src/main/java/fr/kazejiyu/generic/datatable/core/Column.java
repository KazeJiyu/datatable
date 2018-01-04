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
 * A column that belongs to a {@link Table}.
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
	 * Returns the element of the column located in {@code row}.
	 * 
	 * @param row
	 * 			The id of the row that contains the element to retrieve.
	 * 
	 * @return the element of the column located in {@code row}.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || row < 0 || size <= row)
	 * @throws ClassCastException if the element cannot be casted to {@code T}.
	 */
	T get(int row);
}
