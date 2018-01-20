package fr.kazejiyu.generic.datatable.core.impl;

public class ColumnOfNumbersId <T extends Number> extends ColumnId<T> {

	ColumnOfNumbersId(ColumnId<T> id) {
		super(id.type(), id.header());
	}

}
