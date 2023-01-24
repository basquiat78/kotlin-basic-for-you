# 동시성 통신

이번 챕터에서는 `Channel`, `Producer`, `Ticker`, `Actor`를 다뤄볼 예정이다.

# 채널

채널은 동시성이라는 영역내에서 서로 안전하게 통신을 할 수 있도록 해준다.    

먼저 채널하면 떠오르는 흔히 생각하는 티비 채널같은 것을 생각해 보자.     

수많은 채널중에 하나를 선택해서 우리는 시청을 하게 된다.    

수많은 채널중에 하나가 나에게 메세지를 보내고 나는 그것을 본다는 상상을 해보자. 

고랭에서도 동시성 통신을 위해 채널을 활용하고 있다. 

그 중에 이더리움 코드들을 살펴보면 이 채널을 어마무시하게 사용하고 있다.     

`chan`을 통해서 채널을 생성해서 결과값을 받거나 넘길 수 있다.     

또한 함수를 넘길수도 있고 채널 `chan chan`을 통해서 채널를 반환하거나 넘기기도 한다.    

물론 이런 뎁스가 깊어질 수록 코드를 분석하거나 따라가는것이 어려워질 수 있지만 그만큼 안전하게 통신을 하기 때문에 가능할 것이다. 

어째든 그렇다면 코틀린에서는 이 채널을 어떻게 활용할 수 있을까? 

이것이 주된 주제가 될것이다.

## Channel Interface

`Channel Interface`는 채널에 대한 기본적인 연산을 제공하는데 그것은 익숙한 이름의 `send`와 `receive`함수이다.       

사실 자바에도 이와 비슷한 역할을 하는 `BlockingQueue`가 존재한다. 

하지만 이름에도 알 수 있듯이 `BlockingQueue`는 말 그대로 스레드를 블럭킹한다.      

그리고 `BlockingQueue`의 `put`은 `send`와 `take`는 `receive`와 매칭할 수 있을 것이다.

그러나 코틀린의 채널은 채널 내의 버퍼가 꽉 찬다면 스레드를 블럭시키는 것이 아니라 일시 중단시켰다가 처리가 가능한 시점에 다시 재개시킨다.

그렇다면 채널은 어떻게 생겨먹은 넘일까?

```kotlin
public interface Channel<E> : SendChannel<E>, ReceiveChannel<E>
```
코드를 보면 `SendChannel`과 `ReceiveChannel`를 상속하고 있다.

각각의 코드가 어떻게 생겼는지 알지 못하더라도 코드 자체만으로 어떻게 동작하는지 대충 감이 올만큼 명확하다.

그리고 각각의 인터페이스를 따라가면 위에서 언급한 `send`와 `receive`함수를 볼 수 있고 `suspend`가 붙은 `일시 중단 함수`이라는 것을 확인할 수 있다.            

물론 해당 인터페이스를 따라가면 `trySend`, `tryReceive`같은 `일시 중단 함수`가 아닌 녀석들도 존재한다.      

이미 여러분들은 코루틴을 통해서 `일시 중단 함수`의 특징을 알고 있기 때문에 이 두 함수의 특징이 어떨지 예상할 수 있다.      

어째든 코드로 한번 우리는 살펴보는게 젤 좋다.

```kotlin
fun main() {

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

}
```
다음과 같이 채널을 생성하고 코루틴내에서 루프를 돌면서 채널로 생성한 값을 보내자.

여기서 우리는 채널의 용량을 루프 사이즈보다 작은 3으로 잡아두었다. 

만일 정하지 않는다면 채널 용량은 기본적으로 정해진 내부 버퍼 사이즈를 적용하게 된다.

여기서 `delay`함수에 랜덤한 시간을 정해서 보내게 되면 보내는 쪽과 받는 쪽의 코루틴중에서 채널 용량으로 인해 한쪽의 버퍼가 차거나 비게 만들수 있다.    

즉 일시 중단을 발생시켜서 다양한 출력이 나올 수 있도록 만든 코드이다.

실제로 보내고 받는 출력은 순서가 섞여 있을 수 있지만 어찌되었든 수신쪽에서는 보낸 순서대로 받는 것을 확인할 수 있다.

만일 용량 사이즈와 루프 사이즈를 똑같이 설정하면 순차적으로 주고 받는 것을 확인할 수 있다.

## Channel is FAIR

위 내용을 토대로 보면 채널은 공평하다.     

FIFO(First-In First-Out)에 입각해서 특정 채널이 독점하지 않고 순차적으로 데이터를 가져가는 것을 볼수 있다.

## Channel Capacity

여기서 우리 `Channel`함수의 동작을 바꾸는 채널 용량을 정의한 값을 살펴볼 필요가 있다.

