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
import org.junit.jupiter.api.Test;
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
public class SimpleRowsTest {
	
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
		assertThat(empty.rows()).isEmpty();
	}
	
	@Test
	void shouldNotBeEmptyWhenFilled() {
		assertThat(people.rows()).isNotEmpty();
	}
	
	// size()
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 20, 100})
	void shouldHaveTheRightSize(int nbRows) {
		for( int i = 0 ; i < nbRows ; i++ )
			empty.rows().create();
		
		assertThat(empty.rows().size())
			.as("with " + nbRows + " rows")
			.isEqualTo(nbRows);
	}
	
	// create()
	
	@ParameterizedTest
	@MethodSource("createWrongSizedRows")
	void shouldThrowWhenCreatingAWrongSizedRow(List<Object> row) {
		assertThatExceptionOfType(InconsistentRowSizeException.class)
			.isThrownBy(() -> people.rows().create(row));
	}
	
	@SuppressWarnings("unused")
	private static Stream<List<Object>> createWrongSizedRows() {
		return Stream.of(
			asList(),
			asList(1),
			asList(1, 2, 3, 4, 5)
		);
	}
	
	@ParameterizedTest
	@CsvSource({"'name', 'age', 'M'", "12, 23, 'F'", "'name', 42, 12"})
	void shouldThrowWhenCreatingARowWithWrongContent(Object name, Object age, Object sex) {
		assertThatExceptionOfType(ClassCastException.class)
			.isThrownBy(() -> people.rows().create(name, age, sex));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void shouldAppendCreatedRowsAtTheEnd() {
		people.rows().create("Eva", 21, "Female");
		
		assertThat(people.rows().last())
			.containsExactly("Eva", 21, "Female");
	}
	
	// first()
	
	@Test
	void canReturnItsFirstRow() {
		assertThat(people.rows().first())
			.containsExactly("Luc", 23, "Male");
	}
	
	@Test
	void shouldThrowWhenFirstColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.rows()::first);
	}
	
	// last()
	
	@Test
	void canReturnItsLastColumn() {
		assertThat(people.rows().last())
			.containsExactly("Mathilde", 21, "Female");
	}
	
	@Test
	void shouldThrowWhenLastColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(empty.rows()::last);
	}
	
	// iterator()
	
	@Test
	void shouldReturnALoneIteratorWhenEmpty() {
		assertThat(empty.rows().iterator())
			.isNotNull()
			.isEmpty();
	}
	
	@Test
	void shouldReturnAWellSizeIterator() {
		assertThat(people.rows().iterator()).size().isEqualTo(4);
	}
	
	@Test
	void shouldReturnAnOrderedIterator() {
		Iterator<Row> itRows = people.rows().iterator();
		
		SoftAssertions softly = new SoftAssertions();
		
		softly.assertThat(itRows.next()).containsExactly("Luc", 23, "Male");
		softly.assertThat(itRows.next()).containsExactly("Baptiste", 32, "Male");
		softly.assertThat(itRows.next()).containsExactly("Anya", 0, "Female");
		softly.assertThat(itRows.next()).containsExactly("Mathilde", 21, "Female");
		
		softly.assertAll();
	}
	
	// stream()
	
	@Test
	void shouldReturnALoneStreamWhenEmpty() {
		assertThat(empty.rows().stream())
			.isEmpty();
	}
	
	@Test
	void shouldReturnAWellSizedStream() {
		assertThat(people.rows().stream()).size().isEqualTo(4);
	}
	
	@Test
	void shouldReturnAnOrderedStream() {
		List<Row> rows = people.rows().stream().collect(toList());
		
		SoftAssertions softly = new SoftAssertions();
		
		softly.assertThat(rows.get(0)).containsExactly("Luc", 23, "Male");
		softly.assertThat(rows.get(1)).containsExactly("Baptiste", 32, "Male");
		softly.assertThat(rows.get(2)).containsExactly("Anya", 0, "Female");
		softly.assertThat(rows.get(3)).containsExactly("Mathilde", 21, "Female");
	
		softly.assertAll();
	}
	
	// clear()
	
	@Test
	void shouldRemoveAllItsRowsOnClear() {
		people.rows().clear();
		assertThat(people.rows()).isEmpty();
	}
	
	@Test
	void shouldNotAlterNumberOfColumnsOnClear() {
		people.rows().clear();
		assertThat(people.columns()).size().isEqualTo(3);
	}
	
	@Test
	void shouldLeaveEmptyColumnsOnClear() {
		people.rows().clear();
		assertThat(people.columns()).allMatch(Column::isEmpty);
	}
}
