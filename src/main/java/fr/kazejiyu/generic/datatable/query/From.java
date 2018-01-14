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

import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfNumbersId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfStringsId;
import fr.kazejiyu.generic.datatable.query.impl.WhereNumber;
import fr.kazejiyu.generic.datatable.query.impl.WhereStr;

/**
 * Contribute to a query by selecting one or more column to filter.
 * <br><br>
 * See {@link Query} for further details about the Querying API.
 * <br><br>
 * The DSL defined by the class is closed to the SQL "FROM" clause. 
 * 
 * @author Emmanuel CHEBBI
 */
public interface From {
	
	/**
	 * Prepares to apply a filter on all the columns of the table.
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	Where<?> where();
	
	/**
	 * Prepares to apply a filter on the specified columns.
	 * <br><br>
	 * For instance, the code :
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A", "B").asNumber().isPositive();</pre>
	 * It is semantically equal to:
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A").asNumber().isPositive();
	 *     .where("B").asNumber().isPositive();</pre>
	 * 
	 * @param headers
	 * 			The columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	Where<?> where(String... headers);
	
	/**
	 * Prepares to apply a filter on the specified columns.
	 * <br><br>
	 * For instance, the code :
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A", "B").asNumber().isPositive();</pre>
	 * It is semantically equal to:
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A").asNumber().isPositive();
	 *     .where("B").asNumber().isPositive();</pre>
	 * 
	 * @param headers
	 * 			The columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	Where<?> where(Collection<String> headers);
	
	<T> Where<T> where(ColumnId<T> id);
	
	WhereStr where(ColumnOfStringsId id);

	WhereNumber where(ColumnOfNumbersId id);

	WhereStr where(ColumnOfStringsId[] ids);

	WhereNumber where(ColumnOfNumbersId[] ids);
}
