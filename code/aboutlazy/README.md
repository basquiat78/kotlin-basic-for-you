# 지연 초기화

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/delegation/kotlin/AboutLazy.kt)     
 
지금까지는 코틀린의 기본적인 문법을 배웠다면 지금부터는 코틀린의 디테일한 부분을 하나씩 알아가 보는 시간을 가져볼까 한다.

그 중에 첫 번째는 최상위 프로퍼티이다.

## 변수를 임포트 할 수 있다고??

이 테스트는 코틀린 놀이터가 아닌 IDE에서 테스트해봐야 한다.

예를 들면 자바에서 전역 변수로 사용하는 방법은 다음과 같다.

io.basquiat.constant 패키지에 불변 객체 Constants를 만들고 prefix라는 상수를 만들자.       

물론 enum class로도 가능하다.

```java
package io.basquiat.constant;

public final class Constants {
    public static final String prefix = "java";
}
```

그렇다면 다른 패키지에서 prefix를 사용하고 싶다면

```java
package io.basquiat.service;

import io.basquiat.constant.Constants;

public class Service {

    public String returnString() {
        return Constants.prefix + " Language";
    }

}
```
위와 같은 방식으로 사용할 수 있다. 물론 static import를 사용해서 코드를 더 간결하게 만들수도 있다.

코틀린에서는 자바처럼 최상위 가시성 키워드인 'public/internal/private'을 지정할 수 있다.     

그리고 다음과 같이 사용할 수도 있다.

```kotlin
package util

val prefix = "Java"
```

```kotlin
package main

import util.prefix
        
fun main() {
    println("$prefix Language")
}
```
처럼 사용할 수 있다.     

사실 변수를 임포트한다고 하지만 실제로 디컴파일된 코드를 보면 클래스로 감싸져 있어서 이런 방식이 가능하다.       

자바스크립트랑 많이 비슷하다.

## 지연초기화

자바에서 클래스를 인스턴스화 할 때는 우리는 빈 생성자 또는 아규먼트가 있는 생성자를 사용할 수 있다.


```java
public class Musician {
    private String name;
    private string genre;
}
```
롬복을 쓰든 직접 빈 생성자와 아규먼트가 있는 생성자 또는 setter/getter를 직접 선언하든 있다는 가정하에 다음과 같이 코드를 작성할 수 있다.

```java
public class Main {
    public static void main(String[] args) {
        Musician musician = new Musician();
        musician.setName("John Coltrane");
        musician.setGenre("Jazz");

        Musician musicianWithConstructor = new Musician("John Coltrane", "Jazz");
    }
}
```

코틀린에서 프로퍼티를 생성할 때 생성자에 정의하는 것과는 달리 바디내에 프로퍼티를 정의할 때는 자바와 다른 점이 있다.

그것은 코틀린에서는 클래스의 바디내에 프로퍼티를 정의할 때는 초기화 해야하는 엄격함이 존재한다.      

특히 그 중 하나가 우리가 앞서 배웠던 코틀린의 특징중 하나는 nullsafe와 관련된 내용이다.    

```kotlin
class Musician {

    // backing properties
    //private var text: String
    var genre: String
        get() = "Jazz"

    fun setUpGenre() {
        name = "Jazz"
    }
}
```
자바를 생각하고 위와 같이 커스텀 세터를 만들든 setUpGenre을 만들어서 name 값을 할당하든 코드를 작성한다면 여러분들의 IDE에는 다음과 같은 시뻘건 에러가 보일 것이다.

```
Property must be initialized
```
음? 프로퍼티는 무조건 초기화해야 한다고?

그렇다면 null일 수 있다는 가정을 하고 다음과 같이 작성해야 한다.

```kotlin
class Musician {

    var genre: String? = null
        get() = "Jazz"
    
}
```
물론 nullable이 아닌 다른 장르를 기본값으로 세팅할 수 있다.     

