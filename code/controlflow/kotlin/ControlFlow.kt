import java.math.BigDecimal

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    /*
     	val str = "test"
        val value = "test".equals(str) ? 10 : 5
        println(value)
        error msg -> Unexpected tokens (use ';' to separate expressions on the same line)
    */

    val str = "test"
    var oldFashioned = 0
    // 전통적인 방식으로 작성해 본다.
    if("test".equals(str)) {
        oldFashioned = 10
    } else {
        oldFashioned = 5
    }
    println("oldFashioned is $oldFashioned")

    //val value = if("test".equals(str)) {
    //    10
    //} else {
    //    5
    //}
    // 코드를 더 간략하게
    val kotinStyle = if("test".equals(str)) 10 else 5
    println("kotinStyle is $kotinStyle")

    // if else.. else if
    val num = 4
    val numCheckResult: String = if(num > 10)
        "5보다 큰 숫자가 왔네요"
    else if( num >= 5 && num <= 10)
        "5와 10사이의 숫자가 옴"
    else
        "5보다 작은 숫자가 옴"

    println("num check result is $numCheckResult")

    // 물론 이렇게 takeIf/takeUnless를 이용해서
    // '널 안전성'을 활용해 삼항 연산자처럼 비스무리하게 표현할 수 있다.
    // 하지만 if expression과 비교한다면 좀 오버하는 느낌이 든다.
    var takeIfStr = "test"
    //var takeIfStr = "test11"
    val valueTakeIfStr = takeIfStr.takeIf{ "test".equals(it) }?.let{ 10 } ?: 5
    println("valueTakeIfStr is $valueTakeIfStr")

    // old fashioned
    // 코드 짜기 귀찮아서 그냥..uppercase()
    val genre = "jazz"
    //val genre = "jazz123456"
    var upperCase: String = ""
    when(genre) {
        "jazz"   -> upperCase = genre.uppercase()
        "rock"   -> upperCase = genre.uppercase()
        "hiphop" -> upperCase = genre.uppercase()
        else -> upperCase = "ETC"
    }
    println("old fashioned upperCase is $upperCase")

    // 리턴하는 when 표현식
    var upperCaseGenre = when(genre) {
        "jazz"   -> genre.uppercase()
        "rock"   -> genre.uppercase()
        "hiphop" -> genre.uppercase()
        else -> "ETC"
    }
    println("kotinStyle is $upperCaseGenre")

    // 자바에서 가장 흔히 사용하는 열거형 enum으로 구현
    // 다만 자바처럼
    // when(genreCode) {
    //    JAZZ -> "재애즈"
    // }
    // 같이 사용할 수 없다.
    var genreCode = Genre.ROCK
    var result = when(genreCode) {
        Genre.JAZZ -> "째애즈"
        Genre.ROCK -> "롹!"
        Genre.HIPHOP -> "랩!!!!"
        else -> "기타 등등"
    }
    println("kotinStyle using ENUM is $result")

    // 자바처럼 몇 개의 case를 묶어서 처리하는 방식
    var otherResult = when(genreCode) {
        Genre.JAZZ, Genre.ROCK, Genre.HIPHOP -> "대중 음악"
        else -> "월드 뮤직"
    }
    println("kotinStyle using ENUM is $otherResult")

    /*
        이건 안되는 걸로...
        for(i: Int = 0 ; i < 10; i++) {
            println(i)
        }
    */
    // 1부터 10까지
    for(index in 1..10) {
        println("index is $index")
    }

    // api를 이용 -> 1부터 10까지
    for(index in 1.rangeTo(10)) {
        println("index is $index")
    }

    // until의 경우에는 (index = 0; index < 11; i++)
    // 즉 index는 10까지 찍힌다.
    for(index in 1 until 11) {
        println("index is $index")
    }

    // 난 홀수만 찍고 싶은데?
    for(index in 1 until 11 step 2) {
        println("index is $index")
    }

    // Char 위와 동일
    for(char in 'a'..'f') {
        println("char is $char")
    }

    for(char in 'a'.rangeTo('f')) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'a' until 'g' step 2) {
        println("char is $char")
    }

    // 한글은 안되는 걸로
    for(char in 'ㄱ' until 'ㄹ') {
        println("char is $char")
    }

    for(char in 'A'..'F') {
        println("char is $char")
    }

    for(char in 'A'.rangeTo('F')) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'A' until 'G' step 2) {
        println("char is $char")
    }

    /*
        택도 없죠?
        코드가 동작조차 않하죠?
        for(index in 10..1) {
            println("index is $index")
        }
    */
    // 10부터 5까지
    for(index in 10 downTo 5) {
        println("index is $index")
    }

    // z to s
    for(char in 'z' downTo 's') {
        println("char is $char")
    }

    // Z to S
    for(char in 'Z' downTo 'S') {
        println("char is $char")
    }

    // 10부터 5까지
    for(index in 10.downTo(5)) {
        println("index is $index")
    }

    // z to s
    for(char in 'z'.downTo('s')) {
        println("char is $char")
    }

    // Z to S
    for(char in 'Z'.downTo('S')) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'A'.until('G').step(2)) {
        println("char is $char")
    }

    // 퐁당퐁당
    for(char in 'a'.rangeTo('f').step(2)) {
        println("char is $char")
    }

    // 난 홀수만 찍고 싶은데?
    for(index in 1.until(11).step(2)) {
        println("index is $index")
    }

    val array = arrayOf(1, 2, 3, 4, 5, 6)
    for(intValue in array) {
        println("intValue is $intValue")
    }

    // 역으로 소팅해서
    for(intValue in array.reversed()) {
        println("intValue is $intValue")
    }

    val list = arrayListOf(Person("John", 22), Person("Jane", 21))
    for(person in list) {
        // 앞서 배운 스코프 함수 let도 써보고 하자
        person.let {
            println("name is ${it.name} and age is ${it.age}")
        }
    }

    var whileIdx = 0;
    // 5보다 작을 떄까지 돌아라!
    while(whileIdx < 5) {
        println("whileIdx is $whileIdx")
        whileIdx++
    }

    var startIdx = 5;
    // 5부터 시작해서 0이 될때까지 돌아라
    while(startIdx > 0) {
        println("startIdx is $startIdx")
        startIdx--
    }

    var doIndex = 0;
    // 5보다 작을 떄까지 돌아라!
    do {
        println("doIndex is $doIndex")
        doIndex++
    } while(doIndex < 5)

    var doStartIndex = 5;
    // 5부터 시작해서 0이 될때까지 돌아라
    do {
        println("doStartIndex is $doStartIndex")
        doStartIndex--
    } while(doStartIndex > 0)

    val stopIndex = 3
    // index가 3이 되면 3까지 찍고 루프를 빠져나오기 때문에 3까지만 찍힐 것이다.
    for(index in 1..10) {
        println("index is $index")
        if(index == stopIndex) {
            break;
        }
    }

    // break가 걸리면 전체 루프를 빠져나올줄 알았지?? 응 아니야!
    for(idx in 1..4) {
        println("idx is $idx")
        for(jdx in 1..4) {
            println("idx = $idx / jdx = $jdx")
            if(idx == 2) {
                break
            }
        }
    }

    // 상위 바깥에 있는 for에 이 녀석은 outerLoop라고 라벨링을 한다.
    outerLoop@ for(idx in 1..4) {
        println("label idx is $idx")
        // 루프 안쪽에 중첩 루프에는 innerLoop라고 라벨링을 한다.
        innerLoop@ for(jdx in 1..4) {
            println("label idx = $idx / label jdx = $jdx")
            if(idx == 2) {
                // break가 걸리면 sign된 라벨로 전파한다.
                break@outerLoop
            }
        }
    }

}

class Person(
    val name: String,
    val age: Int,
)

/**
 * 음악 장르
 */
enum class Genre {
    JAZZ,
    ROCK,
    HIPHOP,
    ETC
}
