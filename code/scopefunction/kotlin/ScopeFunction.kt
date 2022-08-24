fun main() {

    val nonNullStr: String = "non null test"
    nonNullStr.let {
        println("str value is $it and str length is ${it.length}")
    }

    var str: String = "test"
    //str.let { s: String -> 
    str.let { s ->
        println("str value is $s and str length is ${s.length}")
        var b: Int = s.length
        //b.let { l: Int -> 
        b.let { l ->
            println("b length is $l")

        }
    }

    // 마지막 코드 라인이 반환된다.
    var firstStr = "테스트 대상"
    var secondStr = firstStr.let {
        println("first Str is $it")
        "테스트 완료"
    }
    println(secondStr)

    // 마지막 코드 라인이 반환되기 때문에 Int타입의 10000이 반환된다.
    var typeStrVar = "스트링입니다."
    var resultInt = typeStrVar.let {
        println("typeStrVar is $it")
        10000
    }
    println(resultInt)

    // this는 생략해서 표현된다. 명시적으로 this를 붙일 수 있다.
    var strForRun = "testistest"
    strForRun.run {
        println(this) // strForRun 그 자신
        println("strForRun length is $length") // 실제로는 this.length가 될것이지만 이것이 생략된다.
    }

    // val name = "Jane" //아래 코드를 테스트 해본 후에는 다음 테스트를 위해 주석처리한다.

    val john = John("John")
    john.run {
        println("john 객체의 이름은 $name") // john 객체의 이름은 Jane로 의도치 않게 이게 되네??
    }
    john.let {
        println("john 객체의 이름은 ${it.name}") // john 객체의 이름은 Jane로 의도치 않게 이게 되네??
    }

    var johnsHelloWordByLet = john.let { it.returnJohnHelloWord() }
    var ageByLet: Int = john.let { it.age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByLet is $johnsHelloWordByLet")
    println("ageByLet is $ageByLet")

    var johnsHelloWordByRun = john.run { returnJohnHelloWord() }
    var ageByRun: Int = john.run { age() } // 타입을 명시적으로 표현해주자
    println("johnsHelloWordByRun is $johnsHelloWordByRun")
    println("ageByRun is $ageByRun")

    // 마치 익명함수 처럼
    val helloWordCombine = run {
        var name = john.name
        var age = john.age()
        var johnsNameWord = "나의 이름은 $name 입니다."
        var johnsAgeWord = "나이는 $age 이죠."
        var hello = "반가워요"
        """$johnsNameWord 
        $johnsAgeWord 
        $hello"""

    }
    println(helloWordCombine)

    with(john) {
        println("john의 이름은 $name")
        println(age())
    }

    // 마지막 코드라인 String이 반환된다.
    val johnName = with(john) {
        "${name}인건가?"
    }
    println(johnName)

    // You 객체를 반환하게 된다.
    val you = with(john) {
        You(yourName = name, yourAge = age())
    }
    you.let{
        println(it.yourName)
        println(it.yourAge)
    }

    //with -> run, let으로도 똑같이 작동하도록 코딩이 가능
    val youByLet = john.let {
        You(yourName = it.name, yourAge = it.age())
    }
    println(youByLet)

    val youByRun = john.run {
        You(yourName = name, yourAge = age())
    }
    println(youByRun)

    // apply와 also는 거의 같은 동작을 한다.
    // 다만 Object reference가 나 자신이냐 it이냐!!!!

    // apply는 마지막 코드라인이 뭐든 그냥 context Object, 즉 자신을 반환하게 된다.
    val me = Me("Basquiat", 100).apply {
        initializeMe()
    }
    println("${me.name} and ${me.age} and $me")

    // also역시 마지막 코드라인이 뭐든 그냥 context Object, 즉 자신을 반환하게 된다.
    val meByAlso = Me("Basquiat", 100).also {
        it.initializeMe()
    }
    println("${meByAlso.name} and ${meByAlso.age} and $meByAlso")

    // 조건이 맞으면 context Object, 즉 자신을 반환하고 아니면 null을 반환다.
    // 따라서 메소드 체이닝을 할 때는 null일수 있기에 safe Call(?.)로 체이닝해야 한다.
    val takeif = Take("notnull").takeIf{ it.value == null }
    println(takeif)
    val takeunless = Take("notnull").takeUnless{ it.value == null }
    println(takeunless)

    // 엘비스 연산자로 null일때 후처리 한다.
    val basquiatTake = Take("basquiat")
    //val basquiatTake = Take("basquiat11111")
    val result = basquiatTake.takeIf{ "basquiat".equals(it.value) }?.let{ "바스키아입니다." } ?: "바스키아가 아닙니다."
    println(result)

}

/**
 * 테스트를 위해 대충 만든 John!!!
 */
class John(
    val name: String
) {
    // 존의 인사말을 스트링으로 반환한다.
    fun returnJohnHelloWord() = "안녕 난 존이라고 해"
    fun age() = 20
}

class You(
    val yourName: String,
    val yourAge: Int
)

class Me(
    var name: String,
    var age: Int,
) {
    // apply, also 테스트를 위해 만든 메소드
    fun initializeMe() {
        this.name = "initialized " + this.name
        this.age = this.age + 5
    }
}

class Take(
    var value: String? = null
)
