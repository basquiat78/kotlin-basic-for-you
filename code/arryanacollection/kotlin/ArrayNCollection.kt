// operator overloading fun
operator fun Int.plus(into: String): String {
    return "$this  (Int + String)    $into"
}

fun main() {
    // 기본적으로 immutable하다.
    val strList: List<String> = listOf("a", "b")
    for(value in strList) {
        println("strList is $value")
    }
    val checked = strList.contains("c")
    println("checked is $checked")

    // 밑에 코드는 No set method providing array access가 뜬다.
    // 그리고 이것을 허용하려면 mutableList로 변경하라고 친절하게 알려준다.
    // strList[0] = "c"

    // null이 있다면 리스트에서 제외시킨다.
    val notNullList: List<String> = listOfNotNull("a", null, "c", null, null, "d")
    println(notNullList.size) // 사이즈는 null을 제외했기 때문에 3
    for(value in strList) {
        println("notNullList is $value")
    }

    // 빈 리스트를 반환하고자 한다면 사용할 수 있다.
    val listEmpty: List<String> = emptyList()

    // 수정/삭제/변경이 가능한 mutableList 생성
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

    // 직접적으로 생성하기.
    // 사이즈는 초기 선언해서 생성하지만 길이가 고정이 아니기 때문에
    // 추가 및 삭제가 가능하다.
    val direct = MutableList(5) { "a[${it}]" }
    println(direct)
    direct.add("direct")
    println(direct)

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

    val specificList = listOf(1,2,3,4,5)
    // ArrayIndexOutOfBoundsException Index 6 out of bounds for length 5
    //val outOfIndex = specificList[6]
    //println("outOfIndex is $outOfIndex")

    // ArrayIndexOutOfBoundsException라면 null로 그냥 반환한다.
    val escapeOutOfIndex = specificList.getOrNull(6)
    println("escapeOutOfIndex is $escapeOutOfIndex")

    // 후행 람다 전달 방식로 특정 값으로 처리해 반환할 수 있다.
    val defaultWhenOutOfIndex = specificList.getOrElse(6) { "존재하지 않는 index" }
    println("defaultWhenOutOfIndex is $defaultWhenOutOfIndex")

    // Pair를 생성할때는 내부적으로 infix함수로 받도록 설계되어 있다.
    // 밑에 방식도 가능하지만 IDE에서는 infix form으로 사용하도록 추천하고 있다.
    //val immutableMap = mapOf(1.to("a"), 2.to("b"))
    // 앞이 key가 되고 to 뒤로 오는 것이 value가 되는 형식이다.
    //val immutableMap: Map<Int, String> = mapOf(1 to "a", 2 to "b")
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

    // 제너릭하지 않게 사용한 mapOf의 경우 어떤 타입도 k,v로 들어온다.
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

    // 기본은 immutable
    val immutableSet = setOf("a", "b", "c")
    println("immutableSet : $immutableSet")
    println("immutableSet size : ${immutableSet.size}")
    println("immutableSet : $immutableSet")

    val mutableSet = mutableSetOf("a", "b", "c")
    mutableSet.add("D")
    println("mutableSet : $mutableSet")
    // 후행 람다 전달 방식
    mutableSet.removeIf { it == "a" || it == "D" }
    println("mutableSet after removeIf : $mutableSet")

    // 예상대로 mutableSet으로 변경하는 함수가 있다.
    val immutableToMutableSet = immutableSet.toMutableSet()
    immutableToMutableSet.add("d")
    println("immutableToMutableSet : $immutableToMutableSet")

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

    val beforeSet = mutableSetOf(1,2,6,3)
    val afterSet = mutableSetOf(3,7,5,8,6)
    //val intersectSet = beforeSet.intersect(afterSet)
    val intersectSet = beforeSet intersect afterSet
    println("intersectSet : $intersectSet")

    //val subtractSet = beforeSet.subtract(afterSet)
    // expected 1, 2
    val subtractSet = beforeSet subtract afterSet
    println("subtractSet : $subtractSet")

    // operator overloading test
    val prev = 100
    val next = "10000"
    // Int + String이 자바처럼 암묵적인 형변환을 허용하지 않는다.
    // 물론 String + Int의 경우에는 가능하다. 문자열로 반환하게 된다. -> 1000100
    // 그래서 탑레벨에 operator overloading을 통해 아래 코드가 작동하도록 만들자.
    // result -> "100  (Int + String)    10000"
    println(prev + next)

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

    val prevMap = mutableMapOf(1 to "Jean-Michel Basquiat", 2 to "Andy Warhol")
    val nextMap = mutableMapOf(3 to "Keith Haring", 4 to "Joseph Beuys")
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

    val prevSet = mutableSetOf("Jean-Michel Basquiat", "Andy Warhol")
    val nextSet = mutableSetOf("Keith Haring", "Joseph Beuys")
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