```
1. Channel.UNLIMITED: 채널의 용량을 Int.MAX_VALUE만큼 설정한다.
    - 거의 용량의 제한이 없기 때문에 send에서는 일시 중단이 발생하지 않는다.
    - 다만 receive쪽에서는 버퍼가 비는 경우가 생길 수 있는데 이때는 일시 중단이 발생할 수 있다.
2. Channel.RENDEZVOUS: 채널의 용량을 0으로 잡는 경우로 내부 버퍼가 없는 랑데부 채널이 된다.
    - send함수를 호출할 때 다른 코루틴의 receive를 호출할 때까지 항상 일시 중단이 된다.
    - 이 역도 똑같이 성립한다.
    - 따라서 교대로 활성화되기 때문에 안정적인 동작 순서를 보게 된다.
    - 이것은 루프 사이즈와 버퍼 사이즈가 같을 때랑도 똑같다.
3. Channel.CONFLATED: -1로 잡혀져 있다. 이것은 송신된 값이 합져지는 채널이라고 한다.
    - send함수가 호출되면 하나의 버퍼에만 저장하고 이것을 receive할때까지 기다린다.
    - 이때 만일 다른 send함수가 요청되면 기존 버퍼에 저장된 값이 덥어진다.
    - 따라서 이전 수신받지 못한 값이 소실될 수 있다.
    - send함수는 일시 중단되지 않는다.
    
4. Channel.BUFFERD: -2로 잡혀있다.
    - 사실 이와 관련 자료를 찾아봐도 마땅한게 없다.
    - API의 내용을 살펴보면 시스템이 정한 용량을 활당한다는 것을 알 수 있다.
    /**
     * Requests a buffered channel with the default buffer capacity in the `Channel(...)` factory function.
     * The default capacity for a channel that [suspends][BufferOverflow.SUSPEND] on overflow
     * is 64 and can be overridden by setting [DEFAULT_BUFFER_PROPERTY_NAME] on JVM.
     * For non-suspending channels, a buffer of capacity 1 is used.
     */
    public const val BUFFERED: Int = -2    
    
    /**
     * Name of the property that defines the default channel capacity when
     * [BUFFERED] is used as parameter in `Channel(...)` factory function.
     */
    public const val DEFAULT_BUFFER_PROPERTY_NAME: String = "kotlinx.coroutines.channels.defaultBuffer"

    internal val CHANNEL_DEFAULT_CAPACITY = systemProp(DEFAULT_BUFFER_PROPERTY_NAME,
        64, 1, UNLIMITED - 1
    )
```
다음과 같이 정의한 값들의 특징들을 살펴보았다.

그 중에서 특이점이 있는 `Channel.CONFLATED`를 사용한 코드를 확인해 보자.

## Channel.CONFLATED

일단 이 설정을 채널에 주게 되면 사용하는데 주의할 점이 생긴다.

먼저 보내는 쪽의 딜레이를 수신받는 쪽보다 길게 해보면 위에서 설명한 특징을 살펴볼 수 있다.

```kotlin
fun main() {

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
        }

        launch {
            (1..size).forEach { _ ->
                delay(200)
                val received = channel.receive()
                println("received is $received")
            }
        }
    }

}
```
해당 코드를 실행하게 되면 수신받는 코루틴쪽에서 딜레이되는 시간을 계산하면 3개만 받는 것을 알 수 있다.     

게다가 더 흥미로운 것은 이 코드를 실행하게 되면 종료가 되지 않고 계속 대기한다.      

이유는 수신받는 코루틴쪽에서는 5개가 올것이라고 판단하겠지만 실제로는 3개만 오기 때문에 계속 대기하고 있게 된다.     

이럴때는 채널을 닫아서 더 이상 데이터를 기다리지 말라고 신호를 줘야 한다.

따라서 이 코드는 다음과 같이 변경해야 한다.

```kotlin
fun main() {

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
            /*
            (1..size).forEach { _ ->
                delay(200)
                val received = channel.receive()
                println("received is $received")
            }
            */
            for (it in channel) {
                println("received : $it")
                delay(200)
            }

            /*
            channel.consumeEach {
                println("received : $it")
                delay(200)
            }
            */
        }
    }

}
```
기존 코드에서는 수신하는 코루틴쪽에서도 고정된 사이즈만큼 루프를 돌면서 채널의 정보를 가져왔었다.    

어째든 위 코드에서 맨위 주석처리된 코드로 실행하게 되면 송수신을 하면서 로그를 찍다가 채널을 닫게 되면 수신받는 쪽에서 

```
Exception in thread "main" kotlinx.coroutines.channels.ClosedReceiveChannelException: Channel was closed
```
와 같은 예외가 발생하게 된다.

물론 채널을 닫은 후에 송신을 해도 예외가 발생할 것이다.

따라서 고정된 사이즈로 루프를 돌리기보다는 채널에서 들어오는 데이터에 대해서 `for-loop`하는 방법을 취한다.

