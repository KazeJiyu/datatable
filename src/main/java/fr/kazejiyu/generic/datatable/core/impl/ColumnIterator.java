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
package fr.kazejiyu.generic.datatable.core.impl;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;

import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Rows;

/**
 * Iterates across the elements of a {@link Column}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the elements stored by the column
 */
class ColumnIterator <T> implements Iterator <T> {

	/** Iterate through the rows of the table. */
	private final Iterator <Row> rows;
	
	/** The index of the column to iterate. */
	private final int column;
	
	/** The type of column's elements. */
	private final Class<T> type;
	
	/**
	 * Creates a new iterator on the column identified by {@code header}.
	 * 
	 * @param type
	 * 			The type of the elements of the column. Must not be {@code null}.
	 * @param rows
	 * 			The whole rows of the table. Must not be {@code null}.
	 * @param column
	 * 			The column to iterate through. Must not be {@code null}.
	 * 
	 * @throws NullPointerException if {@code rows} or {@code column} is {@code null}.
	 */
	ColumnIterator(Class<T> type, final Rows rows, final int column) {
		this.type = requireNonNull(type, "The type must not be null");
		this.rows = requireNonNull(rows, "The rows must not be null").iterator();
		this.column = requireNonNull(column, "The column to iterate on must not be null");
	}

	@Override
	public boolean hasNext() {
		return rows.hasNext();
	}

	@Override
	public T next() {
		return type.cast(rows.next().get(column));
	}

}