```kotlin
class Musician {

    var genre: String = "jazz"
        // backing field를 이용하거나
        // get() = field
        get() = "JAZZ"
    
}
```

하지만 가만히 보면 어떤 면에서는 참 불필요한 초기화를 해야 한다는 것이다.         

그래서 코틀린에서는 1.2부터 이러한 프로퍼티 지연 초기화를 가능하게 하는 `Late Initialization`을 지원한다.

## 키워드 lateinit

키워드의 의미에 모든 것이 담겨져 있다.       

```kotlin
fun main() {
    val musician = Musician()
    musician.genre = "Jazz"
    println(musician.genre)
    
}

class Musician {

    lateinit var genre: String
    
}
```
물론 이 코드를 실행할 때 genre에 값을 대입하지 않으면 

```
Exception in thread "main" kotlin.UninitializedPropertyAccessException: lateinit property genre has not been initialized
```
이런 에러를 만나게 된다.      

코틀린에서 인스턴스화 될때 초기값이 세팅되길 바랬건만 어떤 값도 초기화되지 않았기 때문에 발생하는 에러이다.     

하지만 그렇다고 무조건 lateinit를 사용할 수 있는 것은 아니다. 여기에는 몇가지 조건이 맞아야 한다.

```
1. 프로퍼티가 여러 곳에서 코드를 통해 변경될 수 있으므로 가변 프로퍼티, 즉 var로 정의해야한다.
2. 프로퍼티의 타입이 null이 아닌 타입이여야 한다.
    - INT, Long, Boolean같은 원시값을 표현하는 타입은 허용하지 않는다.
    - 이 경우에는 'lateinit' modifier is not allowed on properties of primitive types를 만나게 된다.
    - 초기화 식을 지정해서 바로 값을 대입할 수 없다.
    - lateinit var genre: String = "jazz"값은 방법이 불가능하다.
    - 게다가 이렇게 한다는 것을 lateinit를 사용하는 이유가 없어진다.
    - 그리고 이 경우에는 'lateinit' modifier is not allowed on properties with initializer를 만나게 된다.
```
또한 코틀린 1.2부터는 다음과 같이 최상위 프로퍼티에서도 `lateinit`을 사용할 수 있다.

```kotlin
lateinit var top: String

fun initTop() {
    top = "TOP PROPERTY"
}

fun main() {
    initTop()
    println(top)
}
```

## isInitialized

`lateinit`으로 선언된 프로퍼티가 초기화되었는지 확인하는 방법이 있다.       

사실 이걸 사용할 일이 있을지 모르겠지만 `isInitialized`을 활용하는 것이다.       

해당 함수는 붙어 있는 애노테이션을 따라가면 단독으로 사용할 수 없다는 것을 알 수 있다.      

어째든 다음과 같이 사용해보자.

```kotlin
fun main() {
    val musician = Musician()
    println(musician.genreInitialized())
    musician.genre = "Jazz"
    println(musician.genreInitialized())
    println(musician.genre)
}

class Musician {
    lateinit var genre: String
    fun genreInitialized(): Boolean = this::genre.isInitialized
}
```
즉 값을 할당하기 전과 후를 찍어보면 false, true가 찍히는 것을 확인할 수 있다.    


## 키워드 lazy

`lateinit`을 활용한 지연 초기화는 위에서 언급한 몇 가지 조건을 만족해야 한다고 했다.      

하지만 가변 프로퍼티가 아닌 불변 프로퍼티에서도 이런 지연 초기화를 해야하는 경우가 발생하는데 이 때는 `lazy`를 통해서 가능하다.      

불변 프로퍼티라는 점을 유념하고 다음과 같은 코드를 한번 살펴보자.

어떤 API를 호출할 때 그에 해당하는 코드값을 넘겨받는다고 생각해 보자.

