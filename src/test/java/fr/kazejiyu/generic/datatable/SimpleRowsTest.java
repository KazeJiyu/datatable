package fr.kazejiyu.generic.datatable;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Rows;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentRowSizeException;

/**
 * Tests the behavior of a {@link Rows} implementation.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("Rows")
class SimpleRowsTest {
	
	@Nested
	@DisplayName("of an empty table")
	class EmptyTable {
		
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.rows()).isEmpty();
		}
		
		// size()
		
		@ParameterizedTest
		@ValueSource(ints = {0, 1, 20, 100})
		@DisplayName("has the right size")
		void has_the_right_size(int nbRows) {
			for( int i = 0 ; i < nbRows ; i++ )
				empty.rows().create();
			
			assertThat(empty.rows().size())
				.as("with " + nbRows + " rows")
				.isEqualTo(nbRows);
		}
		
		// first()
		
		@Test @DisplayName("throws when asked for its first row")
		void throws_when_asked_for_its_first_row() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.rows()::first);
		}
		
		// last()
		
		@Test @DisplayName("throws when asked for its last row")
		void throws_when_asked_fpr_its_last_row() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.rows()::last);
		}
		
		// iterator()
		
		@Test @DisplayName("returns a lone iterator")
		void returns_a_lone_iterator() {
			assertThat(empty.rows().iterator())
			.isNotNull()
			.isEmpty();
		}
		
		// iterator()
		
		@Test @DisplayName("returns a lone stream")
		void returns_a_lone_stream() {
			assertThat(empty.rows().stream())
				.isEmpty();
		}
		
		// remove()
		
		@Test @DisplayName("throws when removing a row")
		void throws_when_removing_a_row() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> empty.rows().remove(0));
		}
	}
	
	@Nested
	@DisplayName("of a non empty table")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class NonEmptyTable {
		
		private Table people;
		
		private static final String AGE_HEADER = "AGE";
		private static final String NAME_HEADER = "name";
		private static final String SEX_HEADER = "sEx";
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(Integer.class, AGE_HEADER, 23, 32, 0, 21)
					.create(String.class, SEX_HEADER, "Male", "Male", "Female", "Female");
		}
	
		// isEmpty()
		
		@Test @DisplayName("is not empty")
		void is_not_empty() {
			assertThat(people.rows()).isNotEmpty();
		}
		
		// create()
		
		@ParameterizedTest
		@MethodSource("createWrongSizedRows")
		@DisplayName("throws when creating a wrong-sized row")
		void throws_when_creating_a_wrong_sized_row(List<Object> row) {
			assertThatExceptionOfType(InconsistentRowSizeException.class)
				.isThrownBy(() -> people.rows().create(row));
		}
		
		@SuppressWarnings("unused")
		/** Used as Method Source */
		private Stream<List<Object>> createWrongSizedRows() {
			return Stream.of(
				asList(),
				asList(1),
				asList(1, 2, 3, 4, 5)
			);
		}
		
		@ParameterizedTest
		@CsvSource({"'name', 'age', 'M'", "12, 23, 'F'", "'name', 42, 12"})
		@DisplayName("throws when creating a row with forbidden elements")
		void throws_when_creating_a_row_with_wrong_content(Object name, Object age, Object sex) {
			assertThatExceptionOfType(ClassCastException.class)
				.isThrownBy(() -> people.rows().create(name, age, sex));
		}
		
		@Test @DisplayName("appends created rows at the end")
		void appends_created_rows_at_the_end() {
			people.rows().create("Eva", 21, "Female");
			
			assertThat(people.rows().last())
				.containsExactly("Eva", 21, "Female");
		}
		
		// first()
		
		@Test @DisplayName("can return its first row")
		void can_return_its_first_row() {
			assertThat(people.rows().first())
				.containsExactly("Luc", 23, "Male");
		}
		
		// last()
		
		@Test @DisplayName("can return its last row")
		void can_return_its_last_row() {
			assertThat(people.rows().last())
				.containsExactly("Mathilde", 21, "Female");
		}
		
		@Test @DisplayName("returns a well-sized iterator")
		void returns_a_well_sized_iterator() {
			assertThat(people.rows().iterator()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("returns an iterator containing all the rows in order")
		void returns_an_ordered_iterator() {
			Iterator<Row> itRows = people.rows().iterator();
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat(itRows.next()).containsExactly("Luc", 23, "Male");
			softly.assertThat(itRows.next()).containsExactly("Baptiste", 32, "Male");
			softly.assertThat(itRows.next()).containsExactly("Anya", 0, "Female");
			softly.assertThat(itRows.next()).containsExactly("Mathilde", 21, "Female");
			
			softly.assertAll();
		}
		
		// stream()
		
		@Test @DisplayName("returns a well-sized stream")
		void returns_a_well_sized_stream() {
			assertThat(people.rows().stream()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("returns a stream containing all the rows in order")
		void returns_an_ordered_stream() {
			List<Row> rows = people.rows().stream().collect(toList());
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat(rows.get(0)).containsExactly("Luc", 23, "Male");
			softly.assertThat(rows.get(1)).containsExactly("Baptiste", 32, "Male");
			softly.assertThat(rows.get(2)).containsExactly("Anya", 0, "Female");
			softly.assertThat(rows.get(3)).containsExactly("Mathilde", 21, "Female");
		
			softly.assertAll();
		}
		
		// remove()
		
		@Test @DisplayName("can remove a row")
		void can_remove_a_row() {
			people.rows().remove(1);
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat(people.rows().size()).isEqualTo(3);
			softly.assertThat(people.rows().get(0)).containsExactly("Luc", 23, "Male");
			softly.assertThat(people.rows().get(1)).containsExactly("Anya", 0, "Female");
			softly.assertThat(people.rows().get(2)).containsExactly("Mathilde", 21, "Female");
		
			softly.assertAll();			
		}
		
		// clear()
		
		@Test @DisplayName("removes all its rows when cleared")
		void removes_all_its_rows_on_clear() {
			people.rows().clear();
			assertThat(people.rows()).isEmpty();
		}
		
		@Test @DisplayName("does not alter the number of columns when cleared")
		void avoids_altering_the_number_of_columns_on_clear() {
			people.rows().clear();
			assertThat(people.columns()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("makes every column empty when cleared")
		void leaves_empty_columns_on_clear() {
			people.rows().clear();
			assertThat(people.columns()).allMatch(Column::isEmpty);
		}
		
		// equals()
		
		@Test @DisplayName("is not equal to a non Rows object")
		void is_not_equal_to_a_non_rows_object() {
			assertThat(people.rows()).isNotEqualTo(12);
		}
		
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
