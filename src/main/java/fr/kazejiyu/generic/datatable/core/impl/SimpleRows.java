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

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Rows;
import fr.kazejiyu.generic.datatable.core.Table;

/**
 * An implementation of {@link Rows}.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleRows implements Rows {
	
	/** The table that owns the rows. */
	private final Table table;
	
	/** Checks methods' preconditions. */
	private final RowsPreconditions preconditions;
	
	/** The content of the rows. */
	private final EventList <Row> elements = new BasicEventList<>();
	
	/**
	 * Creates the rows of {@code table}.
	 * 
	 * @param table
	 * 			The table that owns the rows. Must not be {@code null}.
	 * 
	 * @throws NullPointerException if {@code table} is {@code null}.
	 */
	SimpleRows(final Table table) {
		this.table = requireNonNull(table, "The table that owns the columns must not be null");
		this.preconditions = new RowsPreconditions(table);
	}
	
	/** @return the {@code EventList} used internally */
	EventList <Row> internal() {
		return elements;
	}

	@Override
	public Iterator<Row> iterator() {
		return elements.iterator();
	}
	
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public Row get(int index) {
		return elements.get(index);
	}
	
	@Override
	public Stream<Row> stream() {
		return elements.stream();
	}
	
	private int nextId() {
		return isEmpty() ? 0 : last().id() + 1;
	}
	
	@Override
	public Rows create(final List <Object> elements) {
		preconditions.assertIsAValidNewRow(elements);
		return add(new SimpleRow(table, nextId(), elements));
	}

	@Override
	public Rows add(final Row row) {
		elements.add(requireNonNull(row, "The row to add must not be null"));
		return this;
	}

	@Override
	public Rows insert(final int position, final Row row) {
		elements.add(position, requireNonNull(row, "The row to insert must not be null"));
		return this;
	}

	@Override
	public Rows remove(final int index) {
		elements.remove(index);
		return this;
	}

	@Override
	public Rows clear() {
		elements.clear();
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + elements.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleRows))
			return false;
		SimpleRows other = (SimpleRows) obj;
		return elements.equals(other.elements);
	}
}