# Unit 0: Limits
## Introduction to Limits
微积分（caculas）有两个主要的概念——integral（积分） and derivative（导数）。要了解这两个概念首先必须知道limit（极限）。
假设在一条曲线上区两个点A，B。A固定不动,过A，B两点做一条直线，当B逐渐向A靠近。取极限（limit），这条直线会变成一条“切线”，这条切线的slope（斜率）就是在点A的derivative（导数）。

计算一条曲线围成的面积，通常很难直接进行计算，而一条水平线围成的面积则很容易。最初一个每一个小矩形存在一个较大的宽度。当矩形的宽度越来越小，矩形的数量越来越多，小矩形的面积之和越来越接近曲线围城的面积。小矩形的面积之和的极限就是这个曲线的面积（宽度趋近0）。

### Moving closer and closer
微积分研究的对象是函数，但在此处我们关注的是一个范围中的输入，而不是某一个输入。
变量x自x=0处从左向右移动，不断地靠近1。注意：靠近不是等于（永远不不要认为相等）。
x对应的函数f(x) = $\displaystyle \sqrt{\frac{3-5x+x^2+x^3}{x-1}}$。
根据这个函数，x在x=1处没有定义。

|x|f(x)|
|---|---|
|0|$-\sqrt{3} \approx -1.73$|
|0.5|-1.87|
|0.9|-1.97|
|0.99|-1.997|

由上表可知，随着x越来越接近1,f(x)越来越接近-2；
当从右边向左边移动x，逐渐靠近1

### One-sided limits

由此表格：
|x|f(x)|
|---|---|
|0|$-\sqrt{3} \approx -1.73$|
|0.5|-1.87|
|0.9|-1.97|
|0.99|-1.997|

记作 $\displaystyle x \rightarrow 1^-,f(x)\rightarrow -2$。其中$1^-$表示的是x从左边或者说是从负方向接近（approach）1,$f(x)$接近-2。

相对应的，当x从右边（正方向）接近1时，
|x|f(x)|
|---|---|
|2|2.24|
|1.5|2.12|
|1.1|2.02|
|1.05|2.002|

f(x)会靠近2。
但是，以上的情况在x=1时，无法得到一个值因为分母是x-1。
绘制成图像如下，

![](ref/Unit%200/20230719150411.png)

此处将函数值趋近于某个值给出一个正式的名字。称之为极限（limit）。
所以在右边，我们将说f(x)在x趋近于1时的极限是2，表示：$\displaystyle \lim_{x \to 1^+}f(x) =2$,通常称为在x等于1点的右侧极限或右极限。
左侧是相对应的情况。

### Definitions of right-hand and left-hand limits

![](ref/Unit%200/20230719151727.png)
Suppose $\  f(x)$ gets really close to $R$ for values of $x$ that get really close to  (but are not equal to)  $a$ from the right. Then we say $R$ is the ==right-hand limit== of the function $\ f(x)$ as $x$ approaches $a$ from the right.

We write
$$
f(x) \rightarrow R\ as \  x \rightarrow a^+ \\
or \\
\lim _{x\rightarrow \mathbf{a^+}} f(x) = R
$$

if $\  f(x)$ gets really close to $L$ for values of $x$ that get really close to  (but are not equal to)  $a$ from the Left. Then we say $L$ is the ==left-hand limit== of the function $\ f(x)$ as $x$ approaches $a$ from the right.

### Possible limit behaviors
right-limit和left-limit表现出如下的行为

* 左极限和有极限存在但不相等
  * ![](ref/Unit%200/20230719162208.png)

* 左极限和右极限存在且向等
  * ![](ref/Unit%200/20230719162446.png)
  * 此处如果$\ g(x)$的值存在且等于极限值，那么空心圆会使用实心圆。但实际上在讨论极限时，我们并不会考虑$\ g(a)$，它是无关紧要的，实际函数中，我认为$\ g(a)$根本不存在。

* 极限不存在
  * 极限值趋近与无穷（$\infin\ or \ -\infin）$
    * ![](ref/Unit%200/20230719163703.png)
    * ![](ref/Unit%200/20230719163525.png)
    * 用DNE表示（does not exsit）
  * 函数值在不停地振荡，无法测量
    * ![](ref/Unit%200/20230719163908.png)

#### exercise
$$
Suppose \  f(a) = K.\  that . Must \lim_{x \to a^+}{f(x)= K}?
$$
这个论述是错误的，**极限与某一个点的实际值是否存在和值的多少并没有联系。**
