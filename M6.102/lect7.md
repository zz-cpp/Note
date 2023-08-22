# Reading 7: Abstraction Functions & Rep Invariants

Objectives

<ul id="@invariants_representation_exposure">
<li><a class="jump" href="#@invariants_representation_exposure"></a>invariants</li>
<li>representation exposure</li>
<li>abstraction functions</li>
<li>representation invariants</li>
</ul>

<p id="@reading_study_more"><a class="jump" href="#@reading_study_more"></a>In this reading, we study a more formal mathematical idea of what it means for a class to implement an ADT via the notions of <em>abstraction functions</em> and <em>rep invariants</em>.
These mathematical notions are eminently practical in software design.
The abstraction function gives us a way to cleanly define the equality operation on an abstract data type (which we’ll discuss in more depth in a future class).
The rep invariant makes it easier to catch bugs caused by a corrupted data structure.</p>

## Invariants

<div data-outline="invariants">

<p id="@resuming_our_discussion"><a class="jump" href="#@resuming_our_discussion"></a>Resuming our discussion of what makes a good abstract data type, the final, and perhaps most important, property of a good abstract data type is that it <strong>preserves its own invariants</strong>.
An <mark data-structure-text="invariant" id="^invariant"><em>invariant</em></mark> is a property of a program that is always true, for every possible runtime state of the program, given it was true at the beginning. You may recall this concept of a preserved invariant from 6.1200 [formerly 6.042].
Immutability is one crucial invariant that we’ve already encountered: once created, an immutable object should always represent the same value, for its entire lifetime.
But there are many other kinds of invariants, including types of variables (<code>i: number</code> means that <code>i</code> is always a number), or relationships between variables (e.g. when <code>i</code> is used to loop over the indices of an array, then <code>0 &lt;= i &lt; array.length</code> is an invariant within the body of the loop).</p>

<p id="@saying_adt_preserves"><a class="jump" href="#@saying_adt_preserves"></a>Saying that the ADT <em>preserves its own invariants</em> means that the ADT is responsible for ensuring that its own invariants hold.
This is done by hiding or protecting the variables involved in the invariants (e.g., using language features like <code>private</code>), and allowing access only through operations with well-defined contracts.</p>

<p id="@adt_preserves_own"><a class="jump" href="#@adt_preserves_own"></a>When an ADT preserves its own invariants, reasoning about the code becomes much easier.
If you can count on the fact that strings are immutable, for example, then you can rule out the possibility that a string has been mutated when you’re debugging code that uses strings – or when you’re trying to establish an invariant for another ADT that uses strings.
Contrast that with a mutable string type, which can be mutated by any code that has access to it.
To reason about a bug or invariant involving a mutable string, you’d have to check all the places in the code where the string might be accessible.</p>

<div class="reading-exercises exercises panel-group converted" id="ex-invariants"><iframe class="exercises-status" src="https://6031.mit.edu/handx/sp23/status.php"></iframe><h4 class="text-danger">reading exercises</h4><div class="panel panel-danger"><div class="panel-heading" data-structure-tag="exercise" id="@ex-invariants-statically-checked_invariants" data-target="#ex-invariants-statically-checked_invariants" data-toggle="collapse"><a class="jump" href="#@ex-invariants-statically-checked_invariants"></a><span class="panel-title">Statically-checked invariants</span></div><div class="panel-collapse collapse exercise-panel" id="ex-invariants-statically-checked_invariants" data-outline="statically-checked_invariants" data-ex-id="invariants/statically-checked_invariants" data-ex-category="reading-exercises" data-ex-remote="https://6031.mit.edu/handx/sp23/submit.php" data-ex-handout="classes-07-abstraction-functions-rep-invariants"><div class="panel-body">

<p id="@already_accustomed_declaring"><a class="jump" href="#@already_accustomed_declaring"></a>We’re already accustomed to declaring some useful invariants.
Consider this line of code:</p>

<pre id="@const_names_array-string"><a class="jump" href="#@const_names_array-string"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">const</span> names: <span class="hljs-built_in">Array</span>&lt;<span class="hljs-built_in">string</span>&gt; = [<span class="hljs-string">"Huey"</span>, <span class="hljs-string">"Dewey"</span>, <span class="hljs-string">"Louie"</span>];</code></pre>

