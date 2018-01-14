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

import java.util.ArrayList;
import java.util.List;

import fr.kazejiyu.generic.datatable.core.Column;

/**
 * Identifies uniquely a {@link Column}. Also helps to turn a column
 * into an heterogeneous type-safe container.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of column's elements.
 */
public class ColumnId<T> {

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((type == null) ? 0 : type.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnId<?> other = (ColumnId<?>) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.getName().equals(other.type.getName()))
			return false;
		return true;
	}
	
	/** @return a new {@code ColumnId} */
	public static <T> ColumnId<T> id(Class<T> type, String header) {
		return new ColumnId<>(type, header);
	}
	
	/** @return a new {@code ColumnId} */
	public static ColumnOfStringsId s(ColumnId<String> id) {
		return new ColumnOfStringsId(id);
	}
	
	/** @return a new {@code ColumnId} */
	@SafeVarargs
	public static ColumnOfStringsId[] s(ColumnId<String> id, ColumnId<String>... ids) {
		List<ColumnOfStringsId> columns = new ArrayList<>();
		columns.add(new ColumnOfStringsId(id));
		
		for(ColumnId<String> col : ids) 
			columns.add(new ColumnOfStringsId(col));
		
		return columns.toArray(new ColumnOfStringsId[columns.size()]);
	}
	
	/** @return a new {@code ColumnId} */
	public static ColumnOfNumbersId n(ColumnId<? extends Number> id) {
		return new ColumnOfNumbersId(id);
	}
}