또한 `consumeEach`함수를 사용해서 채널의 콘텐츠를 가져오는 방법도 있다.

## fan out (팬 아웃) / fan in (팬 인)
지금까지 살펴본 예제는 채널을 통해서 통신에 참여해서 송신하는 프로듀서, 죽 생산자 역할을 하는 하나의 코루틴과 수신하는 컨슈머, 즉 소비자 역할을 하는 하나의 코루틴에 대한 예제이다.

하지만 한 채널에 여러 코루틴이 동시에 송신을 하거나 한 채널을 여러 채널이 동시에 수신할 수도 있다.

### fan out

`fan out`은 그 중에 한 채널을 여러 코루틴이 동시에 읽는 경우이다.

그렇다면 한번 코드로 짜보자. 

하나의 코루틴을 통해서 송신을 하면 루프를 돌면서 여러개의 코루틴이 수신을 하는 것을 상상하면 될것이다.

```kotlin
fun main() {

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

}
```
코드를 보면 3개의 컨슈머 코루틴을 생성해서 하나의 채널로부터 데이터를 수신하는 코드이다.

위에서 언급했듯이 `Channel is FAIR`가 그대로 드러난다.    

여러 개의 코루틴이 수신을 하더라도 특정 코루틴이 독점하는 것이 아니라 먼저 수신받은 코루틴이 데이터를 읽는다.

### fan in
여러 개의 코루틴이 생산자 역할을 하고 하나의 코루틴이 수신을 하는 경우이다.

이 때는 코드가 다음과 같이 변경이 된다.

```kotlin

fun main() {

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

}

```
`for-loop`를 통해서 5개의 생산자 코루틴을 만들고 `repeat`를 통해서 그 수만큼 돌면서 소비하는 방식이다.

`repeat`대신 주석된 부분을 사용해도 된다.    

만일 컨슈머쪽에서 루프를 한정해서 사용하겠다고 하면 size를 줄일 수 있는데 이때는 `Channel.CONFLATED`의 예제 코드에서 봤던 현상이 벌어진다.

5개가 들어오길 기대하겠지만 그 미만만 받겠다고 하면 일시 중단이 벌어지면서 프로그램이 종료되지 않기 때문이다.

이때는 주석처리한 `coroutineContext.cancelChildren()`을 활성화시켜서 코루틴을 취소시키면 프로그램이 종료가 된다.

### 특별한 코루틴 빌더 produce

동시성 데이터 스트림을 생성하는 방법으로 `produce`라는 특별한 코루틴빌더를 사용할 수 있다.

```kotlin
fun main() {

    runBlocking {
        val size = 5

        val channel = produce {
            (1..5).forEach {
                val name = "producer-$it"
                println("name is $name")
                send(name)
            }
        }

        launch {
            //for(it in channel) {
            channel.consumeEach {
                println("received data is $it")
            }
        }

    }
}
```
다음과 같은 방식으로 사용할 수 있다.       

이 코루틴 빌더는 코루틴이 종료가 되면 자동으로 채널을 닫아주는 역할도 한다.

```kotlin
public fun <E> CoroutineScope.produce(
    context: CoroutineContext = EmptyCoroutineContext,
    capacity: Int = 0,
    @BuilderInference block: suspend ProducerScope<E>.() -> Unit
): ReceiveChannel<E>
```
해당 함수를 살펴보면 `ReceiveChannel`를 반환한다.

그리고 이것을 통해서 스트림 데이터를 읽는 방식이다.      

일반적으로 이런 방식을 파이프라인 패턴이라고 할 수 있다.

이 패턴은 단지 코루틴에서만 국한된 패턴은 아니다. 

`go`에서도 이런 파이프라인 패턴이 적용된다.

## ticker
코루틴에는 티커라는 특별한 랑데뷰 채널이 존재한다.

랑데뷰는 위에서 우리가 배웠듯이 생산자와 소비자가 교대로 주고니 받거니 하는 특징이 존재한다.    

티커는 `Unit`값을 계속 발생시키는 채널로 사실 값을 주고 받는 채널은 아니다.      

그래서 상대적으로 활용도는 떨어질 수 있는 채널이다. 

개인적으로 이 티커를 사용할 일이 없었다.

그래도 특정 시간을 기준으로 지연된 시간만큼의 스트림을 만들기 때문에 이 특징을 이용하는 로직이 필요하다면 사용할 수 있을 것이다.

일단 어떻게 생겨먹었는지 확인해 보자.

```kotlin
public fun ticker(
    delayMillis: Long,
    initialDelayMillis: Long = delayMillis,
    context: CoroutineContext = EmptyCoroutineContext,
    mode: TickerMode = TickerMode.FIXED_PERIOD
): ReceiveChannel<Unit>
```

각 파라미터가 무엇인지 먼저 알아보자.

