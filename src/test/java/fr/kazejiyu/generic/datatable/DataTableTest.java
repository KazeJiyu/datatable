package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A DataTable")
class DataTableTest {
	
	@Nested
	@DisplayName("when empty")
	class Empty {
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty rows")
		void is_empty_with_only_empty_rows() {
			empty.rows().create()
						.create();
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty columns")
		void is_empty_with_only_empty_columns() {
			empty.columns().create("Empty Column")
						   .create("Another Empty Column");
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("returns a non-null Rows instance")
		void returns_non_null_rows() {
			assertThat(empty.rows()).isNotNull();
		}
		
		@Test @DisplayName("returns a non-null Columns instance")
		void returns_non_null_columns() {
			assertThat(empty.columns()).isNotNull();
		}
		
		@Test @DisplayName("is equal to itself")
		void is_equal_to_itself() {
			assertThat(empty).isEqualTo(empty);
		}
		
		@Test @DisplayName("is equal to a new datatable")
		void is_equal_to_a_new_datatable() {
			assertThat(empty).isEqualTo(new DataTable());
		}
	}
	
	@Nested
	@DisplayName("when not empty")
	class NonEmpty {
		private Table people;
		
		private static final String AGE_HEADER = "AGE";
		private static final String NAME_HEADER = "name";
		private static final String SEX_HEADER = "sEx";
		
		@BeforeEach
		void initializePeopleTable() {
			people = createPeople();
		}
		
		Table createPeople() {
			Table table = new DataTable();
			table.columns()
					.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(Integer.class, AGE_HEADER, 23, 32, 0, 21)
					.create(String.class, SEX_HEADER, "Male", "Male", "Female", "Female");
			
			return table;
		}
	
		@Test @DisplayName("is not empty")
		void is_not_empty() {
			assertThat(people.isEmpty()).isFalse();
		}
		
		@Test @DisplayName("removes all its rows when cleared")
		void removes_all_its_rows_when_cleared() {
			people.clear();
			assertThat(people.rows().isEmpty()).isTrue();
		}
		
		@Test @DisplayName("removes all its columns when cleared")
		void removes_all_its_columns_when_cleared() {
			people.clear();
			assertThat(people.columns().isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is equal to another table that has the same content")
		void is_equal_to_another_table_that_has_the_same_content() {
			assertThat(people).isEqualTo(createPeople());
		}
	}
}
