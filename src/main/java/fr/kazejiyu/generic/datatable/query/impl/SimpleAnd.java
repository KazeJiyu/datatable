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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfNumbersId;
import fr.kazejiyu.generic.datatable.core.impl.ColumnOfStringsId;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.query.And;
import fr.kazejiyu.generic.datatable.query.Where;

/**
 * An implementation of {@link And} able to deal with {@link Table}s.
 * 
 * @author Emmanuel CHEBBI
 */
class SimpleAnd implements And {

	/** The context of the query. */
	private final QueryContext context;

	/**
	 * Creates a new And.
	 * 
	 * @param context
	 * 			The context of the query.
	 */
	public SimpleAnd(final QueryContext context) {
		this.context = context;
	}
	
	@Override
	public Where<?> and() {
		return and(context.table.columns().headers());
	}

	@Override
	public Where<?> and(String... headers) {
		return and(Arrays.asList(headers));
	}
	
	@Override
	public Where<?> and(Collection <String> headers) {
		return new SimpleWhere<>(context, headers);
	}
	
	@Override
	public <T> Where<T> and(ColumnId<T> id) {
		return new SimpleWhere<>(context, id.header());
	}
	
	@Override
	public WhereStr and(ColumnOfStringsId id) {
		return new WhereStr(context, id.header());
	}
	
	@Override
	public WhereNumber and(ColumnOfNumbersId id) {
		return new WhereNumber(context, id.header());
	}

	@Override
	public Table select() {
		return select(context.table.columns().headers());
	}

	@Override
	public Table select(String... headers) {
		return select(asList(headers));
	}

	@Override
	public Table select(Collection<String> headers) {
		LinkedHashSet<String> selectedHeaders = new LinkedHashSet<>();
		selectedHeaders.addAll(headers);
		return context.table.filter(context.filters, selectedHeaders);
	}

}
