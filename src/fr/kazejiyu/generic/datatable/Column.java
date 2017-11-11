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

/**
 * A column that belongs to a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the elements stored by the column
 */
public interface Column <T> extends Iterable <T> {

	/** @return the header of the column */
	public String header();
	
	/**
	 * Returns the element of the column located in {@code row}.
	 * 
	 * @param row
	 * 			The id of the row that contains the element to retrieve.
	 * 
	 * @return the element of the column located in {@code row}.
	 */
	public T get(int row);

	/**
	 * Creates a new {@code Column} from a given iterable.
	 * 
	 * @param header
	 * 			The column's header
	 * @param elements
	 * 			The elements of the column
	 * 
	 * @return the new column containing {@code elements}.
	 */
	@SafeVarargs
	public static <N> Column<N> of(String header, N... elements) {
		return of(header, Arrays.asList(elements));
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
	 */
	public static <N> Column<N> of(String header, Iterable<N> elements) {
		return null;	// TODO Implement Column.of(String, Iterable)
	}
}
