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
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Rows;

public class SimpleRows implements Rows {
	
	private List<Row> elements;
	
	SimpleRows() {
		elements = new BasicEventList<>();
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

	@Override
	public Rows add(Row row) {
		elements.add(row);
		return this;
	}

	@Override
	public Rows insert(int position, Row row) {
		elements.add(position, row);
		return this;
	}

	@Override
	public Rows remove(int index) {
		elements.remove(index);
		return this;
	}

}
