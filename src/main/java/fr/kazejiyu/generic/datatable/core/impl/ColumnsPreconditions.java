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

import com.google.common.collect.Iterables;

import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Table;
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

	void assertHeaderDoesNotExist(String header) {
		if( columns.hasHeader(header) )
			throw new HeaderAlreadyExistsException("The header " + header + " already exist in the table");
	}

	void assertHeaderExist(String header) {
		if( ! columns.hasHeader(header) )
			throw new HeaderNotFoundException("The header " + header + " does not exist in the table");
	}

	void assertColumnSizeIsConsistent(Iterable<?> column) {
		if( table.isEmpty() )
			return;
		
		int size = Iterables.size(column);
		
		if( size != table.rows().size() )
			throw new InconsistentColumnSizeException("The column's size does not match the number of rows in the table (got: " + size + ", expected: " + table.rows().size() +")");
	}
}
