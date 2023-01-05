import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * coroutinScope를 사용해 커스텀 영역을 만들 수 있다.
 * 또한 반환값을 가질 수 있다.
 */
fun main() {
    runBlocking {
        customScope()
        val a = returnA()
        println("a is $a")
        val b = returnB()
        println("b is $b")
        println("RunBlocking Thread : ${Thread.currentThread().name}")
    }
    println("End")
}

/**
 * coroutineScope는 일시 중단 함수로 다음과 같이 자식 코루틴을 갖는 커스텀 영역을 만들 수 있다.
 */
suspend fun customScope() = coroutineScope {
    launch {
        delay(100)
        println("Launch Thread1 : ${Thread.currentThread().name}")
    }

    launch {
        delay(100)
        println("Launch Thread2 : ${Thread.currentThread().name}")
    }
    println("customScope Thread : ${Thread.currentThread().name}")
}

suspend fun returnA() = coroutineScope {
    delay(100)
    println("ReturnA Thread : ${Thread.currentThread().name}")
    "A"
}

suspend fun returnB() = coroutineScope {
    delay(100)
    println("returnB Thread : ${Thread.currentThread().name}")
    "B"
}