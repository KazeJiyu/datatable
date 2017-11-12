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

import java.util.Collection;

import fr.kazejiyu.generic.datatable.And;
import fr.kazejiyu.generic.datatable.Where;

/**
 * A specialized {@link Where} aimed to deal with strings.
 * 
 * @author Emmanuel CHEBBI
 */
public class WhereStr extends GlazedWhere <String> {
	
	/**
	 * Creates a new specialized Where &amp;String&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public WhereStr(QueryContext context, String header) {
		super(context, header);
	}
	
	/**
	 * Creates a new specialized Where &amp;String&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public WhereStr(QueryContext context, Collection<String> headers) {
		super(context, headers);
	}

	/**
	 * Adds a filter to keep the rows containing an empty string.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isEmpty() {
		return matchSafe(String::isEmpty);
	}

	/**
	 * Adds a filter to keep the rows containing a string that is equal to {@code expected}
	 * in a case-insensitive way
	 * 
	 * @param expected
	 * 			The value to keep
	 * .
	 * @return a {@code And} instance to continue the query.
	 */
	public And equalsIgnoreCase(String expected) {
		return matchSafe(str -> str.equalsIgnoreCase(expected));
	}

	/**
	 * Adds a filter to keep the rows containing lower case strings.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isInLowerCase() {
		return matchSafe(str -> str.equals(str.toLowerCase()));
	}

	/**
	 * Adds a filter to keep the rows containing upper case strings.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isInUpperCase() {
		return matchSafe(str -> str.equals(str.toUpperCase()));
	}

	/**
	 * Adds a filter to keep the rows containing string that contain {@code sub}.
	 * 
	 * @param sub
	 * 			The sub-string that must be contained. Must not be {@code null}.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And contains(String sub) {
		return matchSafe(str -> str.contains(sub));
	}

	/**
	 * Adds a filter to keep the rows containing string that starts with {@code start}.
	 * 
	 * @param start
	 * 			Must be at the begin of the string. Must not be {@code null}.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And startsWith(String start) {
		return matchSafe(str -> str.startsWith(start));
	}

	/**
	 * Adds a filter to keep the rows containing string that ends with {@code end}..
	 * 
	 * @param end
	 * 			Must be at the end of the string. Must not be {@code null}.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And endsWith(String end) {
		return matchSafe(str -> str.endsWith(end));
	}
	
}
