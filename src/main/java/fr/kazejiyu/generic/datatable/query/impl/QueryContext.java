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

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;

/**
 * The context of a query.
 * 
 * @author Emmanuel CHEBBI
 */
class QueryContext {
	
	/** The Table that contains the rows to filter. */
	public Table table;
	
	/** The columns that will be returned by the final query. */
	public final LinkedHashSet<String> selectedHeaders = new LinkedHashSet<>();
	
	/** The ids of the columns that will be returned by the final query. */
	public final LinkedHashSet<ColumnId<?>> selectedIds = new LinkedHashSet<>();
	
	/** The filters to apply on the table to obtain the desired result. */
	public final Filters filters = new Filters();
	
}
