# Reading 11: Recursive Data Types

## Introduction

In this reading we’ll look at recursively-defined types, how to specify operations on such types, and how to implement them. Our main example will be immutable lists.

## Recursion

## Example: Immutable lists

## Recursive data type definitions

递归数据类型的定义的典型特征是：
在data的左侧是抽象数据类型，右侧具体的表示中有包含了左侧的抽象类型。

> Tree<E> = Empty + Node(e: E, left: Tree<E>, right: Tree<E>)