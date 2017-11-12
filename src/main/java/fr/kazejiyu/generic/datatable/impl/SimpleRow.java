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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ca.odell.glazedlists.GlazedLists;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Table;

/**
 * An implementation of {@link Row} that internally uses GlazedLists.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleRow extends ModifiableRow {
	
	/** The table that owns the row */
	private final Table table;
	
	/** The id of the row */
	private final int id;
	
	/** The content of the row */
	private List<Object> elements;
	
	SimpleRow(Table table, int id) {
		this(table, id, Collections.emptyList());
	}
	
	SimpleRow(Table table, int id, List <Object> elements) {
		this.table = table;
		this.id = id;
		this.elements = GlazedLists.eventList(elements);
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
	@SuppressWarnings("unchecked")
	public <T> T get(int column) {
		Class <?> runtimeClass = table.columns().get(column).type();
		return (T) runtimeClass.cast(elements.get(column));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String header) {
		int index = table.columns().indexOf(header);
		return (T) elements.get(index);
	}

	@Override
	Row insert(int position, Object element) {
		this.elements.add(position, element);
		return this;
	}
	
	@Override
	Row remove(int position) {
		this.elements.remove(position);
		return this;
	}

}
