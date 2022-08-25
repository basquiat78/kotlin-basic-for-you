# 스코프 함수를 알아보자

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/scopefunction/kotlin/ScopeFunction.kt)

예정대로라면 제어흐름을 진행해야 하지만 스텝을 진행하면서 편의성을 위해 자주 쓰게 될 것 같아서 이 부분을 먼저 진행한다.

[Official Kotlin Scope Function](https://kotlinlang.org/docs/scope-functions.html)

사이트에 5개의 스코프 함수에 대한 표가 있다.

그 중에 'Object reference/Return value/Is extension function' 이 항목에 대한 내용들을 잘 살펴보고 진행해 보자.

특히 Object reference은 잘 살펴보고 시작해 보자.

그리고 스코프 함수에서는 마지막 코드가 리턴값으로 반환된다는 점을 알아두고 코드를 보자.

반환하겠다고 return을 사용하면 에러를 만나게 될 것이다.

# let

가장 심플하게 어떤 String이 있는데 이 스트링에 대해 특정 범위내에서 코드를 실행하고 싶은 경우이다.


```Kotlin
fun main() {

    val nonNullStr: String = "test"
    nonNullStr.let {
        println("str value is $it and str length is ${it.length}")
    }

    var str: String = "test"
    str.let {
        println("str value is $it and str length is ${it.length}")
    }

}

```
표에서 Object reference가 it으로 표시되어 있는데 해당 변수에 대한 스코프 블록내에서 유효한 변수로 str 자신이 된다.

근데 만일 중첩된 코드를 생각해 보자 let안에 let이 있는 경우이다.


```Kotlin
fun main() {
    var str: String = "test"
    str.let {
        println("str value is $it and str length is ${it.length}")
        var b: Int = it.length
        b.let {
            println("b length is $it")
        }
    }
}
```
물론 문제없는 코드이지만 it에 대한 변수가 가독성에 방해가 될 수 있다는 판단이 들 수 있다.

코틀린 사이트에서 스코프 함수에 대한 return value가 Lambda result라는 말에 주목해 보면


```Kotlin
fun main() {
    var str: String = "test"
    //str.let { s: String -> 
    str.let { s ->
        println("str value is $s and str length is ${s.length}")
        var b: Int = s.length
        //b.let { l: Int -> 
        b.let { l ->
            println("b length is $l")
        }
    }
}
```
이렇게 it대신에 유효한 변수명으로 치환할 수 있다.

단 명시적으로 변수 타입을 지정해서 표현할 수 도 있다.

이것은 확실히 취향 차이일 수 있으니 자신에게 맞는 방식으로 코딩하면 된다.

또한 Lambda result이기 때문에 해당 스코프 함수내에서 값을 리턴할 수 있다.


```Kotlin
fun main() {
    var firstStr = "테스트 대상"
    var secondStr = firstStr.let {
        println("first Str is $it")
        "테스트 완료"
    }
    println(secondStr)

    var typeStrVar = "스트링입니다."
    var resultInt = typeStrVar.let {
        println("typeStrVar is $it")
        10000
    }
    println(resultInt)
}
```
다음과 같이 사용할 수 있다.

이것을 자바로 표현한다고 생각해보면 코드 자체가 상당히 간결하다는 것을 알 수 있다.

다만 언제나 그렇듯 적절하게 사용하는 것이 코드 가독성에 도움이 된다고 먼저 말하고 싶다.

# run

run은 특이하게도 표를 보면 2개로 나눠서 표현하고 있다.

Object reference의 경우에는 this이거나 -로 되어 있고 Is extension function가 Yes/No로 구분되어 있다.

일반적으로 스코프 함수는 다음에 배울 with를 제외하고는 확장 함수이다.

하지만 이 run은 안철수이다.

```
나는 확장함수일 수도 있고 아닐 수도 있습니다.        
```

일단 코드로 보자.

```Kotlin
fun main() {
    var strForRun = "testistest"
    strForRun.run {
        println(this) // strForRun 그 자신
        println("strForRun length is $length") // 실제로는 this.length가 될것이지만 이것이 생략된다.
    }
}
```

무엇보다 객체를 다룰 때 참 편하다.


```Kotlin
fun main() {
    val john = John("John")
    john.run {
        println("john 객체의 이름은 $name")
    }
}

class John(
    val name: String
)
```

실제로는 john.name처럼 코드를 작성해야하는데 내부적으로 객체 자신인 예약 키워드 'this'를 Object reference로 사용하고 생략이 가능하다.

그래서 코드가 상당히 간략해진다.

다만 이런 경우는 조심해야 한다.

```Kotlin
fun main() {
    val name = "Jane"
    val john = John("John")
    john.run {
        println("john 객체의 이름은 $name")
    }
}

class John(
    val name: String
)

// result 
// john 객체의 이름은 Jane
```
어랏? 이렇게 되네??????

따라서 생략가능한 this를 붙여서 이 문제를 해결할 수 있다.

그렇다면 차라리

```Kotlin
fun main() {
    val name = "Jane"
    val john = John("John")
    john.run {
        println("john 객체의 이름은 $name") // john 객체의 이름은 Jane로 의도치 않게 이게 되네??
    }
    john.let {
        println("john 객체의 이름은 ${it.name}") // john 객체의 이름은 John 의도한대로!
    }
}

class John(
    val name: String
)
```

run역시 let처럼 반환을 할 수 있다.

```Kotlin
fun main() {
    val john = John("John")
    var johnsHelloWordByLet = john.let { it.returnJohnHelloWord() }
    var ageByLet: Int = john.let { it.age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByLet is $johnsHelloWordByLet")
    println("ageByLet is $ageByLet")

    var johnsHelloWordByRun = john.run { returnJohnHelloWord() }
    var ageByRun: Int = john.run { age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByRun is $johnsHelloWordByRun")
    println("ageByRun is $ageByRun")

}

class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}

```

run의 또 다른 사용 예제는 표에서 is extension function, 즉 확장함수로 사용하는지 여부에 대해 yes와 no로 볼 수 있다.

일반적으로 어떤 변수뒤에 .하고 스코프 함수를 사용하게 되는데 그냥 run을 사용할 경우이다.

사용하는 걸 많이 본 적은 없지만 자바스크립트에서 일종의 익명함수처럼 사용하고자 할 때 유용하게 사용할 수 있는데 예를 들어보자.

return Value부분을 보면 yes든 no든 lambda resut이기 때문에 가능한데 코드르 보는게 더 빠르다.

```Kotlin
fun main() {

    val john = John("John")

    var johnsHelloWordByLet = john.let { it.returnJohnHelloWord() }
    var ageByLet: Int = john.let { it.age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByLet is $johnsHelloWordByLet")
    println("ageByLet is $ageByLet")

    var johnsHelloWordByRun = john.run { returnJohnHelloWord() }
    var ageByRun: Int = john.run { age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByRun is $johnsHelloWordByRun")
    println("ageByRun is $ageByRun")

    // 마치 익명함수 처럼
    val helloWordCombine = run {
        var name = john.name
        var age = john.age()
        var johnsNameWord = "나의 이름은 $name 입니다."
        var johnsAgeWord = "나이는 $age 이죠."
        var hello = "반가워요"
        """$johnsNameWord 
        $johnsAgeWord 
        $hello"""

    }
    println(helloWordCombine)

}

class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}


```

# with

표에서도 알 수 있듯이 이 녀석은 확장 함수가 아니다. 따라서 사용방식이 좀 다르다.

다만 run이나 apply처럼 Object Reference가 this라 run과 똑같이 작동한다.

거두절미하고

```Kotlin
fun main() {

    val john = John("John")

    with(john) {
        println("john의 이름은 $name")
        println(age())
    }

    val johnName = with(john) {
        "${name}인건가?"
    }
    println(johnName)

}

class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}

```
문득 이런 생각이 든다.

'아니 이럴거면 뭘 이렇게 같은 동작을 하는 스코프 함수를 제공하는거야? 헛갈리게?"

나의 경우 with를 사용할 경우 보통 다른 객체에 어떤 값을 세팅할 때 with의 타겟 객체를 감싸서 사용한다.

즉 with할 타겟 객체인 A를 with라는 의미에 맞춰서

```
나는 A라는 객체와 함께 이것을 사용해서 B라는 객체에 어떤 짓거리를 하고 싶어 
```

이런 느낌


```Kotlin
fun main() {

    val john = John("John")

    val you = with(john) {
        You(yourName = name, yourAge = age())
    }
    you.let{
        println(it.yourName)
        println(it.yourAge)
    }
}

class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}

class You(
    val yourName: String,
    val yourAge: Int
)

```

당연히 이 코드는 run, let을 이용할 수 있다.


```Kotlin
fun main() {
    val john = John("John")

    val youByLet = john.let {
        You(yourName = it.name, yourAge = it.age())
    }
    println(youByLet)

    val youByRun = john.run {
        You(yourName = name, yourAge = age())
    }
    println(youByRun)
}

class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}

class You(
    val yourName: String,
    val yourAge: Int
)

//result
//You@439f5b3d
//You@1d56ce6a
```
결과를 찍어보면 You라는 객체로 생성된 것을 볼 수 있다.

with, run, let을 사용해도 똑같은 결과를 만들어 낼 수 있다.

다만 나의 경우 with의 경우에는 다루고자 하는 객체에 대해서 with의 타겟이 되는 객체의 어떤 의미있는 정보를 통해 무언가를 행할때 사용한다.

그래서 A라는 객체를 새로운 DTO나 다른 객체로 반환하고자 할 때는 오히려 let를 쓰는 경우가 많다.      

~~어쩔때는 with도 쓰고 let쓰고 그냥 내맘대로~~

어떤 특정 메소드를 실행하거나 그 특정 메소드를 실행이후 리턴값을 반환하는 경우에는 run을 사용하다.

결국 이것은 개발자의 생각이나 회사 또는 팀의 코드 컨벤션에 따라서 무엇을 사용하든 얼마든지 원하는 동작을 할 수 있도록 유연함을 갖는 것이 더 중요하다고 본다.

# apply

apply와 다음에 알아 볼 also의 경우에는 위에 스코프 함수들과 비슷하지만 Return value가 Context object라는 것을 알 수 있다.

뭔 짓을 해도 최종적으로 리턴되는 결과는 자신이라는 의미이다.

이게 무슨 말이냐? 코드로 알아보자.

```Kotlin
fun main() {
    val me = Me("Basquiat", 100).apply {
        name = "Jean-Michel Basquiat"
        age = 20
        "testtttttt"
    }
    println("${me.name} and ${me.age} and $me")
}

class Me(
    var name: String,
    var age: Int,
)

// expected result
// "testtttttt"
// but actual result
// Me@17f052a3 <- 오브젝트 Me
```

결국 자바 코드로 살펴보면


```java
public class Main {

    public Me me() {
        Me me = new Me();
        me.setName("your name");
        me.setAge(1000);
        return me;
    }

}

```
요런 식일 것이다.

```Kotlin
fun main() {
    val me = Me("Basquiat", 100).apply {
        initializeMe()
    }
    println("${me.name} and ${me.age} and $me")
}

class Me(
	var name: String,
    var age: Int,
) {
    fun initializeMe() {
        this.name = "initialized " + this.name
        this.age = this.age + 5
	}
}
```
보통 이런식으로 많이 쓰일 것이라고 본다.

# also

그렇다면 apply랑 비스무리하면서도 다른 also역시 그다지 다르지 않을 것이다.


```Kotlin
fun main() {
    val me = Me("Basquiat", 100).apply {
        initializeMe()
    }
    println("${me.name} and ${me.age} and $me")
    
    val meByAlso = Me("Basquiat", 100).also {
        it.initializeMe()
    }
    println("${meByAlso.name} and ${meByAlso.age} and $meByAlso")
    
}

class Me(
	var name: String,
    var age: Int,
) {
    fun initializeMe() {
        this.name = "initialized " + this.name
        this.age = this.age + 5
	}
}

// result
// initialized Basquiat and 105 and Me@17f052a3
// initialized Basquiat and 105 and Me@2e0fa5d3
```

# takeIf와 takeUnless

일종의 필터같은 역할은 하는 녀석이다. 즉 predicate를 사용한다.

takeIf는 어떤 조건이 맞으면 리턴을 하고 아니면 null을 던져주고 takeUnless는 그 반대라고 보면 된다.

```Kotlin
fun main() {
    val takeif = Take("notnull").takeIf{ it.value == null }
    println(takeif)
    val takeunless = Take("notnull").takeUnless{ it.value == null }
    println(takeunless)
    
}

class Take(
	var value: String? = null
)

//result
//null
//Take@685f4c2e
```
코드를 보면 Take객체의 value가 존재한다.

그래서 takeIf의 경우에는 이 값이 null이라면 해당 객체를 반환하는데 null이 아니니 null를 리턴했고 아래 코드는 그 반대기 때문에 Take객체를 반환했다.

자 그럼 이것은 메소드 체인닝을 사용할 수 있다는 것을 알 수 있다.


```Kotlin
fun main() {
    val basquiatTake = Take("basquiat")
	//val basquiatTake = Take("basquiat11111")
    val result = basquiatTake.takeIf{ "basquiat".equals(it.value) }?.let{ "바스키아입니다." } ?: "바스키아가 아닙니다."
    println(result)
    
}

class Take(
	var value: String? = null
)

```

위에 설명했듯이 이 두녀석은 조건이 맞으면 context Object, 즉 자기 자신을 반환하고 아니면 null을 반환한다.

따라서 그 이후 메소드 체이닝을 하기 위해서는 무조건 Safe call (?.)을 붙여야 한다.          

그리고 엘비스 연산자 ?:를 사용해서 null이면 다른 처리를 할 수 있다.

이것을 여러분이 자바 코드로 변경한다면 어떤 모습일지 상상해 보라.

물론 스코프 함수를 사용해서 저런 체이닝코드가 난무하면 가독성을 떨어뜨릴 수 있을 수 있다.       

그래서 적절하게 사용하는게 중요하다.

문제는 그 적절하게라는 말이 참 어려워서 그렇지.......


# At a Glance

지금까지 스코프 함수를 알아봤다.

하지만 가만히 내용을 돌아보면 몇가지 의문이 드는 점이 있다. 그리고 이것을 사용하는 여러분도 가질 수 있다.

'아니 뭔가 전부 다 똑같이 작동하는거 같은데??????'

그렇다!. 어떤 것을 사용해도 원하는 동작을 하게 만들 수 있다.

다만 나의 경우에는 단어가 주는 그 의미에 맞춰서 선택해서 각 스코프 함수가 같는 특징에 따라 그에 맞는 상황에 맞게 사용한다.    

또는 여러분이 속한 팀 또는 회사의 코드 컨벤션에 따라 진행하는 것이 좋다.

다음에는 제어흐름을 진행할 것이다. 👏
