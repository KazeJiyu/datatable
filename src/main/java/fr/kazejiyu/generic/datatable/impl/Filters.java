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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import ca.odell.glazedlists.matchers.Matcher;
import fr.kazejiyu.generic.datatable.Row;

/**
 * An aggregation of {@link Filter}s used to match a {@link Row}. <br>
 * <br>
 * This way, a {@link Row} can be checked against multiple values.
 * 
 * @author Emmanuel CHEBBI
 */
public class Filters implements Matcher<Row> {

	/** The matchers to check against each table's row. */
	private final List<Filter<?>> matchers = new ArrayList<>();
	
	/**
	 * Adds a new filter.
	 * 
	 * @param matcher
	 * 			The filter to add. Must not be {@code null}
	 * 
	 * @return the current instance to enable method chaining.
	 * 
	 * @throws NullPointerException if {@code matcher} is null.
	 */
	public Filters add(final Filter <?> matcher) {
		matchers.add(requireNonNull(matcher));
		return this;
	}

	@Override
	public boolean matches(final Row row) {
		for( Filter <?> filter : matchers ) 
			for( String header : filter.headers() )
				if( ! filter.matches(row.get(header)) )
					return false;
		
		return true;
	}
}