```
delayMillis : 티커가 스트림에 `Uint`을 채널에 전송하는 발생 시간 간격으로 단위는 밀리초이다.
initialDelayMillis : 기본값은 delayMillis에 따라간다. 티커의 생성 시점과 최초로 `Uint`요소를 발생시키는 시간 사이의 간격이다. 
context: 코루틴 문맥으로 코드에서 볼 수 있듯이 기본값은 빈 문맥이다.
mode: 티커의 행동 방식을 결정하는 모드로 2가지가 있다.
    - TickerMode.FIXED_PERIOD : 기본값으로 이 모드는 소비자쪽에서 지연이 되는 경우 다음 송신을 그에 맞춰서 실제 지연 시간을 조정한다.
    - TickerMode.FIXED_DELAY  : TickerMode.FIXED_PERIOD와는 달리 지정한 지연 시간 간격으로 무조건 원소를 송신한다.
```
스프링의 `Schedule`을 사용해 본 분이라면 대충 어떤 느낌인지 알 수 있을 것이다.

그렇다면 여기서 우리는 저 모드에 대해서 알아보자.

사실 여러 책에서는 이 티커를 설명할때의 코드가 길어서 설명도 상당히 길어진다.     

스프링의 `Schedule`을 다루면서 느꼈던 것을 아주 간략한 방식으로 동료에게 설명한 적이 있는데 그 방식대로 해볼 생각이다.

```kotlin
fun main() {

    runBlocking {

        val ticker = ticker(100, mode = TickerMode.FIXED_DELAY)
        // val ticker = ticker(100, mode = TickerMode.FIXED_PERIOD)
        delay(150)                                              (1)
        println(withTimeoutOrNull(50) { ticker.receive() })     (2)
        println(withTimeoutOrNull(50) { ticker.receive() })     (3)
    }

}
```
아주 심플하게 일단 티커 채널을 생성할 때 `delayMillis`를 100밀리초로 줬다.   

여기서 먼저 딜레이를 150밀리초를 주고 다음 코드 (2)가 실행될 때는 어떤 모드이든 간에 무조건 `kotlin.Unit`값이 찍힐 것이다.

이유는 티커 채널이 생성되고 100밀리초만큼 지연된 시간이후 채널로 전송이 되었을 텐데 그 중간에 (1)이 주어졌기 때문이다.

하지만 모드가 `TickerMode.FIXED_DELAY`라면 (3)에서 달라진다.    

위에서 설명한것처럼 지정한 지연 시간 간격으로 원소를 그냥 송신하기 때문이다.     

이런 특징으로 (2)을 수신하고 나서 다음 (3)에서도 100밀리초 이후에나 수신을 받을 것이다.

하지만 `withTimeoutOrNull`함수에서 50밀리초만 주었기 때문에 타임아웃에 의해 `null`이 찍힌다.

그렇다면 모드를 `TickerMode.FIXED_PERIOD`로 바꿔서 실행해보자.

일단 (2)까지는 똑같다. 

하지만 이 모드에 의해서 티커는 150밀리초라는 시간이 지연된 것을 알 수 있다.

100밀리초 단위를 하나의 주기로 볼때 이 모드 입장에서는 50밀리초만큼의 시간이 딜레이된 것을 파악한다. 

원래대로라면 (2)이후 (3)에서는 100밀리초이후에 수신을 받아야 한다.

하지만 이 모드의 특징떄문에 다음 송신을 할 때는 이 지연 시간을 고려해서 50밀리초만에 보내게 된다.     

그래서 (3)은 `null`이 아니라 `kotlin.Unit`가 찍힌다.

이런 방식으로 모드에 따라서 위와 같은 방식으로 여러번 호출하게 될 경우에 동작 방식이 달라진다.     

## Mutex

액터를 배우기 전에 `Mutex`를 먼저 알아보자.     

`Semaphore`와 `Mutex`라는 개념은 우리가 지금까지 자바를 하면서 이미 배운 방법들이다.

예를 들면 `Semaphore`의 경우에는 `java.util.concurrent`패키지의 `Semaphore`를 활용할 수 있다.

`Mutex`는 우리가 흔히 말하는 객체를 통해 `syncronized`키워드로 락을 걸고 해제하는 방식이다.

그외에도 `Spin Lock`도 존재한다.

일단 아래 코드를 한번 보자.

```kotlin
var counter = 0

suspend fun increase() = coroutineScope{
    (1..100).forEach { _ ->
        launch(Dispatchers.Default) {
            counter++
        }
    }
}

fun main() {
    runBlocking {
        increase()
    }
    println("counter is $counter")

}
```
하나의 전역 변수에 접근해서 1씩 증가시키는 코드이다.

`launch`에서 디스패처 설정을 `Dispatchers.Default`로 뒀기 때문에 2개 이상의 스레드 풀에서 코드가 실행될 것이다.

