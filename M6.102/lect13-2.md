# Parser generator

<div data-outline="parser_generators">

<p id="@parser_generator_good"><a class="jump" href="#@parser_generator_good"></a>A <mark data-structure-text="parser generator" id="^parser_generator"><em>parser generator</em></mark> is a good tool that you should add to your toolbox.  A parser generator takes a grammar as input and automatically generates a <mark data-structure-text="parser" id="^parser"><em>parser</em></mark>, which takes a sequence of characters and tries to match the sequence against the grammar.</p>

<p id="@parser_typically_produces"><a class="jump" href="#@parser_typically_produces"></a>The parser typically produces a <mark data-structure-text="parse tree" id="^parse_tree_2"><em>parse tree</em></mark>, which shows how grammar productions are expanded into a sentence that matches the character sequence.  The root of the parse tree is the root nonterminal of the grammar.  Each node of the parse tree expands into one production of the grammar.  We’ll see how a parse tree actually looks later in this reading.</p>

<p id="@final_step_parsing"><a class="jump" href="#@final_step_parsing"></a>The final step of parsing is to do something useful with this parse tree.  We are going to translate it into a value of a recursive data type.  Recursive abstract data types are often used to represent expressions in a language, like HTML, Markdown, TypeScript, or algebraic expressions (the symbolic language of algebra).  A recursive abstract data type that represents a language expression is called an <em>abstract syntax tree</em> (AST).</p>

<p id="@6-102_going_use"><a class="jump" href="#@6-102_going_use"></a>In 6.102, we are going to use <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/">ParserLib</a>, a parser generator for TypeScript developed by the course staff. The parser generator is similar in spirit to more widely used parser generators like <a href="http://www.antlr.org/">Antlr</a>, but it has a simpler interface and is generally easier to use.</p>

<h3 id="a_parserlib_grammar"><a class="jump" href="#a_parserlib_grammar"></a>A ParserLib grammar</h3><div data-outline="a_parserlib_grammar">

<div class="handout-solo alert alert-warning"><p id="@documentation_parserlib_can"><a class="jump" href="#@documentation_parserlib_can"></a><a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/" class="alert-link">Documentation for ParserLib</a> can be found online.</p></div>

<p id="@here_our_html"><a class="jump" href="#@here_our_html"></a>Here is what <a href="#example_markdown_and_html">our HTML grammar</a> looks like as a ParserLib grammar:</p>

<div><pre id="@html_italic_normal_italic_i_html"><a class="jump" href="#@html_italic_normal_italic_i_html"></a><code class="language-parserlib hljs">html <span class="hljs-keyword">::=</span> ( italic <span class="hljs-keyword">|</span> normal ) <span class="hljs-keyword">*</span> ;
italic <span class="hljs-keyword">::=</span> <span class="hljs-string">'&lt;i&gt;'</span> html <span class="hljs-string">'&lt;/i&gt;'</span> ;
normal <span class="hljs-keyword">::=</span> text ; 
text <span class="hljs-keyword">::=</span> <span class="hljs-string">[^&lt;&gt;]</span><span class="hljs-keyword">+</span> ;  <span class="hljs-comment">/* represents a string of one or more characters that are not &lt; or &gt; */</span></code></pre></div>

<p id="@break_down"><a class="jump" href="#@break_down"></a>Let’s break it down. </p>

<p id="@each_parserlib_rule"><a class="jump" href="#@each_parserlib_rule"></a>Each ParserLib rule consists of a name, followed by a <code>::=</code>, followed by its definition, terminated by a semicolon. The ParserLib grammar can also include TypeScript-style comments, both single line and multiline.</p>

<p id="@convention_use_lowercase"><a class="jump" href="#@convention_use_lowercase"></a>By convention, we use lowercase for nonterminals: <code>html</code>, <code>normal</code>, <code>italic</code>, <code>text</code>. 
The ParserLib library is case-insensitive with respect to nonterminal names; it canonicalizes names to all-lowercase, so even if you don’t write all your names into lowercase, you will see them as lowercase when you print your grammar.</p>

<p id="@terminals_quoted_strings"><a class="jump" href="#@terminals_quoted_strings"></a>Terminals are quoted strings, like <code>'&lt;i&gt;'</code>, or more generally regular expressions, like <code>[^&lt;&gt;]+</code>.
Unlike normal regular expressions, however, ParserLib syntax requires literal characters to be either quoted or enclosed in character-class brackets.
Thus the regex <code>(alpha|beta|[c-z])*</code> is written as <code>('alpha'|'beta'|[c-z])*</code> in ParserLib syntax.</p>

<pre><code class="language-parserlib hljs">html <span class="hljs-keyword">::=</span> ( italic <span class="hljs-keyword">|</span> normal ) <span class="hljs-keyword">*</span> ;</code></pre>

<p id="@rule_shows_parserlib"><a class="jump" href="#@rule_shows_parserlib"></a>This rule shows that ParserLib rules can have the alternation operator <code>|</code>,  repetition operators like <code>*</code> (and also <code>+</code> and <code>?</code>, even though they’re not shown in this rule), and parentheses for grouping, in the same way we’ve been using in the <a href="#grammar_operators">grammars section</a>.</p>

<p id="@whitespace_grammar_not"><a class="jump" href="#@whitespace_grammar_not"></a>Whitespace in a grammar is not significant (outside of quoted strings or <code>[...]</code> character classes).
Here the operators have been surrounded by spaces to make them more visible.
Writing the rule as <code>html::=(italic|normal)*;</code> would have the same effect, just with less readability.</p>

<p id="@html_nonterminal_also"><a class="jump" href="#@html_nonterminal_also"></a>The <code>html</code> nonterminal also happens to be the <em>root</em> of this grammar (also called starting symbol).
The root is the nonterminal that the whole input needs to match.
It’s good practice to put the rule for the root nonterminal first in the grammar, so that a human reader can take a top-down view, but it isn’t essential.
When we create a parser from the grammar, we tell ParserLib which nonterminal the parse should use as the root.</p>

<pre id="@italic_i_html"><a class="jump" href="#@italic_i_html"></a><code class="language-parserlib hljs">italic <span class="hljs-keyword">::=</span> <span class="hljs-string">'&lt;i&gt;'</span> html <span class="hljs-string">'&lt;/i&gt;'</span> ;
normal <span class="hljs-keyword">::=</span> text ;
text <span class="hljs-keyword">::=</span> <span class="hljs-string">[^&lt;&gt;]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@note_text_production"><a class="jump" href="#@note_text_production"></a>Note that the <code>text</code> production uses the inverted character class <code>[^&lt;&gt;]</code>, discussed in the <a href="#more_grammar_operators">grammars section</a>, to represent any character except <code>&lt;</code> and <code>&gt;</code>.</p>

</div><h3 id="whitespace"><a class="jump" href="#whitespace"></a>Whitespace</h3><div data-outline="whitespace">

<p id="@consider_grammar_shown"><a class="jump" href="#@consider_grammar_shown"></a>Consider the grammar shown below.</p>

<pre id="@expr_sum_sum"><a class="jump" href="#@expr_sum_sum"></a><code class="language-parserlib hljs">expr <span class="hljs-keyword">::=</span> sum ;
sum <span class="hljs-keyword">::=</span> primary (<span class="hljs-string">'+'</span> primary)<span class="hljs-keyword">*</span> ;
primary <span class="hljs-keyword">::=</span> constant <span class="hljs-keyword">|</span> <span class="hljs-string">'('</span> sum <span class="hljs-string">')'</span> ;
constant <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@grammar_accept_expression"><a class="jump" href="#@grammar_accept_expression"></a>This grammar will accept an expression like <code>42+2+5</code>, but will reject a similar expression that has any spaces between the constants and the <code>+</code> signs. We could modify the grammar to allow whitespace around the plus sign by modifying the production rule for <code>sum</code> like this:</p>

<pre id="@sum_primary_whitespace"><a class="jump" href="#@sum_primary_whitespace"></a><code class="language-parserlib hljs">sum <span class="hljs-keyword">::=</span> primary (whitespace<span class="hljs-keyword">*</span> <span class="hljs-string">'+'</span> whitespace<span class="hljs-keyword">*</span> primary)<span class="hljs-keyword">*</span> ;
whitespace <span class="hljs-keyword">::=</span> <span class="hljs-string">[ \t\r\n]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@however_can_become"><a class="jump" href="#@however_can_become"></a>However, this can become cumbersome very quickly once the grammar becomes more complicated. ParserLib allows a shorthand to indicate that certain kinds of characters should be skipped. </p>

