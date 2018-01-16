package fr.kazejiyu.generic.datatable;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.kazejiyu.generic.datatable.core.Table;
import fr.kazejiyu.generic.datatable.core.impl.DataTable;
import fr.kazejiyu.generic.datatable.query.Query;

/**
 * Tests the behavior of {@link DataTable} instances.
 * 
 * @author Emmanuel CHEBBI
 */
@DisplayName("A Query")
class DslTest {
	
	@Nested
	@DisplayName("on an empty table")
	class Empty {
		private Table empty;
		
		@BeforeEach
		void initializeEmptyTable() {
			empty = new DataTable();
		}
		
		@Test @DisplayName("returns an empty table")
		void returnsAnEmptyTable() {
			Table result = Query
					.from(empty)
					.where().isNonNull() // throwaway filter
					.select();
			
			assertThat(result.isEmpty()).isTrue();
		}
	}
	
	@Nested
	@DisplayName("when not empty")
	class NonEmpty {
		private Table people;
		
		private static final String AGE_HEADER = "AGE";
		private static final String NAME_HEADER = "name";
		private static final String SEX_HEADER = "sEx";
		
		@BeforeEach
		void initializePeopleTable() {
			people = new DataTable();
			people.columns()
					.create(String.class, NAME_HEADER, "Luc", "Baptiste", "Anya", "Mathilde")
					.create(Integer.class, AGE_HEADER, 23, 32, 0, 21)
					.create(String.class, SEX_HEADER, "Male", "Male", "Female", "Female");
		}
		
		@Test @DisplayName("can filter a table upon a criteria")
		void returnsAFilteredTable() {
			Table result = Query
					.from(people)
					.where("name").asStr().endsWith("e")
					.select();
			
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(result.rows()).size().isEqualTo(2);
			softly.assertThat(result.rows().get(0)).containsExactly("Baptiste", 32, "Male");
			softly.assertThat(result.rows().get(1)).containsExactly("Mathilde", 21, "Female");
			softly.assertAll();
		}
	}
}
