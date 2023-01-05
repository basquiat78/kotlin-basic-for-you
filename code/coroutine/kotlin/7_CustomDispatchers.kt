import kotlinx.coroutines.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger

fun main() {

    val id = AtomicInteger(0)
    // core size를 5
    // 즉 5개의 특정 스레드 이름을 갖는 디스패처를 생성한다.
    // ScheduledThreadPoolExecutor는 코드를 따라가다보면 최종적으로 Executor의 구현체이다.
    // java.util.concurrent의 고수준의 API를 활용해 일종의 분배기를 만든다.
    val executor = ScheduledThreadPoolExecutor(5) { runnable ->
        Thread(
            runnable,
            "Thread-${id.incrementAndGet()}"
        ).apply { isDaemon = true}
    }

    // 여기서 use를 사용하는 것에 주목하자.
    // asCoroutineDispatcher은 ExecutorCoroutineDispatcher를 반환한다.
    // 이것은 Closeable를 구현하고 있기 때문에 우리가 [이펙티브 코틀린]을 봤다면
    // 스레드를 위해 할당했던 리소스를 해제아기 위해 close()를 사용하거나 use를 사용해 자동으로 해제하도록 한다.
    executor.asCoroutineDispatcher().use { dispatcher ->
        runBlocking {
            for(i in 1..10) {
                launch(dispatcher) {
                    println(Thread.currentThread().name)
                    delay(1000)
                }
            }
        }
    }


    // 위 코드는 실제로 코틀린의 ThreadPoolDispatcher.kt에 구현된 코드로 다음과 같이 사용할 수 있다.
    // newSingleThreadContext은 newFixedThreadPoolContext의 스레드 갯수를 1로 고정해서 만든 API이다.
    newFixedThreadPoolContext(5, "Thread").use { dispatcher ->
        runBlocking {
            for(i in 1..10) {
                launch(dispatcher) {
                    println(Thread.currentThread().name)
                    delay(1000)
                }
            }
        }
    }

}