잘 아시는 분들은 최종 `counter`의 값이 100일수도 있고 아닐수도 있다는 것을 알 수 있다.     

동시성 문제로 원자성을 보장하지 못하기 때문에 때로는 동시에 같은 값을 보고 증가시키는 경우도 생길 수 있기 때문이다.

어째든 이런 문제를 `Mutex`로 한번 처리해 보자.

원래 `Mutex`는 `Boolean`값이나 객체를 값으로 마치 `syncronized`같은 방식으로 락을 걸고 해제하는 코드 형식이지만 심플하게 `withLock`을 사용해 보자.

```kotlin
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

fun main() {
    runBlocking(Dispatchers.Default) {
        increase()
    }
    println("counter is $counter")
}
```
코드는 크게 바뀌지 않았다. `Mutex`객체를 하나 생성하고 락을 걸 부분을 `withLock`으로 감싸면 알아서 락을 걸고 완료가 되면 해제를 해준다.

위 코드를 보면 `counter++`이 실행될 떄 해당 자원을 락을 걸게 될텐데 총 루프를 100번 돌았으니 눈에 보이지 않더라도 100번의 락과 해제작업이 뒤에서 벌어졌을 것이다.

하지만 코틀린 공식 사이트에서는 이것은 `Thread confinement fine-grained (작은 단위의 스레드 한정)`에 속한다고 설명하고 있다.    

떄에 따라서는 이것이 유리한 경우도 있지만 어찌되었든 이런 `스레드 한정`은 `컨텍스트 전환`이 발생하기에 리소스가 든다는 것을 알 수 있다.     

## Actor

이 액터 모델의 역사는 꽤 긴걸로 알고 있다. 1974년 `Carl Hewitt`라는 분이 제안한 모델로 `Erlang`이 이런 액터 모델을 가지고 태어난 언어로 알고 있다. 

자바진영쪽에서는 가장 먼저 `scala`의 `Akka`를 먼저 떠올릴 것이다.      

그렇다면 액터가 무엇인지 궁금할 것이다. 

액터는 가변 상태를 `thread-safe`, 즉 스레드 안전하게 공유하는 방법을 제시한다.

액터 기반의 모델은 사실 사람과의 대화를 주고받는 방식을 상상하면 딱 어울린다.     

예를 들면 이 액터는 자신에게 들어오는 메세지가 오는지 듣고 그에 따라 메세지를 전달한다.    

이 액터 모델은 락 기반이 아니기 때문에 이런 동기화 관련한 여러 가지 문제로부터 자유롭다.

그렇다면 코드로 어떻게 구현되는지 한번 확인해 보자.

### 코루틴 빌더 actor()

코틀린에서는 `actor`라는 코루틴 빌더를 제공한다. 

```kotlin
public fun <E> CoroutineScope.actor(
    context: CoroutineContext = EmptyCoroutineContext,
    capacity: Int = 0, // todo: Maybe Channel.DEFAULT here?
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onCompletion: CompletionHandler? = null,
    block: suspend ActorScope<E>.() -> Unit
): SendChannel<E> {
    val newContext = newCoroutineContext(context)
    val channel = Channel<E>(capacity)
    val coroutine = if (start.isLazy)
        LazyActorCoroutine(newContext, channel, block) else
        ActorCoroutine(newContext, channel, active = true)
    if (onCompletion != null) coroutine.invokeOnCompletion(handler = onCompletion)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```
외형상으로는 `SendChannel`를 상속하고 있지만 내부적으로 `ActorCoroutine` -> `ActorScope`로 이어진다.

```kotlin
public interface ActorScope<E> : CoroutineScope, ReceiveChannel<E> {
```
이 `ActorScope`는 코드에서 볼 수 있듯이 `ReceiveChannel`를 갖고 있는 것을 알 수 있다.

따라서 이 액터는 내부적으로 `ReceiveChannel`이 존재한다고 보면 된다.

위에서 언급한 것처럼 이 액터는 자신에게 들어오는 메세지를 듣고 상태를 메세지로 보낸다고 했다.    

바로 이 형태를 그대로 따르는 것을 알 수 있다.

이제부터는 우리가 `Mutex`를 통해서 작성한 코드를 이 액터 모델을 활용해서 작업해 보자.

먼저 액터 모델은 `Mutex`와 비교하면 약간 손이 많이 간다.

일종의 `트레이드 오프`로 락을 걸면서 생기는 단점을 상쇄하지만 코드 작업이 필요하다.

### 시나리오
위에서 반복하면서 언급하듯이 액터는 자신에게 들어오는 메세지를 보고 그에 따라 반응하는 모델이다.    

따라서 액터의 행동을 정의해야 한다.    

```
1. 액터는 시나리오에 따라 counter를 증감시켜달라는 메세지를 받는다.
2. 그리고 그에 대한 결과값을 메세지를 통해서 준다.
```
아주 심플하다.

