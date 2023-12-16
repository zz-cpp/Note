# The Basic Tools

## Power Editing 

First, look at yourself while you’re editing. Every time you find yourself doing something repetitive, get into the habit of thinking “there must be a better way.” Then find it.
Once you’ve discovered a new, useful feature, you now need to get it installed into your muscle memory, so you can use it without thinking. The only way we know to do that is through repetition. Consciously look for opportunities to use your new superpower, ideally many times a day. After a week or so, you’ll find you use it without thinking.

## Debuging

### Fix the Problem, Not the Blame

It doesn’t really matter whether the bug is your fault or
someone else’s. It is still your problem.

### A DEBUGGING MINDSET

在debug之前，保证你现在是舒服的，这意味着你要释放你的压力。扔掉你一直以来思维习惯。
不要害怕。
当你看见了报错的信息，不要一根经的想着“这不可能发生”，他已经发生了。
不要总是盯着报错的地方，抑制住你一致想修改报错符号的冲动。真正有问题的部分可能离报错的地方还隔着很多的状态。
开始debug前，尽可能调高编译器的的日志等级，去掉warning的警告。
要尽可能让数据准确，剔除掉意外的因素，有时可能是用某种的操作顺序造成bug。
尽可能测试更多的边界情况。

## Pragmatic Starter Kit

### FULL AUTOMATION

### DEBUGGING STRATEGIES

1. 让你的bug能够复现。
2. 在修复代码之前应该先编写让程序失败的测试。也就是隔离bug。
3. 如果程序没有crash，而是错误的值，那么此时使用debugger是一个好选择。查看stack，检查local environment。用笔快速记录很有帮助。如果流程太长可以使用二分查找法。

### Bug scenarios

#### Sensitivity to Input Values

如果你的程序因为某一个特殊的数据集而崩溃，那么将这个数据集copy，让他在出问题的应用的复制上跑，这么一来可以使用二分法来确定是哪一个值导致了crash

### THE BINARY CHOP

在测试data set 或者在一些列的release版本中找到出现bug的版本，可以采用类似binary chop(binary search)。

### Logging and/or Tracing

使用trace message 来跟踪程序的状态(数据结构或者值)。它通常是：

1. 程序运行的位置
2. "x = 2"
这样的结构。
保持trace messages的格式一致很有帮助，在通过文本处理工具或者shell命令来处理log file。

### Rubber Ducking

### eliminant prove when debug

当你只做了一件事，产生了bug，那么无论它是什么，他就是最有可能的原因。

不要过于自信，而是要始终保持怀疑和验证的态度。当出现问题时，不要假设代码没有错误，而是积极地验证它，以确保它在特定情境下的正确性。这是提高代码质量和解决问题的关键之一。

当遇到意外的错误时，不仅要修复它，还需要确定为什么这个故障之前没有被捕获。
要考虑是否有相同或者相类似的地方需要一并修复。
如果修复这个错误的时间过长，需要考虑是否应该加日志文件分析器。
修复错误的同时，需要进行根本性的分析和改进，以便在未来不再受到相同或类似错误的困扰。这有助于提高代码质量和开发流程的效率。

## Text Manipulation

* awk
* sed
* shells
* python
* ruby

## The Power of Plain Text

