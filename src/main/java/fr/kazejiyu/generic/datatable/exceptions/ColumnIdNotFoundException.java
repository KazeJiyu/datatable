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
package fr.kazejiyu.generic.datatable.exceptions;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;

/**
 * Thrown when a {@link Column} is requested via its {@link ColumnId}, but no matching can be found. 
 */
public class ColumnIdNotFoundException extends RuntimeException {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 7581627698428029561L;

	public ColumnIdNotFoundException() {
		super();
	}

	public ColumnIdNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ColumnIdNotFoundException(String arg0) {
		super(arg0);
	}

	public ColumnIdNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