<p id="@which_following_invariants"><a class="jump" href="#@which_following_invariants"></a>Which of the following are invariants expressed by this code?</p>

<div class="form-group exercise-part" data-outline="a"><div class="checkbox exercise-choice" data-outline="names refers to an object of type Array"><label for="md_converted_choice_1_0"><input type="checkbox" id="md_converted_choice_1_0"><code>names</code> refers to an object of type <code>Array</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="every element of names is the name of a real person"><label for="md_converted_choice_1_1"><input type="checkbox" id="md_converted_choice_1_1">every element of <code>names</code> is the name of a real person</label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="every element of names is an object of type string"><label for="md_converted_choice_1_2"><input type="checkbox" id="md_converted_choice_1_2">every element of <code>names</code> is an object of type <code>string</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
<div class="checkbox exercise-choice" data-outline="the first element of names is the string &quot;Huey&quot;"><label for="md_converted_choice_1_3"><input type="checkbox" id="md_converted_choice_1_3">the first element of <code>names</code> is the string <code>"Huey"</code></label><span class="exercise-answer exercise-remote" style="display: none;">(missing answer)</span></div>
</div>

<div class="exercise-explain exercise-remote"><p>(missing explanation)</p></div><div class="form-inline"><div class="form-group"><button class="btn btn-default exercise-submit">check</button> <button class="btn btn-default exercise-reveal" style="display: none;">explain</button></div><div class="exercise-progress progress"><div class="progress-bar progress-bar-danger progress-bar-striped active"></div></div><div class="exercise-error"></div></div></div></div></div></div>

<h3 id="immutability"><a class="jump" href="#immutability"></a>Immutability</h3><div data-outline="immutability">

<p id="@later_reading_see"><a class="jump" href="#@later_reading_see"></a>Later in this reading, we’ll see many interesting invariants.
Let’s focus on immutability for now. Here’s a specific example:</p>

<pre id="@immutable_data_type"><a class="jump" href="#@immutable_data_type"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * This immutable data type represents a tweet from Twitter.
 */</span>
<span class="hljs-keyword">class</span> Tweet {

    <span class="hljs-keyword">public</span> author: <span class="hljs-built_in">string</span>;
    <span class="hljs-keyword">public</span> text: <span class="hljs-built_in">string</span>;
    <span class="hljs-keyword">public</span> timestamp: <span class="hljs-built_in">Date</span>;

    <span class="hljs-comment">/**
     * Make a Tweet.
     * @param author    Twitter user who wrote the tweet
     * @param text      text of the tweet
     * @param timestamp date/time when the tweet was sent
     */</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">constructor</span>(<span class="hljs-params">author: <span class="hljs-built_in">string</span>, text: <span class="hljs-built_in">string</span>, timestamp: <span class="hljs-built_in">Date</span></span>) {
        <span class="hljs-keyword">this</span>.author = author;
        <span class="hljs-keyword">this</span>.text = text;
        <span class="hljs-keyword">this</span>.timestamp = timestamp;
    }
}</code></pre>

<p id="@do_guarantee_these"><a class="jump" href="#@do_guarantee_these"></a>How do we guarantee that these Tweet objects are immutable – that, once a tweet is created, its author, message, and date can never be changed?</p>

<p id="@first_threat_immutability"><a class="jump" href="#@first_threat_immutability"></a>The first threat to immutability comes from the fact that clients can — in fact must — directly access its fields. So nothing’s stopping us from writing code like this:</p>

<pre id="@let_t_tweet"><a class="jump" href="#@let_t_tweet"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">let</span> t: Tweet = <span class="hljs-keyword">new</span> Tweet(<span class="hljs-string">"justinbieber"</span>,
                    <span class="hljs-string">"Thanks to all those beliebers out there inspiring me every day"</span>,
                    <span class="hljs-keyword">new</span> <span class="hljs-built_in">Date</span>());
