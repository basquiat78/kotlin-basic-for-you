# 컬렉션 함수 - Aggregation, Filter Deep Dive

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/aggregationandfilter/kotlin/AggregationAndFilter.kt)

하나의 스텝에 전부 설명하기는 사실 힘들다. 게다가 여기서 이 모든 것을 소개하는 것 역시 무리수이다.

그 이유는 코틀린에서는 컬렉션에 대한 풍부한 기능을 통해 간결함을 추구하고 있으며 상당히 많은 컬렉션 함수들이 있기 때문이다.

어느 글에서 200개를 제공한다고 하니 처음 볼 법한 함수들을 만나게 될 것이다.     

참고로 이전부터 자바스크립트 진형의 lodash를 자주 언급했는데 다음 사이트를 한번 살펴보길 바란다.

[lodash API Document](https://lodash.com/docs/4.17.15)      

다른 듯 하면서도 상당히 유사한 부분이 많고 실제로 사용하는 방식도 거의 흡사하다.       

아마도 여러분들이 이와 관련 함수들을 살펴보면 실무에서 자주 사용하게 될 녀석들과 아닐 녀석들도 눈에 들어올 것이다.       

결국 이 모든 것을 다 설명할 수 있는 내용이 아니기 때문에 가장 빈번히 사용되고 실무에서도 가장 많이 사용될 함수들을 주로 소개할까 한다.

또한 자바에서 흔히 사용하는 방법과 동일한 녀석들의 경우, 예를 들면 sum같이 잘 알려진 녀석들은 건너 띌 생각이다.

그 이유는 하나씩 알아가 보면 대부분 비슷한 스타일이기 때문에 API만 보면 바로 사용할 수 있을 것이기 때문이다.

다만 자바에도 같은 기능을 하는 녀석임에도 코틀린만의 독특함이 있는 녀석들은 소개할 것이다.       

이 수많은 함수들의 기능들의 공통적인 부분을 모아보면 4개 정도로 분류해서 압축할 수 있다.

그중에 컬렉션을 만들 때 설명했던 헬퍼 함수들과 정렬은 앞서 소개를 했고 filter, aggregation, convert, sub collection extraction 정도로 나눌 수 있다.

각종 블로그나 해외 개발자들의 기술 블로그를 보면 관점에 따라서 좀 다르긴 하지만 어째든 큰 줄거리에서 얘기해보면 저 정도로 거의 설명이 가능하다.

이 스텝에서는 aggregation, 즉 집계 부분과 지금까지 사용했던 filter에 대해서 좀 더 딥 다이브로 한번 들어가 볼까 한다.

# Filter Deep Dive

## 컬렉션의 첫 번째 요소와 마지막 요소를 가져오자

자바에서는 가장 간단한 방법은 첫 번째 요소는 인덱스가 0일테니 그냥 구해진다.

마지막 요소라면 리스트의 사이즈에서 -1를 빼면 마지막 인덱스가 구해지니 해당 인덱스로 구하면 된다.

여기까지는 그다지 어렵지 않다.

다음 코드를 한번 살펴보자.

```kotlin
fun main() {
    val list = emptyList<String>()
    val first = list.first()
    val last = list.last()
    println("$first : $last")
    /*
    if(list.isNotEmpty()) {
        val first = list.first()
        val last = list.last()
        println("$first : $last")
    }
    */
}
```
하지만 저 위 코드는 실행하게 되면 NoSuchElementException 에러가 난다.

왜냐하면 빈 리스트이기 때문이다.

사실 저것을 언급한 이유는 대부분 DB나 외부 API를 통해 메세지를 보내고 어떤 컬렉션 정보를 받는다고 생각해 보자.

메세지를 보낸 입장에서는 건네 준 정보가 비어 있는지 null인지 몇개가 존재하고 있는지는 알 수 없다.

그래서 보통은 주석처리된 코드에서 볼 수 있듯이 넘겨 받은 리스트가 빈 리스트인지 먼저 확인하고 처리를 하게 된다.

코틀린에서는 이와 관련 다른 선택지를 준다.

첫 번째 또는 마지막 요소를 가져올 때 있으면 리스트가 비어 있다면 null를 반환하고 아니면 해당 요소를 가져오는 함수가 존재한다.

```Kotlin
fun main() {
    val list = emptyList<String>()
    val list = emptyList<String>()
    val first = list.firstOrNull()
    val last = list.lastOrNull()
    println("$first : $last")
}
```
따라서 위 코드를 통해 엘비스 연산자를 사용한다 null에 대한 후처리를 하기가 용이해진다.       

하지만 다음과 같이 어떤 조건을 통해 걸러낸 리스트에서 첫 번째 요소와 마지막 요소를 가져오고 싶을 수도 있다.

자바의 경우 for-loop를 돌면서 조건을 걸고 새로운 리스트에 담을 수 있다.

또는 Stream API를 이용해서 filter를 통해 조건을 걸어 그 속에서 첫 번째 요소와 마지막 요소를 가져올 수 있다.

자바의 Stream API를 사용하게 되면 마지막 요소는 reduce를 활용할 수 있는데 이게 번거롭다.

하지만 코틀린에서는 어떻게 할 수 있을까?

```Kotlin
fun filterCondition(it: Int) : Boolean {
    return it in 4..6
}

fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    val findCondition: (Int) -> Boolean = {
        it in 4..6
    }

    // 일반적인 방법
    // var filterNormal = list.filter({ it in 4..6 })
    // 후행 람다로 추천하는 방식
    // var filterNormal = list.filter { it in 4..6 }
    // 익명 함수를 활용한 일반적인 방법
    //var filterByAnonymous = list.filter(findCondition)
    // 공통된 조건을 함수로 정의한 경우
    //var filterByUseFunction = list.filter { filterCondition(it) }
    // 코틀린에서는 메소드 레퍼런스같은 의미의 함수 레퍼런스라는 것도 있다.
    var filterByUseFunction = list.filter(::filterCondition)
    println("filterByUseFunction : $filterByUseFunction")

    //val filteredFirst = list.filter(findCondition)
    val filteredFirst = list.filter { it in 3..6 }
                            .first()
    //val filteredLast = list.filter(findCondition)
    val filteredLast = list.filter { it in 3..6 }
                           .last()
}
```
아마도 처음 코틀린을 작성하게 되면 위와 같이 생각할 것이다.

여기서 예제를 보면 맨 처음 코드는 filter를 사용하는 방법을 작성한 것이다.

익명 함수로 filter에 넘기는 경우 (자바의 funtional interface처럼)와 공통된 부분이라면 함수로 만들어서 사용한 예제이다.

자바처럼 코틀린에서는 메소드 레퍼런스도 있지만 이와 비슷하게 함수 레퍼런스라는 것도 있다.

사용 방법을 보면 언급한 적 없지만 Exception을 사용하는 방식과 상당히 비슷하다는 것을 알 수 있다.

코드를 살펴본다면 이해할 수 있을 것이다.      

게다라 이 하나의 예제에서 알 수 있듯이 대부분의 컬렉션 함수에는 이런 방식을 적용할 수 있다.        

갑자기 삼천포로 빠졌는데 어쨰든 이것은 문제가 되지 않는다. 하지만 IDE에서는 merge를 하라는 경고를 하나 띄워준다.

이유는 first와 last함수는 조건을 수행하는 익명 함수를 받을 수 있는 함수도 제공하기 때문이다.

즉 filter와 결합된 형태이다.

따라서 저런 경우에는 다음과 같이 변경이 가능하다.

```Kotlin
fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    val filteredFirst = list.first { it in 3..6 }
    val filteredLast = list.last { it in 3..6 }
    println("$filteredFirst : $filteredLast")
}
```
굉장히 간결해 진다.

하지만 만일 이 코드에서 조건에 부합되는 요소가 없다면 어떻게 될까?

위에서 알 수 있듯이 이 녀석은 조건에 부합되는 요소가 없다면 NoSuchElementException에러를 던지게 되어 있다.

```Kotlin

fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    //val filteredFirst1 = list.firstOrNull { it in 9..11 } ?: "빈 리스트이기 때문에 첫 번째 요소를 가져올 수 없습니다."
    //val filteredLast1 = list.lastOrNull { it in 9..11 } ?: "빈 리스트이기 때문에 마지막 요소를 가져올 수 없습니다."
    val filteredFirst1 = list.firstOrNull { it in 9..11 }
    val filteredLast1 = list.lastOrNull { it in 9..11 }
    println("$filteredFirst1 : $filteredLast1")
}
```
따라서 위에 사용했던 함수를 활용할 수 있다.

이것의 주석 처리한 코드처럼 엘비스 연산자를 통해서 보상 로직이나 또는 따로 커스텀 익셉셥을 사용해서 에러를 던질수 있는 장점이 있다.

이 하나의 예제에서 알 수 있듯이 코틀린에서는 여러 개의 메소드 체이닝을 통한 컬렉션 함수를 하나로 머지할 수 있는 함수를 제공하기도 한다는 것을 알게 된다.

## filterNot?

부정 변형 함수라고 하는데 Not이라는 의미에서 알 수 있듯이 조건의 부정형이라는 것을 알 수 있다.       

코드로 간략하게

```
if( a != b ) ...
```
같은 경우일 것이다.      

사실 이 함수를 사용해 본적이 없다.      

솔직히 말하면 나의 경우에는 이 함수가 가끔 혼동을 불러오기 때문이다.      

하지만 사용하라고 존재하는 함수이니 한번 사용해 보자.

```Kotlin
fun main() {
    val listOfForFilter = listOf(1, 2, 10, 12, 6, 3, 4, 5)
    // 6보다 작은 요소의 부정이니 6을 포함한 그 이상의 숫자로 걸러질 것이다.
    println(listOfForFilter.filterNot { it < 6 })

    val mapOfForFilter = mapOf(1 to "abc", 2 to "befgh", 3 to "tuvwxyz")
    // key가 3보다 큰 요소의 부정이니 map의 모든 요소가 나올것이다.
    println(mapOfForFilter.filterNot { it.key > 3 })
    // 값의 문자열 길이가 3보다 큰 요소의 부정이니 (1 to "abc")만 나온다.
    println(mapOfForFilter.filterNot { it.value.length > 3 })
}
//result
// [10, 12, 6]
// {1=abc, 2=befgh, 3=tuvwxyz}
// {1=abc}
```
Map의 경우에는 자체적으로 filterKeys, filterValues 함수를 제공한다.

다만 부정 변형 함수는 없다.

```Kotlin

fun main() {
    val mapOfForFilter = mapOf(1 to "abc", 2 to "befgh", 3 to "tuvwxyz")
    // 부정 변형은 없지만 key, value에 맞춰서 사용할 수 있다.
    println(mapOfForFilter.filterKeys { it > 3 })
    println(mapOfForFilter.filterValues { it.length > 3 })
}
//result
//{} // 키값이 3보다 큰 경우는 없으니 빈 map이 나온다.
//{2=befgh, 3=tuvwxyz} // abc를 빼고 각 값의 문자열 길이는 3보다 크기 때문에
```
위와 같이 사용할 수 있다.

## filterIsInstance()?????

조슈아 블로크의 [이펙티브 자바]에는 제너릭과 관련해서 아이템 26에 다음과 같은 내용이 있다.

```
로 타입은 사용하지 말라.
```
raw type과 관련해서는 더 이상 자세하게 설명하지 않겠다.       

이유는 iterator를 통해 순회를 할때 캐스팅 오류가 발생할 수 있기 때문이다.      

사실 이걸 쓸 일이 있을까만은....

아래 코드를 한번 살펴보자.

```Kotlin
fun main() {
    val rawTypeList = listOf(1, "Basquiat", mapOf(1 to "Map"), 2, 4, "Warhol", listOf("a", "b", "c"))
    val stringList = rawTypeList.filterIsInstance<String>()
    val intList = rawTypeList.filterIsInstance<Int>()
    val mapList = rawTypeList.filterIsInstance<Map<Int, String>>()
    val listList = rawTypeList.filterIsInstance<List<String>>()
    println("stringList : $stringList")
    println("intList : $intList")
    println("mapList : $mapList")
    println("listList : $listList")
}
//result
//stringList : [Basquiat, Warhol]
//intList : [1, 2, 4]
//mapList : [{1=Map}]
//listList : [[a, b, c]]
```
이렇게 요소를 타입별로 걸러내는 함수이다.

아마도 자바에서 코틀린으로 리팩토링을 고려한다면 거의 사용할 일이 없을 수 있지만 필요에 의해 사용한다면 고려해볼 만한 함수이다.

## filterIndexed

고전적인 for-loop에서는 우리는 index를 사용하게 된다.

당연히 이 index를 사용하지 않을 수도 있지만 index 정보를 통해서 걸러낼 필요가 있을 수 있다.

이 때 사용할 수 있는 것이 filterIndexed이다.

일반적으로 filter는 배열, 리스트 혹은 맵의 각 요소들을 돌면서 해당 요소를 it으로 넘겨준다.

하지만 인덱스가 필요할 때는 이것을 통해서 튜플 형식으로 인덱스와 요소 (index, element)를 넘겨주는 녀석이다.

앞서 배운대로 인덱스만 사용하고 요소를 조건을 태우지 않는다면 '_'로 표현할 수 있다

```Kotlin
fun main() {
    val listByIndex = listOf(1, 2, 10, 12, 6, 3, 4, 5)

    // 인덱스와 요소의 값으로 무언가를 해볼 때 사용하는 함수이다.
    val filteredByIndex = listByIndex.filterIndexed { i, _ -> i > 3 }
    val filteredByValue = listByIndex.filterIndexed { _, v -> v > 4 }
    println("filteredByIndex : $filteredByIndex")
    println("filteredByValue : $filteredByValue")
}
```

## filterNotNull

listOfNotNull을 기억할 것이다. 요소에 null이 있다면 이것을 제거해주는 헬퍼 함수인데 이것을 filter로 제공한다.

```Kotlin
fun main() {
    val listIncludeNull = mutableListOf(1, 2, null, 12, 6, null, 4, 5)
    println("original : $listIncludeNull")
    val filteredNotNull = listIncludeNull.filterNotNull()
    println("filteredNotNull : $filteredNotNull")
}
//result
//original : [1, 2, null, 12, 6, null, 4, 5]
//filteredNotNull : [1, 2, 12, 6, 4, 5]
```
사용하는 경우가 있을지 모르겠지만 어떤 리스트에 null 있을 수 있다는 상황에 놓여진다면 유용한 함수이다.      

## postfix에 To를 붙여보자.

지금까지 소개한 filter의 함수들은 자바와는 다르게 조건 검사를 실행 이후에 불변 컬렉션을 반환하게 되어 있다.

그래서 filter이후의 반환된 컬렉션에는 추가/삭제를 할 수 없다.

IDE에서는 애초에 관련 API가 뜨지도 않는다.

물론 불변 컬렉션은 가변 컬렉션으로 쉽게 바꾸는 함수를 앞서 배웠기 때문에 문제가 되진 않는다.          

사실 이런 함수들은 코드의 편의성을 위해 존재하는 경우로 하나만 소개해 보자.

시나리오는 이렇다. 어떤 리스트로부터 조건을 걸러내고 다른 가변 컬렉션에 추가하는 것이다.

```Kotlin
fun main() {
    val mutableList = mutableListOf<Int>()
    val lisTo = listOf(6,1,7,9,3,11)

    // 고전적인 방식
    //val filterFromMutableList = listTo.filter { it > 5 }
    //mutableList.addAll(filterFromMutableList)

    // To함수를 이용해서 
    println("before mutableList : $mutableList")
    list.filterTo(mutableList) { it > 5 }
    println("after mutableList : $mutableList")
}
```
일반적인 방식은 리스트로부터 필터를 거쳐 나온 리스트를 mutableList에 addAll하는 방식이다.

이 뒤에 To가 붙은 filter함수들은 위와 같이 조건에 걸려진 리스트를 해당 리스트로 자동으로 더해주는 함수들이다.

이런 디테일까지 제공한다.

~~두 줄 코딩보다 한줄로 끝내는게 좋아보이지 않나?~~

## partition

고랭에서 함수는 여러 개의 값을 반환을 할 수 있다.

이게 참 자바를 사용하다 보면 엄청 부럽다. 왜냐하면 자바는 그렇게 할 수 없기 때문에 리스트나 맵 또는 dto를 통해서 여러 개의 결과값을 반환해야 한다.

두 개의 결과값이 있는데 이것을 반환하기 위해서는 컬렉션이나 객체에 매핑해서 반환해야 하는 불편함이 존재한다.

이와 관련 코틀린에서는 파티션이라는 독특한 함수를 제공한다.

내부적으로 Pair를 활용해서 반환하기 때문에 튜플 형식으로 반환하게 된다.

이때 첫 번째 요소는 조건에 해당하는 컬렉션이고 두 번째는 조건의 부정에 해당하는 컬렉션이다.

참고로 이 함수는 리스트, 시퀀스에만 해당하는 함수이다.

예를 들어 1부터 10까지의 어떤 리스트의 값에서 홀/짝를 모아서 각기 다른 리스트로 무언가를 해보고 싶을 것이다.

자바라면 아마도 2번의 공수가 들것이다. 하지만 코틀린은 한큐에 끝내버린다.

```Kotlin
fun main() {
    //2로 나눈 몫이 0이면 짝수
    val (evens, odds) = (1..10).toList()
                               .partition { it % 2 ==0 }
    println("evens : $evens")
    println("odds : $odds")
}
//result
//evens : [2, 4, 6, 8, 10]
//odds : [1, 3, 5, 7, 9]
```

# Aggregation (집계)

## 리스트의 사이즈를 가져와라

일반적으로 List의 요소가 몇개 있는지 알고 싶으면 간단하게

```Kotlin
fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    println("list length : ${list.size}")
    println("list length : ${list.count()}")
}
```
다음과 같이 알 수 있다. size가 있는데 굳이 count()함수를 제공한 이유는 무엇일까?

눈치가 있는 분이라면 이런 생각을 할 것이다.

'어? 그럼 리스트에서 특정 요소를 필터링하고 남은 요소의 사이즈가 몇 개인지 알고 싶을 때 사용할 수 있을거 같네요?'

맞다.

위와 같은 조건이라면 filter를 통해 특정 요소를 반환해서 사이즈를 체크할 수 있는데 이걸 그냥 count에서 처리가 가능하다.

```Kotlin
fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    val filteredList = list.filter { it < 5 }
    println("filter list length : ${filteredList.size}")
    // 위에 코드는 다음과 같이
    val filteredSizeByCount = list.count { it < 5 }
    println("filteredSizeByCount length : $filteredSizeByCount")
}
```
코드만으로도 어떤 행위를 하는지 명확하게 알 수 있으면서도 간결함이 느껴지는 코드이다.

## 컬렉션의 최대/최소값 구하기

코틀린에서는 이와 관련 몇가지 함수를 제공한다.

단순하게 컬렉션의 최대/최소값을 구하는 방법은 다음과 같다.

```Kotlin
fun filterCondition(it: Int) : Boolean {
    return it in 4..6
}

fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    val minValue = list.minOrNull()
    val maxValue = list.maxOrNull()
    println("$minValue : $maxValue")
}
```
물론 함수명을 보면 알겠지만 내부적으로 빈 리스트인 경우에는 null을 반환하고 아니면 min/max를 구해서 반환한다.

역시 엘비스 연산자를 이용해 후처리를 하기에도 용이하다.

하지만 지금같이 일종의 원형 타입의 요소를 담은 리스트가 아닌 특정 객체를 담은 컬렉션인 경우에는 다른 방식을 제공한다.

이것은 여러분이 이전 스텝에서 정렬을 할 때 이미 한번 경험해 본 것이고 그 방식에서 크게 벗어나지 않는다.

## (min/max)Of((min/max)OfOrNull)과 (min/max)ByOrNull의 차이가 뭐야?

이 둘의 차이는 객체가 담긴 리스트의 min/max를 구할 때 반환되는 대상 자체가 달라진다.

실제로 해당 함수를 보면 반환하는 객체가 다르다는 것을 알 수 있는데 다음 코드를 통해서 한번 알아보자.

참고로 Map과 Set 역시 사용할 수 있으며 Set은 리스트와 비슷하기 때문에 Map에 대한 테스트 코드도 남겨본다.

```Kotlin
data class User(
    val name: String,
    val age: Int,
)

fun main() {
    // user 객체를 담고 있는 리스트
    val users = listOf(
        User("Smith", 20),
        User("Fodera", 22),
        User("Foster", 23),
        User("Riley", 22),
        User("Scott", 25),
        User("Aron", 25),
        User("Mason", 21),
        User("Tasker", 21),
    )

    val minNameOfOrNull = users.minOfOrNull { it.name }
    val minNameByOrNull = users.minByOrNull { it.name }
    println("test min name of and by diff [ $minNameOfOrNull : $minNameByOrNull ]")
    val minAgeOfOrNull = users.minOfOrNull { it.age }
    val minAgeByOrNull = users.minByOrNull { it.age }
    println("test min age of and by diff [ $minAgeOfOrNull : $minAgeByOrNull]")

    val maps = mapOf(3 to "Smith", 1 to "Foster", 2 to "Foster")
    val minNameOfOrNullMap = maps.minOfOrNull { (_, name) -> name }
    val minNameByOrNullMap = maps.minByOrNull { (_, name) -> name }
    println("test min name of and by diff [ $minNameOfOrNullMap : $minNameByOrNullMap ]")
    val minAgeOfOrNullMap = maps.minOfOrNull { (key, _) -> key }
    val minAgeByOrNullMap = maps.minByOrNull { (key, _) -> key }
    println("test min age of and by diff [ $minAgeOfOrNullMap : $minAgeByOrNullMap]")
}
//result

//test min name of and by diff [ Fodera : User(name=Fodera, age=22) ]

//test min age of and by diff [ 20 : User(name=Smith, age=20)]

//map test min name of and by diff [ Foster : 1=Foster ]

//map test min age of and by diff [ 1 : 1=Foster]
```

Comparable을 통해서 후행 람다로 들어온 객체의 요소를 비교해서 min/max를 구하는 부분은 동일하다.

동작하는 방식을 보면 실제로는 그렇지 않지만 마치 asc/desc로 정렬하고 첫 번째 요소를 가져온 것이랑 결과가 같다는 것을 알 수 있다.

하지만 결과는 by냐 of냐에 따라서 달라진다.

해당 함수의 코드가 반환하는 타입을 봐도 알 수 있겠지만 다음과 같이 정리할 수 있다.

    - By는 리시버에서 선언된 요소로 min/max를 구하고 대상 객체, 즉 User를 반환한다.
    - Of는 리시버에서 선언된 요소로 min/max를 구하고 해당 요소를 반환한다. 즉 name이라면 name을 age면 age를 반환한다.

이런 차이점을 알아두자.

사실 객체의 프로퍼티의 min/max값을 구해서 후 처리를 하는 경우가 그다지 많지 않을 것이다.

오히려 객체 자체를 반환받아서 후 처리를 하는 경우가 대다수일텐데 그럼에도 Of를 통해서 특정 객체의 프로퍼티값으로만 처리하는 경우가 발생하면 유용하게 사용할 수 있다.

어째든 이런 방식을 가만히 보니 눈치가 빠른 분들이라면 이런 생각도 해볼 것이다.

'이렇다는 것은 지금 User의 두 프로퍼티를 마치 정렬처럼 메소드 체이닝을 통해서 처리할 수 있다고 생각하는데요?'

그렇다.

sortedBy와 sortedWith의 차이를 이전 정렬을 공부하면서 느꼈을 것인데 이녀석도 바로 이와 비슷한 함수를 제공한다.

```Kotlin

data class User(
    val name: String,
    val age: Int,
)

fun main() {
    // user 객체를 담고 있는 리스트
    val users = listOf(
        User("Smith", 20),
        User("Fodera", 22),
        User("Foster", 23),
        User("Riley", 22),
        User("Scott", 25),
        User("Aron", 25),
        User("Mason", 21),
        User("Tasker", 21),
    )
    val minWithBy = users.minWithOrNull(compareByDescending<User> { it.age }.thenBy { it.name })
    val minWithByDesc = users.minWithOrNull(compareByDescending<User> { it.age }.thenByDescending { it.name })
    println("$minWithBy : $minWithByDesc")
}
//result
// User(name=Aron, age=25) : User(name=Scott, age=25)
```
코드를 보면 알겠지만 일단 나이로 내림차순을 하고 그 이후 이름을 오름/내림 차순해서 해당 객체를 가져오는 것이다.

Of의 경우에는 저 방식을 사용할 수 없다.

왜냐하면 함수가 Comparator와 셀렉터를 받는데 셀렉터의 결과가 Comparator<in R>를 통해 비교하기 때문에 연쇄적으로 처리가 불가능하다.

그래서 꼬아서 원하는 결과를 받아야 하는데 차라리 With를 사용하는 방식이 좋아보인다.

Of는 오히려 객체가 아닌 원형 타입형식의 리스트에 사용하기에는 괜찮은 방식이다.

이렇게 보면 sort에서 해왔던 방식과 유사함으로 인해 코드를 이해하는데 수월하다.

그렇다면 Of의 With는 어떻게 사용하는지 간단한 예제로 확인해 보자

```Kotlin
fun main() {
    val listMinOfWith = listOf(4,1,8,3,2,5,4)

    // 리스트에서 후행 람다로 들어오는 셀렉터에서 10을 곱한 값을 compareBy로 비교해서 min/max로 집계한다.
    val minOfWithValue = listMinOfWith.maxOfWithOrNull(compareBy { it }) { it * 10 }
    println("minOfWithValue : $minOfWithValue")
}
```
만일 여러분이 해당 리스트에서 min/max를 구하고 10을 곱한 값을 구한다고 생각해 보자.

그렇다면 애초에 각 요소에 10을 곱하고 min/max를 구하면 끝날 것이다.

이런 경우라면 위의 max(min)OfWithOrNull같은 잡계 함수를 사용하면 한번에 완료할 수 있다.

# joinToString

javascript에서 다음과 같은 코드를 한번 살펴보자.

```javascript
var list = []
list.push(1,2,3,4,5,6)
(50[1, 2, 3, 4, 5]
list.toString()
"1,2,3,4,5"
```
스크립트에서 배열을 toString()하게 되면 기본적으로 구분자 ','로 문자열로 변환된다.

자바에서는 Stream API를 사용하면 다음과 같이 사용할 for문을 사용하지 않아도 쉽게 만들 수 있다.

물론 리스트가 문자열이라면 'String.join'을 통해서 만들 수 있다.

```java
public class Main {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 3, 2, 9, 6);
        String strFormat = list.stream()
                               .map(Object::toString)
                               .collect(joining(","));
        System.out.println(strFormat);

        String strUseReduce = list.stream()
                                  .map(Object::toString)
                                  .reduce((p, n) -> p + "," + n)
                                  .orElse("");
        System.out.println(strFormat);

    }
}
```
물론 앞으로 배울 reduce를 활용해도 가능하긴 하다.

이렇게 기본적인 방식외에도 앞뒤로 괄호나 여러 기호를 붙일 수도 있다.

일반적으로 배열을 표현하는 방식으로 스트링을 보여주겠다면

```java
public class Main {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 3, 2, 9, 6);
        String strFormat = list.stream()
                               .map(Object::toString)
                               .collect(joining(", ", "[", "]"));
        System.out.println(strFormat);

    }
}
```
따닥따닥 붙어 있는게 좀 거시기 해보여서 ','뒤에 공백을 넣고 prefix와 susfix를 정의해서 만들 수도 있다.

코틀린은 좀 더 심플하다.

두 가지 방법을 제공하는데 리스트의 요소가 Int 타입이라면 joinToString()을 제공한다.

다만 주의할 점은 separator를 지정하지 않으면 스크립트와는 다르게 기본 구분은 공백이 뒤에 있는 ", "이라는 것을 명심해야 한다.

```Kotlin
fun main() {

    val listOf = listOf(1, 2, 3, 4, 5)
    println(listOf.joinToString())
    println(listOf.joinToString(",", "[", "]"))
    // named argument을 이용해 코틀린의 기본 구분자를 사용하고 prefix, susfix를 붙일 수 있다.
    println(listOf.joinToString(prefix ="["))
    println(listOf.joinToString(postfix ="]"))
    println(listOf.joinToString(prefix ="[", postfix ="]"))

    // 화면상에서 길이가 길면 '이것은 긴 배열입니다. 따라서....'같이 뒤에 ...를 통해서 특정 길이를 체크해 자를수 있다.
    // 이때 limit는 join할 리스트의 요소 갯수이다. 따라서 밑에 코드는 1, 2, 3, 까지 스트링을 조이닝하고
    // 기본 truncated는 ...이기 때문에 결과는 1, 2, 3, ...
    println(listOf.joinToString(limit = 3))
    // truncated 지정
    println(listOf.joinToString(limit = 3, truncated = "....."))
    // 구분자는 공백없는 ','로
    println(listOf.joinToString(separator = ",", limit = 3, truncated = "....."))
    // transform은 람다 표현식을 받는다. 후행 담다를 통해서 해당 요소에 문자를 붙여보자
    println(listOf.joinToString(separator = ",", limit = 3, truncated = ".....") { "$it (숫자) "})
}
//result
//1, 2, 3, 4, 5
//[1,2,3,4,5]
//[1, 2, 3, 4, 5
//1, 2, 3, 4, 5]
//[1, 2, 3, 4, 5]
//1, 2, 3, ...
//1, 2, 3, .....
//1,2,3,.....
//1 (숫자) ,2 (숫자) ,3 (숫자) ,.....
```

joinTo는 joinToString과 비슷하지만 반환되는 타입이 문자열이 아니다.

함수를 타고 들어가면 Appendable을 반환하고 있는데 대표적으로 StringBuilder가 있다.

물론 StringBuffer도 가능하다.

하지만 비동기에서 사용할 녀석은 아니니 일반적으로 StringBuilder를 사용하게 된다.

```Kotlin
fun main() {
    val builder = StringBuilder("use joinTo fun ")
    val result = (1..10).joinTo(builder, separator = ",", prefix = "[", postfix = "]")
    println("result : $result")
}
//result
// "use joinTo fun [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
```
여기서 result는 문자열이 아니고 Appendable를 반환하기 때문에 실제로는 StringBuilder이다.

# reduceIndexed

reduce는 자바와 같은 방식이다.

하지만 컬렉션의 인덱스를 필요로 하는 경우가 생길 수 있다.

자바라면 스레드 안정성과 원자성을 지원하는 AtomicInteger나 AtomicLong을 사용하게 된다.

다음 자바 예제이다.

```java
public class Main {

    public static void main(String[] args) {
        List<Integer> list = List.of(1,2,3,4);
        AtomicInteger ai = new AtomicInteger();
        int reduce = list.stream()
                         .reduce((p, n) ->  {
                                    int i = ai.incrementAndGet();
                                    if( (i % 2) == 0) {
                                        return p + n * ai.get();
                                    }
                                    return p;

                         }).orElse(0);
        System.out.println(reduce);
    }
}
```
인덱스가 짝수인 경우에는 해당 요소와 인덱스를 곱한 값을 누적으로 처리하는 방법이다.

코틀린은 이것을 위해서 reduceIndexed를 따로 제공한다.

```Kotlin
fun main() {
    val reduceResult = (1..4).reduceIndexed { index, acc, i ->
                                                if(index % 2 == 0)
                                                    acc + (i * index)
                                                else
                                                    acc
                                             }
    println("reduceResult : $reduceResult")
}
```
코틀린의 경우 index가 1부터 시작한다. 이유가 무엇일까?

실제로 코드상에서 acc가 누적된 값이고 i가 컬렉션에서 보면 현재의 값이다.

하지만 가만히 생각해 보면 누적된 값이란게 최초에는 존재하지 않는다. 따라서 첫 번째는 순환을 할 때는 리스트의 0과 1번째 인덱스 정보가 acc와 i로 넘어오게 된다.

그래서 실제로는 인덱스를 찍어보면 1,2,3만 찍히게 되어 있다.

맨 위의 자바 코드는 그것을 고려한 코드이다. 실제로 자바코드와 코틀린 코드는 결과값이 똑같다.

그래서 자바의 경우에는 다음과 같이 초기값을 설정할 수 있다.

```java
public class Main {

    public static void main(String[] args) {
        List<Integer> list = List.of(1,2,3,4);
        AtomicInteger ai = new AtomicInteger();
        int reduce = list.stream()
                         .reduce(0, (p, n) ->  {
                                    int i = ai.getAndIncrement();
                                    if( (i % 2) == 0) {
                                        return p + n * ai.get();
                                    }
                                    return p;

                         });
        System.out.println(reduce);
    }
}
```
reduce에 초기값을 설정하게 되면 처음 순회를 할때 p는 0으로 n은 현재값인 1이 나올것이다.

이 때는 Stream인 아닌 해당 리스트의 제너릭 타입으로 반환한다.

하지만 코틀린은 reduce가 아닌 fold라는 것으로 이것을 대처한다.

이유는 reduce는 reduce대로 초기값을 지정하고 싶으면 fold로 사용하도록 명시적으로 나눈 듯 싶다.

다만 fold는 살짝 다른 점이 있다.

자바의 reduce에서 초기값을 설정할 때는 리스트의 제너릭 타입으로 설정해야 하기 때문에 위 코드에서 0대신 스트링을 넣으면 예외가 발생한다.

일단 코틀린의 코드를 한번 살펴보자.

```Kotlin
fun main() {
    val foldResult = (1..4).fold(0) { acc, i -> acc + i }
    println("foldResult : $foldResult")

    val foldResultOtherType = (1..4).fold("0") { acc, i -> acc + i }
    println("foldResultOtherType : $foldResultOtherType")

    val foldResultOtherTypeInt = listOf("1", "2", "3").fold(0) { acc, i -> acc + i.toInt() }
    println("foldResultOtherTypeInt : $foldResultOtherTypeInt")
}
```
즉, 위와 같이 표현이 가능하다.

다만 fold의 인자값으로 반환 타입이 결정된다.     

밑에 2, 3번 째 코드를 보면 어떤 의미인지 알 수 있을 것이다.

그렇다면 위의 자바 코드는 다음과 같이

```Kotlin
fun main() {
    val reduceToFold = (1..4).foldIndexed(0) { index, acc, i -> 
                                                        if(index % 2 == 0) {
                                                            acc + i*index
                                                        } else {
                                                            acc
                                                        }
                                                   }
    println("reduceToFold : $reduceToFold")
}
```
변경이 가능하다.

잘 보면 자바의 경우에도 reduce에서 초기값을 사용하게 되면 컬렉션이 비어있어도 예외가 발생하지 않는다.

초기값을 사용하지 않은 경우에는 예외가 발생하기 때문에 그런 이유로 orElse나 익명 함수를 활용해 다른 방식으로 처리하는 orElseGet이나 orElseThrow를 사용할 수 있다.

코틀린도 마찬가지로 빈 컬렉션의 경우 예외가 발생하지만 fold는 초기값을 인자로 지정하기 때문에 예외가 발생하지 않는다.      

## Right까지 만들어 줌!
reduce/reduceIndexed와 fold/foldIndexed 이 4개의 함수에는 Right가 붙는 함수들이 쌍으로 존재한다.

```
reduceRight/reduceRightIndexed와 foldRight/foldRightIndexed
```

사실 컬렉션의 마지막 원소부터 반대로 reduce나 fold를 사용하고 싶을 수 있다.

사실 컬렉션을 reversed하고 사용하면 될 것 같은데 코틀린은 참 친절하다.

~~이렇게 디테일하게 만들어 줄 것 까지 있었을까 생각이 들정도!~~

```Kotlin
fun main() {
    val foldRight = listOf(1,2,3,4).foldRight(0) { n, acc ->
                                                        println("$n $acc")
                                                        acc + n
                                                 }
    println("foldRight : $foldRight")
}
```
주의할 것은 컬렉션의 원소의 마지막 요소부터 반대 방향으로 가는 것을 따라서 람다의 파라미터 순서도 뒤집혀져 있다.

즉 첫 번째 파라미터는 누적이 아닌 현재 컬렉션의 원소이고  두 번째가 누적 값이다.

# 아니 running은 뭐여????

위에 각 4개의 함수 앞에 running붙어 있는 함수들이 있다.

```
runningReduce/runningReduceIndexed와 runningFold/runningFoldIndexed
```
~~아니 뭐 이런걸 다 제공하는건가요?~~

쓸 일이 있을까 싶긴 하지만 사용한 예가 있어서 소개를 해 볼까 한다.

사실 이것은 reduce/fold가 순회를 할 때 누적되는 값들만 모아서 리스트로 반환하는 함수이다.

1,2,3,4를 담은 리스트에서 fold(0)을 이용한다면 초기값부터 시작해서 0 -> 1 -> 3 -> 6 -> 10이 단계별 누적된 값이 될것이다.

이것을 리스트로 담아서 반환하는데 실제 코드로 보는게 빠르다.

```Kotlin
fun main() {
    val runningFold = listOf(1,2,3,4).runningFold(0) { acc, i -> acc + i }
    println("runningFold : $runningFold")
}
//result
//runningFold : [0, 1, 3, 6, 10]
```

# At a Glance

일반적인 filter 함수의 확장된 형태와 집계에 대해서 알아 보았다.

각 함수들의 특징에 맞춰서 적절하게 사용한다면 간결한 코드를 작성할 수 있으며 가독성 역시 높아지기 때문에 연습이 필요한 부분이다.

다음에는 실무에서 가장 많이 사용될 convert와 하위 리스트 추출관련 함수들을 알아보고자 한다.       
