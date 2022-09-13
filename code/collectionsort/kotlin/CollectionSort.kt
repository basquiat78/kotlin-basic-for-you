data class Musician(
    val name: String,
    val genre: String,
)

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

    val treeMap = sortedMapOf(3 to "Basquiat", 1 to "Warhol")
    treeMap += (2 to "Harring")
    println("treeMap : $treeMap")

    val treeSet = sortedSetOf("Basquiat", "Warhol")
    treeSet += "Harring"
    println("treeSet : $treeSet")

    val mapOf = mapOf(3 to "Basquiat", 1 to "Warhol")
    val sortedMapOf = mapOf.toSortedMap()
    println("mapOf : $mapOf")
    println("sortedMapOf : $sortedMapOf")

    val setOf = setOf("Warhol", "Basquiat")
    val sortedSetOf = setOf.toSortedSet()
    println("setOf : $setOf")
    println("sortedSetOf : $sortedSetOf")

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

    val musician = Musician("John Coltrane", "jazz")
    println("뮤지션 이름 : ${musician.component1()}")
    println("뮤지션 장르 : ${musician.component2()}")

    // 뮤지션의 이름과 장르를 튜플로 표현하면 어떨까?
    val (name, genre) = musician
    println("뮤지션 이름 : $name")
    println("뮤지션 장르 : $genre")

    val mapOfBy = mapOf(4 to "Yoko", 3 to "Basquiat", 1 to "Warhol", 2 to "Harring", 5 to "Kiefer")
    //public fun <K, V> Map<out K, V>.toList(): List<Pair<K, V>>
    // 오호라 List<Pari<K, V>>로 들어온다.
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