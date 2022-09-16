data class User(
    val name: String,
    val age: Int,
)

fun filterCondition(it: Int) : Boolean {
    return it in 4..6
}

fun main() {
    val list = listOf(1,2,3,4,5,6,7)
    // 조건이 길다면 가독성을 위해서 익명 함수로 정의하고 filter에 사용할 수 있다.
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

    //val filteredFirst = list.filter { it in 3..6 }
    //val filteredFirst = list.filter(findCondition)
    //                        .first()
    //val filteredLast = list.filter { it in 3..6 }
    //val filteredLast = list.filter(findCondition)
    //                       .last()

    //val filteredFirst = list.first(findCondition)
    //val filteredLast = list.last(findCondition)

    val filteredFirst = list.first { it in 3..6 }
    val filteredLast = list.last { it in 3..6 }
    println("$filteredFirst : $filteredLast")

    //val filteredFirst1 = list.first { it in 9..11 }
    //val filteredLast1 = list.last { it in 9..11 }
    val filteredFirst1 = list.firstOrNull { it in 9..11 } ?: "빈 리스트이기 때문에 첫 번째 요소를 가져올 수 없습니다."
    val filteredLast1 = list.lastOrNull { it in 9..11 } ?: "빈 리스트이기 때문에 마지막 요소를 가져올 수 없습니다."
    println("$filteredFirst1 : $filteredLast1")

    println("list length : ${list.size}")
    println("list length : ${list.count()}")

    val filteredList = list.filter { it < 5 }
    println("filter list length : ${filteredList.size}")
    // 위에 코드는 다음과 같이
    val filteredSizeByCount = list.count { it < 5 }
    println("filteredSizeByCount length : $filteredSizeByCount")

    // filterNot 함수
    val listOfForFilter = listOf(1, 2, 10, 12, 6, 3, 4, 5)
    // 6보다 작은 요소의 부정이니 6을 포함한 그 이상의 숫자로 걸러질 것이다.
    println(listOfForFilter.filterNot { it < 6 })

    val mapOfForFilter = mapOf(1 to "abc", 2 to "befgh", 3 to "tuvwxyz")
    // key가 3보다 큰 요소의 부정이니 map의 모든 요소가 나올것이다.
    println(mapOfForFilter.filterNot { it.key > 3 })
    // 값의 스트링 길이가 3보다 큰 요소의 부정이니 (1 to "abc")만 나온다.
    println(mapOfForFilter.filterNot { it.value.length > 3 })
    // 부정 변형은 없지만 key, value에 맞춰서 사용할 수 있다.
    println(mapOfForFilter.filterKeys { it > 3 })
    println(mapOfForFilter.filterValues { it.length > 3 })

    val rawTypeList = listOf(1, "Basquiat", mapOf(1 to "Map"), 2, 4, "Warhol", listOf("a", "b", "c"))
    val stringList = rawTypeList.filterIsInstance<String>()
    val intList = rawTypeList.filterIsInstance<Int>()
    val mapList = rawTypeList.filterIsInstance<Map<Int, String>>()
    val listList = rawTypeList.filterIsInstance<List<String>>()
    println("stringList : $stringList")
    println("intList : $intList")
    println("mapList : $mapList")
    println("listList : $listList")

    val listByIndex = listOf(1, 2, 10, 12, 6, 3, 4, 5)

    // 인덱스와 요소의 값으로 무언가를 해볼 때 사용하는 함수이다.
    val filteredByIndex = listByIndex.filterIndexed { i, _ -> i > 3 }
    val filteredByValue = listByIndex.filterIndexed { _, v -> v > 4 }
    println("filteredByIndex : $filteredByIndex")
    println("filteredByValue : $filteredByValue")

    val listIncludeNull = mutableListOf(1, 2, null, 12, 6, null, 4, 5)
    println("original : $listIncludeNull")
    val filteredNotNull = listIncludeNull.filterNotNull()
    println("filteredNotNull : $filteredNotNull")

    val mutableList = mutableListOf<Int>()
    val lisTo = listOf(6,1,7,9,3,11)
    // 고전적인 방식
    //val filterFromMutableList = listTo.filter { it > 5 }
    //mutableList.addAll(filterFromMutableList)

    // To함수를 이용해서
    println("before mutableList : $mutableList")
    list.filterTo(mutableList) { it > 5 }
    println("after mutableList : $mutableList")

    //2로 나눈 몫이 0이면 짝수
    val (evens, odds) = (1..10).toList().partition { it % 2 ==0 }
    println("evens : $evens")
    println("odds : $odds")


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

    // Map과 Set 역시 가능
    val maps = mapOf(3 to "Smith", 1 to "Foster", 2 to "Foster")
    val minNameOfOrNullMap = maps.minOfOrNull { (_, name) -> name }
    val minNameByOrNullMap = maps.minByOrNull { (_, name) -> name }
    println("test min name of and by diff [ $minNameOfOrNullMap : $minNameByOrNullMap ]")
    val minAgeOfOrNullMap = maps.minOfOrNull { (key, _) -> key }
    val minAgeByOrNullMap = maps.minByOrNull { (key, _) -> key }
    println("test min age of and by diff [ $minAgeOfOrNullMap : $minAgeByOrNullMap]")

    val minWithBy = users.minWithOrNull(compareByDescending<User> { it.age }.thenBy { it.name })
    val minWithByDesc = users.minWithOrNull(compareByDescending<User> { it.age }.thenByDescending { it.name })
    println("$minWithBy : $minWithByDesc")

    val listMinOfWith = listOf(4,1,8,3,2,5,4)
    val minOfWithValue = listMinOfWith.maxOfWithOrNull(compareBy { it }) { it * 10 }
    println("minOfWithValue : $minOfWithValue")

    val listOfJoin = listOf(1, 2, 3, 4, 5)
    println(listOfJoin.joinToString())
    println(listOfJoin.joinToString(",", "[", "]"))
    // named argument을 이용해 코틀린의 기본 구분자를 사용하고 prefix, susfix를 붙일 수 있다.
    println(listOfJoin.joinToString(prefix ="["))
    println(listOfJoin.joinToString(postfix ="]"))
    println(listOfJoin.joinToString(prefix ="[", postfix ="]"))

    // 화면상에서 길이가 길면 '이것은 긴 배열입니다. 따라서....'같이 뒤에 ...를 통해서 특정 길이를 체크해 자를수 있다.
    // 이때 limit는 join할 리스트의 요소 갯수이다. 따라서 밑에 코드는 1, 2, 3, 까지 스트링을 조이닝하고
    // 기본 truncated는 ...이기 때문에 결과는 1, 2, 3, ...
    println(listOfJoin.joinToString(limit = 3))
    // truncated 지정
    println(listOfJoin.joinToString(limit = 3, truncated = "....."))
    // 구분자는 공백없는 ','로
    println(listOfJoin.joinToString(separator = ",", limit = 3, truncated = "....."))
    // 후행 담다를 통해서 해당 요소에 문자를 붙여보자
    println(listOfJoin.joinToString(separator = ",", limit = 3, truncated = ".....") { "$it (숫자) "})

    val builder = StringBuilder("use joinTo fun ")
    val resultJoinTo = (1..10).joinTo(builder, separator = ",", prefix = "[", postfix = "]")
    println("resultJoinTo : $resultJoinTo")

    val reduceResult = (1..4).reduceIndexed { index, acc, i ->
                                                println(index)
                                                if(index % 2 == 0)
                                                    acc + (i * index)
                                                else
                                                    acc
                                            }
    println("reduceResult : $reduceResult")

    val foldResult = (1..4).fold(0) { acc, i -> acc + i}
    println("foldResult : $foldResult")

    val foldResultOtherType = (1..4).fold("0") { acc, i -> acc + i}
    println("foldResultOtherType : $foldResultOtherType")

    val foldResultOtherTypeInt = listOf("1", "2", "3").fold(0) { acc, i -> acc + i.toInt()}
    println("foldResultOtherTypeInt : $foldResultOtherTypeInt")

    val reduceToFold = (1..4).foldIndexed(0) { index, acc, i ->
        if(index % 2 == 0) {
            acc + i*index
        } else {
            acc
        }
    }
    println("reduceToFold : $reduceToFold")

    val foldRight = listOf(1,2,3,4).foldRight(0) { n, acc ->
                                                    println("$n $acc")
                                                    acc + n
                                                 }
    println("foldRight : $foldRight")

    val foldRight = listOf(1,2,3,4).runningFold(0) { n, acc ->
        println("$n $acc")
        acc + n
    }
    println("foldRight : $foldRight")

    val runningFold = listOf(1,2,3,4).runningFold(0) { acc, i -> acc + i }
    println("runningFold : $runningFold")
}