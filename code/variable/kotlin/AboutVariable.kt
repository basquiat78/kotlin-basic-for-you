fun main() {

    //var str: String = null // Null can not be a value of a non-null type String
    //var longValue: Long = null // Null can not be a value of a non-null type Long
    //var intValue: Int = null // Null can not be a value of a non-null type Int

    var str: String? = null // ?을 통해 nullable하다는 것을 알려준다.
    var longValue: Long? = null // ?을 통해 nullable하다는 것을 알려준다.
    var intValue: Int? = null // ?을 통해 nullable하다는 것을 알려준다.

    var nullableStr: String? = "nullable String"
    testNullable(nullableStr) // 가능
    testNonNull(nullableStr!!) // 단언 연산자 !!을 통해서 널이 아니라고 확신한다면 다음과 같이 사용이 가능하다.
    //testNonNull(nullableStr) // 불가능

    var nonNullStr: String = "non null String"
    testNullable(nonNullStr) // 가능
    testNonNull(nonNullStr) // 가능

    //var initStr: String = "test"
    var initStr = "test" // 타입 추론으로 타입 생략가능
    println(initStr)
    initStr = "change word"
    println(initStr)

    var otherStr =  "test"
    println(otherStr)
    //otherStr = 0 타입 추론으로 타입이 생략되었더라도 치환되는 타입이 다르면 불가능하다.
    //println(otherStr)

    //val a: String = "test"
    val valStr = "test"
    println(valStr)
    // valStr = "change word" // Val cannot be reassigned

    // val은 할당이 되면 변경이 불가능하지만 다음과 같이 지연초기화로 값을 동적으로 활당이 가능하다.
    val lazyinitStr: String
    var flag = "Y"
    //var flag = "N"
    lazyinitStr = if("Y".equals(flag)) {
        "Yes"
    } else {
        "No"
    }
    println(lazyinitStr)
}

fun testNullable(str: String?) {
    println(str)
}

fun testNonNull(str: String) {
    println(str)
}