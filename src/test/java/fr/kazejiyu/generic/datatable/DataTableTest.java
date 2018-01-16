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
		void isEmpty() {
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty rows")
		void isEmptyWithOnlyEmptyRows() {
			empty.rows().create()
						.create();
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty columns")
		void isEmptyWithOnlyEmptyColumns() {
			empty.columns().create("Empty Column")
						   .create("Another Empty Column");
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("returns a non-null Rows instance")
		void returnsNonNullRows() {
			assertThat(empty.rows()).isNotNull();
		}
		
		@Test @DisplayName("returns a non-null Columns instance")
		void returnsNonNullColumns() {
			assertThat(empty.columns()).isNotNull();
		}
		
		@Test @DisplayName("is equal to itself")
		void isEqualToItself() {
			assertThat(empty).isEqualTo(empty);
		}
		
		@Test @DisplayName("is equal to a new datatable")
		void isEqualToANewDatatable() {
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
		void isNotEmpty() {
			assertThat(people.isEmpty()).isFalse();
		}
		
		@Test @DisplayName("removes all its rows when cleared")
		void removesAllItsRowsWhenCleared() {
			people.clear();
			assertThat(people.rows().isEmpty()).isTrue();
		}
		
		@Test @DisplayName("removes all its columns when cleared")
		void removesAllItsColumnsWhenCleared() {
			people.clear();
			assertThat(people.columns().isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is equal to another table that has the same content")
		void isEqualToAnotherTableThatHasTheSameContent() {
			assertThat(people).isEqualTo(createPeople());
		}
	}
}
