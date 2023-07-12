# 5. 多处理器编程：从入门到放弃

## 课后阅读

### 桃子

教授用🍑来举例子，如果一群🧑‍🦰想要吃桌子上放置的🍑，让他们彼此直接拿是高效的做法。
但是会出现一个👨‍🚀已经伸出👋去拿但是🍑被另一个👨‍🍳拿去做🥫。
我们可以让他们去排队，这样一定能够保证所有人伸出🫴都一定会得到🍑,但是这样一来就变得很缓慢。
所以我们希望能够又快又准确。

在💻中有一类程序called mutil-threaded。每一个thread代表着着程序去完成工作。thread(=🕺)需要访问某块内存来获得数据（=🍑)。所以如果不采取一些措施，我们就会遭遇上面讨论的问题。

对于多线程OS主要使用🔒和condition variable的primitives。

### Concurrency

一个thread创建之后，可能会立即执行也可能会处于等待执行的状态，这主要取决于==调度程序==。先创建的线程$\ne$先执行。

#### Share data

多个thread有时需要修改一个全局变量，这个变量就是一个share data。各个thread之间在执行时，也会受到线程调度程序的控制，在某一个时间点switch context，将数据保存到TCB(thread control block)。

对一个share data：counter执行++操作，实际汇编

```asm
100 mov 0x8049a1c, %eax
105 add $0x1, %eax
108 mov %eax, 0x8049a1c 
```

一条代码并对应着三条汇编，所以实际上每一个thread在执行counter++操作时都是可能被打断的。打断的位置不同，同时每一个thread都是独立的PC，造成的结果也是不相同。

上述执行的代码(asm)执行的结果是不确定的，这段代码被称作critical section。代码的结果与线程的随机调度顺序相关的状况被称作race condational。

对于critical section，不能够允许多个thread同时执行。
有以下一些思路来解决:

* 原子指令
  * 通过硬件来实现原子指令，消除中间状态。如此可以保证线程的执行结果都是确定的，仅和输入（可能来自其他的线程）相关，而不会线程受到线程切换的影响。

## 多处理器编程入门
gcc hello.c && ./a.out
top

printf()
crash
无法打印。

./a.out | less | sort -nk3
## 放弃 (1)：原子性

