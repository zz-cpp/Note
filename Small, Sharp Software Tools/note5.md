# Streams of Text

## Redirecting Input and Output with File Descriptors

> : will overwrite if exist
>> : append text to file instead of contents

unix systems will give a integer as descriptor when a process open a file and will store in a table.

0 -> standard input
1 -> standard output
2 -> standard error

not care output from program: /dev/null:

cat can accept input or data from stand input

* cat command.txt(cat open)
* cat < command.txt(shell open)

when use < , the file will open and contents send to cat.

example mysql 

```mysql
mysql -u root -p < tables.sql
```

receive data from stand input.
table.sql contains a bunch of SQL statements to create tables.

```shell
$ cat << 'EOF' > names.txt
> Homer
> Marge
> Bart
> Lisa
> Maggie
> EOF
```

<< create "here document" as a separate file to cat and then redirects the output to standard output for name.txt.
Using cat with a heredoc is a useful hack for creating a file.

herestring: create a premade string as input to program.

bc : command-line calculator

```shell
bc <<< "2 + 2"
```
use herestring by specify with <<<

同样是使用here document，为什么cat << 'EOF' > names.txt 是<< 而bc <<< "2 + 2" 是<<<
>ChatGPT: << is for here documents, used for input redirection with multiple lines, like cat << 'EOF' > names.txt. <<< is for here strings, used to pass a single string as input to a command, like bc <<< "2 + 2".

### Creating Pipelines of Data

One of the main foundations of a Unix-based system is that programs work together like a pipeline;


```shell

$ ls -alh /usr/bin | less
$ ls -alh /usr/bin | head -n 3
s -alh /usr/bin/ | tail -n +3 | head -n 1
```

ls result send to stand output and | will  piped to the less which will see the stream stand input.

print the most common used command.

```shell
history | awk '{c[$2]++}END{for(i in c){print c[i] " " i}}' | sort -rn | head
```

## Splitting Streams with tee

![](ref/note7/20231122122546.png)

Tee can make your screen and a file get the input from a program.

Tee: a T-shaped pipe fitting that splits the water off in two directions.

tee will overwrite the file or append with a.

```shell
history | tee commands.txt
date | tee -a commands.txt
```

Tee will make you redirection that will not be allowed in some place.

```shell
$ sudo history > /var/commands.txt
-bash: /var/commands.txt: Permission denied

history | sudo tee /var/commands.txt
```

redirection is not part of the sudo process.

```shell
$ sudo echo "Line one" >> /var/mylog.txt
bash: /var/mylog.txt: Permission denied
$ echo "Line one" | sudo tee -a /var/mylog.txt
Line one
```

you can also send to /dec/null 

```shell
$ echo "Line two" | sudo tee -a /var/mylog.txt > /dev/null
```

create multiline files with sudo 

```shell
sudo tee -a /var/mylog.txt << EOF > /dev/null
```


easier to create files in home directory and use sudo to move where you want

```shell

$ echo "some text" > ~/myfile.txt
$ sudo mv ~/myfile.txt /var/myfile

```

## Handling Standard Error Streams

use stand error stream: 2>

```shell
$ find /root/ > output.txt 2> errors.txt
$ cat output.txt
/root/
$ cat errors.txt
find: `/root/': Permission denied
```

It makes stand input into output.txt and stand error into errors.txt

```shell
$ find /root/ > log.txt 2>&1
$ cat log.txt
/root/
find: `/root/': Permission denied
```

2>&1: 将标准错误流重定向到标准输出流。

The characters 2>&1 tell the shell to take file descriptor 2, the error stream,and send it to file descriptor 1, the standard output stream.

如果你想将标准错误重定向到与标准输出相同的地方，你需要使用 2>&1 表示将标准错误（file descriptor 2）重定向到与标准输出（file descriptor 1）相同的地方。这是因为 1 是标准输出的文件描述符。

在shell中，& 后面可以跟一个文件描述符，用于将输出或错误重定向到相应的文件描述符。对于标准输出（stdout）和标准错误（stderr），分别对应文件描述符 1 和 2。

