# Unit 7: Bayesian inference

## Inferring a Discrete Random Variable from a Continuous Measurement

Let X be a discrete random variable that takes the values 1 with probability p and −1 with
probability 1 − p. Let Y be a continuous random variable independent of X with the Laplacian (two-sided exponential) distribution

$\displaystyle f_Y(y) = \frac{1}{2} \lambda e^{-\lambda |y|}$

and let Z = X + Y . Find P(X = 1 | Z = z). Check that the expression obtained makes sense
for p → 0+, p → 1− , λ → 0+, and λ → ∞.

* $\displaystyle P(X = 1 \mid Z = z) = \frac{P(X = 1, Z = z)}{P_Z(z)} = \frac{P_X(1)P_{Z \mid X}(z \mid 1)}{P_{Z \mid X}(z \mid x = 1) \times P_{Z \mid X}(z \mid x = -1)} $ 
最终得到的表达式: 

* $\displaystyle P_{Z \mid X}(z \mid x=1) = P_Y(z - 1)$
* 分布使用total probability

* 明确各种情况的意义：
  * $\displaystyle \lambda \to \infin$，Y的概率密度图像会变成与y轴的交接处会变得非常高，但下降的也会非常快。根据概率密度来看，Y大概率会是0，所以y的影响会变得非常小。

## 

Let Q be a continuous random variable with PDF

fQ(q) = 6q(1 − q), if 0 ≤ q ≤ 1,
0, otherwise.
This Q represents the probability of success of a Bernoulli random variable X, i.e.,
P(X = 1 |
 Q = q) = q.
Find fQ|X(q|x) for x ∈ {0, 1} and all q.


## A Derived Distribution Example

如果已知的随机变量是建立在一个已知的分布基础之上，通过某一些变换从而派生出来的，想要求取他的概率密度。解决这一类问题通常按照以下的流程：

1. 首先确定出能够求取某一个范围的CDF: $\displaystyle F( y) = P(Y \leq y)$
2. 根据已知的关系，将这个未知量和他的CDF相关的范围转化为已知量和其相关的范围。(这一步可以做出Y与X的关系图，在图中确定Y的范围对应X的范围是多少。)
3. 根据已知量的分布，计算Y的CDF
4. 求导，得到Y的PDF，注意要标注Y的有效范围。

## Inferring a Parameter of Uniform

Romeo and Juliet start dating, but Juliet will be late on any date by a random amount X, uniformly distributed over the interval [0, θ]. The parameter θ is unknown and is modeled as the value of a random variable Θ, uniformly distributed between zero and one hour.

(a) Assuming that Juliet was late by an amount x on their first date, how should Romeo use this information to update the distribution of Θ?

(b) How should Romeo update the distribution of Θ if he observes that Juliet is late by x1, . . . , xn on the first n dates? Assume that Juliet is late by a random amount X1, . . . , Xn on the first n dates where, given θ, X1, . . . , Xn are uniformly distributed between zero and θ and are conditionally independent.
(c) Find the MAP estimate of Θ based on the observation X = x.

(d) Find the LMS estimate of Θ based on the observation X = x.

(e) Calculate the conditional mean squared error for the MAP and the LMS estimates. Compare
your results.

(f) Derive the linear LMS estimator of Θ based on X.

(g) Calculate the conditional mean squared error for the linear LMS estimate. Compare your answer
to the results of part (e). 

solution: 

* (a)部分在处理$f_X(X)$时，要注意有效的范围：$x \leq \theta$，所以在使用积分的时候不是从1到0。
* (b)部分与此是相同的，同样要考虑$\displaystyle x \leq \theta $，所不同的是，由于这次有多个样本x，所以需要满足：所有的x样本都必须要小于$\theta$，因此设置一个变量$\displaystyle \bar{x}= MAX(x_1,x_2,...,x_n)$
* 