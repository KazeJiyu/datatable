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
import static ca.odell.glazedlists.GlazedLists.eventList;

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
	@SuppressWarnings("unchecked")
	public <T> T get(final int column) {
		Class <?> runtimeClass = table.columns().get(column).type();
		return (T) runtimeClass.cast(elements.get(column));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(final String header) {
		int index = table.columns().indexOf(header);
		return (T) elements.get(index);
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

}