&1: reference the target(1: stand output)

a concise way to redirection to  error and stand output(Bash)

```Bash
$ find /root/ &> log.txt
$ cat log.txt
/root/
find: `/root/': Permission denied
```

send the error from programs to dash

```Bash
$ find /root/ 2> /dev/null
/root/
```

```Bash
$ find /var/log -mmin -30 | less 2>&1
```

管道 |：

| 用于将一个命令的标准输出传递给另一个命令的标准输入。


重定向 >：
> 用于将命令的标准输出重定向到文件，或者创建一个新文件并将输出写入其中。


## Filtering with grep

<< 是 shell 中的一个特殊运算符，用于创建一个称为 "here document" 的文本块。Here document 允许你在脚本或命令行中直接插入多行文本，而不必将文本存储在外部文件中。

```Bash
$ cat << 'EOF' > words.txt
> blue
> apple
> candy
> hand
> fork
> EOF
```

command << delimiter

delimiter : as a end symbol when meet will end up the content input.

Surrounding the search term in quotes isn’t always necessary, but it’s a good habit to get into because you can run into some strange edge cases with special characters if you don’t.

```Bash
grep 'and' -v words.txt
blue
apple
fork
```

-v will remove containing search symbol.

grep can process file or output from other programs.

```Bash
$ history | grep 'ls'
...
471 ls
479 ls
484 ls
500 history | grep 'ls'

$ history | grep 'ls' | grep -v 'grep'
...
471 ls
479 ls
484 ls

$ history | grep 'ls' | grep -v 'grep' | less
```

```Bash
$ grep 'blue' words.txt words2.txt
words.txt:blue
words2.txt:blue car

```

```Bash
$ grep 'blue' words.txt words2.txt
words.txt:blue
words2.txt:blue car
```

only show the match result along name.

```Bash
$ grep 'candy' -A 2 -B 2 words*
words2.txt-blue car
words2.txt-apple pie
words2.txt:candy bar
words2.txt-hand in hand
words2.txt-fork in the road
--
words.txt-blue
words.txt-apple
words.txt:candy
words.txt-hand
words.txt-fork
```

-A 2 意味着显示匹配行及其后两行（after）。
-B 2 意味着显示匹配行及其前两行（before）。


```Bash
grep 'candy' -C 2 words*
```

-C 2 展示项下文两项。

```Bash
$ grep 'candy' -C 2 -n words*
words2.txt-1-blue car
words2.txt-2-apple pie
words2.txt:3:candy bar
words2.txt-4-hand in hand
words2.txt-5-fork in the road
--
words.txt-1-blue
words.txt-2-apple
words.txt:3:candy
words.txt-4-hand
words.txt-5-fork

```

-n 添加行号被发现的相匹配的地方。

grep + regex


```Bash
$ grep 'b' words*
words.txt:blue
words2.txt:blue car
words2.txt:candy bar

$ grep '^b' words*
words.txt:blue
words2.txt:blue car

$ grep 'e$' words*
words.txt:blue
words.txt:apple
words2.txt:apple pie

$ grep -E 'blue|apple' words*
words.txt:blue
words.txt:apple
words2.txt:blue car
words2.txt:apple pie

```

-E make you extend the regular expressions: |, ?, +, {, (, and )

```Bash
$ grep -E 'a(n|r)' words*
words2.txt:blue car
words2.txt:candy bar
words2.txt:hand in hand
words.txt:candy
words.txt:hand

```

grep is a general purpose text search tool, and while there are some other options out there like ack2 or ripgrep,3 which have additional features aimed at working with source code, you should be comfortable using grep since it’s universally available.

## Removing Characters from Output with cut

cut

```Bash
$ echo 'hello world' | cut -c 2-4
ell

```
-c cut at characters rather than bytes

```Bash
$ echo 'hello world' | cut -c -5
hello

