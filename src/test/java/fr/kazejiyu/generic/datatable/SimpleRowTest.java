package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A DataTable's Row")
class SimpleRowTest {
	
	@Nested
	@DisplayName("when empty")
	class Empty {
		private Table table;
		private Row empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			table = new DataTable();
			empty = table.rows().create().get(0);
		}
		
		// empty()
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is not empty anymore when columns are added to the table")
		void is_empty_with_only_empty_rows() {
			table.columns().create(Integer.class, "ints", 12);
			assertThat(empty.isEmpty()).isFalse();
		}
		
		// size()
		
		@Test @DisplayName("has a size of 0")
		void has_size_of_zero() {
			assertThat(empty.size()).isEqualTo(0);
		}

		// iterator()
		
		@Test @DisplayName("returns a lone iterator")
		void returns_a_lone_iterator() {
			assertThat(empty).isEmpty();
		}
		
		// get()
		
		@Test @DisplayName("throws when asked for an element")
		void throws_when_asked_for_an_element() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> empty.get(0));
		}
		
		// set()
		
		@Test @DisplayName("throws when asked to set an element")
		void throws_when_asked_to_set_an_element() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> empty.set(0, null));
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
		
		// empty()
	
		@Test @DisplayName("is not empty")
		void is_not_empty() {
			assertThat(people.rows()).noneMatch(Row::isEmpty);
		}
		
		@Test @DisplayName("becomes empty when columns are cleared")
		void removes_all_its_rows_when_cleared() {
			people.columns().clear();
			assertThat(people.rows()).allMatch(Row::isEmpty);
		}
		
		// size()
		
		@Test @DisplayName("has the size equal to the number of columns")
		void removes_all_its_columns_when_cleared() {
			assertThat(people.rows()).allMatch(row -> row.size() == people.columns().size());
		}
		
		// get()
		
		@Test @DisplayName("throws when asked for an element with index < 0")
		void throws_when_asked_for_an_element_with_index_lt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.rows().first().get(-1));
		}
		
		@Test @DisplayName("throws when asked for an element with index == size")
		void throws_when_asked_for_an_element_with_index_eq_size() {
			Row row = people.rows().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> row.get(row.size()));
		}
		
		@Test @DisplayName("throws when asked for an element with index > size")
		void throws_when_asked_for_an_element_with_index_gt_size() {
			Row row = people.rows().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> row.get(row.size() + 1));
		}
		
		@Test @DisplayName("throws when asked for an element at non existing header")
		void throws_when_asked_for_an_element_at_non_existing_header() {
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.rows().first().get("non existing"));
		}
		
		@Test @DisplayName("throws when asked for an element at id with wrong header")
		void throws_when_asked_for_an_element_at_id_with_wrong_header() {
			ColumnId<String> nonExisting = id(String.class, "nonExisting");
			
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.rows().first().get(nonExisting));
		}
		
		@Test @DisplayName("throws when asked for an element at id with wrong type")
		void throws_when_asked_for_an_element_at_id_with_wrong_type() {
			ColumnId<Integer> nonExisting = id(Integer.class, NAME_HEADER);
			
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.rows().first().get(nonExisting));
		}
		
		@Test @DisplayName("returns an element from its column's id")
		void returns_an_element_from_its_column_id() {
			ColumnId<String> name = id(String.class, NAME_HEADER);
			assertThat(people.rows().first().get(name)).isEqualTo("Luc");
		}
		
		// set()
		
		@Test @DisplayName("throws when asked to set an element at index < 0")
		void throws_when_asked_to_set_an_element_at_index_lt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.rows().first().set(-1, null));
		}
		
		@Test @DisplayName("throws when asked to_set an element at index == size")
		void throws_when_asked_to_an_element_at_index_eq_size() {
			Row row = people.rows().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> row.set(row.size(), null));
		}
		
		@Test @DisplayName("throws when asked to_set an element at index > size")
		void throws_when_asked_to_set_an_element_at_index_gt_size() {
			Row row = people.rows().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> row.set(row.size() + 1, null));
		}
		
		@Test @DisplayName("throws when asked to_set an element at non existing header")
		void throws_when_asked_to_set_an_element_at_non_existing_header() {
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.rows().first().set("non existing", null));
		}
		
		@Test @DisplayName("throws when asked to_set an element at id at wrong header")
		void throws_when_asked_to_set_an_element_at_id_at_wrong_header() {
			ColumnId<String> nonExisting = id(String.class, "nonExisting");
			
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.rows().first().set(nonExisting, null));
		}
		
		@Test @DisplayName("throws when asked to_set an element at id at wrong type")
		void throws_when_asked_to_set_an_element_at_id_at_wrong_type() {
			ColumnId<Integer> nonExisting = id(Integer.class, NAME_HEADER);
			
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.rows().first().set(nonExisting, null));
		}
		
		@Test @DisplayName("throws when asked to set at header an element of unexpected type")
		void throws_when_asked_to_set_at_header_an_element_of_unexpected_type() {
			assertThatExceptionOfType(ClassCastException.class)
				.isThrownBy(() -> people.rows().first().set(NAME_HEADER, 12));
		}
		
		@Test @DisplayName("throws when asked to set at index an element of unexpected type")
		void throws_when_asked_to_set_at_index_an_element_of_unexpected_type() {
			assertThatExceptionOfType(ClassCastException.class)
			.isThrownBy(() -> people.rows().first().set(0, 12));
		}
		
		@Test @DisplayName("can set an element from its column's id")
		void can_set_an_element_from_its_column_id() {
			ColumnId<String> name = id(String.class, NAME_HEADER);
			people.rows().first().set(name, "Gandalf");
			
			assertThat(people.rows().first().get(name)).isEqualTo("Gandalf");
		}
		
		@Test @DisplayName("can set an element from its index")
		void can_set_an_element_from_its_index() {
			people.rows().first().set(0, "Gandalf");
			assertThat(people.rows().first().get(0)).isEqualTo("Gandalf");
		}
		
		// equals()
		
		@Test @DisplayName("is not equal to null")
		void is_not_equal_to_null() {
			assertThat(people.rows()).noneMatch(row -> row.equals(null));
		}
		
		@SuppressWarnings("unlikely-arg-type")
		@Test @DisplayName("is not equal to a non Row object")
		void is_not_equal_to_a_non_row_object() {
			assertThat(people.rows()).noneMatch(row -> row.equals("str"));
		}
		
		@Test @DisplayName("is equal to self")
		void is_equal_to_self() {
			assertThat(people.rows()).allMatch(row -> row.equals(row));
		}
	}
}
