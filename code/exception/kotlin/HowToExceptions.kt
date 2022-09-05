fun main() {
    val message = "Hi! There~"
    console(message)

    val str: String = "10008"
    val convertInt: Int = strToInt(str)
    println("message is $convertInt")

    val strValue: String? = "null"

    val checkValue = strValue ?: fail()
    println(checkValue)
    // 밑에 코드는 checkValue에서 fail()이 호출된다면 Nothing이 반환되기 때문에 도달하지 못한다.
    println(test("test"))
}

/**
 * 코틀린은 checked exception을 강제하지 않기 때문에
 * throws InterruptedException를 명시하지 않아도 된다.
 */
fun console(message: String) {
    println("before console")

    Thread.sleep(1000)

    println("message is $message")
}

/**
 * 일반적인 try-catch-finally 형식
 */
fun consoleWithTry(message: String) {
    println("before console")
    try {
        Thread.sleep(1000)
    } catch(e: InterruptedException) {
        println("에러야!")
    } finally {
        println("무조건 실행 finally")
    }
    println("message is $message")
}

fun strToInt(strValue: String) = try {
    strValue.toInt()
} catch(e: Exception) {
    0
}

/*
fun strToInt(strValue: String): Int {
    return try {
        strValue.toInt()
    } catch(e: Exception) {
        0
    }

    /*
    try {
        return strValue.toInt()
    } catch(e: Exception) {
        return 0
    }

    */
}
*/

// 조슈아 블로크의 [이펙티브 자바]의 try-with-resource를 코틀린에서 use를 사용해서 변환한 코드
fun firstLineOfFile(path: String?, defaultVal: String?): String? {
    /*
    try {
        return BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        return defaultVal
    }
    */
    return try {
        BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        defaultVal
    }

}

fun firstLineOfFile(path: String?, defaultVal: String?): String? = try {
    BufferedReader(FileReader(path)).use { br -> return br.readLine() }
} catch (e: IOException) {
    defaultVal
}

// Nothng의 경우에는 타입추론을 사용하면 안된다.
// 일반적으로 Unit의 경우에는 생략을 하게 되는데 만일 생략하게 되면 Unit으로 인식하기 때문에 오류가 발생한다.
fun fail(message: String = "파라미터값을 확인해 보세요."): Nothing = throw IllegalArgumentException(message)

// Nothng의 경우에는 생략하면 안된다.
// 일반적으로 Unit의 경우에는 생략을 하게 되는데 만일 생략하게 되면 Unit으로 인식하기 때문에 오류가 발생한다.
fun failWithBody(message: String = "파라미터값을 확인해 보세요."): Nothing {
    throw IllegalArgumentException(message)
}

// 타입추론에 의해 반환타입이 String이라는 것을 명시해주지 않아도 된다.
fun test(test: String) = test.uppercase()