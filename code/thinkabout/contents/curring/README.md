# 커리한 함수? 커링 함수?

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/thinkabout/kotlin/Curring.kt)

2015년으로 확실히 기억한다.

당시 왜 함수형 프로그래밍을 공부할려고 했는지 지금도 기억이 안나는데 이와 관련 `스칼라`를 공부하던 떄에 몇 가지 개념들로 인해서 잠시 손을 놓았던 기억이 새록새록난다.

그 이유는 약간 수학적인 개념도 포함하고 있고 자바만 해온 나에게 있어서 `HOF(Higher Order Function)`, 고차 함수나 `클로저(Closure)`에 대한 이해도가 많이 부족했기 때문이다.

게다가 몇 가지 개념들은 지금까지 해왔던 자바와는 전혀 다른 형태여서 아마 혼란이 있었던 걸로 기억한다.

특히 나같은 `범인(凡人)`은 기존의 익숙함에서 벗어나 어떤 새로운 패러다임을 받아드리기에는 역부족이 아니었나 생각해 본다.

이 시기를 기억하는 이유는 당시 자바와 코틀린, `스칼라` - 그렇다고 잘하는 건 아니다. `스칼라`도 안한 지 오래되서.. -, 그리고 대세가 되어가고 있던 `NodeJs`때문이었다.

또한 당시 어렵게 느껴졌던 하이버네이트를 다시 시작하게 된 계기가 된 `JPA의 바이블`이라 할 수 있는 김영한님의 책이 이 시기에 나오기도 했다.

이 때 `jQuery`에 의존하면서 단지 보조적인 역할로만 사용하다가 모던 자바스크립트도 접하게 되면서 일종의 터닝 포인트가 되던 해였기 때문이다.

어쨰든 내 자신이 함수형 프로그래밍의 고수는 아니지만 그래도 어느 정도 이런 개념들을 익혀가면서 조금씩 알아갔던 내용들을 좀 얘기해 볼까 한다.

구글에 검색해도 많은 자료들이 많기 때문에 구글링으로 찾아보셔도 된다.

그리고 이 내용은 단순하게 코틀린에만 국한하지 않기를 바란다.

함수형 프로그래밍이 가능하다면 그 어떤 언어에서도 적용되는 내용이기 때문이다.

# 벌써부터 머리가 아파진다?

`커링(curring)`이라는 말은 하스켈 커리(Haskell Curry)라는 수학자의 이름을 딴 것이다.

실제로는 모세 쉔핀켈(Moses Schonfinkel)이라는 사람이 처음 만든 것이라 `쉔필켄`이라고도 불리기도 하는데 이런 이야기를 왜하냐?

궁금할 것이다.

수학자 이름을 딴 것이라는 것에 힌트가 있는데 이것은 일단 수학적인 개념이라는 것이다.

일단 이 커링이라는 개념은 프로그래밍에 국한된 내용은 아니다.

수학적인 개념에서 함수라는 것은 좀 어려운 말로 하자면 정의역과 공역사이에서 어떤 조건을 만족시키는 대응 관계로 설명한다.

여기서 정의역이라는 것은 영어로 우리가 너무나 잘 알고 있는 `도메인(Domain)`이고 공역은 `코도메인(Codomain)`이다.

마치 `relation`과 `corresponding`이 떠오르는 대목이기도 한데 어째든 이러한 함수의 정의에는 다음과 같은 독특한 특징이 있다.

1. 함수는 둘 이상의 정의역과 공역의 대응 관계가 아니다. 따라서 함수는 인자를 여러개 받을 수 없다.
2. 여러개의 인자를 받는 것처럼 보이지만 이것은 튜플로 이뤄진 하나의 인자이다.

이 내용을 처음 봤을 때는 이게 뭔 소리여? 이랬던 기억이.....

아무튼 말이 어려운데 수학적인 기호로 `더하기`를 함수로 표현해 보자.

```
f((x, y)) = x + y

f(x, y) = x + y
```
괄호 안에 튜플로 x와 y를 묶어서 표현한 것이 보이는가?

