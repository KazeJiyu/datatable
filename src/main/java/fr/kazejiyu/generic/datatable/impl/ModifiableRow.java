package fr.kazejiyu.generic.datatable.impl;

import fr.kazejiyu.generic.datatable.Row;

interface ModifiableRow extends Row {

	public default Row add(Object element) {
		return insert(size(), element);
	}
	
	public Row insert(int position, Object element);
	
	public Row remove(int position);
		
}
