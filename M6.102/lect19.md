# Reading 19: Writing a Program with Abstract Data Types


## Recipes for programming

### Writing a method:

1. Spec. Write the spec, including the method signature (name, argument types, return types, exceptions), and the precondition and the postcondition as a Javadoc comment.

2. Test. Create systematic test cases and put them in a JUnit class so you can run them automatically. 
   1. You may have to go back and change your spec when you start to write test cases. Just the process of writing test cases puts pressure on your spec, because you’re thinking about how a client would call the method. So steps 1 and 2 iterate until you’ve got a better spec and some good test cases. 
   2. Make sure at least some of your tests are failing at first. A test suite that passes all tests even when you haven’t implemented the method is not a good test suite for finding bugs. 

3. Implement. Write the body of the method. You’re done when the tests are all green in JUnit. Implementing the method puts pressure on both the tests and the specs, and you may find bugs that you have to go back and fix. So finishing the method may require changing the implementation, the tests, and the specs, and bouncing back and forth among them.

### Writing an abstract data type:

1. Spec. Write specs for the operations of the datatype, including method signatures, preconditions, and postconditions. 

2. Test. Write test cases for the ADT’s operations. Again, this puts pressure on the spec. You may discover that you need operations you hadn’t anticipated, so you’ll have to add them to the spec. 

3. Implement. For an ADT, this part expands to: 
   1. Choose rep. Write down the private fields of a class, or the variants of a recursive datatype. Write down the rep invariant and abstraction function as a comment. 
   2. Assert rep invariant. 
   3. Implement a checkRep() method that enforces the rep invariant. This is critically important if the rep invariant is nontrivial, because it will catch bugs much earlier. Implement operations. Write the method bodies of the operations, making sure to call checkRep() in them. You’re done when the tests are all green in JUnit.


### Writing a program (consisting of ADTs and static methods):

1. Choose datatypes. Decide which ones will be mutable and which immutable. 
2. Choose static methods. Write your top-level main method and break it down into smaller steps. 
3. Spec. Spec out the ADTs and methods. Keep the ADT operations simple and few at first. Only add complex operations as you need them. 
4. Test. Write test cases for each unit (ADT or method). 
5. Implement simply first. Choose simple, brute-force representations. The point here is to put pressure on the specs and the tests, and try to pull your whole program together as soon as possible. Make the whole program work correctly first. Skip the advanced features for now. Skip performance optimization. Skip corner cases. Keep a to-do list of what you have to revisit. 
6. Iterate. Now that it’s all working, make it work better. Reimplement, optimize, redesign if necessary.


## Problem: matrix multiplication

a,b 是常量，X是矩阵

优化前: 需要迭代矩阵两次
> (aX)b

更改矩阵惩罚的顺序之后
> (ab)X

## Spec

接着上面的描述，我们来完成spec for MatrixExpression

### Choose datatypes

Let’s call this a MatrixExpression. To make the definitions easier to read, we’ll abbreviate that to MatExpr.

```

make: double → MatExpr

effects:
returns an expression consisting of just the given scalar
make: double[][] array → MatExpr

requires:
array.length > 0, and array[i].lengths are equal and > 0, for all valid indices i
effects:
returns an expression consisting of just the given matrix
times: MatExpr m1 × MatExpr m2 → MatExpr

requires:
m1 and m2 are compatible for multiplication
effects:
returns m1×m2
isIdentity: MatExpr → boolean

effects:
returns true if and only if the expression is the multiplicative identity
```

额外补充我们想要的：

```
optimize: MatExpr → MatExpr

effects:
returns an expression with the same value, but which may be faster to compute

```

## Test

optimize()

* partition on number of scalars in expression: 0, 1, 2, >2
* partition on position of scalar in expression tree: left child, right child, left-child-of-left-child, left-child-of-right-child, right-child-of-left-child, right-child-of-right-child, more than 2 levels deep