t.author = <span class="hljs-string">"rbmllr"</span>;</code></pre>

<p id="@trivial_example_representation"><a class="jump" href="#@trivial_example_representation"></a>This is a trivial example of <mark data-structure-text="representation exposure" id="^representation_exposure"><strong>representation exposure</strong></mark>, meaning that code outside the class can modify the representation directly. Rep exposure like this threatens not only invariants, but also <a href="../06-abstract-data-types/#representation_independence">representation independence</a>.
We can’t change the implementation of Tweet without affecting all the clients who are directly accessing those fields.</p>

<p id="@fortunately_typescript_gives"><a class="jump" href="#@fortunately_typescript_gives"></a>Fortunately, TypeScript gives us language mechanisms to deal with this kind of rep exposure:</p>

<pre id="@class_tweet_private"><a class="jump" href="#@class_tweet_private"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">class</span> Tweet {

    <span class="hljs-keyword">private</span> readonly author: <span class="hljs-built_in">string</span>;
    <span class="hljs-keyword">private</span> readonly text: <span class="hljs-built_in">string</span>;
    <span class="hljs-keyword">private</span> readonly timestamp: <span class="hljs-built_in">Date</span>;

    <span class="hljs-keyword">public</span> <span class="hljs-keyword">constructor</span>(<span class="hljs-params">author: <span class="hljs-built_in">string</span>, text: <span class="hljs-built_in">string</span>, timestamp: <span class="hljs-built_in">Date</span></span>) {
        <span class="hljs-keyword">this</span>.author = author;
        <span class="hljs-keyword">this</span>.text = text;
        <span class="hljs-keyword">this</span>.timestamp = timestamp;
    }

    <span class="hljs-comment">/**
     * @returns Twitter user who wrote the tweet
     */</span>
    <span class="hljs-keyword">public</span> getAuthor(): <span class="hljs-built_in">string</span> {
        <span class="hljs-keyword">return</span> <span class="hljs-keyword">this</span>.author;
    }

    <span class="hljs-comment">/**
     * @returns text of the tweet
     */</span>
    <span class="hljs-keyword">public</span> getText(): <span class="hljs-built_in">string</span> {
        <span class="hljs-keyword">return</span> <span class="hljs-keyword">this</span>.text;
    }

    <span class="hljs-comment">/**
     * @returns date/time when the tweet was sent
     */</span>
    <span class="hljs-keyword">public</span> getTimestamp(): <span class="hljs-built_in">Date</span> {
        <span class="hljs-keyword">return</span> <span class="hljs-keyword">this</span>.timestamp;
    }

}</code></pre>

<p id="@private_public_keywords"><a class="jump" href="#@private_public_keywords"></a>The <code>private</code> and <code>public</code> keywords indicate which fields and methods are accessible only within the class and which can be accessed from outside the class.
<a href="https://www.typescriptlang.org/docs/handbook/2/classes.html#private"><strong><code>private</code></strong></a> is used for the object’s internal state and internal helper methods, while <a href="https://www.typescriptlang.org/docs/handbook/2/classes.html#public"><strong><code>public</code></strong></a> indicates methods and constructors that are intended for clients of the class.
The observers <code>getAuthor()</code>, <code>getText()</code>, and <code>getTimestamp()</code> allow clients to still access a Tweet object’s properties.</p>

<span id="readonly"></span>

<p id="@readonly_used_indicate"><a class="jump" href="#@readonly_used_indicate"></a><mark data-structure-text="readonly" id="^readonly"><a href="https://www.typescriptlang.org/docs/handbook/2/classes.html#readonly"><strong><code>readonly</code></strong></a></mark> is used to indicate which of the object’s instance variables can be reassigned and which can’t.
Note that TypeScript uses <code>readonly</code> only for instance variables, and <code>const</code> only for local and global variables, but both <code>readonly</code> and <code>const</code> mean the same thing: unreassignable.</p>

<div class="panel panel-figure pull-right pull-margin"><object data="figures/retweetLater.svg" alt="retweetLater breaking Tweet's immutability"></object></div>

<span id="rep-exposure-by-returning-mutable-object"></span>

