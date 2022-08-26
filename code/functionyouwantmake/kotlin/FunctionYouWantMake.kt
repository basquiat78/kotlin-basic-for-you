/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */

// top level에 선언해 보자
// null이면 0을 반환하자
//fun Int?.ifNullZero() = this?.let { it } ?: 0

// Generic하게 만들어 보자.
fun <T: Number> T?.ifNullZero() = this?.let { it } ?: 0


fun main() {

    //fun test(): Unit {
    fun test() {
        println("되니?")
    }

    hello()
    test()
    //innerHello() 아~ 안돼!
    //hello().innerHello() 이렇게 해보고 싶은 욕망이 막 솟구쳐 올랐을거야! 어림도 없다!
    helloWithParam("거기 누구 있어요?")
    helloWithParam()

    val normalStr = getStr()
    println("normalStr is $normalStr")
    var strExpression = getStrExpression()
    println("strExpression is $strExpression")

    val sum = sum(1, 1)
    println("sum is $sum")

    val sumex = sumEx(1, 1)
    println("sumex is $sumex")


    val target = "test"
    var destination = "TeST"
    if(target.equals(destination)) {
        println("match")
    } else {
        println("not match")
    }
    // equals의 스펙은 ignoreCase를 default와 Named Arguments가 적용된다.
    if(target.equals(destination, false)) {
        println("match")
    } else {
        println("not match")
    }

    // equals의 스펙은 ignoreCase를 default와 Named Arguments가 적용된다.
    // public actual fun String?.equals(other: String?, ignoreCase: Boolean = false): Boolean
    // 따라서 false를 생략하면 대소문자 구분
    if(target.equals(destination, false)) {
        println("match")
    } else {
        println("not match")
    }

    // Named Arguments은 순서에 상관없이 함수에 선언된 변수명으로 접근해서 넣을 수 있다.
    if(target.equals(ignoreCase = false, other = destination)) {
        println("match")
    } else {
        println("not match")
    }

    // Named Arguments 생략
    if(target.equals(destination, true)) {
        println("match")
    } else {
        println("not match")
    }

    // 명시적으로 사용한다.
    if(target.equals(destination, ignoreCase = true)) {
        println("match")
    } else {
        println("not match")
    }

    // Named Arguments를 사용하면 순서 상관없다.
    // 단 처음 들어오는 파라미터에 Named Arguments를 적용한다면 뒤에 들어오는 파라미터는 Named Arguments로 접근해야 한다.
    if(target.equals(ignoreCase = true, other = destination)) {
        // if(target.equals(ignoreCase = true, destination)) { 이건 안된다.
        println("match")
    } else {
        println("not match")
    }

    // 메세지가 없다면 이럴 때는 isUpper에 대해서 명시적으로 Named Arguments로 설정해 줘야 한다.
    helloWithTwoParam(isUpper = true)
    // 아무것도 없으면 default로 any body here? 찍힌다
    helloWithTwoParam()
    helloWithTwoParam("how are you? i'm fine")
    helloWithTwoParam("how are you? i'm fine", true)
    helloWithTwoParam("how are you? i'm fine", isUpper = true)
    helloWithTwoParam(isUpper = true, message = "how are you? i'm fine")
    // helloWithTwoParam(isUpper = true, "how are you? i'm fine") 이거 안된다~


    val trueMe = true
    val falseMe = false

    println("trueMe || falseMe is ${trueMe || falseMe}") // true
    println("trueMe or falseMe is ${trueMe or falseMe}") // true
    println("trueMe or falseMe is ${trueMe.or(falseMe)}") // true
    println("trueMe && falseMe is ${trueMe && falseMe}") // false
    println("trueMe and falseMe is ${trueMe and falseMe}") // false
    println("trueMe and falseMe is ${trueMe.and(falseMe)}") // false

    val hello = "hello"
    val heLLo = "heLLo"
    //val heLLo = "heLLo111"
    println("hello same heLLo is ${hello sameTo heLLo}")
    println("hello same heLLo is ${hello.sameTo(heLLo)}")

    val testInt: Int? = null
    println("what is testInt? -> $testInt")
    println("what is testInt? -> ${testInt.ifNullZero()}")

    val testLong: Long? = null
    println("what is testLong? -> $testLong")
    println("what is testLong? -> ${testLong.ifNullZero()}")

    val testFloat: Float? = null
    println("what is testFloat? -> $testFloat")
    println("what is testFloat? -> ${testFloat.ifNullZero()}")

    val testDouble: Double? = null
    println("what is testDouble? -> $testDouble")
    println("what is testDouble? -> ${testDouble.ifNullZero()}")

    val testShort: Short? = null
    println("what is testShort? -> $testShort")
    println("what is testShort? -> ${testShort.ifNullZero()}")

}

// 문자열 타입을 반환하는 일반적인 형식의 함수
// {} -> body가 있는 경우에는 Unit이 아니라면 타입을 명시해줘야 한다.
fun getStr(): String {
    return "string"
}

// 문자열 타입을 반환하는 표현식 방식으로 타입 추론이 가능하면 반환 타입 생략가능
// return은 생략해야 한다.
fun getStrExpression() = "string expression"

// Int 타입도 마찬가지
fun sum(a: Int, b: Int): Int {
    return a + b
}

// 표현식
fun sumEx(a: Int, b: Int) = a + b

// 표현식으로 간략하게 만들어보자
infix fun String.sameTo(to: String) = (this == to) or this.equals(to, ignoreCase = true)

// 변수를 받자
// fun helloWithParam(message: String) {
fun helloWithParam(message: String = "거기 아무도 없어요?") {
    println(message)
}

// 두개의 변수를 받자
//
fun helloWithTwoParam(message: String = "any body here?", isUpper: Boolean = false) {
    if(isUpper) println(message.uppercase()) else println(message)

}

//fun hello(): Unit {
fun hello() {
    println("안녕~ 난 코틀린이라고 해")
    //fun innerHello() {
    fun innerHello(): Unit {
        println("반가워")
    }
    innerHello()
}
