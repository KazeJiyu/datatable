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
 * A row that belongs to a {@link Table}. 
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
	<T> T get(int column);
	
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
	public <T> T get(String header);
}
