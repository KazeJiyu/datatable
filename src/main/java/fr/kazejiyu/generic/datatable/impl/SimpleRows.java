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

import java.util.Iterator;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Rows;
import fr.kazejiyu.generic.datatable.Table;

/**
 * An implementation of {@link Rows}.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleRows implements Rows {
	
	/** The table that owns the rows. */
	private final Table table;
	
	/** The content of the rows. */
	private final EventList <Row> elements = new BasicEventList<>();
	
	/**
	 * Creates the rows of {@code table}.
	 * 
	 * @param table
	 * 			The table that owns the rows.
	 */
	SimpleRows(final Table table) {
		this.table = table;
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
	public int size() {
		return elements.size();
	}

	@Override
	public Row get(int index) {
		return elements.get(index);
	}
	
	private int nextId() {
		return isEmpty() ? 0 : last().id() + 1;
	}
	
	@Override
	public Rows create(final List <Object> elements) {
		return add(new SimpleRow(table, nextId(), elements));
	}

	@Override
	public Rows add(final Row row) {
		elements.add(row);
		return this;
	}

	@Override
	public Rows insert(final int position, final Row row) {
		elements.add(position, row);
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
}