|Test case|Partitions covered|
X ⇒ X| covers 0 scalars|
aX ⇒ aX|1 scalar, left child
a(Xb) ⇒ (ab)X|2 scalars, left child, right-child-of-right-child
(aX)b ⇒ (ab)X|2 scalars, right child, left-child-of-left-child
(Xa)(bY) ⇒ (((ab)X)Y)|2 scalars, left-child-of-right-child, right-child-of-left-child

## Implement

> MatExpr = Identity + Scalar(value:double) + Matrix(array:double[][]) + Product(m1:MatExpr, m2:MatExpr)

```java

/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {
    // Datatype definition:
    //   MatExpr = Identity + Scalar(value:double)
    //             + Matrix(array:double[][]) + Product(m1:MatExpr, m2:MatExpr)
    // ...
}

class Identity implements MatrixExpression {
    public Identity() {
    }
}

class Scalar implements MatrixExpression {
    private final double value;
    // RI: true
    // AF(value) = the real scalar represented by value

    public Scalar(double value) {
        this.value = value;
    }
}

class Matrix implements MatrixExpression {
    private final double[][] array;
    // RI: array.length > 0, and all array[i] are equal nonzero length
    // AF(array) = the matrix with array.length rows and array[0].length columns
    //             whose (row,column) entry is array[row][column]

    public Matrix(double[][] array) {
        this.array = array; // note: danger!
    }
}

class Product implements MatrixExpression {
    private final MatrixExpression m1;
    private final MatrixExpression m2;
    // RI: m1's column count == m2's row count, or m1 or m2 is scalar
    // AF(m1, m2) = the matrix product m1*m2

    public Product(MatrixExpression m1, MatrixExpression m2) {
        this.m1 = m1;
        this.m2 = m2;
    }
}

```

### Choose an identity

在datatype 中单独表示nothing是十分好的。

```java
/** Identity for all matrix computations. */
public static final MatrixExpression I = new Identity();
```
Unfortunately, we’ll see that this is not a perfect situation: other MatExprs might also be the identity.

### Implementing make: use factory methods

Let’s start implementing, starting with the make() creators.

We don’t want to expose our concrete rep classes Scalar, Matrix, and Product, so that clients won’t depend on them and we’ll be able to change them later (being ready for change).

So we’ll create static factory methods in MatrixExpression to implement make():

```java

/** @return a matrix expression consisting of just the scalar value */
public static MatrixExpression make(double value) {
return new Scalar(value);
}

/** @return a matrix expression consisting of just the matrix given */
public static MatrixExpression make(double[][] array) {
return new Matrix(array);
}
```

### Implementing isIdentity: don’t use instanceof

Now let’s implement the isIdentity observer. Here’s a bad way to do it:

```java

// don't do this
public static boolean isIdentity(MatrixExpression m) {
    if (m instanceof Scalar) {
        return ((Scalar)m).value == 1;
    } else if (m instanceof Matrix) {
        // ... check for 1s on the diagonal and 0s everywhere else
    } else ... // do the right thing for other variant classes
}
```

In general, using instanceof in object-oriented programming is a bad smell. Break the operation down into pieces that are appropriate for each class, and write instance methods instead:

```java
class Identity implements MatrixExpression {
    // no fields
    ...
    public boolean isIdentity() {

return true;
    }
}
```

```java

class Scalar implements MatrixExpression {
    private final double value;
    // RI: true
    // AF(value) = the real scalar represented by value
    ...
    public boolean isIdentity() {

return value == 1;
    }
}
```

```java

class Matrix implements MatrixExpression {
    private final double[][] array;
    // RI: array.length > 0, and all array[i] are equal nonzero length
    // AF(array) = the matrix with array.length rows and array[0].length columns
    //             whose (row,column) entry is array[row][column]
    ...
    public boolean isIdentity() { 
        if (array.length != array[0].length) {
         return false;
        }
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                double expected = (row == col) ? 1 : 0;
                if (array[row][col] != expected) {
                  return false;
                }
            }
        }
      return true;
    }
}
```

