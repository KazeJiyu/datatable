package fr.kazejiyu.generic.datatable;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
//		Table table = null;
//		
//		table.headers();
//		table.columns().add(Column.of("ENTIERS", Arrays.asList(1,2,"coucou", 3,4)));

		Column.of("ENTIERS", Arrays.asList(1,2,"coucou", 3,4));
		Column.of("HEADER", 1,2,3,4);
		Column.of("HEADER", "un", "deux", "trois");
		Column.of("HEADER", 1, "deux", 3, 5.0);
	}

}
