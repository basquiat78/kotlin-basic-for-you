# vararg와 호출 가능 참조

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/varargandcallablereference/kotlin/VarargAndCallableReference.kt)

# vararg

`Spread Syntax`라는 말이 있다.

해석하자면 `전개 구문`이라고 표현하기도 하는데 `Spread Operator`라고도 한다.

예를 들면 우리가 자주 사용하는 것중 하나인 `MessageFormat`의 `format`이라는 메서드이다.

```java
public class MessageFormat extends Format {

    public static String format(String pattern, Object ... arguments) {
        MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
    }
}
```

또한 queryDsl의 where, orderBy같은 메서드 역시 이런 `전개 구문`표현을 사용하고 있다.

queryDSL의 QueryBase.java
```java
public abstract class QueryBase<Q extends QueryBase<Q>> {
 
    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    public Q where(Predicate... o) {
        return queryMixin.where(o);
    }
}
```

위 사용법은 다음과 같이 사용할 수 있다.

```java
public class Main {

    public static void main(String[] args) {

        String a = "a";
        String b = "b";
        String c = "c";
        String message1 = MessageFormat.format("{0} {1} {2}", a, b, c);
        System.out.println(message1);

        String[] array = {"a", "b", "c"};
        String message2 = MessageFormat.format("{0} {1} {2}", array);
        System.out.println(message2);
    }

}
// result
// a b c
// a b c
```

둘 다 결과는 같다.

물론 직접 작성해서 사용할 수도 있다.

```java
public class Main {

    // 타입에 ...을 붙이거나 파라미터명 앞에 붙이거나 같이 독장한다.
    //public static int sum(int ...args) {
    public static int sum(int... args) {
        return Arrays.stream(args).sum();
    }

    public static void main(String[] args) {

        System.out.println(sum(1,2,3));

        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(sum(array));
    }

}
```

이런 스프레드 연산자는 기존 배열이나 이터러블한 객체를 하나의 개별 요소로 분리해서 다루는 방식을 말한다.

이것은 자바스크립트 역시 동일하다.

```javascript
function test(...args) {
    for(idx in args) {
        console.log(args[idx])
    }

}

test(1,2,3,4,5,6, 10000)

let array = [1,2,3,"test"]
test(...array)

```
사용하는 방식이 살짝 다르긴 해도 개념 자체는 똑같다고 보면 된다.

## 그러면 코틀린도 당연히?

자바의 경우에는 배열의 경우에는 그냥 넘길 수 있다.

~~리스트는 안된다~~

자바스크립트의 경우에는 차이가 있는데 배열을 넘길 때 앞에 `...`를 붙여서 `날 스프레드 연산자로 다뤄줘`라고 명시적으로 알려줘야 한다.

그렇지 않으면 그냥 하나의 파라미터로 인식한다.

코틀린의 경우에는 자바스크립트에 좀 더 가까운 느낌을 준다.

다만 코틀린에서는 `vararg`변경자를 붙여줘서 작성하는 방식이다.

```kotlin
fun main() {

    println(sum(1,2,3,4))

    val intArray = intArrayOf(1,2,3)
    // Type mismatch: inferred type is IntArray but Int was expected
    //println(sum(intArray))
    println(sum(1,2,3,4))
    println(sum(*intArray))

}

fun sum(vararg values: Int): Int {
    return values.sum()
}
```
배열을 넘겨줄 때는 자바스크립트에 더 가까운데 코틀린은 `...`대신 코틀린의 스프레드 연산자인 `*`를 사용하는 차이점이 존재한다.

위 코드를 보면 주석 처리된 부분을 확인해 보자. 타입이 맞지 않는다고 알려준다.

`*`가 붙었기 때문에 내부적으로 스프레드 연산자로 작동하게 되면서 배열의 요소를 하나의 개별 요소로 취급해서 다루게 되는 것이다.

다만 이 `*`를 사용하게 되면 해당 배열을 복사해서 넘겨주게 되어 있다.