이제는 이것을 액터로 구현하자.

```kotlin
// Action객체의 메세지 타입을 정의한다.
enum class MessageType {
    // 증감 요청 명령
    COMMAND,
    // 결과 리턴 명령
    RESPONSE
}

class Action(
    // 메세지를 받고 결과값을 줄 때는 증강한 counter정보를 보내주기 때문에 Int타입이 된다.
    // 단지 messageType이 COMMAND라면 이 부분은 null일수 있다.
    val response: CompletableDeferred<Int> ?= null,
    val messageType: MessageType,
)


fun CoroutineScope.increaseCounterActor() = actor<Action> {
    var counter = 0
    for (action in channel) {
        when(action.messageType) {
            MessageType.COMMAND -> counter++
            MessageType.RESPONSE -> action.response?.complete(counter)
        }
    }
}
```
먼저 액터는 메세지를 주고 받기 때문에 이 메세지를 정의하는 객체를 하나 정의를 해야 한다.

이때 우리는 `CompletableDeferred`라는 타입의 프로퍼티를 사용할 수 있다.      

이 프로퍼티의 `complete`를 사용해서 받은 메세지에 대한 처리 결과를 돌려주게 된다.    

여기서 `Action`객체를 주고 받을 것이다. `messageType`을 통해서 액터는 자신의 액션을 정의할 것이다.

`CoroutineScope`객체에 확장 함수를 `increaseCounterActor`를 하나 정의했다.

내용은 채널로 들어온 `Action`을 상태값을 보고 증감을 할것인지 증감이후의 값을 결과값으로 보내줄 것인지 결정한다.

기존의 `increase`함수 역시 바꿔야 한다. 

우리가 정의한 `increaseCounterActor`안에 상태 변경 변수인 `counter`가 존재하는데 이 함수를 채널로 보내는 액션으로 바꿔야 한다.

```kotlin
suspend fun increase(action: suspend () -> Unit) = coroutineScope{
    (1..100).forEach { _ ->
        launch(Dispatchers.Default) {
            action()
        }
    }
}
```
`Channel`의 `send`함수는 `일시 중단 함수`이기 때문에 파라미터에 `suspend`가 붙어 있다.

최종 코드는 다음과 같다.

```kotlin
suspend fun increase(action: suspend () -> Unit) = coroutineScope{
    (1..100).forEach { _ ->
        launch(Dispatchers.Default) {
            action()
        }
    }
}

enum class MessageType {
    COMMAND,
    RESPONSE
}

class Action(
    // 메세지를 받고 결과값을 줄 때는 증강한 counter정보를 보내주기 때문에 Int타입이 된다.
    val response: CompletableDeferred<Int>? = null,
    val messageType: MessageType,
)


fun CoroutineScope.increaseCounterActor() = actor<Action> {
    var counter = 0
    for (action in channel) {
        when(action.messageType) {
            MessageType.COMMAND -> counter++
            MessageType.RESPONSE -> action.response?.complete(counter)
        }
    }
}

fun main() {
    runBlocking {
        val counterActor = increaseCounterActor()                                   (1)
        increase { counterActor.send(Action(messageType = MessageType.COMMAND))}    (2)

        val response = CompletableDeferred<Int>()                                   (3)
        counterActor.send(Action(response, MessageType.RESPONSE))                   (4)
        println("counter is ${response.await()}")                                   (5)
        counterActor.close()                                                        (6)
    }
}
```
코드를 보면 자바의 `CompletableFuture`와 상당히 유사한 것을 알 수 있다.    

(1) 액터를 생성하고 

(2) `increase`함수를 통해 100개의 카운터 증가 요청을 메세지로 보낸다.

(3) 미래의 결과를 담을 response을 생성해서 

(4) 액터에 요청을 한다.

(5) `await`를 통해 결과값을 기다리고 콘솔에 찍는다.

(6) 액터 채널을 닫는다.

### 코틀린 공식 홈페이지 예제 코드

위 코드는 `enum`을 활용해서 작성한 코드이다.      

공식 사이트에서는 다형성을 활용한 코드를 예제로 보여주고 있는데 이 부분도 살펴보자.

우리가 앞서 배운 `sealed class`는 클래스를 한정짓는 특징이 있기 때문에 `enum`처럼 활용할 수 있다.

따라서 위 코드는 다음과 같이 변경할 수 있다.