<pre><code class="language-parserlib hljs"><span class="hljs-comment">// the IntegerExpression grammar</span>
<span class="hljs-keyword">@skip</span> whitespace {
    expr <span class="hljs-keyword">::=</span> sum ;
    sum <span class="hljs-keyword">::=</span> primary (<span class="hljs-string">'+'</span> primary)<span class="hljs-keyword">*</span> ;
    primary <span class="hljs-keyword">::=</span> constant <span class="hljs-keyword">|</span> <span class="hljs-string">'('</span> sum <span class="hljs-string">')'</span> ;
}
whitespace <span class="hljs-keyword">::=</span> <span class="hljs-string">[ \t\r\n]</span><span class="hljs-keyword">+</span> ;
constant <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@skip_whitespace_notation"><a class="jump" href="#@skip_whitespace_notation"></a>The <code>@skip whitespace</code> notation indicates that zero or more matches to the <code>whitespace</code> nonterminal should be automatically ignored, before and after each terminal, nonterminal, and character class on the righthand side of <code>expr</code>, <code>sum</code>, and <code>primary</code>.
So from the point of view of grammar matching, these three rules effectively become:</p>

<pre class="pull-margin" id="@expr_whitespace_sum"><a class="jump" href="#@expr_whitespace_sum"></a><code class="language-parserlib hljs">expr <span class="hljs-keyword">::=</span> whitespace<span class="hljs-keyword">*</span> sum whitespace<span class="hljs-keyword">*</span> ;
sum <span class="hljs-keyword">::=</span> whitespace<span class="hljs-keyword">*</span> primary whitespace<span class="hljs-keyword">*</span> (whitespace<span class="hljs-keyword">*</span> <span class="hljs-string">'+'</span> whitespace<span class="hljs-keyword">*</span> primary whitespace<span class="hljs-keyword">*</span>)<span class="hljs-keyword">*</span> whitespace<span class="hljs-keyword">*</span> ;
primary <span class="hljs-keyword">::=</span> whitespace<span class="hljs-keyword">*</span> constant whitespace<span class="hljs-keyword">*</span> <span class="hljs-keyword">|</span> whitespace<span class="hljs-keyword">*</span> <span class="hljs-string">'('</span>  whitespace<span class="hljs-keyword">*</span> sum  whitespace<span class="hljs-keyword">*</span> <span class="hljs-string">')'</span> whitespace<span class="hljs-keyword">*</span> ;</code></pre>

<p id="@several_things_important"><a class="jump" href="#@several_things_important"></a>Several things are important to note about <code>@skip</code>.
First, there is nothing special about <code>whitespace</code>. The <code>@skip</code> directive works with any nonterminal defined in the grammar – so you could <code>@skip punctuation</code> or <code>@skip spacesAndComments</code> instead, by defining appropriate rules for those nonterminals.</p>

<p id="@second_note_definition"><a class="jump" href="#@second_note_definition"></a>Second, note how the definition of <code>constant</code> was intentionally placed <em>outside</em> the <code>@skip</code> block.
This is because we want to accept expressions with extra space <em>around</em> a constant, like <code>42  +  2</code>, but we want to reject spaces <em>within</em> constants, like <code>4   2 + 2</code>.
Putting the <code>constant</code> rule inside the <code>@skip</code> block would cause it to effectively become <code>constant ::= (whitespace* [0-9] whitespace*)+</code>, so it would accept constants with spaces inside them.
Putting the rule for <code>primary</code> inside the <code>@skip</code> block – so that its use of <code>constant</code> can be surrounded by whitespace – but keeping the rule for <code>constant</code> outside, provides the desired behavior.</p>

>当构建一个解析器来解析特定类型的语法时，有时候我们想要在解析过程中自动忽略掉某些特定的字符，例如空格、注释、或者其他不影响语法结构的标记。在这种情况下，@skip 是一个很有用的工具，它允许我们在语法定义中标记哪些部分应该被自动忽略。   
具体来说，@skip 指令的作用是告诉解析器在匹配过程中忽略指定的非终结符，即不会创建对应的语法树节点。这通常用于那些我们在语法解析过程中不关心的部分，例如空白字符、注释等。
举个例子，假设我们有一个用于解析数学表达式的语法，其中包括数字、操作符和括号。但是我们希望在解析过程中自动忽略掉表达式中的空格，以便更灵活地处理输入。这时，我们可以在适当的位置使用 @skip whitespace，这将告诉解析器在匹配数字、操作符和括号时忽略掉空格字符。
使用 @skip 可以简化复杂的语法定义，使其更易于阅读和维护。它让我们能够专注于真正影响语法结构的部分，而将不必要的标记自动过滤掉。

<div class="reading-exercises exercises panel-group converted" id="ex-parser_generators"><h4 class="text-danger">reading exercises</h4><div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators-skip" data-target="#ex-parser_generators-skip" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators-skip"></a><span class="panel-title">@skip</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators-skip" data-outline="skip" data-ex-id="whitespace/skip" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@consider_simple_grammar"><a class="jump" href="#@consider_simple_grammar"></a>Consider this simple grammar, which has <code>semester</code> as its root nonterminal:</p>

<pre><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces {
  semester <span class="hljs-keyword">::=</span> season year ;
  season <span class="hljs-keyword">::=</span> <span class="hljs-string">'Fall'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'Spring'</span> ;
  year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span> <span class="hljs-string">[0-9]</span> ;
}
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@which_following_strings"><a class="jump" href="#@which_following_strings"></a>Which of the following strings are matched by this grammar (where <code>␣</code> indicates a space in the string)?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="Fall15"><label for="md_converted_choice_6_0"><input type="checkbox" id="md_converted_choice_6_0"><code>Fall15</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="␣␣␣␣Spring␣␣␣␣␣␣23"><label for="md_converted_choice_6_1"><input type="checkbox" id="md_converted_choice_6_1"><code>␣␣␣␣Spring␣␣␣␣␣␣23</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Spring␣9␣9"><label for="md_converted_choice_6_2"><input type="checkbox" id="md_converted_choice_6_2"><code>Spring␣9␣9</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="F␣a␣l␣l␣06"><label for="md_converted_choice_6_3"><input type="checkbox" id="md_converted_choice_6_3"><code>F␣a␣l␣l␣06</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators-skip_less" data-target="#ex-parser_generators-skip_less" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators-skip_less"></a><span class="panel-title">@skip less</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators-skip_less" data-outline="skip_less" data-ex-id="whitespace/skip_less" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p>Now suppose the grammar is instead:</p>

<pre><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces {
  semester <span class="hljs-keyword">::=</span> season year ;
}
season <span class="hljs-keyword">::=</span> <span class="hljs-string">'Fall'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'Spring'</span> ;
year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span> <span class="hljs-string">[0-9]</span> ;
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@rules_still_same"><a class="jump" href="#@rules_still_same"></a>The rules are still the same, but <code>@skip spaces</code> only wraps around the <code>semester</code> rule.</p>

<p>Which of these strings match the new grammar?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="Fall15"><label for="md_converted_choice_7_0"><input type="checkbox" id="md_converted_choice_7_0"><code>Fall15</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="␣␣␣␣Spring␣␣␣␣␣␣23"><label for="md_converted_choice_7_1"><input type="checkbox" id="md_converted_choice_7_1"><code>␣␣␣␣Spring␣␣␣␣␣␣23</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Spring␣9␣9"><label for="md_converted_choice_7_2"><input type="checkbox" id="md_converted_choice_7_2"><code>Spring␣9␣9</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="F␣a␣l␣l␣06"><label for="md_converted_choice_7_3"><input type="checkbox" id="md_converted_choice_7_3"><code>F␣a␣l␣l␣06</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators-another_skip" data-target="#ex-parser_generators-another_skip" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators-another_skip"></a><span class="panel-title">Another @skip</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators-another_skip" data-outline="another_skip" data-ex-id="whitespace/another_skip" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p>Now suppose the grammar is:</p>

<pre id="@skip_space_semester"><a class="jump" href="#@skip_space_semester"></a><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> space {
  semester <span class="hljs-keyword">::=</span> season year ;
}
season <span class="hljs-keyword">::=</span> <span class="hljs-string">'Fall'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'Spring'</span> ;
year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span> <span class="hljs-string">[0-9]</span> ;
space <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span> ;</code></pre>

