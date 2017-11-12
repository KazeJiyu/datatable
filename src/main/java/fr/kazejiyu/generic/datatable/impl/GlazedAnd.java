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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import fr.kazejiyu.generic.datatable.And;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Table;
import fr.kazejiyu.generic.datatable.Where;

/**
 * An implement of {@link And} able to deal with {@link DataTable}s.
 * 
 * @author Emmanuel CHEBBI
 */
public class GlazedAnd implements And {

	/** The context of the query */
	private final QueryContext context;

	public GlazedAnd(QueryContext context) {
		this.context = context;
	}
	
	@Override
	public Where<?> and() {
		return and(context.table.columns().headers());
	}

	@Override
	public Where<?> and(String... headers) {
		return and(Arrays.asList(headers));
	}
	
	@Override
	public Where<?> and(Collection <String> headers) {
		return new GlazedWhere<>(context, headers);
	}

	@Override
	public Table queryTable() {
		EventList <Row> rows = context.table.internal();
		FilterList <Row> filtered = new FilterList<>(rows, context.filters);
	
		List <Integer> indexes = indexes(context);
		Table queried = emptyTable(context);
		
		for( Row row : filtered ) {
			List <Object> elements = new ArrayList<>();
			
			for( int index : indexes )
				elements.add(row.get(index));
			
			queried.rows().create(elements);
		}
		
		filtered.dispose();	// Avoid possible memory leaks
		return queried;
	}
	
	/** @return the indexes of the selected columns */
	private List <Integer> indexes(QueryContext context) {
		return context.selectedHeaders.stream()
			   .map(context.table.columns()::indexOf)
			   .collect(Collectors.toList());
	}
	
	/** @return a new empty Table with the selected columns */
	private Table emptyTable(QueryContext context) {
		Table empty = new DataTable();
		
		for( String header : context.selectedHeaders ) 
			empty.columns().create(context.table.columns().get(header).type(), header);
		
		return empty;
	}

}
