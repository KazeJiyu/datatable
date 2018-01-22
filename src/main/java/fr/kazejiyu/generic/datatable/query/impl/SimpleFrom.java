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

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfNumbersId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfStringsId;
import fr.kazejiyu.generic.datatable.query.From;
import fr.kazejiyu.generic.datatable.query.Query;
import fr.kazejiyu.generic.datatable.query.Where;

/**
 * An implementation of {@link From} able to deal with {@link Table}s.
 * 
 * @author Emmanuel CHEBBI
 */
public class SimpleFrom implements From {

	/** The context of the query. */
	private final QueryContext context = new QueryContext();

	/**
	 * Starts the construction of a new query. <br>
	 * <br>
	 * <strong>Caution</strong>: should not be called directly. The creation
	 * of a new instance should be delegated to {@link Query#from(Table)}.
	 * 
	 * @param table
	 * 			The table to query.
	 * 
	 * @see Query#from(Table) Query.from(Table) to start a new query
	 */
	public SimpleFrom(final Table table) {
		this.context.table = table;
	}
	
	@Override
	public Where<?> where() {
		return where(context.table.columns().headers());
	}
	
	@Override
	public Where<?> where(final String... headers) {
		return where(asList(headers));
	}
	
	@Override
	public Where<?> where(final Collection<String> headers) {
		return new SimpleWhere<>(context, headers);
	}
	
	@Override
	public <T> Where<T> where(ColumnId<T> id) {
		return new SimpleWhere<>(context, id.header());
	}
	
	@Override
	public WhereStr where(ColumnOfStringsId id) {
		return new WhereStr(context, id.header());
	}
	
	@Override
	public WhereStr where(ColumnOfStringsId[] ids) {
		return new WhereStr(context, headersOf(ids));
	}
	
	@Override
	public WhereNumber where(ColumnOfNumbersId<?>[] ids) {
		return new WhereNumber(context, headersOf(ids));
	}
	
	private LinkedHashSet<String> headersOf(ColumnOfStringsId[] ids) {
		LinkedHashSet<String> headers = new LinkedHashSet<>();
		
		for(ColumnOfStringsId id : ids)
			headers.add(id.header());
		
		return headers;
	}
	
	private LinkedHashSet<String> headersOf(ColumnOfNumbersId<?>[] ids) {
		LinkedHashSet<String> headers = new LinkedHashSet<>();
		
		for(ColumnOfNumbersId<?> id : ids)
			headers.add(id.header());
		
		return headers;
	}
	
	@Override
	public WhereNumber where(ColumnOfNumbersId<?> id) {
		return new WhereNumber(context, id.header());
	}
}
