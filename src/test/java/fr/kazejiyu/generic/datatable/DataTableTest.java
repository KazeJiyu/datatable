package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

import java.util.LinkedHashSet;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.exceptions.HeaderNotFoundException;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A DataTable")
class DataTableTest {
	
	@Nested
	@DisplayName("when empty")
	class Empty {
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		// isEmpty()
		
		@Test @DisplayName("is empty")
		void is_empty() {
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty rows")
		void is_empty_with_only_empty_rows() {
			empty.rows().create()
						.create();
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		@Test @DisplayName("is still empty when adding empty columns")
		void is_empty_with_only_empty_columns() {
			empty.columns().create("Empty Column")
						   .create("Another Empty Column");
			
			assertThat(empty.isEmpty()).isTrue();
		}
		
		// rows()
		
		@Test @DisplayName("returns a non-null Rows instance")
		void returns_non_null_rows() {
			assertThat(empty.rows()).isNotNull();
		}
		
		// columns()
		
		@Test @DisplayName("returns a non-null Columns instance")
		void returns_non_null_columns() {
			assertThat(empty.columns()).isNotNull();
		}
		
		// filter()
		
		@Test @DisplayName("returns an an equivalent table when filtered")
		void returns_an_empty_table_when_filtered() {
			assertThat(empty.filter(row -> true)).isEqualTo(empty);
		}
		
		// hashCode()
		
		@Test @DisplayName("has the same hashCode as a new datatable")
		void has_the_same_hashCode_as_a_new_datatable() {
			assertThat(empty.hashCode()).isEqualTo(new DataTable().hashCode());
		}
		
		// equals()
		
		@Test @DisplayName("is equal to itself")
		void is_equal_to_itself() {
			assertThat(empty).isEqualTo(empty);
		}
		
		@Test @DisplayName("is equal to a new datatable")
		void is_equal_to_a_new_datatable() {
			assertThat(empty).isEqualTo(new DataTable());
		}
	}
	
	@Nested
	@DisplayName("when not empty")
	class NonEmpty {
		private Table people;
		
		private final ColumnId<String> NAME = id(String.class, NAME_HEADER);
		
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
			assertThat(people.isEmpty()).isFalse();
		}
		
		// clear()
		
		@Test @DisplayName("removes all its rows when cleared")
		void removes_all_its_rows_when_cleared() {
			people.clear();
			assertThat(people.rows().isEmpty()).isTrue();
		}
		
		@Test @DisplayName("removes all its columns when cleared")
		void removes_all_its_columns_when_cleared() {
			people.clear();
			assertThat(people.columns().isEmpty()).isTrue();
		}
		
		// hashCode
		
		@Test @DisplayName("has the same hashCode as an identical table")
		void has_the_same_hashCode_as_an_identical_table() {
			assertThat(people.hashCode()).isEqualTo(createPeople().hashCode());
		}
		
		// equals()
		
		@Test @DisplayName("is equal to another table that has the same content")
		void is_equal_to_another_table_that_has_the_same_content() {
			assertThat(people).isEqualTo(createPeople());
		}
		
		@Test @DisplayName("is not equal to another table that almost has the same content")
		void is_not_equal_to_another_table_that_almost_has_the_same_content() {
			Table almostPeople = createPeople();
			almostPeople.rows().last().set(AGE_HEADER, 200);
			
			assertThat(people).isNotEqualTo(almostPeople);
		}
		
		@Test @DisplayName("is not equal to null")
		void is_not_equal_to_null() {
			assertThat(people).isNotEqualTo(null);
		}
		
		@Test @DisplayName("is not equal to a non DataTable object")
		void is_not_equal_to_a_non_DataTable_object() {
			assertThat(people).isNotEqualTo("not a datatable");
		}
		
		// filter()
		
		@Test @DisplayName("throws when filter on non existing header")
		void throws_when_filtering_on_non_existing_header() {
			LinkedHashSet<String> headers = new LinkedHashSet<>();
			headers.add(NAME_HEADER);
			headers.add("non existing");
			headers.add(SEX_HEADER);
			
			assertThatExceptionOfType(HeaderNotFoundException.class)
				.isThrownBy(() -> people.filter(headers, r -> true));
		}
		
		@Test @DisplayName("can filter only specific columns")
		void can_filter_only_specific_columns() {
			LinkedHashSet<String> headers = new LinkedHashSet<>();
			headers.add(NAME_HEADER);
			headers.add(AGE_HEADER);
			
			Table result = people.filter(headers, row ->
				row.get(NAME).endsWith("e")
			);
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows().size()).isEqualTo(2);
			softly.assertThat(result.columns().size()).isEqualTo(2);
			softly.assertThat(result.rows().get(0)).containsExactly("Baptiste", 32);
			softly.assertThat(result.rows().get(1)).containsExactly("Mathilde", 21);
			softly.assertAll();	
		}
	}
}
