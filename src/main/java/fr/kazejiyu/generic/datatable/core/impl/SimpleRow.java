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

import static ca.odell.glazedlists.GlazedLists.eventList;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Table;

/**
 * An implementation of {@link Row} that internally uses GlazedLists.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleRow extends ModifiableRow {
	
	/** The table that owns the row. */
	private final Table table;
	
	/** The id of the row. */
	private final int id;
	
	/** The content of the row. */
	private final List<Object> elements;
	
	/** Checks methods' preconditions. */
	private final RowPreconditions preconditions;
	
	/**
	 * Creates a new empty row.
	 * 
	 * @param table
	 * 			The table that owns the row. Must not be {@code null}.
	 * @param id
	 * 			Identify the row. Must not be {@code null}.
	 * 
	 * @throws NullPointerException if at least one of the arguments if {@code null}.
	 */
	SimpleRow(final Table table, final int id) {
		this(table, id, Collections.emptyList());
	}
	
	/**
	 * Creates a new row with specified content.
	 * 
	 * @param table
	 * 			The table that owns the row. Must not be {@code null}.
	 * @param id
	 * 			Identify the row. Must not be {@code null}.
	 * @param elements
	 * 			The content of the row. Must not be {@code null}.
	 * 
	 * @throws NullPointerException if at least one of the arguments is {@code null}.
	 */
	SimpleRow(final Table table, final int id, final List <Object> elements) {
		this.id = id;
		this.table = requireNonNull(table, "The table that owns the row must not be null");
		this.elements = eventList(requireNonNull(elements, "The elements of the row must not be null"));
		this.preconditions = new RowPreconditions(table);
	}

	@Override
	public Iterator<Object> iterator() {
		return elements.iterator();
	}

	@Override
	public int id() {
		return id;
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
	public Object get(final int column) {
		return elements.get(column);
	}

	@Override
	public Object get(final String header) {
		int index = table.columns().indexOf(header);
		return elements.get(index);
	}
	
	@Override
	public <T> T get(final ColumnId<T> id) {
		int index = table.columns().indexOf(id);
		return id.type().cast( elements.get(index) );
	}
	
	@Override
	public void set(final int column, Object element) {
		preconditions.assertIsAValidIndex(column);
		preconditions.assertIsAValidElementForIndex(column, element);
		this.elements.set(column, element);
	}
	
	@Override
	public <T> void set(final ColumnId<T> id, T element) {
		preconditions.assertIsAValidColumnId(id);
		this.set(id.header(), element);
	}
	
	@Override
	public void set(final String header, Object element) {
		preconditions.assertHeaderExist(header);
		preconditions.assertIsAValidElementForHeader(header, element);
		this.set(table.columns().indexOf(header), element);
	}

	@Override
	Row insert(final int position, final Object element) {
		this.elements.add(position, element);
		return this;
	}
	
	@Override
	Row remove(final int position) {
		this.elements.remove(position);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleRow))
			return false;
		SimpleRow other = (SimpleRow) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
}
