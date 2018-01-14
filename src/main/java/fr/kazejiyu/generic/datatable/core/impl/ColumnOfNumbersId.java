package fr.kazejiyu.generic.datatable.core.impl;

public class ColumnOfNumbersId extends ColumnId<Number> {

	<T extends Number> ColumnOfNumbersId(ColumnId<T> id) {
		super(Number.class, id.header());
	}

}