$ echo 'hello world' | cut -c 7-
world
```

-c -5 : cut out characters end in position index 5.
-c 7- : cut out characters  begin in position index 7.

```bash
history
5 ls
6 cd Documents/
7 cd
8 history
..

$ history | cut -c 8-
ls
cd Documents/
cd
history

$ history | cut -c 8- > commands.txt
```


```Bash

$ echo -e "hello\tworld" | cut -f 1
hello
$ echo -e "hello\tworld" | cut -f 2
world

```

cut at file
echo -e print string .
the file with tables using \t

```BAsh
$ cat << 'EOF' > contacts.txt
> Homer,Simpson,939-555-4795,742 Evergreen Terrace
> EOF

cut -d ',' -f 3 contacts.txt
939-555-4795
```

specify delimiter like comma ... .

## Sorting Output

sort

```bash

$ cat words.txt
blue
apple
candy
hand
fork

$ sort words.txt
apple
blue
candy
fork
hand

$ sort -r words.txt
hand
fork
candy
blue
apple

$ sort -R words.txt
apple
blue
candy
fork
hand

```

-R randomly

## Editing Streams with sed

If you need to modify text in a pipeline or in a file.
sed is short for stream editor.


```shell
$ echo "Hello World" | sed -e 's/Hello/Goodbye/'
Goodbye World

```

-e: specify expression
s/Hello/Goodbye : use Goodbye to substitute

```shell
$ cat << 'EOF' > document.md
> This is *very* important text with *very* important words.
>
> * These words are not important.
> * Neither are these.
>
> You can *always* tell very important text because it's in italics.
> EOF


$ sed -e 's/very/really/' document.md
➤ This is *really* important text with *very* important words.
* These words are not important.
* Neither are these.


$ sed -e 's/very/really/g' document.md
➤ This is *really* important text with *really* important words.
* These words are not important.
* Neither are these.

```

sed doesn’t modify the original file. Let’s look at how to save changes.

for saving modify:

```shell

$sed -e 's/very/really/g' document.md > new_document.md

$sed -i -e 's/really/very/g' new_document.md

```

1. redirection to a file
2. use -i to represent switch(better to run two times for check and then switch using modifying)

### Editing Specific Lines

```markdown

This is *very* important text with *very* important words.
* These words are not important.
* Neither are these.
You can *always* tell very important text because it's in italics

Try using a basic substitution to replace them:

$ sed -e 's/*/_/g' document.md
This is _very_ important text with _very_ important words.

➤ _ These words are not important.
➤ _ Neither are these.

You can _always_ tell very important text because it's in italics.
```

```shell
$ cat << 'EOF' > urls.txt
> http://example.com
> http://facebook.com
> http://twitter.com
> https://pragprog.com
> EOF

$ sed -e 's/http/https/' urls.txt
https://example.com
https://facebook.com
https://twitter.com
➤ httpss://pragprog.com

$ sed -e 's/http:/https:/' urls.txt
https://example.com
https://facebook.com
https://twitter.com
https://pragprog.com

$ sed -e '/facebook/s/^/#/' urls.txt
http://example.com
➤ #http://facebook.com
http://twitter.com
https://pragprog.com

$ sed -e '/facebook/s/^/#/' urls.txt > commented_urls.txt

$ sed -e '/facebook/s/^#//' commented_urls.txt
http://example.com
➤ http://facebook.com
http://twitter.com
https://pragprog.com

```

s/^#//：这是一个替换操作，它使用正则表达式将行开头的#符号替换为空（即删除#符号）。
s/：指明这是一个替换操作。
^#：是一个正则表达式模式，表示行的开头（^）有一个 # 符号。
/：分隔符，标志着模式和替换文本的结束以及可能的标志的开始。

you can target the which line to modify in example 4


```shell
$ sed -e '/\*.*\*/s/\*/_/g' document.md
➤ This is _very_ important text with _very_ important words.
* These words are not important.
* Neither are these.
➤ You can _always_ tell very important text because it's in italics.
```

### Operating on Lines by Number

```shell
$ sed -e '1 {s/^/#/}' urls.txt
➤ #http://example.com
http://facebook.com
http://twitter.com
https://pragprog.com

