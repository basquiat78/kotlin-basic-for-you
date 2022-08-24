# 당신을 위한 코를린 기본
코틀린의 기본 문법을 배워보자.

# 목적

팀원을 꼬시기 위한 목적이다. 단순하게 스프링 부트로 리팩토링 또는 애초부터 코틀린으로 프로젝트를 통해 코틀린의 장점을 부각시키고자 했다.

하지만 이건 나만의 착각인가? 좀 더 자유롭고 자바처럼 익숙해지기 위해서는 기본적인 문법을 건너띄면 안되겠다는 생각이 들었다.

또한 현재 진행중인 프로젝트에 이런 것들을 일일히 설명하는게 힘들어서 그것을 해소하고자 한다.

# 심플하게 시작하자.

[go to play.kotilnlang](https://play.kotlinlang.org)

나의 경우에는 IDE가 아닌 코틀린에서 제공하는 웹 IDE로 작성할 생각이다.

다만 이 부분은 IDE의 막강한 기능을 사용할 수 없기 때문에 코드 작성시 발생하는 컴파일 오류를 확인할 수 없다.

IDE세팅이 되어 있다면 IDE를 적극 활용해도 무방하다.

따라서 이 README파일은 내가 작성한 그 어느 것보다 가장 긴 내용을 담을 것 같다.

# 가장 먼저 알아야 하는 변수 타입

자바의 경우에는 원시 타입, 즉 primitive type과 그것을 감싼 래퍼 클래스가 존재한다.

하지만 코틀린은 래퍼 클래스만 존재한다.

조슈아 블로크의 [이펙티브 자바]에 따르면 래퍼 클래스의 장점에 대해 설명하는 부분이 있는데 아마도 코틀린에서는 이런 장점을 극대화 시킨 것이 아닌가 싶다.

이런 변수 타입은 코틀린에서는 기본적으로 non-null Type이다.

이게 무슨 말인가 하면

```java
public class My {

    public void test() {
        Integer intValue = null;
        Long longValue = null;
        String strValue = null;
    }

}

```
자바에서는 저게 가능하지만 코틀린에서는 불가능하다.

```Kotlin
fun main() {
    
    var str: String = null // Null can not be a value of a non-null type String
    
}

```
null을 세팅하고 싶다는 욕망이 들때는 어떻게 해야할것이가??


```Kotlin
fun main() {
    
    var str: String? = null // possible
    
}

```

여기서 '?'를 통해 non-null타입을 nullable하다고 알려줄 수 있다.

다만 주의할 점은 이 변수를 메소드의 파라미터로 사용할 경우에는 해당 메소드역시 들어오는 변수가 nullable하다는 것을 명시해야 한다.

예를 들면

```Kotlin
fun main() {
    var nu: String? = "test"
    test(nu) // Type mismatch: inferred type is String? but String was expected   
}

fun test(str: String) {
    println(str)
}

```
위와 같은 에러가 발생한다.

코틀린에서는 non-null 타입에 대해서는 엄격하게 체크하는 방식을 택하고 있다.

두가지 방법이 있는데

```Kotlin
fun main() {
    var nu: String? = "test"
    test(nu) // Type mismatch: inferred type is String? but String was expected   
}

fun test(str: String?) {
    println(str)
}

```
test라는 메소드에 ?을 붙여서 nullable한 타입이 들어올 수 있다고 명시하거나

```Kotlin
fun main() {
    var nu: String? = "test"
    test(nu!!) // nu라는 이 변수는 null일수 없다고 확신한다!!!!!!!!
}

fun test(str: String) {
    println(str)
}
```

메소드를 호출할때 변수에 위와 같이 단언 연산자(not-null assertion)인 '!!'를 통해서 null일 수 없다고 확신한다고 명시를 해서 처리할 수 있다.

다만 그 반대의 경우

```Kotlin
fun main() {   
    var nu: String = "test"
    test(nu)
}

fun test(str: String?) {
    println(str)
}
```
이런 건 가능하다.

즉 코틀린은 null에 대한 안정성을 위한 방식을 선택하고 있다는 것을 알 수 있다.

또한 코틀린에서 변수를 선언하는 방법은 두가지 키워드를 제공하는데 그것이 바로 val과 var이다.

자바스크립트를 자주 다뤄봤던 분이라면 최근에는 var 대신 let을 그리고 final에 해당하는 const를 사용하게 되는데 코틀린의 경우에도 마찬가지이다.

```
val: value의 약어로 일종의 const 또는 final이 붙은 변수. Immutable(변경 불가능). 한번 할당하면 변경하지 못하지만 지연 초기화 사용 가능

var: variable의 약어로 가변변수라는 의미를 가지고 있으며 자바스크립트에서는 let과 같은 의미이로 자바에서는 일반 변수를 의미한다.

```

그렇다면 실제로 이것의 특징이 뭔지 알아보자.

위에 언급한 사이트에서 바로 테스트 해보자.

```Kotlin

fun main() {
    
    var a: String = "test"
    println(a)
    
    a = "change word"
    println(a)
    
    
}
```
여기서 코틀린의 타입추론을 이용하면

```Kotlin
fun main() {
    
    //var a: String = "test"
    var a = "test"
    println(a)
    
    a = "change word"
    println(a)
    
    
}
```

다만 타입이 맞지 않으면 에러가 발생한다.

```Kotlin
fun main() {
    var str: String =  "test"
    println(a)
    str = 0
    println(a)
}
```
String타입의 str변수에 Int타입의 0으로 변경하면 당연히 에러가 발생한다.

그렇다면 val은 어떻게 될까?

```Kotlin
fun main() {
    
    //val a: String = "test"
    val a = "test"
    println(a)
    
    a = "change word" // Val cannot be reassigned
    println(a)
}
```

한번 할당되면 변경이 불가능하지만 지연 초기화는 사용가능하다.


```Kotlin
fun main() {
    val a: String
    var flag = "Y"
	//var flag = "N"
    a = if("Y".equals(flag)) {
        "Yes"
    } else {
        "No"
    }
    println(a)   
}
```
즉 어떤 값이 조건에 따라서 지연 초기화를 통해 할당이 가능하다.          

지금까지 타입을 다루는 방법과 변수 선언 키워드 var과 val에 대한 특징을 설명했다.

Wait Next Step! Stay Tuned!
