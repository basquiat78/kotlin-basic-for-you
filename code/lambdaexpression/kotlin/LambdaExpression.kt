fun main() {

    // 모든 내용을 훝어보셨다면
    // 후행 람다 전달 방식으로 표현되는 것을 알 수 있을 것이다.
    //val noInline = noInline({ "test".uppercase() })
    //val useInline = useInline({ "test".uppercase() })
    val noInline = noInline { "test".uppercase() }
    val useInline = useInline { "test".uppercase() }
    println("noInline is $noInline")
    println("useInline is $useInline")

    // param이 없다면
    val anyFuntion: () -> String = { "Hello Any Function" }
    println(anyFuntion())

    val oneParamFuntion: (String) -> String = { "Hello Any Function $it" }
    //val oneParamFuntion: (String) -> String = {message ->  "Hello Any Function $message" }
    println(oneParamFuntion("with One Param"))

    val overTwoParamFunction: (String, String) -> String = { p1, p2 -> "$p1 그리고 $p2"}
    println(overTwoParamFunction("Param1", "Param2"))

    val anyFuntionDirect = { "Hello Any Function Direct" }
    println(anyFuntionDirect())

    val oneParamFuntionDirect = {it: String ->  "Hello Any Function $it" }
    //val oneParamFuntion: (String) -> String = {message ->  "Hello Any Function Direct $message" }
    println(oneParamFuntionDirect("with One Param"))

    val overTwoParamFunctionDirect = { p1: String, p2: String -> "Direct $p1 그리고 $p2"}
    println(overTwoParamFunctionDirect("Param1", "Param2"))

    val first = 100
    val second = 200
    val result = forTrailing(10, { first + second })
    println("result is $result")

    val resultForTrailing = forTrailing(10){ first + second }
    println("resultForTrailing is $resultForTrailing")

    val filteredNums = (1..10).filter({it in 4..8})
    println("filteredNums is $filteredNums")

    val filteredList = (1..10).filter {it in 4..8}.toList()
    println("filteredList is $filteredList")

    val filter: (Int) -> Boolean = { it in 4..8}
    val filteredWithList = (1..10).filter(filter).toList()
    println("filteredWithList is $filteredWithList")
}

inline fun forTrailing(value: Int, block: () -> Int): Int {
    return value + block()
}

// 인자없는 익명 함수 block을 받아서
fun <R> noInline(block: () -> R): R {
    return block()
}

// 인자없는 익명 함수 block을 받아서
inline fun <R> useInline(block: () -> R): R {
    return block()
}