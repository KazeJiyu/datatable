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
package fr.kazejiyu.generic.datatable.query;

import java.util.Arrays;
import java.util.Collection;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.query.impl.GlazedSelect;

/**
 * Helper methods to start a new query.
 * 
 * TODO Describe the querying API in details
 * 
 * @author Emmanuel CHEBBI
 */
public interface Query {

	/**
	 * Returns a new query that selects all the columns of a {@link Table}.
	 * @return a new query that selects all the columns of a {@code Table}.
	 */
	public static Select<DataTable> select() {
		return new GlazedSelect();
	}
	
	/**
	 * Returns a new query that selects the columns called {@code headers} of a {@link Table}.
	 * 
	 * @param headers
	 * 			The name of the columns to select in the table.
	 * 
	 * @return a new query that selects the columns called {@code headers} of a {@code Table}.
	 */
	public static Select<DataTable> select(String... headers) {
		return new GlazedSelect(Arrays.asList(headers));
	}
	
	/**
	 * Returns a new query that selects the columns called {@code headers} of a {@link Table}.
	 * 
	 * @param headers
	 * 			The name of the columns to select in the table.
	 * 
	 * @return a new query that selects the columns called {@code headers} of a {@code Table}.
	 */
	public static Select<DataTable> select(Collection <String> headers) {
		return new GlazedSelect(headers);
	}
	
}
