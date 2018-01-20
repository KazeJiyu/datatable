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

import java.util.Collection;

import fr.kazejiyu.generic.datatable.query.And;
import fr.kazejiyu.generic.datatable.query.Where;

/**
 * A specialized {@link Where} aimed to deal with booleans.
 * 
 * @author Emmanuel CHEBBI
 */
public class WhereBool extends SimpleWhere <Boolean> {
	
	/**
	 * Creates a new specialized Where &amp;Boolean&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public WhereBool(final QueryContext context, final String header) {
		super(context, header);
	}
	
	/**
	 * Creates a new specialized Where &amp;Boolean&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param headers
	 * 			The name of the columns to filter.
	 */
	public WhereBool(final QueryContext context, final Collection<String> headers) {
		super(context, headers);
	}

	/**
	 * Adds a filter to keep the rows containing {@code true}.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isTrue() {
		return matchSafe(Boolean::booleanValue);
	}

	/**
	 * Adds a filter to keep the rows containing {@code false}.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isFalse() {
		return matchSafe(bool -> ! bool);
	}
	
}
