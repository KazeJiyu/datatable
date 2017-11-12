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

public class WhereNumber extends GlazedWhere <Number> {
	
	public WhereNumber(QueryContext context, String header) {
		super(context, header);
	}
	
	public WhereNumber(QueryContext context, Collection<String> headers) {
		super(context, headers);
	}

	public And isZero() {
		return match(n -> n.doubleValue() == 0);
	}
	
	public And isPositive() {
		return match(n -> n.doubleValue() > 0);
	}
	
	public And isNegative() {
		return match(n -> n.doubleValue() < 0);
	}
	
	public And isEven() {
		return match(n -> n.doubleValue() % 2 == 0);
	}
	
	public And isOdd() {
		return match(n -> n.doubleValue() % 2 != 0);
	}
	
	public And inRange(double min, double max) {
		return match(n -> min <= n.doubleValue() && n.doubleValue() <= max);
	}

	public And lt(double b) {
		return match(a -> a.doubleValue() < b);
	}
	
	public And le(double b) {
		return matchSafe(a -> a.doubleValue() <= b);
	}

	public And gt(double b) {
		return match(a -> a.doubleValue() > b);
	}
	
	public And ge(double b) {
		return match(a -> a.doubleValue() >= b);
	}
}
