# Heterogeneous and type-safe tables

![build status](https://travis-ci.org/KazeJiyu/datatable.svg?branch=master) [![codecov](https://codecov.io/gh/KazeJiyu/datatable/branch/master/graph/badge.svg)](https://codecov.io/gh/KazeJiyu/datatable)

# Motivation
This project is aimed to experiment with the implementation of heterogeneous yet type-safe table structure.

As of now, the API makes able to:

- create heterogeneous tables,
- modify them and their content,
- query their content in a type-safe way. 

# Full example

Because code is better than words, here is a quick overview of the API:

```java
ColumnId<Integer> AGE = id(Integer.class, "age");
ColumnId<String> NAME = id(String.class, "name");

Table people = new DataTable();
people.columns()
    .create(NAME, "Luc", "Baptiste", "Marie")
    .create(AGE, 23, 32, 42);
	
Table adultsWhoseNameEndsWithLetterE = people.filter(row ->
    row.get(AGE) > 18 &&
    row.get(NAME).startsWith("E")
);
```

# Quick start

## Understanding ids

Ids are a trick used to provide a type-safe way to access the content of a table.

Basically, they are simple data structure containing:

- a `type`, that is the Java class of the column's elements
- a `header`, that is the name of the column.

An id is represented by the [ColumnId](https://github.com/KazeJiyu/datatable/blob/master/src/main/java/fr/kazejiyu/generic/datatable/core/impl/ColumnId.java) class and can be created with the `id` static method :
```java
// name is an id that matches any column containing Strings and which header is "col1"
ColumnId<String> name = ColumnId.id(String.class, "col1");
```

> In the following document, ids are used everywhere.
> However, although they are the advised way to deal with a table, they always can be replaced by indexes or column names.

## Populating a `Table`

Creation of a new [Table](https://github.com/KazeJiyu/datatable/blob/master/src/main/java/fr/kazejiyu/generic/datatable/core/Table.java) consists of an easy one-liner : 

```java
Table people = new DataTable();
```

A table can be filled either by adding new columns :

```java
people.columns()
    .create("Name", String.class, "Luc", "Baptiste", "Marie")
    .create("Age", Integer.class, 23, 32, 42);
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

The `filter` method makes easy to retrieve the rows of a table that match a specific criterion:

```java
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

public class Main {

    // Reference table's columns
    private static final ColumnId<Integer> AGE = id(Integer.class, "age");
    private static final ColumnId<String> NAME = id(String.class, "name");
    
    Table adultsWhoseNameEndsWithLetterE(Table people) {
        return people.filter(row ->
            row.get(AGE) > 18 &&
            row.get(NAME).startsWith("E")
        );
    }
}
```

## SQL-like DSL

For more complex cases, the API also brings a SQL-like DSL that makes possible to apply a same filter to multiple columns. It can be used as follows :

```java
import static fr.kazejiyu.generic.datatable.core.impl.ColumnId.*;

public class Main {
	
    // Reference table's columns
    private static final ColumnId<Integer> AGE = id(Integer.class, "age");
    private static final ColumnId<String> NAME = id(String.class, "name");
    private static final ColumnId<String> SURENAME = id(String.class, "surename");

    // Retrieve all european adults whose both name and surename ends with the letter "e"
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

- `n`: used to apply a same filter to several columns of numbers,
- `b`: used to apply a same filter to several columns of booleans.