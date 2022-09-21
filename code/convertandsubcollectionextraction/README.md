# 컬렉션 함수 - Convert, Sub Collection Extraction

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/convertandsubcollectionextraction/kotlin/ConvertAndSubCollectionExtraction.kt)

뭐가 더 중요한 거 없이 전부 중요한 부분이지만 실제 핵심적인 부분은 보통 컬렉션의 정보를 다루는 것이다.

그래서 일반적으로 이것을 변환 함수라고 칭하는데 크게 3가지 정도로 나눌 수 있을 것이다.

    - 매핑
    - 평탄화
    - 연관성 매핑
    - 그룹핑

사실 그룹핑을 집계로 봐야 하지 않을까 싶은데 컬렉션의 주어진 원소들을 어떤 규칙이나 요구사항에 따라 새로운 컬렉션으로 만든다는 관점에서 포함시켰다.

여기서는 원형 타입에 대한 테스트보다는 실무에서 가장 많이 사용하게 될 객체를 중심으로 설명을 하고 자 한다.

가장 흔한 방법인데 지금까지 활용했던 뮤지션이라는 객체로 설명을 해보고자 한다.

사실 뮤지션이나 Person이나 Member나 똑같겠지만 이와 관련 내용을 조금이라도 재미지게 작업하고자 한다.

# Convert

자바에서는 이와 관련해서 중간 연산이라는 표현을 한다. 대표적으로 map이 이에 해당한다고 볼 수 있다.

자바의 Stream class을 보면 IntStream, LongStream, DoubleStream이 존재한다.

원형 타입에 대한 래퍼 클래스와 오토 박싱과 관련해 이에 맞는 mapToInt/Long/Double을 제공하고 있다.

mapToObject는 내부적으로 IntFunction을 받고 있는데 위 3개의 타입별 stream에서 `Stream<U>` 로 변경할 때 사용한다.

사실 이렇게 보면 패턴에 의해 상황에 맞춰 사용할 수 있도록 제공한다는 점에서 멋지다는 느낌을 준다.

하지만 코틀린에서는 그런거 없다.

어째든 우리는 이름과 나이, 장르, 다룰 줄 아는 악기를 담은 리스트를 가지는 Musician이라는 객체를 만들어서 변환 함수에 대해서 한번 테스트해보자.

그 전에 초 간단 map을 테스트해보자.

예를 들면 어떤 문자열이 담긴 리스트에서 각 요소의 문자열 길이를 담은 리스트로 만드는 간단 예제이다.

```Kotlin
fun main() {
    val strList = listOf("abc", "defd", "agzzz", "ede", "a", "ab")

    val result = strList.map { it.length }
                        .distinct()
    println("result : $result")
    val firstDistinct = strList.distinctBy { it.length }
                               .map { it.length }
    println("firstDistinct : $firstDistinct")
}
// result : [3, 4, 5, 1, 2]
// firstDistinct : [3, 4, 5, 1, 2]
```
map에서 최종적으로 반환되는 타입으로 리스트가 생성되는 것을 알 수 있다.

문자열 길이가 같은 원소가 리스트상에 있는데 중복을 제거 하고 싶으면 distinct()을 사용할 수 있다.

어짜피 문자열 길이가 같은 원소는 중복으로 제거하기 때문에 distinctBy로 길이가 같은 원소는 중복으로 처리해서 map으로도 할 수 있을 것이다.

## 인덱스를 사용하고 싶다면?
자바에서 Stream API에서 forEach사용시 인덱스를 사용하고 싶은 경우가 생긴다.

조슈아 블로크의 [이펙티브 자바]나 통상적인 방법을 고려해 보면 이것을 위해 쓰레드 안정성과 원자성을 보장해주는 객체를 제공한다.

자바로 된 코드를 한번 살펴보자면

```java
public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("1", "2", "3", "4");
        // 초기값은 1부터 시작
        AtomicInteger index = new AtomicInteger(1);
        list.stream()
             .forEach(str -> {
                 // index로부터 값을 취하고 하나를 증가시켜준다.
                 System.out.println(Integer.parseInt(str) * index.getAndIncrement());
             });
    }
}
```
위와 같이 사용할 수 있다.

또는 다음과 같이

