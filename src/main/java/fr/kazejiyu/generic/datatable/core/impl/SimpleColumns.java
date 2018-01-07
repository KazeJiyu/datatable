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

import static java.util.Arrays.asList;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Table;

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
	
	/** Checks methods' preconditions. */
	private final ColumnsPreconditions preconditions;
	
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
		this.preconditions = new ColumnsPreconditions(table, this);
	}
	
	@Override
	public LinkedHashSet<String> headers() {
		LinkedHashSet <String> headers = new LinkedHashSet<>();
		
		for( Column <?> column : elements )
			headers.add(column.header());
		
		return headers;
	}
	
	@Override
	public boolean hasHeader(String header) {
		return headerToIndex.containsKey(normalize(header));
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
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Column<?> get(final int index) {
		return elements.get(index);
	}
	
	@Override
	public Stream<Column<?>> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	private String normalize(final String header) {
		return header.toLowerCase();
	}

	@Override
	public Columns remove(final int index) {
		preconditions.assertIsAValidIndex(index);
			
		elements.remove(index);
		headerToIndex.inverse().remove(index);
		
		for( Row row : table.rows() )
			((ModifiableRow) row).remove(index);
		
		return this;
	}

	@Override
	public int indexOf(final String header) {
		preconditions.assertHeaderExist(header);
		return headerToIndex.get(normalize(header));
	}

	@Override
	public <N> Columns create(final Class<N> type, final String header, final Iterable<N> column) {
		preconditions.assertIsAValidNewColumn(type, header, column);
		
		Iterator<N> itElement = column.iterator();

		if( table.rows().isEmpty() ) {
			while( itElement.hasNext() )
				table.rows().create(asList(itElement.next()));
		}
		else {
			for(Row row : table.rows())
				((ModifiableRow) row).add(itElement.next());
		}
		
		addColumn( new SimpleColumn<>(type, table, header) );
		return this;
	}
	
	private void addColumn(final Column <?> column) {
		headerToIndex.put(normalize(column.header()), size());
		elements.add(column);
	}
	
	@Override
	public Columns clear() {
		for( int i = size() ; i > 0 ; --i )
			remove(i-1);
		
		return this;
	}
}
