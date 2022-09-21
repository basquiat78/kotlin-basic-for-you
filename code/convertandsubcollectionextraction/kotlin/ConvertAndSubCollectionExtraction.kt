fun main() {
    val strList = listOf("abc", "defd", "agzzz", "ede", "a", "ab")

    val result = strList.map { it.length }
                        .distinct()
    println("result : $result")
    val firstDistinct = strList.distinctBy { it.length }
                               .map { it.length }
    println("firstDistinct : $firstDistinct")

    val list = listOf("1", "2", "3", "4")
    list.forEachIndexed { i, value ->
        println(value.toInt() * (i + 1))
    }

    list.mapIndexed { i, value ->
        value.toInt() * (i + 1)
    }.forEach { value ->
        println(value)
    }

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

    /*
    모양이 거시기 하지만 뭔가 작동은 한다.
    작동은 옳바르게 하고 결과도 원하는 결과를 얻을 수 있지만 map의 의도와는 맞지 않는 코드이다.
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
    */
    /**
     * 모든 장르의 뮤지션들을 담은 리스트에서 Young and old, 장르가 재즈인 뮤지션을 거르고 JazzMusician이라는 객체로 변환해서 리스트에 담는다.
     */
    val youngLions = musicians.filter { it.age <= 25 && it.genre == "Jazz"}
                              .map { JazzMusician(name = it.name, age = it.age, instrumental = it.instrumental, generation = "Young Lions") }

    val oldTigers = musicians.filter { it.age > 25 && it.genre == "Jazz"}
                             .map { JazzMusician(name = it.name, age = it.age, instrumental = it.instrumental, generation = "Old Tigers") }

    println("youngLions : $youngLions")
    println("oldTigers : $oldTigers")

    val mapNotNull = listOf("1", "a", "3", "5").mapNotNull { it.toIntOrNull() }
    println("mapNotNull : $mapNotNull")

    listOf("1", "3", "5").mapTo(intList) { it.toInt() }
    println("intList 1 : $intList")
    listOf("7", "8", "9").mapTo(intList) { it.toInt() }
    println("intList 2 : $intList")

    // Map을 map을 활용해서 변환한다.
    // 단 이경우에는 리스트로 반환하게 된다.
    val exampleMap = mapOf(1 to "one", 2 to "two", 3 to "three")
    val mapToList = exampleMap.map {
        "${it.key} ${it.value.uppercase()}"
    }

    // 따라서 Map의 경우에는 Map내부의 요소를 변환하고자 한다면 그에 맞춰 다음과 같은 함수를 선택할 수 있다.
    val mapKeyMultipleTen = exampleMap.mapKeys { it.key * 10 }
    println("mapKeyMultipleTen : $mapKeyMultipleTen")

    val mapValuesUppercase = exampleMap.mapValues { it.value.uppercase() }
    println("mapValuesUppercase : $mapValuesUppercase")

    // 리스트 타입을 원소로 갖는 리스트의 경우 모든 요소에 대해 루프를 돌아야 하는 경우 각 리스트의 요소를 하나씩 꺼내서
    // 하나의 리스트로 만든다.
    val testList = listOf(listOf(1,2), listOf(6,2), listOf(3,4))
    val flatResultByMap = mutableListOf<Int>()
    // 루프를 돌면서 mapTo를 통해 각각의 원소를 리스트에 추가하는 방식으로 진행한다.
    testList.forEach { elem ->
        elem.mapTo(flatResultByMap) { it }
    }
    println("flatResultByMap : $flatResultByMap")

    val testList = listOf(listOf(1,2), listOf(6,2), listOf(3,4))
    println("testList : $testList")
    val flatMapResult = testList.flatMap { it.toList() }
    println("flatMapResult : $flatMapResult")
    val flattenResult = testList.flatten()
    println("flattenResult : $flattenResult")

    val resultList = (1..3).toList()
                           //.map { fetchNumberList(it) }
                           .flatMap { fetchNumberList(it) }
    println("resultList : $resultList")

    val myList = arrayOf("banana", "apple", "peach", "orange")
    println("associateWith result : " + myList.associateWith { it.length })
    println("associateBy result : " + myList.associateBy { it.length })
    println("associate one result : " + myList.associate { it to it.length })
    println("associate two result : " + myList.associate { it.length to it.uppercase() })

    val groupByResult = listOf("One", "two", "Three", "four", "Five").groupBy { it.first().toUpperCase() }    // Key
    println("groupByResult : $groupByResult")

    // 장르로 그룹바이를 한다.
    val musicianGroupByGenre = musicians.groupBy { it.genre.uppercase() }
    println("musicianGroupByGenre : $musicianGroupByGenre")

    // 밑에 arrayOf로 만든 배열은 subList함수가 존재하지 않는다.
    //val examArray = arrayOf(1,2,3,4,5)
    // arrayListOf 또는 listOf
    //val examArray = arrayListOf(1,2,3,4,5)
    val examArray = listOf(1,2,3,4,5)
    // 정수의 쌍이고 앞의 인덱스는 포함하고 뒤에 인덱스는 포함하지 않는다. 2 until 4와 같은 느낌을 가진다.
    val fromSubList = examArray.subList(2, 4)
    println("fromSubList : $fromSubList")

    // subList와는 다르게 범위로 추출하고 밤위 내 인덱스를 모두 포함한다.
    val examArrayOf = arrayOf(1,2,3,4,5)
    // 밑에 두개는 같은 동작을 한다. 중위 함수이기 때문이다.
    //val fromSlice = examArray.slice(2.rangeTo(4))
    // 단 slice는 list를 반환한다.
    val fromSlice = examArrayOf.slice(2..4)
    println("fromSlice : $fromSlice")

    // 배열을 다른 배열로 반환하고 싶으면 sliceArray
    val arrayFromSlice = examArrayOf.sliceArray(2..4)
    println("arrayFromSlice : $arrayFromSlice")
    println("arrayFromSlice : ${arrayFromSlice.contentToString()}")

    val myTestList = listOf(1,2,3,4,5,6,7,8,9,10)
    val fromTake = myTestList.take(4)
    val fromDrop = myTestList.drop(4)
    println("fromTake : $fromTake")
    println("fromDrop : $fromDrop")
    val likeTake = myTestList.filterIndexed{ i, _ -> i < 4}
    val likeDrop = myTestList.filterIndexed{ i, _ -> i > 3}
    println("likeTake : $likeTake")
    println("likeDrop : $likeDrop")
    val fromTakeLast = myTestList.takeLast(4)
    val fromDropLast = myTestList.dropLast(4)
    println("fromTakeLast : $fromTakeLast")
    println("fromDropLast : $fromDropLast")

    val otherMyTestList = listOf(1,2,5,4,5,6,7,8,9,10)
    val fromTakeWhile = otherMyTestList.takeWhile { it < 5}
    val fromDropWhile = otherMyTestList.dropWhile { it < 5}
    val fromTakeLastWhile = otherMyTestList.takeLastWhile { it > 5}
    val fromDropLastWhile = otherMyTestList.dropLastWhile { it > 5}
    println("fromTakeWhile : $fromTakeWhile")
    println("fromDropWhile : $fromDropWhile")
    println("fromTakeLastWhile : $fromTakeLastWhile")
    println("fromDropLastWhile : $fromDropLastWhile")

    val myChunkedForList = arrayListOf(1,2,3,4,5,6,7,8,9,10)
    val chunked3 = myChunkedForList.chunked(3)
    println("chunked3 : $chunked3")
}

/**
 * 파라미터를 통해서 어떤 리스트를 만들어내는 임시 메소드
 */
fun fetchNumberList(value: Int): List<Int> {
    return Array(3) { (it +1) * value }.toList()
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
