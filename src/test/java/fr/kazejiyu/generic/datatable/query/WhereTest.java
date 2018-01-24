package fr.kazejiyu.generic.datatable.query;

import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;
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
@DisplayName("A Query's Where clause")
class WhereTest {
	
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
					.where().isNonNull() // throwaway filter
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
		
		private final ColumnId<String> NAME = id(NAME_HEADER, String.class);
		private final ColumnId<Integer> AGE = id(AGE_HEADER, Integer.class);
		private final ColumnId<String> SEX = id(SEX_HEADER, String.class);
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(NAME_HEADER, String.class, "Luc", null, "Anya", "Mathilde")
					.create(AGE_HEADER, Integer.class, 23, 32, 0, 21)
					.create(SEX_HEADER, String.class, "Male", "Male", "Female", "Female");
		}
		
		@Test @DisplayName("can apply filters on null values without throwing")
		void can_apply_filter_on_null_values_without_throwing() {
			Table result = Query
					.from(people)
					.where(NAME).matchSafe(name -> name.startsWith("M"))
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values equal to another one")
		void can_keep_only_values_equal_to_another_one() {
			Table result = Query
					.from(people)
					.where(NAME).eq("Mathilde")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values not equal to another one")
		void can_keep_only_values_not_equal_to_another_one() {
			Table result = Query
					.from(people)
					.where(NAME).ne("Mathilde")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(3);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 32, 0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", null, "Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only null values")
		void can_keep_only_null_values() {
			Table result = Query
					.from(people)
					.where("name").isNull()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly(new String[] { null });
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only not null values")
		void can_keep_only_not_null_values() {
			Table result = Query
					.from(people)
					.where("name").isNonNull()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(3);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 0, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Anya", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep values of a specific type")
		void can_keep_values_of_a_specific_type() {
			Table result = Query
					.from(people)
					.where("name").isInstanceOf(String.class)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(3);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 0, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Anya", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can avoid values of a specific type")
		void can_avoid_values_of_a_specific_type() {
			Table result = Query
					.from(people)
					.where("name").isInstanceOf(Double.class)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(0);
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only specific values")
		void can_keep_only_specific_values() {
			Table result = Query
					.from(people)
					.where(NAME).in("Luc", "Mathilde")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can avoid only specific values")
		void can_avoid_only_specific_values() {
			Table result = Query
					.from(people)
					.where(NAME).notIn("Luc", "Mathilde")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32, 0);
			softly.assertThat(result.columns().get(NAME)).containsExactly(null, "Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can filter values of any type")
		void can_filter_values_of_any_type() {
			Table result = Query
					.from(people)
					.where("name").as(String.class).matchSafe(name -> name.endsWith("c"))
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can filter from a ColumnOfStringsId")
		void can_filter_from_a_column_of_strings_id() {
			Table result = Query
					.from(people)
					.where(s(NAME)).endsWith("c")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can filter from an array of ColumnOfStringsId")
		void can_filter_from_an_array_of_column_of_strings_id() {
			Table result = Query
					.from(people)
					.where(s(NAME, SEX)).endsWith("e")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can filter from a ColumnOfNumbersId")
		void can_filter_from_a_column_of_numbers_id() {
			Table result = Query
					.from(people)
					.where(n(AGE)).isZero()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can filter from an array of ColumnOfNumbersIds")
		void can_filter_from_an_array_of_column_of_numbers_ids() {
			people.columns().create("Size", Integer.class, 122, 0, 42, -45);
			ColumnId<Integer> SIZE = id("Size", Integer.class);
			
			Table result = Query
					.from(people)
					.where(n(AGE, SIZE)).isPositive()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
	}
}
