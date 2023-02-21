# 자바의 동시성을 코틀린에서 사용해 보자.

떄론 코루틴 말고 자바의 동시성 제어를 통한 멀티 스레드 작업을 해야하는 경우도 생길 수 있다.   

코틀린에서는 이러한 자바의 스레드와 동기화 관련 작업을 좀 더 쉽게 다룰 수 있도록 API를 제공한다.

여기서는 우리가 자바에서 사용하던 스레드를 코틀린에서 어떻게 다루는지 확인하는 챕터가 될 것이다.

## thread
코틀린에서는 자바의 스레드를 코틀린의 철학에 맞게 좀 더 쉽게 사용할 수 있도록 `thread`함수를 제공한다.

자바의 경우에는 `Thread`객체를 상속하는 방법과 `Runnable`인터페이스를 구현하는 방식을 사용하게 된다.

물론 이 방식은 코틀린에서도 그대로 사용할 수 있다. 

다만 일반적으로 `Runnable`인터페이스를 구현 상속하는 방식을 좀 더 선호하는 경향이 있다.      

이유는 다중 상속을 허용하지 않기 때문에 제한적일수 밖에 없기 때문이다.    

먼저 가장 간단한 방법인 코틀린에서 제공하는 `thread`함수를 사용하기 전에 전통적인 자바의 방식을 한번 먼저 살펴보자.

### Thread 상속

가장 초간단 `Thread`객체 상속 예제이다.

```kotlin

fun main() {
    val myThread = MyThread(5)
    myThread.start()
}

class MyThread(
    private val loop: Int
): Thread() {

    override fun run() {
        repeat(loop) {
            println("MyThread-$it (thread name: $name)")
        }
    }
}
```
이 경우에는 `Thread`객체의 `run`함수를 오버라이딩해서 구현하게 된다.    

위 결과는 하나의 별도 스레드를 생성해서 5번의 루프를 돌기 때문에 결과는 다음과 같이 나오게 된다.

```
MyThread-0 (thread name: Thread-0)
MyThread-1 (thread name: Thread-0)
MyThread-2 (thread name: Thread-0)
MyThread-3 (thread name: Thread-0)
MyThread-4 (thread name: Thread-0)
```

다만 이 경우에는 다중 상속을 허용하지 않기 때문에 제한적이다.

### Runnable 인터페이스 상속 구현

`Runnable`인터페이스 상속 구현 역시 코드는 그게 다르지 않다.

다만 이것을 구현한 객체를 `Thread`의 생성자로 넘겨서 스레드를 생성해서 실행하는 방식의 차이가 보인다.


```kotlin
fun main() {
    val runnable = MyRunnable(5)
    val myRunnable = Thread(runnable)
    myRunnable.start()
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
```
결과는 똑같이 나온다.

물론 자바처럼 스레드의 네임을 따로 줄수 있는 것도 자바와 동일한 방식으로 진행된다.

### Like Java using Lambda
자바에서는 람다를 활용해서 스레드를 생성하는 방법이 있다.   

코틀린에서는 익명 함수와 람다 형식으로 구현할 수 있다.

```kotlin
fun main() {

    val loop = 5
    // 익명 함수 사용
    val anonymous = object: Thread() {
        override fun run() {
            repeat(loop) {
                println("anonymousThread-$it Thread Name (${currentThread().name}))")
            }
        }
    }
    anonymous.start()
    
    // 람다 사용
    val lambda = Thread {
        repeat(loop) {
            println("lambdaThread-$it Thread Name (${currentThread().name}))")
        }
    }
    lambda.start()
}
```
재활용이라는 관점을 제외하면 간단하게 스레드를 생성해서 사용할 수 있는 방법이다.

이 경우에는 일종의 헬퍼 함수를 생성할 수 있는 단초를 제공하게 된다.

예를 들면 위에 예제를 좀 더 쉽게 사용할 수 있도록 함수로 제공하고 필요한 정보만 넘겨준다면 어떨까?

```kotlin
fun main() {

    helpThread("basquiat") {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }.start()

}

/**
 * @param name -> thread name
 * @param run -> action block
 */
fun helpThread(name: String? = null, run: () -> Unit): Thread {
    val thread = object: Thread() {
        override fun run() {
            run()
        }
    }
    // name이 null이면 Thread.currentThread().name을 따르고 아니면 이걸로 세팅한다.
    name?.let { threadName -> thread.name = threadName }
    return thread
}
```
여기서 `helpThread`에 우선순위라든가, 데몬 여부등을 더 넘겨서 줄 수 도 있을 것이다.

