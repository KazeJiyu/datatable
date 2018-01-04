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
package fr.kazejiyu.generic.datatable.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import fr.kazejiyu.generic.datatable.And;
import fr.kazejiyu.generic.datatable.Where;

/**
 * An implement of {@link Where} able to deal with {@link DataTable}s.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the instances to filter.
 */
class GlazedWhere <T> implements Where <T> {

	/** The context of the query */
	protected final QueryContext context;
	
	/** The headers of the columns to filter */
	protected final Collection <String> headers;
	
	/**
	 * Prepare to filter the column called {@code header}.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public GlazedWhere(final QueryContext context, final String header) {
		this(context, Arrays.asList(header));
	}

	/**
	 * Prepare to filter the columns which name is in {@code headers}.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param headers
	 * 			The name of the column to filter.
	 */
	public GlazedWhere(final QueryContext context, final Collection <String> headers) {
		this.context = context;
		this.headers = headers;
	}

	@Override
	public And match(final Predicate <T> predicate) {
		context.filters.add(new Filter<T>(headers, predicate));
		return new GlazedAnd(context);
	}
	
	private static class MappedWhere <T,N> extends GlazedWhere <N> {
		
		private Function<T, N> mapper;

		public MappedWhere(QueryContext context, Collection<String> headers, Function <T,N> mapper) {
			super(context, headers);
			this.mapper = mapper;
			System.out.println("map");
		}

		@Override
		public And match(final Predicate <N> predicate) {
			System.out.println("Map predicate !");
			context.filters.add(new Filter<T>(headers, o-> predicate.test(mapper.apply(o))));
			return new GlazedAnd(context);
		}
	}
	
	@Override
	public <N> Where<N> map(Function<T, N> mapper) {
		return new MappedWhere<T,N>(context, headers, mapper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <N> Where<N> as(final Class <N> clazz) {
		if( clazz == String.class )
			return (Where <N>) asStr();
		
		else if( clazz == Number.class ) 
			return (Where <N>) asNumber();
		
		else if( clazz == Boolean.class ) 
			return (Where <N>) asBool();
		
		return new GlazedWhere<>(context, headers);
	}

	@Override
	public WhereStr asStr() {
		return new WhereStr(context, headers);
	}

	@Override
	public WhereBool asBool() {
		return new WhereBool(context, headers);
	}

	@Override
	public WhereNumber asNumber() {
		return new WhereNumber(context, headers);
	}
}
