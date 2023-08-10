# Arrays

## make

### Compile
#### preprocess

```c
#include <stdio.h>
```

实际上是在检索,"/usr/include",查找文件< >，然后复制粘贴文件的内容。
这样包含的是文件的全部内容。

#### link

### Decompile
Decompile无法得到函数名称，变量名称，因为底层并不会关心你具体是用什么名字；同时，有时候底层的机器玛可能是相同的（比如loop，while，for表示相同的逻辑，mash code可能是一样的）。


### Debuging

1. printf
2. debuger
3. rubber duck "Duck debuger"