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
package fr.kazejiyu.generic.datatable.query.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.query.From;
import fr.kazejiyu.generic.datatable.query.Select;

/**
 * An implementation of {@link Select} able to deal with {@link DataTable}s.
 * 
 * @author Emmanuel CHEBBI
 */
public class GlazedSelect implements Select <DataTable> {

	/** The name of the columns to return at the end of the query. */
	private final LinkedHashSet <String> selectedHeaders;
	
	/**
	 * Initialize the query by selecting all the columns of the table.
	 */
	public GlazedSelect() {
		this(Collections.emptyList());
	}
	
	/**
	 * Initialize the query by selecting a unique column from the table.
	 * 
	 * @param header
	 * 			The name of the column to return at the end of the query.
	 */
	public GlazedSelect(final String header) {
		this(Arrays.asList(header));
	}
	
	/**
	 * Initialize the query by selecting a unique column from the table.
	 * 
	 * @param headers
	 * 			The name of the columns to return at the end of the query.
	 */
	public GlazedSelect(final Collection <String> headers) {
		this.selectedHeaders = new LinkedHashSet<>(headers);
	}

	@Override
	public From from(final DataTable table) {
		if( selectedHeaders.isEmpty() )
			return new GlazedFrom(new QueryContext(table, table.columns().headers()));
		
		return new GlazedFrom(new QueryContext(table, selectedHeaders));
	}
	
}
