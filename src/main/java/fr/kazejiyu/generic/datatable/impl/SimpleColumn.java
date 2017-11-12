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

import fr.kazejiyu.generic.datatable.Column;
import fr.kazejiyu.generic.datatable.Table;

/**
 * A simple implementation of {@link Column}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the elements in the column.
 */
class SimpleColumn <T> implements Column <T> {

	/** The table that owns the column. */
	private final Table table;
	
	/** The column's header. */
	private final String header;
	
	/** The type of the elements in the column. */
	private final Class <T> type;
	
	/**
	 * Creates a new column.
	 * 
	 * @param type
	 * 			The type of the elements in the column.
	 * @param table
	 * 			The table that owns the column.
	 * @param header
	 * 			The header of the column.
	 */
	SimpleColumn(final Class <T> type, final Table table, final String header) {
		this.type = type;
		this.table = table;
		this.header = header;
	}
	
	@Override
	public Iterator <T> iterator() {
		return new ColumnIterator<>(table.rows(), table.columns().indexOf(header));
	}

	@Override
	public String header() {
		return header;
	}

	@Override
	public int size() {
		return table.rows().size();
	}

	@Override
	public Class <T> type() {
		return type;
	}

	@Override
	public T get(final int row) {
		int column = table.columns().indexOf(header);
		return table.rows().get(row).get(column);
	}

}
