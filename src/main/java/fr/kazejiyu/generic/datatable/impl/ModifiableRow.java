package fr.kazejiyu.generic.datatable.impl;

import fr.kazejiyu.generic.datatable.Row;

abstract class ModifiableRow implements Row {

	Row add(Object element) {
		return insert(size(), element);
	}
	
	abstract Row insert(int position, Object element);
	
	abstract Row remove(int position);
		
}
