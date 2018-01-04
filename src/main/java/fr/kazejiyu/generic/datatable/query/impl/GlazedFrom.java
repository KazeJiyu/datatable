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

import java.util.Arrays;
import java.util.Collection;

import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.query.From;
import fr.kazejiyu.generic.datatable.query.Where;

/**
 * An implementation of {@link From} able to deal with {@link DataTable}s.
 * 
 * @author Emmanuel CHEBBI
 */
class GlazedFrom implements From {

	/** The context of the query. */
	private final QueryContext context;

	/**
	 * Creates a new From.
	 * 
	 * @param context
	 * 			The context of the query.
	 */
	public GlazedFrom(final QueryContext context) {
		this.context = context;
	}
	
	@Override
	public Where<?> where() {
		return where(context.table.columns().headers());
	}

	@Override
	public Where<?> where(final String... headers) {
		return where(Arrays.asList(headers));
	}
	
	@Override
	public Where<?> where(final Collection <String> headers) {
		return new GlazedWhere<>(context, headers);
	}
}