이게 무슨 말인지 한번 살펴보자.


```kotlin
fun main() {

    val intArray = intArrayOf(3,2,1)
    operateSort(*intArray)
    println(intArray.contentToString())
    
}

fun operateSort(vararg values: Int) {
	values.sort()
    println(values.contentToString())
}
// result
//[1, 2, 3]
//[3, 2, 1]
```
결과를 보면 넘겨받은 배열을 소팅했지만 원본인 intArray정보는 정보는 변경되지 않은 것을 알 수 있다.

하지만 이 복사는 `deep`이 아닌 `shallow`복사이기 때문에 배열 내부의 참조값은 변경될 수 있다.

이게 무슨 말일까???

```kotlin
fun main() {
    val a = intArrayOf(1,2,3)
    val b = intArrayOf(4,5,6)
    val intArrays = arrayOf(a, b)
    shallowCopy(*intArrays)
    println(a.contentToString())
    println(b.contentToString())
}

fun shallowCopy(vararg values: IntArray) {
    values[0][0] = 7
}

// result
// [7, 2, 3]
// [4, 5, 6]
```

intArray를 하나의 요소로 담은 arrayOf가 있다고 생각을 해보자.

어째든 `*`를 통해 스프레드 연산자가 작동하게 되면서 arrayOf 내의 intArray가 하나의 요소로 작동하는 방식이다.

물론 arrayOf는 복사가 되었을 것이다. 하지만 얕은 복사가 이뤄지기 때문에 배열 내부의 intArray는 참조가 복사된다.

따라서 `values[0][0]`코드는 arrayOf로 넘어온 요소의 첫 번째 a의 intArrayOf를 찾고 0번째 인덱스 값을 7로 바꾼다.

`call by reference vs call by value`

잘 아는 분들이라면 그냥 패스, 아리까리하다면 한번 저 주제로 구글링해보시는걸 추천드린다.

## vararg의 side-effect

몇 가지 제한 사항이 존재한다.

### vararg 파라미터는 둘 이상 선언하는 것은 금지

둘 이상의 `vararg`를 선언하면

```
Multiple vararg-parameters are prohibited
```
위와 같이 에러를 보여준다.

다만 스프레드 연산자 `*`와 혼용해서 사용이 가능하다.

예를 들면

```kotlin
fun main() {

    val a = intArrayOf(1, 2, 3)
    val b = 10
    println("result is ${sum(10, 20, *a, b)}")
}

fun sum(vararg values: Int): Int {
    values.forEach {
        println("$it")
    }
    return values.sum()
}
// result
//10
//20
//1
//2
//3
//10
//result is 46
```
과 같이 사용이 가능하다.

위와 같이 표현을 해도 단일 배열로 합쳐져서 순서대로 이터러블하게 처리하는 것을 확인할 수 있다.

### named parameter 사용시 주의점

named parameter를 사용할 때는 주의할 것이 있다.

만일 `vararg`가 맨 마지막에 있는 것이 아니라면 `vararg`뒤에 존재하는 파라미터는 무조건 named parameter로 전달해야만 한다.

```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"
    val long: Long = 1_000_000

    varargPrint(*values, name, long)
    
}

fun varargPrint(vararg values: Int, name: String, long: Long) {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")
    println("long is $long")

}
```
위 코드를 보면 에러가 발생한다.

```
Type mismatch: inferred type is String but Int was expected
Type mismatch: inferred type is Long but Int was expected
No value passed for parameter 'name'
No value passed for parameter 'long'
```
에러를 보면 뒤에 name과 long은 Int이길 기대했지만 타입이 다르기 때문에 오류가 발생한다.

아마도 values가 `vararg`이기 때문에 뒤에 넘어오는 파라미터도 `vararg`로 인식해서 발생하는 오류이다.

따라서 저 코드는 다음과 같이