<p id="@but_that-s_not"><a class="jump" href="#@but_that-s_not"></a>But that’s not the end of the story: the rep is still exposed! Consider this perfectly reasonable client code that uses <code>Tweet</code>:</p>

<pre id="@returns_tweet_retweets"><a class="jump" href="#@returns_tweet_retweets"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * @returns a tweet that retweets t, one hour later
 */</span>
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">retweetLater</span>(<span class="hljs-params">t: Tweet</span>): <span class="hljs-title">Tweet</span> </span>{
    <span class="hljs-keyword">const</span> d: <span class="hljs-built_in">Date</span> = t.getTimestamp();
    d.setHours(d.getHours()+<span class="hljs-number">1</span>);
    <span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> Tweet(<span class="hljs-string">"rbmllr"</span>, t.getText(), d);
}</code></pre>

<p id="@retweetlater_takes_tweet"><a class="jump" href="#@retweetlater_takes_tweet"></a><code>retweetLater</code> takes a tweet and should return another tweet with the same message (called a <em>retweet</em>) but sent an hour later. The <code>retweetLater</code> method might be part of a system that automatically echoes funny things that Twitter celebrities say.</p>

<p id="@what-s_problem_here"><a class="jump" href="#@what-s_problem_here"></a>What’s the problem here? The <code>getTimestamp</code> call returns a reference to the same <code>Date</code> object referenced by tweet <code>t</code>.
<code>t.timestamp</code> and <code>d</code> are aliases to the same mutable object. So when that date object is mutated by <code>d.setHours()</code>, this affects the date in <code>t</code> as well, as shown in the snapshot diagram.</p>

<p id="@tweet-s_immutability_invariant"><a class="jump" href="#@tweet-s_immutability_invariant"></a><code>Tweet</code>’s immutability invariant has been broken. The problem is that <code>Tweet</code> leaked out a reference to a mutable object that its immutability depended on. We exposed the rep, in such a way that <code>Tweet</code> can no longer guarantee that its objects are immutable. Perfectly reasonable client code created a subtle bug.</p>

<p id="@can_patch_kind"><a class="jump" href="#@can_patch_kind"></a>We can patch this kind of rep exposure by using <mark data-mark-text="defensive copy" data-structure-text="defensive copy" id="^defensive_copy">defensive copying</mark>, a <a href="../05-designing-specs/#@pattern_defensive_copying">strategy we’ve seen before</a>: making a copy of a mutable object to avoid leaking out references to the rep. Here’s the code:</p>

<pre id="@public_gettimestamp_date"><a class="jump" href="#@public_gettimestamp_date"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">public</span> getTimestamp(): <span class="hljs-built_in">Date</span> {
    <span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> <span class="hljs-built_in">Date</span>(<span class="hljs-keyword">this</span>.timestamp.getTime());
}</code></pre>

<p id="@mutable_types_often"><a class="jump" href="#@mutable_types_often"></a>Mutable types often have a copy constructor that allows you to make a new instance that duplicates the value of an existing instance.
In this case, <code>Date</code>’s copy constructor uses the timestamp value, measured in milliseconds since January 1, 1970. As another example, <code>Map</code>’s copy constructor takes an array of key-value pairs, which can be extracted from the map you want to copy by calling <code>entries()</code>.</p>

<div class="panel panel-figure pull-right pull-margin"><object data="figures/tweetEveryHourToday.svg" alt="tweetEveryHourToday breaking Tweet's immutability"></object></div>

<p id="@so_we-ve_done"><a class="jump" href="#@so_we-ve_done"></a>So we’ve done some defensive copying in the return value of <code>getTimestamp</code>.
But we’re not done yet! There’s still rep exposure. Consider this (again perfectly reasonable) client code:</p>

<pre id="@returns_array_24"><a class="jump" href="#@returns_array_24"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * @returns an array of 24 inspiring tweets, one per hour today
 */</span>
