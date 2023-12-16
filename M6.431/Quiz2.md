# Quiz 2

## 2018 spring

### a

Let X1, X2, and X3 be independent random variables with the continuous uniform distribution over [0, 1]. Then P(X1 < X2 < X3) =


### b 

Let X and Y be two continuous random variables. Then
(i) E[XY ] = E[X]E[Y ]
(ii) E[X2 + Y 2] = E[X2] + E[Y 2]
(iii) fX+Y (x + y) = fX(x)fY (y)
j(iv) var(X + Y ) = var(X) + var(Y )

### c

Suppose X is uniformly distributed over [0, 4] and Y is uniformly distributed over [0, 1]. Assume
X and Y are independent. Let Z = X + Y . Then
(i) fZ(4.5) = 0
(ii) fZ(4.5) = 1/8
(iii) fZ(4.5) = 1/4
(iv) fZ(4.5) = 1/2

$\displaystyle (e^(-λ) * λ^k) / k!$

### f

Suppose X and Y are Poisson random variables with parameters λ1 and λ2 respectively, where
X and Y are independent. Define W = X + Y , then
(i) W is Poisson with parameter min(λ1, λ2)
(ii) W is Poisson with parameter λ1 + λ2
(iii) W may not be Poisson but has mean equal to min(λ1, λ2)
(iv) W may not be Poisson but has mean equal to λ1 + λ2

* The Sum of Independent Normal Random Variables

### g

Let X be a random variable whose transform is given by $M_X(s) = (0.4 + 0.6e^s)^50$. Then
(i) P(X = 0) = P(X = 50)
(ii) P(X = 51) > 0
(iii) P(X = 0) = (0.4)50
(iv) P(X = 50) = 0.6

Solution: Note that $M_X(s)$ is the transform of a binomial random variable X with n = 50 trials and the probability of success p = 0.6. Thus, $P(X = 0) = 0.4^50.$ 

这道题目使用了moment generating function，暂时撤。

### h

Let Xi, i = 1, 2, . . . be independent random variables all distributed according to the PDF $f_X(x) = \frac{x}{8}$ for 0 ≤ x ≤ 4. Let $S = \frac{1}{100} \sum_{i=1}^{100} X_i$. Then P(S > 3) is approximately equal 100 i=1
to
(i) 1 − Φ(5)
(ii) Φ(5) 
(iii) $1 − Φ \frac{5}{\sqrt{2}}$  
(iv) $ Φ \frac{5}{\sqrt{2}}$  

Solution: 
Let $ S = \frac{1}{100} \sum_{i = 1}^{100}Y_i $ where $Y_i$ is the random variable given by $Y_i$ = $X_1/100$. Since Yi are iid, the distribution of S is approximately
normal with mean E[S] and variance var(S).
3 − E(S)
Thus, P(S > 3) = 1 − P(S ≤ 3) ≈ $1 − Φ(\frac{3 - E(s)}{\sqrt{var(s)}}) $. Now,

* 这里需要使用CLT的结论，将s转化成 Z(z是 var(Z)=1  E[Z] = 0)。

### i
           
Let Xi, i = 1, 2, . . . be independent random variables all distributed according to the PDF f_X(x) = 1, 0 ≤ x ≤ 1. Define Y_n = X_1X_2X_3 . . . X_n, for some integer n. Then var(Yn) is equal to

solution: 

$\frac{1}{3^n} - \frac{1}{4^n} $

* 要认识到 E[XY] = E[X]E[Y]，在X和Y为独立的前提下。

### Question 2: 

Each Mac book has a lifetime that is exponentially distributed with parameter λ.The lifetime of Mac books are independent of each other. Suppose you have two Mac books, which you begin using at the same time. Define T1 as the time of the first laptop failure and T2 as the time of the second laptop failure.

