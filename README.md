# Motivation
Create a Java class representing a table that can be filtered easily.

The Querying API, which makes able to filter a table, consists of a SQL-like DSL.

__Important__: this API is still in progress and will change in the near future.

# Code samples

Creating a new table:

```java
Table people = new DataTable();

// Creates two columns and fill them
people.columns()
    .create(String.class, "Name", "Luc", "Baptiste", "Marie")
    .create(Integer.class, "Age", 23, 32, 42);
    
// Rows can also be added as follows:
people.rows()
    .create("Julie", 12)
    .create("Mathieu", 67);
    
// The resulting table is :
// +----------+-----+
// |   Name   | Age |
// +----------+-----|
// |   Luc    | 23  |
// | Baptiste | 32  |
// |   Marie  | 42  |
// |   Julie  | 12  |
// |  Mathieu | 67  |
// +----------+-----+
```

Querying the table:

```java
// Retrieve all the adults whose name ends with the letter "e"
Table adults = Query
  .select("Name")
  .from(people)
  .where("Name").match(name -> ((String) name).endsWith("e"))
  .and("Age").match(age -> (int) age > 18)
  .queryTable();
```

Since the type of a column's elements is only know at runtime, the cast is mandatory within the lambda expression. However, since this is pretty sad to both write and read, the API provides some shortcuts :

- `asStr()` to indicate that the column contains `String`s,
- `asBool()` to indicate that the column contains `Boolean`s,
- `asNumber()` to indicate that the column contains `Number`s,
- `as(Class<T>)` to indicate that the column contains `T`s.

__Important__: these methods do not perform dynamic casts. If the column's elements are not of the rigth type, a `ClassCastException` is thrown.

Hence, the previous snippet can be re-written as follows: 
```java
// Retrieve all the adults whose name ends with the letter "e"
Table adults = Query
  .select("Name")
  .from(people)
  .where("Name").asStr().endsWith("e")
  .and("Age").asNumber().gt(18)
  .queryTable();
  
// adults now contains :
// +----------+
// |   Name   |
// +----------+
// | Baptiste |
// |   Marie  |
// +----------+
```
