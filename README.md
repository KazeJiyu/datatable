# Heterogeneous and type-safe tables

![build status](https://travis-ci.org/KazeJiyu/datatable.svg?branch=master) [![codecov](https://codecov.io/gh/KazeJiyu/datatable/branch/master/graph/badge.svg)](https://codecov.io/gh/KazeJiyu/datatable)

# Motivation
This project is aimed to experiment with the implementation of heterogeneous yet type-safe table structure.

As of now, the API makes able to:

- create heterogeneous tables,
- modify them by adding either rows or columns,
- query their content in a type-safe way. 

# Full example

Because code is better than words, here is an overview of the API:

```java
Table people = new DataTable();
people.columns()
    .create(String.class, "Name", "Luc", "Baptiste", "Marie")
    .create(Integer.class, "Age", 23, 32, 42);

// Used to identify table's columns in a type-safe way    
ColumnId<Integer> AGE = id(Integer.class, "age");
ColumnId<String> NAME = id(String.class, "name");
	
Table adultsWhoseNameEndsWithLetterE = people.filter(row ->
    row.get(AGE) > 18 &&
    row.get(NAME).startsWith("E")
);
```

# Quick start

## Populating a `Table`

Creation of a new [Table](https://github.com/KazeJiyu/datatable/blob/master/src/main/java/fr/kazejiyu/generic/datatable/core/Table.java) consists of an easy one-liner : 

```java
Table people = new DataTable();
```

A table can be filled either by adding new columns :

```java
people.columns()
    .create(String.class, "Name", "Luc", "Baptiste", "Marie")
    .create(Integer.class, "Age", 23, 32, 42);
```

or by adding new rows : 

```java
people.rows()
    .create("Julie", 12)
    .create("Mathieu", 67);
```
   
The table resulting of the above statements is the following :

```java
+----------+-----+
|   Name   | Age |
+----------+-----|
|   Luc    | 23  |
| Baptiste | 32  |
|   Marie  | 42  |
|   Julie  | 12  |
|  Mathieu | 67  |
+----------+-----+
```

## Depopulating a `Table`

A table can be emptied either by removing columns :

```java
people.columns()
    .remove("age");
```

or by removing rows :

```java
people.rows()
   .remove(4)
   .remove(2);
```

Notice how the last row has been removed first in order to avoid bugs related to changes of ids. The table now looks like :

```java
+----------+
|   Name   |
+----------+
|   Luc    |
| Baptiste |
|   Julie  |
+----------+
```

## Querying a `Table`

### Standard way

The `filter` method makes easy to retrieve the rows of a table that match a specific criterion :

```java
Table adultsWhoseNameEndsWithLetterE(Table people) {
    return people.filter( row ->
        (int) row.get("age") > 18 &&
        ((String) row.get("name")).endsWith("e")
    );
}
```

Since the type of a column's elements is only known at runtime, the cast is mandatory within the lambda expression. However, since dealing with numerous casts can be really tedious, the API provides a type-safe way to query a `Table`. 

### Type safety

A column can be identified by a [ColumnId](https://github.com/KazeJiyu/datatable/blob/master/src/main/java/fr/kazejiyu/generic/datatable/core/impl/ColumnIterator.java), which is a simple structure containing the type of the column's elements and its header.

An id can be get with the `id` static method :
```java
// Matches any column that contains Strings and which header is "name"
ColumnId<String> name = ColumnId.id(String.class, "name");
```

The following snippet shows how the `filter` method can benefit from this feature:

```java
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

public class Main {
	
    // Ids that match table's columns
    private static final ColumnId<Integer> AGE = id(Integer.class, "age");
    private static final ColumnId<String> NAME = id(String.class, "name");
    
    // no need for casting anymore
    Table adultsWhoseNameEndsWithLetterE(Table people) {
        return people.filter(row ->
            row.get(AGE) > 18 &&
            row.get(NAME).startsWith("E")
        );
    }
}
```

## SQL-like DSL

### Standard way

For more complex cases, the API also brings a SQL-like DSL that makes possible to apply a same filter to multiple columns. It can be used as follows :

```java
// Retrieve all european adults whose name and surename ends with the letter "e"
Table adultsWhoseNameEndsWithLetterE(Table people) {
    return Query
        .from(people)
        .where("Name", "Surename").asStr().endsWith("e")
        .and("Age").asNumber().gt(18)
        .and("Country").match(country -> ((Country) country).isInEurope())
        .select();
}
```

The `as*` methods provide tailored methods, making easier to build explicit queries. As of now, four of them are available :

- `asStr()` to indicate that the column contains `String`s,
- `asBool()` to indicate that the column contains `Boolean`s,
- `asNumber()` to indicate that the column contains `Number`s,
- `as(Class<T>)` to indicate that the column contains `T`s.

__Important__: these methods do not perform dynamic casts. If the column's elements are not of the right type, a `ClassCastException` is thrown.

Again, the API provides a way to avoid the explicit cast.

### Type safety

A clearer way to use the DSL is the following:

```java
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

public class Main {
	
    // Ids that match table's columns
    private static final ColumnId<Integer> AGE = id(Integer.class, "age");
    private static final ColumnId<String> NAME = id(String.class, "name");
    private static final ColumnId<String> SURENAME = id(String.class, "surename");

    // Retrieve all european adults whose name and surename ends with the letter "e"
    Table adultsWhoseNameEndsWithLetterE(Table people) {
        return Query
            .from(people)
            .where(s(NAME, SURENAME)).endsWith("e")
            .and(AGE).gt(18)
            .and(COUNTRY).match(Country::isInEurope)
            .select();
    }
}
```

Notice how the `s` method is used to specify several columns of type String to the `WHERE`clause. Due to Java's type erasure, this trick is mandatory. This static method is defined in the `ColumnId` class, as well as the methods:

- `n`: used to specify several methods of type Number,
- `b`: used to specify several methods of type Boolean.