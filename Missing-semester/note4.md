# 数据整理

## ssh

数据整理的使用场景：日志的整理

```bash
ssh myserver journalctl
```

filet

```bash
ssh myserver journalctl | grep sshd
```

在服务器端再传输到本机中

```bash
ssh myserver 'journalctl | grep sshd | grep "Disconnected from"' | less
```

less 为我们创建来一个文件分页器，使我们可以通过翻页的方式浏览较长的文本。

```bash
 ssh myserver 'journalctl | grep sshd | grep "Disconnected from"' > ssh.log
 less ssh.log
```  

保存成文件。
**'**的作用就是在服务器端执行指令

## sed

sed 是一个基于文本编辑器ed构建的”流编辑器” 。在 sed 中，您基本上是利用一些简短的命令来修改文件，而不是直接操作文件的内容
最常用的是 s，即替换命令。

```shell
ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed 's/.*Disconnected from //'
```

s/REGEX/SUBSTITUTION/, 其中 REGEX 部分是我们需要使用的正则表达式，而 SUBSTITUTION 是用于替换匹配结果的文本。

sed 的正则表达式有些时候是比较奇怪的，它需要你在这些模式前添加\才能使其具有特殊含义。或者，您也可以添加-E选项来支持这些匹配。

**+** 和 * 在默认情况下是贪婪模式，也就是说，它们会尽可能多的匹配文本。

对于某些正则表达式的实现来说，您可以给 * 或 + 增加一个? 后缀使其变成非贪婪模式，但是很可惜 sed 并不支持该后缀。不过，我们可以切换到 perl 的命令行模式，该模式支持编写这样的正则表达式：

```shell
perl -pe 's/.*?Disconnected from //'
```
某人情况下正则表达式只会执行一次，添加g为执行与匹配次数一致。

sed 还可以非常方便的做一些事情，例如打印匹配后的内容，一次调用中进行多次替换搜索等。

去掉用户名后面的后缀，应该如何操作呢？

```shell
| sed -E 's/.*Disconnected from (invalid |authenticating )?user .* [^ ]+ port [0-9.]+( \[preauth\])?$//'
```
E 添加现代regex语法支持，或者需要使用\

我们实际上希望能够将用户名保留下来。对此，我们可以使用“捕获组（capture groups）”来完成。被圆括号内的正则表达式匹配到的文本，都会被存入一系列以编号区分的捕获组中。
捕获组的内容可以在替换字符串时使用（有些正则表达式的引擎甚至支持替换表达式本身），例如\1、 \2、\3等等，因此可以使用如下命令:

```shell
| sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9.]+( \[preauth\])?$/\2/'
```
在命令行中使用()默认进行了捕获，使用捕获组是"\number"，按照捕获的次序来使用。

sed 还可以做很多各种各样有趣的事情，例如文本注入：(使用 i 命令)，打印特定的行 (使用 p命令)，基于索引选择特定行等等。详情请见man sed!

## sort

```shell
ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9]+( \[preauth\])?$/\2/'
 | sort | uniq -c
```

sort 会对其输入数据进行排序。uniq -c 会把连续出现的行折叠为一行并使用出现次数作为前缀。我们希望按照出现次数排序，过滤出最常出现的用户名：

```shell
ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9.]+( \[preauth\])?$/\2/'
 | sort | uniq -c
 | sort -nk1,1 | tail -n10
```

如果我们希望得到登录次数最少的用户，我们可以使用 head 来代替tail。或者使用sort -r来进行倒序排序。

## wc

统计工具，-l 按照行数记数

## unique

uniq -c 会把连续出现的行折叠为一行并使用出现次数作为前缀。

## awk – 另外一种编辑器

```shell
ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9]+( \[preauth\])?$/\2/'
 | sort | uniq -c
 | sort -nk1,1 | tail -n10
 | awk '{print $2}' | paste -sd,
```

awk 其实是一种编程语言，只不过它碰巧非常善于处理文本。

