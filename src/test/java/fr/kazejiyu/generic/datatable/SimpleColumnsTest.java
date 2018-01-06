package fr.kazejiyu.generic.datatable;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.kazejiyu.generic.datatable.core.Column;
import fr.kazejiyu.generic.datatable.core.Columns;
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
				.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Mathilde")
				.create(Integer.class, AGE_HEADER, 23, 32, 21)
				.create(String.class, SEX_HEADER, "Male", "Male", "Female");
	}
	
	// isEmpty()
	
	@Test
	void shouldBeEmptyByDefault() {
		assertThat(empty.columns().isEmpty()).isTrue();
	}
	
	@Test
	void shouldNotBeEmptyWhenFilled() {
		assertThat(people.columns().isEmpty()).isFalse();
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
		people.columns().create(Double.class, "SomeValue", 0.6, 12d, 0d);
		
		assertThat((Column<Double>) people.columns().last())
			.containsExactly(.6, 12d, 0d);
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
			.containsExactly("Luc", "Baptiste", "Mathilde");
	}
	
	@Test
	void shouldThrowWhenFirstColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(() -> empty.columns().first());
	}
	
	// last()
	
	@Test
	@SuppressWarnings("unchecked")
	void canReturnItsLastColumn() {
		assertThat((Column<Object>) people.columns().last())
			.containsExactly("Male", "Male", "Female");
	}
	
	@Test
	void shouldThrowWhenLastColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(() -> empty.columns().last());
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
			.isNotNull()
			.isEmpty();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldReturnAnOrderedIterator() {
		Iterator<Column<?>> itRows = people.columns().iterator();
		
		assertThat((Column<String>) itRows.next()).containsExactly("Luc", "Baptiste", "Mathilde");
		assertThat((Column<Integer>) itRows.next()).containsExactly(23, 32, 21);
		assertThat((Column<String>) itRows.next()).containsExactly("Male", "Male", "Female");
		
		assertThat(itRows.hasNext()).isFalse();
	}
	
	// stream()
	
	@Test
	void shouldReturnALoneStreamWhenEmpty() {
		assertThat(empty.columns().stream())
			.isEmpty();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldReturnAnOrderedStream() {
		List<Column<?>> columns = people.columns().stream().collect(toList());
		
		assertThat(columns).size().isEqualTo(3);
		
		assertThat((Column<String>) columns.get(0)).containsExactly("Luc", "Baptiste", "Mathilde");
		assertThat((Column<Integer>) columns.get(1)).containsExactly(23, 32, 21);
		assertThat((Column<String>) columns.get(2)).containsExactly("Male", "Male", "Female");
	}
}
