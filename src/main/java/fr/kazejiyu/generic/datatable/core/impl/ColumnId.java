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
package fr.kazejiyu.generic.datatable.core.impl;

import fr.kazejiyu.generic.datatable.core.Column;

/**
 * Identifies uniquely a {@link Column}. Also helps to turn a column
 * into an heterogeneous type-safe container.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of column's elements.
 */
public final class ColumnId<T> {
	
	private final Class<T> type;
	
	private final String header;

	public ColumnId(Class<T> type, String header) {
		super();
		this.type = type;
		this.header = header;
	}

	/** @return the type of column's elements */
	public Class<T> type() {
		return this.type;
	}
	
	/** @return the header of the column */
	public String header() {
		return this.header;
	}
	
	/** @return a new {@code ColumnId} */
	public static <T> ColumnId<T> id(Class<T> type, String header) {
		return new ColumnId<>(type, header);
	}
}