```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"
    val long: Long = 1_000_000

    varargPrint(*values, name = name, long = long)
    
}

fun varargPrint(vararg values: Int, name: String, long: Long) {
    values.forEach {
        println("$it")
    }

    println("name is $name")
    println("long is $long")

}
```
named parameter를 이용해서 `나는 vararg가 아니에요`라고 명시적으로 넘겨줘야 한다.

저런 방식이 너무 불편하다면 차라리 `vararg`파라미터를 맨뒤로 넣어버리자.

```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"
    val long: Long = 1_000_000

    varargPrint(name, long, *values)
    //varargPrint(name, long, values = values)
    
}

fun varargPrint(name: String, long: Long, vararg values: Int) {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")
    println("long is $long")

}
```
named parameter로 `vararg`파라미터를 넘길 때는 스프레드 연산자 `*`를 생략해도 상관없다.

하지만 다음과 같은 방식은 불가능하다.

```kotlin
fun main() {
    varargPrint(name, long, values = 1, 2, 3)
}
```
이런 방식이 불가능하기 때문에 다음에 나오는 내용에 영향을 준다.

## default를 갖는 파라미터와 쓸 때는 주의해라

거두절미하고

```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"

    varargPrint(name, *values)
    
}

fun varargPrint(name: String = "no name", vararg values: Int) {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")

}
```
위 코드는 잘 작동한다.

하지만 `varargPrint`함수에서 name은 디폴트 파라미터를 가지고 있기 때문에 다음과 같이 코드를 작성할 수도 있다.


```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"

    varargPrint(*values)
    
}

fun varargPrint(name: String = "no name", vararg values: Int) {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")

}
```
하지만 위 코드는 바로 에러를 보여준다.

```
The spread operator (*foo) may only be applied in a vararg position
Type mismatch: inferred type is IntArray but String was expected
```
일단 밑에 보면 type mismatch가 눈에 띄는데 String이 오길 기대했는데 IntArray가 왔다는 의미가 된다.

즉 이것은 `vararg`파라미터가 첫 번째 파라미터인 name으로 전달된 것을 알 수 있다.

이 경우에는 두가지 방법이 있다. 차라리 `vararg`파라미터를 앞에 두거나 또는 named parameter를 사용하는 것이다.

~~으잉? 아까는 맨 뒤로 두라더니 이제는 맨 앞에 둬야되는거야????~~

```kotlin
fun main() {
    
    val values = intArrayOf(1, 2, 3)
	val name = "Basquiat"

    //varargPrint(name, *values)
    varargPrint(values = values)
    
}

fun varargPrint(name: String = "no name", vararg values: Int) {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")

}
// result
//1
//2
//3
//name is no name
```
하지만 이 방법은 여러모로 `vararg`의 장점을 살리기 힘들다.

```kotlin
fun main() {

    val values = intArrayOf(1, 2, 3)
    val name = "Basquiat"
    varargPrint(name, 1, 2, 3)
    varargPrint(values = values)

}

fun varargPrint(name: String = "no name", vararg values: Int) {
    values.forEach {
        println("$it")
    }

    println("name is $name")

}
```
위 코드를 보면 둘 다 작동하고 `vararg`의 장점을 살려서

```
varargPrint(name, 1, 2, 3)
```
같이 사용이 가능하지만 name 파라미터가 없다면

```
varargPrint(values = 1, 2, 3)
```
처럼 사용할 수 없기 때문에 `vararg`의 장점이 사라진다.

따라서 이런 경우라면 디폴트 파라미터를 맨 뒤로 옮기고 named parameter를 통해서 작성하는 방법뿐이다.

뭐가 되었든 손이 가는 코드가 된다.
```kotlin
fun main() {
    
    val name = "Basquiat"
	varargPrint(1, 2, 3, 4, 5)
    varargPrint(1, 2, 3, 4, 5, name = name)
    
}

fun varargPrint(vararg values: Int, name: String = "no name") {
    values.forEach {
        println("$it")
    }
    
    println("name is $name")

}
```