함수의 정의에 의하면 2번에 해당하는 의미이다.

통상적으로 저것은 불편하기 때문에 아래 표현처럼 생략해서 사용하게 된다.

## Curried Form

`커리한 형태` 또는 `커리된 형태`라고 표현하는게 이게 대체 무슨 말일까?

이것을 이해하기 위해서는 `HOF`, 즉 고차 함수에 대해서 알아야 한다.

쉽게 말하자면 함수를 일급 객체로 취급하는 언어에서는 함수가 함수를 반환할 수 있다.

그리고 함수를 인자로 넘길수도 있다.

```javascript
function test(value) {
    return value + 10
}

// 함수를 넘길 수 있다.
function myFunction(numValue, callback) {
    return callback(numValue)
}

myFunction(10, test);

//result
// 20
```

그렇다면 위에 표현식은 분리해서 표현이 가능하다.

```
f(x)(y) = x + y

f(x) = g

g(y) = x + y

// 1과 2를 더한다면

f(1)(2) = g(2) = 1 + 2 = 3
```

여기서 `f(x)(y)`를 `f(x, y)`의 `Curried Form`, 즉 커리한 형태라고 부른다.

이렇게 튜플 형식의 인자에 대한 함수를 해당 함수가 함수를 반환하도록 바꾸는 것을 `커링`이라고 한다.

그리고 그렇게 변환된 함수를 `Curried Function`, 해석하자면 `커리한 함수`라고 할 수 있을 것이다.

그럼 이걸 실제로 자바스크립트에서 한번 살펴보자.

크롬에서 개발자 도구를 이용해 한번 테스트해보길 바란다.

```javascript
function f(x) {
    return function(y) {
        return x + y
    }
}

// 꼭 g일 필요는 없지만 위의 공식을 예제로 g라고 이름을 주자.
g = f(1)

g(2)
// result
// 3
```
당연히 이것은 모던 자바스크립트를 아신다면 그냥 `arrow`형식으로 표현이 가능하다는 것을 알 것이다.

```javascript
let f = x => y => x + y

let g = f(1)

g(2)

// result
// 3
```

`let`보다는 불변으로 `const`로 두는게 나을 거 같다.

# 음? 그럼 자바는 안돼???

이런 의문도 들것이다.

위 코드는 다음과 같이 `functional interface`중 하나인 `Function<T, R>`을 사용하면 된다.

```java
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        // FM대로 해도 상관없지만 불편하니 좀 더 간결하게 아래 방식으로 처리해도 된다.
        //Function<Integer, Function<Integer, Integer>> f = (x) -> (y) -> x + y;
        Function<Integer, Function<Integer, Integer>> f = x -> y -> x + y;
        Function<Integer, Integer> g = f.apply(1);
        System.out.println(g.apply(2));
    }
    
}
```

`Function<Integer, Function<Integer, Integer>>`라는 표현이 좀 많이 불편해 보인다.

이럴바엔 차라리 `Function`을 확장해서 사용해 하나를 정의해 보는것도 나쁘지 않다.

관례적인 이름을 줘서 하나의 인터페이스를 정의해보자.

```java
import java.util.function.Function;

public class Main {

    interface TriFunction<T, R> extends Function<T, Function<T, R>> {

    }

    public static void main(String[] args) {
        // FM대로 해도 상관없지만 불편하니 좀 더 간결하게 아래 방식으로 처리해도 된다.
        //TriFunction<Integer, Integer> f = (x) -> (y) -> x + y;
        
        // 만일 해당 add가 반환하는 것인 Function<T, R>이라는 것을 구분짓고자 한다면 아래처럼 묶어서 가시성을 높일 수 있다.
        // 1.TriFunction<Integer, Integer> f = (x) -> ((y) -> x + y);
        // 2.TriFunction<Integer, Integer> f = x -> ((y) -> x + y);
        // 3.TriFunction<Integer, Integer> f = x -> (y -> x + y);
        
        TriFunction<Integer, Integer> f = x -> y -> x + y;
        Function<Integer, Integer> g = f.apply(1);
        System.out.println(g.apply(2));
    }
}
```

