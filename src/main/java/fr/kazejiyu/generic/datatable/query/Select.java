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

import java.util.Collection;

import fr.kazejiyu.generic.datatable.core.Table;

/**
 * Last query's statement ; makes able to choose the columns of the original table to keep.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Select {
	
	/**
	 * Returns a new query that selects all the columns of a {@link Table}.
	 * @return a new query that selects all the columns of a {@code Table}.
	 */
	public Table select();
	
	/**
	 * Returns a new query that selects the columns called {@code headers} of a {@link Table}.
	 * 
	 * @param headers
	 * 			The name of the columns to select in the table.
	 * 
	 * @return a new query that selects the columns called {@code headers} of a {@code Table}.
	 */
	public Table select(String... headers);
	
	/**
	 * Returns a new query that selects the columns called {@code headers} of a {@link Table}.
	 * 
	 * @param headers
	 * 			The name of the columns to select in the table.
	 * 
	 * @return a new query that selects the columns called {@code headers} of a {@code Table}.
	 */
	public Table select(Collection <String> headers);
	
}
