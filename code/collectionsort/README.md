# 컬렉션 정렬 feat. 구조 분해 선언 (destructuring declaration)

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/collectionsort/kotlin/CollectionSort.kt)

# 리스트의 정렬

베열, 리스트의 경우 sort라는 함수와 sorted라는 함수를 여러분들은 볼 수 있다.

사실 자바에서도 마찬가지인데 이 sort와 sorted는 분명 차이가 있다.

왜냐하면 sort는 원본의 순서를 정렬하기 때문에 immutable한 경우에는 오류가 발생한다.

```java
public class Main {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1,3,2,9,6));
        Collections.sort(list);
        System.out.println("list is " + list);

        List<Integer> immutable = List.of(1,3,2,9,6);
        Collections.sort(immutable);

    }

}
```
위 자바코드를 보면 실제 에러는 나지 않지만 사실 인텔리제이에서는 'Collections.sort(immutable);' 이 부분에 경고를 뜨워준다.

왜냐하면 불변 리스트이기 때문에 ArrayList로 감싸라는 것이다.

실제로 실행하게 되면 바로 저 부분에서 에러가 난다.

```
Exception in thread "main" java.lang.UnsupportedOperationException
	at java.base/java.util.ImmutableCollections.uoe(ImmutableCollections.java:72)
	at java.base/java.util.ImmutableCollections$AbstractImmutableList.sort(ImmutableCollections.java:111)
	at java.base/java.util.Collections.sort(Collections.java:145)
	at Main.main(Main.java:12)
```

저렇게 Collections의 sort를 사용하게 되면 원본의 데이터를 변경시켜서 반환하게 되어 있다.

코틀린에서는 자바와는 다르게 편의성을 위해서 컬렉션 자체의 API로 지원하는데 애초에 불변이라면 sort라는 함수 자체가 IDE에서 뜨지도 않는다.

즉 애초에 에러를 차단하고 있다.

바로 코드로 살펴보자.

```Kotlin
fun main() {
    val immutableList = listOf(1,4,2,7,5,3)
    // 아예 함수가 없다.
    // 원본 데이터를 통해서 정렬이후 새로운 mutableList로 반환하는 sorted만 사용가능
    //immutableList.sort()
    val newSortedList = immutableList.sorted()
    println("immutableList : $immutableList")
    println("newSortedList : $newSortedList")

    val mutableList = mutableListOf(1,4,2,7,5,3)
    println("============before sort============")
    println("mutableList : $mutableList")

    mutableList.sort()
    println("============after sort============")
    println("mutableList : $mutableList")

    val otherMutableList = mutableListOf(1,4,2,7,5,3)
    println("============before sorted============")
    println("otherMutableList : $otherMutableList")

    val sortedList = otherMutableList.sorted()
    println("============after sorted============")
    println("otherMutableList after sorted : $otherMutableList")
    println("sortedList : $sortedList")
}
```
실제로 sort는 컬렉션의 sort를 사용하고 있는데 자바와 같다.

그래서 원본의 데이터를 정렬하기 때문에 sort이후에는 원본 배열이나 리스트의 정렬이 변경된 것을 볼 수 있다.

하지만 sorted의 경우에는 API를 보면 대상이 되는 원본을 정렬한 이후에 새로 배열이나 리스트로 반환한다.

그래서 콘솔에 찍힌 것을 확인해 보면 원본 정보는 정렬되지 않는 것을 볼 수 있다.

대부분의 언어들은 sort의 경우에는 오름차순 즉 Ascending이다.      

자바와 코틀린도 마찬가지이고 이에 반하는 Descending, 즉 내림차순 역시 제공한다.

```Kotlin
fun main() {
    val mutableList = mutableListOf(1,4,2,7,5,3)
    //mutableList.sortDescending()
    //val desSortList = mutableList.sortedDescending()
}
```
역시 불변이냐 가변이냐에 따라 IDE에서도 이에 맞추서 API를 사용할 수 있도록 되어 있다.

reverse라는 API도 있다.

이녀석도 reverse와 reversed로 원본의 정보를 변경하느냐 원본 데이터를 바탕으로 새로운 컬렉션을 반환느냐로 나눠진다.

가끔 주니어 개발자 분이 이것이 내림 차순인줄 알고 사용했다가 버그를 발생시킨 적이 있다.

리버스는 말 그대로 역으로 뒤집는 것이다.

예를 들면 위와 같이 '(1,4,2,7,5,3)'라면 그 역인 '(3,5,7,2,4,1)'로 뒤집는 것이다.