위 코드가 `Function`의 제네릭 타입에 또 자신을 넣는게 불편해 보일 수 있느니 다음과 같이도 사용할 수 있다.

```java
public class Main {

    private static Function<Integer, Integer> f(int x) {
        return y -> x + y;
    }
    
    public static void main(String[] args) {
        Function<Integer, Integer> g = f(1);
        System.out.println(g.apply(2));
    }

}

```
만일 위 코드를 이해하지 못했다면 여러분들은 모던 자바에 익숙하지 못한 것이다.

<img alt="이미지" height="300" src="https://w.namu.la/s/4de4ba10f9ed35b70cabf25743074e5d6fcd98b0f80c11e933101a6d03bf6e82d007fee3caa899bc6090d723d1fc98410439baecb2850b8ee25247895161fe73beb0aa56311b50db4c71ee813440145b3076559d0cf0daa107fa89248dfb71353bc1ee461001c85ffdf8955f217f891e" width="500"/>

어째든 코드량이 좀 길거나 많긴 해도 자바스크립트랑 똑같다.

코틀린은요?

```kotlin
fun main() {
    val f: (Int) -> (Int) -> Int = { x ->
        { y ->
            x + y
        }
    }
    val g = f(1)
    println(g(2))
    
    
}
```

물론 `typealias`로 다음과 같이 별칭을 주고 코드를 좀 더 간결하게 가져갈 수 있다.

```kotlin
typealias TriFunction<T> = (T) -> (T) -> T

fun main() {
    val f: TriFunction<Int> = { x ->
        { y ->
            x + y
        }
    }
    val g = f(1)
    println(g(2))
}
```

아니면 그냥 다음처럼 람다안에 어떤 타입의 변수를 받겠다고 명시하면 더 간략해진다.

```kotlin
fun main() {
    val f = { x: Int -> { y: Int -> x + y } }
    val g = f(1)
    println(g(2))
}
```

물론 함수로

```kotlin
fun main() {
    val g = f(1)
    println(g(2))
}

fun f(x: Int) = { y: Int -> x + y }
```
결국 함수를 반환받고 처리하는 방식이다.

당연히 지금까지의 코드를 이해하시는 분들이라면 `클로저(Closure)`개념을 이미 아시는 분이실 것이고 만일 모르신다면 구글링 해보시길 바란다.

물론 `Clojure`라는 언어와 혼동하지 않으시리라 생각한다.

자 이제부터 위에 코드는 자바는 좀 다르긴 해도 다음과 같이 `커리한 형태`로 표현이 된다.

```
f(1)(2)

java
f.apply(1).apply(2)
```
마치 우리가 앞서 언급한 함수의 정의에 딱 부합하는 형식이 아닌가?

마치 모든 인자를 하나씩 순차적으로 계산해가는 형태를 지니고 있다.

사실 이런 초간단 덧셈 로직을 `커리한 함수`로 변경하는 것은 의미가 없어보인다.

왜냐하면 실제 `커리한 함수`는 인자의 순서가 중요하다고 설명한다.

맨 앞의 인자일수록 변동가능성이 적고 뒤에 있는 인자일 수록 변동가능성이 높다고 본다.

그 이유는 바로 아래!

# 게으른 평가

자바스크립트의 코드를 다시 한번 살펴보자.

```javascript
function f(x) {
    return function(y) {
        return x + y
    }
}

g = f(1)

g(2)
// result
// 3
```
위 코드에서 `f(1)`은 호출시 바로 값을 반환하지 않는다.

예를 들면

```javascript
console.log(f(1))

//result
//ƒ (y) {
//    return x + y
//}
```
어떤 값이 계산되서 나올것 같지만 실제로는 함수하나가 그냥 로그에 통채로 찍힌다.

그러면 자바는 어떨까?