a. (4 pts) Compute fT1 (t1)
b. (5 pts) Let X = T2 − T1. Compute fX|T1 (x|t1)
c. (5 pts) Is X independent of T1? Give a mathematical justification for your answer.
d. (8 pts) Compute fT2 (t2) and E[T2]
e. (5 pts) Now suppose you have 100 Mac books, and let Y be the time of the first laptop failure.

Find the best answer for P(Y < 0.01)

Your friend, Charlie, loves Mac books so much he buys S new Mac books every day! On any given day
S is equally likely to be 4 or 8, and all days are independent from each other. Let S100 be the number
of Mac books Charlie buys over the next 100 days.

f. (6 pts) Find the best approximation for P(S_100 ≤ 608). Express your final answer in terms of Φ(.), the CDF of the standard normal.

Let $X = T_2 − T_1$. Compute $f_{X|T_1} (x|t_1)$.

Solution: 

Conditioned on the time of the first mac book failure, the time until the other mac book fails is an exponential random variable by the memoryless property. The memoryless property tells us that regardless of the elapsed life time of the mac book, the time until failure has the same exponential CDF. Consequently,

$f_{X|T1 (x)} = λe^{−λx} \quad, x ≥ 0$.


b 项，需要运用指数分布的memoryless的性质。在本题中是如下的表达式：

$\displaystyle P_{X \mid T_1}(x \mid t_1) = P_{T_2 \mid T_1}(x \mid t_1 ) = P_{T_2}(x) $

更加直观：$\displaystyle P(T_2 = T_1 + x \mid T_1 = t_1) = P_{T_2}(x)$
与灯泡的例子所不同的是此处将已经“使用了的时间设置成了一个随机变量”。

c 项，对于独立的定义：P(A,B) = P(A)P(B)，此处使用定义来判定。

$ f_{X, T_1}(x, t_1) = f_{T_1}(t_1)f_{X \mid T_1}(x \mid t_1) $


$ f_{X \mid T_1}(x \mid t_1) = \lambda e^{-\lambda x} $
在T_1的条件下，可以理解为模型变化成了所熟悉的样例中的样子，变成了一个单纯的指数分布。

d 项，
$ f_{X \mid T_1}(x \mid t_1) = f_{X}(x) $ 这个就很类似T_2已经使用了t_1时间，继续使用x时间。但是与笔记中了样例并不相同：他只是一个灯泡，而且将总时间设置为T，使用了一个常量t_1，剩余使用的时间设置为X。然后得出了结论：$\displaystyle P(X = x \mid T > t_1 ) \quad , T = t_1 + x \quad, P(T = x + t_1) $
由此可以得出: $\displaystyle P(X = x \mid T > t) = P(T = x) $

这个我是不知道为什么：$\displaystyle f_X(x) = \lambda e^{- \lambda x}$
虽然我可以得到$\displaystyle f_{X \mid T_1}(x \mid t_1)$，在条件下他是等效于 💡模型，但是为什么f_X(x)也会是指数分布？

e 项，思路与a是一样的。


Your friend, Charlie, loves Mac books so much he buys S new Mac books every day! On any given day S is equally likely to be 4 or 8, and all days are independent from each other. Let S100 be the number of Mac books Charlie buys over the next 100 days.

f. (6 pts) Find the best approximation for P(S100 ≤ 608). Express your final answer in terms of
Φ( ), · the CDF of the standard normal.

Solution
Using the De Moivre - Laplace Approximation to the Binomial, and noting that the step size between values that S can take on is 4,

使用CLT的思想，但是这道题目额外使用了De Moivre - Laplace，暂时没学。

$p(S_100 \leq 608) = Φ(\frac{(608 + 2 - 100 * 6)}{ \sqrt{100 * 4}}) $


### Question 3: 

Question 3: Saif is a well intentioned though slightly indecisive fellow. Every morning he flips a coin to decide where to go. If the coin is heads he drives to the mall, if it comes up tails he volunteers at the local shelter. Saif’s coin is not necessarily fair, rather it possesses a probability of heads equal to q. We do not know q, but we do know it is well-modeled by a random variable Q where the density of Q is

