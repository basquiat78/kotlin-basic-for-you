import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Job 동시성 작업의 생명주기를 표현하는 객체이다.
 * 기본적으로 CoroutineStart객체를 통해 초기 상태를 설정한다.
 * 어떤 설정도 하지 않는다면 기본은 CoroutineStart.DEFAULT이다.
 *
 */
fun main() {
    runBlocking {
        // CoroutineStart.DEFAULT은 생성과 동시에 활성화된다.
        val job = launch {
            println("OK job is start")
        }
        delay(100)



        // CoroutineStart.LAZY는 생성은 하자미나 활성화는 지연시킨다.
        val lazyJob = launch(start = CoroutineStart.LAZY) {
            println("OK job is start")
        }
        delay(100)
        println("Job is isCompleted? : ${lazyJob.isCompleted}") // result 2
        println("before job start")
        lazyJob.start()
        delay(100)
        println("Job is isCompleted? : ${lazyJob.isCompleted}") // result 0



        // children 프로퍼티를 통해 자식 Job의 정보에 접근할 수 있다.
        val parentJob = coroutineContext[Job.Key]!!
        val job1 = launch(start = CoroutineStart.LAZY) { println("Task One") }
        val job2 = launch(start = CoroutineStart.LAZY) { println("Task Two") }
        println("child coroutine: ${parentJob.children.count()}") // result 2
        job1.start()
        job2.start()
        //delay(100)
        //자식 Job이 완료될떄까지 딜레이를 줬지만 join을 통해서 해당 Job이 완료될때까지 기다릴 수 있다.
        job1.join()
        job2.join()
        println("child coroutine: ${parentJob.children.count()}") // result 0


        // job cancel
        val longMaxJob = launch {
            for(i in 1..Long.MAX_VALUE) {
                println("i is $i")
                delay(100)
            }
        }
        delay(1000)
        //longMaxJob.cancelAndJoin()
        longMaxJob.cancel()
        longMaxJob.join()
        println("cancel completed")



        // 부모 Job이 취소되면 자식 Job도 취소가 된다.
        val parent = launch {
            println("parent start")
            child()
            delay(500)
            println("parent end")
        }
        delay(100)
        parent.cancel()




        // timeout
        val callApi = async {
            delay(50)
            //delay(150)
            api()
        }

        try {
            val result = withTimeout(100) { callApi.await() }
            //val result = withTimeoutOrNull(100) { callApi.await() }
            println(result)
        } catch (e: TimeoutCancellationException) {
            println(e.message)
        }
    }
}

suspend fun child() = coroutineScope {
    launch {
        println("childOne start")       // (9)
        delay(100)                      // (10)
        println("childOne end")         // (11)
    }

    launch {
        println("childTwo start")       // (12)
        delay(100)                      // (13)
        println("childTwo end")         // (14)
    }
}

fun api(): String {
    return "OK"
}