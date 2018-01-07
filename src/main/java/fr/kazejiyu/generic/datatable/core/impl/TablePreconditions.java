package fr.kazejiyu.generic.datatable.core.impl;

import java.util.LinkedHashSet;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

public class TablePreconditions {

	private final Table table;

	public TablePreconditions(DataTable table) {
		this.table = table;
	}

	/** @throws HeaderNotFoundException if an element of {@ode columnsToKeep} does not match any column */
	void assertAreExistingHeaders(LinkedHashSet<String> columnsToKeep) {
		for(final String columnToKeep : columnsToKeep)
			if( ! table.columns().hasHeader(columnToKeep) )
				throw new HeaderNotFoundException("The header " + columnToKeep + " does not exist in the table");
	}

}