$ sed -e '2,4 {s/^/#/}' urls.txt
http://example.com
➤ #http://facebook.com
➤ #http://twitter.com
➤ #https://pragprog.com

$ sed -e '1i\Bookmarks' urls.txt
➤ Bookmarks
http://example.com
http://facebook.com
http://twitter.com
https://pragprog.com

$ sed -e '$a\http://google.com' urls.txt
http://example.com
http://facebook.com
http://twitter.com
https://pragprog.com
➤ http://google.com

$ sed -e '1i\Bookmarks' -e '$a\http://google.com' urls.txt
➤ Bookmarks
http://example.com
http://facebook.com
http://twitter.com
https://pragprog.com
➤ http://google.com

$ sed -e '1c\https://github.com'
https://github.com
http://facebook.com
http://twitter.com
https://pragprog.com
urls.txt

```

1i: insert in 1 line
a : append a line to the end of the file
1d: delete the 1 line
c : change

### Replacing Text with Content from Another File

```shell

$ cat << 'EOF' > useful_links.md
> Here are a few interesting links:
> LINKS
> I hope you find them useful.
> EOF

$ sed -e '/LINKS/r urls.txt' -e '/LINKS/d' useful_links.md
Here are a few interesting links:
http://example.com
http://facebook.com
http://twitter.com
https://pragprog.com
I hope you find them useful.

```

## Advanced Processing with awk

extract fields in a file, calculate totals, and even change the order of the output of a file. scripting language

```shell
$ cat << 'EOF' > population.txt
> State,Population,Reps
> Alabama,4802982,7
> California,37341989,53
> Florida,18900773,27
> Hawaii,1366862,2
> Illinois,12864380,18
> New York,19421055,27
> South Dakota,819761,1
> Wisconsin,5698230,8
> Wyoming,568300,1
> EOF

$ awk '/Wisconsin/' population.txt
Wisconsin,5698230,8

$ awk -F ',' '/Wisconsin/ {print $1}' population.txt
Wisconsin

$ awk -F ',' '/Wisconsin/ {print $2}' population.txt
5698230

$ awk -F ',' '/Wisconsin/ { printf("%'"'"'d\n", $2) }' population.txt
5,698,230

$ awk -F ',' '{ print $1 $2 }' population.txt
StatePopulation
Alabama4802982
California37341989
Florida18900773
Hawaii1366862
Illinois12864380
New York19421055
South Dakota819761
Wisconsin5698230
Wyoming568300

$ awk -F ',' '{ print $1, $2 }' population.txt
State Population
Alabama 4802982
California 37341989
...
Wyoming 568300

$ awk -F ',' '{ print $1 " - " $2 }' population.txt
State - Population
Alabama - 4802982
California - 37341989
...
Wyoming - 568300

$ awk -F ',' '{ print $1 "\t" $2 "\t" $3 }' population.txt
State   Population  Reps
Alabama 4802982 7
California 37341989 53
Florida 18900773 27
Hawaii 1366862 2
Illinois 12864380 18
New York 19421055 27
South Dakota 819761 1
Wisconsin 5698230 8
Wyoming 568300 1

$ column -t -s, population.txt
State   Population Reps
Alabama   4802982   7
California   37341989   53
...
Wyoming   568300   1

$ awk -F ',' 'NR>1 { print $1 " - " $2 }' population.txt
Alabama - 4802982
California - 37341989
...
Wyoming - 568300

$ awk -F ',' '{ print $2, $1, $3 }' population.txt
Population State Reps
4802982 Alabama 7
37341989 California 53
18900773 Florida 27
1366862 Hawaii 2
12864380 Illinois 18
19421055 New York 27
819761 South Dakota 1
5698230 Wisconsin 8
568300 Wyoming 1

```

-F : specify the field delimiter
{print $1} : tells awk to print the first field

-t : output in a table
-s : specifies what the original file uses for delimiter

NR>1 : if the row number > 1 output 1