<p id="@spaces_nonterminal_now"><a class="jump" href="#@spaces_nonterminal_now"></a>The <code>spaces</code> nonterminal has now been changed to match just one <code>space</code>.</p>

<p>Which of these strings match the new grammar?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="Fall15"><label for="md_converted_choice_8_0"><input type="checkbox" id="md_converted_choice_8_0"><code>Fall15</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="␣␣␣␣Spring␣␣␣␣␣␣23"><label for="md_converted_choice_8_1"><input type="checkbox" id="md_converted_choice_8_1"><code>␣␣␣␣Spring␣␣␣␣␣␣23</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Spring␣9␣9"><label for="md_converted_choice_8_2"><input type="checkbox" id="md_converted_choice_8_2"><code>Spring␣9␣9</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="F␣a␣l␣l␣06"><label for="md_converted_choice_8_3"><input type="checkbox" id="md_converted_choice_8_3"><code>F␣a␣l␣l␣06</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div><div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div></div>

</div><h3 id="generating_the_parser"><a class="jump" href="#generating_the_parser"></a>Generating the parser</h3><div data-outline="generating_the_parser">

<div class="handout-solo alert alert-warning"><p id="@code_examples_follow"><a class="jump" href="#@code_examples_follow"></a>The code for the examples that follow is downloadable as <a href="code.html" class="alert-link">ex12-intexpr</a>.</p>

<p id="@download_project_run"><a class="jump" href="#@download_project_run"></a>Download the project, run <code>npm install</code>, open in VS Code, and use <code>npm run start</code> to try your own modifications.</p>

<p id="@refer_documentation_parserlib"><a class="jump" href="#@refer_documentation_parserlib"></a>Refer to the <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/" class="alert-link">documentation for ParserLib</a> online.</p></div>