그래서 java의 Stream API를 사용하다보면 어떤 요소를 오름차순으로 정렬하고 reversed를 통해서 내림차순으로 정렬하는 코드들을 볼 수 있다.

```java
public class Main {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 3, 2, 9, 6));
        List<Integer> desc = list.stream()
                                 .sorted(reverseOrder())
                                 .collect(toList());

        // 다시 뒤집어서 
        List<Integer> again = list.stream()
                                  .sorted(reverseOrder().reversed())
                                  .collect(toList());

        System.out.println("원본 list : " + list);
        System.out.println("내림차순된  list : " + desc);
        System.out.println("again  list : " + again);
    }
}

//result
//원본 list : [1, 3, 2, 9, 6]
//내림차순된  list : [9, 6, 3, 2, 1]
//again  list : [1, 2, 3, 6, 9]
```
자바의 경우에는 지금같이 Integer가 아닌 dto나 엔티티같은 객체인 경우에는 메소드 체이닝을 통해서 멀티 정렬을 할 수 있다.

마치 sql의 정렬 방식처럼 id로 먼저 정렬하고 그 이후에 이름이나 나이로 정렬하는 방식들을 메소드 체이닝으로 해결할 수 있다.

물론 코틀린 역시 가능한데 대부분은 내림/오름 정렬 관련 API을 제공하기 때문에 편의성을 더하고 있다.                   

다만 원본 데이터를 직접 조작하느냐 아니면 원본 데이터를 바탕으로 새로운 객체를 반환하느냐에 따른 선택지가 존재하는 것이다.

# Sort Map, Sort Set in Kotlin

Map과 Set의 경우에는 좀 차이가 있다.

Map은 Iterable 인터페이스를 구현한 컬렉션이 아니기 때문에 일반적인 sort나 sorted를 따로 제공하지 않는다.

이와 관련 스택오버플로우에 다음과 같은 글이 있다.