$$
f_Q(q)=
\begin{cases}
2q \quad for 0 \leq q \leq 1 \\
0 \quad otherwise    
\end{cases}
$$

Assume conditioned on Q each coin flip is independent. Note parts a, b, c, and {d, e} may be answered independent of each other.
a. (4 pts) What’s the probability that Saif goes to the local shelter if he flips the coin once?

In an attempt to promote virtuous behavior, Saif’s father offers to pay him $4 every day he volunteers at the local shelter. Define X as Saif’s payout if he flips the coin every morning for the next 30 days.

b. (6 pts) Find var(X)

Let event B be that Saif goes to the local shelter at least once in k days.
c. (6 pts) Find the conditional density of Q given B, fQ B(q) |
While shopping at the mall, Saif gets a call from his sister Mais. They agree to meet at the Coco Cabana Court yard at exactly 1:30PM. Unfortunately Mais arrives Z minutes late, where Z is a continuous uniform random variable from zero to 10 minutes. Saif is furious that Mais has kept him waiting, and demands Mais pay him R dollars where R = exp(Z + 2).
    
d. (6 pts) Find Saif’s expected payout, E[R]

e. (6 pts) Find the density of Saif’s payout, fR(r)


a 项，令我感到迷惑的是
$$
f_{X,Y}(x,y) = f_{Y}(y)f_{X \mid Y}(x \mid y) \\ \\ 
f_X(x) = \int f_Y(y)f_{X \mid Y}(x \mid y) dy
$$

写出来后清晰很多，实际上$\displaystyle f_{X\mid Y}(x \mid y)f_Y(Y)=f_{X,Y}(x,y)$，在将其作积分实际上他就是运用了joint PDF 计算margin PDF.

$\displaystyle P(X)= P( X=x\mid Q=q )P_Q(q)$

$\displaystyle \int_0^1 (1-q) f_q (q)dq$

b 项，思路的关键点是使用total variance . $\displaystyle var(X)= E[var(X\mid Y)]+var(E[X\mid Y])$ 这里有一个关键点：
1. 这是我的使用total variance的思路：$var(X) = var(Y_1+Y_2+Y_3+ ...+ Y_30 = 30 var(Y) = 30 E[var(Y \mid Q)] + var(E[Y\mid Q])$
2. 这是答案的思路: $var(X) = E[var(Y_1+Y_2+Y_3+ ... + Y_30 \mid Q)] + var(E[Y_1+Y_2+Y_3+ ... +Y_1 \mid Q])$

以下引用chatgpt的回答：
>对于方差的独立性，确实当随机变量是相互独立时，它们的方差之和等于各自方差之和。然而，在这个问题中，$Y_i$ 不是相互独立的，因为它们都依赖于相同的随机变量 $Q$。每个 $Y_i$ 都是在给定 $Q$ 的情况下独立的，但它们之间并不独立。
具体来说，方差的独立性是指如果 $X_1, X_2, \ldots, X_n$ 是相互独立的随机变量，则 $\text{var}(X_1 + X_2 + \ldots + X_n) = \text{var}(X_1) + \text{var}(X_2) + \ldots + \text{var}(X_n)$。在这个问题中，$Y_1, Y_2, \ldots, Y_{30}$ 都依赖于相同的随机变量 $Q$，因此它们之间并不是相互独立的。因此，不能简单地将它们的方差相加。

c 项，计算$\displaystyle f_{Q \mid B}(q)$:

类比离散随机变量可以得到：$\displaystyle P_{Q \mid B}(q) = \frac{P(q \leq Q \leq q + \delta , B)}{P(B)}$

$\displaystyle P(B)=\sum_Q P_Q(q)P(B \mid Q=q)$，这里使用的思想是：total probability。所不同的是Q是连续随机变量。
$\displaystyle P(B) =\sum_Q f_Q(q)\times \delta \times P(B \mid Q=q)$
写成积分的形式：

$\displaystyle P(B) =\int_{-\infin}^{+\infin}P(B \mid Q=q)f_Q(q)dq$

分子部分


$\displaystyle P(q \leq Q \leq q + \delta, B) = P_Q(q)P_{B \mid Q}(B \mid q) = 
f_Q(q)\delta P_{B \mid Q}(B \mid q)$

由此可以得到：

$$
P_{Q \mid B}(q) = \frac{f_Q(q)\delta P_{B \mid Q}(B \mid q)}{\int_{-\infin}^{+\infin}P(B \mid Q=q)f_Q(q)dq} \\
f_{Q \mid B}(q)\delta  = \frac{f_Q(q)\delta P_{B \mid Q}(B \mid q)}{\int_{-\infin}^{+\infin}P(B \mid Q=q)f_Q(q)dq}\\
f_{Q \mid B}(q)  = \frac{f_Q(q) P_{B \mid Q}(B \mid q)}{\int_{-\infin}^{+\infin}P(B \mid Q=q)f_Q(q)dq}
$$

## 2009


### Problem 3. (10 points)

For the following questions, mark the correct answer. If you get it right, you receive 5 points for that question.
You receive no credit if you get it wrong. A justification is not required and will not be taken into account.
Let X and Y be continuous random variables. Let N be a discrete random variable.

a. (5 points) The quantity E[X | Y ] is always:

(i) A number.

(ii) A discrete random variable.

(iii) A continuous random variable.

(iv) Not enough informa]b
tion to choose between (i)-(iii).


