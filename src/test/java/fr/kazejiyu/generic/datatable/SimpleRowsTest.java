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
		
		@Test
		void shouldBeEmptyByDefault() {
			assertThat(empty.rows()).isEmpty();
		}
		
		// size()
		
		@ParameterizedTest
		@ValueSource(ints = {0, 1, 20, 100})
		@DisplayName("has the right size")
		void hasTheRightSize(int nbRows) {
			for( int i = 0 ; i < nbRows ; i++ )
				empty.rows().create();
			
			assertThat(empty.rows().size())
				.as("with " + nbRows + " rows")
				.isEqualTo(nbRows);
		}
		
		@Test @DisplayName("throws when asked for its first row")
		void throwsWhenAskedForItsFirstColumn() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.rows()::first);
		}
		
		@Test @DisplayName("throws when asked for its last row")
		void shouldThrowWhenAskedForItsLastRow() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.rows()::last);
		}
		
		@Test @DisplayName("returns a lone iterator")
		void returnsALoneIterator() {
			assertThat(empty.rows().iterator())
			.isNotNull()
			.isEmpty();
		}
		
		@Test @DisplayName("returns a lone stream")
		void returnsALoneStream() {
			assertThat(empty.rows().stream())
				.isEmpty();
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
		void isNotEmpty() {
			assertThat(people.rows()).isNotEmpty();
		}
		
		// create()
		
		@ParameterizedTest
		@MethodSource("createWrongSizedRows")
		@DisplayName("throws when creating a wrong-sized row")
		void throwsWhenCreatingAWrongSizedRow(List<Object> row) {
			assertThatExceptionOfType(InconsistentRowSizeException.class)
				.isThrownBy(() -> people.rows().create(row));
		}
		
		@SuppressWarnings("unused")
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
		void throwsWhenCreatingARowWithWrongContent(Object name, Object age, Object sex) {
			assertThatExceptionOfType(ClassCastException.class)
				.isThrownBy(() -> people.rows().create(name, age, sex));
		}
		
		@Test @DisplayName("appends created rows at the end")
		@SuppressWarnings("unchecked")
		void appendsCreatedRowsAtTheEnd() {
			people.rows().create("Eva", 21, "Female");
			
			assertThat(people.rows().last())
				.containsExactly("Eva", 21, "Female");
		}
		
		// first()
		
		@Test @DisplayName("can return its first row")
		void canReturnItsFirstRow() {
			assertThat(people.rows().first())
				.containsExactly("Luc", 23, "Male");
		}
		
		// last()
		
		@Test @DisplayName("can return its last row")
		void canReturnItsLastRow() {
			assertThat(people.rows().last())
				.containsExactly("Mathilde", 21, "Female");
		}
		
		@Test @DisplayName("returns a well-sized iterator")
		void returnsAWellSizeIterator() {
			assertThat(people.rows().iterator()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("returns an iterator containing all the rows in order")
		void returnsAnOrderedIterator() {
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
		void returnsAWellSizedStream() {
			assertThat(people.rows().stream()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("returns a stream containing all the rows in order")
		void returnsAnOrderedStream() {
			List<Row> rows = people.rows().stream().collect(toList());
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat(rows.get(0)).containsExactly("Luc", 23, "Male");
			softly.assertThat(rows.get(1)).containsExactly("Baptiste", 32, "Male");
			softly.assertThat(rows.get(2)).containsExactly("Anya", 0, "Female");
			softly.assertThat(rows.get(3)).containsExactly("Mathilde", 21, "Female");
		
			softly.assertAll();
		}
		
		// clear()
		
		@Test @DisplayName("removes all its rows when cleared")
		void removesAllItsRowsOnClear() {
			people.rows().clear();
			assertThat(people.rows()).isEmpty();
		}
		
		@Test @DisplayName("does not alter the number of columns when cleared")
		void avoidsAlteringNumberOfColumnsOnClear() {
			people.rows().clear();
			assertThat(people.columns()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("makes every column empty when cleared")
		void shouldLeaveEmptyColumnsOnClear() {
			people.rows().clear();
			assertThat(people.columns()).allMatch(Column::isEmpty);
		}
	}
}
