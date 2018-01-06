package fr.kazejiyu.generic.datatable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.kazejiyu.generic.datatable.core.Column;
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
				.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Mathilde")
				.create(Integer.class, AGE_HEADER, 23, 32, 21)
				.create(String.class, SEX_HEADER, "Male", "Male", "Female");
	}
	
	// isEmpty()
	
	@Test
	void shouldBeEmptyByDefault() {
		assertThat(empty.rows().isEmpty()).isTrue();
	}
	
	@Test
	void shouldNotBeEmptyWhenFilled() {
		assertThat(people.rows().isEmpty()).isFalse();
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
	@SuppressWarnings("unchecked")
	void canReturnItsFirstRow() {
		assertThat((Column<Object>) people.rows().first())
			.containsExactly("Luc", "25", "Male");
	}
	
	@Test
	void shouldThrowWhenFirstColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(() -> empty.rows().first());
	}
	
	// last()
	
	@Test
	@SuppressWarnings("unchecked")
	void canReturnItsLastColumn() {
		assertThat((Column<Object>) people.rows().last())
			.containsExactly("Mathilde", 21, "Female");
	}
	
	@Test
	void shouldThrowWhenLastColumnIsAskForWhileEmpty() {
		assertThatExceptionOfType(IndexOutOfBoundsException.class)
			.isThrownBy(() -> empty.rows().last());
	}
}