```kotlin
suspend fun increase(action: suspend () -> Unit) = coroutineScope{
    (1..100).forEach { _ ->
        launch(Dispatchers.Default) {
            action()
        }
    }
}

// sealed class를 만든다.
sealed class MessageType

// Command -> 단지 요청을 하는 객체로 actor내에서 스마트 캐스팅을 활용해 액션을 정하게 한다.
object Command : MessageType()

// 결과값을 주기 때문에 CompletableDeferred<Int>을 생성자로 받는다.
class Response(
    val response: CompletableDeferred<Int>,
): MessageType()

fun CoroutineScope.increaseCounterActor() = actor<MessageType> {
    var counter = 0
    for (msgType in channel) {
        when(msgType) {
            is Command -> counter++
            is Response -> msgType.response.complete(counter)
        }
    }
}

fun main() {
    runBlocking {
        val counterActor = increaseCounterActor()
        increase { counterActor.send(Command)}

        val response = CompletableDeferred<Int>()
        counterActor.send(Response(response))
        println("counter is ${response.await()}")
        counterActor.close()
    }
}
```
`enum`을 활용한 것보다는 다형성을 이용하기 때문에 확장면에서는 좀 더 유리할 수 있다.

여기서 사용된 `actor`빌더는 앞서 배웠던 `produce`빌더와 대응된다.     

이 두개는 모두 채널을 사용한다. 

하지만 액터는 데이터를 받기 위해서 채널을 사용하고 `produce`빌더는 데이터를 보내기 위해 채널을 생성하는 차이점이 있다.     

위에서 액터의 행동 패턴을 보면 `랑데뷰 채널`이라는 것을 알 수 있다.

물론 `actor`코드를 보면 `capacity`를 인자로 받기 때문에 다양한 행동 패턴을 정의할 수도 있다.

## Mutex Vs Actor
속도에 대한 궁금증이 있을 것이다.

`Mutex`가 루프만큼 락을 걸고 해제하는 작업때문에 느릴것 같아 보이지만 잘 생각해 보면 액터가 `Mutex`보다는 느릴 것이라는 것을 어느 정도 파악할 수 있다.

이유는 액터는 통신을 채널을 통해서 하고 있으며 하는 작업 역시 많아 보인다.    

실제로 다음과 같이

```kotlin
// 액터 increase
suspend fun increase(action: suspend () -> Unit) = coroutineScope{
    val time = measureTimeMillis {
        (1..100).forEach { _ ->
            launch(Dispatchers.Default) {
                action()
            }
        }
    }

    println("actor complete time is $time")
}

// mutex increase
suspend fun increase() = coroutineScope{
    val time = measureTimeMillis {
        (1..100).forEach { _ ->
            launch(Dispatchers.Default) {
                mutex.withLock {
                    counter++
                }
            }
        }
    }
    println("mutex complete time is $time")
}
```
처리 속도를 찍어보면 확실히 `Mutex`가 거의 압승이다.

다만 액터는 안정성과 디자인 모델이 가지는 명확성이 확실하다.     

결국 어떤 것을 어떤 상황에 사용할 것인지는 딱히 정해진 답은 없다.    

유연하게 사용하는 것이 가장 중요할 것이다.

## 좀 더 현실적인 액터 모델 구현해 보기

단순한 카운터 증감 예제로는 좀 부족하다.      

좀 더 현실적인 부분은 이 동시성 모델에서 자주 나오는 은행 업무 관련일 것이다.

아무래도 가장 중요하고 민감한 고객의 자산 문제이기 때문일 것이다.    

이 예제는 [코틀린 완벽 가이드]의 예제로 위 내용을 이해했다면 그다지 어려운 코드는 아니다.

여러분들이 이 코드를 보기 전에 한번 시나리오를 만들고 이 모델을 디자인해보는 것이 중요하다.     

책에서는 그냥 코드와 함께 설명을 하고 있는데 이것을 최대한 풀어서 이야기해보고자 한다.

```
1. 은행 계좌를 시나리오로 책정한다.
2. 계좌 관리는 3개의 상태를 갖는다.
   - 출금
   - 입금
   - 잔고 조회
```

가장 먼저 액터 모델을 디자인하기 위해서는 계좌의 상태를 정의한 클래스가 필요할 것이다.

```kotlin
// 계좌의 상태를 정의한다.
sealed class AccountStatus

// 입금 요청 객체
// 입금 요청만 하기 때문에 단지 입금액만 담고 
// 잔액 조회는 Balance객체를 통해 따로 할 것이다.
class Deposit(
    val amount: Long,
): AccountStatus()

// 출금 요청 객체
class Withdraw(
    val amount: Long,
    // 출금이 가능한지 체크해야 한다.
    // 전체 발란스보다 출금 금액이 더 크면 false를 보내줄 것이고 출금 가능하면 true를 담아낼 것이다.
    // 따라서 출금 성공여부 정보를 보내기 위해 설정한다.
    val isPermitted: CompletableDeferred<Boolean>,
): AccountStatus()

// 발란스 객체
class Balance(
    // 계좌의 전체 발란스를 응답으로 준다.
    val amount: CompletableDeferred<Long>,
): AccountStatus()
```
이제는 액터를 정의하자.     

앞서 카운터 증감 예제처럼 계좌를 관리하는 액터 모델이 될것이다.

