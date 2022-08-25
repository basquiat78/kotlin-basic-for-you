# 흐름을 제어하라

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/flowcontrol/kotlin/FlowControl.kt)

딱히 떠오르는 단어가 생각나지 않는다.

어째든 흐름 제어라는 것은 프로그래밍에서 가장 기본적이기 때문에 여기서 이것을 알아보고자 한다.

# if expression

***코틀린은 삼항 연산자가 없어??????***

처음에 황당했었다.

그렇다면 삼항 연산자가 뭔데라는 질문을 던질 수 있는데 여러분이 자바스크립트나 이미 잘 쓰고 있는 것이다.


```java
public class Main {
    public static void main(String[] args) {
        int value = 0;
        String str = "test";
        if("test".equals(str)) {
            value = 10;
        } else {
            value = 5;
        }
        System.out.println(value);
        // expected 10
    }
}
```

이런 코드가 있다면

```java
public class Main {
    public static void main(String[] args) {
        int value = "test".equals(str) ? 10 : 5;
        // expected 10
    }
}
```
이게 바로 삼항 연산자이다.

코드는 잘 알것이다. 참이면 value는 10이고 아니면 5라는 아주 단순한 형식이지만 실제로 코드를 줄이는데 한 몫하는 표현식이다.

그럼 진짜 코틀린에서 저게 안된다고?

```Kotlin
fun main() {
    val str = "test"
    val value = "test".equals(str) ? 10 : 5
    println(value)
}
```
과연 될것인가?

아마도 'Unexpected tokens (use ';' to separate expressions on the same line)'이런 문구를 보여주며 컴파일 에러를 보게 된다.

아니 같은 라인에 ;을 사용해서 표현식을 구분하라고?

그래서 여기저기 ';'를 붙여본들 어림도 없다!!!

코를린에서는 이것을 표현식으로 제공한다.

```Kotlin
fun main() {
    /*
     	val str = "test"
        val value = "test".equals(str) ? 10 : 5
        println(value)
        error msg -> Unexpected tokens (use ';' to separate expressions on the same line)
    */
    val str = "test"
    var oldFashioned = 0
    // 전통적인 방식으로 작성해 본다.
    if("test".equals(str)) {
        oldFashioned = 10
    } else {
        oldFashioned = 5
    }
    println("oldFashioned is $oldFashioned")

    //val value = if("test".equals(str)) {
    //    10 
    //} else {
    //    5
    //}
    // 코드를 더 간략하게
    val newType = if("test".equals(str)) 10 else 5
    println("newType is $newType")

    // if else.. else if
    val num = 4
    val numCheckResult: String = if(num > 10)
        "5보다 큰 숫자가 왔네요"
    else if( num >= 5 && num <= 10)
        "5와 10사이의 숫자가 옴"
    else
        "5보다 작은 숫자가 옴"

    println("num check result is $numCheckResult")
}
```
바로 저런 식으로 사용할 수 있다. 앞서 배운 scope function과 좀 비슷하긴 하지만 코드가 상당히 간결해져서 삼항연산자랑 별반 다를게 없다.

물론 '널 안전성'을 활용해서 takeIf나 takeUnless로 삼항연산자 비스무리하게 할 수는 있다.

```Kotlin
fun main() {
    var takeIfStr = "test"
    //var takeIfStr = "test11"
    val valueTakeIfStr = takeIfStr.takeIf { "test".equals(it) }?.let { 10 } ?: 5
    println("valueTakeIfStr is $valueTakeIfStr")
}
```
Safe Call (?.)과 엘비스 연산자로 처리 가능하다. 

하지만 if..esle.. else if같이 중첩되는 코드를 짠다고 생각한다면 차라리 if expression으로 처리하는게 더 좋아보인다.

# when

java의 switch와 비교해 볼 수 있다.

