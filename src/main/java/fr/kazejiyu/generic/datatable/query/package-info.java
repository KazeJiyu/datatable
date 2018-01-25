/**
 * Describes a SQL-like DSL to query the content of a {@link fr.kazejiyu.generic.datatable.core.Table Table}. 
 *
 * <h3>SQL-like DSL</h3>
 * 
 * The API can be used as follows:
 * 
 * <pre>ColumnId&lt;Integer&gt; AGE = id(Integer.class, "age");
 *ColumnId&lt;String&gt; NAME = id(String.class, "name");
 *ColumnId&lt;String&gt; SURENAME = id(String.class, "surename");
 *
 *Table europeanAdultsWhoseNameEndsWithLetterE(Table people) {
 *    return Query
 *        .from(people)
 *        .where(s(NAME, SURENAME)).endsWith("e")
 *        .and(AGE).gt(18)
 *        .and(COUNTRY).match(Country::isInEurope)
 *        .select();
 *}</pre>
 * 
 * @author Emmanuel CHEBBI
 */
package fr.kazejiyu.generic.datatable.query;