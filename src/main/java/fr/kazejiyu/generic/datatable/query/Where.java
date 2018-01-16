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
package fr.kazejiyu.generic.datatable.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import fr.kazejiyu.generic.datatable.query.impl.WhereBool;
import fr.kazejiyu.generic.datatable.query.impl.WhereNumber;
import fr.kazejiyu.generic.datatable.query.impl.WhereStr;

/**
 * Contribute to a query by adding a new filter. <br>
 * <br>
 * The column(s) on which apply the filter should have been selected thanks to
 * a previous {@link From} instance. <br>
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The runtime type of the elements stored in the column(s) to filter
 * 
 * @see Query Query for further details about the Querying API
 */
public interface Where <T> {
	
	/**
	 * Adds a filter keeping the rows that match {@code predicate}.
	 * 
	 * @param predicate
	 * 			The predicate to test against the table's rows.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #matchSafe(Predicate)
	 */
	public And match(Predicate <T> predicate);
	
	/**
	 * Adds a filter keeping the rows that match {@code predicate}. <br>
	 * <br>
	 * If the row's element is {@code null}, the filter is not applied
	 * and the row is kept.
	 * 
	 * @param predicate
	 * 			The predicate to test against the table's rows.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #match(Predicate)
	 */
	public default And matchSafe(Predicate <T> predicate) {
		return match(item -> item != null && predicate.test(item));
	}
	
	/**
	 * Adds a filter keeping the rows containing an element equal to {@code value}. <br>
	 * <br>
	 * The equality is tested via {@link Objects#equals(Object)}.
	 * 
	 * @param value
	 * 			The value to keep on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #ne(Object)
	 */
	public default And eq(T value) {
		return match(o -> Objects.equals(o, value));
	}
	
	/**
	 * Adds a filter keeping the rows containing an element that is not equal to {@code value}. <br>
	 * <br>
	 * The equality is tested via {@link Objects#equals(Object)}.
	 * 
	 * @param value
	 * 			The value to avoid on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #eq(Object)
	 */
	public default And ne(T value) {
		return match(o -> ! Objects.equals(o, value));
	}
	
	/**
	 * Adds a filter keeping the rows containing {@code null} values.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #isNonNull()
	 */
	public default And isNull() {
		return match(Objects::isNull);
	}
	
	/**
	 * Adds a filter keeping the rows containing non {@code null} values.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #isNull()
	 */
	public default And isNonNull() {
		return match(Objects::nonNull);
	}
	
	/**
	 * Adds a filter keeping the rows containing values that are instances of {@code clazz}.
	 * 
	 * @param clazz
	 * 			The subclass of the values to keep.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public default And isInstanceOf(Class <?> clazz) {
		return match(clazz::isInstance);
	}
	
	/**
	 * Adds a filter keeping the rows which values are contained into {@code elements}.
	 * 
	 * @param elements
	 * 			The elements that must be kept on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #in(Collection)
	 * @see #notIn(Object...)
	 * @see #notIn(Collection)
	 */
	@SuppressWarnings("unchecked")
	public default And in(T... elements) {
		return in(Arrays.asList(elements));
	}
	
	/**
	 * Adds a filter keeping the rows which values are contained into {@code elements}.
	 * 
	 * @param elements
	 * 			The elements that must be kept on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #in(Object...)
	 * @see #notIn(Object...)
	 * @see #notIn(Collection)
	 */
	public default And in(Collection <T> elements) {
		return match(elements::contains);
	}
	
	/**
	 * Adds a filter keeping the rows which values are not contained into {@code elements}.
	 * 
	 * @param elements
	 * 			The elements that must be kept on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #in(Object...)
	 * @see #in(Collection)
	 * @see #notIn(Collection)
	 */
	@SuppressWarnings("unchecked")
	public default And notIn(T... elements) {
		return notIn(Arrays.asList(elements));
	}
	
	/**
	 * Adds a filter keeping the rows which values are not contained into {@code elements}.
	 * 
	 * @param elements
	 * 			The elements that must be kept on the selected columns.
	 * 
	 * @return a {@code And} instance to continue the query.
	 * 
	 * @see #in(Object...)
	 * @see #in(Collection)
	 * @see #notIn(Object...)
	 */
	public default And notIn(Collection <T> elements) {
		return match(elements::contains);
	}
	
	/**
	 * Returns a specialized instance of {@code Where} to ease lambda's writing.
	 * <br><br>
	 * This method makes possible to write:
	 * <br><br>
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A").as(MyCustomClass.class).match(MyCustomClass::myCondition)</pre>
	 *     
	 * instead of :
	 * 
	 * <pre>Query.select()
	 *     .from(table)
	 *     .where("A").match(o -> ((MyCustomClass) o).myCondition())</pre>     
	 * 
	 * @param clazz
	 * 			The type of the elements contained by the column to filter.
	 * 
	 * @return a specialized instance of {@code Where}.
	 * 
	 * @param <N> The type of the elements to filter.
	 */
	public <N> Where <N> as(Class <N> clazz);

	/**
	 * Returns an instance of {@code Where} specialized to deal with strings.
	 * @return an instance of {@code Where} specialized to deal with strings.
	 */
	public WhereStr asStr();
	
	/**
	 * Returns an instance of {@code Where} specialized to deal with booleans.
	 * @return an instance of {@code Where} specialized to deal with booleans.
	 */
	public WhereBool asBool();
	
	/**
	 * Returns an instance of {@code Where} specialized to deal with numbers.
	 * @return an instance of {@code Where} specialized to deal with booleans.
	 */
	public WhereNumber asNumber();
}