만일 좀 더 간결하게 바로 해당 헬퍼 함수를 통해 생성하자 마자 스레드를 실행하고 싶다면 실행 여부도 넘겨서 실행할지 말지 결정할 수도 있을 것이다.    

일반적으로 자바에서 스레드를 이런 헬퍼 메서드를 통해서 공통적인 행위를 정의해서 사용하기도 하는데 코틀린에서도 똑같이 사용할 수 있다. 

### 더 간결한 thread()함수
코틀린에서 제공하는 `thread`함수는 바로 어떻게 보면 위에서 우리가 작업했던 헬퍼 함수와 상당히 유사하다.

```kotlin
public fun thread(
    start: Boolean = true,
    isDaemon: Boolean = false,
    contextClassLoader: ClassLoader? = null,
    name: String? = null,
    priority: Int = -1,
    block: () -> Unit
): Thread {
    val thread = object : Thread() {
        public override fun run() {
            block()
        }
    }
    if (isDaemon)
        thread.isDaemon = true
    if (priority > 0)
        thread.priority = priority
    if (name != null)
        thread.name = name
    if (contextClassLoader != null)
        thread.contextClassLoader = contextClassLoader
    if (start)
        thread.start()
    return thread
}
```
코틀린의 표준 라이브러리에서 제공하는 `thread`함수의 코드를 보면 알겠지만 결국 이 녀석은 헬퍼 함수와 똑같은 녀석이다.

```kotlin
fun main() {
    thread {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }
}
```
우리가 위에서 헬퍼 함수와 형식이 똑같다.     

`start`파라미터의 기본값이 `true`이기 때문에 해당 함수를 사용하고 실행하면 바로 스레드가 실행하게 된다.

만일 스레드를 생성하고 지연 실행을 하고자 한다면 이 변수를 `false`로 넘기면 된다.

```kotlin
fun main() {
    val lazyThread = thread(start = false) {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
        }
    }

    // do something

    lazyThread.start()
}
```

`isDaemon`은 우리가 코루틴을 배웠기 때문에 데몬 모드는 

*오마에오 모 신데이루*

그렇다면 이 특징이 그대로 적용되는지 코드로 확인해 보자.    

코드를 보면 이 파라미터의 기본값은 `false`이다.

```kotlin
fun main() {
    thread(name = "DaemonTest") {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
            Thread.sleep(200)
        }
    }

    Thread.sleep(800)
    println("main thread is end.....")
}
```
지금같이 데몬 모드가 아니라면 `Thread.sleep(800)`에 의해 800밀리초를 기다리고 메인 스레드가 종료가 된다.

하지만 스레드 실행시 200밀리초를 기다리는 총 5번의 루프가 돈다.  

결과는 다음과 같이

```
Thread-0 / Thread Name (DaemonTest))
Thread-1 / Thread Name (DaemonTest))
Thread-2 / Thread Name (DaemonTest))
Thread-3 / Thread Name (DaemonTest))
thread is end.....
Thread-4 / Thread Name (DaemonTest))
```
메인 스레드가 끝나더라도 스레드가 전부 실행된 것을 확인할 수 있다.       

하지만 `isDaemon`모드는 활성화하면

```kotlin
fun main() {
    thread(name = "DaemonTest", isDaemon = true) {
        repeat(5) {
            println("Thread-$it / Thread Name (${Thread.currentThread().name}))")
            Thread.sleep(200)
        }
    }

    Thread.sleep(800)
    println("main is end.....")
}
//result
// Thread-0 / Thread Name (DaemonTest))
// Thread-1 / Thread Name (DaemonTest))
// Thread-2 / Thread Name (DaemonTest))
// Thread-3 / Thread Name (DaemonTest))
// thread is end.....
```
800밀리초 이후 스레드가 종료되는 시점에 해당 스레드도 종료되기 때문에 마지막 로프를 돌지 못하고 종료가 된다.    

### join()

다음 아래 코드를 보자.

```kotlin
fun main() {

    val threadArray = mutableListOf<Thread>()

    repeat(5) {
        val thread = thread(name = "DaemonTest", start = false, isDaemon = true) {
            println("Thread Name (${Thread.currentThread().name}))")
        }
        thread.start()
        threadArray.add(thread)
    }

    threadArray.forEach {               // (1)
        try {
            it.join()
        } catch (e: Exception) {

        }
    }

    println("main thread is end.....")  // (2) 

}
```
`thread`함수를 통해 5개의 스레드를 생성하고 실행이후 해당 스레드를 리스트에 담는 코드이다.

