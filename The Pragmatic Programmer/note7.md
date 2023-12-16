# while you coding

## Test to Code

### Build End-to-End, Not Top-Down or Bottom Up

coding 是一个渐进的过程。因为你并不会在一开始就知道你要做什么，至少大部分时候是这样的。

More complex chips and systems may have a full
Built-In Self Test (BIST) feature that runs some base-level diagnostics internally, or a Test Access Mechanism (TAM) that
provides a test harness that allows the external environment to provide stimuli and collect responses from the chip.
在软件中也可以借鉴上述的做法，用组件的思想构建，使得代码自带testbility。

### Unit test

## Design by Contrtact

### Design to Test

At the end of the debugging session, you need to formalize this
ad hoc test.

### BUILD A TEST WINDOW

总而言之就是在代码中放置类似log的东西，可以看见代码在真实的数据中的状态。
Log messages should be in a regular, consistent format.
以便之后自动化处理

hotkey,magic URL类似针对调试人员所涉及的，通常不会展示给用户。要么是特殊的组合键或者url，使得调试人员能够得到需要调试的数据。

Should you write tests? Yes. But after you’ve been doing it for 30 years, feel free to
experiment a little to see where the benefit lies for you.

### Test Your Software, or Your Users Will


## Property-Based Testing

TDD的一个巨大好处就是你能够在写代码的时候进行整理(让他解耦，让它减少特殊代码...)

property = invariant + contracts

property-based testing framework.

标记出invariant，然后测试框架会自动生成测试，来检测invariant在调用方法之后会不会发生改变。

Our suggestion is that when a property-based test fails, find out
what parameters it was passing to the test function, and then
use those values to create a separate, regular, unit test.

property-based tests 利用invariant和contrast,他基本上确保你的程序一定不能够处于某一种状态。

when a property-based test fails, find out
what parameters it was passing to the test function, and then
use those values to create a separate, regular, unit test.
