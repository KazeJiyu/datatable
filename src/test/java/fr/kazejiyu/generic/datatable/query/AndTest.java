package fr.kazejiyu.generic.datatable.query;

import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

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
@DisplayName("A Query's And clause")
class AndTest {
	
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
					.create(NAME_HEADER, String.class, "Luc", "Martin", "Anya", "Mathilde")
					.create(AGE_HEADER, Integer.class, 23, 32, 0, 21)
					.create(SEX_HEADER, String.class, "Male", "Male", "Female", "Female");
		}
		
		@Test @DisplayName("can apply a filter on all columns")
		void can_apply_a_filter_on_all_columns() {
			people.rows().get(0).set(NAME, null);
			
			Table result = Query
					.from(people)
					.where(n(AGE)).isPositive()
					.and().isNonNull()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male", "Female");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on named columns")
		void can_apply_a_filter_on_named_columns() {
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(NAME_HEADER, SEX_HEADER).asStr().startsWith("M")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on a ColumnId")
		void can_apply_a_filter_on_a_column_id() {
			Table result = Query
					.from(people)
					.where(s(NAME)).startsWith("M")
					.and(AGE).asNumber().gt(22)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on a ColumnOfStringsId")
		void can_apply_a_filter_on_a_column_of_strings_id() {
			Table result = Query
					.from(people)
					.where(n(AGE)).gt(22)
					.and(s(NAME)).startsWith("M")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on a ColumnOfNumbersId")
		void can_apply_a_filter_on_a_column_of_numbers_id() {
			Table result = Query
					.from(people)
					.where(s(NAME)).startsWith("M")
					.and(n(AGE)).gt(22)
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on an array of ColumnOfStringsIds")
		void can_apply_a_filter_on_an_array_of_column_of_strings_ids() {
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(s(NAME, SEX)).startsWith("M")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can apply a filter on an array of ColumnOfNumbersIds")
		void can_apply_a_filter_on_an_array_of_column_of_numbers_ids() {
			people.columns().create("Size", Integer.class, 122, 0, 42, -45);
			ColumnId<Integer> SIZE = id(Integer.class, "Size");
			
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(n(SIZE, AGE)).isPositive()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc");
			softly.assertThat(result.columns().get(SEX)).containsExactly("Male");
			softly.assertAll();
		}
		
		@Test @DisplayName("can select only specific columns")
		void can_select_only_specific_columns() {
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(s(NAME, SEX)).startsWith("M")
					.select("name", "age");
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.columns().size()).isEqualTo(2);
			softly.assertThat(result.rows().size()).isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().contains(SEX_HEADER)).isFalse();
			softly.assertAll();
		}
		
		@Test @DisplayName("can select only one id")
		void can_select_only_one_id() {
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(s(NAME, SEX)).startsWith("M")
					.select(AGE);
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.columns().size()).isEqualTo(1);
			softly.assertThat(result.rows().size()).isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().contains(NAME)).isFalse();
			softly.assertThat(result.columns().contains(SEX_HEADER)).isFalse();
			softly.assertAll();
		}
		
		@Test @DisplayName("can select only specific ids")
		void can_select_only_specific_ids() {
			Table result = Query
					.from(people)
					.where().isNonNull()
					.and(s(NAME, SEX)).startsWith("M")
					.select(NAME, AGE);
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.columns().size()).isEqualTo(2);
			softly.assertThat(result.rows().size()).isEqualTo(1);
			softly.assertThat(result.columns().get(AGE)).containsExactly(32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Martin");
			softly.assertThat(result.columns().contains(SEX_HEADER)).isFalse();
			softly.assertAll();
		}
	}
}
