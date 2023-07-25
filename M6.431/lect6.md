# Lec. 6: Variance; Conditioning on an event; Multiple r.v.'s

* Variance and its properties
  * Variance of the Bernoulli and uniform PMFs
* Conditioning a r.v. on an event
  * Conditional PMF, mean, variance
  * Total expectation theorem
* Geometric PMF
  * Memorylessness
  * Mean value
* Multiple random variables
  * Joint and marginal PMFs
  * Expected value rule
  * Linearity of expectations
* The mean of the binomial PMF

## Variance—— a measure of the spread of PMF

distanve from the mran
假设有两个算计变量，red和blue，他们有相同的均质（mean）—— $\mathbf{E[X]}=\mu$，随机变量的值与其期望之差。

* one point **distance from the mean:** $\displaystyle |X - \mu|$
图中的红线和蓝线是PMF的值，轴上的点是随机变量的输出。
![](ref/lect6/20230724100706.png)

* Average distance from mean
  $\mathbf{E[X - \mu]}= \mathbf{E[X]} - \mu = \mu - \mu= 0$

  由于，from the distance 是0，所以没办法获得信息。在数学上一般使用绝对值或者平方来表示（用平方更好）。

* Defination: $var(X)= E[(X- \mu)^2]$

* How to conclude the variance ,**$\displaystyle var(X)= \mathbf{E[g(x)]}= \sum_x g(x)P_X(x)$**:
    use expection value rule ,$$
      \mathbf(g(x))= (X - \mu)  \\
        \mathbf{E}[(X - \mu)^2]= \sum_{x}(x - \mu)^2 \mathbf{P_X}(x)
    $$

方差在某些方面有一些难以解释——因为使用了平方，他的单位是错误的（x= ？/m，结果的单位是 $/m^2$），更加直观的概念是标准差（stand deviation）

* $\displaystyle \sigma_X= \sqrt{var(x)}$

### property of variance

* Notation: $\displaystyle \mu = \mathbf{E}[X]$
* $Y= X + b$

$$
\begin{aligned}
v&= \mathbf{E}(Y)=\mathbf{E}(X + b)= \mathbf{E}(X) + b \\
var(Y)&=E[(Y - v)^2] \\
&= \mathbf{E}[(X + b - (\mu + b))^2]\\
&= \mathbf{E}[(X - \mu)^2]
\end{aligned}
$$
可以发现，当随机变量添加一个常量时，Variance并不会改变。从图形上看，只是将PMF向左或者向右平移，输出的点与期望的差值并没有改变。

* $Y=aX$

$$
\begin{aligned}
 v &= \mathbf{E}[Y] =  \mathbf{E}[aX]= a\mathbf{E}[X] \\
 var(Y)&= \mathbf{E}[(Y - v)^2] \\
 &= \mathbf{E}[(aX-a\mu)^2] \\
 &= \mathbf{E}[a^2(X - \mu)^2] \\
 &= a^2\mathbf{E}[(X - \mu)^2]
\end{aligned}
$$
此时可以发现，将一个随机变量乘以$a$，方差将乘以$a$的平方。

**所以存在：$var(aX+b)= a^2var(X)$**

==快速计算Variance公式：$var(x)= E[X^2] - (E[X])^2$==

A derivation that does not rely on this linearity property, goes as follows: This expression is verified as follows:

$\displaystyle var(x)= $
$=\displaystyle \sum _{x}\big (x-{\bf E}[X]\big )^2p_ X(x)$
$=\displaystyle \sum _{x}\Big(x^2-2x{\bf E}[X]+\big ({\bf E}[X]\big )^2\Big)p_ X(x)$
$=\displaystyle \sum _{x}x^2p_ X(x)-2{\bf E}[X]\sum _{x}xp_ X(x)+\big ({\bf E}[X]\big )^2\sum _{x}p_ X (x)$
$=\displaystyle  {\bf E}[X^2]-2\big ({\bf E}[X]\big )^2+\big ({\bf E}[X]\big )^2$
$=\displaystyle {\bf E}[X^2]-\big ({\bf E}[X]\big )^2.$

## Variance of the Bernoulli and the uniform

计算常见算计变量的方差

### Bernoulli

$X=\displaystyle \begin{cases}
  1 \qquad p \\
   0 \qquad 1-p
\end{cases}$

1. 适用方差定义 + 期望rule
$\displaystyle \mu = \mathbf{E}[X]= p$
  $\displaystyle var(x)= \mathbf{E}[(X - \mu)^2]= \sum_x (x-\mu)^2P_X(x)=(1-p)^2p + (0 -p)^2(1-p)= p + p^3 -2p^2 + p^2 - p^3= p - p^2 $

2. 使用quick formula
$\displaystyle var(x)= \mathbf{E}[x^2]- (\mathbf{E}[x])^2$
$\displaystyle 1^2 = 1 \quad 0^2= 0 \rightarrow x=x^2 \, , x\in \{1,0\} $
$\displaystyle var(x)= \mathbf{E}[x] - (\mathbf{E}[x])^2 = p - p^2 = p(1-p)$

使用表达式，$p(1-p)$作出图像,
![](ref/lect6/20230725090929.png)

伯努利随机变量的方差形式对p有一个有趣的依赖关系。p乘以1减去p是一个抛物线。当p为0或1时，它是0。它具有这个特定的形状，这个抛物线的峰值出现在p等于1/2的时候，此时方差为1/4。

**方差是衡量随机变量不确定性或者说随机性的量**
当一枚硬币投掷时，如果是公平的，即$p = 1$，他是最随机的，硬币的方差在最公平的时候随机性最高。
但是当为1,和0时，方差为0,此时结果是确定的，完全不随机。
从期望的角度来说，期望就是根据已值的情况，所显示的最有可能的结果（因为按照概率的比重），所以当随机变量的值与期望差别越来越大时，越说明距期望的结果越远，也就越随机。

### Uniform

![](ref/lect6/20230725092458.png)

$\displaystyle var(X)= \mathbf{E}[x^2] - (\mathbf{E}[X])^2 = \frac{1}{n+ 1}(1^2 + 2^2 + 3^2  + \cdots n^2) - (\frac{n}{2})^2$

$= \displaystyle \frac{1}{n + 1}(\frac{1}{6}n(n + 1)(2n + 1))- (\frac{n}{2})^2= \frac{1}{12}n(n+2)$

对于一般的形式，

![](ref/lect6/20230725095727.png)

第一幅图像和第二幅图像有什么区别?
我们令$n = b - a$,第二幅图可以视作是第一幅图的随机变量添加了一个常数。如果一个随机变量取值为0,那么第二个PMF中他所表是的就是a，为1就是$a+1$.

所以这个偏移后的PMF是与随机变量等于原始随机变量加上一个常数相对应的PMF。但我们知道加上一个常数不会改变方差。因此，只要我们做出n等于b减去a的对应，这个PMF的方差将与原始PMF的方差相同。

所以在之前推导的公式中进行这个替换，我们得到 $\displaystyle var(x)= \frac{1}{12}(b-a)(b-a + 2)$。

## Conditional PMFs and expectations given an event