이때 스레드를 데몬 모드로 설정하자.

만일 그 아래 (1) 코드가 없다면 어떻게 될까?

결과는 메인 스레드가 끝나버리면 너머지 스레드도 종료되기 때문에 스레드의 이름을 찍는 로그가 찍기도 전에 (2)로그만 남고 종료된다.

이 방식은 자바에서도 그대로 통용되는 코드이다.   

## timer()

코루틴에서 우리는 `ticker`를 배웠다. 

그중에 자바 타이머 관련 함수로 코틀린에서 `timer`함수를 제공한다.

```kotlin
public inline fun timer(name: String? = null, daemon: Boolean = false, startAt: Date, period: Long, crossinline action: TimerTask.() -> Unit): Timer {
    val timer = timer(name, daemon)
    timer.schedule(startAt, period, action)
    return timer
}
```
`thread`함수와 비슷하면서도 마치 스케쥴을 연상하는 코드이다.

가장 기본적인 사용법부터 알아보자.

```kotlin
fun main() {
    timer(name = "TIMER", period = 100) {
        println("Thread Name (${Thread.currentThread().name}))")
    }

    Thread.sleep(400)
    println("main thread is end...")
}
```
해당 코드는 400밀리초 이후 메인 스레드가 종료되더라도 100밀리초 간격으로 로그를 계속 실행시킨다.

당연히 `daemon`을 `true`로 주면 메인 스레드 종료시 같이 종료될 것이다.

`fixedRateTimer`라는 함수도 존재한다.

```kotlin

fun main() {
    var counter = 0
    fixedRateTimer(name = "TIMER", period = 1000, daemon = false) {
        //timer(name = "TIMER", period = 1000, daemon = false) {
        println("Thread Name (${Thread.currentThread().name}))")
        counter++
        if (counter % 3 == 0) {
            Thread.sleep(2100)
        }
    }
}
```
다음과 같은 코드를 한번 실행해 보자.

`fixedRateTimer`와 `timer`와의 차이점이 살짝 드러난다.

`ticker`에서 `TickerMode`에서 `FIXED_PERIOD`와 `FIXED_DELAY`모드에 따른 실행 결과의 변화를 배웠다.      

`fixedRateTimer`는 `FIXED_PERIOD`와 비슷한 개념이라고 보면 된다.

따라서 위 코드에서 보면 지연된 시간만큼 보상된 주기로 실행되기 때문에 로그가 2개가 빠른 간격으로 찍히는 것을 볼 수 있다. 

반면 `timer`는 `FIXED_DELAY`처럼 앞이 지연되더라도 그 지연된 시간을 기준으로 `period`에 정의한 밀리초간격으로 일정하게 실행된다.

## 동기화

자바에서 고수준의 API를 이용해서 만든 코드가 있다고 해보자.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static int counter = 0;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(1, 11).forEach( i -> {
            Runnable runnable = () -> {
                counter++;
                System.out.print(counter + ", ");
            };
            executorService.submit(runnable);
        });
        executorService.shutdown();
    }
}
```
실행하면 대부분 멀티 스레드로 인해 순서는 다를 수 있어도 최종적으로 하나의 자원인 `counter`변수 값이 10인 경우를 보게 될 것이다.

하지만 이 경우는 `Race Condition`이 발생한다.

동시에 몇 개의 스레드가 값은 변수의 값을 보게 되면 순서야 어찌되었든 10이 찍혀야 하지만 `Race Condition`으로 인해 간혹 10이라는 결과를 못 볼수 도 있다.

다음과 같은

```
1, 4, 3, 2, 1, 5, 7, 9, 6, 8,
```
간헐적으로 이런 결과가 나올 수도 있는데 1이 두번 찍힌 것을 볼 수 있다.      

`Race Condition`이 발생한 것이다.

여러분들이 이 코드를 실행하면 아주 간헐적으로 나타나기 때문에 몇 번의 테스트로 저런 현상을 보지 못했다 하더라도 이것은 잠재적인 버그를 발생시킬 수 있다.

여기서 우리는 이렇게 멀티 스레드 환경에서 하나의 자원을 공유할때 이러한 `Race Condition`이 발생할 수 있는 지점을 `Critical section`, 소위 `임계 영역`이라고 표현한다.

저 위의 코드에서의 `임계 영역`은 바로 `counter++`이라는 것을 알 수 있다.

거두절미하고 위 코드를 코틀린으로 변경한 다음 코드를 보자.

```kotlin
var counter = 0

