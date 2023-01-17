typealias TriFunction<T> = (T) -> (T) -> T

/**
 * HOF의 특징을 보여주는 일반적인 코드
 */
fun main() {
    val f: (Int) -> (Int) -> Int = { x ->
        { y ->
            x + y
        }
    }
    val g = f(1)
    println(g(2))
}

/**
 * typealias를 활용해서
 */
fun main() {
    val f: TriFunction<Int> = { x ->
        { y ->
            x + y
        }
    }
    val g = f(1)
    println(g(2))
}

/**
 * 람다를 활용한 순차적인 형태로
 */
fun main() {
    val f = { x: Int -> { y: Int -> x + y } }
    val g = f(1)
    println(g(2))
}

/**
 * 함수로 뺴보자
 */
fun main() {
    val g = f(1)
    println(g(2))
}

fun f(x: Int) = { y: Int -> x + y }


enum class DayType {
    Yesterday,
    Today,
    Tomorrow
}

enum class LogType {
    Info,
    Debug,
    Waring,
    Error
}

/**
 * 커리한 함수를 활용하지 않고 작성해 보기
 */
fun main() {

    val message = "이것은 메세지다."

    // 어제 info 메세지 찍기
    printLog(DayType.Yesterday, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)
    // 어제 info 메세지 찍기
    printLog(DayType.Yesterday, LogType.Info, message)
    // 내일 info 메세지 찍기
    printLog(DayType.Tomorrow, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)
    // 오늘 info 메세지 찍기
    printLog(DayType.Today, LogType.Info, message)

}

fun printLog(dayType: DayType, logType: LogType, message: String) {
    println("[${dayType.name}][${logType.name}] $message")
}

/**
 * 커리된 함수로 게으른 평가를 통해 함수 재활용하기
 */
fun main() {

    val message = "이것은 메세지다."

    val yesterdayInfo = printLog(DayType.Yesterday)(LogType.Info)
    val todayInfo = printLog(DayType.Today)(LogType.Info)
    val tomorrowInfo = printLog(DayType.Tomorrow)(LogType.Info)

    // 어제 info 메세지 찍기
    yesterdayInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)
    // 어제 info 메세지 찍기
    yesterdayInfo(message)
    // 내일 info 메세지 찍기
    tomorrowInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)
    // 오늘 info 메세지 찍기
    todayInfo(message)

}

fun printLog(dayType: DayType) = {
        logType: LogType ->
    { message: String ->
        println("[${dayType.name}][${logType.name}] $message")
    }
}

/**
 * 부분 적용 함수 만들어보기
 */
fun main() {
    //val add: (Int, Int) -> Int = { x, y -> x + y }
    val add = { x: Int, y: Int -> x + y }
    val constantValue = 10
    val partial: (Int) -> Int = { a -> add(a, constantValue) }
    println(partial(10))
    println(partial(11))
}

typealias IntFunction = (Int, Int) -> Int

fun main() {
    val add: IntFunction = { x, y -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun IntFunction.partialApplied(p: Int): (Int) -> Int {
    return { v -> this(v, p) }
}

typealias Function<T, U, R> = (T, U) -> R

fun main() {
    val add: Function<Int, Int, Int> = { x, y -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun <T, U, R> Function<T, U, R>.partialApplied(p: U): (T) -> R {
    return { v -> this(v, p) }
}

fun main() {
    //val add: (Int, Int) -> Int = { x, y -> x + y }
    val add = { x: Int, y: Int -> x + y }
    val constantValue = 10
    val partial = add.partialApplied(constantValue)
    println(partial(10))
    println(partial(11))
}

fun <T, U, R> ((T, U) -> R).partialApplied(p: U): (T) -> R {
    return { v -> this(v, p) }
}