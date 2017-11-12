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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.From;
import fr.kazejiyu.generic.datatable.Select;
import fr.kazejiyu.generic.datatable.Table;

public class GlazedSelect implements Select {

	private final LinkedHashSet <String> selectedHeaders;
	
	public GlazedSelect() {
		this(Collections.emptyList());
	}
	
	public GlazedSelect(String header) {
		this(Arrays.asList(header));
	}
	
	public GlazedSelect(Collection <String> headers) {
		this.selectedHeaders = new LinkedHashSet<>(headers);
	}

	@Override
	public From from(Table table) {
//		return new GlazedFrom(
//				new QueryContext(table, selectedHeaders));
		return null;
	}

	@Override
	public From from(DataTable table) {
		if( selectedHeaders.isEmpty() )
			return new GlazedFrom(new QueryContext(table, table.columns().headers()));
		
		return new GlazedFrom(new QueryContext(table, selectedHeaders));
	}
	
}
