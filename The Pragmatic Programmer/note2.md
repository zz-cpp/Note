# A Pragmatic Approach

## The Essence of Good Design

### Good Design Is Easier to Change Than Bad Design

ETC: easier to change

以下所有的设计原则都是ETC的思想的子集

* decoupling
* single responsibility
* naming

### ETC IS A VALUE, NOT A RULE

Values are things that help you make decisions: should I do this, or that? When it comes to thinking about software, ETC is a
guide, helping you choose between paths.

ETC 是一个有意识的思考。

ECT的日常：

1. 当你不确定那个部分可以使用ETC时，尽可能地将代码改写成可替代。
2. 记录你当前的选择以及对未来的预期。
3. 在写测试，保存文件，修复bug时，问自己：“你刚才所作的是否让你的代码更加ETC。

## DRY—The Evils of Duplication

maintenance 是开发中的一部分而不是单独的一个活动。

Every piece of knowledge must have a single, unambiguous,
authoritative representation within a system.

### DRY—Don’t Repeat Yourself

### DRY IS MORE THAN CODE

DRY 不是仅仅不拷贝和复制代码。

当你的代码出现：

1. 修改一次就要修改许多地方(documentation,database schema...)
2. 你必须修改很对次

那么他就不DRY

example: 

```Ruby
def print_balance(account)
    printf "Debits: %10.2f\n", account.debits
    printf "Credits: %10.2f\n", account.credits
    if account.fees < 0
        printf "Fees: %10.2f-\n", -account.fees
    else
        printf "Fees: %10.2f\n", account.fees
    end 
    printf "———-\n"
    if account.balance < 0
        printf "Balance: %10.2f-\n", -account.balance
    else
        printf "Balance: %10.2f\n", account.balance
    end
end
```

第一个重复：对于负数的处理的逻辑，添加一个函数。

```Ruby
def format_amount(value)
 result = sprintf("%10.2f", value.abs)
 if value < 0
  result + "-"
 else
  result + " "
 end
end

def print_balance(account)
 printf "Debits: %10.2f\n", account.debits
 printf "Credits: %10.2f\n", account.credits
 printf "Fees: %s\n", format_amount(account.fees)
 printf "———-\n"
 printf "Balance: %s\n", format_amount(account.balance)
end

```

第二个重复：打印函数的宽度设定

```Ruby
def format_amount(value)
 result = sprintf("%10.2f", value.abs)
 if value < 0
  result + "-"
 else
  result + " "
 end
end

def print_balance(account)
 printf "Debits: %s\n", format_amount(account.debits)
 printf "Credits: %s\n", format_amount(account.credits)
 printf "Fees: %s\n", format_amount(account.fees)
 printf "———-\n"
 printf "Balance: %s\n", format_amount(account.balance)
end

```

这依然有问题，如果客户端此时想要一个打印一个额外的label-amount.

```Ruby

def format_amount(value)
 result = sprintf("%10.2f", value.abs)
 if value < 0
  result + "-"
 else
  result + " "
 end
end

def print_line(label, value)
 printf "%-9s%s\n", label, value
end

def report_line(label, amount)
 print_line(label + ":", format_amount(amount))
end

def print_balance(account)
 report_line("Debits", account.debits)
 report_line("Credits", account.credits)
 report_line("Fees", account.fees)
 print_line("","———-")
 report_line("Balance", account.balance)
end
```

### Not All Code Duplication Is Knowledge Duplication

```Ruby
def validate_age(value):
 validate_type(value, :integer)
 validate_min_integer(value, 0)
def validate_quantity(value):
 validate_type(value, :integer)
 validate_min_integer(value, 0)
```

从代码上看，这两个代码是重复的，他们的逻辑相同，但是他们验证的是两个分别的量，所以并不是重复的。

### DUPLICATION IN DOCUMENTATION

