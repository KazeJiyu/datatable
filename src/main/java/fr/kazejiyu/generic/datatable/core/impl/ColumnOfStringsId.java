package fr.kazejiyu.generic.datatable.core.impl;

public class ColumnOfStringsId extends ColumnId<String> {

	ColumnOfStringsId(ColumnId<String> id) {
		super(String.class, id.header());
	}

}