[Reason HashMap does not implement Iterable interface?](https://stackoverflow.com/questions/19422365/reason-hashmap-does-not-implement-iterable-interface)

# TreeMap, TreeSet

일반적으로 요소를 추가할 때는 그 추가된 순서대로 버킷에 저장된다.

물론 Set의 경우에는 그렇지 않을 수 있지만 TreeMap과 TreeSet은 기본적으로 오름차순으로 정렬한다.

TreeMap은 key값을 대상으로 오름차순이다.

자바의 경우에는 만일 맵이라며 기존의 맵을 Stream API를 통해서 정렬할 수도 있고 TreeMap으로 변환해서 정렬을 할 수 있다.

코틀린에서는 어떻게 할 수 있을까?

```Kotlin
fun main() {

    val treeMap = sortedMapOf(3 to "Basquiat", 1 to "Warhol")
    treeMap += (2 to "Harring")
    println("treeMap : $treeMap")

    val treeSet = sortedSetOf("Basquiat", "Warhol")
    treeSet += "Harring"
    println("treeSet : $treeSet")

}
//result
//treeMap : {1=Warhol, 2=Harring, 3=Basquiat}
//treeSet : [Basquiat, Harring, Warhol]
```
내부적으로 TreeMap과 TreeSet으로 구현되어져 있기 때문에 정렬을 한다.

또한 기존의 Map과 Set을 정렬할 수 있는 방법 역시 제공한다.

```Kotlin
fun main() {

    val mapOf = mapOf(3 to "Basquiat", 1 to "Warhol")
    val sortedMapOf = mapOf.toSortedMap()
    println("mapOf : $mapOf")
    println("sortedMapOf : $sortedMapOf")

    val setOf = setOf("Warhol", "Basquiat")
    val sortedSetOf = setOf.toSortedSet()
    println("setOf : $setOf")
    println("sortedSetOf : $sortedSetOf")

}
//result
//mapOf : {3=Basquiat, 1=Warhol}
//sortedMapOf : {1=Warhol, 3=Basquiat}
//setOf : [Warhol, Basquiat]
//sortedSetOf : [Basquiat, Warhol]
```

sorted라는 단어에서 알 수 있듯이 원본으로부터 TreeMap과 TreeSet로 변환해서 반환한다는 것을 알 수 있다.

코드로 보면 실제로는 SortedMap과 SortedSet이지만 TreeMap과 TreeSet은 이것을 구현한 구현체이다.

```
// 더 정확히는 이것을 구현한 NavigableMap, NavigableSet을 구현한 클래스
SortedMap -> NavigableMap -> TreeMap

SrotedSet -> NavigableSet -> TreeSet

```
불변이든 가변이든 이것을 통해서 mutable한 Set과 Map을 반환하다.

Set의 경우에는 toSortedSet()에 Comparator를 구현해서도 가능하다.

```Kotlin
fun main() {

    val setOfBy = setOf("Warhol", "Basquiat", "Harring", "Yoko")

    // compareBy -> 요소를 통해서 기본 오름차순으로 
    val ascSetOfBy = setOfBy.toSortedSet(compareBy{ it })

    // 오름차순를 reversed로 내림차순으로 변경도 가능하고 compareByDescending도 있다.
    //val descSetOfBy = setOfBy.toSortedSet(compareBy{ it }).reversed()
    val descSetOfBy = setOfBy.toSortedSet(compareByDescending{ it })
    println("setOfBy : $setOfBy")
    println("ascSetOfBy : $ascSetOfBy")
    println("descSetOfBy : $descSetOfBy")

    // 요소의 길이로 정렬을 해보자. 오름/내림
    val ascSetOfByLength = setOfBy.toSortedSet(compareBy{ it.length })
    //val descSetOfByLength = setOfBy.toSortedSet(compareBy{ it.length }).reversed()
    val descSetOfByLength = setOfBy.toSortedSet(compareByDescending{ it.length })
    println("ascSetOfByLength : $ascSetOfByLength")
    println("descSetOfByLength : $descSetOfByLength")

}
```
우리는 각 컬렉션들의 특징을 잘 알고 있다.

특히 Linked의 경우에는 그 특징이 두드러진다.

그래서 가만히 생각해 보면 Set은 toList를 통해 List로 변경해 정렬을 하고 다시 LinkedHashSet으로 변경하는 방법도 있을 것이다.

그렇다면 Map은 어떨까?        

Map도 크게 다르지 않다.

```Kotlin
fun main() {

    val mapOfBy = mapOf(4 to "Yoko", 3 to "Basquiat", 1 to "Warhol", 2 to "Harring")
    // 오름 차순이랑 똑같다.
    val ascSortedMapOfBy = mapOfBy.toSortedMap(compareBy{ it })
    val descSortedMapOfBy = mapOfBy.toSortedMap(compareByDescending{ it })

    println("mapOfBy : $mapOfBy")
    println("ascSortedMapOfBy : $ascSortedMapOfBy")
    println("descSortedMapOfBy : $descSortedMapOfBy")

}
```
물론 여러분들은 값을 소팅하고 싶은 경우가 생길 것이다.

이 때는 딱히 방법이 없다. entries를 이용해야 한다.

그 전에 우리는 Map을 통해서 배웠던 Pair에 대해서 기억이 날 것이다.

이 때 언급하지 않는 것이 있는데 바로 그것은 '구조 분해 선언'이다.

## 구조 분해 선언(destructuring declaration)

이와 관련해서 언급하지 않았지만 Map을 루프를 돌 때 (k, v) 이 표현 방식을 기억할 텐데 이것이 사실 구조 분해 선언이다.

구조 분해 선언은 원래대로라면 data class를 소개할 때 같이 언급해야하는 부분으로 늦었지만 여기서 소개해 보고자 한다.

data class를 소개할 때 주 생성자에 선언된 프로퍼티는 componentN으로 생성자에 선언된 순서대로 생성된다는 것을 언급한 적이 있다.

이것이 중요한 이유는 튜플, 즉 (a,b,c,d)처럼 어떤 프로퍼티의 쌍을 표현할 때 해당 componentN의 순서대로 튜플을 만들 수 있기 때문이다.

다음 코드를 보자

```Kotlin
data class Musician(
    val name: String,
    val genre: String,
)

fun main() {

    val musician = Musician("John Coltrane", "jazz")
    println("뮤지션 이름 : ${musician.component1()}")
    println("뮤지션 장르 : ${musician.component2()}")

    // 뮤지션의 이름과 장르를 튜플로 표현하면 어떨까?
    val (name, genre) = musician
    println("뮤지션 이름 : $name")
    println("뮤지션 장르 : $genre")

}
```

실제로 구조 분해 선언은 바로 저 순서에 의해서 정해진다.

즉 생성자에 선언된 순서대로 정의가 되는데 실제로 디컴파일된 코드를 보면

```java
public final class Main {
   public static final void main() {
      Musician musician = new Musician("John Coltrane", "jazz");
      String name = "뮤지션 이름 : " + musician.component1();
      System.out.println(name);
      name = "뮤지션 장르 : " + musician.component2();
      System.out.println(name);
      name = musician.component1();
      String genre = musician.component2();
      String var3 = "뮤지션 이름 : " + name;
      System.out.println(var3);
      var3 = "뮤지션 장르 : " + genre;
      System.out.println(var3);
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }
}
```
componetN을 호출하는 것을 알 수 있다.

Map의 경우에는 생성시에 Pair의 fix (중위)를 통해서 (1 to "test")처럼 추가할 수 있는데 이 Pair와 Triple은 data class이다.

특이한 것은 List도 이 구조 분해 선언을 사용할 수 있다.            

```Kotlin
fun main() {
    val list = listOf(1,2,3,4)
    println(list.component1())
    println(list.component2())
    println(list.component3())
    println(list.component4())
}
```

구조 분해 선언이나 람다에서 리시버가 이런 튜플 형식일 경우 안쓰는 경우도 생길 수 있다.

그럴 때는 다음과 같이 코틀린의 예약어중 하나인 '_'를 이용한다.

```Kotlin
fun main() {
    val pair = Pair(1,2)
    val (_, second) = pair
    println("second is $second")
}
```

자 이렇다는 것은 Map의 경우에도

```Kotlin
fun main() {
    val map = mapOf(1 to "a", 2 to "b")
    for((_, value) in map) {
        println("value : $value")
    }
}
```
처럼 key는 사용하지 않고 value로 무언가를 하는 경우라면 위와 같이 사용하면 된다.

자 그럼 이런 특성을 이용해서 맵의 값으로 정렬을 한번 해보자.

```Kotlin
fun main() {
    val mapOfBy = mapOf(4 to "Yoko", 3 to "Basquiat", 1 to "Warhol", 2 to "Harring", 5 to "Kiefer")
    //public fun <K, V> Map<out K, V>.toList(): List<Pair<K, V>>
    // 오호라 List<Pair<K, V>>로 들어온다.
    // 리시버는 구조 분해 선언으로 표현되고 value에 대해서 오름 차순
    // value로 정렬하기 때문에 key에 대해서는 사용하지 않는다고 '_'로 표시한다.
    // list의 내부 요소인 Pair는 이 value를 기준으로 정렬될 것이다.
    val ascListOf = mapOfBy.toList()
                           .sortedBy { (_, value) -> value }

    val ascSortMapByValue = ascListOf.toMap()
    println("ascSortMapByValue : $ascSortMapByValue")

    val descListOf = mapOfBy.toList()
                            .sortedByDescending { (_, value) -> value }

    val descSortMapByValue = descListOf.toMap()
    println("descSortMapByValue : $descSortMapByValue")

    // value -> 예술가의 이름의 길이로 한번 해보자.
    // 길이로 오름 차순
    val ascListOfByLength = mapOfBy.toList()
                                   .sortedBy { (_, value) -> value.length }

    val ascSortMapByValueLength = ascListOfByLength.toMap()
    println("ascSortMapByValueLength : $ascSortMapByValueLength")

    // 길이로 내림 차순
    val descListOfByLength = mapOfBy.toList()
                                    .sortedByDescending { (_, value) -> value.length }

    val descSortMapByValueLength = descListOfByLength.toMap()
    println("descSortMapByValueLength : $descSortMapByValueLength")

    // 예술가 이름의 길이로 오름 차순하고 그 다음 키로 내림 차순을 한다.
    val multiListOf = mapOfBy.toList()
                             .sortedWith(
                                 compareBy<Pair<Int, String>>{ (_, value) -> value.length }
                                 .thenByDescending { (key, _) -> key }
                             )
    val multiMapOf = multiListOf.toMap()
    println("multiMapOf : $multiMapOf")


    // 예술가 이름의 길이로 오름 차순하고 그 다음 키도 오름 차순을 한다.
    val multiListOfOther = mapOfBy.toList()
                                  .sortedWith(
                                      compareBy<Pair<Int, String>>{ (_, value) -> value.length }
                                      .thenBy { (key, _) -> key }
                                  )
    val multiMapOfOther = multiListOfOther.toMap()
    println("multiMapOfOther : $multiMapOfOther")

}
//result

//ascSortMapByValue : {3=Basquiat, 2=Harring, 5=Kiefer, 1=Warhol, 4=Yoko}
//descSortMapByValue : {4=Yoko, 1=Warhol, 5=Kiefer, 2=Harring, 3=Basquiat}

//ascSortMapByValueLength : {4=Yoko, 1=Warhol, 5=Kiefer, 2=Harring, 3=Basquiat}
//descSortMapByValueLength : {3=Basquiat, 2=Harring, 1=Warhol, 5=Kiefer, 4=Yoko}

// sql like as value.length asc, key desc
//multiMapOf : {4=Yoko, 5=Kiefer, 1=Warhol, 2=Harring, 3=Basquiat}

// sql like as value.length asc, key asc
//multiMapOfOther : {4=Yoko, 1=Warhol, 5=Kiefer, 2=Harring, 3=Basquiat}
```
밑에 두개의 코드는 key와 value를 복합적으로 정렬을 한 것이다.

길이가 같은 warhol과 kiefer의 경우에는 이름의 길이로 정렬하고 key로 내림/오름으로 정렬하기 때문에 결과를 보면 차이를 알 수 있다.

리스트의 요소가 객체라면 위와 같은 방식을 그대로 적용할 수 있다.      

위에 예제로 만들었던 Musician을 재할용해서 한번 만들어 보자

```Kotlin
data class Musician(
    val name: String,
    val genre: String,
)

fun main() {
    val musicians = mutableListOf(
        Musician("a", "Jazz"),
        Musician("c", "Jazz"),
        Musician("w", "Pop"),
        Musician("d", "Hiphop"),
        Musician("f", "Rock"),
        Musician("z", "Pop"),
        Musician("t", "Pop"),
    )
    println("musicians : $musicians")
    val ascSortedByName = musicians.sortedBy { it.name }
    val descSortedByName = musicians.sortedByDescending { it.name }
    println("ascSortedByName : $ascSortedByName")
    println("descSortedByName : $descSortedByName")

    val ascSortedByGenre = musicians.sortedBy { it.genre }
    val descSortedByGenre = musicians.sortedByDescending { it.genre }
    println("ascSortedByGenre : $ascSortedByGenre")
    println("descSortedByGenre : $descSortedByGenre")

    // 장르를 기준으로 먼저 오름 차순을 하고 그 이후 뮤지션의 이름으로 오름 차순을 하자
    val sortedByMultiAscAsc = musicians.sortedWith(
                                            compareBy<Musician> { it.genre }
                                            .thenBy {it.name }
                                       )
    println("sortedByMultiAscAsc : $sortedByMultiAscAsc")
    // 장르를 기준으로 먼저 오름 차순을 하고 그 이후 뮤지션의 이름으로 내림 차순을 하자
    val sortedByMultiAscDesc = musicians.sortedWith(
                                            compareBy<Musician> { it.genre }
                                            .thenByDescending {it.name }
                                        )
    println("sortedByMultiAscDesc : $sortedByMultiAscDesc")

}
```
우리는 구조 분해 선언에 대해서 알고 있다.       

Musician은 data class이기 때문에 이것을 실제로 후행 람다에서 사용할 수 있다.    

```Kotlin
val sortedByMultiAscAsc = musicians.sortedWith(
                                        compareBy<Musician> { (_, genre) -> genre }
                                        .thenBy { (name, _) -> name  }
                                   )
```
위와 같이 표현이 가능하다. 하지만 data class에서 data를 지우면 바로 IDE에서 튜플에 뻘겋게 에러가 날 것이다.      

왜냐하면 우리는 다 배웠다. data class가 아니기 때문에 componentN이 존재하지 않기 때문이다.      

하지만 이렇게 쓰고 싶진 않을 것이다.       

~~귀찮으니깐!~~

다만 배운 것이 적용되는지 확인해 보는 용도에서 체크해보자.

# At a Glance

이것으로 컬렉션의 정렬에 대해서 한번 알아보았다.       

그리고 좀 늦은 감이 있지만 구조 분해 선언 (destructuring declaration)에 대해서도 알아보았다.

지금까지 경험한 지식을 토대로 본격적으로 컬렉션 함수를 다뤄볼까 한다.

자바의 Stream API와 비슷하면서도 다른 특징들이 있지만 Stream API를 능숙하게 사용할 수 있다면 큰 무리는 없을 것이라 본다.

또한 코틀린의 컬렉션 함수는 자바스크립트에서 많이 사용되는 lodash와도 상당히 유사하기 때문에 이 라이브러리를 자주 활용했던 분들 역시 짬과 바이브로 무리가 없을 거라 본다.     

이에 더해서 사실 코틀린에는 시퀀스를 제공하고 있는데 자바의 Stream API와 비슷하다는 느낌을 주는 녀석으로 이 부분은 차후에 소개해 볼까 한다. 