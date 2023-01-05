# 동시성

아마도 대부분, 어떤 언어이든간에 이 동시성이라는 주제는 실제 애플리케이션에서 가장 중요한 부분일 것이다.     

특히 지금같이 대용량 트래픽 - 그넘의 대용량 - 시대에는 이 동시성 이슈는 언제나 개발자들과는 떨어질 수 없는 관계이다.     

처음 막 IT에 입문할 때에는 이런 동시성, 멀티 스레드 관련 고민을 하지 않았을 시절에 - 사실은 잘 몰랐으니까 - 어떤 면접에서 받았던 질문은 그 때의 나에게는 충격적이었다.

```
당신은 멀티 스레드를 고려한 개발을 하고 있나요?
```

사실 모르기 때문에 한번도 생각해 본적이 없기 때문이다. 

그냥 당시 struts랑 스프링이 혼재하던 시대에 실제로 했던 생각은 딱 이거 뿐이었다.

```
아니 내가 멀티 스레드를 고려하면서 개발해야하는건가??

struts랑 spring framework 잘 알고 요구 사항에 맞춰서 DB에서 데이터 잘 가져오고 하면 끝 아냐?

이런 프레임워크들이 알아서 해주는거 아닌가???
```
이 동시성과 관련해서 코틀린에는 자바와는 다른 몇 가지 독특한 특징들이 있다.      

그 중에 여러분들이 스칼라의 `Play Framework`를 접해보셨다면 `actor`모델을 기반으로 하는 `akka`를 사용했을 수도 있다.          

코틀린에도 이 액터에 대한 주제들이 있기 때문에 이 부분도 살펴보는 시간이 될것이다.

물론 자바에도 이 `actor`모델 기반의 프로그래밍 기법이 존재한다.      

하지만 생각보다 어렵고 당시에 언어를 두루 잘하셨던 선배분은 '그런 기반의 병렬 프로그래밍을 주로 할꺼면 차라리 스칼라하지 그래?'라는 답변을 받았을 정도다.

# 시작하기 전에

`Gopher`들에게는 익숙한 `고루틴`처럼 코틀린에는 `코루틴`이 있다.

이것을 사용하기 위해서는 코루틴 라이브러리를 추가해야 한다.

```
- maven

<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-core</artifactId>
    <version>1.6.4</version>
    <type>pom</type>
</dependency>

- gradle

implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
```
2023년 1월 9일 작성되는 시점의 최신 버전은 `1.6.4`버전이다.

만일 그냥 심플한 코틀린 프로젝트라면 인텔리제이에서 해당 라이브러리를 프로젝트 모듈에 추가할 수 있으니 이 때는 IDE의 도움을 받도록 하자.

코루틴은 또한 할 이야기가 많기 때문에 두 개의 파트로 나눠서 최대한 펼쳐서 설명하고자 한다.     

사실 공식 사이트의 내용이 중요한 내용을 다루긴 해도 생각보다 부족하기 때문에 여러 책의 내용들을 종합해서 다루고자 한다.        

따라서 모든 코드는 한번씩 실행해 보고 결과를 눈으로 직접 확인해 봐야한다.      

영어를 나름대로 해석해서 잘못 전달되는 경우도 있을 수 있다. 

또는 내 자신이 우매해서 잘못 이해하고 잘못 설명한 부분도 있을 수 있다.

