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
@DisplayName("A Query's WhereNumber clause")
class WhereNumberTest {
	
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
					.where().asNumber().isNonNull() // throwaway filter
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
					.create(NAME_HEADER, String.class, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(AGE_HEADER, Integer.class, 23, -32, 0, 21)
					.create(SEX_HEADER, String.class, "Male", "Male", "Female", "Female");
		}
		
		@Test @DisplayName("can keep only 0")
		void can_keep_only_zeros() {
			Table result = Query
					.from(people)
					.where("age").asNumber().isZero()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only positive values")
		void can_keep_only_positive_values() {
			Table result = Query
					.from(people)
					.where("age").asNumber().isPositive()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only negative values")
		void can_keep_only_negative_values() {
			Table result = Query
					.from(people)
					.where("age").asNumber().isNegative()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(-32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Baptiste");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only even values")
		void can_keep_only_even_values() {
			Table result = Query
					.from(people)
					.where("age").asNumber().isEven()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(-32, 0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Baptiste", "Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only odd values")
		void can_keep_only_odd_values() {
			Table result = Query
					.from(people)
					.where("age").asNumber().isOdd()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values that are in a closed range")
		void can_keep_only_values_that_are_in_a_closed_range() {
			Table result = Query
					.from(people)
					.where("age").asNumber().inClosedInterval(-32, 21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(3);
			softly.assertThat(result.columns().get(AGE)).containsExactly(-32, 0, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Baptiste", "Anya", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values that are in an open range")
		void can_keep_only_values_that_are_in_an_open_range() {
			Table result = Query
					.from(people)
					.where("age").asNumber().inOpenInterval(-32, 21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values lower than a specific number")
		void can_keep_only_values_lower_than_a_specific_number() {
			Table result = Query
					.from(people)
					.where("age").asNumber().lt(21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(-32, 0);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Baptiste", "Anya");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values greater than a specific number")
		void can_keep_only_values_greater_than_a_specific_number() {
			Table result = Query
					.from(people)
					.where("age").asNumber().gt(21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values lower than or equal to a specific number")
		void can_keep_only_values_lower_than_or_equal_to_a_specific_number() {
			Table result = Query
					.from(people)
					.where("age").asNumber().le(21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(3);
			softly.assertThat(result.columns().get(AGE)).containsExactly(-32, 0, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Baptiste", "Anya", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only values greater than or equal to a specific number")
		void can_keep_only_values_greater_than_or_equal_to_a_specific_number() {
			Table result = Query
					.from(people)
					.where("age").asNumber().ge(21)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
	}
}
