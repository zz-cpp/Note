# Reading 15: Promises

## Promises

他表示在未来会返回结果的行为，是一种承诺。
使用他可以courrent 执行任务。
有三种状态，pending,fulfilled,rejected。
pending：是初始和中间的状态，表示计算的开始，正在进行当中。
fulfilled：是计算完成。
rejected：是计算失败。

## Await

一种内置的操作，他会等待promise完成，然后提取出他的返回值。如果返回值是erro，则会抛出异常。

如果使用类型为void的promise，那么他并不会返回任何值，他的作用就只是执行函数的side-effect。

如果不使用await去等待某一个promise函数，那么主线的执行就不会停下。

## Asynchronous functions

Asynchronous functions是在这个function完成之前，就将控制返回给调用他的函数，而不用等到他执行完成。而通常的函数则必须要等到执行完成，通常将这样的函数成为synchronous functions

return promise 是实现Asychronous的一种方式，另一种常见的方式是callback function——调用者将modual传递给需要调用的函数

in modern TS/JS programming, using promises, an asynchronous function can be declared using the async keyword, and must have return type Promise
**async：表示当前函数是Asynchronous function，在函数body种依然可以retuen**


## Concurrency model



