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
package fr.kazejiyu.generic.datatable.core;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import fr.kazejiyu.generic.datatable.exceptions.InconsistentRowSizeException;

/**
 * An ordered collection of {@link Row}s that belongs to a {@link Table}. <br>
 * <br>
 * See {@link fr.kazejiyu.generic.datatable.core} for further details.
 * 
 * @author Emmanuel CHEBBI
 */
public interface Rows extends Iterable <Row> {

	/** @return the number of rows in the table */
	int size();
	
	/** @return whether the collection is empty */
	boolean isEmpty();
	
	/**
	 * Returns the first row of the table.
	 * @return the first row of the table.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty()
	 * 
	 * @see #last()
	 * @see #get(int)
	 */
	public default Row first() {
		return get(0);
	}
	
	/**
	 * Returns the last row of the table.
	 * @return the last row of the table.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty()
	 * 
	 * @see #first()
	 * @see #get(int)
	 */
	public default Row last() {
		return get(size() - 1);
	}
	
	/**
	 * Returns the row located at {@code index}.
	 * 
	 * @param index
	 * 			The index of the row.
	 * 
	 * @return the row located at {@code index}.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || (index &lt; 0 || size &lt;= index)
	 * 
	 * @see #first()
	 * @see #last()
	 */
	Row get(int index);
	
	/** @return a Stream of all the rows of the table. */
	Stream<Row> stream();
	
	/**
	 * Creates an empty row then adds it to the table.
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws InconsistentRowSizeException if ! columns.isEmpty()
	 * 
	 * @see #create(List)
	 * @see #create(Object...)
	 */
	default Rows create() {
		return create(new ArrayList<>());
	}
	
	/**
	 * Creates then adds a new row.
	 * 
	 * @param row
	 * 			The elements of the new row. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws ClassCastException if any row's element is not of the type expected by its column.
	 * @throws NullPointerException if {@code row} == null
	 * @throws InconsistentRowSizeException if {@code row.length} != {@code columns().size()}
	 * 
	 * @see #create()
	 * @see #create(List)
	 */
	default Rows create(Object... row) {
		return create(asList(row));
	}
	
	/**
	 * Creates then adds a new row.
	 * 
	 * @param row
	 * 			The elements of the new row. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws ClassCastException if any row's element is not of the type expected by its column
	 * @throws NullPointerException if {@code row} == {@code null}
	 * @throws InconsistentRowSizeException if {@code row.size()} != {@code columns().size()}
	 * 
	 * @see #add(Row)
	 * @see #create()
	 * @see #create(Object...)
	 */
	Rows create(List<Object> row);
	
	/**
	 * Adds a new row to the table.
	 * 
	 * @param row
	 * 			The new row.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @see #create(List)
	 */
	Rows add(Row row);
	
	/**
	 * Inserts a new row at the specified location.
	 * 
	 * @param position
	 * 			The location of the new row.
	 * @param row
	 * 			The new row. Must not be {@code null}.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws NullPointerException if {@code row} is {@code null}.
	 * @throws 
	 */
//	Rows insert(int position, Row row);
	
	/**
	 * Removes a row from the table.
	 * 
	 * @param index
	 * 			The index of the row to remove.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 * 
	 * @throws IndexOutOfBoundsException if isEmpty || (index &lt; 0 || size &lt;= index)
	 */
	Rows remove(int index);
	
	/**
	 * Removes all the rows.
	 * 
	 * @return a reference to the instance to enable method chaining.
	 */
	Rows clear();	
}
