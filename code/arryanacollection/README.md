# 배열과 컬렉션 프레임워크

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/arryanacollection/kotlin/ArrayNCollection.kt)

# Array in Kotlin

자바와 크게 다르진 않다.

여러분은 자바의 배열 특징을 잘 알것이다.

    - 배열의 크기는 고정되어 있다.
    - 배열의 크기는 고정되면 변경할 수 없다. 
    - 인덱스로 배열의 요소에 접근 가능하다.
    - 배열은 기본적으로 mutable하다. 따라서 인덱스로 배열의 요소를 변경가능하다.

```Java
public class Main {
    public static void main(String[] args) {
        String[] arrStr = new String[5];
        arrStr[0] = "a";
        arrStr[1] = "b";
        arrStr[2] = "c";
        arrStr[3] = "d";
        arrStr[4] = "e";

        for(String value: arrStr) {
            System.out.println(value);
        }

        arrStr[4] = "E";

        for(String value: arrStr) {
            System.out.println(value);
        }

        arrStr[5] = "f";
    }

}
```
위 코드를 보면 초기 배열의 크기를 선언한다. 크기를 선언하지 않으면 오류가 발생한다.

각 요소를 인덱스로 접근하고 인덱스로 변경가능하다.

하지만 길이가 고정되기 때문에 6번째 'f'를 배열에 넣을려는 순간 'java.lang.ArrayIndexOutOfBoundsException'이 발생한다.

그렇다면 코틀린의 배열은 어떨까?

```Kotlin
/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin

/**
 * Represents an array (specifically, a Java array when targeting the JVM platform).
 * Array instances can be created using the [arrayOf], [arrayOfNulls] and [emptyArray]
 * standard library functions.
 * See [Kotlin language documentation](https://kotlinlang.org/docs/reference/basic-types.html#arrays)
 * for more information on arrays.
 */
public class Array<T> {
    /**
     * Creates a new array with the specified [size], where each element is calculated by calling the specified
     * [init] function.
     *
     * The function [init] is called for each array element sequentially starting from the first one.
     * It should return the value for an array element given its index.
     */
    public inline constructor(size: Int, init: (Int) -> T)

    /**
     * Returns the array element at the specified [index]. This method can be called using the
     * index operator.
     * ```
     * value = arr[index]
     * ```
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    public operator fun get(index: Int): T

    /**
     * Sets the array element at the specified [index] to the specified [value]. This method can
     * be called using the index operator.
     * ```
     * arr[index] = value
     * ```
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    public operator fun set(index: Int, value: T): Unit

    /**
     * Returns the number of elements in the array.
     */
    public val size: Int

    /**
     * Creates an [Iterator] for iterating over the elements of the array.
     */
    public operator fun iterator(): Iterator<T>
}
```
그리고 각 타입별, 그러니깐 Byte, Short, Int등 이와 관련된 배열에 대한 정보는 Arrays.kt에 정의되어 있다.

여기서는 그냥 일반적인 배열에 대해서만 설명한다.

```Kotlin
fun main() {
    // 가장 기본적인 배열 생성
    // varags로 들어온 크기만큼 배열 크기가 고정된다. 
    val intArrays: Array<Int> = arrayOf(1,2,3)
    for(value in intArrays) {
        println("intArrays is $value")
    }

    // Array의 생성자를 보면 마지막 인자가 람다이다.
    // 후행 람다 형식으로 표현가능하고 이때 넘어온 it은 인덱스정보가 넘어온다.
    val intArraysLambda: Array<Int> = Array(4) {it}
    for(value in intArraysLambda) {
        println("intArraysLambda is $value")
    }

    // 배열 크기를 4로 고정하고 null로 채운다. 
    // 이때 nullable해야하기 때문에 Int?로 선언한다.
    val intArraysDefaultNull: Array<Int?> = Array(4) {null}
    for(value in intArraysDefaultNull) {
        println("intArraysDefaultNull is $value")
    }

    // 이것은 위에 방식을 API를 이용해서 생성할 수 있다.     
    val intArraysWithNull: Array<Int?> = arrayOfNulls(4)
    for(value in intArraysWithNull) {
        println("intArraysWithNull is $value")
    }

    val arrayOf: Array<Int> = arrayOf(1,2,3,4,5)
    // set으로 접근해도 되지만 IDE에서는 
    // 밑의 [index] 방식을 추천한다.
    arrayOf.set(0, 100)
    println(arrayOf[0])
    arrayOf[0] = 200
    println(arrayOf[0])

    val empty: Array<Int> = emptyArray()

}
```
코드를 보면 몇 가지 특징들이 있지만 자바와 다르지 않다.