```java
public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("1", "2", "3", "4");
        AtomicInteger index = new AtomicInteger(1);
        list.stream()
            .mapToInt(str -> Integer.parseInt(str) * index.getAndIncrement())
            .forEach(System.out::println);
    }
}
```
어떤 방식을 사용해도 같은 결과를 얻을 수 있다.

그렇다면 문득 궁금해 질것이다.

지금까지 진행해오면서 forEach에 대해서 언급해 본적이 없지만 코틀린에서는 저 인덱스를 어떻게 사용할 것인가에 대해서 말이다.

하지만 퍼득 이런 느낌도 들것이다. 앞서 filter의 경우도 그렇고 aggregation관련 Indexed라는 suffix가 붙은 함수들이 떠오를 것이다.

여러분의 느낌이 맞다.

## forEachIndexed, mapIndexed

그렇다면 위 코드를 어떻게 변경할 수 있을까라는 질문은 여러분들에게 그저 'a piece of cake'아닐까?

```kotlin
fun main() {
    val list = listOf("1", "2", "3", "4")
    list.forEachIndexed { i, value ->
        println(value.toInt() * (i + 1))
    }

    list.mapIndexed { i, value ->
        value.toInt() * (i + 1)
    }.forEach { value ->
        println(value)
    }
}
```

index를 사용할 수 있다는 것을 보기 위한 예제 코드로 모양새가 영 좋지 않지만 충분히 그 의도는 전달된다고 생각한다.


## 이런 시나리오는 어떨까?

```
여러명의 재즈 뮤지션이 있다.

피아니스트 Dave Brubeck의 1994년 음반 <Young Lions & Old Tigers>이 있다.

당시를 기준으로 신세대 뮤지션을 Young Lions로 그리고 베테랑 선배 뮤지션을 Old Tigers로 표현한 것이다.

바로 이 신세대 뮤지션과 선배 뮤지션들의 콜라보를 다룬 작품이다.

나이의 기준은 딱히 없지만 30세를 기준으로 이하는 Young Lions로 그리고 나머지를 Old Tigers라고 하자.

```

조건을 기준으로 해당 객체에 generation이라는 멤버 변수에 Young Lions와 Old Tigers를 표시해보자.

물론 이것을 enum으로 처리해도 상관없지만 여기서는 그냥 문자열로 한번 처리하는 방식을 가져보고자 한다.

## partition()로 어떻게 안되겠니?

```kotlin
fun main() {
    val musicians = listOf(
        Musician("Joe Lovano", 69, "Jazz", "Saxophones"),
        Musician("Richie Sambora", 63, "Rock", "Guitars"),
        Musician("Brad Mehldau", 52, "Jazz", "Piano"),
        Musician("BE'O", 22, "Hiphop", "Rap"),
        Musician("Julius Rodriguez", 24, "Jazz", "Piano"),
        Musician("Makaya McCraven", 38, "Jazz", "Drums"),
        Musician("Immanuel Wilkins", 24, "Jazz", "Saxophones"),
    )

    val (youngLions, oldTigers) = musicians.partition { it.age <= 25 && it.genre == "Jazz" }
    println("youngLions : $youngLions")
    println("oldTigers : $oldTigers")

}

/**
 * 뮤지션 클래스
 * generation은 시대에 따라 변경될 수 있기 때문에 var로
 *
 */
data class Musician(
    val name: String,
    val age: Int,
    val genre: String,
    val instrumental: String,
    var generation: String? = "",
)
```
자 위 코드는 바로 그 partition을 이용한 코드이다.

의도는 조아따~~

youngLions는 의도한 대로 나오지만 oldTigers가 문제다.

왜냐하면 조건 이외의 원소를 담은 리스트가 나오기 때문에 실제로는 oldTiger를 한번 더 filter를 걸어줘야 한다.

가장 고전적인 방식은 각각의 리스트를 filter와 map을 통해서 처리해 보자.