### 오버로딩에 대한 side-effect

자바 - 코틀린도 마찬가지 -에서 어떤 메서드를 호출한 때 메서드의 시그니처가 영향을 준다.

이것이 `varage`와 만나면 묘해진다.

```kotlin
fun main() {
    
    stringPrintln("a")
    stringPrintln("a", "b")
    stringPrintln("a", "b", "c")
    
}

fun stringPrintln(vararg values: String) {
    values.forEach {
        println("value is : $it")
    }
}

fun stringPrintln(str: String, value: String, item: String) {
	println("$str | $value | $item")
    
}
```
다음 코드를 보자. 함수의 시그니처가 다른 2개의 함수가 있다.

맨 처음 'a'와 [a, b]를 찍는 경우에는 당연히 `vararg`를 파라미터를 시그니처로 가지고 있는 `stringPrintln(vararg values: String)`가 선택된다

너무나 당연한 이야기지만 만일 맨 밑에 [a, b, c]를 넘기는 함수는 어떤 녀석을 타게 될까?

실제로 결과를 보면 다음과 같다.

```
result

value is : a

value is : a
value is : b

a | b | c
```
그런데 만일 밑에 함수를 디폴트 파라미터로

```Kotlin
fun stringPrintln(str: String, value: String, item: String = "test") {
	println("$str | $value | $item")
    
}
```
처럼 작성했다면 어떻게 될까? 이 때 두개의 인자를 넘기는 코드에서는 위 코드를 타게 된다.

그 이유는 `vararg`를 갖는 함수는 뭔가 뭉뜽그려 표현하는 방식이기 때문에 시그니처가 구체적이지 못한다고 판단하게 된다.

따라서 같은 각 함수가 호출될 때 구체적인 함수가 존재하면 그것이 우선순위가 되는 것이다.

`vararg`를 활용한 함수를 작성할 때는 이런 오버로딩 함수들에 대해에 어떤 영향을 끼칠 지 고민해 봐야 한다.

# Callable Reference

자바 8이후, 물론 코틀린 포함해서 유용하게 사용하는 것중 하나가 Method Reference이다.

이것을 잘 활용하면 코드가 상당히 간결해 지는 것을 아마도 여러분들은 경험했을 것이다.      

특히 Stream API를 사용하는 경우에는 이것이 정말 강력한 경험을 선사한다.        

파라미터 타입추론으로 길어지는 코드를 간결하게 만들어 주기 때문이다.      

코틀린에서는 Method Reference외에도 몇가지 reference를 제공하고 있다.

코틀린에서는 함수를 마치 함수값처럼 고차 함수에 넘기고 싶은 경우가 있다.      

예를 들면 다음 코드를 한번 보자.


```kotlin
fun main() {

    val str = "A"
    val validation: (String) -> Boolean = { it.first().isUpperCase()}

    // 이 코드는 밑에 코드라 같은 의미이다.
    //println(validUpperCase(str) { str: String -> str.first().isUpperCase() })
    println(validUpperCase(str) { validation(it) })
    println(validUpperCase(str, validation))

}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}
```
이 코드는 람다 형식의 익명 함수를 사용하는 다양한 방법일 것이다.       

그런데 곰곰히 생각해 보면 이 익명함수가 자주 사용될 것 같다는 느낌적인 느낌이 와서 하나의 함수로 빼고자 한다.

```kotlin
fun main() {
    val str = "A"
    println(validUpperCase(str) { validation(it) })
    println(validUpperCase(str, validation))

}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}
```
위와 같이 하면 여러분들의 IDE에서 마지막 코드 부분인

```kotlin
validUpperCase(str, validation)
```
이 부분에서 빨간 줄이 그어질 것이다.

```
Function invocation 'validation(...)' expected
```
즉 결국에는 위 코드처럼 람다로 감싸서 전달하는 수 밖에 없다.      

