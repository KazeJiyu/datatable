package fr.kazejiyu.generic.datatable.core.impl;

/**
 * A ColumnId dedicated for columns storing numbers. <br>
 * <br>
 * This class is used to tailor {@link fr.kazejiyu.generic.datatable.query Query's DSL}.
 * 
 * @author Emmanuel CHEBBI
 *
 * @param <T> The type of type of the column
 */
public class ColumnOfNumbersId <T extends Number> extends ColumnIdDecorator<T> {

	ColumnOfNumbersId(ColumnId<T> id) {
		super(id);
	}
	
}