```kotlin

fun main() {

    val test = Test()

    println(test.code)
    println(test.code)
    println(test.code)

}

class Test {

    val code get() = callAPI()

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}
//call api
//my code
//call api
//my code
//call api
//my code
```
사실 좀 어거지긴 하지만 여기서 콘솔에 `code`값을 찍을 때마다 어떤 일이 벌어지는지 확인해 보자.

같은 값을 호출할 텐데 그럼에도 `callAPI`가 콘솔에 찍힌 수만큼 호출된다는 것을 알 수 있다.      

일단 한번 불변 프로퍼티에 값이 초기화되면 변경할 수 없다.       

이런 점을 활용할 때 우리는 `lazy`라는 `지연 계산 프로퍼티` 키워드를 활용할 수 있다.      

우리는 앞서 `by`를 통해 위임을 하는 방법을 알고 있다. 이 두 키워드의 조합을 통해서 이 방법을 구현할 수 있다.      

```kotlin
fun main() {

    val test = Test()

    println(test.code)
    println(test.code)
    println(test.code)

}

class Test {

    val code: String by lazy { callAPI() }

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}

//call api
//my code
//my code
//my code

```
`지연 계산 프로퍼티`라는 말에 주목해 보자. `by`키워드 뒤로 `lazy`라는 `지연 계산 프로퍼티`에 위임을 하고 있는 모습이라는 것을 알 수 있다.     

`lazy`를 IDE를 통해서 해당 함수에 대한 내용을 살펴보면 initializer라는 파라미터 네임으로 람다를 받는다는 것을 알 수 있다.     

즉 이 람다를 통해서 지연 초기화를 하고 있는 것을 알 수 있다.    

그리고 이것은 한번만 초기화이후 초기화된 값을 반환하게 된다.     

또한 해당 코드는 내부적으로 `SynchronizedLazyImpl`를 사용하고 있는데 함수명에서 느껴지는 향기는 왠지 스레드와 관련있어 보인다.      

즉 기본적으로 `lazy`는 `thread-safe`하다. 이와 관련 여러가지 옵션을 제공하는데 이 부분은 밑에서 내용에서 다시 한번 다뤄보고자 한다.      

`lateinit`과 마찬가지로 지역 변수에서도 사용할 수 있다.

다만 `lateinit`와 반대로 `var`에서는 사용할 수 없다.

그러면 이런 늦은 초기화를 언제/왜 사용하냐는 질문이 있을 수 있다.     

프로퍼티를 정의할 때 이 프로퍼티가 null이 아닌 유형의 타입을 가지는 프로퍼티에 사용하게 되기 때문에 개발자의 판단이 중요해 보이는 부분이다.      

따라서 개발자가 이것을 사용할지 여부를 판단해야한다.       

## Delegates Observable & Vetoable properties

이전 위임을 다루면서 건너띈 녀석들이 있는데 그 중에 한번 소개해 볼만한 2개를 한번 다뤄보고자 한다.      

보통 Observable하면 옵저버 패턴을 먼저 떠올릴 것이다.      

즉 옵저버들이 어떤 상태 변화가 생길 때마다 어떤 행위를 하는 것을 의미하는데 이것을 코틀린에서는 쉽게 다룰 수 있도록 하고 있다.      

정확히는 프로퍼티의 상태 변경 감지 기능정도로 보면 될 것이다.       

```kotlin
import kotlin.properties.Delegates.observable

fun main() {

    val member = Member(1000)
    member.name = "Bas"
    member.name = "Bas!"
    member.name = "Bas!!"

}

class Member(
    val id: Long? = null,
) {
    var name: String by observable("init") {_, prev, new ->
        changeProperties(prev, new)
    }

    private fun changeProperties(prev: String, new: String) {
        println("Member Id : ${this.id}, $prev changed to $new ")
    }

}
```
해당 코드를 실행하면 name이 변경될 때마다 감지를 하고 그 정보를 콘솔에 찍는 간단한 코드이다.     

