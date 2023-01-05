import kotlinx.coroutines.*

/**
 * 코루틴은 Dispatchers객체를 통해서 3개의 디스패처 설정을 제공한다.
 * Default
 * Unconfined
 * IO
 * Main
 */
fun main() {
    runBlocking {

        // 구조적 동시성에 의하면 어떤 디스패처 설정도 하지 않는 경우 부모를 상속받는다.
        println("runBlocking 스레드 이름 : ${Thread.currentThread().name}")
        launch {
            println("before delay, launch 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, launch 스레드 이름 : ${Thread.currentThread().name}")
        }

        // Dispatchers.Default로 디스패처 설정시 다른 스레드를 분배받는다. 이것은 마치 GloblaScope와 비슷하다.
        launch(context = Dispatchers.Default) {
            println("before delay, Dispatchers.Default 스레드 이름 : ${Thread.currentThread().name}")
            delay(100)
            println("after delay, Dispatchers.Default 스레드 이름 : ${Thread.currentThread().name}")
        }

        // 이 녀석은 처음에는 부모를 따라가다 자신을 중단하고 다시 재게될 떄 해당 스레드를 따라가는 친화력 만렙의 근본없는 녀석이다
        launch(context = Dispatchers.Unconfined) { // (1)
            println("before delay, Dispatchers.Unconfined 스레드 이름 : ${Thread.currentThread().name}") // (2)
            delay(100) // (3)
            println("after delay, Dispatchers.Unconfined 스레드 이름 : ${Thread.currentThread().name}") // (4)
        }

    }
}