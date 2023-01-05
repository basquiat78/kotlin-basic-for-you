import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 *
 * 코루틴 빌더 async를 사용해 본다.
 *
 */
suspend fun main() {

    val hello = GlobalScope.async {
        delay(100)
        "안녕~"
    }

    val message = GlobalScope.async {
        delay(100)
        "반가워!"
    }

    // 형태만 다를 뿐, 마치 모던 자바스크립트의 async, await와 같은 동작을 보여준다.
    // 또한 자바스크립트에서 await를 호출하기 위해서는 해당 함수는 async함수여야한다.
    // 코틀린도 마찬가지로 이미 설명했듯이 일반 함수는 일시 중단 함수를 호출할 수 없고 일시 중단 함수는 둘 다 호출이 가능하다.
    val result = "${hello.await()} ${message.await()}"
    println(result)

}