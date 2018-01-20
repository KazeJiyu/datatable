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

import java.util.List;

import fr.kazejiyu.generic.datatable.core.Rows;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentRowSizeException;

/**
 * Enforces preconditions about {@link Rows} instances.
 * 
 * Emmanuel CHEBBI
 */
class RowsPreconditions {
	
	private final Table table;
	
	public RowsPreconditions(Table table) {
		this.table = table;
	}

	void assertRowSizeIsConsistent(List<Object> elements) {
		if( ! table.columns().isEmpty() && elements.size() != table.columns().size() )
			throw new InconsistentRowSizeException(
					"Row's size does not match the number of columns in the table "
				  + "(got: " + elements.size() + ", expected: " + table.columns().size() + ")");
	}

	void assertRowElementsAreOfTheExpectedTypes(List<Object> elements) {
		for(int i = 0 ; i < table.columns().size() ; ++i)
			if( ! table.columns().get(i).accepts(elements.get(i)) )
				throw new ClassCastException(
						"Row's " + i + "th element has not the expected type "
					  + "(got: " + elements.getClass() + " expecting: " + table.columns().get(i).type() + ")");
	}

	/**
	 * @throws NullPointerException if elements == null
	 * @throws InconsistentRowSizeException if table.columns().size() != row.size()
	 * @throws ClassCastException if row's elements have not the type expected by table's columns
	 */
	void assertIsAValidNewRow(List<Object> row) {
		requireNonNull(row, "The content of the new row must not be null");
		assertRowSizeIsConsistent(row);
		assertRowElementsAreOfTheExpectedTypes(row);
	}
	
	

}
