import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis

/**
 *
 * 가장 기본적인 코루틴 빌더 launch를 사용해 본다.
 *
 */
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

    // 하지만 위 코드는 자식 코루틴이 실행되기 전에 부모 스레드가 종료되면서
    // 자식 코루틴도 동시에 종료되기 때문에 로그를 찍지 못한다.
    // 따라서 자식 코루틴이 실행될 수 있도록 sleep을 걸거나 일시 중단 함수인 delay를 준다.
    //Thread.sleep(500)
    //delay(500)

}