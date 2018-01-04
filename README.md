[![Sandbox](https://img.shields.io/badge/state-sandbox-yellow.svg)](https://img.shields.io/badge/state-sandbox-yellow.svg)

# Sandbox
As of now, this project is much more a sandbox to experiment implementation of _mutable_ datatables in Java than a real usable API.

# Motivation
Create a Java class representing a table that can be filtered easily.

The Querying API, which makes able to filter a table, consists of a SQL-like DSL.

__Important__: this API is still in progress and will change in the near future.

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
```
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

The `filter` method makes easy to retrieve the rows of a table that match a specific criterion :

```java
Table adultsWhoseNameEndsWithLetterE(Table people) {
    return people.filter( row ->
        (int) row.get("age") > 18 &&
        ((String) row.get("name")).endsWith("e")
    );
}
```

Since the type of a column's elements is only known at runtime, the cast is mandatory within the lambda expression.

For more complex cases, the API also brings a SQL-like DSL that can be used as follows :

Hence, the previous snippet can be re-written as follows: 
```java
// Retrieve all the adults whose name ends with the letter "e"
Table adults = Query
  .select("Name")
  .from(people)
  .where("Name").asStr().endsWith("e")
  .and("Age").asNumber().gt(18)
  .queryTable();
```

The `as*` methods provide tailored methods, making easier to build explicit queries. As of now, three of them are available :

- `asStr()` to indicate that the column contains `String`s,
- `asBool()` to indicate that the column contains `Boolean`s,
- `asNumber()` to indicate that the column contains `Number`s,
- `as(Class<T>)` to indicate that the column contains `T`s.

__Important__: these methods do not perform dynamic casts. If the column's elements are not of the rigth type, a `ClassCastException` is thrown.