b. (5 points) The quantity E[ E[X | Y, N] | N] is always:

(i) A number.

(ii) A discrete random variable.

(iii) A continuous random variable.

(iv) Not enough information to choose between (i)-(iii).


将条件期望视作随机变量：E[X]是一个number，$\displaystyle E[X\mid Y]$是关于Y的函数：$g(Y)=g(y), if Y=y$

在讨论a，b项时，要根据X和Y和N是否是独立划分情况：

1. $E[X\mid Y]=E[X] \quad if \  X \ and \ Y \ is \ independent$
2. $E[X\mid Y]=g(Y) \quad if \  X \ and \ Y \ is \ independent$


## Problem 4. (25 points)

The probability of obtaining heads in a single flip of a certain coin is itself a random variable, denoted by Q,

which is uniformly distributed in [0, 1]. Let X = 1 if the coin flip results in heads, and X = 0 if the coin flip

results in tails.

(a) 
(i) (5 points) Find the mean of X.
(ii) (5 points) Find the variance of X.

(b) (7 points) Find the covariance of X and Q.

(c) (8 points) Find the conditional PDF of Q given that X = 1.


b 项，计算协方差：$\displaystyle cov(X,Q)=E[XQ] -E[X]E[Q]$,在计算E[XQ]时需要用到条件期望的思想：

1. 条件的思想就是将一个复杂的计算缩小一定的范围来计算，这是一种分而治之的策略。
2. 期望的分治思想的具体体现就是使用：total expection 和 iteration rule.

$\displaystyle E[XQ]= E[E[XQ \mid Q]] = \sum_X \sum_Q xqP_{X,Q \mid Q}(x,q)$

$\displaystyle P_{X,Q \mid Q}(x,q)=P_{Q \mid Q}(q) P_{X \mid Q ,Q}(x)=P_{Q}(q)P_{X \mid Q}(x)$

利用这两个式子可以用定义表示出$\displaystyle E[XQ \mid Q] \to E[Q]E[X \mid Q]$

$\displaystyle E[Q] = q$。

### Problem 5

Let X and Y be independent continuous random variables with marginal PDFs fX and fY , and marginal
CDFs FX and FY , respectively. Let S = min{X, Y }, L = max{X, Y }.

