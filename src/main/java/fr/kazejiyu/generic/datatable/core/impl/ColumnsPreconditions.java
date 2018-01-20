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

import com.google.common.collect.Iterables;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderAlreadyExistsException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentColumnSizeException;

/**
 * Enforces preconditions about {@link Columns} instances.
 * 
 * @author Emmanuel CHEBBI
 */
class ColumnsPreconditions {
	
	private final Table table;
	private final Columns columns;
	
	ColumnsPreconditions(final Table table, final Columns columns) {
		this.table = table;
		this.columns = columns;
	}

	/** @throws HeaderAlreadyExistsException if columns.hasHeader(header) */
	void assertHeaderDoesNotExist(String header) {
		if( columns.hasHeader(header) )
			throw new HeaderAlreadyExistsException("The header " + header + " already exist in the table");
	}

	/** @throws HeaderNotFoundException if ! columns.hasHeader(header) */
	void assertHeaderExist(String header) {
		if( ! columns.hasHeader(header) )
			throw new HeaderNotFoundException("The header " + header + " does not exist in the table");
	}

	/** @throws InconsistentColumnSizeException if ! table.isEmpty() && table.rows().size() != size(column) */
	void assertColumnSizeIsConsistent(Iterable<?> column) {
		if( table.isEmpty() )
			return;
		
		int size = Iterables.size(column);
		
		if( size != table.rows().size() )
			throw new InconsistentColumnSizeException("The column's size does not match the number of rows in the table (got: " + size + ", expected: " + table.rows().size() +")");
	}

	/** @throws IndexOutOfBoundsException if columns.isEmpty() || index < 0 || columns.size() < index */
	void assertIsAValidIndex(int index) {
		if ((columns.isEmpty()) || (index < 0 || columns.size() < index))
			throw new IndexOutOfBoundsException("There is no row at index " + index);		
	}

	/** 
	 * @throws NullPointerException if any parameter is {@code null}
	 * @throws HeaderAlreadyExistsException if columns.hasHeader(header)
	 * @throws InconsistentColumnSizeException if size(column) != table.rows().size()
	 */
	<N> void assertIsAValidNewColumn(Class<N> type, String header, Iterable<N> column) {
		requireNonNull(type, "The type of a column must not be null");
		requireNonNull(header, "The header of a column must not be null");
		requireNonNull(column, "The content of a column must not be null");
		assertHeaderDoesNotExist(header);
		assertColumnSizeIsConsistent(column);
	}

	<T> void assertIsAValidColumnId(ColumnId<T> id) {
		requireNonNull(id, "The index must not be null");
		
		if( ! table.columns().hasHeader(id.header())
		 || ! id.type().isAssignableFrom(table.columns().get(id.header()).type()) )
			throw new ColumnIdNotFoundException("The id with header [" + id.header() + "] and type [" + id.type() + "] does not match any column");
	}

	public void assertIsAValidElementForIndex(int index, Object element) {
		Column<?> column = table.columns().get(index);
		
		if( ! column.type().isInstance(element) )
			throw new ClassCastException(
					element + " cannot be added to column " + column.header() + ": "
					+ "expected type is " + column.type() + " but was " + (element == null ? "null" : element.getClass()));
	}

	public void assertIsAValidElementForHeader(String header, Object element) {
		Column<?> column = table.columns().get(header);
		
		if( ! column.type().isInstance(element) )
			throw new ClassCastException(
					element + " cannot be added to column " + column.header() + ": "
					+ "expected type is " + column.type() + " but was " + (element == null ? "null" : element.getClass()));
	}
}
