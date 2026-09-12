# TOMS - Table Order Management Software

Hello! I am Donato Gambacorta (#S00003124), and this is an application for the tables and orders management 
for a classic Italian Restaurant abroad.

This is my project for the Exam in Object-Oriented Programming for the second semester of the Bachelor in
Computer Engineering & Artificial Intelligence.

It is a modular Java application for managing restaurant tables, orders, and dish catalogs using Composite Pattern & Factory Pattern, file‑based persistence, and exception shielding.

---

## 1. Application Overview and Functionality

Table Order Management Software is a console‑based Java application designed to simulate the workflow of managing tables and orders in a restaurant environment.  
The system stores each table as a physical file on disk and loads them when asked by input in the submenu.  

### Core Features
- Create new tables (automatically generates `table{id}.txt`)
- Load existing tables from the filesystem
- Add dishes to a table
- Compute the amount due for a table, by summing its order lines
- Close a table (removes the associated file)
- Manage a catalog of dishes and categories
- Validate and sanitize every value entering the application
- Convert all internal errors into a unified custom exception (`TomsException`)
- Log all operations and errors using `java.util.logging`, in a file-handling way and storing these file in 5 rotating files    
named `toms.log.*` placed in the `logs/` directory.
- Provide JUnit test coverage for the patterns, the validation and the file operations
---

## 2. Requirements Coverage

| Requirement | Where it is implemented |
|---|---|
| **Factory** | `DishFactory` picks between `Course`, `Drink` and `Dessert`, all behind the `DishService` interface |
| **Composite** | `MenuComponent` / `TableOrder` (leaf) / `TableComposite` (composite), with a recursive `getTotal()` |
| **Iterator** | `Aggregate<T>` / `Iterator<T>` / `TableIterator`, implemented by `TableMapCollection` |
| **Exception Shielding** | `TomsException` plus wrapping at every I/O boundary, and a single catch-all in `TomsApp.main` |
| **Collections** | `HashMap` of the opened tables, `List` of the order lines, read only views returned to the callers |
| **Generics** | `Repository<K, T>`, `Aggregate<T>`, `Iterator<T>`, plus upper bounded wildcards on the read only parameters |
| **Java I/O** | `BufferedReader` and `PrintWriter` in try-with-resources, NIO `Files` and `Paths`, explicit UTF-8 |
| **Logging** | `java.util.logging`, 5 rotating files of 1 MB, no console handler |
| **JUnit testing** | 5 test classes, one per requirement, independent from each other |
| **Input sanitisation** | `InputValidator`, applied to the console input and to the content of the files alike |
| **No hardcoded configuration** | `src/main/resources/config.properties`, read through `AppConfig` |
| **Controlled propagation** | Only `TomsException` crosses the layers, no stack trace ever reaches the user |

---

## 3. Technologies and Patterns Used (with Justification)

### ✔ Factory Pattern
Used to create dishes:
- `DishService` (interface)
- `DishFactory` (to create the dish based on the menu files)
- `Dish` (abstract: it holds category, name and price)
- `Course`, `Drink`, `Dessert` (the concrete products)

This module centralizes the restaurant’s menu logic and allows tables to reference structured dish data.


**Why Factory?**  
To make dish creation more flexible, maintainable, and scalable.   
Instead of calling constructors directly, you delegate the creation process to a dedicated “factory” class or method.

The three products are not the same object with a different label: each one answers with its own `Station`,
which is the point the comanda has to be routed to (kitchen, bar or pastry section). That is what makes the
factory a real choice between concrete types. Adding a new kind of dish means touching the `switch` inside
`DishFactory` and nothing else, because every client works on the `DishService` interface.

### ✔ Composite Pattern
Used to model tables and their items:
- `MenuComponent` (abstract)
- `TableOrder` (single order line, with its price)
- `TableComposite` (a table containing multiple items)

**Why Composite?**  
It allows the system to treat individual items and groups of items uniformly.  
This makes the architecture flexible, scalable, and easy to extend (e.g., adding sub‑menus or nested structures).

The operation that makes the pattern worth using is `getTotal()`: the leaf answers with its own price, the
table sums its children recursively, and the calling code is identical in the two cases. Because a
`TableComposite` is itself a `MenuComponent`, the structure is not limited to two levels: a table could
contain another table, for instance a course shared between two guests, and the sum would keep working
without a single change.

### ✔ Exception Shielding
A technical exception (`IOException`, `NumberFormatException`) is caught at the boundary where it happens, it
is logged **with its full detail** into `logs/toms.log`, and it is rethrown as a `TomsException` carrying a
neutral message. `TomsApp.main` is the single point where those messages are shown to the user, and a final
`catch (RuntimeException)` reports anything unforeseen with a generic sentence.

Two consequences worth pointing out:
- `main` does not declare `throws`: a declared exception would end up on the console as a stack trace,
  revealing the internals of the program.
- the offending value is never repeated inside the message. `DishFactoryTest` asserts exactly this.

This ensures:
- No crashes due to unchecked exceptions
- Clear and consistent error reporting
- No information leaking towards the user

### ✔ Input Sanitisation
`InputValidator` is the single place where a value entering the application is checked. It applies an
allowlist strategy, caps the length of the free text, removes the control characters and rejects the field
separator, so that a crafted name cannot forge extra fields inside a stored order.

The same validation is applied to the content of the files and not only to the keyboard: nothing prevents a
user from opening a table file with a text editor, so **a file is untrusted input exactly like the console**.

### ✔ External Configuration
Every path, size and limit lives in `src/main/resources/config.properties` and is read through `AppConfig`:
the folders of the menus and of the tables, the rotation of the log files, and the accepted ranges for the
table number and for the number of guests. Nothing of this is written inside the classes, so a value can be
changed without recompiling. If the file is missing, documented defaults keep the application usable.

### ✔ File‑Based Persistence
Each table is stored as a text file inside a `data/tables/` directory.  
Each Menù is stored as a text file inside a `data/menu/` directory.
This approach is:
- Simple
- Transparent
- Dependency‑free
- Easy to debug and inspect manually

The menu files are divided by category, and they provide each menù for:
- Appetizers
- First Courses
- Second Courses
- Side Dishes
- Drinks
- Desserts

An order line is stored as `category;name;price`, using the same separator as the menu files. Keeping the
price as a field of its own, instead of burying it inside a sentence, is what allows a table read back from
the disk to know its own total.

### ✔ Iterator
Iterator Pattern it is used in this project to iterate through the tables because it gives us a safe, encapsulated, 
and flexible way to traverse tables,   
independent of how they’re stored internally, and perfectly aligned with the Composite architecture.

`TableIterator` works on a copy of the collection, so opening or closing a table while an iteration is running
does not break it, and `next()` throws `NoSuchElementException` when the iteration is over, as the contract of
an iterator prescribes.

### ✔ JUnit
JUnit 4, declared once in the `pom.xml`.

The suite is deliberately small: one test class for each requirement that has to be
demonstrated, and nothing beyond that.

| Test class | Requirement it demonstrates |
|---|---|
| `DishFactoryTest` | Factory choosing the concrete product, input sanitisation, Exception Shielding |
| `CompositeTest` | the recursive total over the structure, and the leaf refusing children |
| `TableIteratorTest` | the traversal of the aggregate, and the end of the iteration |
| `TableFileTest` | Java I/O: a table file is written when the table opens, and deleted when it closes |
| `MenuLoaderTest` | every menu file of the restaurant can actually be parsed |

Every test is independent from the others and removes the files it creates, so the order of
execution does not matter and the data folder is left as it was found.

---

## 4. Setup and Execution Instructions

Requires **JDK 17** or newer, and Maven 3.8 or newer.

### Installation

1. Clone the repository:

2. Open the project in IntelliJ IDEA.

3. Ensure the following directory structure is recognized:

`src/main/java → Sources Root`

`src/main/resources → Resources Root`

`src/test/java → Test Sources Root`

### Running the Application

From the IDE:

1. Open the class `main/java/edu/epicode/tomSoftware/TomsApp` in IntelliJ
2. Run it using Run → Run 'TomsApp.main()'

Then use the interactive menu to:
- Insert a new order opening a new table and creating its own file
- Add dishes, divided for courses
- View existing tables and their orders, with the amount due
- Close tables and deleting its file

The `logs/` and `data/tables/` folders are created automatically at the first run, if they are missing.

## 5. Running the Test Suite

```bash
mvn test
```

The tests are discovered automatically by Maven, there is no suite class to launch by hand.
From the IDE, right click on `src/test/java` → Run 'All Tests'.

# UML Diagrams

The sources are the PlantUML files inside `UML Diagrams/`, and can be rendered with the PlantUML plugin for
IntelliJ.

___
### ✔ DISH FACTORY COMPONENTS
___
![img.png](UML%20Diagrams/img.png)
___
### ✔ ORDER MANAGEMENT SYSTEM
___
![img_1.png](UML%20Diagrams/img_1.png)
___
### ✔ TABLES COMPOSITE COMPONENTS
___
![img_2.png](UML%20Diagrams/img_2.png)