```java
public class Main {

    interface TriFunction<T, R> extends Function<T, Function<T, R>> {

    }

    public static void main(String[] args) {
        TriFunction<Integer, Integer> f = (x) -> ((y) -> x + y);
        Function<Integer, Integer> g = f.apply(1);
        System.out.println(g);
    }
}
//result
//Main$$Lambda$15/0x0000000800066c40@7d417077
```
어랏? 마찬가지로 자바 람다의 내부동작에 의해서 `invokedynamic`으로 `opcode코드`로 표현되는 걸로 알고 있는데 그 주소값이 튀어나온게 아닐까 생각한다.

코틀린도 마찬가지이다.

```kotlin
fun main() {
    val g = f(1)
    println(g)
}

fun f(x: Int) = { y: Int -> x + y }

// result
//(kotlin.Int) -> kotlin.Int
```
보면 `(Int) -> Int` 이렇게 정의한 익명 함수가 찍히는 것처럼 보인다.

그럼 이 게으른 평가가 도대체 뭔데? 라는 의문이 들지도 모른다.

만일 여러분이 어떤 로그를 찍는데 그 로그의 형태를 떄에 따라서 가져가고 싶은 경우가 있다.

예를 들면 어떤 상황에는 `[Yesterday][Info] your message`라든가 또는 `[Today][Debug] your message`라든가 하는 식으로 말이다.

만일 한땀한땀 코드로 만들어 간다고 해보자.

```kotlin
enum class DayType {
    Yesterday,
    Today,
    Tomorrow
}

enum class LogType {
    Info,
    Debug,
    Waring,
    Error
}

fun main() {

    val message = "이것은 메세지다."

    // 어제 info 메세지 찍기
    printLog(DayType.Yesterday, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)
    // 어제 info 메세지 찍기
    printLog(DayType.Yesterday, LogType.Info, message)
    // 내일 info 메세지 찍기
    printLog(DayType.Tomorrow, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)
    
}

fun printLog(dayType: DayType, logType: LogType, message: String) {
    println("[${dayType.name}][${logType.name}] $message")
}
```
위 코드를 그냥 보면 전혀 문제가 없는 코드이다.

하지만 가만히 살펴보면 몇 가지 패턴이 보인다.

눈치가 빠른 분들이라면 `[][]` 이 부분이 반복되는 패턴이 나온다는 것을 알 수 있다.

지금 이 `printLog`함수는 들어온 인자에 대해서 즉각적인 계산이 이뤄져 하나의 로그를 찍는다.

하지만 이 넘을 `커리한 함수`로 변경한다면 `HOF`의 특징을 이용해서 뭔가 재사용할 수 있는 여지가 보이지 않을까?

```kotlin
enum class DayType {
    Yesterday,
    Today,
    Tomorrow
}

enum class LogType {
    Info,
    Debug,
    Waring,
    Error
}

fun main() {

    val message = "이것은 메세지다."

    val yesterdayInfo = printLog(DayType.Yesterday)(LogType.Info)
    val todayInfo = printLog(DayType.Today)(LogType.Info)
    val tomorrowInfo = printLog(DayType.Tomorrow)(LogType.Info)

    // 어제 info 메세지 찍기
    yesterdayInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)
    // 어제 info 메세지 찍기
    yesterdayInfo(message)
    // 내일 info 메세지 찍기
    tomorrowInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)

}

fun printLog(dayType: DayType) = {
    logType: LogType ->
        { message: String ->
            println("[${dayType.name}][${logType.name}] $message")
        }
}
```
`커리한 함수`에서 인자의 순서가 중요하다는 의미는 바로 이런 고정값에 대해서 `게으른 평가`를 적용해 볼 수 있다.

물론 인자의 순서가 중요하지 않을수도 있다. 그것은 함수를 설계하는데 있어서 이런 부분을 고려할지 안할지는 개발자의 몫이기 때문이다.

# Partial Applied Function

일명 `부분 적용 함수`라고 부르는 것이다.

사실 처음 이 개념을 알게 된 것은 `스칼라`를 통해 함수를 다루는 방법을 배우면서 접했던 내용이다.

`스칼라`라에서는 이것을 언어 차원에서 제공하지만 코틀린에서는 이런 것을 제공하지 않기 때문에 한번 만들어 보는 시간을 가져보자.

