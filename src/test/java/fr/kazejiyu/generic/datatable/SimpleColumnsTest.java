package fr.kazejiyu.generic.datatable;

import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.id;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
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
		
		// isEmpty()
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.columns()).isEmpty();
		}
		
		// size()

		@ParameterizedTest 
		@ValueSource(ints = {0, 1, 20, 100})
		@DisplayName("has the right size")
		void has_the_right_size(int nbRows) {
			for( int i = 0 ; i < nbRows ; i++ )
				empty.columns().create("Empty Column #" + i);
			
			assertThat(empty.columns().size())
				.as("with " + nbRows + " rows")
				.isEqualTo(nbRows);
		}
		
		// create()
		
		@ParameterizedTest 
		@ValueSource(strings = {"", "UPPERCASE", "lowercase", "camelCase", "snake_case", 
								"  space before", "space after   ", "with space"})
		@DisplayName("throws when adding duplicate headers")
		void throws_on_duplicate_headers(String header) {
			assertThatExceptionOfType(HeaderAlreadyExistsException.class)
				.as("with header \"" + header + "\"")
				.isThrownBy(addingTheSameHeaderTwice(empty, header));
		}
		
		@ParameterizedTest 
		@CsvSource({"'heLLo', 'HellO'", "'HELLO', 'hello'", "'he LO', 'HE lo'"})
		@DisplayName("throws when adding duplicate headers (case insensitively)")
		void throws_on_duplicate_headers_ignoring_case(String header) {
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
		
		@Test @DisplayName("can create empty columns at the end")
		void can_create_empty_columns_at_the_end() {
			ColumnId<Integer> height = id(Integer.class, "height");
			empty.columns().create(height);
			Column<?> last = empty.columns().last();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(last.header()).isEqualTo("height");
			softly.assertThat(last.type()).isEqualTo(Integer.class);
			softly.assertThat(last).isEmpty();
			softly.assertAll();
		}
		
		// headers()
		
		@Test @DisplayName("has no header")
		void has_no_header() {
			assertThat(empty.columns().headers()).isEmpty();
		}
		
		// first()
		
		@Test @DisplayName("throws when asked for its first column")
		void throws_when_asked_for_its_first_column() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.columns()::first);
		}
		
		// last()
		
		@Test @DisplayName("throws when asked for its last column")
		void throws_when_asked_for_its_last_column() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(empty.columns()::last);
		}
		
		// iterator()
		
		@Test @DisplayName("returns a lone iterator")
		void returns_a_lone_iterator() {
			assertThat(empty.columns().iterator())
				.isEmpty();
		}
		
		// stream()
		
		@Test @DisplayName("returns a lone stream")
		void returns_a_lone_stream() {
			assertThat(empty.columns().stream())
				.isEmpty();
		}
		
		// get()
		
		@Test @DisplayName("throws when getting a column")
		void throws_when_getting_a_column() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> empty.columns().get(0));
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
		
		private final ColumnId<Integer> AGE = id(Integer.class, AGE_HEADER);
		private final ColumnId<String> NAME = id(String.class, NAME_HEADER);
		private final ColumnId<String> SEX = id(String.class, SEX_HEADER);
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(NAME_HEADER, String.class, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(AGE_HEADER, Integer.class, 23, 32, 0, 21)
					.create(SEX_HEADER, String.class, "Male", "Male", "Female", "Female");
		}
		
		// isEmpty()
		
		@Test @DisplayName("is not empty")
		void is_not_empty() {
			assertThat(people.columns()).isNotEmpty();
		}
		
		// create()
		
		@ParameterizedTest
		@MethodSource("createWrongSizedColumns")
		@DisplayName("throws when creating a wrong-sized column")
		void throws_when_creating_a_wrong_sized_column(List<Object> column) {
			assertThatExceptionOfType(InconsistentColumnSizeException.class)
				.as("creating a column of " + column.size() + " elements")
				.isThrownBy(() -> people.columns().create(Object.class, "Illegal Column", column)); 
		}
		
		@SuppressWarnings("unused")
		/** Used as Method Source */
		private Stream<List<Object>> createWrongSizedColumns() {
			return Stream.of(
				asList(),
				asList(1),
				asList(1, 2, 3, 4, 5, 6)
			);
		}
		
		@Test @DisplayName("appends created columns at the end")
		@SuppressWarnings("unchecked")
		void appends_created_columns_at_the_end() {
			people.columns().create("SomeValue", Double.class, 0.6, 12d, 0d, 42d);
			Column<Double> last = (Column<Double>) people.columns().last();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(last.header()).isEqualTo("SomeValue");
			softly.assertThat(last.type()).isEqualTo(Double.class);
			softly.assertThat(last).containsExactly(.6, 12d, 0d, 42d);
			softly.assertAll();
		}
		
		@Test @DisplayName("appends columns created from an id at the end")
		@SuppressWarnings("unchecked")
		void appends_columns_created_from_an_id_at_the_end() {
			ColumnId<String> str = id(String.class, "str");
			people.columns().create(str, "a", "c", "b", "d");
			Column<String> last = (Column<String>) people.columns().last();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(last.header()).isEqualTo("str");
			softly.assertThat(last.type()).isEqualTo(String.class);
			softly.assertThat(last).containsExactly("a", "c", "b", "d");
			softly.assertAll();
		}
		
		
		// headers()
		
		@Test @DisplayName("provides its headers in order")
		void provides_its_headers_in_order() {
			assertThat(people.columns().headers())
				.containsExactly(NAME_HEADER, AGE_HEADER, SEX_HEADER);
		}
		
		// first()
		
		@Test @DisplayName("can return its first column")
		@SuppressWarnings("unchecked")
		void can_return_its_first_column() {
			assertThat((Column<Object>) people.columns().first())
				.containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
		}
		
		// last()
		
		@Test @DisplayName("can return its last column")
		@SuppressWarnings("unchecked")
		void can_return_its_last_column() {
			assertThat((Column<Object>) people.columns().last())
				.containsExactly("Male", "Male", "Female", "Female");
		}
		
		// indexOf
		
		@ParameterizedTest
		@CsvSource({"'name', 0", "'aGe', 1", "'SEX', 2"})
		@DisplayName("can return the index of a column from its header")
		void can_return_the_index_of_a_column(String header, int expectedIndex) {
			assertThat(people.columns().indexOf(header))
				.isEqualTo(expectedIndex);
		}
		
		@Test @DisplayName("throws when asked for the index of a non existing header")
		void throws_when_asked_for_the_index_of_a_non_existing_header() {
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf("wrong header"));
		}

		@ParameterizedTest
		@CsvSource({"'NAme', 'java.lang.String', 0", "'agE', 'java.lang.Integer', 1", "'sex', 'java.lang.String', 2"})
		@DisplayName("can return the index of a column from its id")
		void can_return_the_index_of_a_column_from_its_id(String header, String className, int expectedIndex) throws ClassNotFoundException {
			assertThat(people.columns().indexOf(id(Class.forName(className), header)))
				.isEqualTo(expectedIndex);
		}
		
		@Test @DisplayName("throws when asked for the index of a column which id's type is wrong")
		void throws_when_asked_for_the_index_of_an_id_with_wrong_type() {
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf(id(String.class, "age")));
		}
		
		@Test @DisplayName("throws when asked for the index of a column which id's header is wrong")
		void throws_when_asked_for_the_index_of_an_id_with_wrong_header() {
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.columns().indexOf(id(Integer.class, "name")));
		}
		
		// iterator()
		
		@Test @DisplayName("returns a well-sized iterator")
		void returns_a_well_sized_iterator() {
			assertThat(people.columns().iterator()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("returns an iterator containing all the columns in order")
		@SuppressWarnings("unchecked")
		void returns_an_ordered_iterator() {
			Iterator<Column<?>> itRows = people.columns().iterator();
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat((Column<String>) itRows.next()).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
			softly.assertThat((Column<Integer>) itRows.next()).containsExactly(23, 32, 0, 21);
			softly.assertThat((Column<String>) itRows.next()).containsExactly("Male", "Male", "Female", "Female");
			
			softly.assertAll();
		}
		
		// stream()
		
		@Test @DisplayName("returns a well-sized stream")
		void returns_a_well_sized_stream() {
			assertThat(people.columns().stream()).size().isEqualTo(3);
		}
		
		@Test @DisplayName("returns a stream containing all the columns in order")
		@SuppressWarnings("unchecked")
		void returns_an_ordered_stream() {
			List<Column<?>> columns = people.columns().stream().collect(toList());
			
			SoftAssertions softly = new SoftAssertions();
			
			softly.assertThat((Column<String>) columns.get(0)).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
			softly.assertThat((Column<Integer>) columns.get(1)).containsExactly(23, 32, 0, 21);
			softly.assertThat((Column<String>) columns.get(2)).containsExactly("Male", "Male", "Female", "Female");
			
			softly.assertAll();
		}
		
		// clear()
		
		@Test @DisplayName("removes all its columns when cleared")
		void removes_all_its_columns_on_clear() {
			people.columns().clear();
			assertThat(people.columns()).isEmpty();
		}
		
		@Test @DisplayName("does not alter the number of rows when cleared")
		void avoids_altering_the_number_of_rows_on_clear() {
			people.columns().clear();
			assertThat(people.rows()).size().isEqualTo(4);
		}
		
		@Test @DisplayName("makes every row empty when cleared")
		void leaves_empty_rows_on_clear() {
			people.columns().clear();
			assertThat(people.rows()).allMatch(Row::isEmpty);
		}
		
		// get()
		
		@Test @DisplayName("throws when getting a column from an id with non existing header")
		void throws_when_getting_a_column_from_an_id_with_non_existing_header() {
			ColumnId<String> nonExisting = id(String.class, "string");
			assertThatExceptionOfType(ColumnIdNotFoundException.class)
				.isThrownBy(() -> people.columns().get(nonExisting));
		}
		
		@Test @DisplayName("throws when getting a column with index < 0")
		void throws_when_getting_a_column_from_an_id_with_index_lt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.columns().get(-1));
		}
		
		@Test @DisplayName("throws when asked for a column with index == size")
		void throws_when_asked_for_a_column_with_index_eq_size() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.columns().get(people.columns().size()));
		}
		
		@Test @DisplayName("throws when asked for a column with index > size")
		void throws_when_asked_for_a_column_with_index_gt_0() {
			assertThatExceptionOfType(IndexOutOfBoundsException.class)
				.isThrownBy(() -> people.columns().get(people.columns().size() + 1));
		}
		
		@Test @DisplayName("can return a column from its id")
		void can_return_a_column_from_its_id() {
			ColumnId<Integer> age = id(Integer.class, AGE_HEADER);
			assertThat(people.columns().get(age)).containsExactly(23, 32, 0, 21);
		}
		
		// remove()
		
		@Test @DisplayName("can remove a column from the column's header")
		void can_remove_a_column_from_the_column_header() {
			people.columns().remove(SEX_HEADER);
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(people.columns().size()).isEqualTo(2);
			softly.assertThat(people.columns().get(AGE)).containsExactly(23, 32, 0, 21);
			softly.assertThat(people.columns().get(NAME)).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
			softly.assertAll();
		}
		
		@Test @DisplayName("throws when removing a column from a non existing header")
		void throws_when_removing_a_column_from_a_non_existing_header() {
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.columns().remove("non existing"));
		}
		
		// contains()
		
		@Test @DisplayName("knows when it contains a column with a specific name")
		void knows_when_it_contains_a_column_with_a_specific_name() {
			for( String header : asList(NAME_HEADER.toUpperCase(), SEX_HEADER.toLowerCase(), AGE_HEADER) )
				assertThat(people.columns().contains(header)).isTrue();
		}
		
		@Test @DisplayName("knows when it does not contain any column with a specific name")
		void knows_when_it_does_not_contain_any_column_with_a_specific_name() {
			for( String header : asList("  " + NAME_HEADER, "non existing", AGE_HEADER + " ") )
				assertThat(people.columns().contains(header)).isFalse();
		}
		
		@Test @DisplayName("knows when it contains a column with a specific id")
		void knows_when_it_contains_a_column_with_a_specific_id() {
			for( ColumnId<?> id : asList(NAME, SEX, AGE) )
				assertThat(people.columns().contains(id)).isTrue();
		}
		
		@Test @DisplayName("knows when it does not contain a column with a specific id")
		void knows_when_it_does_not_contain_any_column_with_a_specific_id() {
			for( ColumnId<?> id : asList(id(Integer.class, NAME_HEADER), id(String.class, AGE_HEADER)) )
				assertThat(people.columns().contains(id)).isFalse();
		}
	}
}