하지만 `validUpperCase`이라는 함수의 validation부분이 람다라는 것을 인지한다면 실제로 어떤 파라미터가 넘어올지 추론할 수 있다.      

이 때 바로 Callable Reference, 즉 호출 가능 참조가 가능하다.      

사용법은 간단하다.

```kotlin
fun main() {

    val str = "A"
    println(validUpperCase(str) { validation(it) })
    println(validUpperCase(str, ::validation))

}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}
```
즉 `::`를 붙여서 함숫값을 표현해주는 것이다.

클래스에도 사용할 수 있다. 

다음 코드를 살펴보자.

```kotlin
fun main() {

    // 일반적인 방식
    val charlieParker = Musician("Charlie Parker", 35)
    
    // Callable Reference로 치환해서 사용할 수 있다.
    
    val createMusician = ::Musician
    val johnColtrane = createMusician("John Coltrane", 45)
    

}

data class Musician(
    val name: String,
    val age: Int,
)
```
특이한 점은 createMusician을 IDE에서 보면 `KFunction2<String, Int, Musician>`으로 표현된다.      

아마도 내부적으로 Relection을 통해서 작업이 이뤄진다는 것을 알 수 있다.      

저 의미대로라면 2개의 변수를 사용하고 있고 뒤에 Musician을 반환한다는 의미이다.     

# Bound Callable Reference

클래스의 멤버 함수를 호출할 때 사용할 수 있는 방법인데 예제로 한번 살펴보자.

```kotlin
fun main() {

    // 일반적인 방식
    val charlieParker = Musician("Charlie Parker", 34)

    // Callable Reference로 치환해서 사용할 수 있다.

    val createMusician = ::Musician
    // 락밴드 KISS의 혀가 엄청 긴 리더이자 베이시스트
    val geneSimmons = createMusician("Gene Simmons",73)

    val isJazz1 = charlieParker::isJazzMusician
    println("Charlie Parker is Jazz Musician? -> ${isJazz1("Jazz")}")

    val isJazz2 = geneSimmons::isJazzMusician
    println("Charlie Parker is Jazz Musician? -> ${isJazz2("Rock")}")
}

data class Musician(
    val name: String,
    val age: Int,
) {

    fun isJazzMusician(genre: String): Boolean = genre == "Jazz"

}
```
사실 이런 bound callable reference의 경우에는 잘 쓰일지 모르겠다.       

하지만 내부적으로 자주 쓰일 일이 있는 경우, 예를 들면 클래스 내에 함수명이 길다면 좀 간략하게 만들어서 사용할 때는 유용할 수 있다는 생각을 한다.      

예제처럼 `isJazzMusician`이라는 긴 함수명보다 `isJazz`로 줄여서 사용할 여지가 있다는 점은 나름 괜찮아 보이긴 한다.    

# 같은 함수명 다른 시그니처의 Callable Reference

`호출 가능 참조`의 사용 방법을 보면 이런 생각이 들 것이다.

```
같은 함수명에 시그니처가 다르면 어떻게 되는거야?
```

위 예제를 하나 다시 살펴보자.

```kotlin
fun main() {
    val valid = ::validation
    println(valid("A"))
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}
```
위와 같은 경우에는 실행하게 되면 true가 떨어진다.     

하지만 다음 코드를 보자

```kotlin
fun main() {
    val valid = ::validation
    println(valid("A"))
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}

fun validation(char: Char): Boolean {
    return char.isUpperCase()
}
```
예제기는 하지만 만일 저런 코드가 존재한다면 call reference를 사용한 곳에 빨간 줄이 그어진다.

```
Overload resolution ambiguity. All these functions match.
```

이것을 통해서 call reference는 오버로딩된 함수들을 선택할 수 없다는 것을 알게 된다.

따라서 이럴 때는 저 에러의 내용대로 사용하는 함수의 시그니처를 명시적으로 작성해야 한다.