prop은 해당 프로퍼티의 속성을 보여주는 정보로 사용하지 않기 때문에 `_`로 표현했다.

Vetoable은 거부라는 의미를 지니고 있다. 어떤 프로퍼티가 변경될 때 조건을 두어서 변경 여부를 결정하게 할 수 있다.      

```kotlin
import kotlin.properties.Delegates.vetoable

fun main() {

    val member = Member(1000)
    println(member.name)
    member.name = "WORLD"
    println(member.name)
    member.name = "Bas!"
    println(member.name)
    member.name = "Bas!!"
    println(member.name)

}

class Member(
    val id: Long? = null,
) {
    var name: String by vetoable("Jazz") {_, _, new ->
        validation(new)
    }

    private fun validation(new: String): Boolean {
        return new != "WORLD"
    }

}
```
위 코드의 경우에는 새로운 이름이 WORLD가 아닌 경우에 변경을 허용하는 코드이다.

따라서 중간에 WORLD로 값을 할당한 경우에는 Jazz로 찍히는 것을 알 수 있다.        

물론 경우에 따라서 이 부분은 어떤 특정 값이 오면 에러를 던질 수 있다.      

```kotlin
import kotlin.properties.Delegates.vetoable

fun main() {

    val member = Member(1000)
    println(member.name)
    member.name = "WORLD"
    println(member.name)
    member.name = "Bas!"
    println(member.name)
    member.name = "Bas!!"
    println(member.name)

}

class Member(
    val id: Long? = null,
) {
    var name: String by vetoable("Jazz") {_, _, new ->
        validation(new)
    }

    private fun validation(new: String): Boolean = if(new != "WORLD") true else throw IllegalArgumentException("변경할 수 없습니다.")

}
```
만일 프로퍼티의 속성이 Number형식이라면 이전값보다 커야 하거나 작아야한다는 조건을 붙여서 코드를 구성할 수 도 있을 것이다.      

# More Detail

위에서 배웠던 `lazy`와 `Delegates`에 대한 좀 더 디테일한 부분을 다뤄보고자 한다.

## lazy의 LazyThreadSafetyMode

`lazy`는 다양한 함수를 제공한다. 

그중에 동기화와 관련해 아래와 같은 고전적인 방식의 함수도 제공한다.

```kotlin
public actual fun <T> lazy(lock: Any?, initializer: () -> T): Lazy<T> = SynchronizedLazyImpl(initializer, lock)
```

예를 들면 다음과 같다.

```kotlin
fun main() {
    val test = Test()
    for(i in 1..10) {
        Thread {
            println(test.code)
        }.start()
    }
}

class Test {
    private var lock = Any()
    val code: String by lazy(lock) { callAPI() }

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}
```
마치 자바의 `synchronized`같은 방식을 사용하고 있다.

그런데 다음 `lazy`코드를 보면 `LazyThreadSafetyMode`를 가질 수 있다.      

```kotlin
public actual fun <T> lazy(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T> =
    when (mode) {
        LazyThreadSafetyMode.SYNCHRONIZED -> SynchronizedLazyImpl(initializer)
        LazyThreadSafetyMode.PUBLICATION -> SafePublicationLazyImpl(initializer)
        LazyThreadSafetyMode.NONE -> UnsafeLazyImpl(initializer)
    }
```

기본적으로 멀티 쓰레드 환경을 고려해서 `LazyThreadSafetyMode.SYNCHRONIZED`이다.      

그리고 위에서 사용한 lock을 이용한 코드를 따라가면 결국 `LazyThreadSafetyMode.SYNCHRONIZED`와 똑같은 함수를 타게 되어 있다.      

하지만 지금 코드에서는 멀티스레드에서 동기화를 고려하지 않아도 문제없는 코드이다.        

그럼에도 기본 모드가 멀티 스레드의 동기화를 기본으로 가져가기 때문에 어떻게 보면 오버헤드라고 할 수 있다.      

