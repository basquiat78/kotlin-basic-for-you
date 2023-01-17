# 불변 리스트인데 추가가 된다고?

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/thinkabout/kotlin/PureFunction.kt)

[코틀린을 다루는 기술]에 보면 `순수 함수 (pure function)`와 `순수하지 않은 함수 (impure function)`에 대한 설명이 있다.

사실 우리가 개발을 하면서 이런 생각을 해본 적이 없지만 이 책에서 `순수 함수`의 필요 조건을 다음과 같이 정의한다.

```
1. 함수 외부의 어떤 것도 변이시켜서는 안된다. 내부에서 상태를 변이시키더라도 그 상태를 외부에서 관찰할 수 없어야 한다.
2. 인자를 변이시켜서는 안 된다.
3. 예외나 오류를 던져서는 안된다.
4. 항상 값을 반환해야 한다.
5. 인자가 같으면 항상 같은 결과를 내놓아야 한다.
```

근데 다음 두 개의 함수를 한번 살펴보자.

```kotlin
fun <T> addTo(element: T, list: MutableList<T>): List<T> {
    list.add(element)
    return list
}

fun <T> addElement(element: T, list: List<T>): List<T> {
    return list + element
}
```

첫 번째 `addTo()`함수는 들어오는 인자를 변이시킨다.

이유는 들어온 가변 리스트 MutableList에 요소를 더해서 반환한다.

그리고 두 번째 `addElement()`함수는 들어온 불변 리스트에 `연산자 오버로딩`을 통해 요소를 더해서 반환한다.

~~어? 불변 리스트인데 이게 가능헌거???~~

그렇다면 두 함수는 위 `순수 함수`의 정의에 의하면 `순수 함수`가 아닐까?

일단 조건 4는 만족하지만 다음 코드를 통해서 우리는 확인해 볼 수 있다.

```kotlin
fun main() {
    val mutableList = mutableListOf("A", "B", "C")
    val result = addTo("D", mutableList)
    println(result)
    println(mutableList)
}
//result is [A, B, C, D]
//mutableList is [A, B, C, D]
```
결과를 보면 `addTo()`함수에 의해서 인자로 들어온 가변 리스트 mutableList가 변이를 일으켰다.

그리고 이것이 함수 외부의 가변 리스트 mutableList까지 변이가 되었다.

이 함수의 결과로 인해서 조건 1과 조건 2번에 만족하지 못하기 때문에 이 함수는 `순수 함수`가 아니다.

두 번째 `addElement()`함수를 살펴보자. 들어온 인자는 불변 리스트이지만 `연산자 오버로드 함수`로 인해 예상과는 다른 결과를 불러온다.

챕터를 진행하면서 따로 작업을 했어야 하는 부분이지만 이 내용을 다음 챕터에서 간략하게 설명한 터라 부족한 부분이 있을 수 있다.

[Operator Overloading](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/arryanacollection#operator-overloading)

어째든 불변 리스트에 요소를 더할 수 있을까? 하지만 이 컬렉션의 `+`라는 `연산자 오버로드 함수`는 생각과는 다른 방식으로 동작한다.

`_Colletions.kt`에는 다음고 같이 정의되어 있다.

```kotlin
public operator fun <T> Collection<T>.plus(element: T): List<T> {
    val result = ArrayList<T>(size + 1)
    result.addAll(this)
    result.add(element)
    return result
}
```
어라? 코드의 내용을 살펴보면 들어온 컬렉션의 사이즈에서 1를 더한 만큼의 ArrayList를 만들어서 거기에 기존 컬렉션 정보를 넣고 들어온 인자를 더하는 방식이다.

그리고 최종적으로 새로운 불변 리스트로 반환한다.

```kotlin
fun main() {
    val immutableList = listOf("A", "B", "C")
    val newList = addElement("D", immutableList)
    println("newList is $newList")
    println("immutableList is $immutableList")
}
//newList is [A, B, C, D]
//immutableList is [A, B, C]
```
이 함수의 결과는 위부의 불변 리스트 immutableList와 들어온 인자 역시 어떤 변이도 일어나지 않았다.

즉, `addElement()`함수는 `순수 함수`이다.

자바의 Stream API나 코틀린의 컬렉션 함수들은 이렇게 새로운 객체를 반환하는 것을 이미 우리는 알고 있다.

```java
public class Main {
    public static void main(String[] args) {
        List<Number> anyNumberList = List.of(BigDecimal.ZERO, 100L, 1.1, 10F);
        List<Long> newList = anyNumberList.stream()
                                          .map(value -> value.longValue())
                                          .collect(toList());
        newList.add(10000L);
        System.out.println(anyNumberList);
        System.out.println(newList);
    }
}
//result
//[0, 100, 1.1, 10.0]
//[0, 100, 1, 10, 10000]
```

```kotlin
fun main() {
    val immutableList = listOf("A", "B", "C")
    val toMutableList = immutableList.map { it.lowercase() }
                                     .toMutableList()
    toMutableList.add("test")
    println("immutableList is $immutableList")
    println("toMutableList is $toMutableList")
}
//resutl
//immutableList is [A, B, C]
//toMutableList is [a, b, c, test]
```

단 특이점이 있다면 컬렉션 함수에 `To`가 붙어있는 변종들은 예외다.

```kotlin
fun main() {
    val immutableList = listOf("A", "B", "C")
    val otherMutableList = mutableListOf("t")
    println("otherMutableList is $otherMutableList")
    val newOtherList = immutableList.mapTo(otherMutableList) { it.lowercase() }
    newOtherList.add("DDDDDDDDDD")
    println("otherMutableList is $otherMutableList")
    println("newOtherList is $newOtherList")
}
//result
//otherMutableList is [t]
//otherMutableList is [t, a, b, c, DDDDDDDDDD]
//newOtherList is [t, a, b, c, DDDDDDDDDD]
```
일반적으로 이런 변종들은 반환보다는 상태를 변이시키는 용도로 사용한다.

즉, map을 통해 변환된 데이터를 기존의 가변 컬렉션에 추가하는 방식이다.

물론 값을 반환하지만 함수의 리턴 값은 결국 들어온 가변 컬렉션를 고스란히 리턴하기 때문에 이것은 코드를 안전하지 못하게 만드는 요인이 된다.

즉 otherMutableList나 반환된 newOtherList는 같은 녀석이다.

그래서 newOtherList에 새로운 요소를 추가하게 되면 otherMutableList에도 변이가 발생하게 된다.