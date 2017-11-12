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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import fr.kazejiyu.generic.datatable.Table;

/**
 * A filter that can be applied on multipled columns.
 * <br><br>
 * Helps to select the rows to keep or remove when querying a {@link Table}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the instances to filter
 */
public class Filter <T> {
	
	/** The headers of the columns on which apply this filter */
	public final Set <String> headers;
	
	/** Indicates whether a value has to be kept */
	public final Predicate <T> predicate;
	
	Filter(String header, Predicate <T> predicate) {
		this(Arrays.asList(header), predicate);
	}
	
	Filter(Collection <String> headers, Predicate <T> predicate) {
		this.headers = new HashSet<>(headers);
		this.predicate = predicate;
	}

}