물론 지금같은 환경에서는 이런 오버헤드가 무의미할 수도 있고 아닐 수도 있는데 

```kotlin
class Test {

    val code: String by lazy(LazyThreadSafetyMode.PUBLICATION) { callAPI() }

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}
```
다음과 같이 모드 정보를 명시해서 사용할 수 있다.      

`SYNCHRONIZED`와 `PUBLICATION`의 차이점은 코드를 찾아 들어가보면 `synchronized(lock)`사용 여부에 있다.

동기화를 하지 않아도 문제가 없다고 판단되는 경우이기 때문에 오버헤드를 줄인다는 목적에 부합한다.

물론 별도의 쓰레드 처리를 하고 싶지 않다면 NONE을 사용할 수 있다. 

그렇다면 이 차이를 보고 싶다면 코드를 다음과 같이 작성해서 실행해 보자.

```Kotlin
fun main() {

    val test = Test()

    for(i in 1..10) {
        Thread {
            println(test.code)
        }.start()
    }
}

class Test {

    val code: String by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { callAPI() }
    //val code: String by lazy(LazyThreadSafetyMode.NONE) { callAPI() }
    //val code: String by lazy(LazyThreadSafetyMode.PUBLICATION) { callAPI() }

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}

```
`LazyThreadSafetyMode.SYNCHRONIZED`가 아니면 초기화가 여러 번되는 경우를 볼 수 있다.       

따라서 이 모드는 상황에 따라서 설정할 수 있다.     

요약하자면,

```
1. LazyThreadSafetyMode.SYNCHRONIZED
    - 기본 세팅값이다.
    - 멀티 스레드 환경에서 프로퍼티 접근을 동기화한다.
    - 한번에 한 스레드만 프로퍼티 값을 초기화한다.
    

2. LazyThreadSafetyMode.PUBLICATION
    - 멀티 스레드 환경에서 초기화 함수가 여러 번 호출될 수 있다.
    - 하지만 다른 스레드에서 초기화되어 할당된 값이 있다면 그 값으로 동기화한다.

3. LazyThreadSafetyMode.NONE
    - 가장 빠르다.
    - 단일 쓰레드 환경에서 추천
    - 동기화를 하지 않기 때문에 멀티 스레드 환경에서 올바른 동작을 보장할 수 없다.
    - 이로 인해 NPE가 날 확률이 존재한다.


```
정도가 될 것이다.

## Delegates의 observable와 vetoable조합

하나의 프로퍼티에 대해서 하나의 위임을 할 수 있지만 만일 어떤 프로퍼티에 대해서 이 두개의 역할을 다 하고 싶은 경우가 있다.      

Delegates.kt의 내용을 살펴보면 내부적으로 `ObservableProperty`의 `beforeChange`, `afterChange`두개를 활용하고 있다는 것을 알 수 있다.     

```kotlin
public inline fun <T> observable(initialValue: T, crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit):
            ReadWriteProperty<Any?, T> =
        object : ObservableProperty<T>(initialValue) {
            override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(property, oldValue, newValue)
        }

public inline fun <T> vetoable(initialValue: T, crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean):
        ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean = onChange(property, oldValue, newValue)
    }
```

변경 감지는 afterChange를 오버라이드하고 있으며 변경 제한의 경우에는 변경 전에 crossinline으로 들어온 함수를 실행하고 난 이 후의 true/false에 따라 무언가를 한다는 것을 알 수 있다.     


그리고 `setValue`의 로직을 살펴보면 

```kotlin
public override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
    val oldValue = this.value
    if (!beforeChange(property, oldValue, value)) {
        return
    }
    this.value = value
    afterChange(property, oldValue, value)
}
```
`beforeChange`에서 false가 떨어지면 그냥 해당 함수가 끝이 난다.      

하지만 beforeChange의 값은 `ObservableProperty.kt`를 보면 기본값이 true이다.     

