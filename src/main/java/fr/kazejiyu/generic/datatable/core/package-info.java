/**
 * Describes a Table data structure.
 * 
 * <h3>ColumnId</h3>
 * 
 * The term <em>id</em> refers to instances of {@link fr.kazejiyu.generic.datatable.core.impl.ColumnId ColumnId}. <br>
 * An id is a trick used to reference a table's column in a type-safe way and can be created with the
 * {@link fr.kazejiyu.generic.datatable.core.impl.ColumnId#id(String, Class) ColumnId.id(String,Class)} static method. <br>
 * <br>
 * For instance, the following id references any column which name is "age" and which stores integers: <br>
 * <br>
 * {@code ColumnId<Integer> age = id("name", Integer.class);}
 * 
 * <h3>Populating a Table</h3>
 * 
 * Creation of a new {@link fr.kazejiyu.generic.datatable.core.Table Table} consists of an easy one-liner : <br> 
 * <br>
 * {@code Table people = new DataTable();} <br>
 * <br>
 * A table can be filled either by adding new columns :
 * 
 * <pre>people.columns()
 *   .create("Name", String.class, "Luc", "Baptiste", "Marie")
 *   .create("Age", Integer.class, 23, 32, 42);}</pre>
 *
 * or by adding new rows : 
 *
 * <pre>people.rows()
 *    .create("Julie", 12)
 *    .create("Mathieu", 67);</pre>
 *  
 * <h3>Depopulating a Table</h3>
 * 
 * A table can be emptied either by removing columns:
 * 
 * <pre>people.columns()
 *    .remove("age");</pre>
 *    
 * <h3>Querying a Table</h3>
 * 
 * The {@link Table#filter(ca.odell.glazedlists.matchers.Matcher) Table.filter(Matcher)} makes easy to retrieve
 * the rows of a table that match a specific criterion:
 * 
 * <pre>ColumnId&lt;Integer&gt; AGE = id(Integer.class, "age");
 *ColumnId&lt;String&gt; NAME = id(String.class, "name");
 *
 *Table adultsWhoseNameEndsWithLetterE(Table people) {
 *    return people.filter(row -&gt;
 *        row.get(AGE) &gt; 18 &amp;&amp;
 *        row.get(NAME).startsWith("E")
 *    );
 *}</pre>
 *
 * For more complex cases, the API also defines a SQL-like DSL that makes possible to apply a same filter on mutliple
 * columns. See {@link fr.kazejiyu.generic.datatable.query} for an overview.
 *    
 * @author Emmanuel CHEBBI
 */
package fr.kazejiyu.generic.datatable.core;