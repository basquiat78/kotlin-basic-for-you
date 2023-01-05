import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() {

    /*
    runBlocking {
        val asyncResult = async {
            "result"
        }
        val result = asyncResult.await();
        println("result is $result")
    }
    */

    // async 코루틴 빌드이외데 withContext를 사용해 좀 더 간결하게 사용할 수 있다.
    /*
    runBlocking {
        val result = withContext(Dispatchers.Default) {
            "result"
        }
        println("result is $result")
    }
    */

    // 이코드는 async와 withContext의 차이점을 보여주는 코드이다.
    //val limit = 100_000
    //val limit = 10_000
    val limit = 1000

    runBlocking {
        launch {
            val asyncDiff = measureTimeMillis {
                for (i in 1..limit) {
                    val result = async(Dispatchers.Default) {
                        i * 100
                    }
                }
            }
            println("asyncDiff is $asyncDiff")
        }

        launch {
            val withContextDiff = measureTimeMillis {
                for (i in 1..limit) {
                    val result = withContext(Dispatchers.Default) {
                        i * 100
                    }
                }
            }
            println("withContextDiff is $withContextDiff")
        }
    }

}