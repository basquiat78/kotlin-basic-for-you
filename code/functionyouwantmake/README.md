# 함수를 다루는 사용설명서

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/functionyouwantmake/kotlin/FunctionYouWantMake.kt)

함수냐 메소드냐?

이게 자바를 오래하다보니 메소드가 입에 딱 붙어서 그런지 메소드라는 말을 쓰게 되는데 그냥 입가고 손가는데로 작성해 볼까 한다.

~~딴지걸기 있귀 없귀?~~

특히 이 내용은 자바와는 다른 부분이 많음에도 여러분이 thymeleaf의 기능을 많이 접하고 다뤄봤다면 상당히 익숙한 냄새가 풀풀 날 것이다.

이건 확신한다.

왜 그런지 이제부터 하나씩 알아보자.

그 전에 왜 변수나 함수내부에 파라미터를 정의할 떄 방식이 자바와는 다른지에 대한 내용은 실제로 이전에 고랭을 할 때 봤던 어떤 글에서였다.

[Go's Declaration Syntax](https://go.dev/blog/declaration-syntax)

글을 보면 사람이 코드를 읽어가는 방법에 대한 설명을 하고 있다.

실제로 C에서는 Right-Left(Spiral Syntax Rule)을 적용해서 코드를 읽어가는 방법에 대한 글도 있었는데 Kotlin 역시 이런 부분이 적용된 것이 아닐까 싶다.

한번쯤 읽어보면 좋다.

이런 것을 염두해 두고 코드를 보면 어느 정도 감이 오리라 생각이 든다.


# 함수의 기본적인 구조

go, typescript같은 언어들을 잘 아는 분들이라면 특별히 설명할 부분은 없는 내용이지만 자바만 해왔던 분들이라면 지금까지 스텝을 진행하면서 독특함을 느꼈을 것이다.

하지만 결국 가만히 보게 되면 자바에 비해 구문이 다를 뿐 결국 그게 그거라는 것이다.

익숙해지는 것이 중요하다.

처음 자바에서 어떤 기능을 하느 메소드를 만들기 위해서는 유틸 클래스에 정의하거나 클래스 내부에 정의하는 것이 원칙이다.

하지만 코틀린에서는 그런거 없다.

마치 자바스크립트 처럼 클래스가 없더라도 탑 레벨에서 정의가 가능하고 함수 안에 함수를 정의할 수도 있다.


```Kotlin
fun main() {

    hello()

}

fun hello() {
    println("안녕~ 난 코틀린이라고 해")
}

```

```Kotlin
fun main() {

    fun test() {
        println("되니?")
    }

    hello()
    test()
    //innerHello() 아~ 안돼!
    //hello().innerHello() 이렇게 해보고 싶은 욕망이 막 솟구쳐 올랐을거야! 어림도 없다!

}

fun hello() {
    println("안녕~ 난 코틀린이라고 해")
    fun innerHello() {
        println("반가워")
    }
    innerHello()
}
```

이렇게도 가능하다.

사실 눈에 보이지 않지만 코틀린의 상위에는 Unit, Nothing, Any가 존재하는데 그중에 Unit이라는 녀석이 존재한다.

자바에서 말하면 void와 같은 녀석으로 실제로 println같이 코틀린에서 제공하는 api따라가면

```Kotlin
@kotlin.internal.InlineOnly
public actual inline fun println(message: Any?) {
    System.out.println(message)
}

```
하지만 마우스를 가져다 대면 다음과 같이

```
kotlin.io Console.kt 
@InlineOnly
public actual inline fun println(message: Any?): Unit

Prints the given message and the line separator to the standard output stream.
```
Unit을 반화한다.

따라서 실제로는

```Kotlin
fun main(): Unit {

    fun test(): Unit {
        println("되니?")
    }
    
    hello()
    test()
    //innerHello() 아~ 안돼!
    //hello().innerHello() 이렇게 해보고 싶은 욕망이 막 솟구쳐 올랐을거야! 어림도 없다!
    
}

fun hello(): Unit {
    println("안녕~ 난 코틀린이라고 해")
    fun innerHello(): Unit {
        println("반가워")
    } 
    innerHello()
}
```
이겠지만 타입추론에 의해 생략이 가능하다.

이제는 파라미터를 받는 함수를 만들어 보자. 사실 변수 설명할때 언급했던 것이지만 변수의 non-null/nullable을 체크한다.

그렇다면 일단 hello함수에 메세제를 받아서 콘솔에 찍는 함수를 만들자.


```Kotlin
fun main() {

    helloWithParam("거기 누구 있어요?")
    
}

// 변수를 받자
fun helloWithParam(message: String) {
    println(message)
}
```
참 쉽죠?

반환타입이 있는 함수는

```Kotlin
fun main() {
    val normalStr = getStr()
    println("normalStr is $normalStr")
    var strExpression = getStrExpression()
    println("strExpression is $strExpression")
 
 	val sum = sum(1, 1)
 	println("sum is $sum")
    
    val sumex = sumEx(1, 1)
 	println("sumex is $sumex")
}

// 문자열 타입을 반환하는 일반적인 형식의 함수
// {} -> body가 있는 경우에는 Unit이 아니라면 타입을 명시해줘야 한다.
fun getStr(): String {
    return "string"
}

// 문자열 타입을 반환하는 표현식 방식으로 타입 추론이 가능하면 반환 타입 생략가능
fun getStrExpression() = "string expression"

// Int 타입도 마찬가지
fun sum(a: Int, b: Int): Int {
    return a + b
}

// 표현식
fun sumEx(a: Int, b: Int) = a + b
```

근데 코틀린에는 'Kotlin Default and Named Arguments'라는 개념이 존재한다.

지금 hellowWithParam을 호출할 때 helloWithParam()같이 호출하면 'No value passed for parameter message'메세지가 뜬다.

이때 해당 파라미터에 기본 값을 설정하는 것이다.

마치 스프링부트의 컨트롤러에서 @RequestParam을 통해 요청 파라미터를 받을 때 null이면 defaultValue = "1"처럼 이런 것을 제공한다.

이런 반론을 할 수 있을 것이다.

'아니 자바에서도 사용할 수 있잖아?'

하지만 저것은 스프링에서 제공하는 애노테이션을 사용했기에 가능하지 자바의 순수한 기능은 아니다.

사용 방법은


```Kotlin
fun main() {
    helloWithParam("거기 누구 있어요?")
    helloWithParam()
    
}

// 변수를 받자
fun helloWithParam(message: String = "거기 아무도 없어요?") {
    println(message)
}
```

와우! 저것을 통해서 해당 함수에서 넘어온 파라미터가 isBlank()라면 다른 처리를 해야할 때 저렇게 쉽게 처리할 수 있게 된다.

그리고 Named Arguments라는 것이 무엇일까?


# 당황했던 첫 번째 기억

자바에서 어던 문자열을 비교할 때 sensitive case 즉 대소문자를 무시하고 비교해야할 필요가 간혹 있다.

예를 들면 자바에서는

```java
public class Main {
    public static void main(String[] args) {
        String target = "a";
        String destination = "A";
        if(target.equlas(destination)) {
            // doSome
        }
        // 대소문자 무시
        if(target.equalsIgnoreCase(destination)) {
                // doSome
            }
        }
}
```

하지만 코틀린에서 equalsIgnoreCase가 없어서 엄청 당황했던 기억이 난다.

자바에서는 이런 기능이 없지만 코틀린에서는 default parameter'와 'Named Arguments'를 이용해 이것을 처리할 수 있다.


```Kotlin
fun main() {
    val target = "test"
    var destination = "TeST"
    if(target.equals(destination)) {
        println("match")
    } else {
        println("not match")
    }
    // equals의 스펙은 ignoreCase를 default와 Named Arguments가 적용된다.
    if(target.equals(destination, false)) {
        println("match")
    } else {
        println("not match")
    }
    
    // equals의 스펙은 ignoreCase를 default와 Named Arguments가 적용된다.
    // public actual fun String?.equals(other: String?, ignoreCase: Boolean = false): Boolean
    // 따라서 false를 생략하면 대소문자 구분
    if(target.equals(destination, false)) {
        println("match")
    } else {
        println("not match")
    }
    
    // Named Arguments은 순서에 상관없이 함수에 선언된 변수명으로 접근해서 넣을 수 있다.
    if(target.equals(ignoreCase = false, other = destination)) {
        println("match")
    } else {
        println("not match")
    }

    // Named Arguments 생략
    if(target.equals(destination, true)) {
        println("match")
    } else {
        println("not match")
    }
    
    // 명시적으로 사용한다.
    if(target.equals(destination, ignoreCase = true)) {
        println("match")
    } else {
        println("not match")
    }
    
    // Named Arguments를 사용하면 순서 상관없다.
    // 단 처음 들어오는 파라미터에 Named Arguments를 적용한다면 뒤에 들어오는 파라미터는 Named Arguments로 접근해야 한다.
  	if(target.equals(ignoreCase = true, other = destination)) {
    // if(target.equals(ignoreCase = true, destination)) { 이건 안된다.
        println("match")
    } else {
        println("not match")
    }
}
```

장점은 변수가 좀 많을 경우에 순서에 맞춰서 넣어야 할 때 저렇게 순서에 구애받지 않고 Named Arguments를 통해서 명시적으로 넘겨줄 수 있다.

다만 맨 처음 Named Arguments로 파라미터를 세팅하면 뒤에 오는 파라미터는 전부 Named Arguments로 선언해서 넘겨줘야 한다.

다음 코드를 보자

```Kotlin
fun main() {
    // 메세지가 없다면 이럴 때는 isUpper에 대해서 명시적으로 Named Arguments로 설정해 줘야 한다.
    helloWithTwoParam(isUpper = true)
    // 아무것도 없으면 default로 any body here? 찍힌다
    helloWithTwoParam()
    helloWithTwoParam("how are you? i'm fine")
    helloWithTwoParam("how are you? i'm fine", true)
    helloWithTwoParam("how are you? i'm fine", isUpper = true)
    helloWithTwoParam(isUpper = true, message = "how are you? i'm fine")
    // helloWithTwoParam(isUpper = true, "how are you? i'm fine") 이거 안된다~
}

// 두개의 변수를 받자
// isUpper가 true면 대문자로 콘솔에 찍는다. 
fun helloWithTwoParam(message: String = "any body here?", isUpper: Boolean = false) {
    if(isUpper) println(message.uppercase()) else println(message)
    
}
```

즉 이런 기능들을 최대한 활용하면 불필요한 코드를 줄일 수 있게 된다.

# infix function create and call

thymeleaf에서 ${}내에서 && 또는 ||을 and 나 or로 사용하는 것을 본 적이 있을 것이다.

코틀린에서도 Boolean.kt에서 이런 것들을 제공한다.

```Kotlin
fun main() {
    val trueMe = true
    val falseMe = false
    println("trueMe || falseMe is ${trueMe || falseMe}") // true
    println("trueMe or falseMe is ${trueMe or falseMe}") // true
    println("trueMe or falseMe is ${trueMe.or(falseMe)}") // true
    println("trueMe && falseMe is ${trueMe && falseMe}") // false
    println("trueMe and falseMe is ${trueMe and falseMe}") // false
    println("trueMe and falseMe is ${trueMe.and(falseMe)}") // false
}

```
앞서서 제어흐름에서 봤던 downTo나 until같은 녀석들은 infix function으로 만들어져 있어서 가능한 것이다.


사실 어플리케이션 설계 시점에서 이런 infix function을 만들 일이 있을까만은

```Kotlin
fun main() {
    val hello = "hello"
    val heLLo = "heLLo"
    //val heLLo = "heLLo111"
    println("hello same heLLo is ${hello sameTo heLLo}")
    println("hello same heLLo is ${hello.sameTo(heLLo)}")

}

// 표현식으로 간략하게 만들어보자
infix fun String.sameTo(to: String) = (this == to) or this.equals(to, ignoreCase = true)

```
간단한 코드로 실제로는 자바의 equalsIgnoreCase와 같이 작동하게 infix function으로 만든 sameTo를 만들고 사용할 수 있다.

# Extension Function

확장 함수가 무엇일까?

여러분의 브라우저(크롬)에서 개발자 도구를 열고 콘솔창에 다음과 같이 처보자.

```javascript
// 배열에서 마지막 요소를 반환한다.
Array.prototype.lastElem = function() {
     return this[this.length - 1];
}

let array = [5,4,3,2,1, "last"];
console.log(array.lastElem());
```
Array객체는 우리들 개발자가 새로 만들거나 기존의 객체에 새로운 어떤 함수를 만들 수가 없다.

그래서 prototype이라는 키워드로 함수를 정의하고 그것을 확장할 수 있다.

```javascript
function Basquiat(name) {
    this.name = name;
}

Basquiat.prototype.convertToEngName = () => {
    return name + "eng";
}

let basquiat = new Basquiat("test");

basquiat.convertToEngName();
```
이런 것도 가능하다.

물론 자바에서는 인터페이스를 통해서 이것을 해결해 볼수 있는 방법이 있다.

프레임워크나 특정 라이브러리에서 제공하는 객체 클래스를 직접 손볼 수 없으니 특정 클래스를 인터페이스나 여러 방법으로 상속 구현해서 새로운 메소드를 만들고 그것을 사용할 수 있다.

대표적인 것들이 JDBC관련 라이브러리라든가 WebFlux의 R2DBC를 상속 구현한 일렬의 라이브러리들이 그렇게 제공되고 있다.

하지만 어클리케이션 레벨에서는 단점이 존재하는데 기존에 사용하는 특정 클래스를 상속 구현하고 새로 작성한 클래스로 교체를 해야한다는 점이다.

만일 자바스크립트처럼 prototype을 이용해서 확장을 할수 있다면????

코틀린에서 그것을 지원한다는 것은 상당히 좋다. 이것을 통해서 기존 코드를 손을 대지 않고도 확장 함수를 사용할수 있다.

대표적으로 CrudRepositoryExtensions.kt가 있다.
```Kotlin
/**
 * Retrieves an entity by its id.
 *
 * @param id the entity id.
 * @return the entity with the given id or `null` if none found
 * @author Sebastien Deleuze
 * @since 2.1.4
 */
fun <T, ID> CrudRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id).orElse(null)
```

코틀린에서 타입들 그러니까 Long, Int같은 녀석들은 primitive type이 아닌 래퍼 함수라고 했었다.

그렇다면 저 확장 함수를 이용해서 Int의 경우 null이라면 0을 반환하는 녀석을 확장 함수로 만들어 보자.

왜냐하면 어플리케이션의 특성상 이런 number type이 null이면 0으로 치환해서 계산해야하는 경우가 많다 보니 이것을 일일히 처리하는게 번거롭기 때문이다.

```Kotlin
// top level에 선언해 보자 
// null이면 0을 반환하자
fun Int?.ifNullZero() = this?.let { it } ?: 0

fun main() {
    val testInt: Int? = null
    println("what is ? -> $testInt")
    println("what is ? -> ${testInt.ifNullZero()}")
}

// result
// what is ? -> null
// what is ? -> 0
```

어 그러면 아직은 배우지 않았지만 제너릭을 통해서 Number을 구현상속하고 있는 녀석들      

Byte, Int, Short, Long, Float, Double

이 녀석들을 다 처리할 수 있지 않을까?

```Kotlin
// top level에 선언해 보자 
// null이면 0을 반환하자
// fun Int?.ifNullZero() = this?.let { it } ?: 0

// Generic하게 만들어 보자.
fun <T: Number> T?.ifNullZero() = this?.let { it } ?: 0

fun main() {
    val testInt: Int? = null
    println("what is testInt? -> $testInt")
    println("what is testInt? -> ${testInt.ifNullZero()}")
    
    val testLong: Long? = null
    println("what is testLong? -> $testLong")
    println("what is testLong? -> ${testLong.ifNullZero()}")
    
    val testFloat: Float? = null
    println("what is testFloat? -> $testFloat")
    println("what is testFloat? -> ${testFloat.ifNullZero()}")
    
    val testDouble: Double? = null
    println("what is testDouble? -> $testDouble")
    println("what is testDouble? -> ${testDouble.ifNullZero()}")
    
    val testShort: Short? = null
    println("what is testShort? -> $testShort")
    println("what is testShort? -> ${testShort.ifNullZero()}")
}

// result
// what is testInt? -> null
// what is testInt? -> 0
// what is testLong? -> null
// what is testLong? -> 0
// what is testFloat? -> null
// what is testFloat? -> 0
// what is testDouble? -> null
// what is testDouble? -> 0
// what is testShort? -> null
// what is testShort? -> 0
```
어설프긴 하지만 잘 된다.       


다만 확장함수의 단점은 어떤 특정 라이브러리의 클래스를 확장해서 사용할때 그 라이브러리가 버전업이 되면서 발생할 수 있는 예상할 수 없는 사이드 이펙트가 존재한다.

조슈아 블로크의 [이펙티브 자바]에 보면 이런 내용이 나오는데 진짜 운이 없어서 버전업된 라이브러리에 같은 이름의 메소드가 생기면 원하는 동작을 하지 않을 수 있다.


# At a Glance
기본적인 함수를 만들고 다루는 방식을 배워봤다.      

그중에 확장 함수의 경우에는 자주 사용할 수 있기 때문에 다양한 방식으로 바리에이션 해보는게 좋다.       

다음에는 클래스에 대해서 알아볼 것이다.      