마지막 emptyArray()의 경우에는 조슈아 블로크의 [이펙티브 자바]의 내용을 적용할 때 사용할 수 있다.

null을 반환하기 보다는 빈 리스트나 빈 배열을 보내주는 것이 좋다는 내용에 따라 선택지로 사용할 수 있다.

# List in Kotlin

코틀린도 자바와 큰 차이점이 없다.

다만 몇 가지 특징이 있는데 일단 자바에서는 'Arrays.asList'를 통해 리스트를 생성하는 API가 있고 자바 9이후 'List.of'를 제공하고 있다.


```java
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> listOf = List.of("a", "b", "c");
        List<String> asList = Arrays.asList("a", "b", "c");
    }
}

```
다만 이 두개의 차이점은 여기서 설명하지 않을 예정이다.

코틀린을 소개하기 위한 곳이기 때문이다.

그렇지만 자바 9 이후 추천하는 List.of의 경우에는 내부적으로 변경/삭제/추가 등에 대해서 'UnsupportedOperationException'를 던진다.

즉 immutable한 리스트를 반환한다.

사실 코틀린의 컬렉션은 자바 컬렉션의 구조를 확장 구현한 것인데 독특한 점을 꼽자면 두 가지의 선택지를 준다는 것이다.

## immutable (불변) vs mutable (가변)

불변과 가변은 해당 리스트의 정보를 어디까지 접근가능하냐에 따라서 구분된다.

불변은 말 그대로 요소를 가져온다든가 특정 요소가 있는지 없는지에 대한 정보등을 가져올 수 만 있고 요소에 대한 조작이 불가능하다.

가변은 그 반대로 다 된다고 생각하면 될것이다.

### immutable (불변)

자바와 좀 비슷하다.

```Kotlin
fun main() {
    // 기본으로 immutable이다.
    val strList: List<String> = listOf("a", "b")
    for(value in strList) {
        println("strList is $value")
    }
    val checked = strList.contains("c")
    println("checked is $checked")

    // No set method providing array access
    // 또는 mutableList로 변경하라고 친절하게 알려준다.
    strList[0] = "c"
}
```
하지만 여기서 add나 remove같은 메소드가 IDE를 통해서 봐도 알겠지만 지원하지 않는다.

또한 밑에 코드처럼 인덱스 0의 요소를 c로 변경할려고 하면 친철하게 홍조를 띄는 것을 알 수 있다.

~~변경하고 싶어요? 그럼 mutableList로 바꾸세요!~~

그리고 독특한 API가 하나 있는데 다음과 같은 것도 있다.

```Kotlin
fun main() {
    val notNullList: List<String> = listOfNotNull("a", null, "c", null, null, "d")
    println(notNullList.size)
    for(value in strList) {
        println("notNullList is $value")
    }

    val listEmpty: List<String> = emptyList()
}
```
null을 허용하지 않는 API도 있다.

그리고 역시 자바처럼 빈 리스트를 생성해서 반환할 수 있다.

### mutable (가변)

가변형 리스트로 만들수 있는 가장 간단한 방법 2가지를 제공한다.

다음 코드를 확인해 보자.

```Kotlin
fun main() {
    val mlo = mutableListOf<String>()
    mlo.add("a")
    val alo = arrayListOf<String>()
    alo.add("aa")
    println(mlo)
    println(alo)
    mlo[0] = "aaaaa"
    alo[0] = "aaaaaaaaaaaaa"
    println(mlo)
    println(alo)

    // API가 아닌 직접 MutableList로 생성할 수 있다.
    val direct = MutableList(5) { "a[${it}]" }
    println(direct)
    direct.add("direct")
    println(direct)
}
```
근데 저 2개는 차이에 대해서는 반환하는 코드를 보면 알 수 있지만 사실 성능적으로 큰 차이를 잘 모르겠다.

근데 스택오버플로우에는 이와 관련된 내용이 있어서 한번 링크를 걸어 본다.

