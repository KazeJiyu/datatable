package fr.kazejiyu.generic.datatable.query;

import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.id;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.ColumnId;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A Query's WhereStr clause")
class WhereStrTest {
	
	@Nested
	@DisplayName("on an empty table")
	class Empty {
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		@Test @DisplayName("returns an empty table")
		void returns_an_empty_table() {
			Table result = Query
					.from(empty)
					.where().asStr().isNonNull() // throwaway filter
					.select();
			
			assertThat(result.isEmpty()).isTrue();
		}
	}
	
	@Nested
	@DisplayName("on a not empty table")
	class NonEmpty {
		private Table people;
		
		private static final String AGE_HEADER = "AGE";
		private static final String NAME_HEADER = "name";
		private static final String SEX_HEADER = "sEx";
		
		private final ColumnId<String> NAME = id(String.class, NAME_HEADER);
		private final ColumnId<Integer> AGE = id(Integer.class, AGE_HEADER);
		private final ColumnId<String> SEX = id(String.class, SEX_HEADER);
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(NAME_HEADER, String.class, "Luc", "", "anya", "MATHILDE")
					.create(AGE_HEADER, Integer.class, 23, 32, 0, 21)
					.create(SEX_HEADER, String.class, "Male", "Male", "Female", "Female");
		}
		
		@Test @DisplayName("can keep only empty Strings")
		void can_keep_only_empty_strings() {
			Table result = Query
					.from(people)
					.where("name").asStr().isEmpty()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep specific Strings without case consideration")
		void can_keep_specific_strings_without_case_consideration() {
			Table result = Query
					.from(people)
					.where("name").asStr().equalsIgnoreCase("LuC")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only lower-case Strings")
		void can_keep_only_lower_case_strings() {
			Table result = Query
					.from(people)
					.where("name").asStr().isInLowerCase()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32, 0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("", "anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only upper-case Strings")
		void can_keep_only_upper_case_strings() {
			Table result = Query
					.from(people)
					.where("name").asStr().isInUpperCase()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("", "MATHILDE");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only Strings containing a specific sequence")
		void can_keep_only_strings_containing_a_specific_sequence() {
			Table result = Query
					.from(people)
					.where("name").asStr().contains("nya")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only Strings starting with a specific sequence")
		void can_keep_only_strings_starting_with_a_specific_sequence() {
			Table result = Query
					.from(people)
					.where("name").asStr().startsWith("Lu")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only Strings ending with a specific sequence")
		void can_keep_only_strings_ending_with_a_specific_sequence() {
			Table result = Query
					.from(people)
					.where("name").asStr().endsWith("LDE")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("MATHILDE");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
	}
}
