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

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Table;

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
	
	/** Identifies uniquely the column by providing its type and its header. */
	private final ColumnId<T> id;
	
	/**
	 * Creates a new column.
	 * @param id
	 * 			Identifies uniquely the column. Must not be {@code null}.
	 * @param table
	 * 			The table that owns the column. Must not be {@code null}.
	 * 
	 * @throws NullPointerException if at least one of the arguments is {@code null}.
	 */
	SimpleColumn(final ColumnId<T> id, final Table table) {
		this.id = requireNonNull(id, "The id of the column must not be null");
		this.table = requireNonNull(table, "The table that owns the column must not be null");
	}
	
	@Override
	public Iterator <T> iterator() {
		return new ColumnIterator<>(id.type(), table.rows(), table.columns().indexOf(id.header()));
	}

	@Override
	public String header() {
		return id.header();
	}

	@Override
	public boolean isEmpty() {
		return table.rows().isEmpty();
	}
	
	@Override
	public int size() {
		return table.rows().size();
	}

	@Override
	public Class <T> type() {
		return id.type();
	}
	
	@Override
	public boolean accepts(Object object) {
		return id.type().isInstance(object);
	}

	@Override
	public T get(final int row) {
		int column = table.columns().indexOf(id.header());
		Object element = table.rows().get(row).get(column);
		return id.type().cast(element);
	}

}