```kotlin
fun CoroutineScope.accountManager(initialBalance: Long) = actor<AccountStatus> {
    var accountBalance: Long = initialBalance
    for(status in channel) {
        when (status) {
            // 계좌 발란스 조회
            // 액터가 가지고 있는 가변 상태 정보를 보내준다.
            is Balance -> status.amount.complete(accountBalance)

            // 입금 요청
            is Deposit -> accountBalance += status.amount

            // 출금 요청
            is Withdraw -> {
                // 출금 가능 체크
                // 당연히 잔액을 체크해야 하고 그에 따라 출금 성공 여부를 보내줄 수 있다.
                val isPermitted = accountBalance >= status.amount
                if(isPermitted) {
                    accountBalance -= status.amount
                }
                // 이 값을 통해서 출금이 성공적으로 이뤄졌는지 잔액 부족으로 출금이 거부되었는지 알려줄 것이다.
                status.isPermitted.complete(isPermitted)
            }
        }
    }
}
```

이것을 가지고 한번 생짜 코드로 짜보자.

```kotlin

fun main() {

    runBlocking {

        // 초기 잔액을 100만원으로 설정한다.
        val manager = accountManager(1_000_000)
        withContext(Dispatchers.Default) {
            repeat(10) {
                launch {
                    // 만원 입금
                    val depositAmount = 10_000L
                    println("depositAmount is $depositAmount")
                    manager.send(Deposit(depositAmount))
                }
            }
            repeat(100) {
                launch {
                    // 1만2천원 출금
                    val withdrawAmount = 12_000L
                    val responseWithdraw = CompletableDeferred<Boolean>()
                    manager.send(Withdraw(withdrawAmount, responseWithdraw))
                    println("출금 가능 여부 : ${responseWithdraw.await()}")
                }
            }
        }

        val responseBalance = CompletableDeferred<Long>()
        manager.send(Balance(responseBalance))
        println("최종 발란스 : ${responseBalance.await()}")
        manager.close()

    }

}
```
출력 순서는 다를 수 있겠지만 최종적으로 이 코드는 입금된 금액이 100,000만원이기 떄문에 최종 금액은 1,100,000만원일 것이다.     

그중에 출금을 1만2천원씩 100번을 할 텐데 계산상으로 91번째 루프를 돌 때 1,092,000원이 되는 시점 이후에는 출금이 불가능하게 될 것이다.

따라서 출금 불가 값이 떨어질 것이고 최종적으로 잔고는 8,000원을 찍게 되어 있다.

이제 이 코드를 확장 함수를 활용해서 코드를 분리해 보자.

`actor`빌드는 위에서 코드 형태를 보았듯이 `SendChannel`를 상속하고 있다.    

따라서 입금/출금/조회 채널을 따로 분리해서 작성을 해보자.

```kotlin
suspend fun SendChannel<AccountStatus>.deposit(amount: Long) {
    // 입금할 금액을 받아서 액터 채널을 통해서 메세지를 보낸다.
    // 기억을 상기시키고자....
    // 확장함수에서 그 자신은 this -> SendChannel이 된다.
    // 이 this는 생략이 가능하다.
    //this.send(Deposit(amount))
    send(Deposit(amount))
    println("Deposit Amount is $amount")
}

suspend fun SendChannel<AccountStatus>.withdraw(amount: Long) {
    // 출금 성공 여부를 받기 위해 다음과 같이 설정한다.
    val status = CompletableDeferred<Boolean>().let {
        send(Withdraw(amount, it))
        if(it.await()) "OK" else "DENIED"
    }
    println("Withdraw Amount is $amount ($status)")
}

suspend fun SendChannel<AccountStatus>.balance() {
    // 밸런스 조회하기
    val balance = CompletableDeferred<Long>().let {
        send(Balance(it))
        it.await()
    }
    println("balance is $balance")
}
```
확장 함수를 통해 채널로 메세지를 보내는 코드이다.     

당연히 이 코드의 행위는 `send`이기 때문에 `일시 중단 함수`가 될 것이다.

최종적으로 

```kotlin
fun main() {

    runBlocking {

        // 잔액을 100만원으로 설정한다.
        val manager = accountManager(1_000_000)
        withContext(Dispatchers.Default) {
            repeat(10) {
                launch {
                    // 만원 입금
                    manager.deposit(10_000)
                }

            }
            repeat(100) {
                launch {
                    manager.withdraw(12_000)
                }
            }
        }
        manager.balance()
        manager.close()
    }

}
```
똑같은 결과를 볼 수 있을 것이다.

# At a Glance

지금까지 코루틴의 여러 빌드들과 채널을 활용한 방법들을 알아본 시간이었다.

실제로도 현업에서 자주 사용되는 부분인 만큼 알아두면 좋기 때문에 많은 연습을 통해 숙달하면 분명 많은 도움이 되리라 생각한다.


