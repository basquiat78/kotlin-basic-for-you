fun main() {
    val john = returnJohn(true) // John객체를 반환한다.
    //val john = returnJohn() // null을 반환한다.
    // println(john.name) //불가능 하다. returnJohn 메소드의 반환 타입이 nullable 타입이기 때문이다.
    println(john!!.name) // 하지만 단언연산자를 사용한다면 가능하다.
    println(john?.name)

    // 엘비스 헤어를 사용해서 코드를 간결하게 처리하자.
    //john?.let{println(it.name)} ?: println("널 보내며~~~~ 괴로워 하네? 널 떄문에!")
    john?.let{println(it.name)} ?: throw IllegalArgumentException("널 보내며~~~~ 괴로워 하네? 널 떄문에!")
}

/**
 * 예제를 위한 어거지 메소드
 * John는 널일수도 있고 아닐수도 있습니다.
 */
fun returnJohn(areYouJohn: Boolean = false): John? = if(areYouJohn) {
    John("John")
} else {
    null
}

/**
 * John!!!!
 */
class John(
    var name: String
)