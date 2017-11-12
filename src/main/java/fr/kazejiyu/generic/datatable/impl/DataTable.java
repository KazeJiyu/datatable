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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import fr.kazejiyu.generic.datatable.Columns;
import fr.kazejiyu.generic.datatable.Row;
import fr.kazejiyu.generic.datatable.Rows;
import fr.kazejiyu.generic.datatable.Table;

/**
 * A simple implementation of {@link Table} that rely on {@code GlazedLists}.
 * 
 * @author Emmanuel CHEBBI
 */
public class DataTable implements Table, AutoCloseable {
	
	/** The rows that compose the table. */
	private final Rows rows;
	
	/** The columns that compose the table. */
	private final Columns columns;
	
	/**
	 * Creates a new table.
	 */
	public DataTable() {
		this.rows = new SimpleRows(this);
		this.columns = new SimpleColumns(this);
	}
	
	/** @return the {@code EventList} used internally */
	EventList <Row> internal() {
		return ((SimpleRows) rows).internal();
	}

	@Override
	public Rows rows() {
		return rows;
	}

	@Override
	public Columns columns() {
		return columns;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		if( internal() instanceof TransformedList<?,?> )
			((TransformedList<?,?>) internal()).dispose();
	}

	@Override
	public void close() throws Exception {
		this.dispose();
	}

}
