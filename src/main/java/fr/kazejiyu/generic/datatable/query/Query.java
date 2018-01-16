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

import static java.util.Objects.requireNonNull;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.query.impl.SimpleFrom;

/**
 * Helper method to start a new query.
 * 
 * TODO Describe the querying API in details
 * 
 * @author Emmanuel CHEBBI
 */
public interface Query {
	
	/**
	 * Starts a new query to filter the content of {@code table}.
	 * 
	 * @param table
	 * 			The table to query. Must not be {@code null}.
	 * 
	 * @return an objet making able to continue the query
	 * 
	 * @throws NullPointerException if table == null
	 */
	public static From from(Table table) {
		requireNonNull(table, "Cannot query a null table");
		return new SimpleFrom(table);
	}
	
}
