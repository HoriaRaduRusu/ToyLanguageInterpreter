# Toy Language Interpreter
An interpreter for a Toy programming language written in Java with a graphical user interface

## Used concepts and Technical Features
- Graphical User Interface built using JavaFX
- Layered architecture: ```presentation layer``` (the application UI), ```business layer``` (application service), ```persistence layer``` (application repositories)
- Use of multi-threading
- Implementations for several common expressions and statements used in programming. A full list will be provided below.
- Exceptions handling

## Functionalities 
- Running one of the preloaded programs step by step, while seeing the execution stack, the symbol, heap, file and latch tables, what was printed and the current list of active threads
- Implementations for several common expressions: evaluating constants and variables, arithmetic expressions, logical expressions, relational expressions and reading from the heap
- Implementations for several common statements: declaring a variable, assignment, conditional assignment, if statement, while statement, print, allocation and writing to the heap and opening, reading and closing a file
- Implementations for several basic multi-threading related statements: fork and creating, awaiting at, and counting down a CountDownLatch