즉, `observable`의 경우에는 afterChange에 대한 로직 부분만 타게 되도록 설계가 되어 있다.

따라서 밑에 값을 할당하고 afterChange를 실행하지 않기 때문에 값이 변경되지 않는것이다. 


```kotlin
inline fun <T> observerAndVeto(initialValue: T,
                                         crossinline onChange : (property: KProperty<*>, oldValue: T, newValue: T) -> Unit,
                                         crossinline onValid: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean):
ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(property, oldValue, newValue)
    override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean = onValid(property, oldValue, newValue)
}
```

observerAndVeto라는 함수를 하나 정의한다.    

물론 Delgates의 확장함수로 작성해도 무방하다.

어쨰든 두 행위에 대한 람다 표현식을 받아야 하는 상황이다.     

변경감지를 위한 함수를 받는 onChange는 그대로 두고 beforChange의 경우에는 onValid로 받게 만든다.

```kotlin
class Custom(
    val id: Long? = null,
) {
    var name: String by Delegates.observerAndVeto(
        "Jazz",
        changeProperty()) { _, _, new ->
        validProperty(new)
    }

    private fun changeProperty() = { _: KProperty<*>, prev: String, new: String ->
        println("Member Id : ${this.id}, $prev changed to $new ")
    }

    private fun validProperty(new: String): Boolean {
        return new != "WORLD"
    }

}

```
onValid는 함수의 마지막이기때문에 후행람다로 표현했고 중간 changeProperty는 람다식으로 받을 수 있도록 처리한 부분이다.      

각 각의 private함수들은 조건에 맞춰서 작성하면 되는 부분일 것이다.

```kotlin
fun main() {

    val custom = Custom(100)
    println(custom.name)
    custom.name = "TEST"
    println(custom.name)
    custom.name = "WORLD"
    println(custom.name)
    custom.name = "COMPLETED"
    println(custom.name)
}

class Custom(
    val id: Long? = null,
) {
    var name: String by observerAndVeto(
        "Jazz",
        changeProperty()) { _, _, new ->
        validProperty(new)
    }

    private fun changeProperty() = { _: KProperty<*>, prev: String, new: String ->
        println("Member Id : ${this.id}, $prev changed to $new ")
    }

    private fun validProperty(new: String): Boolean {
        return new != "WORLD"
    }

}

inline fun <T> observerAndVeto(initialValue: T,
                                         crossinline onChange : (property: KProperty<*>, oldValue: T, newValue: T) -> Unit,
                                         crossinline onValid: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean):
        ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(property, oldValue, newValue)
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean = onValid(property, oldValue, newValue)
    }
```
실행을 하게 되면

```
Jazz
Member Id : 100, Jazz changed to TEST 
TEST
TEST
Member Id : 100, TEST changed to COMPLETED 
COMPLETED
```
observable과 vetoable의 코드가 전부 함께 조합되서 작동하는 것을 알 수 있다.       

더 좋은 방법이 있을까 고민했봤는데 딱히 좋은 방법이 생각나지 않는다.       

코드가 그리 좋아보이지 않아서 더 줄일 수 있을거 같은데도 불구하고 말이다.       

다만 `ObservableProperty`를 상속해서 커스텀 위임 클래스를 만들면 되지 않을까하는 생각까지 해봤다.            

아마도 이렇게 되면 Delegates.kt랑 똑같은 코드가 될것 같은 느낌적인 느낌이라 작업하다 말았는데 만일 있다면 언제든지 제안 부탁드리면서!!!!

# At a Glance 

애초에 이 부분은 앞에서 설명을 해야하지 않았나 싶으면서도 기본적인 방법을 알고 디테일한 부분을 다루는게 좋겠다 생각해왔다.       

다음에는 제네릭스 부분을 다루기 전에 몇 가지 놓쳤던 코틀린의 잡지식들을 좀 훝어보는 시간을 갖을 생각이다.