[java switch 변천사](https://medium.com/@javatechie/the-evolution-of-switch-statement-from-java-7-to-java-17-4b5eee8d29b7)

[java-switch-pattern-matching In Baeldung](https://www.baeldung.com/java-switch-pattern-matching)

위에 글을 살펴보면 요지는 방식의 변천사를 설명하고 있다.

어떤 조건이 만족하면 break를 통해서 해당 로직을 완료하게 된다.

그러다가 break를 앞에 두고 로직이나 반환값을 뒤에 두거나 break대신 yield 키워드로 변경되었다는 것을 알 수 있다.

하지만 break키워드를 사용하지 않는다면 모든 케이스에 대해 실행을 해버린다~~~

물론 의도적으로 사용할 수 있겠지만 그렇게 사용해 본적이 없어서 딱히 잘 모르겠다.

어째든 baeldung사이트의 내용을 보면 switch 자제를 리턴할 수 있다.

코틀린의 경우 when 역시 if문과 같은 표현식이다.

그렇다면 기본적인 코틀린의 코드를 살펴보자.

```Kotlin
fun main() {
    // old fashioned
    // 코드 짜기 귀찮아서 그냥..uppercase()
    val genre = "jazz"
    //val genre = "jazz123456"
    var upperCase: String = ""
    when(genre) {
        "jazz"   -> upperCase = genre.uppercase()
        "rock"   -> upperCase = genre.uppercase()
        "hiphop" -> upperCase = genre.uppercase()
        else -> upperCase = "ETC"
    }
    println("old fashioned upperCase is $upperCase")

    // 리턴하는 when 표현식
    var upperCaseGenre = when(genre) {
        "jazz"   -> genre.uppercase()
        "rock"   -> genre.uppercase()
        "hiphop" -> genre.uppercase()
        else -> "ETC"
    }
    println("kotinStyle is $upperCaseGenre")

    // 자바에서 가장 흔히 사용하는 열거형 enum으로 구현
    // 다만 자바처럼
    // when(genreCode) {
    //    JAZZ -> "재애즈"
    // }
    // 같이 사용할 수 없다.
    var genreCode = Genre.ROCK
    var result = when(genreCode) {
        Genre.JAZZ -> "째애즈"
        Genre.ROCK -> "롹!"
        Genre.HIPHOP -> "랩!!!!"
        else -> "기타 등등"
    }
    println("kotinStyle using ENUM is $result")

    // 자바처럼 몇 개의 case를 묶어서 처리하는 방식
    var otherResult = when(genreCode) {
        Genre.JAZZ, Genre.ROCK, Genre.HIPHOP -> "대중 음악"
        else -> "월드 뮤직"
    }
    println("kotinStyle using ENUM is $otherResult")

}

/**
 * 음악 장르
 */
enum class Genre {
    JAZZ,
    ROCK,
    HIPHOP,
    ETC
}

```
단 java의 경우에는 enum사용시 Genre를 생략할 수 있지만 이건 코틀린에서 안된다.

# for expression

for loop를 사용하기 위해서는 코틀린의 _Ranges.kt이 제공하는 Ranges 표현 방식을 알아야 한다.

기본적으로 어떤 시작점에서 끝점을 사이에 두고 '..'으로 표현하는 범위 연산자로 불리는 방식이 기본적인 표현 방식인데 코드로 보면

```
basic range syntax

1..10 // 1부터 10까지 앞과 뒤의 값을 포함한 범위

'a'..'z' // a to z

'A'..'Z' // A to Z

```

그렇다면 코드로 한번 살펴보자.


```Kotlin
fun main() {
    /*
        이건 안되는 걸로...
        for(i: Int = 0 ; i < 10; i++) {
            println(i)
        }
    */
    // 1부터 10까지
    for(index in 1..10) {
        println("index is $index")
    }

    // api를 이용 -> 1부터 10까지
    for(index in 1.rangeTo(10)) {
        println("index is $index")
    }

    // until의 경우에는 (index = 0; index < 11; i++)
    // 즉 index는 10까지 찍힌다.
    for(index in 1 until 11) {
        println("index is $index")
    }

    // Char 위와 동일
    for(char in 'a'..'f') {
        println("char is $char")
    }

    for(char in 'a'.rangeTo('f')) {
        println("char is $char")
    }

    // 한글은 안되는 걸로
    for(char in 'ㄱ' until 'ㄹ') {
        println("char is $char")
    }

    for(char in 'A'..'F') {
        println("char is $char")
    }

    for(char in 'A'.rangeTo('F')) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'A' until 'G') {
        println("char is $char")
    }
}
```
코드를 보면 1..10은 코틀린의 Range API를 사용해서 1.rangeTo(10)로 표현할 수 있으며 1부터 11전까지라는 표현방식의 '1 until 11'처럼 표현이 가능하다.

until은 범위를 잘 살펴보면 될것같고 위 코드를 실행하면 3개 전부다 같은 결과를 보여줄 것이다.

```Kotlin
fun main() {
    // 난 홀수만 찍고 싶은데?
    for(index in 1 until 11 step 2) {
        println("index is $index")
    }

    // 퐁당퐁당
    for(char in 'a' until 'g' step 2) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'A' until 'G' step 2) {
        println("char is $char")
    }
}
```
코틀린 Range에 step이라는 녀석이 존재한다.      

즉 step 이후 들어온 만큼 건너띄어서 실행하는 방식이다.

따라서 1부터 10가찌 루프를 돈다고 할 때 step를 2로 설정하면 홀수만 나올것이고 Char라면 퐁당퐁당이지 뭐.....

_Ranges.kt
```Kotlin

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 *
 * If the [to] value is less than or equal to `this` value, then the returned range is empty.
 */
public infix fun Int.until(to: Byte): IntRange {
    return this .. (to.toInt() - 1).toInt()
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public infix fun IntProgression.step(step: Int): IntProgression {
    checkStepIsPositive(step > 0, step)
    return IntProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
}

/**
 * Returns a progression from this value down to the specified [to] value with the step -1.
 *
 * The [to] value should be less than or equal to `this` value.
 * If the [to] value is greater than `this` value the returned progression is empty.
 */
public infix fun Int.downTo(to: Int): IntProgression {
    return IntProgression.fromClosedRange(this, to, -1)
}
```

그나저나 자바처럼 10에서 1로 역으로 가는 경우를 생각해서

```Kotlin
fun main() {
    /*
        택도 없죠?
        코드가 동작조차 않하죠?
        for(index in 10..1) {
            println("index is $index")
        }
    */
}
```
택도 없다!

위 _Ranges.kt에서는 타입별로 각 속성 함수들을 제공하고 있는데 그중 downTo를 살펴보자.

딱 보니 주어진 숫자 또는 Char에서 downTo로 넘어온 숫자 또는 Char로 -1씩 감소시킨다는 것을 알 수 있다.

즉 '10 downTo 1'이라고 하면 10부터 1까지 -1로 줄면서 loop를 돌것이고 '10 downTo 5'로 하면 10부터 5까지 -1 줄면서 loop를 돌것이다.


```Kotlin
fun main() {
    /*
        택도 없죠?
        코드가 동작조차 않함
        for(index in 10..1) {
            println("index is $index")
        }
    */
    // 10부터 5까지
    for(index in 10 downTo 5) {
        println("index is $index")
    }

    // z to s
    for(char in 'z' downTo 's') {
        println("char is $char")
    }

    // Z to S
    for(char in 'Z' downTo 'S') {
        println("char is $char")
    }
}
```

_Ranges.kt의 for안에 들어가는 프로퍼티에 대한 스펙을 보면 다음과 같이 표현이 가능하다.       


```Kotlin
fun main() {
    // 10부터 5까지
    for(index in 10.downTo(5)) {
        println("index is $index")
    }

    // z to s
    for(char in 'z'.downTo('s')) {
        println("char is $char")
    }

    // Z to S
    for(char in 'Z'.downTo('S')) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'A'.until('G').step(2)) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'a'.rangeTo('f').step(2)) {
        println("char is $char")
    }

    // 난 홀수만 찍고 싶은데?
    for(index in 1.until(11).step(2)) {
        println("index is $index")
    }

}
```
어떤 방식이 좋아보이는지는 개인취향이라고 해두자.

어떤 분은 fluent API를 사용해서 유창하게 보이고 싶을 수도 있고 난 그런거 모르겠고 '.'같은 안찍고 '(' 와 ')'거 안쓰고 타이핑 편하게 쓰고 싶으면 그런대로 맘가는대로~

물론 배열이나 리스트도 마찬가지로 사용할 수 있다.

간략하게 여기서는 배열을 하나 생성해서 foo loop를 해보자.

```Kotlin
fun main() {
    val array = arrayOf(1, 2, 3, 4, 5, 6)
    for(intValue in array) {
        println("intValue is $intValue")
    }
    
    // 역으로 소팅해서
    for(intValue in array.reversed()) {
        println("intValue is $intValue")
    }
}
```

하는 김에 끝까지 가보자

```Kotlin
fun main() {

    val list = arrayListOf(Person("John", 22), Person("Jane", 21))
    for(person in list) {
        // 앞서 배운 스코프 함수 let도 써보고 하자
        person.let {
            println("name is ${it.name} and age is ${it.age}")
        }
    }
}

class Person(
	val name: String,
    val age: Int,
)
```

# while expression
사실 while은 자바와 다르지 않다. 그냥 같다고 보면 된다.

```Kotlin
fun main() {
    var whileIdx = 0;
    // 5보다 작을 떄까지 돌아라!
   	while(whileIdx < 5) {
		println("whileIdx is $whileIdx")
  		whileIdx++
   	}
    
    var startIdx = 5;
    // 5부터 시작해서 0이 될때까지 돌아라
   	while(startIdx > 0) {
  		println("startIdx is $startIdx")
        startIdx--
   	}
}
```
자바랑 다를게 없다.

do..while도 자바랑 똑같다

```Kotlin
fun main() {
    var doIndex = 0;
    // 5보다 작을 떄까지 돌아라!
	do {
  		println("doIndex is $doIndex")
      	doIndex++
	} while(doIndex < 5)
    
    var doStartIndex = 5;
    // 5부터 시작해서 0이 될때까지 돌아라
	do {
  		println("doStartIndex is $doStartIndex")
      	doStartIndex--
	} while(doStartIndex > 0)
}
```
자... 코틀린 관련 Control Flow 부분을 훝어 봤다.

# 여기서 잠깐!!!

아직 끝난거 아냐~

for와 while의 경우는 자바처럼 어떤 조건을 마주할 떄 반복을 빠져나오거나 계속 진행시킬 수 있다.

키워드는 자바와 똑같이 'break'와 'continue'이다.

예제는 for문 하나만 해보자면

```Kotlin
fun main() {
    val stopIndex = 3
    // index가 3이 되면 3까지 찍고 루프를 빠져나오기 때문에 3까지만 찍힐 것이다.
    for(index in 1..10) {
        println("index is $index")
        if(index == stopIndex) {
            break;
        }
    }
}
```
근데 이런 경우를 한번 살펴보자.

```Kotlin
fun main() {
    // break가 걸리면 전체 루프를 빠져나올줄 알았지?? 응 아니야!
    for(idx in 1..4) {
        println("idx is $idx")
        for(jdx in 1..4) {
            println("idx = $idx / jdx = $jdx")
            if(idx == 2) {
                break
            }
        }
    }

}

```
위 코드를 살펴보자.

for안에 for가 중첩이 되어 있고 중첩된 for에서 idx가 2라면 break를 걸어두었다.

관점에 따라 예상하기로는 break를 만나면 전체 루프를 나올 것이라고 생각할 수 있지만 실제로 그렇진 않다.


```
result
idx is 1
idx = 1 / jdx = 1
idx = 1 / jdx = 2
idx = 1 / jdx = 3
idx = 1 / jdx = 4
idx is 2
idx = 2 / jdx = 1
idx is 3
idx = 3 / jdx = 1
idx = 3 / jdx = 2
idx = 3 / jdx = 3
idx = 3 / jdx = 4
idx is 4
idx = 4 / jdx = 1
idx = 4 / jdx = 2
idx = 4 / jdx = 3
idx = 4 / jdx = 4
```
결과를 보면 idx가 2일 때 break를 만나기 전 'idx = 2 / j = jdx'이 로그만 찍고 안쪽 for문만 루프를 빠져나왔다.      

즉 내부의 중첩된 for에서 break가 걸렸을 뿐이다.      

그렇다면 저 중첩된 for문 안에 break를 만날때 전체 루프를 빠져나오고 싶을 수도 있다.

코틀린에서는 Labeled라 해서 독특한 어노테이션 비슷한 것을 제공한다.

```Kotlin
fun main() {
    // 상위 바깥에 있는 for에 이 녀석은 outerLoop라고 라벨링을 한다.
    outerLoop@ for(idx in 1..4) {
        println("label idx is $idx")
        // 루프 안쪽에 중첩 루프에는 innerLoop라고 라벨링을 한다.
        innerLoop@ for(jdx in 1..4) {
            println("label idx = $idx and label jdx = $jdx")
            if(idx == 2) {
                // break가 걸리면 sign된 라벨로 전파한다.
                break@outerLoop
            }
        }
    }
}
```
좀 독특하긴 하지만 일종의 라벨링을 하고 break가 걸렸을때 해당 이벤트를 break다음에 사인된 라벨링까지 전파한다는 의미로 보면 된다.

이렇게 처리하게 되면

```
result
label idx is 1
label idx = 1 / label jdx = 1
label idx = 1 / label jdx = 2
label idx = 1 / label jdx = 3
label idx = 1 / label jdx = 4
label idx is 2
label idx = 2 / label jdx = 1
```
보면 전체 루프를 빠져나오게 된다.

continue도 마찬가지로 이 Labeled를 사용해서 똑같은 방식으로 처리할 수 있는 방법을 제공하게 된다.      

사실 많이 사용될지 아닐지는 잘 모르겠지만 한번 소개는 해봐야 할거 같아서 마지막에 추가해 봤다.         

# At a Glance