<p id="@rest_reading_use"><a class="jump" href="#@rest_reading_use"></a>The rest of this reading will use as a running example the <em>IntegerExpression</em> grammar that we defined in the previous section.
In order to embed the grammar in TypeScript with its newlines intact, we will use backquotes (also called a <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Template_literals">template literal</a>):</p>

<pre id="@const_grammar_string"><a class="jump" href="#@const_grammar_string"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">const</span> grammar: <span class="hljs-built_in">string</span> = <span class="hljs-string">`
  @skip whitespace {
      expr ::= sum;
      sum ::= primary ('+' primary)*;
      primary ::= constant | '(' sum ')';
  }
  constant ::= [0-9]+;
  whitespace ::= [ \\t\\r\\n]+;  // &lt;-- note that backslashes must be escaped
`</span>;</code></pre>

<p id="@parserlib_parser_generator"><a class="jump" href="#@parserlib_parser_generator"></a>The ParserLib parser generator tool converts a grammar string like this into a parser. In order to do this, you need to follow three steps. First, you need to import the ParserLib library:</p>

<pre id="@import_parser_parsetree"><a class="jump" href="#@import_parser_parsetree"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">import</span> { Parser, ParseTree, compile } <span class="hljs-keyword">from</span> <span class="hljs-string">"parserlib"</span>;</code></pre>

<p id="@second_step_define"><a class="jump" href="#@second_step_define"></a>The second step is to define an <code>enum</code> type that contains all the nonterminals used by your grammar. This will tell ParserLib which definitions to expect in the grammar and will allow it to check for any missing ones.</p>

<div class="code-container"><span class="handout-solo alert alert-warning"><a href="code.html#IntegerGrammar_enum" class="alert-link"><code>parser.ts</code></a></span>

<pre id="@enum_integergrammar_expr"><a class="jump" href="#@enum_integergrammar_expr"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">enum</span> IntegerGrammar {
  Expr, Sum, Primary, Constant, Whitespace
}</code></pre></div>

<p id="@note_parserlib_itself"><a class="jump" href="#@note_parserlib_itself"></a>Note that ParserLib itself is case insensitive, but by convention, the names of <code>enum</code> values are capitalized. </p>

<p id="@within_your_code"><a class="jump" href="#@within_your_code"></a>From within your code, you can create a <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/interfaces/Parser.html"><code>Parser</code></a> by calling its <code>compile</code> factory function.</p>

<div class="code-container"><span class="handout-solo alert alert-warning"><a href="code.html#Parser_compile" class="alert-link"><code>parser.ts</code></a></span>

<pre id="@const_parser_parser-integergrammar"><a class="jump" href="#@const_parser_parser-integergrammar"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">const</span> parser: Parser&lt;IntegerGrammar&gt; = compile(grammar, IntegerGrammar, IntegerGrammar.Expr);</code></pre></div>

<p id="@compile_method_takes"><a class="jump" href="#@compile_method_takes"></a>The <code>compile</code> method takes a grammar string, the nonterminal enumeration, and the nonterminal to use as the root nonterminal of the grammar.
In this case, the root nonterminal we want is <code>expr</code>, so we pass <code>IntegerGrammar.Expr</code>.</p>

<p id="@assuming_you_don-t"><a class="jump" href="#@assuming_you_don-t"></a>Assuming you don’t have any syntax errors in your grammar, the result will be a <code>Parser</code> object that can be used to parse text. Notice that the Parser is a generic type that is parameterized by the <code>IntegerGrammar</code> enum that you defined earlier.</p>

</div><h3 id="calling_the_parser"><a class="jump" href="#calling_the_parser"></a>Calling the parser</h3><div data-outline="calling_the_parser">

<div class="panel panel-figure pull-right pull-margin"><img src="figures/parse-tree.png" width="250px" alt="the parse tree produced by parsing '54+(2+ 89)' with the IntegerExpression grammar"></div>

<p id="@now_you-ve_generated"><a class="jump" href="#@now_you-ve_generated"></a>Now that you’ve generated the parser object, you are ready to parse your own text.  The parser has a method called <code>parse</code> that takes in the text to be parsed as a string, and returns a <code>ParseTree</code>. 
Calling it produces a parse tree:</p>

<pre id="@const_parsetree_parsetree-integergrammar"><a class="jump" href="#@const_parsetree_parsetree-integergrammar"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">const</span> parseTree: ParseTree&lt;IntegerGrammar&gt; = parser.parse(<span class="hljs-string">"54+(2+ 89)"</span>);</code></pre>

<p id="@note_parsetree_also"><a class="jump" href="#@note_parsetree_also"></a>Note that the <code>ParseTree</code> is also a generic type that is parameterized by the enum type <code>IntegerGrammar</code>.</p>

<p id="@debugging_can_then"><a class="jump" href="#@debugging_can_then"></a>For debugging, we can then print this tree out:</p>

<pre id="@console-log-parsetree-tostring"><a class="jump" href="#@console-log-parsetree-tostring"></a><code class="language-ts hljs typescript"><span class="hljs-built_in">console</span>.log(parseTree.toString());</code></pre>

<p id="@you_can_also_try_calling_function"><a class="jump" href="#@you_can_also_try_calling_function"></a>You can also try calling the function <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/index.html#visualizeAsUrl"><code>visualizeAsUrl(tree)</code></a> which will provide you with a URL which you can copy and paste to your browser to view the visualization. </p>

<div class="handout-solo alert alert-warning"><p id="@see_corresponding_code"><a class="jump" href="#@see_corresponding_code"></a>See the corresponding code in <a href="code.html#parser_parse" class="alert-link"><code>parser.ts</code></a>.</p></div>

</div><h3 id="traversing_the_parse_tree"><a class="jump" href="#traversing_the_parse_tree"></a>Traversing the parse tree</h3><div data-outline="traversing_the_parse_tree">

<p id="@so_we-ve_used"><a class="jump" href="#@so_we-ve_used"></a>So we’ve used the parser to turn a stream of characters into a parse tree, which shows how the grammar matches the stream.
Next we want to do something useful with this parse tree: translating it into a value of a recursive abstract data type.</p>

<p id="@like_parser_itself"><a class="jump" href="#@like_parser_itself"></a>Like the <code>Parser</code> itself, the <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/interfaces/ParseTree.html"><code>ParseTree</code></a> is parameterized by the type <code>NT</code>, an <code>enum</code> type that lists all the nonterminals in the grammar, like the <code>IntegerGrammar</code> enumeration we defined earlier.</p>

<p id="@first_step_learn"><a class="jump" href="#@first_step_learn"></a>The first step is to learn how to traverse the parse tree.
The <code>ParseTree</code> object has four methods that you need to be most familiar with.
Three of them are fundamental observers, represented as getter properties:</p>

<pre id="@interface_parsetree-nt_nonterminal"><a class="jump" href="#@interface_parsetree-nt_nonterminal"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">interface</span> ParseTree&lt;NT&gt; {

  <span class="hljs-comment">/**
   * The nonterminal corresponding to this node in the parse tree.
   */</span>
  name: NT;

  <span class="hljs-comment">/**
   * The children of this node, in order, excluding @skipped subtrees
   */</span>
  children: <span class="hljs-built_in">Array</span>&lt;ParseTree&lt;NT&gt;&gt;;

  <span class="hljs-comment">/**
   * The substring of the original string that this subtree matched
   */</span>
  text: <span class="hljs-built_in">string</span>;</code></pre>

<p id="@additionally_you_can"><a class="jump" href="#@additionally_you_can"></a>Additionally, you can query the ParseTree for all children that match a particular production rule:</p>

<pre id="@get_children_correspond"><a class="jump" href="#@get_children_correspond"></a><code class="language-ts hljs typescript">  <span class="hljs-comment">/**
   * Get the children that correspond to a particular production rule 
   * @param name Name of the nonterminal corresponding to the desired production rule.
   * @returns children that represent matches of name's production rule.
   */</span>
  <span class="hljs-keyword">public</span> childrenByName(name: NT): <span class="hljs-built_in">Array</span>&lt;ParseTree&lt;NT&gt;&gt;;
}</code></pre>

<p id="@good_way_visit"><a class="jump" href="#@good_way_visit"></a>A good way to visit the nodes in a parse tree is to write a recursive function.
For example, the recursive function below prints all nodes in the parse tree with proper indentation.</p>

<pre id="@traverse_parse_tree"><a class="jump" href="#@traverse_parse_tree"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * Traverse a parse tree, indenting to make it easier to read.
 * @param node   parse tree to print.
 * @param indent indentation to use.
 */</span>
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">printNodes</span>(<span class="hljs-params">node: ParseTree&lt;IntegerGrammar&gt;, indent: <span class="hljs-built_in">string</span></span>): <span class="hljs-title">void</span> </span>{
    <span class="hljs-built_in">console</span>.log(indent + node.name + <span class="hljs-string">":"</span> + node.text);
    <span class="hljs-keyword">for</span> (<span class="hljs-keyword">const</span> child of node.children){
        printNodes(child, indent + <span class="hljs-string">"  "</span>);
    }
}</code></pre>

<div class="panel panel-figure pull-right pull-margin"><img src="figures/parse-tree.png" width="250px" alt="the parse tree produced by parsing '54+(2+ 89)' with the IntegerExpression grammar"></div>

<div class="panel panel-figure pull-right pull-margin"></div>

<p id="@running_function_parse"><a class="jump" href="#@running_function_parse"></a>Running this function on the parse tree for <code>54+(2+ 89)</code> produces the output below.
For reference, the grammar and the visualized parse tree are shown at right.</p>

<table><tbody><tr><td style="width:30%; padding-right:30px; vertical-align:top"><pre id="@expr-54-2_89_sum-54-2"><a class="jump" href="#@expr-54-2_89_sum-54-2"></a><code class="language-none">Expr:54+(2+ 89)
  Sum:54+(2+ 89)
    Primary:54
      Constant:54
    Primary:(2+ 89)
      Sum:2+ 89
        Primary:2
          Constant:2
        Primary:89
          Constant:89</code></pre></td>
<td style="width:30%; vertical-align:top"><pre><code class="language-parserlib hljs"><span class="hljs-comment">// the IntegerExpression grammar</span>
<span class="hljs-keyword">@skip</span> whitespace {
    expr <span class="hljs-keyword">::=</span> sum ;
    sum <span class="hljs-keyword">::=</span> primary (<span class="hljs-string">'+'</span> primary)<span class="hljs-keyword">*</span> ;
    primary <span class="hljs-keyword">::=</span> constant <span class="hljs-keyword">|</span> <span class="hljs-string">'('</span> sum <span class="hljs-string">')'</span> ;
}
whitespace <span class="hljs-keyword">::=</span> <span class="hljs-string">[ \t\r\n]</span><span class="hljs-keyword">+</span> ;
constant <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre></td></tr></tbody></table>

<div class="clearfix"></div>

<div class="reading-exercises exercises panel-group converted" id="ex-parser_generators_2"><h4 class="text-danger">reading exercises</h4><div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_2-parse_trees" data-target="#ex-parser_generators_2-parse_trees" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_2-parse_trees"></a><span class="panel-title">Parse trees</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_2-parse_trees" data-outline="parse_trees" data-ex-id="traversing_the_parse_tree/parse_trees" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<label class="exercise-part-heading" id="@which_following_statements"><a class="jump" href="#@which_following_statements"></a>Which of the following statements are true of a <code>ParserLib</code> parse tree, from careful examination of the example output just above?</label>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="the root node of the tree corresponds to the root nonterminal of the grammar"><label for="md_converted_choice_9_0"><input type="checkbox" id="md_converted_choice_9_0">the root node of the tree corresponds to the root nonterminal of the grammar</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-group exercise-part" data-outline="b"><div class="checkbox exercise-choice" data-outline="each internal node of the tree is named by a nonterminal in the grammar"><label for="md_converted_choice_10_0"><input type="checkbox" id="md_converted_choice_10_0">each internal node of the tree is named by a nonterminal in the grammar</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-group exercise-part" data-outline="c"><div class="checkbox exercise-choice" data-outline="terminals do not have their own separate nodes that can be retrieved by children"><label for="md_converted_choice_11_0"><input type="checkbox" id="md_converted_choice_11_0">terminals do not have their own separate nodes that can be retrieved by <code>children</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-group exercise-part" data-outline="d"><div class="checkbox exercise-choice" data-outline="only a grammar with recursive productions can generate a parse tree"><label for="md_converted_choice_12_0"><input type="checkbox" id="md_converted_choice_12_0">only a grammar with recursive productions can generate a parse tree</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-group exercise-part" data-outline="e"><div class="checkbox exercise-choice" data-outline="a node’s immediate children must correspond to nonterminals mentioned in the node’s production "><label for="md_converted_choice_13_0"><input type="checkbox" id="md_converted_choice_13_0">a node’s immediate children must correspond to nonterminals mentioned in the node’s production </label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-group exercise-part" data-outline="f"><div class="checkbox exercise-choice" data-outline="@skip N means that the skipped nonterminal N never appears among the children of nodes whose rules are inside the @skip block"><label for="md_converted_choice_14_0"><input type="checkbox" id="md_converted_choice_14_0"><code>@skip N</code> means that the skipped nonterminal <code>N</code> never appears among the <code>children</code> of nodes whose rules are inside the <code>@skip</code> block</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_2-snapshot_diagram_of_a_parsetree" data-target="#ex-parser_generators_2-snapshot_diagram_of_a_parsetree" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_2-snapshot_diagram_of_a_parsetree"></a><span class="panel-title">Snapshot diagram of a ParseTree</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_2-snapshot_diagram_of_a_parsetree" data-outline="snapshot_diagram_of_a_parsetree" data-ex-id="traversing_the_parse_tree/snapshot_diagram_of_a_parsetree" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<div class="panel panel-default panel-figure pull-right pull-margin no-markdown">
<div class="panel-body text-center">
<img src="ref/lect13/20230816134715.png" class="pull-right" width="200">
<img src="ref/lect13/20230816134515.png" class="pull-right" width="500">
</div></div>

<p id="@see_parser-lib_parse"><a class="jump" href="#@see_parser-lib_parse"></a>Let’s see how a <code>Parser­Lib</code> parse tree looks as a snapshot diagram, because it’s closer to how we will think about it and work with it in TypeScript code.</p>

<p id="@partial_snapshot_diagram"><a class="jump" href="#@partial_snapshot_diagram"></a>The partial snapshot diagram at the right corresponds to part of the <code>Integer­Grammar</code> parse tree for <code>"2+ 89"</code>, also shown.</p>

<p id="@snapshot_diagram_shows"><a class="jump" href="#@snapshot_diagram_shows"></a>The snapshot diagram shows the three correspondingly-colored nodes along the left branch of the parse tree: <code>sum</code>, <code>primary</code>, and <code>constant</code>.</p>

<p id="@fill_gray_rectangles"><a class="jump" href="#@fill_gray_rectangles"></a>Fill in the gray rectangles in the snapshot diagram.</p>

<label class="exercise-part-heading">What should appear at location A?</label>

<div class="form-group exercise-part" data-outline="a"><div class="dropdown exercise-choice" data-outline="; Constant; Primary; Sum; “2”; “+”; “89”"><select class="form-control"><option selected="selected"></option><option>Constant</option>,<option>Primary</option>,<option>Sum</option>,<option>“2”</option>,<option>“+”</option>,<option>“89”</option></select><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<label class="exercise-part-heading">What should appear at location B?</label>

<div class="form-group exercise-part" data-outline="b"><div class="dropdown exercise-choice" data-outline="; Constant; Primary; Sum; “2”; “+”; “89”"><select class="form-control"><option selected="selected"></option><option>Constant</option>,<option>Primary</option>,<option>Sum</option>,<option>“2”</option>,<option>“+”</option>,<option>“89”</option></select><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<label class="exercise-part-heading" id="@should_appear_all"><a class="jump" href="#@should_appear_all"></a>What should appear at all the locations marked C?</label>

<div class="form-group exercise-part" data-outline="c"><div class="dropdown exercise-choice" data-outline="; IntegerGrammar; Array<ParseTree<IntegerGrammar>>; ParseTree<IntegerGrammar>"><select class="form-control"><option selected="selected"></option><option>IntegerGrammar</option>,<option>Array&lt;ParseTree&lt;IntegerGrammar&gt;&gt;</option>,<option>ParseTree&lt;IntegerGrammar&gt;</option></select><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div><div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div></div>

</div><h3 id="constructing_an_abstract_syntax_tree"><a class="jump" href="#constructing_an_abstract_syntax_tree"></a>Constructing an abstract syntax tree</h3><div data-outline="constructing_an_abstract_syntax_tree">

<p id="@need_convert_parse"><a class="jump" href="#@need_convert_parse"></a>We need to convert the parse tree into a recursive data type.
Here’s the definition of the recursive data type that we’re going to use to represent integer arithmetic expressions:</p>

<pre id="@integerexpression_constant-n_number"><a class="jump" href="#@integerexpression_constant-n_number"></a><code class="language-none">IntegerExpression = Constant(n: number)
                  + Plus(left: IntegerExpression, right: IntegerExpression)</code></pre>

<p id="@if_syntax_mysterious"><a class="jump" href="#@if_syntax_mysterious"></a>If this syntax is mysterious, review <a href="../11-recursive-data-types/#recursive_data_type_definitions">recursive data type definitions</a>.</p>

<p id="@recursive_data_type"><a class="jump" href="#@recursive_data_type"></a>When a recursive data type represents a language this way, it is often called an <mark data-structure-text="abstract syntax tree" id="^abstract_syntax_tree"><em>abstract syntax tree</em></mark>.  An <a href="code.html#integerexpressionts"><code>IntegerExpression</code></a> value captures the important features of the expression – its grouping and the integers in it – while omitting unnecessary details of the sequence of characters that created it.</p>

<p id="@contrast_parse_tree"><a class="jump" href="#@contrast_parse_tree"></a>By contrast, the parse tree that we just generated with the <code>IntegerExpression</code> parser is a <mark data-structure-text="concrete syntax tree" id="^concrete_syntax_tree"><em>concrete syntax tree</em></mark>.
It’s called concrete, rather than abstract, because it contains more details about how the expression is represented in actual characters.  For example, the strings <code>2+2</code>, <code>((2)+(2))</code>, and <code>0002+0002</code> would each produce a different concrete syntax tree, but these trees would all correspond to the same abstract <code>IntegerExpression</code> value: <code>Plus(Constant(2), Constant(2))</code>.</p>

<p id="@now_can_create"><a class="jump" href="#@now_can_create"></a>Now, we can create a recursive function that walks the <code>ParseTree</code> to produce an <code>IntegerExpression</code> as follows:</p>

<div id="makeAbstractSyntaxTree" class="code-container pull-margin"><span class="handout-solo alert alert-warning"><a href="code.html#makeAbstractSyntaxTree" class="alert-link"><code>parser.ts</code></a></span>

<pre id="@convert_parse_tree"><a class="jump" href="#@convert_parse_tree"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * Convert a parse tree into an abstract syntax tree.
 * 
 * @param parseTree constructed according to the IntegerExpression grammar
 * @returns abstract syntax tree corresponding to parseTree
 */</span>
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">makeAbstractSyntaxTree</span>(<span class="hljs-params">parseTree: ParseTree&lt;IntegerGrammar&gt;</span>): <span class="hljs-title">IntegerExpression</span> </span>{
    <span class="hljs-keyword">if</span> (parseTree.name === IntegerGrammar.Expr) {
        <span class="hljs-comment">// expr ::= sum;</span>
        <span class="hljs-keyword">return</span> makeAbstractSyntaxTree(parseTree.children[<span class="hljs-number">0</span>] ?? assert.fail(<span class="hljs-string">'missing child'</span>));

    } <span class="hljs-keyword">else</span> <span class="hljs-keyword">if</span> (parseTree.name === IntegerGrammar.Sum) {
        <span class="hljs-comment">// sum ::= primary ('+' primary)*;</span>
        <span class="hljs-keyword">const</span> children: <span class="hljs-built_in">Array</span>&lt;ParseTree&lt;IntegerGrammar&gt;&gt; = parseTree.childrenByName(IntegerGrammar.Primary);
        <span class="hljs-keyword">let</span> expression: IntegerExpression = makeAbstractSyntaxTree(children[<span class="hljs-number">0</span>] ?? assert.fail(<span class="hljs-string">"missing child"</span>));
        <span class="hljs-keyword">for</span> (<span class="hljs-keyword">let</span> i = <span class="hljs-number">1</span>; i &lt; children.length; ++i) {
            expression = <span class="hljs-keyword">new</span> Plus(expression, makeAbstractSyntaxTree(children[i] ?? assert.fail(<span class="hljs-string">"missing child"</span>)));
        }
        <span class="hljs-keyword">return</span> expression;

    } <span class="hljs-keyword">else</span> <span class="hljs-keyword">if</span> (parseTree.name === IntegerGrammar.Primary) {
        <span class="hljs-comment">// primary ::= constant | '(' sum ')';</span>
        <span class="hljs-keyword">const</span> child: ParseTree&lt;IntegerGrammar&gt; = parseTree.children[<span class="hljs-number">0</span>] ?? assert.fail(<span class="hljs-string">'missing child'</span>);
        <span class="hljs-comment">// check which alternative (constant or sum) was actually matched</span>
        <span class="hljs-keyword">switch</span> (child.name) {
        <span class="hljs-keyword">case</span> IntegerGrammar.Constant:
            <span class="hljs-keyword">return</span> makeAbstractSyntaxTree(child);
        <span class="hljs-keyword">case</span> IntegerGrammar.Sum:
            <span class="hljs-keyword">return</span> makeAbstractSyntaxTree(child); <span class="hljs-comment">// for this parser, we do the same thing either way</span>
        <span class="hljs-keyword">default</span>:
            assert.fail(<span class="hljs-string">`Primary node unexpected child <span class="hljs-subst">${IntegerGrammar[child.name]}</span>`</span>);
        }

    } <span class="hljs-keyword">else</span> <span class="hljs-keyword">if</span> (parseTree.name === IntegerGrammar.Constant) {
        <span class="hljs-comment">// constant ::= [0-9]+;</span>
        <span class="hljs-keyword">const</span> n: <span class="hljs-built_in">number</span> = <span class="hljs-built_in">parseInt</span>(parseTree.text);
        <span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> Constant(n);

    } <span class="hljs-keyword">else</span> {
        assert.fail(<span class="hljs-string">`cannot make AST for <span class="hljs-subst">${IntegerGrammar[parseTree.name]}</span> node`</span>);
    }
}</code></pre></div>

<p id="@function_follows_structure"><a class="jump" href="#@function_follows_structure"></a>The function follows the structure of the grammar, handling each rule from the grammar in turn: <code>expr</code>, <code>sum</code>, <code>primary</code>, and <code>constant</code>.
The only rule we don’t need to handle here is <code>whitespace</code>, because the grammar uses <code>whitespace</code> only in a <code>@skip</code> block, and skipped subtrees are not returned by <code>children</code> so they should never appear in the traversal.
Note, however, that skipped whitespace will still appear in the <a href="#@interface_parsetree-nt_nonterminal"><code>text</code></a> field of a matched nonterminal.</p>

<p id="@note_code_tied"><a class="jump" href="#@note_code_tied"></a>Note that this code is tied very closely to the grammar.
If you change the rules of the grammar in a significant way, this code will likely need to change too.</p>

<p id="@handle_primary_nodes"><a class="jump" href="#@handle_primary_nodes"></a>To handle <code>primary</code> nodes, this code uses a <code>switch</code> statement.
Any time you use <code>switch</code>, remember: the execution of a <code>switch</code> statement starts at the matching <code>case</code> and <em>falls through</em> to subsequent cases.
Our code here is careful to <code>return</code> (or raise an error) in every case, never falling through.</p>

<div class="reading-exercises exercises panel-group converted" id="ex-parser_generators_3"><h4 class="text-danger">reading exercises</h4><div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-string_to_ast_1" data-target="#ex-parser_generators_3-string_to_ast_1" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-string_to_ast_1"></a><span class="panel-title">String to AST 1</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-string_to_ast_1" data-outline="string_to_ast_1" data-ex-id="constructing_an_abstract_syntax_tree/string_to_ast_1" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@if_input_string"><a class="jump" href="#@if_input_string"></a>If the input string is <code>"19+23+18"</code>, which abstract syntax tree would be produced by <code>makeAbstractSyntaxTree</code> above?</p>

<div class="form-group exercise-part" data-outline="a"><div class="radio exercise-choice" data-outline="Plus(Constant(19))"><label for="md_converted_choice_19_0"><input type="radio" id="md_converted_choice_19_0" name="md_converted_radio_19">Plus(Constant(19))</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="Plus(19, 23, 18)"><label for="md_converted_choice_19_1"><input type="radio" id="md_converted_choice_19_1" name="md_converted_radio_19">Plus(19, 23, 18)</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="Plus(Plus(19, 23), 18)"><label for="md_converted_choice_19_2"><input type="radio" id="md_converted_choice_19_2" name="md_converted_radio_19">Plus(Plus(19, 23), 18)</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="Plus(Plus(Constant(19), Constant(23)), Constant(18))"><label for="md_converted_choice_19_3"><input type="radio" id="md_converted_choice_19_3" name="md_converted_radio_19">Plus(Plus(Constant(19), Constant(23)), Constant(18))</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="Plus(Constant(19), Plus(Constant(23), Constant(18)))"><label for="md_converted_choice_19_4"><input type="radio" id="md_converted_choice_19_4" name="md_converted_radio_19">Plus(Constant(19), Plus(Constant(23), Constant(18)))</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-string_to_ast_2" data-target="#ex-parser_generators_3-string_to_ast_2" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-string_to_ast_2"></a><span class="panel-title">String to AST 2</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-string_to_ast_2" data-outline="string_to_ast_2" data-ex-id="constructing_an_abstract_syntax_tree/string_to_ast_2" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@which_following_input"><a class="jump" href="#@which_following_input"></a>Which of the following input strings would produce:</p>

<pre id="@plus-plus-constant-1_constant-2_plus-constant-3"><a class="jump" href="#@plus-plus-constant-1_constant-2_plus-constant-3"></a><code class="language-none">Plus(Plus(Constant(1), Constant(2)), 
     Plus(Constant(3), Constant(4)))</code></pre>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="&quot;(1+2)+(3+4)&quot;"><label for="md_converted_choice_15_0"><input type="checkbox" id="md_converted_choice_15_0"><code>"(1+2)+(3+4)"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="&quot;1+2+3+4&quot;"><label for="md_converted_choice_15_1"><input type="checkbox" id="md_converted_choice_15_1"><code>"1+2+3+4"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="&quot;(1+2)+3+4&quot;"><label for="md_converted_choice_15_2"><input type="checkbox" id="md_converted_choice_15_2"><code>"(1+2)+3+4"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="&quot;(((1+2)))+(3+4)&quot;"><label for="md_converted_choice_15_3"><input type="checkbox" id="md_converted_choice_15_3"><code>"(((1+2)))+(3+4)"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-binary_parse_tree" data-target="#ex-parser_generators_3-binary_parse_tree" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-binary_parse_tree"></a><span class="panel-title">Binary parse tree</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-binary_parse_tree" data-outline="binary_parse_tree" data-ex-id="constructing_an_abstract_syntax_tree/binary_parse_tree" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@code_looking_plus"><a class="jump" href="#@code_looking_plus"></a>In the code we’re looking at, the <code>Plus</code> operator in the abstract syntax tree is <em>binary</em>.
It represents the sum of exactly two expressions, a lefthand side and a righthand side.</p>

<p id="@corresponding_grammar_rule"><a class="jump" href="#@corresponding_grammar_rule"></a>Its corresponding grammar rule <code>sum ::= primary ('+' primary)*</code> is <em>n-ary</em>.
It matches the sum of <em>n</em> primary expressions, where <em>n</em> ≥ 1.
As a result, a <code>Sum</code> node in the parse tree may have one or more <code>Primary</code> children, not just two.</p>

<p id="@part_complexity_sum"><a class="jump" href="#@part_complexity_sum"></a>Part of the complexity of the <code>Sum</code> case of <code>makeAbstractSyntaxTree()</code> comes from translating an n-ary node into binary nodes.</p>

<p id="@if_wanted_sum"><a class="jump" href="#@if_wanted_sum"></a>If we wanted the <code>Sum</code> node to be (at most) binary instead, which of the following grammar rules would do it correctly?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="sum ::= primary '+' primary"><label for="md_converted_choice_16_0"><input type="checkbox" id="md_converted_choice_16_0"><code>sum ::= primary '+' primary</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="sum ::= primary '+' primary*"><label for="md_converted_choice_16_1"><input type="checkbox" id="md_converted_choice_16_1"><code>sum ::= primary '+' primary*</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="sum ::= primary | primary '+' sum"><label for="md_converted_choice_16_2"><input type="checkbox" id="md_converted_choice_16_2"><code>sum ::= primary | primary '+' sum</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="sum ::= sum '+' sum"><label for="md_converted_choice_16_3"><input type="checkbox" id="md_converted_choice_16_3"><code>sum ::= sum '+' sum</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-n-ary_ast" data-target="#ex-parser_generators_3-n-ary_ast" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-n-ary_ast"></a><span class="panel-title">N-ary AST</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-n-ary_ast" data-outline="n-ary_ast" data-ex-id="constructing_an_abstract_syntax_tree/n-ary_ast" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@suppose_instead_keep"><a class="jump" href="#@suppose_instead_keep"></a>Suppose instead that we keep the <code>Sum</code> node in the parse tree as n-ary, but we want to change the abstract syntax tree <code>Plus</code> node from strictly binary to n-ary.
Which of these data type definitions would do it correctly?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="IntegerExpression = Constant(n: number) + Plus(operands: Array<number>)"><label for="md_converted_choice_17_0"><input type="checkbox" id="md_converted_choice_17_0"><code>IntegerExpression = Constant(n: number) + Plus(operands: Array&lt;number&gt;)</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="IntegerExpression = Constant(n: number) + Plus(operands: Array<IntegerExpression>)"><label for="md_converted_choice_17_1"><input type="checkbox" id="md_converted_choice_17_1"><code>IntegerExpression = Constant(n: number) + Plus(operands: Array&lt;IntegerExpression&gt;)</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="IntegerExpression = Constants(array: Array<number>) + Plus(expr: IntegerExpression)"><label for="md_converted_choice_17_2"><input type="checkbox" id="md_converted_choice_17_2"><code>IntegerExpression = Constants(array: Array&lt;number&gt;) + Plus(expr: IntegerExpression)</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-enumeration" data-target="#ex-parser_generators_3-enumeration" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-enumeration"></a><span class="panel-title">Enumeration</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-enumeration" data-outline="enumeration" data-ex-id="constructing_an_abstract_syntax_tree/enumeration" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p>For this grammar:</p>

<pre id="@skip_spaces_date"><a class="jump" href="#@skip_spaces_date"></a><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces { 
  date <span class="hljs-keyword">::=</span> monthname day <span class="hljs-string">','</span> year <span class="hljs-keyword">|</span> day <span class="hljs-string">'/'</span> monthnum <span class="hljs-string">'/'</span> year ;
}
monthname <span class="hljs-keyword">::=</span> <span class="hljs-string">'January'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'February'</span> ;
monthnum <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ; 
day <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;
year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@values_would_you"><a class="jump" href="#@values_would_you"></a>…what values would you need to have in the enumeration type passed to ParserLib?</p>

<p id="@specifically_you_define"><a class="jump" href="#@specifically_you_define"></a>Specifically, when you define <code>enum DateGrammar { ... }</code> and call <code>compile()</code>, what values should appear in the <code>...</code> of the <code>enum</code>?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="Root"><label for="md_converted_choice_18_0"><input type="checkbox" id="md_converted_choice_18_0">Root</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Date"><label for="md_converted_choice_18_1"><input type="checkbox" id="md_converted_choice_18_1">Date</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Day"><label for="md_converted_choice_18_2"><input type="checkbox" id="md_converted_choice_18_2">Day</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Month"><label for="md_converted_choice_18_3"><input type="checkbox" id="md_converted_choice_18_3">Month</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="MonthName"><label for="md_converted_choice_18_4"><input type="checkbox" id="md_converted_choice_18_4">MonthName</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="MonthNum"><label for="md_converted_choice_18_5"><input type="checkbox" id="md_converted_choice_18_5">MonthNum</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Year"><label for="md_converted_choice_18_6"><input type="checkbox" id="md_converted_choice_18_6">Year</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="January"><label for="md_converted_choice_18_7"><input type="checkbox" id="md_converted_choice_18_7">January</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="February"><label for="md_converted_choice_18_8"><input type="checkbox" id="md_converted_choice_18_8">February</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Spaces"><label for="md_converted_choice_18_9"><input type="checkbox" id="md_converted_choice_18_9">Spaces</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="Constant"><label for="md_converted_choice_18_10"><input type="checkbox" id="md_converted_choice_18_10">Constant</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-using_a_parse_tree" data-target="#ex-parser_generators_3-using_a_parse_tree" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-using_a_parse_tree"></a><span class="panel-title">Using a parse tree</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-using_a_parse_tree" data-outline="using_a_parse_tree" data-ex-id="constructing_an_abstract_syntax_tree/using_a_parse_tree" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@consider_grammar_which"><a class="jump" href="#@consider_grammar_which"></a>Consider this grammar, which is designed to match a list of numbers like <code>5,-8,3</code>:</p>

<pre id="@skip_spaces_list"><a class="jump" href="#@skip_spaces_list"></a><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces { 
  list <span class="hljs-keyword">::=</span> int (<span class="hljs-string">','</span> list)<span class="hljs-keyword">?</span> ;
}
int <span class="hljs-keyword">::=</span> <span class="hljs-string">'-'</span><span class="hljs-keyword">?</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@root_nonterminal_list"><a class="jump" href="#@root_nonterminal_list"></a>The root nonterminal is <code>list</code>.  </p>

<p id="@fill_blanks_function"><a class="jump" href="#@fill_blanks_function"></a>Fill in the blanks of this function that converts a <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/interfaces/ParseTree.html"><code>ParseTree</code></a> for this grammar into a <code>List</code>.</p>

<pre id="@enum_listgrammar_list"><a class="jump" href="#@enum_listgrammar_list"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">enum</span> ListGrammar { List, Int, Spaces }
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">makeList</span>(<span class="hljs-params">parseTree: ParseTree&lt;ListGrammar&gt;</span>): <span class="hljs-title">Array</span>&lt;<span class="hljs-title">number</span>&gt; </span>{
    <span class="hljs-keyword">switch</span> (parseTree.name) {
    <span class="hljs-keyword">case</span> ListGrammar.List:
        <span class="hljs-keyword">const</span> list: <span class="hljs-built_in">Array</span>&lt;<span class="hljs-built_in">number</span>&gt; = [];
        <span class="hljs-keyword">const</span> intChild: ParseTree&lt;ListGrammar&gt;|<span class="hljs-literal">undefined</span> = parseTree.children[<span class="hljs-number">0</span>];
        <span class="hljs-keyword">const</span> listChild: ParseTree&lt;ListGrammar&gt;|<span class="hljs-literal">undefined</span> = parseTree.children[<span class="hljs-number">1</span>];
        assert(intChild);
        list.push(<span class="hljs-built_in">parseInt</span>(▶▶A◀◀));
        <span class="hljs-keyword">if</span> (▶▶B◀◀) {
            list = list.concat(▶▶C◀◀);
        }
        <span class="hljs-keyword">return</span> list;
    <span class="hljs-keyword">default</span>:
        <span class="hljs-keyword">throw</span> <span class="hljs-keyword">new</span> <span class="hljs-built_in">Error</span>(<span class="hljs-string">"should never get here"</span>);
    }
}</code></pre>

<div class="list-style-upper-alpha"><ol>
<li><div class="form-group exercise-part" data-outline="a"><div class="textfield ttfont exercise-choice"><input type="text" class="form-control" style="width: 28%;"><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div></li>
<li><div class="form-group exercise-part" data-outline="b"><div class="textfield ttfont exercise-choice"><input type="text" class="form-control" style="width: 50%;"><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div></li>
<li><div class="form-group exercise-part" data-outline="c"><div class="textfield ttfont exercise-choice"><input type="text" class="form-control" style="width: 34%;"><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div></li>
</ol></div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-to_every_thing" data-target="#ex-parser_generators_3-to_every_thing" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-to_every_thing"></a><span class="panel-title">To every thing…</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-to_every_thing" data-outline="to_every_thing" data-ex-id="constructing_an_abstract_syntax_tree/to_every_thing" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p id="@consider_grammar_semester"><a class="jump" href="#@consider_grammar_semester"></a>Consider this grammar, with <code>semester</code> as its root nonterminal:</p>

<pre><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces {
  semester <span class="hljs-keyword">::=</span> season year ;
  season <span class="hljs-keyword">::=</span> <span class="hljs-string">'Fall'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'Spring'</span> ;
  year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span> <span class="hljs-string">[0-9]</span> ;
}
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@suppose_parserlib_used"><a class="jump" href="#@suppose_parserlib_used"></a>Suppose ParserLib is used to produce a parse tree, and you are writing code to convert the parse tree into abstract data types representing semesters and seasons.
Here is part of that code intended to handle just the season node of the tree:</p>

<pre id="@nonterminals_grammar_enum"><a class="jump" href="#@nonterminals_grammar_enum"></a><code class="language-ts hljs typescript"><span class="hljs-comment">// nonterminals in the grammar</span>
<span class="hljs-keyword">enum</span> SemesterGrammar { Semester, Season, Year, Spaces }

<span class="hljs-comment">// abstract data type representing a season of the year</span>
<span class="hljs-keyword">enum</span> Season { Fall, Spring, Winter, Summer }

<span class="hljs-comment">/**
 * @param node must be a match to the season rule
 * @returns corresponding Season value
 */</span>
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">convertToSeason</span>(<span class="hljs-params">node: ParseTree&lt;SemesterGrammar&gt;</span>): <span class="hljs-title">Season</span> </span>{
  assert.equal(node.name, SemesterGrammar.Season);
  <span class="hljs-keyword">return</span> ▶▶A◀◀ ? Season.Fall : Season.Spring;
}</code></pre>

<p id="@simplest_correct_code"><a class="jump" href="#@simplest_correct_code"></a>What is the simplest correct code for <code>▶▶A◀◀</code>?
(You may want to look at the spec of <code>ParseTree</code>, specifically <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/interfaces/ParseTree.html#text"><code>ParseTree.text</code></a>.)</p>

<div class="form-group exercise-part" data-outline="a"><div class="radio exercise-choice" data-outline="node.text == &quot;Fall&quot;"><label for="md_converted_choice_20_0"><input type="radio" id="md_converted_choice_20_0" name="md_converted_radio_20"><code>node.text ==  "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="node.text === &quot;Fall&quot;"><label for="md_converted_choice_20_1"><input type="radio" id="md_converted_choice_20_1" name="md_converted_radio_20"><code>node.text === "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="node.text.trim() === &quot;Fall&quot;"><label for="md_converted_choice_20_2"><input type="radio" id="md_converted_choice_20_2" name="md_converted_radio_20"><code>node.text.trim() === "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div>

<div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div>

<div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-parser_generators_3-there_is_a_season" data-target="#ex-parser_generators_3-there_is_a_season" data-toggle="collapse"><a class="jump" href="#@ex-parser_generators_3-there_is_a_season"></a><span class="panel-title">…there is a season</span></div><div class="panel-collapse collapse exercise-panel" id="ex-parser_generators_3-there_is_a_season" data-outline="there_is_a_season" data-ex-id="constructing_an_abstract_syntax_tree/there_is_a_season" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-12-regex-grammars"><div class="panel-body">

<p>Now suppose the grammar is instead:</p>

<pre><code class="language-parserlib hljs"><span class="hljs-keyword">@skip</span> spaces {
  semester <span class="hljs-keyword">::=</span> season year ;
}
season <span class="hljs-keyword">::=</span> <span class="hljs-string">'Fall'</span> <span class="hljs-keyword">|</span> <span class="hljs-string">'Spring'</span> ;
year <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span> <span class="hljs-string">[0-9]</span> ;
spaces <span class="hljs-keyword">::=</span> <span class="hljs-string">' '</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@same_line_code"><a class="jump" href="#@same_line_code"></a>In the same line of code in the previous exercise:</p>

<pre id="@return_season-fall_season-spring"><a class="jump" href="#@return_season-fall_season-spring"></a><code class="hljs parserlib">  return ▶▶A◀◀ <span class="hljs-keyword">?</span> Season<span class="hljs-keyword">.</span>Fall : Season<span class="hljs-keyword">.</span>Spring;</code></pre>

<p id="@now_simplest_correct"><a class="jump" href="#@now_simplest_correct"></a>…what is now the simplest correct code for <code>▶▶A◀◀</code>?</p>

<div class="form-group exercise-part" data-outline="a"><div class="radio exercise-choice" data-outline="node.text == &quot;Fall&quot;"><label for="md_converted_choice_21_0"><input type="radio" id="md_converted_choice_21_0" name="md_converted_radio_21"><code>node.text == "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="node.text === &quot;Fall&quot;"><label for="md_converted_choice_21_1"><input type="radio" id="md_converted_choice_21_1" name="md_converted_radio_21"><code>node.text === "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="radio exercise-choice" data-outline="node.text.trim() === &quot;Fall&quot;"><label for="md_converted_choice_21_2"><input type="radio" id="md_converted_choice_21_2" name="md_converted_radio_21"><code>node.text.trim() === "Fall"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div><div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div></div>

</div><h3 id="handling_errors"><a class="jump" href="#handling_errors"></a>Handling errors</h3><div data-outline="handling_errors">

<p id="@several_things_can"><a class="jump" href="#@several_things_can"></a>Several things can go wrong when parsing. </p>

<p id="@your_grammar_may"><a class="jump" href="#@your_grammar_may"></a><strong>Your grammar may have a syntax error in it.</strong>
In this case, <code>compile</code> will throw a <a href="https://web.mit.edu/6.102/www/parserlib/3.2.3/typedoc/classes/ParseError.html"><code>ParseError</code></a>.</p>

<p id="@string_you_trying"><a class="jump" href="#@string_you_trying"></a><strong>The string you are trying to parse may not be parseable with your given grammar.</strong>
This might happen because your grammar is incorrect, or because your string is incorrect.
Either way, the problem will be signaled by the <code>parse</code> method throwing an <code>ParseError</code>.</p>

<p id="@parseerror_error_contains"><a class="jump" href="#@parseerror_error_contains"></a>The <code>ParseError</code> error contains some information about the possible location of the error, although parse errors are sometimes inherently difficult to localize, since the parser cannot know what string you intended to write, so you may need to search a little to find the true location of the error. </p>

</div><h3 id="left_recursion_and_other_parserlib_limitations"><a class="jump" href="#left_recursion_and_other_parserlib_limitations"></a>Left recursion and other ParserLib limitations</h3><div data-outline="left_recursion_and_other_parserlib_limitations">

<p id="@parserlib_works_generating"><a class="jump" href="#@parserlib_works_generating"></a>ParserLib works by generating a top-down <a href="https://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-035-computer-language-engineering-spring-2010/lecture-notes/MIT6_035S10_lec04.pdf">Recursive Descent Parser</a>. These kind of parsers have a few limitations in terms of the grammars that they can parse. There are two in particular that are worth pointing out. </p>

<p id="@left_recursion_recursive"><a class="jump" href="#@left_recursion_recursive"></a><mark data-mark-text="left recursion (parsing)" data-structure-text="left recursion (parsing)" id="^left_recursion_parsing"><strong>Left recursion.</strong></mark> A recursive descent parser can go into an infinite loop if the grammar involves <a href="https://en.wikipedia.org/wiki/Left_recursion">left recursion</a>. This is a case where a definition for a nonterminal involves that nonterminal as its leftmost symbol. For example, the grammar below includes left recursion because one of the possible definitions of <code>sum</code> is <code>sum '+' number</code> which has <code>sum</code> as its leftmost symbol.</p>

<pre id="@sum_number_sum"><a class="jump" href="#@sum_number_sum"></a><code class="language-parserlib hljs">sum <span class="hljs-keyword">::=</span> number <span class="hljs-keyword">|</span> sum <span class="hljs-string">'+'</span> number ;
number <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@left-recursive_definition_problematic"><a class="jump" href="#@left-recursive_definition_problematic"></a>This left-recursive definition is problematic because the recursive descent parser needs to try matching all alternatives for <code>sum</code>, both <code>number</code> and <code>sum '+' number</code>.
But trying to match <code>sum '+' number</code> immediately requires matching <code>sum</code> as its first step.
The parser keeps trying the rule recursively but never makes any progress through the string being parsed – not reducing to a smaller subproblem as correct recursion requires to terminate.
By contrast, a rule like <code>expr = number | '(' expr ')'</code>, which is recursive but not left-recursive, is able to make progress through the string by matching <code>(</code> before recursively applying the <code>expr</code> rule again.</p>

<p id="@left_recursion_can"><a class="jump" href="#@left_recursion_can"></a>Left recursion can also happen indirectly. For example, changing the grammar above to the one below does not address the problem because the definition of <code>sum</code> still indirectly involves a symbol that has <code>sum</code> as its first symbol. </p>

<pre id="@sum_number_thing"><a class="jump" href="#@sum_number_thing"></a><code class="language-parserlib hljs">sum <span class="hljs-keyword">::=</span> number <span class="hljs-keyword">|</span> thing number ;
thing <span class="hljs-keyword">::=</span> sum <span class="hljs-string">'+'</span> ;
number <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@if_you_give"><a class="jump" href="#@if_you_give"></a>If you give any of these grammars to ParserLib and then try to use them to parse a symbol, ParserLib will fail with a <code>ParseError</code> listing the offending nonterminal. </p>

<p id="@there_some_general"><a class="jump" href="#@there_some_general"></a>There are some <a href="https://en.wikipedia.org/wiki/Left_recursion#Removing_left_recursion">general techniques</a> to eliminate left recursion; for our purposes, the simplest approach will be to replace left recursion with repetition (<code>*</code>), so the grammar above becomes:</p>

<pre id="@sum_number_number"><a class="jump" href="#@sum_number_number"></a><code class="language-parserlib hljs">sum <span class="hljs-keyword">::=</span> (number <span class="hljs-string">'+'</span>)<span class="hljs-keyword">*</span> number ;
number <span class="hljs-keyword">::=</span> <span class="hljs-string">[0-9]</span><span class="hljs-keyword">+</span> ;</code></pre>

<p id="@greediness_issue_you"><a class="jump" href="#@greediness_issue_you"></a><strong>Greediness.</strong> This is an issue that you may not run into in this class, but it is a limitation of ParserLib you should be aware of. The ParserLib parsers are greedy in that at every point they try to match a maximal string for any rule they are currently considering. For example, consider the following grammar:</p>

<pre id="@g_ab_threeb"><a class="jump" href="#@g_ab_threeb"></a><code class="language-parserlib hljs">g <span class="hljs-keyword">::=</span> ab threeb ;
ab <span class="hljs-keyword">::=</span> <span class="hljs-string">'a'</span><span class="hljs-keyword">*</span><span class="hljs-string">'b'</span><span class="hljs-keyword">*</span> ;
threeb <span class="hljs-keyword">::=</span> <span class="hljs-string">'bbb'</span> ;</code></pre>

<p id="@string_aaaabbb_clearly"><a class="jump" href="#@string_aaaabbb_clearly"></a>The string <code>'aaaabbb'</code> clearly should match <code>g</code>, but a greedy parser cannot parse it because it will try to parse a maximal substring that matches the <code>ab</code> symbol, and then it will find that it cannot parse <code>threeb</code> because it has already consumed the entire string. Unlike left recursion, which is easy to fix, this is a more fundamental limitation of the type of parser implemented by ParserLib.</p>

</div></div>