[Difference between ArrayList<String>() and mutableListOf<String>() in Kotlin](https://stackoverflow.com/questions/43114367/difference-between-arrayliststring-and-mutablelistofstring-in-kotlin)

내용의 요지를 보면 명시적으로 'MutableList를 씁니다' 또는 '이것은 자바에서 말하는 ArrayList입니다'부터 답변에 수많은 내용들이 있다.

가령 inline과 관련해서 - 우리는 이전에 inline과 관련된 내용을 배운 적이 있다- 인스턴스의 반환 유무부터 다양한 대답들이 있다.

한번 쯤 읽어보면 좋을 것이다.

## 배열에서 리스트로? 그리고 불변에서 가변으로?

자바에서 배열을 리스트로 변경하는 방식은 다양하다.

```java
public class Main {

    public static void main(String[] args) {
        String[] strArray = new String[]{"a", "b", "c"};
        List<String> listOf = List.of("a", "b", "c");
        List<String> asList = Arrays.asList("a", "b", "c");

        // 불가능
        //strArray[3] = "test1111";
        //listOf.add("test");
        //asList.add("test");

        // mutable하게 사용가능
        // stream으로
        List<String> arrayToListStream = Arrays.stream(strArray)
                                               .collect(toList());
        // 배열을 List.of로 만들고 ArrayList로 다시 만든다.
        List<String> mutableOne = new ArrayList<>(List.of(strArray));
        List<String> mutableTwo = new ArrayList<>(asList);
        List<String> mutableThree = new ArrayList<>(listOf);

        arrayToListStream.add("arrayToListStream");
        mutableOne.add("mutableOne");
        mutableTwo.add("mutableTwo");
        mutableThree.add("mutableThree");

        System.out.println(arrayToListStream);
        System.out.println(mutableOne);
        System.out.println(mutableTwo);
        System.out.println(mutableThree);
    }

}
```

뭔가 살짝 번잡해 보이긴 하지만 우리가 알고 있는 지식으로 충분히 쉽게 사용가능하다.

하지만 코틀린은 더 간단하다.


```Kotlin
fun main() {
    val createArray = arrayOf("a", "b", "c")
    // 사이즈 고정으로 인한
    //createArray[3] = "d"
    // immutable list로 변경
    val toList = createArray.toList()
    // immutable이라 불가능
    //toList[3] = "d"
    val toMutableList = createArray.toMutableList()
    toMutableList.add("d")

    val createImmutable = listOf("a", "b", "c")
    // immutable이라 불가능
    //createImmutable[3] = "d"
    val toMutable = createImmutable.toMutableList()
    toMutable.add("d")
}
```
코틀린에서 List는 기본적으로 불변이라고 언급했는데 toList는 불변이고 toMutableList는 함수명으로 명확하게 대변해주고 있다.

이것으로 가장 기본적인 코틀린에서의 배열과 리스트를 다루는 방식을 알아보았다.

## 자바와 다른 독특한 List의 operations

자바에서 sort나 reverse, fill, binary search 같은 기능들은 java.util 패키지에 있는 Collections통해서 따로 적용한다.

그래서 사용하기는 쉬워도 번거로움이 살짝 존재한다. 물론 이것을 감싸서 따로 기능을 제공하는 객체를 만들어서 사용할 수 있을 것이다.

하지만 코틀린의 경우에는 자체적으로 지원하도록 제공하고 있기 때문에 이 부분은 한번 살펴보길 바란다.

그런데 코틀린에서는 이 외에 자바와는 다른 독특한 API 몇 가지를 제공하고 있다.

자주 쓰일 것 같진 않지만 몇 가지 유용한 기능들이 있다.

예를 들면 List에서 어떤 요소들이 있을 때 해당 index로 접근해서 가져 올 수 있다.

그런데 만일 해당 index가 존재하지 않는다면 'ArrayIndexOutOfBoundsException'가 발생한다.

사실 이게 당연한 거지만 만일 이것을 에러를 발생하지 않고 처리하고 싶은 경우가 있을 수 있다.

이럴 경우에는 자바라면 관련 유효성 검사 로직을 따로 작성해 줘야 할 것이다.

그렇다면 코틀린에서는 어떻게???

```Kotlin
fun main() {
    val list = listOf(1,2,3,4,5)
    // ArrayIndexOutOfBoundsException Index 6 out of bounds for length 5
    //val outOfIndex = list[6]
    //println("outOfIndex is $outOfIndex")

    // ArrayIndexOutOfBoundsException라면 null로 그냥 반환한다.
    val escapeOutOfIndex = list.getOrNull(6)
    println("escapeOutOfIndex is $escapeOutOfIndex")

    // 후행 람다 전달 방식
    val defaultWhenOutOfIndex = list.getOrElse(6) { "존재하지 않는 index" }
    println("defaultWhenOutOfIndex is $defaultWhenOutOfIndex")
}
```
이런 API도 제공한다. 근데 잘 사용하는 경우를 잘 못 본거 같다.

애초에 저런 코드가 나온다는 것이 이슈라고 보는데 그럼에도 불구하고 방어 코드를 작성할때 좀 더 수월할 수 있다는 점에서 사용해 볼 만하다.

# Map in Kotlin

## 왠지 Map도 비슷할 거 같은데요?

그렇다. 눈치가 빠른 분들이라면 바로 List처럼 불변, 가변을 위한 API를 제공할 것이라는 것을 바로 알 수 있다.

다만 우리는 그 전에 Pair라는 녀석을 좀 알아 둘 필요가 있다.

## 튜플이 뭔지 아시나요?

여러분들이 SQL을 다루거나 JPA를 배울 때 Projection관련 Tuple이라는 개념을 알게 된다.

사실 JPA에서는 이 Tuple을 사용하는 방식도 있는데 너무 지저분하고 타입변환을 해야하는 단점이 있어서 거의 사용하지 않는다.

어째든 예를 들면 일종의 컬럼의 집합을 의미하는데 다음 쿼리를 보자.

```sql
-- 멤버 전체를 조회한다.
SELECT * 
    FROM MEMBER;

-- 특정 컬럼 정보만 조회한다.
SELECT name
       , age
       , gender
    FROM MEMBER;
```

튜플의 정의를 보면

```
튜플이란 데이터베이스내의 주어진 목록과 관계있는 속성값의 모음이다. 
관련 테이블에서 행한 수치 이상으로 혼합된 자료 요소를 의미한다. 
예를 들어, 지리적 위치는 가끔 2개의 수치로 인해 특성이 명확히 밝혀준다. 
한편, 튜플은 릴레이션을 구성하는 각각의 행을 의미한다.
```
이것을 보면 (name, age, gender)라는 것이 일종의 tuple이라고 할 수 있다.

코틀린에서는 Pair객체를 다루는 Tuples.kt라는 것을 제공한다.

Pair의 의미대로 쌍을 이루는 일종의 튜플 형식이고 data class로 정의되어 있기 때문에 copy기능을 활용할 수 있다.

## 2개로는 부족해요?

그래서 Triple도 제공하고 있답니다.

```Kotlin
fun main() {

    val pair = Pair(1, 2)
    println("pair is $pair")
    // immutableList로 변환
    val pairToList = pair.toList()
    println("pairToList is $pairToList")

    val triple = Triple("a", "b", "c")
    println("triple is $triple")
    // immutableList로 변환
    val tripleToList = triple.toList()
    println("tripleToList is $tripleToList")

    val copyPair = pair.copy(first = 3)
    println("copyPair is $copyPair")

    // 생성 시점에 타입이 정해지기 때문에 아래 코드는 mismatch type에러가 뜬다.
    //val copyTriple = triple.copy(third = 100)
    val copyTriple = triple.copy(third = "C")
    println("copyTriple is $copyTriple")
}
//result
//pair is (1, 2)
//pairToList is [1, 2]
//triple is (a, b, c)
//tripleToList is [a, b, c]
//copyPair is (3, 2)
//copyTriple is (a, b, C)
```
Pair와 Triple을 생성하고 copy를 할 때는 named Argument로 접근할 수 있다.

Pair와 Triple 생성시 명시적으로 어떤 타입이 들어오는지 <>을 통해 정할 수 있으며 없다면 들어온 인자값에 의해 결정되어 진다.

그래서 copy를 사용할 때는 기존의 타입에서 다른 타입으로 바꿀 수 없는 특징을 가지고 있다.

이러한 튜플을 이용해서 인자를 줄일 수 있는 기법들에 활용할 수 있다.

그럼 이 Pair가 어디에서 쓰이느냐? 아래 코드를 보자.

```Kotlin
fun main() {
    // Pair를 생성할때는 내부적으로 infix함수로 받도록 설계되어 있다.
    // 밑에 방식도 가능하지만 IDE에서는 infix form으로 사용하도록 추천하고 있다.
    //val immutableMap = mapOf<Int, String>(1.to("a"), 2.to("b"))
    // 앞이 key가 되고 to 뒤로 오는 것이 value가 되는 형식이다.
    //val immutableMap = mapOf<Int, String>(1 to "a", 2 to "b")
    val immutableMap = mapOf(1 to "a", 2 to "b")

    val mutableMap = mutableMapOf(1 to "a", 2 to "b")
    // get으로 키를 통해 value를 가져올 수 있지만
    //mutableMap.get(1)
    // IDE는 아래 방법을 추천.
    mutableMap[1] = "A"
    mutableMap[3] = "c"
    println(mutableMap)
    //val key0 = mutableMap.get(1)
    // IDE는 아래 방법을 추천.
    val key0 = mutableMap[1]
    println("key0 is $key0")
    // 자바처럼 해당 키가 존재하면 그 키에 대한 value를 반환하고 없으면 버킷에 저장 
    val result = mutableMap.putIfAbsent(1, "CCCC")
    println("result is $result")
    println(mutableMap)

    // immutableMap을 mutableMap으로
    val toMutableMap = immutableMap.toMutableMap()
    //for((key, value) in toMutableMap) {
    // 튜플안에 들어가는 변수명은 짧게 정의해서 사용할 수 있다.
    for((k, v) in toMutableMap) {
        println("key is $k and value is $v")
    }
}
```

주의할 것은 만일 아래처럼 맵을 생성할 때 제너릭 타입을 명시하지 않으면 Any로 받아치게 된다.

```Kotlin
fun main() {
    val anyMap = mapOf(1 to "a", "a" to "그렇군", "key" to 100)
    println("anyMap is $anyMap")
    for((k, v) in anyMap) {
        when(k) {
            is Number -> println("k is number")
            else -> println("k is String")
        }
        when(v) {
            is Number -> println("v is number")
            else -> println("v is String")
        }
    }
}

//result
//anyMap is {1=a, a=그렇군, key=100}

//k is number
//v is String

//k is String
//v is String

//k is String
//v is number
```
따라서 제너릭하게 사용하기 위해서는 타입을 정의해서 사용하는 것이 타입 안정성을 보장할 수 있을 것이다.

물론 타입이 일관성이 있다면 IDE에서는 해당 부분이 회색으로 처리되고 지우라고 할 것이다.

어쨰든 mapOf와 mutableMapOf의 varargs를 보면 Pair를 받게 되어 있고 Pair를 생성할 때 infix로 정의된 함수를 활용하고 있다.

우리가 제어흐름을 배울 때 배웠던 infix의 특징이 눈에 띄는 것을 알 수 있다.

또한 위쪽에 Pair에 대한 테스트 예제를 보면 알겠지만 콘솔에 찍히는 형식이 '(a, b)'같은 형식을 보여주는데 이것은 map객체를 루프를 돌때 활용된다.

그외에도 맵의 사이즈와 존재하는 키를 반환하거나 모든 값을 MutableCollection으로 반환하는 API, 키와 값이 존재 여부를 체크하는 API등 자바와 같은 기능을 제공한다.

참고로 코틀린에서는 저 두개는 LinkedHashMap을 반환하고 있다.

또한 HashMap을 반환하는 hashMapOf과 LinkedHashMap을 반환하는 linkedMapOf도 존재한다.

즉 명시적으로 자바의 컬렉션을 사용하고자 한다면 선택할 수 있다.

## 자바와 다른 독특한 Map의 operations

getOrDefault의 경우에는 자바8이후에 추가된 API로 코틀린에서도 지원한다.

이와 함꼐 다음과 같은 몇가지 API를 제공한다.

```Kotlin
fun main() {
    val checkMap = mapOf<Int, Int>(1 to 100, 2 to 200, 3 to 300)
    // 있으면 해당 키에 대한 value를 가져오고 없으면 람다표현식으로 정의한 값이 반환된다.
    val orElseOne = checkMap.getOrElse(3) { "no key" }
    println("orElseOne is $orElseOne")
    val orElseTwo = checkMap.getOrElse(4) { "no key" }
    println("orElseTwo is $orElseTwo")

    // 자바에서는 entry루프를 돌면서 key, value를 가져오는 API가 존재하는데
    // 코틀린에서 해당 key가 존재하면 value를 가져오고 
    // 없으면 의도적으로 java.util.NoSuchElementException: Key 4 is missing in the map. 에러를 발생한다.
    val value = checkMap.getValue(4)
    println("value is $value")
}
```

역시 자주 사용할 것 같진 않지만 알아두면 유용한 API이다.

# Set in Kotlin

Set의 특징은 자바와 똑같다.

    - 중복 허용하지 않는다.
    - 순서는 먹는거다.     

## 이젠 알려주지 않아도 알 수 있을 거 같아요!

자바에서는 기본적인 HashSet이외에도 들어온 순서 상관없이 오름차순으로 정렬하는 TreeSet과 들어온 순서대로 저장하는 LinkedHashSet을 제공한다.

당연히 코틀린도 마찬가지이다.     
```Kotlin
fun main() {

    // 기본은 immutable 그리고 LinkedHashSet이다.
    val immutableSet = setOf("a", "b", "c")
    println("immutableSet : $immutableSet")
    println("immutableSet size : ${immutableSet.size}")
    println("immutableSet : $immutableSet")

    // LinkedHashSet이다.
    val mutableSet = mutableSetOf("a", "b", "c")
    mutableSet.add("D")
    mutableSet.add("z")
    mutableSet.add("n")
    println("mutableSet : $mutableSet")

    // 후행 람다 전달 방식
    mutableSet.removeIf { it == "a" || it == "D" }
    println("mutableSet after removeIf : $mutableSet")

    // 예상대로 mutableSet으로 변경하는 함수가 있다.
    val immutableToMutableSet = immutableSet.toMutableSet()
    immutableToMutableSet.add("d")
    println("immutableToMutableSet : $immutableToMutableSet")

    // treeSet과 관련해서는 따로 제공하지 않고 직접 생성해서 사용한다.
    // 들어온 순서 상관없이 오름차순으로 정렬된다.
    val treeSet = TreeSet<String>()
    treeSet.add("d")
    treeSet.add("c")
    treeSet.add("z")
    treeSet.add("a")
    println("treeSet : $treeSet")
}
```
뭐 이제는 딱하면 척이다!

기본적으로 자바와 같이 contains나 remove같은 메소드를 제공하기 때문에 자바의 Set를 다룰 줄 안다면 어렵지 않다.

하지만 TreeSet의 경우에는 위와 같이 직접적으로 생성해서 사용해야한다.

다만 위에 설명했듯이 제공되는 API로 set를 생성하게 되면 기본적으로 LinkedHashSet이다.

명시적으로 'linkedHashSetOf()' 함수를 사용할 수 도 있지만 순서를 보장할 필요가 없다면 성능을 위해서 명시적으로 'hashSetOf()'함수를 이용할 수도 있다.

나머지 기능들은 컬렉션 함수를 다룰 때 같이 진행해 볼까 한다.


# 집합을 표현하다.

두 컬렉션의 모든 고유 요소에 대한 집합을 표현하는 방식이 있다.

보통은 Iterable한 컬렉션에 대해서 이것을 사용할 수 있다.

## union (합집합)

두 컬렉션의 모든 요소를 포함하는 union은 내부적으로 Set를 반환한다.

이 말의 의미는 두 컬렉션의 모든 요소를 모을 때 중복되는 것은 제거하고 모든 요소를 포함해서 반환한다는 의미이다.


```Kotlin
fun main() {
    val pList = mutableListOf(1,2,3,4)
    val nList = mutableListOf(1,6,3,8)

    //val unionList = pList.union(nList)
    val unionList = pList union nList
    println("unionList : $unionList")

    val pSet = mutableSetOf(1,2,3,5)
    val nSet = mutableSetOf(1,6,4,5,8)

    //val unionSet = pSet.union(nSet)
    val unionSet = pSet union nSet
    println("unionSet : $unionSet")

    val pArray = arrayOf(1,2,3,4)
    val nArray = mutableListOf(5,8,3,2,9,20)
    
    //val unionArray = pArray.union(nArray)
    val unionArray = pArray.union(nArray)
    println("unionArray : $unionArray")
}
// result
// unionList : [1, 2, 3, 4, 6, 8]
// unionSet : [1, 2, 3, 5, 6, 4, 8]
// unionArray : [1, 2, 3, 4, 5, 8, 9, 20]
```
결과를 보면 확실히 알 수 있다. 다만 배열의 경우에는 배열과 union되는 타겟은 Iterable해야 하기 때문에 배열과 배열의 union은 불가능하다.

이것은 다음 union 함수를 보면 알 수 있다.

```Kotlin
public infix fun <T> Array<out T>.union(other: Iterable<T>): Set<T> {
    val set = this.toMutableSet()
    set.addAll(other)
    return set
}
```
union 대상은 Iterable한 컬렉션이야하며 infix이기 때문에 'CollectionB union CollectionB'같은 표현이 가능하다는 것은 이제 다들 알고 있을 것이다.

## 그럼? 교집합(intersect)도 있나요?
물론이죠.

## 차집합(subtract)도?
당연하죠!

union과 같기 때문에 set에 대해서만 테스트 코드를 작성해 보고자 한다.

```Kotlin
fun main() {
    val beforeSet = mutableSetOf(1,2,6,3)
    val afterSet = mutableSetOf(3,7,5,8,6)
    //val intersectSet = beforeSet.intersect(afterSet)
    val intersectSet = beforeSet intersect afterSet
    println("intersectSet : $intersectSet")

    //val subtractSet = beforeSet.subtract(afterSet)
    // expected 1, 2
    val subtractSet = beforeSet subtract afterSet
    println("subtractSet : $subtractSet")
}

// result
// 교집합
// intersectSet : [6, 3]
// 차집합
// subtractSet : [1, 2]
```

# Operator Overloading

이전 스텝에서 invoke함수 관련 연산자 오버로딩에 대해 언급한 적이 있다.

보통 operator가 붙은 함수들이 이에 해당한다.

일반적으로 우리가 산술 연산자로 잘 알고 있는 plus, minus, mutiple(times in kotlin), divide같은 것은 +, -, *, / 같은 우리가 잘 알고 있는 기호로 접근한다.

코틀린에서는 이와 관련해서 다음과 같은 Convention을 제공한다.

|Function|code|
|:----:|:----:|
|plus|a + b|
|minus|a - b|
|div|a / b|
|rem|a % b|
|times|a * b|
|not|!a|
|unaryPlus|+a|
|unaryMinus|-a|
|PlusAssign|+=|
|MinusAssign|-=|
|inc|++a, a++|
|dec|–a, a–|

즉, 이것은 코틀린에서 약속된 규약이라고 보면 된다.         

여기서 설명할 내용이 아닌데 기왕지사 이렇게 된거 한번 알아보는 시간을 가져보자.

위 테이블은 왼쪽의 함수는 오른 쪽의 표현으로 가능하다는 것을 보여주는 테이블이다.

이게 무슨 소리일까?

코드로 확인하는게 최고

```Kotlin
fun main() {
    val prev = 100
    val next = "test"
    println(prev + next)
}
```
지금 위 코드는 바로 에러가 발생한다.

아마도 자바였다면 암묵적인 형 변환으로 인해 전부 스트링으로 변환되서 '100test'로 찍힐 것이다.

물론 위 코드에서 next와 prev의 순서를 바꾸면 가능하다.

```Kotlin
public operator fun String?.plus(other: Any?): String
```
위와 같은 operator 함수로 정의되어 있기 때문에 String + Int의 경우에는 String 뒤로 오는 인자에 대해서는 Any로 받아서 String으로 반환하기 때문이다.

하지만 코틀린에서는 다음과 같은 에러를 보게 된다.

```
None of the following functions can be called with the arguments supplied.
    - plus(Byte) defined in kotlin.Int
    - plus(Double) defined in kotlin.Int
    - plus(Float) defined in kotlin.Int
    - plus(Int) defined in kotlin.Int
    - plus(Long) defined in kotlin.Int
    - plus(Short) defined in kotlin.Int
```

Primitives.kt에 정의된 함수중 하나만 살펴보면

```Kotlin
public operator fun plus(other: Int): Int
```
operator로 선언된 plus 함수를 볼 수 있다. 위에 테이블의 의하면 이것은 Int + Int로 표현이 가능하다.

하지만 타입이 맞지 않기 때문에 에러가 발생하는 것이다.

자 그럼 위 코드를 한번 자바처럼 만들어 볼 수 있을까라는 생각에 도달한다.

좋다. 만들어 보자.

```Kotlin
// Int + String -> String으로 반환한다.
// operator overloading을 통해서 Int + String 연산식이 되면 스트링으로 넘어온다.
operator fun Int.plus(into: String): String {
    return "$this  (Int + String)    $into"
}

fun main() {
    val prev = 100
    val next = "10000"
    println(prev + next)
}
```

'+'에 대해서 operator overloading에 의해 위에 정의한 형식으로 작동하는 것을 알 수 있다.

'갑자기 컬렉션을 다루는데 뜬금없네요?'

그렇지 않다. 왜냐하면 이것은 컬렉션에서도 적용되기 때문이다.

그리고 컬렉션 뿐만 아니라 몇 몇 이런 방법이 적용되기도 한다.         

이게 무슨 말인지 궁금할 텐데 당연한 이야기지만 mutable한 List, Map, Set에 대해서 이런 방식으로 접근할 수 있다는 것을 먼저 알아두자.       

```Kotlin
fun main() {
    val prevList = mutableListOf(1,2,3,4)
    val nextList = mutableListOf(5,6,7,8)
    // plus -> minus를 통해서 key로 삭제할 수 있다.
    println(prevList + nextList + 100)
    // plusAssign으로 리스트를 추가하거나 요소를 추가한다.
    prevList += nextList
    prevList += 100
    println("prevList plusAssign result : $prevList")
    // minusAssign으로 요소를 삭제한다.
    prevList -= 100
    println("prevList minusAssign result : $prevList")

    val prevMap = mutableMapOf<Int, String>(1 to "Jean-Michel Basquiat", 2 to "Andy Warhol")
    val nextMap = mutableMapOf<Int, String>(3 to "Keith Haring", 4 to "Joseph Beuys")
    // plus -> minus를 통해서 key로 삭제할 수 있다.
    println(prevMap + nextMap + Pair(5, "백남준") + (6 to "Anselm Kiefer"))
    // plusAssign으로 맵을 추가하거나 요소를 추가한다.
    prevMap += nextMap
    prevMap += Pair(5, "백남준")
    prevMap += (6 to "Anselm Kiefer")
    println("prevMap plusAssign : $prevMap")
    // 특정 키로 해당 키 정보를 삭제한다.
    prevMap -= 6
    println("prevMap minusAssign : $prevMap")

    val prevSet = mutableSetOf<String>("Jean-Michel Basquiat", "Andy Warhol")
    val nextSet = mutableSetOf<String>("Keith Haring", "Joseph Beuys")
    // plus -> minus로 특정 요소를 삭제할 수 ㅣㅇㅆ다.
    println(prevSet + nextSet + "백남준")
    // plusAssign으로 Set에 요소를 추가한다.
    prevSet += nextSet
    prevSet += "백남준"
    println("prevSet plusAssign : $prevSet")
    // minusAssign으로 특정 요소를 삭제한다.
    prevSet -= "Keith Haring"
    println("prevSet minusAssign : $prevSet")
}
// result
// [1, 2, 3, 4, 5, 6, 7, 8, 100]

// prevList plusAssign result : [1, 2, 3, 4, 5, 6, 7, 8, 100]
// prevList minusAssign result : [1, 2, 3, 4, 5, 6, 7, 8]

// {1=Jean-Michel Basquiat, 2=Andy Warhol, 3=Keith Haring, 4=Joseph Beuys, 5=백남준, 6=Anselm Kiefer}

// prevMap plusAssign : {1=Jean-Michel Basquiat, 2=Andy Warhol, 3=Keith Haring, 4=Joseph Beuys, 5=백남준, 6=Anselm Kiefer}
// prevMap minusAssign : {1=Jean-Michel Basquiat, 2=Andy Warhol, 3=Keith Haring, 4=Joseph Beuys, 5=백남준}

// [Jean-Michel Basquiat, Andy Warhol, Keith Haring, Joseph Beuys, 백남준]

// prevSet plusAssign : [Jean-Michel Basquiat, Andy Warhol, Keith Haring, Joseph Beuys, 백남준]
// prevSet minusAssign : [Jean-Michel Basquiat, Andy Warhol, Joseph Beuys, 백남준]
```

실제로 kotlin.collections의 MutableCollections.kt에 보면 이와 관련 plus,minus, plusAssign, minusAssign이 operator overloading 함수로 제공된다.

따라서 콜렉션에 요소를 추가하거나 삭제하는 방식을 연산자를 통해서도 가능하다.

# At a Glance

코틀린에서 제공하는 List, Map, Set과 이와 관련 operator overloading에 대해서 알아보았다.

다음 스텝에선 여기서 다루지 않았지만 가장 빈번하게 사용하는 정렬에 대해서 알아보고자 한다. 👏