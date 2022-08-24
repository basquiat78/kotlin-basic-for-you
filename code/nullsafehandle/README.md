# 널이라는 너를 다루는 사용설명서

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/nullsafehandle/kotlin/NullSafeHandle.kt)

이것이 자바에서 코틀린으로 넘어오게 하는 큰 이유중 하나라고 할 수 있는 부분을 다루고자 한다.

null에 대한 독특한 방식을 다루는 코틀린에서는 그렇다면 null을 어떻게 다룰까

## Safe Call
스프링 프레임워크를 하다보면 화면을 다루는 경우가 제법 많은데 그중 가장 많이 사용하는 템플릿이 thymeleaf일 것이다.

참 특이하게도 thymeleaf에서는 이 Safe Call를 지원한다.

예를 들면

```html
<html lang="ko" xmlns:th="http://www.thymeleaf.org"
                xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">

<input type="text" th:value=${ObjectFromServer?.name}>
</html>
```
이런 코드를 작성할 수 있는데 서버로부터 내려온 ObjectFromServer라는 객체가 null이 아니면 해당 객체의 name변수값을 가져오고 아니면 null을 반환하게 된다.

코틀린에서도 이 방식을 통해서 처리가 가능하다.

```Kotlin
/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val john = returnJohn(true) // John객체를 반환한다.
    //val john = returnJohn() // null을 반환한다.
    // println(john.name) //불가능 하다. returnJohn 메소드의 반환 타입이 nullable 타입이기 때문이다. 
    // 위 코드는 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type John?가 뜰것이다.
    println(john!!.name) // 하지만 단언연산자를 사용한다면 가능하다.
    println(john?.name)
}

/**
 * 예제를 위한 어거지 메소드
 * John는 널일수도 있고 아닐수도 있습니다.
 */
fun returnJohn(areYouJohn: Boolean = false): John? = if(areYouJohn) {
    John("John")
} else {
    null
}

/**
 * John!!!!
 */
class John(
    var name: String
)
```

returnJohn에서 true/false에 따라 John이거나 null을 반환하는 예제용 코드를 살펴보자.

Safe call에 의해서 returnJohn메소드로 호출된 값을 체크하고 콘솔에 찍는다.      

그렇다면 이것을 자바 코드로 생각해 본다면


```java
public class Main {

    public static void main(String[] args) {
        John john = returnJohn(true); //or false
        if(john != null) {
            System.out.println(john.getName());
        }
    }

}

```
이런 형식일 것이다. 그리고 만일 null인 경우 어떤 액션올 취하겠다면

```java
public class Main {

    public static void main(String[] args) {
        John john = returnJohn(true); //or false
        if(john != null) {
            System.out.println(john.getName());
        } else {
            System.out.println("널 보내며~~~~ 괴로워 하네? 널 떄문에!");
            //throw new IllegalArgumentException("널 보내며~~~~ 괴로워 하네? 널 떄문에!");
        }
    }

}

```
IllegalArgumentException을 날리거나 심플하게 콘솔에 로그를 남길 수 있다.       

Optional을 통해서 무언가를 해볼 수도 있을것이다.      

하지만 저걸 코틀린으로 처리해 보자.

```Kotlin
/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val john = returnJohn(true) // John객체를 반환한다.
    //john?.let{println(it.name)} ?: println("널 보내며~~~~ 괴로워 하네? 널 떄문에!")
    john?.let{println(it.name)} ?: throw IllegalArgumentException("널 보내며~~~~ 괴로워 하네? 널 떄문에!")
}

/**
 * 예제를 위한 어거지 메소드
 * John는 널일수도 있고 아닐수도 있습니다.
 */
fun returnJohn(areYouJohn: Boolean = false): John? = if(areYouJohn) {
    John("John")
} else {
    null
}

/**
 * John!!!!
 */
class John(
    var name: String
)

```
엘비스 헤어로 불리는 '?:'를 사용하면 한줄로 간단하게 표현이 가능하다.        

[근데 왜 엘비스 연산자라고 하는데?](https://github.com/basquiat78/WHYNOTINJAVAELVISOPERATOR)         

물론 그렇다고 완전히 NPE에 해방되는 것은 아니지만 이것을 다루는 방식이 자바에 비해서 너무나 편하다는 것이다.        

# 단언연산자(not-null assertion) !!

단언 연산자로 불리는 '!!' 역시 null을 다루는 방식이긴 하다.        

하지만 이미 이전 스텝에서 다뤘던 만큼 여기서는 간략하게 설명으로 대처한다.       

어떤 메소드나 함수가 받는 파라미터가 non-null type인 경우에는 앞서 설명했듯이 nullable한 타입에 대해 mismatch 에러를 보여준다.      

하지만 이것이 어떤 이유나 어플리케이션의 특성상 null일수가 없다고 확신할때 단언연산자를 붙여서 nullable타입의 변수를 넘길 수 있다.             

# At a Glance

'근데요 코드에 보니깐요 if같은 걸 그냥 반환허거나 첨 보는 let같은 녀석들이 눈에 띄네요?'         

기딜리면 다 나온다. if같은 경우는 try도 저런 방식으로 가능하다.      

이것은 차후에 설명을 할예정이다.      

다음 스텝은 일반적으로 이런 기본적인 타입을 다루는 방식을 배우게 되면 넘어가는 흐름제어, 즉, if, for같은 것을 배우게 된다.       

대부분의 언어가 큰 차이점이 없지만 코틀린만의 개성이 있기 때문에 어느 정도 공부를 해야 한다.        