```kotlin
fun main() {
    val musicians = listOf(
        Musician("Joe Lovano", 69, "Jazz", "Saxophones"),
        Musician("Richie Sambora", 63, "Rock", "Guitars"),
        Musician("Brad Mehldau", 52, "Jazz", "Piano"),
        Musician("BE'O", 22, "Hiphop", "Rap"),
        Musician("Julius Rodriguez", 24, "Jazz", "Piano"),
        Musician("Makaya McCraven", 38, "Jazz", "Drums"),
        Musician("Immanuel Wilkins", 24, "Jazz", "Saxophones"),
    )

    val youngLions = musicians.filter { it.age <= 25 && it.genre == "Jazz"}
                              .map {
                                    it.generation = "Young Lions"
                                    it
                              }
    val oldTigers = musicians.filter { it.age > 25 && it.genre == "Jazz"}
                             .map {
                                    it.generation = "Old Tigers"
                                    it
                             }
    println("youngLions : $youngLions")
    println("oldTigers : $oldTigers")
}

/**
 * 뮤지션 클래스
 * generation은 시대에 따라 변경될 수 있기 때문에 var로
 *
 */
data class Musician(
    val name: String,
    val age: Int,
    val genre: String,
    val instrumental: String,
    val generation: String? = "",
)
```
map에서 해당 객체에 generation을 세팅해주고 it를 반환하는 요상한 코드 모양이지만 작동은 한다.

하지만 실제로 이 코드는 그냥 이해를 위한 코드이다.

만일 JazzMusician이라는 다른 객체로 변환한다면 어떨까?

```kotlin
fun main() {
    val musicians = listOf(
        Musician("Joe Lovano", 69, "Jazz", "Saxophones"),
        Musician("Richie Sambora", 63, "Rock", "Guitars"),
        Musician("Brad Mehldau", 52, "Jazz", "Piano"),
        Musician("BE'O", 22, "Hiphop", "Rap"),
        Musician("Julius Rodriguez", 24, "Jazz", "Piano"),
        Musician("Makaya McCraven", 38, "Jazz", "Drums"),
        Musician("Immanuel Wilkins", 24, "Jazz", "Saxophones"),
    )

    /**
     * 모든 장르의 뮤지션들을 담은 리스트에서 Young and old, 장르가 재즈인 뮤지션을 거르고 JazzMusician이라는 객체로 변환해서 리스트에 담는다.
     */
    val youngLions = musicians.filter { it.age <= 25 && it.genre == "Jazz"}
                              .map { JazzMusician(name = it.name, age = it.age, instrumental = it.instrumental, generation = "Young Lions") }

    val oldTigers = musicians.filter { it.age > 25 && it.genre == "Jazz"}
                             .map { JazzMusician(name = it.name, age = it.age, instrumental = it.instrumental, generation = "Old Tigers") }

    println("youngLions : $youngLions")
    println("oldTigers : $oldTigers")

}

/**
 * 뮤지션 클래스
 */
data class Musician(
    val name: String,
    val age: Int,
    val genre: String,
    val instrumental: String,
    //var generation: String? = "",
)

/**
 * 재즈뮤지션 클래스
 * 객체 자체가 장르에 특화되어 있기 때문에 genre는 설정하지 않는다.
 * generation은 시대에 따라 변경될 수 있기 때문에 var로
 *
 */
data class JazzMusician(
    val name: String,
    val age: Int,
    val instrumental: String,
    var generation: String? = "",
)
```

## 뭐 이런걸 다~

지금 살펴 본 map에는 변종들이 꽤 존재한다.

그중에 mapNotNull이나 mapIndexedNotNull같은 변종들이 있는데 앞서 배웠던 filterNotNull과 비슷한 형식이다.

예를 들자면

```kotlin
fun main() {
    val mapNotNull = listOf("1", "a", "3", "5").mapNotNull { it.toIntOrNull() }
    println("mapNotNull : $mapNotNull")
}
// restul
// mapNotNull : [1, 3, 5]
```
위 코드를 보면 리스트의 원소를 int로 변환하는데 만일 원소가 숫자형이 아니면 null로 반환하도록 하는 함수를 사용한다.

하지만 mapNotNull에 의해서 null인 원소는 제거가 되고 콘솔에 찍힌 결과를 볼 수 있다.

## filter처럼 postfix에 To를 붙어 있는 녀석도 있다고????

mapTo, mapIndexedTo같은 변종도 있다.

filter에도 이런 녀석이 있다는 것을 기억할 것이다.

