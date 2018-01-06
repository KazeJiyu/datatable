package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
class DataTableTest {
	
	private Table empty;
	private Table people;
	
	private static final String AGE_HEADER = "AGE";
	private static final String NAME_HEADER = "name";
	private static final String SEX_HEADER = "sEx";
	
	@BeforeEach
	void initializeEmptyTable() {
		empty = new DataTable();
	}
	
	@BeforeEach
	void initializePeopleTable() {
		people = new DataTable();
		people.columns()
				.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Mathilde")
				.create(Integer.class, AGE_HEADER, 23, 32, 42)
				.create(String.class, SEX_HEADER, "Male", "Male", "Female");
	}
	
	@Test
	void shouldBeEmptyByDefault() {
		assertThat(empty.isEmpty()).isTrue();
	}
	
	@Test
	void shouldBeEmptyWithOnlyEmptyRows() {
		empty.rows().create()
					.create();
		
		assertThat(empty.isEmpty()).isTrue();
	}
	
	@Test
	void shouldBeEmptyWithOnlyEmptyColumns() {
		empty.columns().create("Empty Column")
					   .create("Another Empty Column");
		
		assertThat(empty.isEmpty()).isTrue();
	}

	@Test
	void shouldNotBeEmptyWithNonEmptyRows() {
		assertThat(people.isEmpty()).isFalse();
	}
	
	@Test
	void shouldNotReturnNullRowsWhenEmpty() {
		assertThat(empty.rows()).isNotNull();
	}
	
	@Test
	void shouldNotReturnNullColumnsWhenEmpty() {
		assertThat(empty.columns()).isNotNull();
	}
}