그럴때는 [메인 README](https://github.com/basquiat78/kotlin-basic-for-you)에 기재된 메일로 신고좀 부탁드려요~

# 자바의 멀티 스레드

기본적으로 자바에서 멀티 스레드를 고려한 전통적인 방식은 대부분 블럭킹 연산을 통한 제어일 것이다.    

우리가 흔히 아는 `Thread`객체를 이용한 `sleep`, `join`과 `Object`객체를 보면 존재하는 동시성 제어를 위해 존재하는 `notify`나 `wait`를 사용하게 된다.

예전에는 `stop`도 있었는데 [이펙티브 자바]에도 언급하기도 하는 내용으로 `deprecated API`이다.

게다가 이 책에서는 `ThreadPoolExecutor`나 `CountDownLatch`와 `Semaphore`를 언급한다.

세마포어의 경우에는 뮤텍스와 쌍으로 언급되는 내용이니 구글링을 한번 해보는 것도 좋다.

물론 당연한 이야기지만 코틀린에서도 이 뮤텍스도 다룬다.       

자바 1.5에서 추가된 `java.util.concurrent`패키지를 통해서 고수준에서 사용하게 되면 `notify`나 `wait`를 쓸 이유가 거의 없다고 말한다.     

게다가 멀티 스레드 관련 고민해야하는 `문맥 전환 (Context Switch)`에 대한 리소스 비용도 고려해야 한다.

또한 멀티 스레드 코딩 스타일은 상당히 하드한 경향이 있다. 

이런 이유로 어려움때문에 처음 입문하시는 분들이 `스레드`부분을 배울 때 일단 이 부분을 나중에 보고자 건너띄는 경우들이 많을 것이다. 그리곤 안보귀??!?

~~나의 경우에는 이와 관련 이슈에 처맞기 전까지는 그랬다....~~

어째든 그래서 가장 효율적인 접근법으로 비동기 프로그래밍을 언급한다.       

자바 진영에서는 이와 관련 `RxJava` 또는 `Vert.x`그리고 스프링 진영에서는 이와 관련 `WebFlux`같은 것이 등장한 배경이 아닌가 싶다.     

하지만 만일 여러분들이 `RxJava`와 `WebFlux`을 처음 접하신다면 진입장벽이 생각보다 높다는 것을 알 수 있을 것이다.       

코루틴이 인기를 얻은 이유는 아무래도 이 진입 장벽이 이와는 달리 낮기 때문일것이다. 

그렇다고 생각만큼 낮은 것은 아니다. 

결국 치트키는 없다!

그래서 개념 자체를 이해해가면서 알아가는 `스텝 바이 스텝`이 정말 중요하다.

# 비동기 프로그래밍

구글에서 코틀린의 코루틴과 관련 2019년 `Dream Code`라는 주제로 재미있는 영상이 하나 있었다.     

[Understand Kotlin Coroutines on Android (Google I/O'19)](https://www.youtube.com/watch?v=BOHK_w09pVA)

이 동영상이 코루틴을 더 유명하게 만든 걸로 기억하는데 안드로이드 관련 영상이긴 하지만 안드로이드에만 국한된건 아니다.      

자바스크립트에서는 `async`, `await`를 통해 이것을 해결하고 있지만 우리가 흔히 알고 있는 비동기 프로그래밍에서 만나는 현상이나 스트릿트 파이터의 `류`가 장풍을 날리는 이미지로 표현한 `callback hell`관련 내용도 언급하기도 한다.

적어도 표현만 다를 뿐이지 이런 개념이 여러분들에게 새로운 것은 아니라고 말하고 싶다. 

그리고 이미 여러분들은 이런 것들을 경험을 하고 있는 것들이다.

# 코루틴 (coroutine)

[테스트코드](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/coroutine/kotlin)

코틀린의 코루틴은 컴파일러가 효율적인 비동기 코드로 자동 변환해주는 강력한 매커니즘을 가지고 있다.     

스레드를 다루는 것은 많은 리소스가 필요하다. 

이미 잘 아시다시피 그 중에 가장 큰 것은 바로 `Context Switching`, 즉 문맥 전환이 벌어질 때 많은 리소스가 든다.    

하지만 코루틴은 이런 문맥 전환을 사용하지 않아서 가볍고 그로 인해 엄청난 수의 코루틴을 동시에 실행이 가능해진다.    

우선 그 중에 가장 먼저 등장하는 `일시 중단 함수`를 알아보자.

## 일시 중단 함수

코루틴에서 가장 기본 요소는 `일시 중단 함수`이다.    

이것을 이해하기 위해서는 다음과 같은 시나리오를 한번 생각해 보자.

자바스크립트로 어떤 API를 ajax로 호출하는 일종의 `pseudo code` 함수가 있다고 생각해 보자.

```javascript
function ajax1() {
   // ajax do something
}

function ajax2(value) {
   // ajax do something
}

function ajax3(value) {
   // ajax do something
}


function total() {
   let result1 = ajax1()           // (1)
   let result2 = ajax2(result1)    // (2)
   let result3 = ajax2(result2)    // (3)
   return result3
}

```
만일 `total()`함수를 실행하면 아마 실패할 확률이 크다.       

왜냐하면 (1)의 결과를 (2)의 함수의 파라미터로 그리고 이 결과를 또 (3)의 함수의 파라미터로 넘기는 방식인데 비동기로 동작하는 함수들이 결과를 얻기도 전에 `total`함수는 종료할 것이다.

물론 정말 운이 좋아서 엄청 빠른 속도로 결과를 받아오면 성공할 지도 모른다.

하지만 어째든 초기에는 `Promise`와 `then`을 이용한 콜백을 이용하다가 콜백헬을 피하고 모던한 방식인 `async`, `await`키워드를 통해 이것을 좀 더 간결하게 제어한다.

이 내용이 위에 링크를 건 2019년에 발표한 `Dream Code`관련 영상에서도 설명하는 부분이기도 하다.

```javascript

async function ajax1() {
   // ajax do something
}

async function ajax2(value) {
   // ajax do something
}

async function ajax3(value) {
   // ajax do something
}

async function total() { (0)
   
   /*
   예를 들자면 이런 callback을 이용한 코드를 짜고 싶을까??
   let result3 = promise1.then((ajax1) => {
      promise2.then((ajax2) =>  {
         promise3.then((ajax3) =>  {
            promise4.then((ajax4) =>  {
               promise5.then((ajax5) =>  {
                  promise6.then((ajax6) =>  {
                     // do something
                  })
               })
            })
         })
      })
   })
   
   */

   let result1 = await ajax1()           // (1)
   let result2 = await ajax2(result1)    // (2)
   let result3 = await ajax2(result2)    // (3)
   return result3
}
```
최초 (0)에서 메인 스레드가 진행이 되다가 (1)을 만나게 되면 (1)의 결과가 올 때까지 (0)의 메인 스레드를 잠시 멈추고 (1)이 메인 스레드를 점유하게 된다.   

그리고 (1)의 결과를 받고 작업이 완료되면 잠시 멈춰 있던 스레드 (0)이 다시 재개된다.

(2), (3)에서도 이렇게 진행되고 최종적으로 모든 작업이 완료된다.         

하지만 함수에 붙는 키워드 `async`와 해당 비동기 함수를 호출할 때의 키워드인 `await`가 눈에 들어올 것이다.     

이런 키워드들 특히 `await`는 일반 함수에서 사용하게 되면 문법적으로 오류를 낸다.      

근데 코루틴의 기본적인 `delay`라는 함수가 존재한다.

대표적인 `일시 중단 함수`로 다음과 같이 생겨먹은 녀석이다.

```kotlin
public suspend fun delay(timeMillis: Long) {
    if (timeMillis <= 0) return // don't delay
    return suspendCancellableCoroutine sc@ { cont: CancellableContinuation<Unit> ->
        // if timeMillis == Long.MAX_VALUE then just wait forever like awaitCancellation, don't schedule.
        if (timeMillis < Long.MAX_VALUE) {
            cont.context.delay.scheduleResumeAfterDelay(timeMillis, cont)
        }
    }
}
```
`suspend`라는 키워드가 변경자로 붙어 있는 것을 볼 수 있는데 그 의미를 생각해보면 의미심장한 느낌이다.

여기서 `일시 중단 함수`는 일시 중단 함수와 일반 함수를 호출할 수 있지만 일반 함수는 `일시 중단 함수`를 호출하는 것을 금지하고 있다.

자바스크립트의 `async`, `await`와 비슷한 느낌을 준다.

```kotlin
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
```
`main`함수에 붙은 `suspend`변경자에 주목하자.      

해당 키워드를 지우면 `일시 중단 함수`인 `delay`를 호출하는 `startAndEnd`때문에 해당 키워드를 추가하라고 여러분의 `IDE`가 친절하게 설명해 준다.

위의 언급한 `일시 중단 함수`의 특징이 위 코드에서 그대로 드러난다.         

이 코드는 어떻게 보면 `Thread.sleep`과 비슷하다.

```java
public class Main {

    public static void start() {
        System.out.println("시작!!!!!");
    }

    public static void end() {
        System.out.println("끝!!!!!");
    }

    public static void main(String[] args) {
        try {
            start();
            Thread.sleep(1000);
            end();
        } catch (InterruptedException ie) {
            // do Something
        }
    }
}
```
그러나 차이점이 있다.

그것은 `Thread.sleep`은 스레드를 블럭킹하지만 `delay`는 스레드를 블럭킹하지 않고 호출한 함수를 말 그대로 `일시 중단`시킨다.      

그리고 다른 `일시 중단`된 함수를 다시 계속 실행할 수 있도록 풀어주거나 하는 작업등을 수월하게 할 수 있도록 해준다.

하지만 위 예제 코드를 보면 의문이 들것이다. 

일반 함수에서는 이런 `일시 중단 함수`를 호출할 수 없을까?       

실제로 운영되는 애플리케이션들은 이러한 동시성 코드의 동작을 제어할 필요가 있다.     

그래서 공통적인 생명 주기와 문맥이 정해진 몇몇 작업이 정의된 어떤 특정 영역에서만 동시성 함수를 호출하게 된다.

생각해보자. 

이런 `일시 중단 함수`를 호출하기 위해서는 `suspend`변경자가 붙어야 하는데 아마도 해당 애플리케이션의 함수들은 `suspend`변경자로 정복당 할수도 있을 것이다.        

그래서 코틀린에서는 이러한 특정 영역을 만들어주는 코루틴 빌더를 제공한다.

# Coroutine Builder

`CoroutineScope`라는 인터페이스를 확장한 가장 기본적인 객체는 `GlobalScope`가 있다.     

이 객체는 독립적인 코루틴을 만들 수 있게 도와주고 또한 자신의 작업을 내포할 수 있다.      

이것을 이용해서 자주 사용하는 대표적인 코루틴 빌더를 하나씩 보자.

## launch()

이 함수의 모양은 다음과 같다.

```kotlin
public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val newContext = newCoroutineContext(context)
    val coroutine = if (start.isLazy)
        LazyStandaloneCoroutine(newContext, block) else
        StandaloneCoroutine(newContext, active = true)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```
일단 뭔지 모르겠지만 반환 객체가 `Job`이다.      

이 객체는 실행중인 작업의 상태를 추적하고 변경할 수 있는 객체이다.

그리고 이 함수가 받는 인자중 `block: suspend CoroutineScope.() -> Unit`를 보자.      

람다를 받지만 `suspend`키워드을 주목하자. 

~~일시 중단 람다라고 불러야 할까??~~     

일단 코드부터 보는게 가장 빠르다.

```kotlin
fun main() {

    val time = currentTimeMillis()

    GlobalScope.launch {
        delay(100)
        println("My Launch One finish!!time millis : [$time]")
    }

    GlobalScope.launch {
        delay(100)
        println("My Launch Two finish!! time millis : [$time]")
    }
}
```
이 코드에서 `main`함수에 `suspend`키워드가 없는 것도 주목해보자.       

이 영역 안에서 이런 `일시 중단 함수`를 일반 함수에서 호출할 수 있도록 도와준다는 것을 알 수 있다.

하지만 이 예제 코드를 실행하면 콘솔에 어떤 내용도 찍히지 않는다. 

왜 그럴까? 그 답은 이미 자바스크립트에서 첫 번째 예제 코드의 `total`함수를 통해 이미 설명한 것이다.      

코틀린의 `main`함수가 실행될 때 두 개의 작업이 실행되기 전에 `main`함수의 스레드가 이미 끝나버리기 때문이다.

따라서 일단 메인 스레드를 두 개의 코루틴 스레드가 완료될 수 있도록 한번 `Thread.sleep`으로 스레드를 잠시 중단시켜서 두 개의 작업이 완료되도록 해보자.

```kotlin
fun main() {

    val time = currentTimeMillis()

    GlobalScope.launch {
        delay(100)
        println("My Launch One time millis : [$time]")
    }

    GlobalScope.launch {
        delay(100)
        // 가능하지만 비검사 경고가 뜬다.
        //Thread.sleep(100)
        println("My Launch Two time millis : [$time]")
    }

    Thread.sleep(500)

}
```
여기서 우리가 짚고 넘어갈 내용은 코루틴을 처리하는 스레드는 일종의 `데몬(Daemon)`처럼 실행된다.       

그래서 `Thread.sleep(500)`가 없었을 때는 코루틴 스레드가 실행되기 전에 `main`함수의 스레드가 종료되면서 같이 자동으로 종료된 것을 확인했다.       

여기서 우리는 또 하나 위에서 언급했던 `delay`가 `Thread.sleep`이랑 비슷하지만 블럭킹을 하느냐 마느냐의 차이가 있다고 언급했다.

실제로 `launch`로 실행한 코루틴내에서 저 위 코드에서 `delay`대신 `Thread.sleep`로 작업해도 코드는 작동하지만 `IDE`에서 비검사 경고를 하나 보여준다.

```
Possibly blocking call in non-blocking context could lead to thread starvation 
Replace this "Thread.sleep()" call with "delay()".
```
`WebFlux`를 해봤거나 비동기 프로그래밍이 익숙한 분들이라면 이 경고의 의미가 어떤지 알 수 있다.   

`thread starvation`, 다른 스레드들이 CPU 시간을 독점하거나 하는 행위로 인해 잠재적인 오류와 메모리 누수를 불러일으킬 수 있기 때문이다.           

그래서 `WebFlux`의 경우에는 블록킹 코드가 있는지 검사하는 `BlockHound`같은 플러그인을 활용하기도 한다.       

어째든 저 코드는 거의 동시에 실행되서 끝나는 것을 알 수 있다. 물론 순서는 보장되지 않는것도 확인할 수 있다.      

자바에서는 우선 순위, 즉 `priority`설정을 통해 어떤 스레드를 먼저 실행할지 결정할 수 있듯이 코루틴에서도 비슷한 것을 지원한다.

코드를 보면 알겠지만 `launch`빌더는 `block: suspend CoroutineScope.() -> Unit`처럼 결과를 반환하지 않는 람다를 받는다.     

그런데 코루틴의 실행 결과를 받아야 하는 상황은 분명 존재한다. 

그래서 제공하는 코루틴 빌더가 존재한다.

## async()

이것은 자바스크립트의 `async`, `await`와 거의 흡사하다.      

코틀린에서는 `async`라는 코루틴 빌더를 제공하는데 모양새는 다음과 같다.

```kotlin
public fun <T> CoroutineScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    val newContext = newCoroutineContext(context)
    val coroutine = if (start.isLazy)
        LazyDeferredCoroutine(newContext, block) else
        DeferredCoroutine<T>(newContext, active = true)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```
`block: suspend CoroutineScope.() -> T`부분에 주목해 보자.

그리고 `launch`와는 다르게 `Deffered`라는 객체를 반환하는 것을 확인할 수 있다.      

여기서 `Deffered`를 따라가면 결국 이 넘은 `Job`의 하위 타입인것을 확인할 수 있다.

일단 코드로 살펴보자.

```kotlin
suspend fun main() {

    val hello = GlobalScope.async {
        delay(100)
        "안녕~"
    }

    val message = GlobalScope.async {
        delay(100)
        "반가워!"
    }

    val result = "${hello.await()} ${message.await()}"

    println(result)

}
//안녕~ 반가워!
```
자바의 `Future`랑 비슷하다. 자바8이후의 모던 자바에서는 `CompletableFuture`를 통해 비동기 메서드를 만들 수 있다.

그리고 이것을 좀 더 간결하게 사용할 수 있도록 `supplyAsync`를 제공하기도 한다.     

`launch`와 `async`는 어쨰든 백그라운드 스레드를 공유하는 `Pool`를 통해 작업을 실행한다.     

앞서 살펴 본 `launch`를 다시 상기시켜보자.        

두 개의 코루틴 스레드가 작업 완료하도록 우리는 블럭킹 코드 `Thread.sleep`을 통해 제어를 했다. 

## runBlocking()

하지만 이것을 `runBlocking()`함수를 통해 제어할 수 있다.

다음 코드를 한번 살펴보자.

```kotlin
fun main() {

    GlobalScope.launch { // (1)
        delay(100)
        println("Launch Thread1 : ${Thread.currentThread().name}")
    }

    runBlocking {        // (2)
        println("RunBlocking Thread : ${Thread.currentThread().name}")
        delay(500)
    }

    println("End")      // (3)

}
//result
//Start
//RunBlocking Thread : main
//Launch Thread1 : DefaultDispatcher-worker-2
//End
```
근데 결과는 흥미롭다.      

`launch`로 시작하는 코루틴은 공유 풀에서 백그라운드 스레드로 할당받은 반면, `runBlocking`은 메인 스레드에서 실행한 것을 볼 수 있다.    

메인 스레드에서 (2)이 실행되고 `delay`로 스레드를 일시 중단했다.

그 사이에 (1) 작업이 완료되고 나서 마지막 (3)이 찍히는 것을 볼 수 있게 된다.

사실 `runBlocking`은 blocking/non-blocking 호출 사이의 다리 역할을 하기 위해 만들어진 코루틴 빌더이다.    

따라서 다른 코루틴 안에서 사용하는 것을 금지한다. 즉 메인 함수같은 곳에서 최상위 코루틴 빌더로 사용하는 경우에만 사용해야한다.

위 코드는 아래와 같이 사용할 수 있다.

```kotlin
fun main() {
    runBlocking {
        GlobalScope.launch {
            delay(100)
            println("Launch Thread1 : ${Thread.currentThread().name}")
        }

       GlobalScope.launch {
          delay(100)
          println("Launch Thread2 : ${Thread.currentThread().name}")
       }

        println("RunBlocking Thread : ${Thread.currentThread().name}")
        delay(500)
    }
    println("End")
}
```

## Structured Concurrency

일명 `구조적 동시성`이라고 해석할 수 있다. 

사실 이 내용은 코틀린에만 국한된 내용은 아니다. 

동시성과 관련된 개념적인 내용이다.

어째든 코틀린 공식 사이트의 [Coroutines basics]에 이와 관련 내용이 있다.   

불친절하게도 내용 자체가 사실 짧막해서 이게 정확히 어떤 의미를 가지고 있는지 명확하지 않다.

이전 우리가 `runBlocking`코루틴 빌더 안에 `launch`로 새로운 코루틴을 생성한 수정된 코드를 가만히 살펴보자.     

여기서 사용된 `launch`코루틴 빌더는 `GlobalScope`의 함수이다.      

말 그대로 해당 코루틴은 전역에서 실행된다는 것을 알 수 있다.      

전역 스코프이기 때문에 이 코루틴의 생명 주기는 전체 애플리케이션의 생명 주기의 의해서 제약되는 영역이다.       

말이 어려운데 그냥 쉽게 말하면 해당 코루틴은 탑 레벨, 최상위 레벨의 코루틴을 생성하는 방식이다.    

탑 레벨에서 코루틴을 생성하는 것이 문제가 되는 것은 많은 메모리를 사용하게 된다는 단점이 있다.      

그리서 일종의 트리 구조를 통해 `부모-자식`의 관계로 가져가는 것을 권장한다.      

따라서 위 코드는 전역 스코프의 `launch`가 아닌 `CoroutineScope`의 코루틴 빌더를 사용하면

```kotlin
fun main() {
    runBlocking {
        launch {
            delay(100)
            println("Launch Thread1 : ${Thread.currentThread().name}")
        }

        launch {
            delay(100)
            println("Launch Thread2 : ${Thread.currentThread().name}")
        }
       
        println("RunBlocking Thread : ${Thread.currentThread().name}")
    }
    println("End")
}
//RunBlocking Thread : main
//Launch Thread1 : main
//Launch Thread2 : main
//End
```
여기서 주목해 볼 것은 `runBlocking`로 생성한 코루틴이 `launch`가 생성한 코루틴이 작업 완료하기까지 `delay`를 걸지 않았다는 것에 주목하자.

이유는 `GloblaScope`에 대해 설명하면서 이미 언급한 것이다. 

즉, 전역 스코프에서는 코루틴을 생성하는 `GloblaScope.launch`는 `runBlocking`과 어떤 관계를 가지지 않는다.

즉 부모-자식관계처럼 보이더라도 이 코루틴은 독립적으로 실행이 된다.

로그에 남는 결과도 주목해 보자.      

여기서 사용한 `launch`는 자신을 감싸는 부모 코루틴 즉, `runBlocking`과의 어떤 관계를 맺고 있는것 처럼 보인다.

어째든 이런 관계에는 몇 가지 특징을 보여준다.

1. 부모의 스레드가 어떤 이유로 취소되면 부모의 모든 자식 스레드는 취소된다.
2. 자식 스레드에서 익셉션을 던지면서 취소하게 된다면 부모 스레드로 전파가 되며 부모 스레드를 취소시킨다.
   - 물론 자식이 특정 이유로 취소를 하게 되면 부모 스레드로 전파하지 않는다.

트리 구조라는 의미를 볼 때 지금 위의 코드는 다음과 같은 느낌을 가진다.

```
├── main
│   ├── runBlocking
│   │   ├── launch
```
따라서 위 수정된 코드는 `runBlocking`이 생성한 부모 코루틴은 `delay`가 없더라고 `launch`가 생성한 자식 코루틴의 작업이 끝나야 부모가 끝나게 된다.     

게다가 로그에 남는 결과도 주목해 보라고 했는데 결과를 보면 스레드명이 전부 메인 스레드인것을 확인할 수 있다.       

`구조적 동시성`은 앞으로의 모든 부분에서 등장하기 때문에 꼭 기억해둬야 한다.

## coroutineScope()

`coroutineScope`를 이용해 새로운 영역을 생성할 수 있다.    

```kotlin
fun main() {
    runBlocking {
        customScope()
        println("RunBlocking Thread : ${Thread.currentThread().name}")
    }
    println("End")
}

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
//result
//customScope Thread : main
//Launch Thread1 : main
//Launch Thread2 : main
//RunBlocking Thread : main
//End
```
`runBlocking`과 비슷한 것 같지만 `coroutineScope`는 다음과 같이 생겼다.

즉 `suspend`키워드가 붙어 있는 `일시 중단 함수`라는 것을 알 수 있다.

또한 `coroutineScope`는 자식 코루틴이 완료되기 전까지 종료되지 않고 기다렸다가 자식들이 완료되야 자신도 완료된다.

```kotlin
public suspend fun <R> coroutineScope(block: suspend CoroutineScope.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return suspendCoroutineUninterceptedOrReturn { uCont ->
        val coroutine = ScopeCoroutine(uCont.context, uCont)
        coroutine.startUndispatchedOrReturn(coroutine, block)
    }
}

// 그러나 runBlocking함수는 일시 중단 함수가 아니다.
@Throws(InterruptedException::class)
public fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T {
   contract {
      callsInPlace(block, InvocationKind.EXACTLY_ONCE)
   }
   val currentThread = Thread.currentThread()
   val contextInterceptor = context[ContinuationInterceptor]
   val eventLoop: EventLoop?
   val newContext: CoroutineContext
   if (contextInterceptor == null) {
      // create or use private event loop if no dispatcher is specified
      eventLoop = ThreadLocalEventLoop.eventLoop
      newContext = GlobalScope.newCoroutineContext(context + eventLoop)
   } else {
      // See if context's interceptor is an event loop that we shall use (to support TestContext)
      // or take an existing thread-local event loop if present to avoid blocking it (but don't create one)
      eventLoop = (contextInterceptor as? EventLoop)?.takeIf { it.shouldBeProcessedFromContext() }
         ?: ThreadLocalEventLoop.currentOrNull()
      newContext = GlobalScope.newCoroutineContext(context)
   }
   val coroutine = BlockingCoroutine<T>(newContext, currentThread, eventLoop)
   coroutine.start(CoroutineStart.DEFAULT, coroutine, block)
   return coroutine.joinBlocking()
}
```
그리고 `coroutineScope`의 제네릭 반환 타입을 보면 커스텀 스코프내에서 결과값을 반환받아 사용할 수도 있다.

```kotlin
fun main() {
    runBlocking {
        val a = returnA()
        println("a is $a")
        val b = returnB()
        println("b is $b")
    }
    println("End")
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
```

## Coroutine Context

코루틴 빌더들의 코드를 보게 되면 각각의 코루틴마다 `CoroutineContext`, 즉 코루틴 문맥이 연관되어 있다.   

일례로 `launch`를 따라가면 `CoroutineContext`를 인자로 받고 있고 이것은 인터페이스로 되어 있다.      

해당 인터페이스를 살펴보면 마치 불변 맵처럼 `key-value`형식의 불변 컬렉션이라는 것을 알 수 있다.     

따라서 일종의 맵이라고 생각하는게 이해하기 수월할 수도 있다.        

코드 내부적으로 `Key`와 `Element` 이 두개의 인터페이스를 맵 형식으로 조합한 형태이기 때문이다.        

결국 코루틴이라는 것은 항상 `코루틴 문맥 (CoroutineContext)`안에서 실행된다는 것을 말하고 싶은 것이다.

이 코루틴 문맥에는 여러 요소가 포함될 수 있지만 그 중에 우리는 두 가지 요소를 주목해야 한다.

1. Job
2. Dispatcher

## Job

우리는 이미 위에서 몇 가지 특징을 알 수 있다.     

예를 들면 어떤 코루틴 안에 새로운 코루틴을 시작하게되면 `부모-자식`처럼 서브 코루틴은 상위 코루틴의 문맥을 상속한다.

그렇다면 이 `Job`은 무엇일까? 

위에서도 확인해 봤지만 `Job`객체와 그 하위 타입인 `Deferred`를 볼 수 있었다.

이러한 `Job`은 동시성 작업의 생명 주기와 관련된 객체라는 것이다.      

코틀린에서는 이 `Job`을 통해서 작업 상태 추적이나 작업 취소등을 할 수 있다.     

`Job`은 생명 주기를 가지고 있다. 

최초 `Job`이 생성되면 이것은 바로 활성화 상태가 된다.       

하지만 어떤 때는 이 `Job`의 활성화를 미루고 싶을 수도 있다.      

일반적으로 `Job`의 초기 상태는 `CoroutineStart`를 통해 제공한다.

1. CoroutineStart.DEFAULT
2. CoroutineStart.LAZY

이게 무슨 말인지 일단 코드로 확인해 보자.

```kotlin
fun main() {
    runBlocking {                           // (1)
        val job = launch {                  // (2)
            println("OK job is start")
        }
        delay(100)                          // (3)
        println("before job start")         // (4)
        job.start()                         // (5)
    }
}
```
이 코드를 실행하면 결과는 어떻게 될까?

얼핏 보면 (5)라는 코드 하나때문에 마치 (4)로그가 찍히고 (5)에 의해 (2)가 실행되면서 로그가 찍힐것처럼 보인다.

하지만 `Job`의 기본 설정은 `CoroutineStart.DEFAULT`이다.

(1)이 시작되고 새로운 코루틴 (2)는 생성되자 마자 활성화가 될 것이다.      

그 사이에 (3)으로 인해 자식 코루틴(2)가 실행되면서 'OK job is start'가 로그에 찍히고 (4)가 찍힐 것이다.

실제로 (4)와 (5)사이에 `println("Job is isCompleted? : ${job.isCompleted}")`를 통해 로그를 하나 더 찍어보면 해당 `Job`은 이미 완료가 되서 `true`를 찍게 된다.

따라서 (5)은 실행되지 않는다.       

그럼 아래 코드는 어떻게 될까?
```kotlin
fun main() {
    runBlocking {                                         // (1)
      val job = launch(start = CoroutineStart.LAZY) {     // (2)
         println("OK job is start")                         
      }
      delay(100)                                          // (3)
      println("Job is isCompleted? : ${job.isCompleted}") // (4)
      println("before job start")                         // (5)
      job.start()                                         // (6)
      delay(100)                                          // (7)          
      println("Job is isCompleted? : ${job.isCompleted}") // (8)
   }
}
// result
// Job is isCompleted? : false
// before job start
// OK job is start
// Job is isCompleted? : true
```
기본 설정이 아닌 `CoroutineStart.LAZY`로 설정했다.       

`LAZY`라는 단어에서 느낌이 팍 온다!

(1)이 생성되고 자식 코루틴인 (2)를 생성하지만 설정으로 인해 바로 활성화되지 않는다.     

따라서 (3)이 있다해도 실행되지 않고 미뤄지게 된다. 

이때 (4)은 어떤 작업도 완료되지 않았기 때문에 `false`를 찍게 된다.

(5)로그가 찍히고 (6)에서 지연된 `Job`을 실행시키기 때문에 그 때 (2)의 로그를 찍는다.     

(7)을 통해 (2)이 완료되기를 잠시 기다렸다고 (8)을 찍으면 `true`가 찍히게 된다.

## children Property

최상위 코루틴내의 생성되는 여러 개의 서브 코루틴의 `Job`의 정보를 알 수 있다.

아래 코드는 자식 `Job`의 완료 여부를 알아낼 수 있다.

```kotlin
fun main() {
    runBlocking {                                                               // (1)
        val parentJob = coroutineContext[Job.Key]!!                             // (2)
        val job1 = launch(start = CoroutineStart.LAZY) { println("Task One") }  // (3)
        val job2 = launch(start = CoroutineStart.LAZY) { println("Task Two") }  // (4)
        println("child coroutine: ${parentJob.children.count()}")               // (5)
        job1.start()                                                            // (6)    
        job2.start()                                                            // (7)
        delay(100)                                                              // (8)                
        println("child coroutine: ${parentJob.children.count()}")               // (9)
    }
    println("end")
}
```
위 코드가 어떻게 동작하는지 한번 생각해 보자.

(1)으로 코루틴이 생성되고 (2)에서는 `코루틴 문맥`을 통해 부모 코루틴의 `Job` 정보를 생성한다.

(3)과 (4)은 `Job`의 상태를 기본값이 아닌 지연 방식을 통해 바로 시작하지 않는다.      

이때 (5)에서는 현재 부모 코루틴의 `Job`으로 부터 서브 코루틴으로 생성된 `Job`의 카운터를 가져온다.      

여기서 `children`프로퍼티를 통해 접근한다.       

아직 시작하지 않았기 때문에 완료되지 않는 자식 코루틴은 2개이기 때문에 2를 찍는다.

(6)과 (7)을 통해서 지연된 코루틴을 시작하고 (0)을 통해 두 개의 자식 코루틴이 완료되도록 일시 중단을 하고 (9)를 찍는다.     

이 떄는 모든 `Job`이 완료되었기 때문에 0을 찍고 마지막으로 'end'를 찍으며 종료가 된다.      

이 코드에서는 `CoroutineStart.LAZY`설정과 `delay`를 이용했지만 `Job`의 `join`함수를 이용해서 조인이 된 `Job`이 완료될 떄까지 부모 코루틴을 일시 중단시킬수도 있다.

```kotlin
fun main() {
    runBlocking {
        val job = coroutineContext[Job.Key]!!
        val job1 = launch(start = CoroutineStart.DEFAULT) { println("Task One") }
        val job2 = launch(start = CoroutineStart.DEFAULT) { println("Task Two") }
        println("child coroutine: ${job.children.count()}")
        job1.join()
        job2.join()
        println("child coroutine: ${job.children.count()}")
    }
    println("end")
}
```
여기서 `join`함수를 통해 해당 `Job`들이 작업을 완료할 떄까지 부모 코루틴을 일시 중단한다.     

그래서 이 코드는 처음 작성한 코드와 같은 방식으로 동작한다.      

저 코드에서 `join`한 코드를 주석처리해보자.

그렇게 하면 콘솔에는 바로 바로 로그를 연달아 찍게 되는데 완료되지 않는 자식 `Job`이 둘다 2개가 있음을 로그에 찍고 자식 `Job`들이 찍히는 것을 보게 된다.

만일 여러분들이 이런 `Job`의 상태를 추적하고 싶을 수 있을 것이다.

해당 프로퍼티는 `isActive`, `isCancelled`, `isComplet`을 통해 접근할 수 있다.

이와 관련된 표가 있어서 아래 표로 작성한다.


| Job Status  | isActive | isCompleted | isCancelled |
|:-----------:|:--------:|:-----------:|:-----------:|
|     New     |  false   |    false    |    false    |
|   Active    |   true   |    false    |    false    |
| Completing  |   true   |    false    |    false    |
| Cancelling  |  false   |    false    |    true     |
|  Completed  |  false   |    true     |    true     |
|  Cancelled  |  false   |    true     |    false    |

표에서 재미있는 것은 `Completed`와 `Cancelled`는 `isCompleted`프로퍼티에서는 `true`라는 것이다.     

따라서 이게 정상적으로 완료된 `Job`인지 아닌지는 `isCancelled`프로퍼티를 통해 좀 더 세부적으로 알 수 있다.       

때론 실행되는 `Job`을 취소할 필요가 있는데 이럴 때는 `cancel`함수를 사용하면 된다.

```kotlin
fun main() {
    runBlocking {                             // (1)
        val longMaxJob = launch {             // (2)
            for(i in 1..Long.MAX_VALUE) {
                println("i is $i")            // (3)
                delay(100)                    // (4)
            }
        }
        delay(1000)                           // (5)
        //longMaxJob.cancelAndJoin()
        longMaxJob.cancel()                   // (6)
        longMaxJob.join()                     // (7)
    }
    println("end")
}
```
위 코드를 보면 다음과 같이 작동한다.

(1)이 실행되고 (2)코루틴이 활성화와 함께 시작이 된다.

(5)를 통해 코루틴이 실행하도록 일시 중단한다.      

대충 자식 코루틴이 루프를 돌면서 100만큼 딜레이를 주기 때문에 i는 10까지 찍고 종료가 될 것이다.     

(4)역시 `일시 중단 함수`으로 반복적으로 일시 중단했다가 재게를 할텐데 이때 (6)을 통해 취소 명령이 들어오고 (7)로 인해 해당 `Job`의 취소가 완료될 떄까지 기다리게 된다.

물론 이건 주석된 부분에서 알 수 있듯이 하나의 함수로도 처리할 수 있다.       

하지만 만일 자식 코루틴의 루프에 존재하는 `일시 중단 함수`인 `delay`를 주석처리하면 어떤 일이 벌어질까???

분명 취소 명령을 날렸음에도 해당 `Job`은 `Long`의 `MAX_VALUE`에 도달할 떄까지 계속 반복할 것이다.

여기에서 우리가 알 수 있는 것은 실행되는 `Job`을 취소하는 데에는 일종의 협조가 필요하다는 것이다.       

코루틴의 모든 `일시 중단 함수`는 취소가 가능하다. 

이것은 지속적으로 자신에게 취소 명령이 있는지 확인하기 때문이다.   

그러나 `delay`라는 `일시 중단 함수`가 없는 `longMaxJob`은 왜 취소가 안되었을까?

이 `Job`은 코드상에서 자신에게 취소 명령이 내려왔는지 확인하는 부분이 없다.       

한번 루프가 돌기 시작하면 이 루프는 결국 `Long`의 `MAX_VALUE`까지 돌고나서야 확인할 것이기 때문이다.     

그 때는 뭐 이미 해당 `Job`의 작업이 종료되는 시점이니 의미가 없다.       

문제는 만일 `while(true)`같은 코드였다면 어떤 일이 벌어질까??        

아마도 무한히 돌다가 `StackOverFlow`가 발생하지 않을까???? 

이것을 해결하기 위해서 `CancellationException`을 발생시켜 취소할 수 있도록 `일시 중단 함수`를 호출하는 방법이 있다.     

위에서 우리가 확인했던 `delay`가 대표적인 예인데 `delay`나 `join`같은 `일시 중단 함수`가 이 예외를 발생시켜주기 때문이다.

하지만 그중에 `yield`함수를 사용하는 방법도 있다.

```kotlin
fun main() {
    runBlocking {
        val longMaxJob = launch {
            for(i in 1..Long.MAX_VALUE) {
                println("i is $i")
                yield()
            }
        }
        delay(1000)
        //longMaxJob.cancelAndJoin()
        longMaxJob.cancel()
        longMaxJob.join()
    }
    println("end")
}
```
여러분은 자바의 `Thread.yield()`가 떠오를 것이다. 

이 `yield`함수는 실행중인 잡이 있으면 일시 중단시켜서 다른 코루틴에게 양보를 해서 해당 코루틴이 실행될 수 있는 기회를 부여한다.

## 부모 코루틴이 취소되면 자식 코루틴도 취소된다.       

이 말은 `구조적 동시성`에서 설명한 부분이다.       

부모 코루틴이 종료되면 자식 코루틴도 종료되는 것은 확인했지만 취소의 경우도 한번 코드를 통해 확인해 보자.

```kotlin
fun main() {
    runBlocking {
        val parent = launch {
            println("parent start")
            child()
            delay(500)
            println("parent end")
        }
    }
    println("end")
}

suspend fun child() = coroutineScope {
    launch {
        println("childOne start")
        delay(100)
        println("childOne end")
    }

    launch {
        println("childTwo start")
        delay(100)
        println("childTwo end")
    }
}
//parent start
//childOne start
//childTwo start
//childOne end
//childTwo end
//parent end
//end
```
지금까지 배운 내용을 토대로 결과를 보면 예상할 수 있는 내용이다.

하지만 다음과 같이 코드를 작성해 보자.

```kotlin
fun main() {
    runBlocking {
        val parent = launch {           // (1)
            println("parent start")     // (2)
            child()                     // (3)
            delay(500)                  // (4)
            println("parent end")       // (5)
        }
        delay(100)                      // (6)
        parent.cancel()                 // (7)
    }
    println("end")                      // (8)
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
//parent start
//childOne start
//childTwo start
//end
```

이 코드를 수행하게 되면 처음 작성한 코드와는 다른 결과를 볼 수 있다.

(1) 코루틴이 생성되고 나서 (2) 로그를 찍는다.

그리고 (6)에 의해 일시중단이 되면 자식 코루틴이 (3)에서 실행된다.   

이때 (9)와 (12)이 동시에 찍히고 `delay(100)`에 의해 일시 중단이 이뤄지는데 이때 (6)에 의해 일시중단된 부모 코루틴은 (7)이 실행된다.

부모 코루틴의 `Job`이 취소되면서 실행되던 (11)과 (14)가 로그로 찍히기 전에 자식 코루틴인 (3)도 취소되면서 결국 이 로그는 찍히지 않는다.

그리고 (8)이 찍힌다.

## 타임 아웃

우리가 어떤 `RESTful api`를 호출할 때, 에를 들면 스프링 부트에서 `RestTemplate`이나 `FeignClient`같은 라이브러리를 통해 외부 API를 호출하는 경우가 있다.     

하지만 외부 API를 호출할 떄 우리가 주의해야 하는 점은 서버의 응답 시긴이다.      

해당 서버가 멀쩡하더라도 네트워크에 문제가 있다면 요청이 들어갔더라도 응답 시간이 오래 걸릴 수도 있다.      

무한정 기다릴수 없기 때문에 특정 시간을 주어 해당 시간내에 응답이 오지 않는다면 에러를 발생시켜 다시 시도할 수 있도록 호출한 쪽에 응답을 주는 편이 좋을 수 있다.     

코루틴에서도 이런 상황이 필요한 경우가 생길 수 있다.      

이 때 사용할 수 있는 함수가 있는데 그것은 `withTimeout`과 `withTimeoutOrNull`을 사용할 수 있다.

```kotlin
fun main() {
   runBlocking {
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

fun api(): String {
   return "OK"
}
```
위 코드를 실행하면 'OK'가 잘 찍힐 것이다. 하지만 해당 코드의 `callApi`에서 `delay`의 시간을 150으로 주고 실행해 보자.

`withTimeout`에 넘긴 시간보다는 응답 시간이 길기 때문에 익셉션이 발생하고 `catch`절에서 다음과 같은 메세지를 찍게 된다.

```
Timed out waiting for 100 ms
```
이것은 API의 성격에 따라 달라질 수 있는데 `try~catch`를 통해 익센셥을 잡을 필요가 없고 그냥 `null`을 반환하겠다면 `withTimeoutOrNull`을 사용할 수 있다.

## Coroutine Dispatcher

우리가 자바의 `Servlet`, 즉 서블릿을 공부하면 이 디스패처라는 단어를 꾸준히 볼수 있다.      

예를 들면 `HTTP`를 통해 들어온 요청을 `디스패처 서블릿`에서 공통 작업을 처리한 이후에 이 요청을 어떤 컨트롤러로 넘길지 결정하게 된다.    

이는 자바 개발자라면 거의 대부분 사용하는 스프링 프레임워크에서도 똑같이 발생한다.      

다만 스프링이 `@GetMapping`같은 애너테이션을 통해 이 모든 것을 처리해주기 때문에 숨겨져 있어서 잘 모르시는 주니어 분들도 있다.

게다가 만일 `thymeleaf`같은 UI를 사용하게 되면 `static`폴더에 대해서는 따로 `resourceHandler`를 작성하거나 `yml`을 통해 설정하기도 한다.

어째든 과거처럼 Tomcat같은 WAS를 설치하고 `web.xml`이나 `servlet.xml`같은 곳에 이런 요청 URL을 일일히 서블릿과 매핑 작업을 할 일이 없을 뿐이다.

문득 옛날 생각을 하니 PTSD 오지는 작업이긴 하다.      

이야기가 삼천포로 빠졌는데 어느 정도 어떤 이야기를 할지 대충 파악이 될 것이라고 생각한다.       

코루틴이 실제로는 스레드와는 무관하게 계속적으로 언급하는 일시 중단 가능하도록 구현할 수 있게 도와준다.      

하지만 그럼에도 여전히 스레드와 따로 떼놓고 말할 수 없다. 

즉 스레드와는 여전히 연관이 있고 연관시켜야 한다.    

바로 이것을 담당하는 특별한 녀석이 존재하는데 그것이 바로 `코루틴 디스패처`이다.       

이 `코루틴 디스패쳐`는 특정 스레드에서 실행하게 제어할 수 있고, 스레드 풀에서 실행하게 제어할 수 있다.     

또는 어느 스레드에서 실행할지 결정하지 않는 상태로 둘 수도 있다.       

`디스패처 서블릿`도 그렇고 `코루틴 디스패쳐`도 가만히 보면 마치 교통 정리를 하는 라우터같은 느낌이 들기도 한다. 

앞서 말했듯이 코루틴은 항상 이 `Coroutine Context`,즉 코루틴 문맥 안에서 실행한다고 했다.     

이 코루틴 디스패쳐는 `코루틴 문맥`의 일부분으로 지금까지 배웠던 코루틴 빌더에서 지정할 수 있다.       

지금까지는 우리는 어떤 디스패처를 설정한 적이 없다.      

하지만 위에 코루틴 빌더의 코드를 살펴보면 들어오는 인자중 `context`를 확인할 수 있는데 지금까지는 어떤 지정도 하지 않았기 때문에 기본적으로 이 코루틴 빌더는 `EmptyCoroutineContext`를 넘기고 있다는 것을 알 수 있다.

`start`는 `CoroutineStart`를 통해 `Job`의 상태를 제어했던 것처럼 이 디스패처도 몇가지를 제공한다.

일단 다시 한번 이전 기본적인 코드를 살펴보자.

```kotlin
fun main() {
    runBlocking {
        launch {
            println("launch 1 스레드 이름 : ${Thread.currentThread().name}")
            println("launch 1 스레드 아이디 : ${Thread.currentThread().id}")
        }
        launch {
            println("launch 2 스레드 이름 : ${Thread.currentThread().name}")
            println("launch 2 스레드 아이디 : ${Thread.currentThread().id}")
        }
        println("runBlocking 스레드 이름 : ${Thread.currentThread().name}")
        println("runBlocking 스레드 아이디 : ${Thread.currentThread().id}")
    }
}
//runBlocking 스레드 이름 : main
//runBlocking 스레드 아이디 : 1
//launch 1 스레드 이름 : main
//launch 1 스레드 아이디 : 1
//launch 2 스레드 이름 : main
//launch 2 스레드 아이디 : 1
```
이 코드는 어떤 디스패처도 설정하지 않았다.       

따라서 자식 코루틴은 자동적으로 부모로부터 상속받은 스레드의 이름과 아이디 정보를 찍는 것을 알 수 있다.      

지금까지 배워온 내용을 상기시키면 `구조적 동시성`의 부모-자식 관계에 의한 당연한 결과일 것이다.

다음은 `Dispatchers.kt`의 주석을 삭제한 코드이다. 

```kotlin
@file:Suppress("unused")

package kotlinx.coroutines

import kotlinx.coroutines.internal.*
import kotlinx.coroutines.scheduling.*
import kotlin.coroutines.*

public const val IO_PARALLELISM_PROPERTY_NAME: String = "kotlinx.coroutines.io.parallelism"

public actual object Dispatchers {
    
   @JvmStatic
    public actual val Default: CoroutineDispatcher = createDefaultDispatcher()

    @JvmStatic
    public actual val Main: MainCoroutineDispatcher get() = MainDispatcherLoader.dispatcher

    @JvmStatic
    public actual val Unconfined: CoroutineDispatcher = kotlinx.coroutines.Unconfined

    @JvmStatic
    public val IO: CoroutineDispatcher = DefaultScheduler.IO
}

```
4개의 `코루틴 디스패처`가 눈에 보이고 그 중에 `Main`은 좀 다른 3개와는 좀 독특한 코드인 것을 확인할 수 있다.     
 
이것은 안드로이드와 관련된 디스패처로 나는 안드로이드를 하지 않기 때문에 이 부분은 솔직히 잘 모른다.      

다만 이 `Dispatcher.Main`은 UI와 상호작용하는 작업을 실행하기 위해서만 사용해야 한다는 설명이 있다.

실제로 예제 관련 코드를 다음과 같이 실행하면

```kotlin
fun main() {
   runBlocking {
      CoroutineScope(Dispatchers.Main).launch {
         println("this?")
      }
   }
}
```
아래와 같이 안드로이드 관련 오류가 발생한다.
```
Exception in thread "main" java.lang.IllegalStateException: Module with the Main dispatcher is missing. 
Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure it has the same version as 'kotlinx-coroutines-core'
```
이 부분은 코틀린의 코루틴 공식 사이트의 [Main](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-main.html)에서 확인할 수 있다.     

```
In order to work with the Main dispatcher, the following artifact should be added to the project runtime dependencies:

kotlinx-coroutines-android — for Android Main thread dispatcher
kotlinx-coroutines-javafx — for JavaFx Application thread dispatcher
kotlinx-coroutines-swing — for Swing EDT dispatcher
```
자 그렇다면 이제 나머지 3개에 대해서 알아보자.

## Dispatchers.Default

이 디스패처의 대한 설명은 다음과 같다.

```
공유 스레드 풀로 일반적으로 풀 크기는 기본적으로 사용가능한 CPU 코어 수나 숫자 2중 큰값을 선택한다.
그래서 CPU속도에 의해 결정되는 작업에 사용하기 용이하다.
```

디스패처를 설정하지 않는 코루틴과 디스패처를 기본값으로 설정한 코루틴 내에 `delay`를 통해 일시 중단했다가 다시 재게하는 초간단 코드이다. 

```kotlin
fun main() {
    runBlocking {
        println("runBlocking 스레드 이름 : ${Thread.currentThread().name}")
        launch {
            println("before delay, launch 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, launch 스레드 이름 : ${Thread.currentThread().name}")
        }
        launch(context = Dispatchers.Default) {
            println("before delay, Dispatchers.Default 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, Dispatchers.Default 스레드 이름 : ${Thread.currentThread().name}")
        }   
    }
}
```
똑같은 말이 되풀이되지만 디스패처를 설정하지 않는 코루틴은 당연히 부모 코루틴의 문맥을 상속받는 것을 확인할 수 있다.     

하지만 `Dispatchers.Default`은 마치 `GlobalScope.launch`처럼 공유 백그라운드 스레드 풀을 사용하고 있다.

```
runBlocking 스레드 이름 : main
before delay, Dispatchers.Default 스레드 이름 : DefaultDispatcher-worker-1
before delay, launch 스레드 이름 : main
after delay, launch 스레드 이름 : main
after delay, Dispatchers.Default 스레드 이름 : DefaultDispatcher-worker-1
```

즉 `Dispatchers.Default`은 디스패처에 의해 어떤 스레드에서 작업할지 결정한다.     

## Dispatchers.Unconfined

일단 이녀석은 뭔가 친화력 만렙인데 근본이 없는 것 같은 녀석이다.       

무슨 말인고 하면 이 디스패처의 설정은 처음 해당 코루틴이 실행할 때는 부모의 문맥을 따라 실행된다.     

하지만 다른 `일시 중단 함수`에 의해 중단되었다가 다시 재게가 되었을 때는 해당 스레드를 따라간다.       

우선 코드를 한번 살펴보자.

```kotlin
fun main() {
    runBlocking {
        println("runBlocking 스레드 이름 : ${Thread.currentThread().name}")
        launch {
            println("before delay, launch 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, launch 스레드 이름 : ${Thread.currentThread().name}")
        }
       
        launch(context = Dispatchers.Unconfined) { // (1)                                                      
            println("before delay, Dispatchers.Unconfined 스레드 이름 : ${Thread.currentThread().name}") // (2)
            delay(100) // (3)
            println("after delay, Dispatchers.Unconfined 스레드 이름 : ${Thread.currentThread().name}") // (4) 
        }
    }
}
```
부모 코루틴이 실행되고 (1)이 생성과 동시에 활성화되고 나서 (2)를 찍으면 부모의 문맥을 상속받은 것까지는 확인이 된다.      

이때 (3)이 일시 중단을 시켰다가 다시 재게되고 나서 (4)에 찍히는 녀석은 `kotlinx.coroutines.DefaultExecutor`이다.     

실제로 이 클래스를 따라가면 코드 내용 중 다음 코드를 확인 할 수 있다.

```kotlin
internal actual val DefaultDelay: Delay = DefaultExecutor
```
그렇다! 

이 녀석은 부모를 따라가다가 잠시 멈췄는데 그 때 자신을 호출한 `delay`함수와 친해져서 재게가 되었을 때는 `DefaultExecutor`를 따라서 그 스레드에서 실행이 재게된 것이다.

즉 위 코드의 결과는 다음과 같다.

```
runBlocking 스레드 이름 : main
before delay, Dispatchers.Unconfined 스레드 이름 : main
before delay, launch 스레드 이름 : main
after delay, Dispatchers.Unconfined 스레드 이름 : kotlinx.coroutines.DefaultExecutor
after delay, launch 스레드 이름 : main
```

이 부분은 코틀린 공식 사이트의 내용을 살펴 볼 필요가 있다.      

이런 이유로 공식 사이트에서도 `Dispatchers.Unconfined`에 대해 다음과 같이 설명하고 있다.

```
The Dispatchers.Unconfined coroutine dispatcher starts a coroutine in the caller thread, but only until the first suspension point. 
After suspension it resumes the coroutine in the thread that is fully determined by the suspending function that was invoked. 
The unconfined dispatcher is appropriate for coroutines which neither consume CPU time nor update any shared data (like UI) confined to a specific thread.

처음에는 Dispatchers.Unconfined은 자신을 호출한 스레드의 코루틴(부모)에서 시작한다. 그러나 이것은 처음 일시 중단 함수를 만나기 전까지이다.
일시 중단이후 다시 시작할 때는 자신을 정지시킨 일시 중단 함수를 따라서 해당 코루틴의 스레드에서 재게가 된다.      
이것은 CPU타입을 점유하지 않거나 공유 데이터, 예를 들면 UI등을 건드리지 않는 등 특정 스레드에 국한되지 않고 동작하는 경우 사용하면 된다.
```
말 그대로 위에 로그를 보면 특정 스레드에 국한되지 않는 모양새를 보여준다.

특이하게도 이 내용은 몇 몇 책에서는 소개하고 있지 않다.     

이유는 모르지만 아마도 공식 사이트에서 일반 코드에서는 사용하면 안된다는 일종의 주의 문구가 있기 때문이 아닌가 추측할 뿐이다.

```
The unconfined dispatcher is an advanced mechanism that can be helpful in certain corner cases where dispatching of a coroutine for its execution later is not needed or produces undesirable side-effects, 
because some operation in a coroutine must be performed right away. 
The unconfined dispatcher should not be used in general code.

이 디스패처는 나중에 실행하기 위해 코루틴을 디스패치할 필요가 없거나 바람직하지 않은 사이드 이펙트가 발생할 수 있는 특정 상황의 케이스에서 사용할 수 있는 고급 메카니즘이다.
왜냐하면 코루틴내의 어떤 오퍼레이션 작업들은 바로 실행되어야 하기 때문이다.      
그래서 일반 코드내에서는 사용하지 마라.
```
사실 개인적으로 나는 이 문구에서 나오는 상황에 대해서는 감이 오지 않는다.       

다만 고민해 볼것은 원래의 부모의 코루틴 문맥을 따라가 작업을 완료해야 하는 자식 코루틴이 중간에 다른 스레드로 의도치 않게 넘어갈 수도 있다는 의미로 해석되기도 한다.     

이런 특징을 잘 이용하면 문제가 없겠지만 모른다면 예상치 않는 결과를 불러일으킬 수 있다고 추측할 뿐이다.      

## Dispatchers.IO

일단 코드부터 보자

```kotlin
fun main() {
    runBlocking {
        println("runBlocking 스레드 이름 : ${Thread.currentThread().name}")
        launch {
            println("before delay, launch 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, launch 스레드 이름 : ${Thread.currentThread().name}")
        }
        launch(context = Dispatchers.IO) {
            println("before delay, Dispatchers.IO 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, Dispatchers.IO 스레드 이름 : ${Thread.currentThread().name}")
        }
    }
}
```
실제로 코드를 수행하면 `Dispatchers.Default`와 크게 다르지 않아 보인다.

다만 이에 대한 설명은 다음과 같다.

```
스레드 풀 기반으로 Dispatchers.Default와 구현이 비슷하지만 몇가지 특징이 있다.
예를 들면 파일을 읽고 쓰는 잠재적인 blocking I/O를 사용하는 환경에 있다면 거기에 최적화된 디스패처이다.
참고로 이 디스페처는 스레드 풀을 Dispatchers.Default와 함께 공유한다. 
하지만 필요에 따라 스레드를 추가하거나 종료시켜주는 기능을 가지고 있다.
```

## asCoroutineDispatcher() 확장 함수 

이렇게 보면 코루틴 디스패처는 이미 언급했듯이 라우터같은 역할을 한다. 

즉 동시 작업에서 스레드를 분배해주는 역할을 한다.        

이렇게 `Dispatchers`에서 제공하는 몇 가지 디스패처말고도 자바처럼 `asCoroutineDispatcher`확장 함수를 코루틴 디스패처를 구현할 수 있다.

다음 코드는 자바의 `Excutors`를 활용한 `asCoroutineDispatcher`확장 함수의 예를 보여준다.

참고로 자바의 `Excutors`는 스레드를 만들고 관리하기 위한 만들어진 고수준의 API이다.   

그리고 이것을 확장한 `ExecutorService`를 사용해서 좀 더 효율적으로 처리하도록 라이브러리를 제공하고 있다.     

구글링 한번 해보시길!

```kotlin
fun main() {
    val id = AtomicInteger(0)
    // core size를 5
    // 즉 5개의 특정 스레드 이름을 갖는 디스패처를 생성한다.
    // ScheduledThreadPoolExecutor는 코드를 따라가다보면 최종적으로 Executor의 구현체이다.
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

}
//Thread-1
//Thread-2
//Thread-3
//Thread-4
//Thread-5
//Thread-4
//Thread-2
//Thread-3
//Thread-4
//Thread-1
```
디스패처를 사용해 총 10개의 코루틴을 생성했을 때 executor에서 만들어 논 5개의 디스패처를 사용하고 있는 것을 확인할 수 있다.  

그리고 그때 그때 마다 디스패처에 의해 분배가 되서 로그에 찍히는 것을 확인할 수 있다.    

물론 처음 5개는 동시에 실행될테니 마치 순차적으로 실행된것처럼 보이지만 그 이후에는 무작위로 찍히는 것을 확인할 수 있다.    

때로 위에 로그처럼 `Thread-4`가 2번 찍힐 떄도 있고 1부터 5까지 무작위로 찍힐 때도 있다.    

어쨰든 이 코드는 `ThreadPoolDispatcher.kt`의 코드를 참조했다.      

```kotlin
@ObsoleteCoroutinesApi
fun main() {
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
```
`newSingleThreadContext`은 실제 코드 내용을 보면 스레드 수를 1로 고정한 것이라는 것을 알 수 있다.    

마치 자바의 `ForkJoin`을 떠올리게 하지만 `newFixedThreadPoolContext`와 `newSingleThreadContext`는 `@ObsoleteCoroutinesApi`이 붙어 있다.

이 애노테이션은 `@Deprecated`랑 좀 비슷해 보이지만 내용을 살펴보면 다음과 같은 설명이 공식 사이트에 명시되어 있다.

```
Marks declarations that are obsolete in coroutines API, 
which means that the design of the corresponding declarations has serious known flaws and they will be redesigned in the future. 
Roughly speaking, these declarations will be deprecated in the future but there is no replacement for them yet, 
so they cannot be deprecated right away.

코루틴 API에서 이 마크는 '구식', 즉 더 이상 사용되지 않는 구식 API임을 선언한다.
심각한 결함이 있는 디자인으로 미래에서는 다시 API를 재설계할 예정이다. 
대략적으로 말하자면 이것은 향후 더 이상 사용하지 않을 예정이지만 당장 이들을 대체할 것이 없기 때문에 바로 `더 이상 사용하지 않는` 상태는 되지 않을 것이다.
```

## withContext

`async`를 활용했던 코드를 한번 살펴보자.

```kotlin
fun main() {
   runBlocking {
      val asyncResult = async {
         "result"
      }
      val result = asyncResult.await();
      println("result is $result")
   }
}
```
`async`에서 이미 배웠던 `Deferred`로 반환되는 `Job`에서 `await`를 통해 값을 받기 전까지 기다렸다가 받아서 로그를 찍는 코드이다.

다만 이것을 `withContext`를 이용해 좀더 간결하게 사용할 수 있다.

```kotlin
fun main() {
    runBlocking {
        val result = withContext(Dispatchers.Default) {
            "result"
        }
        println("result is $result")
    }
}
```
확실히 좀 더 간략해졌다.       

근데 이제 문득 궁금해진다. 

그렇다면 언제 `async`를 쓰고 언제 `withContext`를 써야할까???

```kotlin
fun main() {

    val limit = 100_000
    //val limit = 10_000
    //val limit = 1000
   
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
```
위 코드는 10만번 루프를 돌면서 처리하는 코드로 결과는 둘다 똑같다.      

위 코드의 결과는 `async`의 압승으로 끝난다.     

확실히 속도의 차이가 눈에 띌 만큼 난다. 

하지만 1만번으로 루프를 돌려보자.       
 
여전히 `async`의 승리로 끝나지만 그 속도 차이가 유의미한 차이는 나지 않는다.

하지만 1천번 루프를 돌면 `withContext`가 좀 더 빠르게 끝나는 경우가 많아진다.     

이유는 이 둘의 차이떄문인데 `async`는 `Deferred`, 즉 `Job`을 동시적으로 병렬 처리를 하지만 `withContext`은 순차적으로 작동하기 때문이다.     

`withContext`은 결국 새로운 코루틴을 다른 스레드에서 `동기적`으로 실행하는 코드이다.      

아마도 코루틴내에서 엄청난 양의 작업을 병렬로 진행하는 것이 아니라면 `withContext`를 고려해 볼만하다고 생각한다.

## 코루틴의 예외처리

코루틴은 일반적으로 `CoroutineScope`를 통한 코루틴 빌더안에서 실행된다.

당연한 이야기긴 한데 이로 인해서 스레드의 예외 처리 방식과는 다르게 진행이 된다는 것을 말하고 싶다.

예를 들면 우리가 흔히 에러 처리라고 한다면 먼저 떠올릴 수 있는 것이 `try~catch`이다.   

정말 단순하게 다음과 같이 한다고 해보자.

```kotlin
fun main() {
   runBlocking {
      launch {
         try {
            throw Exception("에이쿠")
         } catch (e: Exception) {
            println("${e.message} 에러야!")
         }
      }

      launch {
         try {
            throw Exception("에이쿠 에이쿠")
         } catch (e: Exception) {
            println("${e.message} 에러야!")
         }
      }
      println("runBlocking")
   }
   println("completed")
}
```
뭔가 설득력이 있어보인다.      

물론 저 코드는 아주 잘 동작한다.       

내부에서 벌어지는 에러를 잡아서 로그를 찍기 때문에 두 자식의 `Job`은 에러 메세지를 찍으며 완료가 된다.       

하지만 지금처럼 코루틴 내부의 어떤 비지니스 로직에 의한 에러가 아닌 코루틴 자체가 에러가 난다고 생각해보자.

```kotlin
fun main() {
   runBlocking {                             // (1)
      try {
         launch {                            // (2)
            throw Exception("에이쿠")          // (3)
         }
      } catch (e: Exception) {
         println("${e.message} 에러야!")       // (4)
      }

      try {
         launch {                            // (5)
            throw Exception("에이쿠 어이쿠")     // (6)
         }
      } catch (e: Exception) {
         println("${e.message} 에러야!")       // (7)
      }

      println("ok")                          // (8)
   }
   println("completed")                      // (9)
}
```
이 코드는 과연 (9)까지 성공할 것인가?

(1)에서 코루틴이 생성되고 (2)과 (5)에서 (1)의 자식으로 활성화 될것이다.

그리고 (8)이 찍히고 자식 코루틴이 끝날 때까지 기다리다가 (3)에서 에러가 발생하면 (4)로그를 찍고 (5)에서 시작한 자식 코루틴 역시 (2)처럼 동작할 것처럼 보인다.        

하지만 그렇지 못하고 (3)에서 발생한 에러는 부모인 (1)으로 에러가 전파되서 프로세스가 비정상 종료된다.     

따라서 (9)는 찍히지 않는다.

당연히 자식 코루틴 (5)까지 도달하지도 못하고 역시 같이 종료되버린다.      

이로써 우리가 `구조적 동시성`에서 언급한 내용들이 그대로 반영되고 있는 것 역시 확인했다.

코루틴에서 예외 처리의 경우에는 코루틴 빌더들은 다음 두 가지 기본 전략중 하나를 선택하게 된다.

1. 부모 코루틴이 똑같은 오류로 취소된다. 이로 인해 부모의 자식도 모두 취소된다.
2. 자식들이 모두 취소되면 부모는 예외를 코루틴 트리의 상위로 전파한다.

즉 이것은 탑 레벨의 코루틴에 도달하기 전까지 이 과정이 포함된다.

똑같은 말이 반복되는데 위 코드는 이것을 설명하고 있는 것이다. 

첫 번째 자식 코루틴이 던진 예외가 부모 코루틴으로 전달되어 최상위 작업이 취소되고 따라서 최상위의 두 자식의 작업도 취소됬다.

이때 이 코드에서는 그 예외에 대한 어떤 작업도 지정한 적이 없다.     

따라서 `Thread.uncaughtExceptionHandler`에 등록된 기본 동작이 실행된다.      

근데 이런 시나리오도 생각해 봐야 한다. 

두 자식 코루틴중 하나가 실패하더라도 해당 작업만 실패 처리하고 나머지는 게속 작업을 실행시켜 완료시키는 시나리오도 분명 존재할 수 있다.     

이 때는 우리는 이것을 위해 특별히 존재하는 `Scope`를 사용할 수 있다.

## supervisorScope

이 특별한 `Scope`는 해당 영역에 존재하는 자식 코루틴들중 하나가 취소가 되더라도 다른 자식 코루틴에 영향을 주지 않는다.

```kotlin
fun main() {

    val errorHandler = CoroutineExceptionHandler { _, e ->
        println("error message : ${e.message}")
    }

    runBlocking {                               // (1)

        supervisorScope {                       // (2)
            launch(errorHandler) {              // (3)
                throw Exception("에이쿠")         // (4)
            }

            launch {                            // (5)
                println("이것은 완료된다!")
            }
        }
       
        println("ok")                            // (6)
    }
    println("completed")                         // (7)
}
```
이 코드는 `CoroutineExceptionHandler`을 활용한 에러핸들러를 하나 만들어서 에러가 나는 코루틴 빌더에 지정을 하는 코드이다.    

일단 `supervisorScope`를 이용해 2개의 코루틴을 하나의 영역으로 묶었다.    

해당 코드는 다음과 같이 동작하게 된다.

(1)이 시작되고 `supervisorScope`를 통해 하나의 영역으로 묶은 (2)가 시작된다.     

(3), (5)이 두 자식 코루틴이 실행되는데 (3)은 에러가 나지만 (2)로 전파되지 않는다.     

그리고 (5)은 그 영향을 받지 않고 실행된다.

따라서 (4)의 에러 메세지는 따로 정의한 `errorHandler`를 통해 로그를 찍는다.   

그리고 (5)의 `Job`도 작업이 완료되서 로그를 찍고 (6)로그를 찍고 최종적으로 (7)까지 도달한다.

```
error message : 에이쿠
이것은 완료된다!
ok
completed
```
이것이 최종 결과과 된다.       

결국 이 `SupervisorJob`은 에러의 전파가 자식으로 영역을 제한하는 것이라는 것을 확인할 수 있다.

## supervisorScope와 async의 콤보

그냥 `launch`를 사용할 때보다는 `async`와 함께 조합해서 사용할 때 위 시나리오가 상당히 설득력있게 보인다.         

예를 들면 어떤 작업 내에서 비동기로 2개의 api를 호출해서 받은 두 개의 결과를 합해서 반환하는 로직이 있다고 생각해 보자.     

우리가 이미 알듯이 둘 중 하나가 에러가 나거나 한다면 프로세스가 종료되는 상황이 발생한다.      

```kotlin
fun main() {

    runBlocking {
         val apiOneDeferred = async {
             throw Exception("에러!") // (1)
             apiOne()
         }
         val apiTwoDeferred = async {
             apiTwo()
         }

         val oneResult = try {
             apiOneDeferred.await()
         } catch (e: Exception) {
             emptyList()
         }

         val twoResult = try {
             apiTwoDeferred.await()
         } catch (e: Exception) {
             emptyList()
         }

         val total = oneResult + twoResult

         println(total)
    }
}

fun apiOne(): List<String> {
    return listOf("A", "B")
}

fun apiTwo(): List<String> {
    return listOf("C", "D")
}
```
일단 (1)을 주석처리해서 코드를 실행하면 결과는 두 개의 리스트를 `+`연산자를 통해서 새로운 불변 리스트로 반환하는 코드이다.    

근데 이 (1)의 주석을 해제하고 실행하면 어떤 일이 벌어질까?     

지금 위 코드는 잘 작동할 것처럼 보인다.      

`await`를 통해 `Deferred`로부터 결과를 가져올 때 에러가 나더라도 `try-catch`를 통해서 잡힐 것처럼 보인다.

따라서 빈 리스트를 반환할 것처럼 보인다.

하지만 앞서 이와 관련 테스트 코드에서 확인해 본 것처럼 에러는 최상위로 전파된다.    

당연히 부모가 취소되면서 정상적으로 작동하는 두 번째 `Job`은 실행조차 못한다.       

물론 빈 배열을 찍긴 하지만 프로세스가 비정상 종료가 되버렸기 때문에 의미가 없다.      

```kotlin
fun main() {

    runBlocking {

        supervisorScope {
            val apiOneDeferred = async {
                throw Exception("에러!")
                apiOne()
            }
            val apiTwoDeferred = async {
                apiTwo()
            }

            val oneResult = try {
                apiOneDeferred.await()
            } catch (e: Exception) {
                emptyList()
            }

            val twoResult = try {
                apiTwoDeferred.await()
            } catch (e: Exception) {
                emptyList()
            }

            val total = oneResult + twoResult
            println(total)
        }
       
    }
}

fun apiOne(): List<String> {
    return listOf("A", "B")
}

fun apiTwo(): List<String> {
    return listOf("C", "D")
}
```

이 때 `supervisorScope`로 해당 자식 코루틴을 감싸서 전파가 자식으로만 한정지어버리면 우리가 원하는 결과를 얻을 수 있다.

