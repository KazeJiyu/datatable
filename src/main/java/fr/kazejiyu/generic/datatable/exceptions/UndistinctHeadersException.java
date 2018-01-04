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

/**
 * Thrown when a collection of headers contains duplicate while it should not.
 */
public class UndistinctHeadersException extends RuntimeException {

	/**
	 * Generated serial ID 
	 */
	private static final long serialVersionUID = -5938695492831626841L;
	
	public UndistinctHeadersException() {
		super();
	}

	public UndistinctHeadersException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndistinctHeadersException(String message) {
		super(message);
	}

	public UndistinctHeadersException(Throwable cause) {
		super(cause);
	}

}
