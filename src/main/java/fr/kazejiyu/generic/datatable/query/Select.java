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

import fr.kazejiyu.generic.datatable.core.Table;

/**
 * First query's statement ; makes able to choose the table to filter.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the {@link Table} to query.
 */
public interface Select <T extends Table> {

	/**
	 * Choose the table to filter.
	 *  
	 * @param table
	 * 			The table to filter.
	 * 
	 * @return the next query's statement
	 */
	public From from(T table);
	
}