[postfix에 To를 붙여보자.](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/aggregationandfilter#postfix%EC%97%90-to%EB%A5%BC-%EB%B6%99%EC%97%AC%EB%B3%B4%EC%9E%90)

```kotlin
fun main() {
    val intList = mutableListOf<Int>()
    listOf("1", "3", "5").mapTo(intList) { it.toInt() }
    println("intList 1 : $intList")
    listOf("7", "8", "9").mapTo(intList) { it.toInt() }
    println("intList 2 : $intList")
}
// result
//intList 1 : [1, 3, 5]
//intList 2 : [1, 3, 5, 7, 8, 9]
```

## map을 Map에 사용할 수도 있나요?

사실 이 변환함수의 반환형은 리스트이다.

따라서 Map에도 사용할 수 있지만 리스트로 반환되기에 주의를 요한다.

예를 들면 다음과 같다.

```kotlin
fun main() {
    val exampleMap = mapOf(1 to "one", 2 to "two", 3 to "three")
    val mapToList = exampleMap.map {
        "${it.key} ${it.value.uppercase()}"
    }
    println("mapToList : $mapToList")
}
// result
// mapToList : [1 ONE, 2 TWO, 3 THREE]
```

만일 여러분이 Map객체의 key나 value에 대한 변환을 원한다면 그에 맞춰 변환함수를 선택해야 한다.

그래서 mapKeys와 mapValues중 하나를 선택해서 사용할 수 있다.

예를 들어 위에 샘플로 만든 맵의 key를 10을 곱한 Map을 만들고 싶다면

```kotlin
fun main() {
    val exampleMap = mapOf(1 to "one", 2 to "two", 3 to "three")
    val mapKeyMultipleTen = exampleMap.mapKeys { it.key * 10 }
    println("mapKeyMultipleTen : $mapKeyMultipleTen")
}
// result
// mapKeyMultipleTen : {10=one, 20=two, 30=three}
```

이렇다는 것은 다음과 같이 생각해 볼 수 있다.

예제 맵의 value를 대문자로 변환하고 싶다면

```kotlin
fun main() {
    val exampleMap = mapOf(1 to "one", 2 to "two", 3 to "three")
    val mapValuesUppercase = exampleMap.mapValues { it.value.uppercase() }
    println("mapValuesUppercase : $mapValuesUppercase")
}
// result
// mapValuesUppercase : {1=ONE, 2=TWO, 3=THREE}
```

## flatMap과 flatten

처음 lodash같은 컬렉션 함수 라이브러리를 접했을때 이 flatMap에 대해서 처음에는 이해를 하지 못했다.

스프링 부트의 비동기 서버를 구현할 수 있는 WebFlux를 사용하다보면 자주 보게 되는 것중 하나가 flatMap이다.

이런 것을 한번 생각해 보자.

어떤 서비스의 함수를 통해서 리스트 형식의 정보를 가져온 것을 0:N으로 표현하는 Flux로 반환하는 경우가 생긴다.

하지만 이로 인해 List<String>을 한번 더 감싸 사이즈가 1인 List<List<String>> 형식으로 변환되는 경우가 있다.

또는 리액터 스트림즈에서 map에서 다른 api를 호출하고 반환되는 값이 List라면 그 최종 값은 List<List>같은 형식이 된다.

즉 Flux<Flux>같은 중첩된 형식이 된다는 것이다.

```
[[1,2,3,4,5]] 

또는 

[[1,2], [1,3], [4,9], [10, 11]]

```
즉 위와 같은 형식이 될것이다. flatten이라는 의미는 평탄화하다는 의미를 지니고 있는데 이것을 하나의 리스트로 평탄화하는것을 의미한다.

만일 비지니스 로직이 각 모든 리스트의 원소를 순회해야하는 경우라면 중첩된 루프가 될 확률이 높다.

결국

```
[[1,2,3,4,5]] -> [1,2,3,4,5]

또는

[[1,2], [1,3], [4,9], [10, 11]] -> [1, 2, 1, 3, 4, 9, 10, 11]
```
처럼 하나의 리스트로 평탄화시키고 루프를 도는 것이 가독성도 높아지고 코드가 한결 깔끔해 질것이다.

물론 요구서항에 따라 중복된 요소를 제거하기도 수월하다.

만일 이것을 여러분들이 map을 통해서 하고자 한다면 다음과 같이 작성해야 할것이다.

```kotlin
fun main() {
    val testList = listOf(listOf(1,2), listOf(6,2), listOf(3,4))
    val flatResultByMap = mutableListOf<Int>()
    // 루프를 돌면서 mapTo를 통해 리스트 안의 리스트의 각각의 원소를 리스트에 추가하는 방식으로 진행한다.
    testList.forEach { elem ->
   		elem.mapTo(flatResultByMap) { it }
    }
    println("flatResultByMap : $flatResultByMap")
}
```

하지만 이런 방식은 아무래도 중첩된 루프의 형태를 띄기 때문에 가독성이 떨어진다.

그래서 flatMap과 flatten을 활용함으로 코드를 간결하게 가져가는 방법을 취하는게 좋다.

```kotlin
fun main() {
    val testList = listOf(listOf(1,2), listOf(6,2), listOf(3,4))
    println("testList : $testList")
    val flatMapResult = testList.flatMap { it.toList() }
    println("flatMapResult : $flatMapResult")
    val flattenResult = testList.flatten()
    println("flattenResult : $flattenResult")
}
```

lodash의 경우에는 해당 리스트안에 또 리스트가 원소로 있을 경우 deep이나 depth을 통해서 어디 뎁스까지 평탄화를 할 것인지 선택할 수 있다.

다만 코틀린에서는 이것이 불가능하기 때문에 좀 더 복잡하게 로직을 짜야 할것이라는 것이 생각이 들겠지만 사실 이런 케이스가 존재할까 싶다.

실제 비지니스 로직상에서라면 다음과 같은 시나리오를 가진 형태가 될 것이다.

## flatMap의 실제 사용 사례

컬렉션 함수 내에서 어떤 서비스 또는 비지니스 로직을 호출해서 값을 가져오는 경우일 것이다.

만일 그런 메소드 또는 함수가 있다면 해당 함수는 리스트 형식의 값을 반환할 것이고 모든 요소를 루프해야 하는 경우 이것을 flatMap을 통해서 평탄화를 할 수 있다.

다음 코드를 살펴보자

```kotlin
fun main() {
    val resultList = (1..3).toList()
                           .map { fetchNumberList(it) }    
    println("resultList : $resultList")
}

/**
 * 파라미터를 통해서 어떤 리스트를 만들어내는 임시 메소드
 */
fun fetchNumberList(value: Int): List<Int> {
    return Array(3) { (it +1) * value }.toList()
}

//result
// [[1, 2, 3], [2, 4, 6], [3, 6, 9]]
```
위의 결과는 예상된 결과일 것이다.

하지만 시나리오에서 모든 원소를 루프를 돌아야 한다면 해당 리스트는 평탄화 작업을 해야 한다.

따라서 다음과 같이 코드를 수정하면 원하는 결과를 얻을 수 있게 된다.


```kotlin
fun main() {
    val resultList = (1..3).toList()
                           //.map { fetchNumberList(it) }
                           .flatMap { fetchNumberList(it) }
    println("resultList : $resultList")
}

/**
 * 파라미터를 통해서 어떤 리스트를 만들어내는 임시 메소드
 */
fun fetchNumberList(value: Int): List<Int> {
    return Array(3) { (it +1) * value }.toList()
}

//result
// [1, 2, 3, 2, 4, 6, 3, 6, 9]
```
물론 중복된 값은 사용하지 않는다고 한다면 distinct()를 통해서 중복제거도 할 수 있다.

대부분은 저런 형식으로 어떤 서비스의 메소드나 함수가 이런 형식일 때 사용할 수 있다.

## flatMap도 몇가지 변종이 존재한다.

앞서 배웠던 mapTo, mapIndexed같은 변종 함수가 flatMap도 존재한다.

여기서는 지금까지의 경험과 짭바를 통해서 여러분들은 어떨 때 사용할 지 이미 알고 있을 것이다.

그래서 코드는 생략한다. ~~귀찮다 솔직히...~~

## associate 관련 함수

Map를 map을 이용할 때는 리스트 형식으로 반환된다는 것을 위에서 언급한 적이 있다.

그런데 List를 Map형식으로 바꿔주는 함수가 존재하는데 그것이 바로 associate관련 함수들이다.

### associateWith vs associateBy

이 두개는 차이가 있다.

with가 붙은 경우에는 해당 리스트의 원소의 값을 key로 람다에서 정의한 값을 value로 같은 맵을 반환한다.

하지만 by의 경우에는 반대가 된다.

맵의 키는 중복이 되지 않는다는 특성을 누구나 잘 알 것이다. 따라서 마지막에 해당하는 것이 최종 맵의 버킷에 들어가게 된다.

결과를 보면 딱 알 수 있다.


```kotlin
fun main() {
    val myList = arrayOf("banana", "apple", "peach", "orange")
    println("associateWith result : " + myList.associateWith { it.length })
    println("associateBy result : " + myList.associateBy { it.length })
}
// result
// associateWith result : {banana=6, apple=5, peach=5, orange=6}
// associateBy result : {6=orange, 5=peach}
```

### 그래서 associate가 존재한다.

리스트의 정보를 가지고 원하는 Map의 형태로 만들고 싶다면 위의 함수로는 제한이 있다.

다음 코드를 보자. 우리가 알아야 하는 것은 Map을 만들 때 중위 함수 'to'를 통해서 만든다는 사실만 알면 된다.

~~이미 알고 있는 사실이잖아!!~~

```kotlin
fun main() {
    val myList = arrayOf("banana", "apple", "peach", "orange")
    println("associate one result : " + myList.associate { it to it.length })
    println("associate two result : " + myList.associate { it.length to it.uppercase() })
}
//result
// associate one result : {banana=6, apple=5, peach=5, orange=6}
// associate two result : {6=ORANGE, 5=PEACH}
```

## associate관련 함수에도 변종이 있다.

이 함수의 경우에는 To가 붙은 변종이 존재한다.

즉 기존의 Map에 변환된 정보를 추가해 주는 함수가 있다.

associateTo, associateByTo, associateWithTo가 있으며 필요해 따라서 선택할 수 있다.

## groupBy

리스트의 정보에서 특정 변수나 정해진 상황에 따른 값을 키로 리스트의 객체들을 묶을 필요가 있다.

java에서는 당연히 Stream API를 사용해서 처리할 수 있고 코틀린에서도 이것을 위해 groupBy를 지원한다.

가장 기초적인 방법부터 한번 알아보자.

```Kotlin
fun main() {
    val groupByResult = listOf("One", "two", "Three", "four", "Five").groupBy { it.first().toUpperCase() }    // Key
    println("groupByResult : $groupByResult")
}
// result
// groupByResult : {O=[One], T=[two, Three], F=[four, Five]}
```

위 코드는 다음과 같이 설명할 수 있다.

groupBy를 하는데 각 요소의 값의 첫 번째, 즉 문자열이기 때문에 첫번째 문자를 가져와서 대문자로 만들고 각 원소들을 돌면서 이것이 같은 하나의 그룹핑으로 묶는다.

코드의 결과를 보면 알 수 있다.

그렇다면 실제 업무에서는 어떻게 사용할 수 있을까.

지금 테스트해왔던 Musician 객체를 담은 리스트가 있다고 할 때 장르별로 묶어서 맵을 만들고 싶은 경우가 있기 마련이다.

이럴 때 이 groupBy를 통해 쉽게 그루핑을 할 수 있다.

```kotlin
fun main() {
    val musicians = listOf(
        Musician("Joe Lovano", 69, "Jazz", "Saxophones"),
        Musician("Richie Sambora", 63, "Rock", "Guitars"),
        Musician("Brad Mehldau", 52, "Jazz", "Piano"),
        Musician("BE'O", 22, "Hiphop", "Rap"),
        Musician("Julius Rodriguez", 24, "Jazz", "Piano"),
        Musician("Makaya McCraven", 38, "Jazz", "Drums"),
        Musician("Immanuel Wilkins", 24, "Jazz", "Saxophones"),
    )
    
    val musicianGroupByGenre = musicians.groupBy { it.genre.uppercase() }
    
    println("musicianGroupByGenre : $musicianGroupByGenre")
    
}
// result
// musicainGroupByGenre : 
/*
{
    JAZZ=[
            Musician(name=Joe Lovano, age=69, genre=Jazz, intstrumental=Saxophones, generation=), 
            Musician(name=Brad Mehldau, age=52, genre=Jazz, intstrumental=Piano, generation=), 
            Musician(name=Julius Rodriguez, age=24, genre=Jazz, intstrumental=Piano, generation=), 
            Musician(name=Makaya McCraven, age=38, genre=Jazz, intstrumental=Drums, generation=), 
            Musician(name=Immanuel Wilkins, age=24, genre=Jazz, intstrumental=Saxophones, generation=)
         ], 
    ROCK=[
            Musician(name=Richie Sambora, age=63, genre=Rock, intstrumental=Guitars, generation=)
         ], 
    
    HIPHOP=[
            Musician(name=BE'O, age=22, genre=Hiphop, intstrumental=Rap, generation=)
           ]
}
*/
```

사실 자바에 비해서 상당히 코드가 간결하긴 하다.

# Sub Collection Extraction

## subList()

slice()와 비슷한 느낌을 주지만 정수의 쌍을 통해서 특정 부분을 걸러낸다.

다만 배열에서는 사용할 수 없고 ArrayList나 List에 사용할 수 있다.

코드의 예를 들어보자

```kotlin
fun main() {
    // 밑에 arrayOf로 만든 배열은 subList함수가 존재하지 않는다.
    //val examArray = arrayOf(1,2,3,4,5)
    // arrayListOf 또는 listOf
    //val examArray = arrayListOf(1,2,3,4,5)
    val examArray = listOf(1,2,3,4,5)
    val fromSubList = examArray.subList(2, 4)
    println("fromSubList : $fromSubList")
}

//result
// fromSubList : [3, 4]
```
결과를 보면 알겠지만 subList의 정수의 쌍을 기준으로 처음 인덱스는 포함해서 쌍의 정수까지 걸러낸다.

그리고 4번째 인덱스를 포함하지 않는다는 것을 알 수 있다.

## 그렇다면 위에서 언급한 slice는 ???

이 녀석은 쌍이 아닌 범위로 표현하며 배열에도 사용할 수 있다.

일단 코드로 보는게 최고다.

```kotlin
fun main() {
    val examArray = arrayOf(1,2,3,4,5)
    //val fromSlice = examArray.slice(2.rangeTo(4))
    val fromSlice = examArray.slice(2..4)
    println("fromSlice : $fromSlice")
}
//result
// fromSlice : [3, 4, 5]
```
rangeTo의 중위 함수 표현식으로 두 가지 방식으로 할 수 있다.

subList와는 다르게 범위내의 인덱스를 포함한 하위 컬렉션을 추출한다.

그런데 slice()의 반환 타입은 List이다.

즉 배열의 경우 리스트가 아닌 배열로 반환받고 싶다면 이 때는 sliceArray를 이용한다.

```kotlin
fun main() {
    val arrayFromSlice = examArray.sliceArray(2..4)
    println("arrayFromSlice : $arrayFromSlice")
    println("arrayFromSlice : ${arrayFromSlice.contentToString()}")
}
//result
//arrayFromSlice : [Ljava.lang.Integer;@b81eda8
//arrayFromSlice : [3, 4, 5]
```
코드의 결과를 보면 알겠지만 sliceArray를 사용하게 되면 배열로 리턴되는 것을 알 수 있다.

단 sliceArray는 배열에만 사용가능하다.

## take와 drop

빈번하게 사용할 만한 컬렉션 함수이다.

작동 방식은 사실 filter와 다르지 않다.

하지만 조건에 대한 간결함을 유지하기 위한 함수들로 사용방법은 다음과 같다.

```kotlin
fun main() {
    val myTestList = listOf(1,2,3,4,5,6,7,8,9,10)
    val fromTake = myTestList.take(4)
    val fromDrop = myTestList.drop(4)
    println("fromTake : $fromTake")
    println("fromDrop : $fromDrop")
}
//result
//fromTake : [1, 2, 3, 4]
//fromDrop : [5, 6, 7, 8, 9, 10]
```
코드를 보면 알겠지만 filterIndexed를 통해서 구현할 수는 있다.

```kotlin
fun main() {
    val myTestList = listOf(1,2,3,4,5,6,7,8,9,10)
    val likeTake = myTestList.filterIndexed{ i, _ -> i < 4}
    val likeDrop = myTestList.filterIndexed{ i, _ -> i > 3}
    println("likeTake : $likeTake")
    println("likeDrop : $likeDrop")
}
```
위 코드는 같은 결과를 내준다.

다만 인덱스냐 길이로 자르냐에 따라 다르고 코드가 확실히 take와 drop을 사용하는 것이 더 가독성이 좋다.

Last가 suffix로 붙은 변종도 존재한다.

즉 뒤에서부터 시작하는 방식이다.

```kotlin
fun main() {
    val myTestList = listOf(1,2,3,4,5,6,7,8,9,10)
    val fromTakeLast = myTestList.takeLast(4)
    val fromDropLast = myTestList.dropLast(4)
    println("fromTakeLast : $fromTakeLast")
    println("fromDropLast : $fromDropLast")
}
//result
//fromTakeLast : [7, 8, 9, 10]
//fromDropLast : [1, 2, 3, 4, 5, 6]
```

또한 While이 suffix로 붙은 변종도 존재한다.

예를 들면 takeWhile, takeLastWhile, dropWhile, dropLastWhile이 존재한다.

이 경우에는 기존의 방식과는 다르게 원소의 값을 비교하게 된다.

그 중에 하나만 테스트 코드로 만들어 본다면

```kotlin
fun main() {
    val otherMyTestList = listOf(1,2,5,4,5,6,7,8,9,10)
    val fromTakeWhile = otherMyTestList.takeWhile { it < 5}
    val fromDropWhile = otherMyTestList.dropWhile { it < 5}
    val fromTakeLastWhile = otherMyTestList.takeLastWhile { it > 5}
    val fromDropLastWhile = otherMyTestList.dropLastWhile { it > 5}
    println("fromTakeWhile : $fromTakeWhile")
    println("fromDropWhile : $fromDropWhile")
    println("fromTakeLastWhile : $fromTakeLastWhile")
    println("fromDropLastWhile : $fromDropLastWhile")
}
// result
// fromTakeWhile : [1, 2]
// fromDropWhile : [5, 4, 5, 6, 7, 8, 9, 10]
// fromTakeLastWhile : [6, 7, 8, 9, 10]
// fromDropLastWhile : [1, 2, 5, 4, 5]
```
일반적인 take/drop 컬렉션 함수와는 달리 처음 조건에 따라 달라지는 것을 알 수 있다.

첫 요소가 5보다 작은 녀석까지만 가져오는 takeWhile과 첫 요소가 5보다 작은 녀석을 제외하고 나머지를 반환하는 dropWhile의 동작 방식을 알 수 있다.

이렇게 결과를 보면 filter와는 좀 다르다는 것을 알 수 있다.

## chunked

쓸 일이 있을지 모르겠지만 일종의 묶음으로 리스트를 만들어 주는 컬렉션 함수도 존재한다.

우선 이 함수는 배열에서는 사용할 수 없고 리스트나 시퀀스에서 사용할 수 있는 함수이다.

예를 들면 '[1,2,3,4,5,6,7,8,9,10]'같은 함수를 3개씩 묶어서 '[[1,2,3],[4,5,6],[7,8,9],[10]]' 사용하고 싶을 수 있다.

~~이런 경우가 있을까만은~~

```kotlin
fun main() {
    val myChunkedForList = arrayListOf(1,2,3,4,5,6,7,8,9,10)
    val chunked3 = myChunkedForList.chunked(3)
    println("chunked3 : $chunked3")
}
//result
// chunked3 : [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10]]
```
즉, 들어온 인자값만큼 앞 원소부터 차례대로 리스트로 묶고 나머지 역시 리스트로 묶어서 위와 같은 형식으로 만들어 주는 함수이다.

# At A Glance

위에 언급한 것 이외에도 함수들이 더 존재하는데 실상 실무에서 사용하는 경우가 거의 없기도 하다.

개인적으로 언제 써야할지도 잘 모르기 때문에 언급하지 않았지만 차 후에 이와 관련 번외로 한번 소개해 보는 시간을 가져볼까 한다.

실제로 지금까지 배운 컬렉션 함수만으로도 충분히 실무에서 조합을 해서 코드를 간결하게 만들 수 있다.

다음 스텝에서는 지금까지 언급만 했던 sequence에 대해서 소개해 볼까 한다.

이 부분은 그냥 넘어갈 생각이였지만 [이펙티브 코틀린]에서도 자주 언급되는 만큼 소개할 필요성이 있다고 생각한다.      