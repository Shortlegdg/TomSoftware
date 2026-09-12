# TOMS - Table Order Management Software

Hello! I am Donato Gambacorta (#S00003124), and this is an application for the management of tables and
orders in a classic Italian restaurant abroad.

This is my project for the exam in Object-Oriented Programming, second semester of the Bachelor in
Computer Engineering & Artificial Intelligence.

It is a modular console application written in Java 17, built around the Factory, Composite and Iterator
patterns, with file-based persistence, external configuration, input validation and exception shielding.

---

## 1. Application Overview and Functionality

TOMS is a console application that simulates the workflow a waiter follows in a restaurant: opening a
table, adding dishes to it course by course, reading back the order with the amount due, and closing the
table at the end of the service.

Each open table lives as a text file on disk. The application has no database and no external
dependency beyond JUnit, so the whole state can be inspected, and corrupted by hand, with a text editor.
That is a deliberate choice, and it is the reason why the files are treated as untrusted input exactly
like the keyboard.

### Core features

- Open a table, identified by a number chosen by the waiter, and create its file `table{id}.txt`
- Load the tables already present on disk at every operation, so the view is always the current one
- Read the dish catalogue from six menu files, one per category
- Add the selected dishes to the order of the open table
- Compute the amount due for a table, by summing its order lines recursively
- Display the order of a single table, or of every open table
- Close a table and delete its file
- Validate the values typed by the waiter and the content of the files before using them
- Log every operation and every error to five rotating files under `logs/`, with nothing on the console
- Cover the three patterns, the menu parsing and the file operations with a JUnit suite

---

## 2. Requirements Coverage

| Requirement | Where it is implemented |
|---|---|
| **Factory** | `DishFactory` chooses between `Course`, `Drink` and `Dessert`, all behind the `DishService` interface |
| **Composite** | `MenuComponent` / `TableOrder` (leaf) / `TableComposite` (composite), with a recursive `getTotal()` |
| **Iterator** | `Aggregate<T>` / `Iterator<T>` / `TableIterator`, with `TableMapCollection` as the aggregate |
| **Collections** | `HashMap` of the open tables, `List` of the order lines, defensive copies handed to the iterator |
| **Generics** | `Repository<K, T>`, `Aggregate<T>`, `Iterator<T>`, plus upper-bounded wildcards on the read-only parameters |
| **Java I/O** | `BufferedReader` and `PrintWriter` in try-with-resources, NIO `Files` and `Paths`, explicit UTF-8 |
| **Exception shielding** | `TomsException` wrapping at every I/O boundary, and a single catch-all in `TomsApp.main` |
| **Logging** | `java.util.logging`, five rotating files of 1 MB, no console handler |
| **Input validation** | `InputValidator`, applied to the values typed by the waiter and to the content of the files |
| **External configuration** | `src/main/resources/config.properties`, read through `AppConfig` |
| **JUnit testing** | Five test classes, independent from each other |

---

## 3. Technologies and Patterns Used

### Factory

- `DishService`, the interface every client works against
- `Dish`, the abstract product holding category, name and price
- `Course`, `Drink`, `Dessert`, the concrete products
- `DishFactory`, which parses a line of a menu file and returns the right one

**Why a factory.** The three products are not the same object with a different label: each one answers
with its own `Station`, which is the point the order has to be routed to (kitchen, bar or pastry section).
That is what makes the factory a real choice between concrete types rather than a wrapper around a
constructor. Every client depends on `DishService` only, so a new kind of dish means a new case in the
`switch` of `DishFactory` and a new concrete class, without touching the code that shows the menu or
writes the order.

The factory is also the point where a malformed line is stopped. `DishFactory.createDish` rejects a line
that does not have exactly two fields, and delegates name and price to `InputValidator`, so a broken
menu file can never produce a half-built `Dish`.

### Composite

- `MenuComponent`, the abstract component
- `TableOrder`, the leaf, one order line with its price
- `TableComposite`, the composite, a table holding several components

**Why a composite.** The operation that justifies the pattern is `getTotal()`: the leaf answers with its
own price, the table sums its children recursively, and the calling code is identical in the two cases.
Because a `TableComposite` is itself a `MenuComponent`, the structure is not limited to two levels: a
table can contain another table, for instance a course shared between two guests, and the sum keeps
working unchanged. `CompositeTest` exercises exactly this nesting.

This is a transparent Composite, so `add` lives on the component and the leaf refuses it with an
`UnsupportedOperationException`. That is the known trade-off of the variant, and it is asserted in the
test suite rather than left implicit.

Note on persistence: the nesting works in memory, but the file format stores a flat sequence of lines, so
a table read back from disk is always one level deep.

### Iterator

- `Aggregate<T>` and `Iterator<T>`, the two interfaces
- `TableIterator`, the concrete iterator
- `TableMapCollection`, the aggregate, which also implements `Repository<Integer, MenuComponent>`

**Why an iterator.** The tables are stored in a `HashMap`, but `TablesBrowser` walks them without knowing
it: swapping the map for another structure would not touch the browsing code. `TableIterator` copies the
list it receives, so opening or closing a table while an iteration is running cannot break it, and
`next()` throws `NoSuchElementException` when the iteration is over, as the contract of an iterator
prescribes.

### Exception shielding

A technical exception, `IOException` or `NumberFormatException`, is caught at the boundary where it
happens, logged with its full detail into `logs/toms.log`, and reported upwards with a neutral message.
There are two paths:

- the checked path, `TomsException`, used wherever the caller can reasonably react;
- the shielded path, `TomsExceptionHandler.handledException`, which logs the original exception and
  raises a `RuntimeException` carrying a generic sentence, for the failures the caller cannot do anything
  about.

`TomsApp.main` is the single place where both are turned into a message for the user. Two consequences
worth pointing out:

- `main` does not declare `throws`, because a declared exception would reach the console as a stack trace
  and reveal the internals of the program;
- the offending value is never repeated inside the message, and belongs to the log file only.
  `DishFactoryTest.doesNotLeakTheOffendingValue` asserts precisely this.

### Input validation and sanitisation

`InputValidator` is the single place where a value entering the application is checked, and it is applied
to the files and to the keyboard alike, because nothing prevents a user from opening a table file with a
text editor.

- `validateTableId` and `validatePeopleCount` check the numbers typed by the waiter against the ranges
  declared in `config.properties`.
- `sanitizeText` strips the control characters, trims the value, rejects an empty result, rejects the
  field separator so that a crafted name cannot forge extra fields inside a stored order, and truncates
  at `MAX_TEXT_LENGTH`.
- `parsePrice` refuses a non-numeric value, a negative price and a price above `MAX_PRICE`.

The strategy on text is a denylist: the value is accepted unless it carries one of the characters that
are known to be dangerous for this format. The dangerous set is small and fully determined by the storage
format, which is what makes the choice defensible here.

### External configuration

The paths of the two data folders, the rotation of the log files and the accepted ranges for the table
number and the number of guests live in `src/main/resources/config.properties` and are read through
`AppConfig`. None of them is written inside the classes, so a value can be changed without recompiling,
and documented defaults keep the application usable if the file is missing.

Three constants remain in the code on purpose, because they are properties of the storage format rather
than of the deployment: `InputValidator.FIELD_SEPARATOR`, `MAX_TEXT_LENGTH` and `MAX_PRICE`.

### File-based persistence

Menu files are read-only and live in
`src/main/java/edu/epicode/tomSoftware/data/menu/`, one per category:

`appetizers.txt`, `firstcourses.txt`, `secondcourses.txt`, `sidedishes.txt`, `drinks.txt`, `desserts.txt`

Each line is `name;price`.

Table files are created at runtime in `src/main/java/edu/epicode/tomSoftware/data/tables/`, named
`table{id}.txt`. The first two lines are a header with the table number, the number of guests and the
opening time; every following line is an order line, stored as `category;name;price`.

Keeping the price as a field of its own, instead of burying it inside a sentence, is what allows a table
read back from disk to recompute its own total. A line that does not match the three-field format, such
as the header, is kept as plain text and counts zero towards the total.

### Logging

`java.util.logging`, configured in `LoggingConfig`. The root logger is stripped of its handlers and given
a single `FileHandler` writing to `logs/toms.log`, rotating over five files of 1 MB. There is no console
handler, so the log never interferes with the menu the waiter is reading.

---

## 4. Setup and Execution

### Clone the repository

```bash
git clone https://github.com/Shortlegdg/TomSoftware.git
cd TomSoftware
```

### Run from the IDE

1. Open the project in IntelliJ IDEA, which imports it from `pom.xml`.
2. Open `src/main/java/edu/epicode/tomSoftware/TomsApp.java`.
3. Run it with Run → Run 'TomsApp.main()'.

### Run as a jar

The `logs/` and `data/tables/` folders are created automatically at the first run if they are missing.

### Using the application

The main menu offers:

| Choice | Action |
|---|---|
| 1 | Open a new table and start taking its order |
| 2 | Display the order and the amount due of one table |
| 3 | Display the orders of every open table |
| 4 | Close a table and delete its file |
| 0 | Exit |

After opening a table, the submenu lists the six categories. Inside a category, the waiter types the
number of a dish to add it to the order, and `0` to go back.

---

## 5. Test Suite

```bash
mvn test
```

The tests are discovered automatically by Maven, there is no suite class to launch by hand. From the IDE,
right-click on `src/test/java` → Run 'All Tests'.

| Test class | What it demonstrates |
|---|---|
| `DishFactoryTest` | The factory chooses the concrete product; a malformed entry is rejected as `TomsException`; the shielded message does not repeat the offending value |
| `CompositeTest` | The total is computed recursively across a nested structure; the leaf refuses children |
| `TableIteratorTest` | The aggregate is traversed in full; `next()` past the last element throws `NoSuchElementException` |
| `TableFileTest` | Opening a table writes its file; closing it removes the file and the entry from the collection |
| `MenuLoaderTest` | Every one of the six menu files parses, and every dish carries a name and a non-negative price |

Every test is independent of the others, so the order of execution does not matter. `TableFileTest`
reserves the table number 999 and deletes its file in an `@After` method, including when the test fails
before reaching the deletion.

---

## 6. Current Limitations

Stated explicitly, because they are design boundaries rather than oversights:

- **One table at a time.** `TableWriter` keeps the path of the last table opened in a static field, so the
  application assumes a single order being taken at any moment. Serving two tables in parallel would need
  the writing responsibility to move into `TableMapCollection`.
- **Prices are `double`.** Adequate for the size of a single order, but not the right type for money:
  `BigDecimal`, or integer cents, would be needed before this ever handled a real till.
- **Flat persistence.** The Composite supports nesting in memory, the file format does not.
- **The menu files live under `src/main/java`.** They are therefore not packaged inside the jar, which is
  why the application resolves them from the working directory.

---

## 7. UML Diagrams

The sources are the PlantUML files inside `UML Diagrams/`, and can be rendered with the PlantUML plugin
for IntelliJ.

### Dish factory components

![Dish factory components](UML%20Diagrams/img.png)

### Order management system

![Order management system](UML%20Diagrams/img_1.png)

### Tables composite components

![Tables composite components](UML%20Diagrams/img_2.png)