(a) (7 points) If X and Y are standard normal, find the probability that S ≥ 1.

(b) (7 points) Fix some s and £ with s ≤ £. Give a formula for P(s ≤ S and L ≤ £)
involving FX and FY , and no integrals.

(c) (7 points) Assume that s ≤ s + δ ≤ £. Give a formula for
P(s ≤ S ≤ s + δ, £ ≤ L ≤ £ + δ), as an integral involving fX and fY .
 


b 项，要注意转换成X和Y的表达式：$\displaystyle P(s \leq X, s\leq Y \ and \ X \leq l, Y \leq l)$

b的答案应该有问题，应该X和Y的表达式是一致的。

c 项，分情况讨论：X > Y and Y > X

## 2010

### Problem 1. (80 points) In this problem:

(i) X is a (continuous) uniform random variable on [0, 4].
(ii) Y is an exponential random variable, independent from X, with parameter λ = 2.

1. (10 points) Find the mean and variance of X − 3Y .
2. (10 points) Find the probability that Y ≥ X.
(Let c be the answer to this question.)
3. (10 points) Find the conditional joint PDF of X and Y , given that the event Y ≥ X has
occurred.(You may express your answer in terms of the constant c from the previous part.)
4. (10 points) Find the PDF of Z = X + Y .
5. (10 points) Provide a fully labeled sketch of the conditional PDF of Z given that Y = 3.
6. (10 points) Find E[Z | Y = y] and E[Z | Y ].
7. (10 points) Find the joint PDF fZ,Y of Z and Y .
8. (10 points) A random variable W is defined as follows. We toss a fair coin (independent of Y ).
If the result is “heads”, we let W = Y ; if it is tails, we let W = 2 + Y . Find the probability of
“heads” given that W = 3.

2 项， 解决的思路是 total probability。只不过题目的表述不太符合习惯。将$\displaystyle Y \geq X$设为事件A，那么可以将其表述为$\displaystyle P(A) = \sum _X P(A \mid X)P(x)$
指数分布 $\displaystyle F(Y \geq y)= e^{-\lambda y}$

3 项，考试思路：当需要计算条件PDF，首先思考bayes。

承接2的叙述再加上使用Bayes $\displaystyle f_{X,Y \mid A}(x,y) = \frac{f_{X,Y}(x,y)}{P(A)}$。这里存在一个误区，将联合的密度表述为：$\displaystyle f_{X,Y,A}(x,y,A)$。
前者是将A当成是一个事件，后者是认为他是一个随机变量。此处引用chatgpt3.5：

>事件表述的是样本空间可能的结果，随机变量是将样本空间映射值的映射。

也就是说，事件是样本空间的结果，随机变量本身代表的就是样本空间，不过使用映射的形式。

4 项，答案给出了如下的形式，经过了变换，讲道理不太能看懂这种形式：$\displaystyle f_Z(z) = \int _{Max(0, z-4)} ^z \frac{1}{4}2e^{-2t}dt$

前面的思路就是利用卷积的公式来计算，但此处需要注意，由于X和Y的定义域都不是对应这整个数轴，所要要确保z的范围是在有效的范围中(因为z来源于X和Y)

分类讨论(要同时能够满足X和Y)：
1. $\displaystyle z \geq 4$,这种情况下， $y= z-x\geq 0$一定成立，所有x的范围[0,4]
2. $\displaystyle 0 \leq z \leq 4$,这种情况下，$y= z-x\geq 0, \quad x\leq z$,所以此时 x的范围[0,z]

在这道题中我重新思考了total probability 在连续和离散随机变量之中的联系：

