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

import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.core.impl.DataTable;

/**
 * The context of a query.
 * 
 * @author Emmanuel CHEBBI
 */
class QueryContext {
	
	/** The DataTable that contains the rows to filter. */
	public final DataTable table;
	
	/** The columns that will be returned by the final query. */
	public final LinkedHashSet<String> selectedHeaders;
	
	/** The filters to apply on the table to obtain the desired result. */
	public final Filters filters;

	/**
	 * Creates a new query's context.
	 * 
	 * @param table
	 * 			The table on which process the query.
	 * @param selectedHeaders
	 * 			The headers selected by the query, will be returned at the query's end.
	 */
	public QueryContext(final DataTable table, final LinkedHashSet<String> selectedHeaders) {
		this.table = table;
		this.selectedHeaders = selectedHeaders;
		this.filters = new Filters();
	}
	
}