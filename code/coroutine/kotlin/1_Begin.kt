import kotlinx.coroutines.delay

/**
 *
 * 일반 함수는 일시 중단 함수를 호출할 수 없다.
 * 하지만 일시 중단 함수는 일반 함수와 일시 중단 함수를 호출할 수 있다.
 *
 */
suspend fun main() {
    startAndEnd()
}

suspend fun startAndEnd() {
    start()
    delay(1000)
    end()
}

fun start() {
    println("시작!!!")
}

fun end() {
    println("끝!!!")
}