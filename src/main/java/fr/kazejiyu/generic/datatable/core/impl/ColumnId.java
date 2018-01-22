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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import fr.kazejiyu.generic.datatable.core.Column;

/**
 * Identifies uniquely a {@link Column}. Also helps to turn a column
 * into an heterogeneous type-safe container. <br>
 * <br>
 * Instances of this class are strictly immutable and hence, once built,
 * are unconditionally <em>thread-safe</em>.  
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of column's elements.
 */
public final class ColumnId<T> {

	private final Class<T> type;
	
	private final String header;

	public ColumnId(Class<T> type, String header) {
		this.type = requireNonNull(type, "The type of a ColumnId must not be null");
		this.header = requireNonNull(header, "The header of a ColumnId must not be null");
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
		result = prime * result + header.toLowerCase().hashCode();
		result = prime * result + type.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if( obj instanceof ColumnId )
			return this.equals((ColumnId<?>) obj);
		if( obj instanceof ColumnIdDecorator )
			return this.equals(((ColumnIdDecorator<?>) obj).id);
		return false;
	}
	
	public boolean equals(ColumnId<?> other) {
		if (!header.equalsIgnoreCase(other.header()))
			return false;
		return type.equals(other.type());
	}
	
	/**
	 * Creates a new {@code ColumnId} with a specific type and header.
	 * 
	 * @param type
	 * 			The class of the elements contained by the column.
	 * @param header
	 * 			The header of the column.
	 * 
	 * @return a new {@code ColumnId}
	 * 
	 * @param <T> the type of the elements contained by the column
	 */
	public static <T> ColumnId<T> id(Class<T> type, String header) {
		return new ColumnId<>(type, header);
	}
	
	/** 
	 * Wraps a {@code ColumnId} into a new object to ease DSL's build.
	 * 
	 * @param id
	 * 			The column id to wrap.
	 * 
	 * @return a new {@code ColumnId} 
	 */
	public static ColumnOfStringsId s(ColumnId<String> id) {
		return new ColumnOfStringsId(id);
	}
	
	/** 
	 * Wraps several {@code ColumnId}s into a new object to ease DSL's build.
	 * 
	 * @param id
	 * 			The first id.
	 * @param ids
	 * 			The other ids.
	 * 
	 * @return a new {@code ColumnId} 
	 */
	@SafeVarargs
	public static ColumnOfStringsId[] s(ColumnId<String> id, ColumnId<String>... ids) {
		List<ColumnOfStringsId> columns = new ArrayList<>();
		columns.add(new ColumnOfStringsId(id));
		
		for(ColumnId<String> col : ids) 
			columns.add(new ColumnOfStringsId(col));
		
		return columns.toArray(new ColumnOfStringsId[columns.size()]);
	}
	
	/**
	 * Wraps a {@code ColumnId} into a new object to ease DSL's build.
	 * 
	 * @param id
	 * 			The column id to wrap.
	 * 
	 * @return a new {@code ColumnId} 
	 */
	public static <T extends Number> ColumnOfNumbersId<T> n(ColumnId<T> id) {
		return new ColumnOfNumbersId<>(id);
	}
}
