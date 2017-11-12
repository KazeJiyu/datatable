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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import fr.kazejiyu.generic.datatable.Column;
import fr.kazejiyu.generic.datatable.Columns;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Table;

/**
 * An implementation of {@link Columns}.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleColumns implements Columns {
	
	/** The table that owns the columns. */
	private final Table table;
	
	/** The columns that compose the table. */
	private final List <Column<?>> elements;
	
	/** Maps the header in a case-insensitive way to their index. */
	private final BiMap <String,Integer> headerToIndex;
	
	/**
	 * Creates the columns of {@code table}.
	 * 
	 * @param table
	 * 			The table that owns the columns.
	 */
	SimpleColumns(final Table table) {
		this.table = table;
		this.elements = new LinkedList<>();
		this.headerToIndex = HashBiMap.create();
	}
	
	@Override
	public LinkedHashSet <String> headers() {
		LinkedHashSet <String> headers = new LinkedHashSet<>();
		
		for( Column <?> column : elements )
			headers.add(column.header());
		
		return headers;
	}

	@Override
	public Iterator<Column<?>> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public Column<?> get(final int index) {
		return elements.get(index);
	}
	
	private String simplify(final String header) {
		return header.toLowerCase();
	}
	
	private void addColumn(final Column <?> column) {
		headerToIndex.put(simplify(column.header()), size());
		elements.add(column);
	}

	@Override
	public Columns insert(final int position, final Column<?> column) {
		addColumn(column);
		
		for( Row row : table.rows() )
			((ModifiableRow) row).insert(position, column);
		
		return this;
	}

	@Override
	public Columns remove(final int index) {
		elements.remove(index);
		headerToIndex.inverse().remove(index);
		
		for( Row row : table.rows() )
			((ModifiableRow) row).remove(index);
		
		return this;
	}

	@Override
	public int indexOf(final String header) {
		return headerToIndex.get(simplify(header));
	}

	@Override
	public <N> Columns create(final Class<N> type, final String header, final Iterable<N> elements) {
		Iterator <N> itElement = elements.iterator();

		if( table.rows().size() == 0 ) {
			for( int id = 0 ; itElement.hasNext() ; id++ )
				table.rows().add(new SimpleRow(table, id, Arrays.asList(itElement.next())));
		}
		else {
			Iterator <Row> itRow = table.rows().iterator();
			
			while( itRow.hasNext() && itElement.hasNext() )
				((ModifiableRow) itRow.next()).add(itElement.next());
		}
		
		addColumn( new SimpleColumn<>(type, table, header) );
		return this;
	}
}
