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
 * A specialized {@link Where} aimed to deal with numbers.
 * 
 * @author Emmanuel CHEBBI
 */
public class WhereNumber extends SimpleWhere <Number> {
	
	/**
	 * Creates a new specialized Where &amp;Number&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public WhereNumber(final QueryContext context, final String header) {
		super(context, header);
	}
	
	/**
	 * Creates a new specialized Where &amp;Number&amp;.
	 * 
	 * @param context
	 * 			The context of the query.
	 * @param header
	 * 			The name of the column to filter.
	 */
	public WhereNumber(final QueryContext context, final Collection<String> headers) {
		super(context, headers);
	}

	/**
	 * Adds a filter to keep the rows containing 0.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isZero() {
		return matchSafe(n -> n.doubleValue() == 0);
	}

	/**
	 * Adds a filter to keep the rows containing a positive number.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isPositive() {
		return matchSafe(n -> n.doubleValue() > 0);
	}

	/**
	 * Adds a filter to keep the rows containing a negative number.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isNegative() {
		return matchSafe(n -> n.doubleValue() < 0);
	}

	/**
	 * Adds a filter to keep the rows containing an even number.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isEven() {
		return matchSafe(n -> n.doubleValue() % 2 == 0);
	}

	/**
	 * Adds a filter to keep the rows containing an odd number.
	 * @return a {@code And} instance to continue the query.
	 */
	public And isOdd() {
		return matchSafe(n -> n.doubleValue() % 2 != 0);
	}

	/**
	 * Adds a filter to keep the rows containing a number comprised between {@code min} and {@code max}.
	 * 
	 * @param min
	 * 			The lower bound of the range.
	 * @param max
	 * 			The upper bound of the range.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And inRange(double min, double max) {
		return matchSafe(n -> min <= n.doubleValue() && n.doubleValue() <= max);
	}

	/**
	 * Adds a filter to keep the rows containing a number lower than {@code b}.
	 * 
	 * @param b
	 * 			Must not be exceeded or equaled.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And lt(double b) {
		return matchSafe(a -> a.doubleValue() < b);
	}

	/**
	 * Adds a filter to keep the rows containing a number lower than or equal to {@code b}.
	 * 
	 * @param b
	 * 			Must not be exceeded.
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And le(double b) {
		return matchSafe(a -> a.doubleValue() <= b);
	}

	/**
	 * Adds a filter to keep the rows containing a number greater than {@code b}.
	 * 
	 * @param b
	 * 			The lower bound
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And gt(double b) {
		return matchSafe(a -> a.doubleValue() > b);
	}

	/**
	 * Adds a filter to keep the rows containing a number greather than or equal to {@code b}.
	 * 
	 * @param b
	 * 			The lower bound
	 * 
	 * @return a {@code And} instance to continue the query.
	 */
	public And ge(double b) {
		return matchSafe(a -> a.doubleValue() >= b);
	}
}