fun main() {
    repeat(5) {
        thread(isDaemon = false) {
            counter++
            print("${counter}, ")
        }
    }
}
```
`Race Condition`이 발생하는 위 코드보다는 스레드 안전한 방식으로 코드로 바꾼다면 다음과 같이 

```kotlin
var counter = AtomicInteger(0)

fun main() {
    repeat(10) {
        thread(isDaemon = false) {
            counter.incrementAndGet()
            print("${counter.get()}, ")
        }
    }
}
```

[이펙티브 자바]에서 권장하는 `AtomicLong`이나 `AtomicInteger`같은 객체를 사용한다.

이 객체들은 락 없이도 스레드 안전하고 원자성까지도 지원하기 때문이다.

하지만 만일 이런 경우가 아니고 어떤 값을 더하는 로직을 작성한다고 생각해 보자.

```kotlin
fun main() {
    var addValue = 0
    repeat(11) {
        thread(isDaemon = false) {
            addValue += it
            print("${addValue}, ")
        }
    }
}
```

11번의 반복을 하겠지만 실질적으로 0부터 10까지의 덧셈일테니 최종적으로 55가 나와야 한다.    

하지만 다음과 같이

```
5, 5, 14, 5, 20, 27, 35, 44, 54, 9,
```
간헐적이지만 이렇게 55가 보장되지 않는 것을 확인할 수 있는데 위에서 설명한 동기화 문제가 발생하기 때문이다.

자바라면 몇 가지 방법이 존재한다.

`임계 영역`을 `synchronized`키워드를 통해 락을 거는 가장 간단한 방법과 앞서 배운 `Mutex`가 있다.

`Mutex`와 유사하지만 마치 `non-blocking`처럼 동작하게 하는 `Lock polling`즉, `Spin Lock`이 있다.

또한 구글신에 물어보면 항상 `Mutex`와 쌍으로 검색되는 `Semaphore`도 존재한다.

물론 `java.util.concurrent.locks`패키지에 있는 `ReentrantLock`같은 고수준의 API를 활용할 수 있다.

다만 `ReentrantLock`의 경우는 `syncronized`키워드보다 손이 좀 가는 코드이다.

암튼 여기서는 자바 시간이 아니기 때문에 코틀린에서 손쉽게 사용하는 몇가지 방법을 소개할 생각이다.

참고로 여러분들은 위에 언급한 방법들에 대해 잘 모르신다면 구글을 통해서 확실히 알고 넘어가길 바란다.

특히 취준생 또는 면접을 준비하시는 분들이라면 가끔 저런 것들을 물어보는 경우가 있기 때문에 한번씩 보고 차이점도 알면 더 좋다.

### synchronized 키워드

자바에서 이 키워드는 다양한 곳에 사용할 수 있다.     

예를 들면 메서드에 해당 키워드를 붙여서 메소드 자체를 `임계 영역`으로 둔다.     

하지만 이 방식은 편리하면서도 메소드에 사용하면 해당 객체 전체에 락을 걸어버린다.    

게다가 메소드가 정적 메소드냐 인스턴스 메소드냐에 따라서 또 달라지기도 한다.

따라서 메소드에 해당 키워드를 사용할 것인지 아니면 블록 단위로 `임계 영역`을 좁게 보고 해당 부분만 락을 걸 것인지는 상황에 따라 달라질 것이다. 

참고로 예전에 착각했던 부분으로 `synchronized`를 걸면 동기화가 적용되는 것이지 순서를 보장하지 않는다는 점을 잊지 말자.

코틀린에서는 함수에 사용할 떄와 블럭 단위, 즉 `임계 영역`에 사용할 때는 다르다.

일단 함수에 사용할때는 애노테이션을 사용해야 한다.

먼저 `임계 영역`에 락을 거는 코드를 살펴보자.

자바와 다를 바가 없다.

```kotlin
fun main() {
    var addValue = 0
    val lock = Any()
    repeat(11) {
        thread(isDaemon = false) {
            synchronized(lock) {
                addValue += it
                println(addValue)
            }
        }
    }
}
```
자바와 크게 다른 게 없다.

위 코드에서 스레드가 실행되는 상황에서 그 순간에 락을 걸어 `addValue`정보를 가져올 수 도 있다.

```kotlin
fun main() {
    var addValue = 0
    val lock = Any()
    repeat(11) {
        thread(isDaemon = false) {
            synchronized(lock) {
                addValue += it
            }
        }
    }
    val currentAddValue = synchronized(lock) { addValue }
    println(currentAddValue)
}
```
스레드가 실행되는 상황에서 중간에 껴들어 락을 걸고 그 때의 값을 가져오는 코드로 상황에 따라 다른 값들이 출력된다.

이제는 함수에 거는 방법도 알아보자.

```kotlin
fun main() {
    val addValue = AddValue()

    repeat(11) {
        thread(isDaemon = false) {
            addValue.add(it)
        }
    }
}