<span class="hljs-function"><span class="hljs-keyword">function</span> <span class="hljs-title">tweetEveryHourToday</span>(<span class="hljs-params"></span>): <span class="hljs-title">Array</span>&lt;<span class="hljs-title">Tweet</span>&gt; </span>{
    <span class="hljs-keyword">const</span> array: <span class="hljs-built_in">Array</span>&lt;Tweet&gt; = [];
    <span class="hljs-keyword">const</span> date: <span class="hljs-built_in">Date</span> = <span class="hljs-keyword">new</span> <span class="hljs-built_in">Date</span>();
    <span class="hljs-keyword">for</span> (<span class="hljs-keyword">let</span> i = <span class="hljs-number">0</span>; i &lt; <span class="hljs-number">24</span>; i++) {
        date.setHours(i);
        array.push(<span class="hljs-keyword">new</span> Tweet(<span class="hljs-string">"rbmllr"</span>, <span class="hljs-string">"keep it up! you can do it"</span>, date));
    }
    <span class="hljs-keyword">return</span> array;
}</code></pre>

<p id="@code_intends_advance"><a class="jump" href="#@code_intends_advance"></a>This code intends to advance a single <code>Date</code> object through the 24 hours of a day, creating a tweet for every hour.
But notice that the constructor of Tweet saves the reference that was passed in, so all 24 Tweet objects end up with the same time, as shown in this snapshot diagram.</p>

<p id="@again_immutability_tweet"><a class="jump" href="#@again_immutability_tweet"></a>Again, the immutability of Tweet has been violated. We can fix this problem, too, by using judicious defensive copying, this time in the constructor:</p>

<pre id="@public_constructor-author_string"><a class="jump" href="#@public_constructor-author_string"></a><code class="language-ts hljs typescript"><span class="hljs-keyword">public</span> <span class="hljs-keyword">constructor</span>(<span class="hljs-params">author: <span class="hljs-built_in">string</span>, text: <span class="hljs-built_in">string</span>, timestamp: <span class="hljs-built_in">Date</span></span>) {
    <span class="hljs-keyword">this</span>.author = author;
    <span class="hljs-keyword">this</span>.text = text;
    <span class="hljs-keyword">this</span>.timestamp = <span class="hljs-keyword">new</span> <span class="hljs-built_in">Date</span>(timestamp.getTime());
}</code></pre>

<p id="@general_you_should"><a class="jump" href="#@general_you_should"></a>In general, you should <strong>carefully</strong> inspect the argument types and return types of all your ADT operations.
If any of the types are mutable, make sure your implementation doesn’t return direct references to its representation.
<strong>Returning a reference to a mutable rep object causes rep exposure.</strong></p>

<p id="@you_may_object"><a class="jump" href="#@you_may_object"></a>You may object that this seems wasteful. Why make all these copies of dates? Why can’t we just solve this problem by a carefully written specification, like this?</p>

<pre id="@make_tweet_param"><a class="jump" href="#@make_tweet_param"></a><code class="language-ts hljs typescript"><span class="hljs-comment">/**
 * Make a Tweet.
 * @param author    Twitter user who wrote the tweet
 * @param text      text of the tweet
 * @param timestamp date/time when the tweet was sent. Caller must never
 *                   mutate this Date object again!
 */</span>
<span class="hljs-keyword">public</span> <span class="hljs-keyword">constructor</span>(<span class="hljs-params">author: <span class="hljs-built_in">string</span>, text: <span class="hljs-built_in">string</span>, timestamp: <span class="hljs-built_in">Date</span></span>) {</code></pre>

<p id="@approach_sometimes_taken"><a class="jump" href="#@approach_sometimes_taken"></a>This approach is sometimes taken when there isn’t any other reasonable alternative – for example, when the mutable object is too large to copy efficiently. But the cost in your ability to reason about the program, and your ability to avoid bugs, is enormous.
In the absence of compelling arguments to the contrary, it’s almost always worth it for an abstract data type to guarantee its own invariants, and preventing rep exposure is essential to that.</p>

<p id="@even_better_solution"><a class="jump" href="#@even_better_solution"></a>An even better solution is to prefer immutable types.
If <code>Date</code> were an immutable type, then we would have ended this section after talking about <code>public</code> and <code>private</code>.
No further rep exposure would have been possible.</p>
