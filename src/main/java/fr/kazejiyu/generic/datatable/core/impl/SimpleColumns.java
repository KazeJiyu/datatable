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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

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
	
	private String normalize(final String header) {
		return header.toLowerCase();
	}
	
	private void addColumn(final Column <?> column) {
		headerToIndex.put(normalize(column.header()), size());
		elements.add(column);
	}

	@Override
	public Columns remove(final int index) {
		if ((isEmpty()) || (index < 0 || size() < index))
			throw new IndexOutOfBoundsException("There is no row at index " + index);
			
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
		requireNonNull(type, "The type of a column must not be null");
		requireNonNull(header, "The header of a column must not be null");
		requireNonNull(column, "The content of a column must not be null");
		preconditions.assertHeaderDoesNotExist(header);
		preconditions.assertColumnSizeIsConsistent(column);
		
		Iterator<N> itElement = column.iterator();

		if( table.rows().isEmpty() ) {
			for( int id = 0 ; itElement.hasNext() ; id++ )
				table.rows().add(new SimpleRow(table, id, Arrays.asList(itElement.next())));
		}
		else {
			for(Row row : table.rows())
				((ModifiableRow) row).add(itElement.next());
		}
		
		addColumn( new SimpleColumn<>(type, table, header) );
		return this;
	}
}
