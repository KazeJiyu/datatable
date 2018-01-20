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
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;
import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Rows;
import fr.kazejiyu.generic.datatable.core.Table;

/**
 * A simple implementation of {@link Table} that relies on {@code GlazedLists}.
 * 
 * @author Emmanuel CHEBBI
 */
public class DataTable implements Table {
	
	/** The rows that compose the table. */
	private final SimpleRows rows;
	
	/** The columns that compose the table. */
	private final Columns columns;
	
	/** Checks methods' preconditions. */
	private final TablePreconditions preconditions;
	
	/**
	 * Creates a new table.
	 */
	public DataTable() {
		this.rows = new SimpleRows(this);
		this.columns = new SimpleColumns(this);
		this.preconditions = new TablePreconditions(this);
	}
	
	@Override
	public Rows rows() {
		return rows;
	}

	@Override
	public Columns columns() {
		return columns;
	}
	
	@Override
	public boolean isEmpty() {
		return rows.isEmpty() || rows.stream().allMatch(Row::isEmpty);
	}
	
	@Override
	public Table clear() {
		rows.clear();
		columns.clear();
		return this;
	}
	
	@Override
	public DataTable filter(Matcher<Row> matcher, LinkedHashSet<String> columnsToKeep) {
		requireNonNull(matcher, "The matcher must not be null");
		requireNonNull(columnsToKeep, "The columns to keep must not be null");
		preconditions.assertAreExistingHeaders(columnsToKeep);
		
		FilterList <Row> filtered = new FilterList<>(rows.internal(), matcher);
		DataTable filteredTable = emptyTable(columnsToKeep);
		
		List<Integer> indexesOfColumnsToKeep = indexesOf(columnsToKeep);
		
		for( Row row : filtered ) {
			List<Object> filteredRow = pickElementsAtIndexes(row, indexesOfColumnsToKeep);
			filteredTable.rows.create(filteredRow);
		}
		
		filtered.dispose(); // avoid possible memory leaks
		return filteredTable;
	}
	
	/** @return the indexes of the selected columns */
	private List<Integer> indexesOf(Set<String> headers) {
		return headers.stream()
				.map(columns::indexOf)
				.collect(toList());
	}
	
	/** @return a new empty Table with the selected columns */
	private DataTable emptyTable(LinkedHashSet<String> headers) {
		DataTable empty = new DataTable();
		
		for( String header : headers ) 
			empty.columns().create(columns.get(header).type(), header);
		
		return empty;
	}

	/** @return a new List containing only the elements of {@row} located at {@code indexes} */
	private List<Object> pickElementsAtIndexes(Row row, List<Integer> indexesOfColumnsToKeep) {
		List <Object> elements = new ArrayList<>();
		
		for( int index : indexesOfColumnsToKeep ) {
			Object value = row.get(index);
			elements.add(value);
		}
		return elements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rows.hashCode();
		return result;
	}

	/**
	 * Returns whether {@code obj} is equal to {@code this}. <br>
	 * <br>
	 * Returns {@code true} when:
	 * <ul>
	 * 	<li>{@code obj} is a {@code DataTable},</li>
	 * 	<li>and {@code obj} has the same <em>content</em> as {@code this}.</li>
	 * </ul>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DataTable))
			return false;
		DataTable other = (DataTable) obj;
		return rows.equals(other.rows);
	}
}
