import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * runBlocking은 메인 스레드를 상속한다. 하지만 일시 중단 함수는 아니다.
 *
 *
 */
fun main() {

    /*
    GlobalScope.launch {
        delay(100)
        println("Launch Thread : ${Thread.currentThread().name}")
    }

    runBlocking {
        println("RunBlocking Thread : ${Thread.currentThread().name}")
        delay(500)
    }
    */


    // GlobalScope는 전역 영역, 즉 탑 레벨의 코루틴으로 runBlocking과는 상관없는 관계를 갖게 된다.
    // 이것은 많은 리소스를 소비한다.
    // 구조적 동시성에 의한 부모-자식의 트리 관계를 갖기 위해서 아래 GlobalScope가 아닌 CoroutineScope의 launch를 사용하자.
    runBlocking {

        //GlobalScope.launch {
        launch {
            delay(100)
            println("Launch Thread : ${Thread.currentThread().name}")
        }
        println("RunBlocking Thread : ${Thread.currentThread().name}")
    }


    println("End")

}