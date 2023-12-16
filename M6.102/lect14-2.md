# Reading 14: Recursion

## Recursion

以下给出了递归函数的构成，我觉得很好不想自已说一遍：

A recursive function is defined in terms of base cases and recursive steps.

* In a base case, we compute the result immediately given the inputs to the function call.
* In a recursive step, we compute the result with the help of one or more recursive calls to this same function, but with the inputs somehow reduced in size or complexity, closer to a base case.

## Structure of recursive implementations

A recursive implementation always has two parts:

* base case, which is the simplest, smallest instance of the problem, that can’t be decomposed any further. Base cases often correspond to emptiness – the empty string, the empty list, the empty set, the empty tree, zero, etc.

* recursive step, which decomposes a larger instance of the problem into one or more simpler or smaller instances that can be solved by recursive calls, and then recombines the results of those subproblems to produce the solution to the original problem.

It’s important for the recursive step to transform the problem instance into something smaller, otherwise the recursion may never end. If every recursive step shrinks the problem, and the base case lies at the bottom, then the recursion is guaranteed to be finite.

A recursive implementation may have more than one base case, or more than one recursive step. For example, the Fibonacci function has two base cases, n = 0 and n = 1.

## Choose the right recursive subproblem

recursive注意事项：

Static variables and aliases to mutable data are very unsafe for recursion, and lead to insidious bugs like this. When you’re implementing recursion, the safest course is to pass in the values it needs, and stick to immutable objects or avoid mutation.

1. 在递归中将需要以值的形式传递。
2. 传递的值使用immutable类型。

### Recursive problems vs. recursive data

当我们处理的数据的类型是递归式的，和自然的就可以联想到利用递归来解决问题。对于递归的数据就是用递归的实现是一种十分自然的解法。

The Java library represents the file system using java.io.File. 

## Mutual recursion

A calls B and B calls A。

```java
/**
 * @param file a file in the filesystem
 */
public static void visitNode(File file) {
  if (file.isDirectory()) {
    visitChildren(file.listFiles());
  }
}

/**
 * @param files a list of files
 */
public static void visitChildren(File[] files) {
  for (File file : files) {
    visitNode(file);
  }
}

```

这么做的有点是使方法更加简便和优雅，同时给予了client自由选择访问单一节点或者是多个节点


不要使用全局的静态变量,他可能导致的问题：

1. 一个程序的不同部分无法在同一时间运行相同的程序，因为此时程序的状态是由全局变量记录的，而不是分布记录了他们两者之间的状态。
2. 函数的独立性受到了破坏。

所以应该使用信息传递的方式。

example:

```java
public static Set<File> visitNode(File file, String pattern) {
    Set<File> resultSet = new HashSet();
    if (file.getName().startsWith(pattern)) { resultSet.add(file); }
    if (file.isDirectory()) {
        resultSet.addAll(visitChildren(file.listFiles(), pattern));
    }
    return collections.unmodifiableSet(resultSet);
}

public static Set<File> visitChildren(File[] files, String pattern) {
    Set<File> resultSet = ▶▶A◀◀;
    for (File file : files) {
        ▶▶B◀◀(visitNode(file));
    }
    return ▶▶C◀◀;
}
```

可变的集合类型应该尽量控制在函数内。

保证immutable在返回时我们设定的类型是不可变的类型，这保证了当我们返回之后其他的mutable 操作是无法改变此次返回的set。

在编写 visitNode 和 visitChildren 方法的规范时，可以选择是否将返回的集合的不可变性作为规范的一部分来明确规定。这取决于函数的需求和设计

## Reentrant code

reentrant code: 他会保存程序的自身状态(自己个本身)。
直接递归和mutual递归就是reentrant code的特殊情况。
但是他不会使用全局变量，或者与其他的程序共享状态。

## When to use recursion rather than iteration

使用递归而不是迭代的契机：

1. 处理的问题天然具有递归的属性
2. 处理的数据天然具有递归的属性

使用递归的好处是，可以将变量设定成不可变和不可分配，从而使递归天然成为pure function 。在设计时，我们能够简化思考的模型，只考虑输入和输出的行为。
对于比需要修改的变量和对象，需要在迭代时，考虑在每一次迭代时和迭代后各个状态的快照，这比仅考虑输入和输出的行为要更加复杂。
但是这么做比省空间，对比递归使用stack保存状态。

## Common mistakes in recursive implementations

* The base case is missing entirely, or the problem needs more than one base case but not all the base cases are covered.
* The recursive step doesn’t reduce to a smaller subproblem, so the recursion doesn’t converge.
* Aliases to a mutable data structure are inadvertently shared, and mutated, among the recursive calls.






