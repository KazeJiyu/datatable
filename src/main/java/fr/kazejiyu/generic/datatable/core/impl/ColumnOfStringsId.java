package fr.kazejiyu.generic.datatable.core.impl;

/**
 * A ColumnId dedicated for columns storing strings. <br>
 * <br>
 * This class is used to tailor {@link fr.kazejiyu.generic.datatable.query Query's DSL}.
 * 
 * @author Emmanuel CHEBBI
 */
public class ColumnOfStringsId extends ColumnIdDecorator<String> {

	ColumnOfStringsId(ColumnId<String> id) {
		super(id);
	}
	
}