만일 변하지 않는 어떤 상수가 주어지게 된 상황에서 새로 들어온 값을 계산해야하는 경우가 생길 수 있다.

아래 코드는 스칼라 방식으로 한번 만들어 본것이다.

하지만 `IDE`나 `코틀린 놀이터`에서는 컴파일 오류가 발생한다는 점 미리 언급한다.

```kotlin
fun main() {
    val add: (Int, Int) -> Int = { x, y -> x + y }
    val constantValue = 10
    val partial = add(_, constantValue)
    println(partial(10))
    println(partial(11))
}
```
`스칼라`에서는 언어 차원에서 `_`로 표현된 부분은 일종의 인자값을 누락시킨 표현이 들어오게 되면 계산을 하지 않고 일종의 `게으른 계산`을 적용하게 된다.

그리고 나머지 인자값이 들어오게 되면 계산을 하게 된다.

그렇다면 코틀린에서는 저걸 어떻게 해볼 수 있을까?

```kotlin
fun main() {
    //val add: (Int, Int) -> Int = { x, y -> x + y }
    val add = { x: Int, y: Int -> x + y }
    val constantValue = 10
    val partial: (Int) -> Int = { a -> add(a, constantValue) }
    println(partial(10))
    println(partial(11))
}
```
아마도 저렇게 람다식으로 처리할 수 있을 것이다.

예제 자체가 덧셈에 대한 초간단 코드라 크게 감이 오지 않지만 재사용이 가능한 함수를 만들 수 있다는 점이다.

한줄 코드가 추가되긴 했지만 변하지 않는 변수에 대해서 다음과 같이 코드를 짜는 것보다는 간결하다는 것이다.

위 코드는 다음과 같이 확장 함수를 이용해서 좀 더 우아한 방식으로 처리 할 수 있다.

```kotlin
typealias IntFunction = (Int, Int) -> Int

fun main() {
    val add: IntFunction = { x, y -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun IntFunction.partialApplied(p: Int): (Int) -> Int {
    return { v -> this(v, p) }
}
```
물론 저렇게 고정적인 `Int`에 대해서 코드를 작업하긴 했지만 저 부분은 필요하다면 요구에 맞게 제네릭스로 처리해서 사용할 수 있을 것이다.

```kotlin
typealias Function<T, U, R> = (T, U) -> R

fun main() {
    val add: Function<Int, Int, Int> = { x, y -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun <T, U, R> Function<T, U, R>.partialApplied(p: U): (T) -> R {
    return { v -> this(v, p) }
}
```
이것도 아니면 `typealias`를 사용하지 않고 풀어서 작업해도 상관없을 것이다.

```kotlin
fun main() {
    //val add: (Int, Int) -> Int = { x, y -> x + y }
    val add = { x: Int, y: Int -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun <T, U, R> ((T, U) -> R).partialApplied(p: U): (T) -> R {
    return { v -> this(v, p) }
}
```

애초에 `constantValue`는 고정된 값이고 실제 결과값은 들어오는 하나의 인자에 의해 결정되어진다.

즉 그 부분을 `부분 적용 함수`를 활용해 `게으른 평가`를 적용할 수 있고 재사용이 가능하다.

어째든 이런 방식을 적용하지 않는다면 아마도 아래와 같은 코드가 가장 일반적일 것이다.

```kotlin
fun main() {
    val add: (Int, Int) -> Int = { x, y -> x + y }
    val constantValue = 10
    println(add(10, constantValue))
    println(add(11, constantValue))
}
```

# At a Glance
지금까지 함수형 프로그래밍에서 다루는 `커링`에 대해서 알아 봤다.

아마도 다음에는 이것을 바탕으로 `함수 합성`과 `고차 함수`, `다형적 HOF`에 대한 내용을 다뤄 볼 생각이다.

시간이 된다면 `arrow-io`를 이용한 `커리한 함수`도 다뤄볼 생각이다.

자바스크립트는 `lodash`를 이용하면 쉽게 다룰 수 있는데 이 부분도 같이 다뤄볼 생각이다.