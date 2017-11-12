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

public class WhereStr extends GlazedWhere <String> {
	
	public WhereStr(QueryContext context, String header) {
		super(context, header);
	}
	
	public WhereStr(QueryContext context, Collection<String> headers) {
		super(context, headers);
	}
	
	public And isEmpty() {
		return matchSafe(String::isEmpty);
	}

	public And equalsIgnoreCase(String expected) {
		return matchSafe(str -> str.equalsIgnoreCase(expected));
	}
	
	public And isInLowerCase() {
		return matchSafe(str -> str.equals(str.toLowerCase()));
	}
	
	public And isInUpperCase() {
		return matchSafe(str -> str.equals(str.toUpperCase()));
	}
	
	public And contains(String sub) {
		return matchSafe(str -> str.contains(sub));
	}
	
	public And startsWith(String start) {
		return matchSafe(str -> str.startsWith(start));
	}
	
	public And endsWith(String end) {
		return matchSafe(str -> str.endsWith(end));
	}
	
}