对于离散随机变量来说，求一个事件的概率直接使用total probability，他直接表述成：$\displaystyle P_X(x)=P_{X \mid Y}(X \mid y=y_1)P_Y(y)+P_{X \mid Y}(X \mid y=y_2)P_Y(y)+P_{X \mid Y}(X \mid y=y_3)P_Y(y)+ \dots +P_{X \mid Y}(X \mid y=y_n)P_Y(y) = \sum_Y P_{X\mid Y}(x \mid y)P_Y(y)$

对于连续随机变量来说，total probability的最上层的思想是：joint PDF求取margin PDF。
$\displaystyle f_X(x)=\int_{-\infin}^{\infin}f_{X,Y}(x,y)dy$，通过乘法法则将其转换成：
$\displaystyle f_X(x)=\int_{-\infin}^{\infin}f_{X\mid Y}(x\mid y)f_Y(y)dy$,这最终得到了贴合离散随机变量的total probabiliy 的形式，也就是连续随机变量的表述形式。

比较这两者的形式，积分号对应着求和号，再将PDF通过乘上z的变化量，用$\delta$表示，就可以看到两者在数学表达上的一。

以下是大致的结构:

discrete: $\displaystyle P_Y(y)=\sum_X P_{X \mid Y}(x\mid y)P_{X}(x)$

discrete -> continuous: $\displaystyle f_Y(y) \epsilon=\sum_X f_{X \mid Y}(x\mid y)\epsilon f_{X}(x)\delta$

continuous: 
$\displaystyle f_Y(y) =f_{X \mid Y}(x\mid y) f_{X}(x)$ 

or 

$\displaystyle f_Y(y) =\int _x f_{X \mid Y}(x\mid y)f_{X}(x)dx$

8 项，思路是使用Bayes。

这里出现了一个迷惑点：当存在连续变量在Bayes的表达式中，而要推断的随机变量是离散的，那我应该怎么表示连续随机变量在某一个点上的概率，这是为零。

事实上，连续的部分是使用PDF和\delta的乘积表示，但是所有的连续部分最后都会将\delta削掉。

### Problem 2. (30 points)

Let X, X1, X2, . . . be independent normal random variables with mean 0
and variance 9. Let N be a positive integer random variable with E[N] = 2 and $E[N^2] = 5. $We assume that the random variables N, X, X1, X2, . . . are independent. Let $S = \sum_{i=1}^N X_i$.

1. (10 points) If δ is a small positive number, we have P(1 ≤ |X| ≤ 1 +δ) ≈ αδ, for some constant
α. Find the value of α.
2. (10 points) Find the variance of S.
3. (5 points) Are N and S uncorrelated? Justify your answer.
4. (5 points) Are N and S independent? Justify your answer.

2 项，计算方差和期望使用定义(方差额外有简便形式计算)较为困难时，可以考虑使用条件期望(iterated expection $E[X \mid Y]$)或者条件方差($var(X_1) = E[var(X_1 | Y)] + var(E[X_1 \mid Y])$)

3 项，计算 相关性可以使用协方差或者条件系数的定义计算。

* 协方差计算的简便形式：$\displaystyle cov(X,Y)=E[XY]-E[X]E[Y]$
* 期望的性质：$E[X + Y] = E[X]+E[Y]$，这个性质是不需要要求独立的，因为采用定义会得到如下的式子：$\displaystyle \sum_X \sum_Y XP_{X,Y}(x,y)+\sum_X \sum_Y YP_{X,Y}(x,y)$
* $var(X_1 + X_2)= var(X_1) + var(X_2) + cov(X_1,X_2)$ 如果X_1 和 X_2 是独立的，那么cov(X_1, X_2)就是0，var(X_1 + X_2)= var(X_1) + var(X_2)

4 项证明S和N的相关性：

* 相关性这证明使用定义：f_S(s)=f_{S \mid N}(S \mid N)
* 这道题目利用：var(S) != var(S | N),来证明 f_S(s)=f_{S \mid N}(s \mid n).因为方差的计算与随机变量本身的分布有关。
* S被认为是正态分布,原因是 CLT(chatgpt说的)。

