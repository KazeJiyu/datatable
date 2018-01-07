package fr.kazejiyu.generic.datatable;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Columns;
import fr.kazejiyu.generic.datatable.core.Row;
import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.HeaderAlreadyExistsException;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;
import fr.kazejiyu.generic.datatable.exceptions.InconsistentColumnSizeException;

/**
 * Tests the behavior of a {@link Columns} implementation.
 * 
 * @author Emmanuel CHEBBI
 */
public class SimpleColumnsTest {
	
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
				.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Anya", "Mathilde")
				.create(Integer.class, AGE_HEADER, 23, 32, 0, 21)
				.create(String.class, SEX_HEADER, "Male", "Male", "Female", "Female");
	}
	
	// isEmpty()
	
	@Test
	void shouldBeEmptyByDefault() {
		assertThat(empty.columns()).isEmpty();
	}
	
	@Test
	void shouldNotBeEmptyWhenFilled() {
		assertThat(people.columns()).isNotEmpty();
	}
	
	// size()

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 20, 100})
	void shouldHaveTheRightSize(int nbRows) {
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
	void shouldThrowOnDuplicateHeaders(String header) {
		assertThatExceptionOfType(HeaderAlreadyExistsException.class)
			.as("with header \"" + header + "\"")
			.isThrownBy(addingTheSameHeaderTwice(empty, header));
	}
	
	@ParameterizedTest
	@CsvSource({"'heLLo', 'HellO'", "'HELLO', 'hello'", "'he LO', 'HE lo'"})
	void shouldThrowOnDuplicateHeadersIgnoringCase(String header) {
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
	
	@ParameterizedTest
	@MethodSource("createWrongSizedColumns")
	void shouldThowWhenCreatingAWrongSizeColumn(List<Object> column) {
		assertThatExceptionOfType(InconsistentColumnSizeException.class)
			.as("creating a column of " + column.size() + " elements")
			.isThrownBy(() -> people.columns().create(Object.class, "Illegal Column", column)); 
	}
	
	@SuppressWarnings("unused")
	private static Stream<List<Object>> createWrongSizedColumns() {
		return Stream.of(
			asList(),
			asList(1),
			asList(1, 2, 3, 4, 5, 6)
		);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldAppendCreatedColumnsAtTheEnd() {
		people.columns().create(Double.class, "SomeValue", 0.6, 12d, 0d, 42d);
		
		assertThat((Column<Double>) people.columns().last())
			.containsExactly(.6, 12d, 0d, 42d);
	}
	
	// headers()
	
	@Test 
	void shouldHaveNoHeaderWhenEmpty() {
		assertThat(empty.columns().headers())
			.isEmpty();
	}
	
	@Test
	void shouldProvideTheRightHeadersInOrder() {
		assertThat(people.columns().headers())
			.containsExactly(NAME_HEADER, AGE_HEADER, SEX_HEADER);
	}
	
	// first()
	
	@Test
	@SuppressWarnings("unchecked")
	void canReturnItsFirstColumn() {
		assertThat((Column<Object>) people.columns().first())
			.containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
	}
	
	@Test
	void shouldThrowWhenFirstColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.columns()::first);
	}
	
	// last()
	
	@Test
	@SuppressWarnings("unchecked")
	void canReturnItsLastColumn() {
		assertThat((Column<Object>) people.columns().last())
			.containsExactly("Male", "Male", "Female", "Female");
	}
	
	@Test
	void shouldThrowWhenLastColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.columns()::last);
	}
	
	// indexOf
	
	@ParameterizedTest
	@CsvSource({"'name', 0", "'age', 1", "'sex', 2"})
	void canReturnTheIndexOfAColumn(String header, int expectedIndex) {
		assertThat(people.columns().indexOf(header))
			.isEqualTo(expectedIndex);
	}
	
	@Test
	void shouldThrowWhenAskedForTheIndexOfANonExistingHeader() {
		assertThatExceptionOfType(HeaderNotFoundException.class)
			.isThrownBy(() -> people.columns().indexOf("wrong header"));
	}
	
	// iterator()
	
	@Test
	void shouldReturnALoneIteratorWhenEmpty() {
		assertThat(empty.columns().iterator())
			.isEmpty();
	}
	
	@Test
	void shouldReturnAWellSizedIterator() {
		assertThat(people.columns().iterator()).size().isEqualTo(3);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldReturnAnOrderedIterator() {
		Iterator<Column<?>> itRows = people.columns().iterator();
		
		SoftAssertions softly = new SoftAssertions();
		
		softly.assertThat((Column<String>) itRows.next()).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
		softly.assertThat((Column<Integer>) itRows.next()).containsExactly(23, 32, 0, 21);
		softly.assertThat((Column<String>) itRows.next()).containsExactly("Male", "Male", "Female", "Female");
		
		softly.assertAll();
	}
	
	// stream()
	
	@Test
	void shouldReturnALoneStreamWhenEmpty() {
		assertThat(empty.columns().stream())
			.isEmpty();
	}
	
	@Test
	void shouldReturnAWellSizedStream() {
		assertThat(people.columns().stream()).size().isEqualTo(3);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldReturnAnOrderedStream() {
		List<Column<?>> columns = people.columns().stream().collect(toList());
		
		SoftAssertions softly = new SoftAssertions();
		
		softly.assertThat((Column<String>) columns.get(0)).containsExactly("Luc", "Baptiste", "Anya", "Mathilde");
		softly.assertThat((Column<Integer>) columns.get(1)).containsExactly(23, 32, 0, 21);
		softly.assertThat((Column<String>) columns.get(2)).containsExactly("Male", "Male", "Female", "Female");
		
		softly.assertAll();
	}
	
	// clear()
	
	@Test
	void shouldRemoveAllItsColumnsOnClear() {
		people.columns().clear();
		assertThat(people.columns()).isEmpty();
	}
	
	@Test
	void shouldNotAlterNumberOfRowsOnClear() {
		people.columns().clear();
		assertThat(people.rows()).size().isEqualTo(4);
	}
	
	@Test
	void shouldLeaveEmptyRowsOnClear() {
		people.columns().clear();
		assertThat(people.rows()).allMatch(Row::isEmpty);
	}
}
