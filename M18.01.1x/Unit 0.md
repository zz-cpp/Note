# Unit 0: Limits
## Introduction to Limits
微积分（caculas）有两个主要的概念——integral（积分） and derivative（导数）。要了解这两个概念首先必须知道limit（极限）。
假设在一条曲线上区两个点A，B。A固定不动,过A，B两点做一条直线，当B逐渐向A靠近。取极限（limit），这条直线会变成一条“切线”，这条切线的slope（斜率）就是在点A的derivative（导数）。

计算一条曲线围成的面积，通常很难直接进行计算，而一条水平线围成的面积则很容易。最初一个每一个小矩形存在一个较大的宽度。当矩形的宽度越来越小，矩形的数量越来越多，小矩形的面积之和越来越接近曲线围城的面积。小矩形的面积之和的极限就是这个曲线的面积（宽度趋近0）。

## Moving closer and closer
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