```kotlin
fun main() {
    val valid: (String) -> Boolean = ::validation
    println(valid("A"))
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}

fun validation(char: Char): Boolean {
    return char.isUpperCase()
}
```

위와 같은 경우에는 `validation`함수를 valid로 간략하게 이름을 줘서 치환해서 사용하고 있는데 직접적으로 사용하는 방법도 있다.

사실 사용할 일이 있을까 싶지만 사용하게 된다면 다음과 같이 사용이 가능하다.

```kotlin
fun main() {
    println((::validation)("A"))
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}
```

즉, call reference자체를 `(`와 `)`로 감싸서 사용할 수 있다.          

```
::validation("A")
```
아니 저렇게 사용할 수 없는 것은 불편하네???

## 확장 함수도 파라미터로 전달할 수 있다.    

당연한 이야기지만 확장함수도 우리가 위에서 테스트로 만들었던 `validUpperCase`의 람다 형식의 validation파라미터에 전달이 가능하다.

그냥 예제로 한번 살펴보자.

```kotlin
fun main() {
    println(validUpperCase("a", String::isUpperCase))
}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

fun String.isUpperCase(): Boolean = this.first().isUpperCase()

fun validation(str: String): Boolean = str.first().isUpperCase()
```

다음과 같은 방식도 가능하다.

```kotlin
fun main() {
    println(validUpperCase("a", Validation::isUpperCase))
}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

class Validation {
    companion object {
        fun isUpperCase(str: String): Boolean = str.first().isUpperCase()
    }
}
```

하지만 이런 경우는 어떨까?

```kotlin

fun main() {
    println(validUpperCase("a", Validation::isUpperCase))
}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

class Validation

fun Validation.isUpperCase(str: String): Boolean = str.first().isUpperCase() 
```
위 경우에는 클래스에 확장 함수를 사용한 경우이지만 에상과는 다르게 예러가 발생한다.     

```
Type mismatch.
Required:
(String) → Boolean
Found:
KFunction2<Validation, String, Boolean>
```
타입 미스매치가 발생하는데 Found부분을 보면 객체의 레퍼런스가 넘어온다.      

아마도 예상컨대 이런 방식은 지원하지 않는 듯 싶다.

## Property Callable Reference

프로퍼티에 대해서도 callable reference사용이 가능하다.       

솔직히 어디까지 사용할지 잘 모르겠지만 이런 특징이 있다 정도로 알고 넘어가도 좋고 좋은 아이디어가 떠오르면 적극 활용해도 상관없다.

```kotlin
fun main() {

    val album = Album("A")
    val title = album::title.getter
    val changeTitle = album::title.setter

    println(title())
    changeTitle("A To the B To The C")
    println(title())
}

data class Album(
    var title: String,
)
```
이 방식은 프로퍼티의 정보를 담고 있는 Reflection 객체다.

실제로 IDE에서 title과 changeTitle의 정보를 살펴보면

```kotlin
val title: KProperty0.Getter<String>

val changeTitle: KMutableProperty0.Setter<String>
```
와 같은 프로퍼티의 정보를 담고 있는 것을 확인 할 수 있다.

title을 `var`로 선언했기 때문에 changeTitle의 경우에는 변경가능한 프로퍼티 정보라는 것을 확인할 수 있다.     

# At a Glance

vararg의 경우에는 다른 라이브러리를 사용하다보면 자주 만날 수 있는 부분이기 때문에 잘 알아두는 것이 좋다.       

또한 Callable Reference의 경우에는 다른 건 몰라도 `Function Callable Reference`의 경우에는 람다와 함께 자주 사용되는 부분이기 때문에 익숙해 지는 것이 좋다.     

특히 코드를 간결하게 하는 방식으로 자주 사용되고 이런 코드를 빠르게 인지하고 사용하는 것이 협업시에도 도움이 되기 때문이다.       

다음 챕터는 제네릭스를 한번 다뤄보고자 한다.