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
 * Contribute to a query by selecting one or more column to filter. <br>
 * <br>
 * This class is similar to {@link From}, except that the "where" method 
 * is renamed "and" in order to make the query's DSL more consistent.
 * 
 * @author Emmanuel CHEBBI
 * 
 * @see Query Query for further details about the Querying API
 */
public interface And extends Select {
	
	/**
	 * Prepares to apply a filter on all the columns of the table.
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	public Where<?> and();
	
	/**
	 * Prepares to apply a filter on the specified columns.
	 * 
	 * @param headers
	 * 			The columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	public Where<?> and(String... headers);
	
	/**
	 * Prepares to apply a filter on the specified columns.
	 * 
	 * @param headers
	 * 			The columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on all the columns of the table.
	 */
	public Where<?> and(Collection <String> headers);
	
	/**
	 * Prepares to apply a filter on the specified column, keeping its type. <br>
	 * <br>
	 * This method should be favored over {@link #and(String...)}
	 * because it keeps the type of the element of the column and hence
	 * avoids casting in subsequent methods.
	 * 
	 * @param id
	 * 			The id of the column on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on the specified column.
	 * 
	 * @param <T> The runtime type of the elements of the column
	 */
	<T> Where<T> and(ColumnId<T> id);
	
	/**
	 * Prepares to apply a filter on the specified column of Strings. <br>
	 * <br>
	 * This method provides a query tailored to filter Strings, and hence should
	 * be preferred when dealing with a column full of Strings. <br>
	 * <br>
	 * A {@code ColumnOfStringsId} instance can be built via {@link ColumnId#s(ColumnId)}.
	 * 
	 * @param id
	 * 			The id of the column on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on a column of Strings.
	 */
	WhereStr and(ColumnOfStringsId id);
	
	/**
	 * Prepares to apply a filter on the specified column of Numbers. <br>
	 * <br>
	 * This method provides a query tailored to filter Numbers, and hence should
	 * be preferred when dealing with a column full of Numbers. <br>
	 * <br>
	 * A {@code ColumnOfNumbersId} instance can be built via {@link ColumnId#n(ColumnId)}.
	 * 
	 * @param id
	 * 			The id of the column on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on a column of Numbers.
	 */
	WhereNumber and(ColumnOfNumbersId<?> id);
	
	/**
	 * Prepares to apply a filter on the specified columns of Strings. <br>
	 * <br>
	 * This method provides a query tailored to filter Strings, and hence should
	 * be preferred when dealing with columns full of Strings. <br>
	 * <br>
	 * A {@code ColumnOfStringsId} array can be built via {@link ColumnId#s(ColumnId, ColumnId...)}.
	 * 
	 * @param ids
	 * 			The ids of the columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on several columns of Strings.
	 */
	WhereStr and(ColumnOfStringsId[] ids);
	
	/**
	 * Prepares to apply a filter on the specified columns of Numbers. <br>
	 * <br>
	 * This method provides a query tailored to filter Numbers, and hence should
	 * be preferred when dealing with columns full of Numbers. <br>
	 * <br>
	 * A {@code ColumnOfStringsId} array can be built via {@link ColumnId#s(ColumnId, ColumnId...)}.
	 * 
	 * @param ids
	 * 			The ids of the columns on which apply a filter.
	 * 
	 * @return a query set up to apply a filter on several columns of Numbers.
	 */
	WhereNumber and(ColumnOfNumbersId<?>[] ids);
}
