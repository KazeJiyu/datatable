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
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

/**
 * Last query's statement ; makes able to choose the columns of the original table to keep.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Select {
	
	/**
	 * Returns a new table resulting of the previously built query. <br>
	 * <br>
	 * This new table has the same columns as its original, and only differs
	 * from its rows.
	 * 
	 * @return a new table resulting of the previously built query.
	 */
	Table select();
	
	/**
	 * Returns a new table resulting of the previously built query. <br>
	 * <br>
	 * This new table only contains the columns of the original table which
	 * name is specified in {@code headers}. The order is relevant.
	 * 
	 * @param headers
	 * 			The name of the columns to keep in the final table.
	 * 
	 * @return a new table resulting of the previously built query.
	 * 
	 * @throws HeaderNotFoundException if one of the specified header does not match any column name.
	 */
	Table select(String... headers);
	
	/**
	 * Returns a new table resulting of the previously built query. <br>
	 * <br>
	 * This new table only contains the columns of the original table which
	 * name is specified in {@code headers}. The order is relevant.
	 * 
	 * @param headers
	 * 			The name of the columns to keep in the final table.
	 * 			Must not be {@code null}.
	 * 
	 * @return a new table resulting of the previously built query.
	 * 
	 * @throws HeaderNotFoundException if one of the specified header does not match any column name.
	 */
	Table select(Collection <String> headers);
	
	/**
	 * Returns a new table resulting of the previously built query. <br>
	 * <br>
	 * This new table only contains the column identified by {@code id}.
	 * 
	 * @param id
	 * 			The name of the columns to keep in the final table.
	 * 			Must not be {@code null}.
	 * 
	 * @return a new table resulting of the previously built query.
	 * 
	 * @throws ColumnIdNotFoundException if id does not match any column
	 */
	Table select(ColumnId<?> id);
	
	/**
	 * Returns a new table resulting of the previously built query. <br>
	 * <br>
	 * This new table only contains the columns identified by the arguments.
	 * The order is relevant.
	 *
	 * @param first
	 * 			The id of first the column to keep.
	 * 			Must not be {@code null}.
	 * @param nexts
	 * 			The ids of the others columns to keep.
	 * 
	 * @return a new table resulting of the previously built query.
	 * 
	 * @throws ColumnIdNotFoundException if id does not match any column
	 */
	Table select(ColumnId<?> first, ColumnId<?>... nexts);
}
