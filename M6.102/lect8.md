# Reading 8: Defining ADTs with Interfaces, Generics, Enums, and Functions

Objectives
Today’s class is about various ways to implement abstract data types, including:

* interfaces: separating the interface of an ADT from its implementation;
* generic types: defining a family of ADTs using generic type parameters;
* enumerations: defining an ADT with a small finite set of values;
* global functions operating on an opaque type: rare in TypeScript but common in non-object-oriented languages.
We also discuss subtyping as a relationship between two types determined by their specs, and distinguish it from subclassing, which involves reps as well.

After today’s class, you should be able to:

* define ADTs using classes, interfaces, generics, and enumerations
* determine whether one type is a subtype of another

## Interfaces

使用Iterfaces实现ADT的好处：
1. Interface中没有rep，只有client所需要的方法。
2. 能够有更多映射相同的abstract value但rep不同的ADT。
3. static error。

### About interfaces in TypeScript
interface contains the spec of an ADT, namely its public method signatures and public instance methods.

It also should not include any abstraction function, rep invariant, or safety from rep exposure argument, as all those depend on having a rep.

## Subtypes
subtypes 是subset of subertype。

“B is a subtype of A” means “every B is an A.” In terms of specifications: “every B satisfies the specification for A.”

编译器不能检查subtype的spec的强弱，设定上应该是至少与实现的interface强度相同。

可以通过继承已经存在的接口来强化spec或是添加更多的方法。

### Structural subtyping in TypeScript

structural subtyping是TS的一种方便的机制：如果B存在所有A需要的操作——相同的public 方法和比变量，并且变量的类型是兼通的，那么即使并没有使用implement或者extend关键字，也依旧认为B是A的subtype。

这种机制有好处也有坏处，好处是方便，但隐患是B的spec与A并不会保持一致(因为没有implement，所以你不知道B要遵守A的spec)。

这就导致如果A是immutability，但B添加了mutator的方法，会直接导致A变量被赋值了B实例，A可以改变状态。

## Why interfaces?

1. 静态检查和简化人类阅读
2. 不同的应用对不同的实现有不同的性能
3. 便于针对各种不同的目的的spec
4. 因为视角的不同而产生不同种类的相同接口的ADT(菜单栏和菜单项)
5. 灵活切换不同的实现，遵照ADT的编写策略

## Subclassing

使用子类实现ADT是另一种方法。他使得子类继承了父类的全部rep和方法。这样做的好处是很多的东西可以直接复用，但是相对的会引入更多的问题：
1. 子类和父类之间充满了rep exposure
2. 子类和父类充满rep dependece
3. 子类和父类可能会意外违反rep invarains

Designing for safe subclassing means that the superclass must now offer two contracts: one for interaction with clients, and one for subclasses. These issues simply do not arise with interfaces.


## Generic types

## Enumerations

## Getters and setters

## ADTs in non-OOP languages

对于像c语言一样非面向对象，实现ADT的方法是将ADT作为函数的的参数，对其所有的操作都是在函数内部完成，只需要传递指针的方式。类似与File类型。将本应该在类中的操作函数化，fclose，fopen，fputs。

One key takeaway from this is that the notion of an abstract data type does not depend on language features like classes, or interfaces, or public/private access control. Data abstraction is a powerful design pattern that is ubiquitous in software engineering.