首先， {print $2} 的作用是什么？ awk 程序接受一个模式串（可选），以及一个代码块，指定当模式匹配时应该做何种操作。默认当模式串即匹配所有行（上面命令中当用法）。 在代码块中，$0 表示整行的内容，$1 到 $n 为一行中的 n 个区域，区域的分割基于 awk 的域分隔符（默认是空格，可以通过-F来修改）。在这个例子中，我们的代码意思是：对于每一行文本，打印其第二个部分，也就是用户名。

我们统计一下所有以c 开头，以 e 结尾，并且仅尝试过一次登录的用户。

```shell
 | awk '$1 == 1 && $2 ~ /^c[^ ]*e$/ { print $2 }' | wc -l
```

让我们好好分析一下。首先，注意这次我们为 awk指定了一个匹配模式串（也就是{...}前面的那部分内容）。该匹配要求文本的第一部分需要等于1（这部分刚好是uniq -c得到的计数值），然后其第二部分必须满足给定的一个正则表达式。**~**: 这是匹配操作符，用于检查一个字段是否匹配正则表达式。代码块中的内容则表示打印用户名。然后我们使用 wc -l 统计输出结果的行数。

不过，既然 awk 是一种编程语言，那么则可以这样：

```shell
BEGIN { rows = 0 }
$1 == 1 && $2 ~ /^c[^ ]*e$/ { rows += $1 }
END { print rows }
```

BEGIN 也是一种模式，它会匹配输入的开头（ END 则匹配结尾）。然后，对每一行第一个部分进行累加，最后将结果输出。事实上，我们完全可以抛弃 grep 和 sed ，因为 awk 就可以解决所有问题。

## 分析数据

### paste 

```shell
 | paste -sd+ | bc -l
```

paste 是一个用于合并文件的命令，通常用于按行或列合并文本文件。在这里，paste 用于合并数据，使用了一些选项和参数：

* -s: 这个选项表示将每行的内容合并成一行。
* -d+: 这个选项表示在合并时使用"+"作为分隔符。

```shell
echo "2*($(data | paste -sd+))" | bc -l
```

### R


您可以通过多种方式获取统计数据。如果已经安装了R语言，st是个不错的选择：

```shell
ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9]+( \[preauth\])?$/\2/'
 | sort | uniq -c
 | awk '{print $1}' | R --slave -e 'x <- scan(file="stdin", quiet=TRUE); summary(x)'
```

summary 可以打印某个向量的统计结果。

如果您希望绘制一些简单的图表， gnuplot 可以帮助到您：

```shell

ssh myserver journalctl
 | grep sshd
 | grep "Disconnected from"
 | sed -E 's/.*Disconnected from (invalid |authenticating )?user (.*) [^ ]+ port [0-9]+( \[preauth\])?$/\2/'
 | sort | uniq -c
 | sort -nk1,1 | tail -n10
 | gnuplot -p -e 'set boxwidth 0.5; plot "-" using 1:xtic(2) with boxes'

```

### 利用数据整理来确定参数

有时候您要利用数据整理技术从一长串列表里找出你所需要安装或移除的东西。我们之前讨论的相关技术配合 xargs 即可实现：

```shell
rustup toolchain list | grep nightly | grep -vE "nightly-x86" | sed 's/-x86.*//' | xargs rustup toolchain uninstall
```
将数据转换成命令行参数。

### 整理二进制数据

虽然到目前为止我们的讨论都是基于文本数据，但对于二进制文件其实同样有用。例如我们可以用 ffmpeg 从相机中捕获一张图片，将其转换成灰度图后通过SSH将压缩后的文件发送到远端服务器，并在那里解压、存档并显示。

```shell
ffmpeg -loglevel panic -i /dev/video0 -frames 1 -f image2 -
 | convert - -colorspace gray -
 | gzip
 | ssh mymachine 'gzip -d | tee copy.jpg | env DISPLAY=:0 feh -'
```

