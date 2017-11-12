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
package fr.kazejiyu.generic.datatable.impl;

import java.util.Iterator;

import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Rows;

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
	
	/**
	 * Creates a new iterator on the column identified by {@code header}.
	 * 
	 * @param rows
	 * 			The whole rows of the table.
	 * @param column
	 * 			The column to iterate through.
	 */
	ColumnIterator(final Rows rows, final int column) {
		this.rows = rows.iterator();
		this.column = column;
	}

	@Override
	public boolean hasNext() {
		return rows.hasNext();
	}

	@Override
	public T next() {
		return rows.next().get(column);
	}

}
