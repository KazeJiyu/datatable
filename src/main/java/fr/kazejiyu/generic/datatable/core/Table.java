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

import java.util.LinkedHashSet;

import ca.odell.glazedlists.matchers.Matcher;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

/**
 * A table containing {@link Rows} and {@link Columns}.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Table {
	
	/**
	 * Returns whether the table is empty or not. <br>
	 * <br>
	 * A table is considered empty if it has either:
	 * <ul>
	 * 	<li>no rows at all,</li>
	 * 	<li>or only empty rows.</li>
	 * </ul>
	 * 
	 * @return whether the table is empty or not. 
	 */
	boolean isEmpty();

	/** @return the rows of the table */
	Rows rows();
	
	/** @return the columns of the table */
	Columns columns();
	
	/**
	 * Erases the content of the table by removing all its rows and
	 * all its columns.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	Table clear();

	/**
	 * Filters the table. <br>
	 * <br>
	 * The returned table is a new one, which backs new rows. However,
	 * since no deep copy is performed the object contained by the resulting
	 * table are the same than the ones stored by the original table.
	 * 
	 * @param matcher
	 * 			Selects the rows to keep. Must not be {@code null}.
	 * 
	 * @return a new {@code Table} containing the filtered rows.
	 * 
	 * @throws NullPointerException if {@code matcher} is {@code null}.
	 */
	default Table filter(Matcher<Row> matcher) {
		return filter(columns().headers(), matcher);
	}
	
	/**
	 * Filters the table keeping only specific columns.
	 * 
	 * @param columnsToKeep
	 * 			The name of the columns to keep. Must not be {@code null}
	 * @param matcher
	 * 			Selects the rows to keep. Must not be {@code null}.
	 * 
	 * @return a new {@code Table} containing the filtered rows.
	 * 
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 * @throws HeaderNotFoundException if ! columns().headers().containsAll(columnsToKeep)
	 */
	Table filter(LinkedHashSet<String> columnsToKeep, Matcher<Row> matcher);
	
	/**
	 * Filters the table keeping only specific columns.
	 * 
	 * @param idsOfColumnsToKeep
	 * 			The ids of the columns to keep. Must not be {@code null}
	 * @param matcher
	 * 			Selects the rows to keep. Must not be {@code null}.
	 * 
	 * @return a new {@code Table} containing the filtered rows.
	 * 
	 * @throws NullPointerException if any of the arguments is {@code null}.
	 * @throws ColumnIdNotFoundException if one of the given ids does not match any column
	 */
	DataTable filterById(LinkedHashSet<ColumnId<?>> idsOfColumnsToKeep, Matcher<Row> matcher);
}
