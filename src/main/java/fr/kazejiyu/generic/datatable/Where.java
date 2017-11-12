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
package fr.kazejiyu.generic.datatable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import fr.kazejiyu.generic.datatable.impl.WhereBool;
import fr.kazejiyu.generic.datatable.impl.WhereNumber;
import fr.kazejiyu.generic.datatable.impl.WhereStr;

public interface Where <T> {
	
	public And match(Predicate <T> predicate);
	
	public default And matchSafe(Predicate <T> predicate) {
		return match(item -> item != null && predicate.test(item));
	}
	
	public default And eq(T value) {
		return match(o -> Objects.equals(o, value));
	}
	
	public default And ne(T value) {
		return match(o -> ! Objects.equals(o, value));
	}
	
	public default And isNull() {
		return match(Objects::isNull);
	}
	
	public default And isNonNull() {
		return match(Objects::nonNull);
	}
	
	public default And isInstanceOf(Class <?> clazz) {
		return match(clazz::isInstance);
	}
	
	@SuppressWarnings("unchecked")
	public default And in(T... elements) {
		return in(Arrays.asList(elements));
	}
	
	public default And in(Collection <T> elements) {
		return match(elements::contains);
	}
	
	@SuppressWarnings("unchecked")
	public default And notIn(T... elements) {
		return notIn(Arrays.asList(elements));
	}
	
	public default And notIn(Collection <T> elements) {
		return match(elements::contains);
	}

	public WhereStr asStr();
	
	public WhereBool asBool();
	
	public WhereNumber asNumber();
}
