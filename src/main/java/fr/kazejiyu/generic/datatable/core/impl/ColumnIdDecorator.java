package fr.kazejiyu.generic.datatable.core.impl;

/**
 * Decorates a {@link ColumnId} to simplify implementations
 * of {@link ColumnOfStringsId} and {@link ColumnOfNumbersId}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of the column
 */
abstract class ColumnIdDecorator<T> {

	final ColumnId<T> id;
	
	ColumnIdDecorator(ColumnId<T> id) {
		this.id = id;
	}

	/** @return the type of column's elements */
	public Class<T> type() {
		return id.type();
	}
	
	/** @return the header of the column */
	public String header() {
		return id.header();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return id.equals(obj);
	}
}
