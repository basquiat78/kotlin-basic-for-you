fun main() {
    val myThread = MyThread(5)
    myThread.start()

    val runnable = MyRunnable(5)
    val myRunnable = Thread(runnable)
    myRunnable.start()


    val loop = 5

    // 익명 함수
    val anonymous = object: Thread() {
        override fun run() {
            repeat(loop) {
                println("anonymousThread-$it Thread Name (${currentThread().name}))")
            }
        }
    }
    anonymous.start()

    // 람다
    val lambda = Thread {
        repeat(loop) {
            println("lambdaThread-$it Thread Name (${Thread.currentThread().name}))")
        }
    }
    lambda.start()

    createThread("basquiat") {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }.start()


    val strictThread = thread(start = true) {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }

    val lazyThread = thread(start = false) {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }

    // do something

    lazyThread.start()

    // isDaemon이 true/flase냐에 따른 결과를 살펴보자
    //thread(name = "DaemonTest", isDaemon = true) {
    thread(name = "DaemonTest") {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
            Thread.sleep(200)
        }
    }

    Thread.sleep(800)
    println("main thread is end.....")

    val threadArray = mutableListOf<Thread>()

    repeat(5) {
        val thread = thread(name = "DaemonTest", start = false, isDaemon = true) {
            println("Thread Name (${Thread.currentThread().name}))")
        }
        thread.start()
        threadArray.add(thread)
    }

    threadArray.forEach {
        try {
            it.join()
        } catch (e: Exception) {

        }
    }

    println("main thread is end.....")


    var counter = 0

    fixedRateTimer(name = "TIMER", period = 1000, daemon = false) {
        //timer(name = "TIMER", period = 1000, daemon = false) {
        println("Thread Name (${Thread.currentThread().name}))")
        counter++
        if (counter % 3 == 0) {
            Thread.sleep(2100)
        }
    }

    println("main thread is end...")

}

// Thread 객체를 상속
class MyThread(
    private val loop: Int
): Thread() {
    override fun run() {
        repeat(loop) {
            println("MyThread-$it (thread name: $name)")
        }
    }
}

class MyRunnable(
    private val loop: Int
): Runnable {
    override fun run() {
        repeat(loop) {
            println("MyThread-$it (thread name: $name)")
        }
    }
}


/**
 * @param name -> thread name
 * @param run -> action block
 */
fun createThread(name: String? = null, run: () -> Unit): Thread {
    val thread = object: Thread() {
        override fun run() {
            run()
        }
    }
    // name이 null이면 Thread.currentThread().name을 따르고 아니면 이걸로 세팅한다.
    name?.let { threadName -> thread.name = threadName }
    return thread
}