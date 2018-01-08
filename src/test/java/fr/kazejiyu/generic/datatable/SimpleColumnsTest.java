package fr.kazejiyu.generic.datatable;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.id;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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
import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.ColumnIdNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderAlreadyExistsException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentColumnSizeException;

/**
 * Tests the behavior of a {@link Columns} implementation.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("Columns")
class SimpleColumnsTest {
	
	@Nested
	@DisplayName("of an empty table")
	class EmptyTable {
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		@Test @DisplayName("is empty")
		void isEmpty() {
			assertThat(empty.columns()).isEmpty();
		}

		@ParameterizedTest 
		@ValueSource(ints = {0, 1, 20, 100})
		@DisplayName("has the right size")
		void hasTheRightSize(int nbRows) {
			for( int i = 0 ; i < nbRows ; i++ )
				empty.columns().create("Empty Column #" + i);
			
			assertThat(empty.columns().size())
				.as("with " + nbRows + " rows")
				.isEqualTo(nbRows);
		}
		
		@ParameterizedTest 
		@ValueSource(strings = {"", "UPPERCASE", "lowercase", "camelCase", "snake_case", 
								"  space before", "space after   ", "with space"})
		@DisplayName("throws when adding duplicate headers")
		void throwsOnDuplicateHeaders(String header) {
			assertThatExceptionOfType(HeaderAlreadyExistsException.class)
				.as("with header \"" + header + "\"")
				.isThrownBy(addingTheSameHeaderTwice(empty, header));
		}
		
		@ParameterizedTest 
		@CsvSource({"'heLLo', 'HellO'", "'HELLO', 'hello'", "'he LO', 'HE lo'"})
		@DisplayName("throws when adding duplicate headers (case insensitively)")
		void throwsOnDuplicateHeadersIgnoringCase(String header) {
			assertThatExceptionOfType(HeaderAlreadyExistsException.class)
				.as("with header \"" + header + "\"")
				.isThrownBy(addingTheSameHeaderTwice(empty, header));
		}

		private ThrowingCallable addingTheSameHeaderTwice(Table table, String header) {
			return () -> {
				table.columns()
					.create(header)
					.create(header);
			};
		}
		
		@Test @DisplayName("has no header")
		void hasNoHeader() {
			assertThat(empty.columns().headers()).isEmpty();
		}
		
		@Test @DisplayName("throws when asked for its first column")
		void throwWhenAskedForItsFirstColumn() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.columns()::first);
		}
		
		@Test @DisplayName("throws when asked for its last column")
		void throwsWhenAskedForItsLastColumn() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.columns()::last);
		}
		
		@Test @DisplayName("returns a lone iterator")
		void returnsALoneIterator() {
			assertThat(empty.columns().iterator())
				.isEmpty();
		}
		
		@Test @DisplayName("returns a lone stream")
		void returnsALoneStream() {
			assertThat(empty.columns().stream())
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
			assertThat(people.columns()).isNotEmpty();
		}
		
		// create()
		
		@ParameterizedTest
		@MethodSource("createWrongSizedColumns")
		@DisplayName("throws when creating a wrong-sized column")
		void throwsWhenCreatingAWrongSizedColumn(List<Object> column) {
			assertThatExceptionOfType(InconsistentColumnSizeException.class)
				.as("creating a column of " + column.size() + " elements")
				.isThrownBy(() -> people.columns().create(Object.class, "Illegal Column", column)); 
		}
		
		@SuppressWarnings("unused")
		private Stream<List<Object>> createWrongSizedColumns() {
			return Stream.of(
				asList(),
				asList(1),
				asList(1, 2, 3, 4, 5, 6)
			);
		}
		
		@Test @DisplayName("appends created columns at the end")
		@SuppressWarnings("unchecked")
		void appendsCreatedColumnsAtTheEnd() {
			people.columns().create(Double.class, "SomeValue", 0.6, 12d, 0d, 42d);
			
			assertThat((Column<Double>) people.columns().last())
				.containsExactly(.6, 12d, 0d, 42d);
		}
		
		// headers()
		
		@Test @DisplayName("provides its headers in order")
		void providesItsHeadersInOrder() {
			assertThat(people.columns().headers())
				.containsExactly(NAME_HEADER, AGE_HEADER, SEX_HEADER);
		}
		
		// first()
		
		@Test @DisplayName("can returns its first column")
		@SuppressWarnings("unchecked")
		void canReturnItsFirstColumn() {
			assertThat((Column<Object>) people.columns().first())
				.containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
		}
		
		// last()
		
		@Test @DisplayName("can returns its last column")
		@SuppressWarnings("unchecked")
		void canReturnItsLastColumn() {
			assertThat((Column<Object>) people.columns().last())
				.containsExactly("Male", "Male", "Female", "Female");
		}
		
		// indexOf
		
		@ParameterizedTest
		@CsvSource({"'name', 0", "'age', 1", "'sex', 2"})
		@DisplayName("can returns the index of a column from its header")
		void canReturnTheIndexOfAColumn(String header, int expectedIndex) {
			assertThat(people.columns().indexOf(header))
				.isEqualTo(expectedIndex);
		}
		
		@Test @DisplayName("throws when asked for the index of a non existing header")
		void throwsWhenAskedForTheIndexOfANonExistingHeader() {
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf("wrong header"));
		}

		@ParameterizedTest
		@CsvSource({"'name', 'java.lang.String', 0", "'age', 'java.lang.Integer', 1", "'sex', 'java.lang.String', 2"})
		@DisplayName("can returns the index of a column from its id")
		void canReturnTheIndexOfAColumnFromItsId(String header, String className, int expectedIndex) throws ClassNotFoundException {
			assertThat(people.columns().indexOf(id(Class.forName(className), header)))
				.isEqualTo(expectedIndex);
		}
		
		@Test @DisplayName("throws when asked for the index of a column which id's type is wrong")
		void throwsWhenAskedForTheIndexOfAnIdWithWrongType() {
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf(id(String.class, "age")));
		}
		
		@Test @DisplayName("throws when asked for the index of a column which id's header is wrong")
		void throwsWhenAskedForTheIndexOfAnIdWithWrongHeader() {
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf(id(Integer.class, "name")));
		}
		
		// iterator()
		
		@Test @DisplayName("returns a well-sized iterator")
		void returnsAWellSizedIterator() {
			assertThat(people.columns().iterator()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("returns an iterator containing all the columns in order")
		@SuppressWarnings("unchecked")
		void returnsAnOrderedIterator() {
			Iterator<Column<?>> itRows = people.columns().iterator();
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat((Column<String>) itRows.next()).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
			softly.assertThat((Column<Integer>) itRows.next()).containsExactly(23, 32, 0, 21);
			softly.assertThat((Column<String>) itRows.next()).containsExactly("Male", "Male", "Female", "Female");
			
			softly.assertAll();
		}
		
		// stream()
		
		@Test @DisplayName("returns a well-sized stream")
		void returnsAWellSizedStream() {
			assertThat(people.columns().stream()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("returns a stream containing all the columns in order")
		@SuppressWarnings("unchecked")
		void returnAnOrderedStream() {
			List<Column<?>> columns = people.columns().stream().collect(toList());
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat((Column<String>) columns.get(0)).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
			softly.assertThat((Column<Integer>) columns.get(1)).containsExactly(23, 32, 0, 21);
			softly.assertThat((Column<String>) columns.get(2)).containsExactly("Male", "Male", "Female", "Female");
			
			softly.assertAll();
		}
		
		// clear()
		
		@Test @DisplayName("removes all its columns when cleared")
		void removesAllItsColumnsOnClear() {
			people.columns().clear();
			assertThat(people.columns()).isEmpty();
		}
		
		@Test @DisplayName("does not alter the number of rows when cleared")
		void avoidsAlteringNumberOfRowsOnClear() {
			people.columns().clear();
			assertThat(people.rows()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("makes every row empty when cleared")
		void leavesEmptyRowsOnClear() {
			people.columns().clear();
			assertThat(people.rows()).allMatch(Row::isEmpty);
		}
		
	}
}
