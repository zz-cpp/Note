# Unit 1: The Derivative
## What is the derivative?

* Instantaneous rate of change

Objectives

* Describe what is meant by an average rate of change, and compute them with appropriate units.

* Describe the difference and relationship between the average rate of change and an instantaneous rate of change.

* Use a limit to find the instantaneous rate of change, also known as the derivative at a point.

* Interpret the sign of a derivative - positive, negative, or zero - as having real-world meaning.


### Rates of change

假设在50mi处，时间是8：00am,在10：00am,位置是100mi处。

由此可以计算出平均速率：$\displaystyle \frac{100 - 50}{10 - 8} = 25$

现在有一个问题，假如想要计算8am的瞬时速度（instanous velocity）。怎么做？

有一种思路，我们将终端时刻n提前，假设在8：10am，这时在51mi处。
使用平均速度的想法：$\displaystyle \frac{51 - 50}{(8:01 - 8:00)/60}= 60$


这样可以得到一个所在区间更小的平均速度，这个速度很直白的感觉与8：00am的瞬时速度很接近。但他并不是，本质上来说，他依然是平均速度。

现在，可以重复刚才的做法的做法，继续将终端时刻向初始时刻靠近。这样我们可以继续得到靠近8：00am的平均速度。虽然这并不是我们想要的结果。

继续推广平均速度，如果使用平均速度来表示i瞬时速度，那么只有当初始时刻等于终端时刻才能表示。

如果真的这么做了，会发现得到一个$\displaystyle \frac{0}{0}$的式子，但这么做没有意义能，因为这样的式子本身没有意义。

也就是说我没没办法n真正得到一个计算instanous velocity的式子。

但是别忘了，我们这种不断的将一个点靠近另一个点的思想，但是最终又无法真正的抵达另一个点的思想，与之前的limit思想如出一辙。

通过计算靠近8:00am的limit，就可以得到一个无限接近但又不等于8：00的值。

用公式来表达：$\displaystyle \lim_{\Delta x \to 0} \frac{\Delta f}{\Delta x}, \quad f(x)$

### Definition of the derivative

The derivative of a function  $f(x)$at a point $x=a$ is defined to be

$\displaystyle f'(a) = \lim _{b\rightarrow a} \frac{f(b) - f(a)}{b-a}.
$

也有另一种定义方式: 
$\displaystyle f'(a) = \lim _{h\rightarrow 0} \frac{f(a+h) - f(a)}{h}.$

derivative 测量的是函数输出对应函数输入的瞬时变化率。

