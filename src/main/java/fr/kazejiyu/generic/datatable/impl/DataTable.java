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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;
import fr.kazejiyu.generic.datatable.Columns;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Rows;
import fr.kazejiyu.generic.datatable.Table;

/**
 * A simple implementation of {@link Table} that relies on {@code GlazedLists}.
 * 
 * @author Emmanuel CHEBBI
 */
public class DataTable implements Table, AutoCloseable {
	
	/** The rows that compose the table. */
	private final SimpleRows rows;
	
	/** The columns that compose the table. */
	private final Columns columns;
	
	/**
	 * Creates a new table.
	 */
	public DataTable() {
		this.rows = new SimpleRows(this);
		this.columns = new SimpleColumns(this);
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
	public DataTable filter(Matcher<Row> matcher, LinkedHashSet<String> columnsToKeep) {
		FilterList <Row> filtered = new FilterList<>(rows.internal(), matcher);
		DataTable filteredTable = emptyTable(columnsToKeep);
		
		List<Integer> indexes = indexesOf(columnsToKeep);
		
		for( Row row : filtered ) {
			List <Object> elements = new ArrayList<>();
			
			for( int index : indexes ) {
				Object value = row.get(index);
				elements.add(value);
			}
			
			filteredTable.rows.create(elements);
		}
		
		filtered.dispose(); // avoid possible memory leaks
		return filteredTable;
	}
	
	/** @return a new empty Table with the selected columns */
	private DataTable emptyTable(LinkedHashSet<String> headers) {
		DataTable empty = new DataTable();
		
		for( String header : headers ) 
			empty.columns().create(columns.get(header).type(), header);
		
		return empty;
	}
	
	/** @return the indexes of the selected columns */
	private List<Integer> indexesOf(Set<String> headers) {
		return headers.stream()
			   .map(columns::indexOf)
			   .collect(toList());
	}

	@Override
	public void close() throws Exception {
		this.dispose();
	}
	
	/**
	 * 
	 */
	public void dispose() {
		if( rows.internal() instanceof TransformedList<?,?> )
			((TransformedList<?,?>) rows.internal()).dispose();
	}

}
