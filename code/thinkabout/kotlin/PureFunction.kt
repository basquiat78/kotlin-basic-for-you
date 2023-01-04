fun <T> addTo(element: T, list: MutableList<T>): List<T> {
    list.add(element)
    return list
}

fun <T> addElement(element: T, list: List<T>): List<T> {
    return list + element
}

fun main() {
    val mutableList = mutableListOf("A", "B", "C")
    val result = addTo("D", mutableList)
    // addTo함수로 인해 함수 외부의 mutableList가 변이되었다.
    // 또한 함수 내부에서도 인자로 들어온 mutableList역시 변이되었다는 것을 알 수 있다.
    println("result is $result")
    println("mutableList is $mutableList")

    val immutableList = listOf("A", "B", "C")
    val newList = addElement("D", immutableList)
    println("newList is $newList")
    println("immutableList is $immutableList")

    // 컬렉션 함수는 원본 데이터에서 새로운 컬렉션으로 반환하는 것을 증명하는 코드.
    val toMutableList = immutableList.map { it.lowercase() }
                                     .toMutableList()
    toMutableList.add("test")

    println("immutableList is $immutableList")
    println("toMutableList is $toMutableList")
}