```Ruby
# Calculate the fees for this account.
#
# * Each returned check costs $20
# * If the account is in overdraft for more than 3 days,
# charge $10 for each day
# * If the average account balance is greater that $2,000
# reduce the fees by 50%
def fees(a)
 f=0
 if a.returned_check_count > 0
  f += 20 * a.returned_check_count
 end
 if a.overdraft_days > 3
  f += 10*a.overdraft_days
 end
 if a.average_balance > 2_000
  f /= 2
 end
 f
end
```

这段代码假设了两次如果account的情况：code and document。这意味着，一旦你修改了逻辑，比如追加了一种新的情况，你就必须要修改两次。把这样的内容添加到代码中，如果其他人想要知道细节，就让他们看源码。

```Ruby
def calculate_account_fees(account)
 fees = 20 * account.returned_check_count
 fees += 10 * account.overdraft_days if account.overdraft_days > 3
 fees /= 2
 if account.average_balance > 2_000
 fees
end
```

### DRY Violations in Data

```Ruby
class Line {
Point start;
Point end;
double length;
};
```

这段代码违背了DRY，length 的定义来源于 start and end的值，改变了start或者end的值，意味着我们也需要修改length的值。

```Ruby
class Line {
Point start;
Point end;
double length() { return start.distanceTo(end); }
};

```

现在将其修改成是动态的，一个方法。

但是在实际开发中，对于比较复杂的过程，我们会使用缓存来存储结果，以免多次重复操作。这个技巧是impact。

```Ruby
class Line {
private double length;
private Point start;
private Point end;
public Line(Point start, Point end) {
this.start = start;
this.end = end;
calculateLength();
}
// public
void setStart(Point p) { this.start = p; calculateLength(); }
void setEnd(Point p) { this.end = p; calculateLength(); }
Point getStart()
Point getEnd()
double getLength()
{ return start; }
{ return end; }
{ return length; }
private void calculateLength() {
this.length = start.distanceTo(end);
}
};

```

对一个模块暴露出的数据结构，在代码里使用时，既可能的利用accessor function 来访问它，这样在以后能够更好地修改。

### REPRESENTATIONAL DUPLICATION

每当你与外界的代码交互都会产生一定的DRY违背。
因为外部和你的代码都需要了解接口的信息，两边任何一边对接口的修改，都可能会造成问题的出现。

总的来说这种问题无法避免，但是有些策略可以减轻他的影响。

#### Duplication Across Internal APIs

#### Duplication with Data Sources

#### INTERDEVELOPER DUPLICATION

## Orthogonality

来自数学的一个概念，即两个不同部分的代码不会相互影响(数据库代码与用户接口不会影响，改变任何一个都是可行的)。

### Eliminate Effects Between Unrelated Things


两个 self-contained components能够获得的好处

1. 组建的改变是区域化的，可以很方便测试，编码，修改，而不需要在意一些其他的一直存在的变化
2. 组件能够很方便的进行组合。
3. 提高生产的效率，因为便于组合，你能够多次复用。因为它们之间的效果不会相互重叠。

Reduce Risk

1. 更容易测试
2. 提高系统的强度，因为所有的改变或者修复都指挥控制在特定的区域之中。
3. 减少与其他组建之间的耦合，因为只要接口不便，你可以随意更换第三方的组建。

### DESIGN

这里的一些术语与orthogonal 实际上是一个意思的：modular,component-based, and layered

Decorator patteren : adding functionality to things without changing.

#### coding

Keep your code decoupled
Avoid global data
 Singleton pattern
Avoid similar functions

## Reversibility

## Tracer Bullets

Tracer Bullets 是为了获取开发的实施反馈。他的核心思想是，快速开发出一些代表系统的关键功能，来获取实施反馈，而不是只依赖文档。

Tracer code 并不是一次性的代码，他是完整的和具有高质量的。Tracer Code 不同于原型（Prototyping），因为它不仅仅是用于探索系统特定方面的快速原型，而是创建了一个完整的、具有质量的系统骨架。因此，Tracer Code 是一个更加持久的解决方案。

## Prototypes and Post-it Notes

## Domain Languages

## Estimating