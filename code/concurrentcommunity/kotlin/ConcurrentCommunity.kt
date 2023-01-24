fun main() {

    // 일반적인 채널 사용법
    runBlocking {
        val size = 5
        val channel = Channel<String>()

        launch {
            (1..size).forEach {
                delay(Random.nextLong(100))
                val channelName = "channel-$it"
                println("name is $channelName")
                channel.send(channelName)
            }
        }

        launch {
            (1..size).forEach { _ ->
                delay(Random.nextLong(100))
                val receivedValue = channel.receive()
                println("received value is $receivedValue")
            }
        }
    }

    // Channel.CONFLATE
    runBlocking {
        val size = 5
        val channel = Channel<String>(Channel.CONFLATED)

        launch {
            (1..size).forEach {
                delay(100)
                val channelName = "channel-$it"
                println("name is $channelName")
                channel.send(channelName)
            }
            channel.close()
        }

        launch {
            for (it in channel) {
            //channel.consumeEach {
                println("received : $it")
                delay(100)
            }
        }
    }

    // fan out
    runBlocking {
        val size = 5
        val channel = Channel<String>(2)

        // 하나의 코루틴이 채널과 통신한다.
        launch {
            (1..size).forEach {
                val channelName = "channel-$it"
                println("name is $channelName")
                channel.send(channelName)
            }
            channel.close()
        }

        // 소비자 코루틴을 3개 정도 생성해서 수신을 한다.
        (1..3).forEach {
            launch {
                for (chan in channel) {
                    //channel.consumeEach { chan ->
                    println("received consumer no-$it, data is $chan")
                    delay(Random.nextLong(100))
                }
            }
        }
    }

    // fan in
    runBlocking {
        val size = 5
        val channel = Channel<String>(2)

        (1..size).forEach {
            launch {
                val channelName = "channel-$it"
                println("name is $channelName")
                channel.send(channelName)
            }
        }

        //for ( i in 1..size) {
        repeat(size) {
            val received =  channel.receive()
            println("received data is $received")
        }
        //coroutineContext.cancelChildren()
    }

    // ticker
    runBlocking {
        //val ticker = ticker(100, mode = TickerMode.FIXED_DELAY)
        val ticker = ticker(100, mode = TickerMode.FIXED_PERIOD)
        delay(150)
        println(withTimeoutOrNull(50) { ticker.receive() })
        println(withTimeoutOrNull(50) { ticker.receive() })
    }

    // Mutex
    runBlocking(Dispatchers.Default) {
        increase()
    }
    println("counter is $counter")

}

private var counter = 0

val mutex = Mutex()

suspend fun increase() = coroutineScope{
    (1..100).forEach { _ ->
        launch(Dispatchers.Default) {
            mutex.withLock {
                counter++
            }
        }
    }
}