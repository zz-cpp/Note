# Reading 10: Equality

Objectives
* Understand the properties of an equivalence relation.
* Understand equality for immutable types defined in terms of the abstraction function and in terms of observation.
* Differentiate between reference equality and object equality.
* Differentiate between observational and behavioral equality for mutable types.

## Instruction

本章介绍定义值相等的概念。
物理世界中没用两个完全一样的东西，哪怕是两个雪花。在数学上同一个事物往往有多种表示，例如$\sqrt{9},3,1+2$。

## Equivalence relation

相等在数学上需要满足的属性：
$E$表示$(x,y)$相等的集合，$T$表示某种类型。
reflexive: $(t,t) \in E $ for all $t \in T$
symmetric: $(t,u) \in E \Rightarrow (u,t) \in E$
transitive: $(t,u) \in E \land (t,v) \in E \Rightarrow (t,v) \in E $

满足以上才能说明两者相等。

以下是TS版本：

![](ref/lect10/20230821081812.png)

## Equality of immutable types

对于不可变类型，定义他的相等属性有两个方向：
1. rep经过AF，映射到相同的abstract value;
2. 根据observer和producer操作，产生相同的结果被测到(observational definition );

## Reference equality vs. value equality
在编程语言中，测试相等的操作有两种：
1. reference equailty
2. value equailty

后者就是这篇文章所讨论的，前者表述的是存储的内存地址是否是相同的。

![](ref/lect10/20230821091546.png)

## Equality of mutable types

对于可变类型，因为其mutator操作会改变当前obj状态，应此此处需要重新定义基于obseration的equality。
1. observational equality 维持原状，不使用mutator。
2. behavioral equality：即使mutator被调用，依然无法被区分。

Behavioral equality is a stronger notion – we can not only use observers and producers to try to distinguish the objects, but we can also call a mutator on one object (but not the other), and then again observe both objects again to see if they now look different from each other.

对于不可变类型，这两种equality是一致的。

## “Deep equality” on collections

## Hash functions

## Summary

In TypeScript, for immutable built-in types (e.g. number, string, bigint):

* Use === for behavioral and observational equality, which are identical because there are no mutators.
* Avoid == because of its automatic conversions.
* Safe for use as Set elements and Map keys.

For immutable object types (e.g. Duration):

* Use equalValue() for both behavioral and observational equality, which are identical because there are no mutators.
* Avoid === because it is too strong, and avoid == because of its automatic conversions.
* Not safe for use as Set elements or Map keys because those data structures compare objects using ===.
For mutable object types:

* Use === for behavioral equality.
* Use equalValue() for observational equality.
* Avoid == because of its automatic conversions.
 Safe for use as Set elements and Map keys.