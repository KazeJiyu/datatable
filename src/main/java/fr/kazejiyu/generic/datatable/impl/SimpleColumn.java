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

import fr.kazejiyu.generic.datatable.Column;
import fr.kazejiyu.generic.datatable.Table;

class SimpleColumn <T> implements Column <T> {

	private final Table table;
	private final String header;
	private final Class <T> type;
	
	SimpleColumn(Class <T> type, Table table, String header) {
		this.type = type;
		this.table = table;
		this.header = header;
	}
	
	@Override
	public Iterator <T> iterator() {
		return new ColumnIterator<>(table.rows(), table.columns().indexOf(header));
	}

	@Override
	public String header() {
		return header;
	}

	@Override
	public int size() {
		return table.rows().size();
	}

	@Override
	public Class <T> type() {
		return type;
	}

	@Override
	public T get(int row) {
		int column = table.columns().indexOf(header);
		return table.rows().get(row).get(column);
	}

}