```java

class Product implements MatrixExpression {
    private final MatrixExpression m1, m2;
    // RI: m1's column count == m2's row count, or m1 or m2 is scalar
    // AF(m1, m2) = the matrix product m1*m2
    ...
    public boolean isIdentity() { 
        return m1.isIdentity() && m2.isIdentity();
    }
}
```

note: 

Logical-and is the best option, but this implementation doesn’t fully satisfy the spec!

It is true that a product is the identity if it is the product of identities, but there are many other ways to factor the identity matrix.

One way to fix this is to write a much stronger implementation of isIdentity – for example, computing the actual product to determine if the result is the identity matrix. But that would undermine the goal of this data type, which is to represent matrix multiplications so that we can minimize the computation of the products.

So more likely, we would want to weaken the spec of isIdentity, so that it is still useful but more implementable. This approach is discussed just below.

Implementing isIdentity exposes an issue that we should have first discovered by writing test cases: we will not always return true for a Product whose value is the identity matrix (e.g. A × A⁠-1). We probably want to resolve this by weakening the spec of isIdentity, so that it doesn’t guarantee to identify every multiplicative identity:

> isIdentity: MatExpr → boolean effects: returns true only if the expression is the multiplicative identity.


### Implementing optimize without instanceof

Let’s implement optimize(). Again, here’s a bad way to do it, which will quickly get us mired in weeds:

```java
// don't do this
class Product implements MatrixExpression {
    public MatrixExpression optimize() {
        if (m1 instanceof Scalar) {
            ...
        } else if (m2 instanceof Scalar) {
            ...
        }
        ...
    }
}
```
If you find yourself writing code with instanceof checks all over the place, you need to take a step back and rethink the problem.

In particular, to optimize the scalars, we’re going to need two recursive helper operations, so we’ll add them to our abstract datatype:

> scalars: MatExpr → MatExpr
effects:
returns a MatExpr with no matrices in it, only the scalars in the input expression
matrices: MatExpr → MatExpr
effects:
returns a MatExpr with no scalars in it, only the matrices in the same order they appear in the input expression

These expressions will allow us to pull the scalars out of an expression and move them together in a single multiplication expression.

```java
/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {

    // ...

    /** @return the product of all the scalars in this expression */
    public MatrixExpression scalars();

    /** @return the product of all the matrices in this expression in order.
     * times(scalars(), matrices()) is equivalent to this expression. */
    public MatrixExpression matrices();
}
```

Now we’ll implement them as expected:

```java
class Identity implements MatrixExpression {
    // no fields
    ...
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return this; }
}

class Scalar implements MatrixExpression {
    private final double value;
    ...
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return I; }
}

class Matrix implements MatrixExpression {
    private final double[][] array;
    ...
    public MatrixExpression scalars() { return I; }
    public MatrixExpression matrices() { return this; }
}

class Product implements MatrixExpression {
    private final MatrixExpression m1, m2;
    ...
    public MatrixExpression scalars() {
        return times(m1.▶▶A◀◀, m2.▶▶B◀◀);
    }
    public MatrixExpression matrices() {
        return times(m1.▶▶C◀◀, m2.▶▶D◀◀);
    }
}
```

Recall that MatrixExpression.I is the constant we defined to represent the identity matrix. The implementations of Scalar.matrices() and Matrix.scalars() return I to represent an empty product. We could also have returned new Identity() here, but there is no need to create a new object when we already have an immutable constant I.

m1.scalars()
scalars()
m2.scalars()
scalars()
m1.matrices()
matrices()
m2.matrices()
matrices()

With these helper functions, optimize() can just separate the scalars and the matrices:

```java

class Identity implements MatrixExpression {
    ...
    public MatrixExpression optimize() { return this; }
}

class Scalar implements MatrixExpression {
    ...
    public MatrixExpression optimize() { return this; }
}

class Matrix implements MatrixExpression {
    ...
    public MatrixExpression optimize() { return ▶▶A◀◀; }
}

class Product implements MatrixExpression {
    ...
    public MatrixExpression optimize() {
        return times(▶▶B◀◀);
    }
}
```