package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A DataTable's Column")
class SimpleColumnTest {
	
	@Nested
	@DisplayName("when empty")
	class Empty {
		private Table table;
		private Column<?> empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			table = new DataTable();
			empty = table.columns()
				.create("Empty")
				.get("empty");
		}
		
		// empty()
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is not empty anymore when empty rows are added to the table")
		void is_not_empty_anymore_when_empty_rows_are_added_to_the_table() {
			table.rows().create("dumb")
						.create("dumb");
			
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
			assertThat(people.columns()).noneMatch(Column::isEmpty);
		}
		
		@Test @DisplayName("becomes empty when rows are cleared")
		void becomes_empty_when_rows_are_cleared() {
			people.rows().clear();
			assertThat(people.columns()).allMatch(Column::isEmpty);
		}
		
		// size()
		
		@Test @DisplayName("has the size equal to the number of rows")
		void has_the_size_equal_to_the_number_of_rows() {
			assertThat(people.columns()).allMatch(col -> col.size() == people.rows().size());
		}
		
		// header()
		
		@Test @DisplayName("has the right header")
		void has_the_right_header() {
			Stream<String> headers = people.columns().stream().map(Column::header);
			assertThat(headers).containsExactly(NAME_HEADER, AGE_HEADER, SEX_HEADER);
		}
		
		// get()
		
		@Test @DisplayName("throws when asked for an element with index < 0")
		void throws_when_asked_for_an_element_with_index_lt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.columns().first().get(-1));
		}
		
		@Test @DisplayName("throws when asked for an element with index == size")
		void throws_when_asked_for_an_element_with_index_eq_size() {
			Column<?> col = people.columns().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> col.get(col.size()));
		}
		
		@Test @DisplayName("throws when asked for an element with index > size")
		void throws_when_asked_for_an_element_with_index_gt_size() {
			Column<?> col = people.columns().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> col.get(col.size() + 1));
		}
		
		@Test @DisplayName("can return an element from row's index")
		@SuppressWarnings("unchecked")
		void can_return_an_element_from_row_index() {
			Column<String> col = (Column<String>) people.columns().get(2);
			assertThat(col).containsExactly("Male", "Male", "Female", "Female");
		}
		
		// set()
		
		@Test @DisplayName("throws when asked to set an element at index < 0")
		void throws_when_asked_to_set_an_element_at_index_lt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.columns().first().set(-1, null));
		}
		
		@Test @DisplayName("throws when asked to set an element at index == size")
		void throws_when_asked_to_set_an_element_at_index_eq_size() {
			Column<?> col = people.columns().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> col.set(col.size(), null));
		}
		
		@Test @DisplayName("throws when asked to set an element at index > size")
		void throws_when_asked_to_set_an_element_at_index_gt_size() {
			Column<?> col = people.columns().first();
			
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> col.set(col.size() + 1, null));
		}
		
		@Test @DisplayName("can set an element at a specific row index")
		@SuppressWarnings("unchecked")
		void can_set_an_element_at_a_specific_row_index() {
			Column<String> col = (Column<String>) people.columns().last();
			col.set(0, "Undefined");
			assertThat(col.get(0)).isEqualTo("Undefined");
		}
		
		// equals()
		
		@Test @DisplayName("is not equal to null")
		void is_not_equal_to_null() {
			assertThat(people.rows()).isNotEqualTo(null);
		}
		
		@Test @DisplayName("is equal to self")
		void is_equal_to_self() {
			assertThat(people.rows()).isEqualTo(people.rows());
		}
	}
}