class AddValue {
    private var addValue = 0

    @Synchronized fun add(value: Int) {
        addValue += value
        print("${addValue}, ")
    }
}
```
자바와 똑같지만 이때는 애노테이션 `@Synchronized`을 사용한다는 차이점만 있다.

### ReentrantLock

앞서 `ReentrantLock`객체에 대해 잠깐 언급한 적이 있다.     

일반적으로 `synchronized`로 `임계 영역`을 감싸면 알아서 락을 걸고 해제하게 된다.     

`ReentrantLock`이 `synchronized`에 비해 `spin lock`같은 몇가지 고급 기능들을 제공하지만 락을 걸고 해제하는 것을 일일히 해줘야 한다.

코틀린에서는 확장 함수를 통해서 이것을 자동으로 해주는 `withLock`함수를 제공한다.

```kotlin
/**
 * Executes the given [action] under this lock.
 * @return the return value of the action.
 */
@kotlin.internal.InlineOnly
public inline fun <T> Lock.withLock(action: () -> T): T {
    contract { callsInPlace(action, InvocationKind.EXACTLY_ONCE) }
    lock()
    try {
        return action()
    } finally {
        unlock()
    }
}
```

위 코드를 한번 바꿔보자.

```kotlin
fun main() {
    val addValue = AddValue()

    repeat(11) {
        thread(isDaemon = false) {
            addValue.add(it)
        }
    }
}

class AddValue {
    private var addValue = 0
    private val lock = ReentrantLock(true)

    fun add(value: Int) {
        lock.withLock {
            addValue += value
            print("${addValue}, ")
        }
    }
}
```
일반적으로 락을 획득하는 것도 어떻게 보면 경쟁일 것이다.    

여러 개의 스레드중 획득하기 위해서 오래 기다린 스레드도 락을 걸어 공평하게 가져갈 수 있도록 `fair`라는 인자값을 줄 수 있다.

기본은 `false`로 되어 있기 때문에 이런 방식을 사용하고 자 한다면 `true`로 주면 된다.

공정하게 스레드가 가져갈 수록 있도록 하면 좋은거 아닌가? 근데 왜 기본값이 `false`일까라는 생각이 들것이다.

단 곰곰히 생각해 보면 공정한 기회를 준다는 것은 이것을 위해서 무언가 처리를 한다는 의미로 받아드릴 수 있다.

왜냐하면 어떤 스레드가 오래 기다렸는지 처리를 해야 할테니 말이다.

성능보다는 공정하게 처리하는 것이 우선 순위인지 아니면 그게 아닌지 판단하고 사용하면 될것이다.    

### 짚고 넘어갈 부분

자바의 최상위 ~~포식..~~ 객체인 `Object`를 따라가면 `wait`나 `notify`같은 동기화 관련 함수를 제공한다.

하지만 코틀린의 `Any`는 그런거 없다.

딸랑 `equals`, `hashCode`, `toString`뿐이다.    

사실 자바의 `wait`나 `notify`같은 함수를 사용할 일이 있을까만은....

```kotlin
(obj as Object).wait()
```
같이 사용할 수 있다고 한다.

# At a Glance
코틀린에서 제공하는 스레드 관련 표준 라이브러리를 활용해 자바의 스레드 방식의 코드 작성에 대해서도 알아 봤다.

자바에서 사용하는 다양한 동기화 방식중 몇가지만 소개했다.

하지만 만일 이런 부분을 잘 모른다면 한번 구글신에게 물어봐서 하나씩 알아가는게 중요하다.