import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main() {

    runBlocking {

        // 구조적 동시성의 특징으로 인해 자식 코루틴이 에러를 던지면 부모로 전파된다.
        // 떄론 이런 에러가 자식에 한정짓고 부모로 전파하지 않기 위해 특별한 스코프를 사용한다.
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