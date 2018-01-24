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
class WhereBoolTest {
	
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
					.where().asBool().isNonNull() // throwaway filter
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
		private static final String SEX_HEADER = "isFemale";
		
		private final ColumnId<String> NAME = id(NAME_HEADER, String.class);
		private final ColumnId<Integer> AGE = id(AGE_HEADER, Integer.class);
		private final ColumnId<Boolean> SEX = id(SEX_HEADER, Boolean.class);
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(NAME_HEADER, String.class, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(AGE_HEADER, Integer.class, 23, 32, 0, 21)
					.create(SEX_HEADER, Boolean.class, false, false, true, true);
		}
		
		@Test @DisplayName("can keep only true values")
		void can_keep_only_true_values() {
			Table result = Query
					.from(people)
					.where("isFemale").asBool().isTrue()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(0, 21);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Anya", "Mathilde");
			softly.assertThat(result.columns().get(SEX)).containsExactly(true, true);
			softly.assertAll();
		}
		
		@Test @DisplayName("can keep only false values")
		void can_keep_only_false_values() {
			Table result = Query
					.from(people)
					.where("isFemale").asBool().isFalse()
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.columns().get(AGE)).containsExactly(23, 32);
			softly.assertThat(result.columns().get(NAME)).containsExactly("Luc", "Baptiste");
			softly.assertThat(result.columns().get(SEX)).containsExactly(false, false);
			softly.assertAll();
		}
	}
}
