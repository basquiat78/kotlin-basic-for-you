fun main() {

    println(sum(1,2,3,4))

    val intArray = intArrayOf(1,2,3)
    // Type mismatch: inferred type is IntArray but Int was expected
    //println(sum(intArray))
    println(sum(1,2,3,4))
    println(sum(*intArray))

    // vararg는 얕은 복사가 된다.
    val arrayInt = intArrayOf(3,2,1)
    operateSort(*arrayInt)
    println(arrayInt.contentToString())

    val a = intArrayOf(1,2,3)
    val b = intArrayOf(4,5,6)
    val intArrays = arrayOf(a, b)
    shallowCopy(*intArrays)
    println(a.contentToString())
    println(b.contentToString())

    // 스프레드 연산자와 혼용해서 사용이 가능
    // 하나의 배열로 합쳐 작동하게 된다.
    val aa = intArrayOf(1, 2, 3)
    val bb = 10
    println("result is ${sum(10, 20, *aa, bb)}")

    val values = intArrayOf(1, 2, 3)
    val name = "Basquiat"
    val long: Long = 1_000_000

    // 에러 발생 뒤에 오는 name과 long도 vararg로 간주해버리기 때문
    //varargPrint(*values, name, long)
    // 위 에러를 회피하기 위해서는 named parameter를 사용하자.
    // vararg를 named parameter로 넘길 때는 스프레드 연산자 '*'를 생략해도 된다.
    //varargPrint(values = values, name = name, long = long)
    varargPrint(*values, name = name, long = long)


    // named parameter에 vararg 방식으로 작성하는 것은 통하지 않는다.
    varargPrint(name = name, long = long, values = 1, 3, 4, 5)

    // 오버로딩 해소에 대한 side-effect
    stringPrintln("a")
    stringPrintln("a", "b")
    stringPrintln("a", "b", "c")

    val str = "A"
    println(validUpperCase(str) { validation(it) })
    // callable reference로 더 간결하게
    println(validUpperCase(str, ::validation))

    // 일반적인 방식
    val charlieParker = Musician("Charlie Parker", 34)

    // Callable Reference로 치환해서 사용할 수 있다.

    val createMusician = ::Musician
    // 락밴드 KISS의 혀가 엄청 긴 리더이자 베이시스트
    val geneSimmons = createMusician("Gene Simmons",73)

    // bound callable reference
    val isJazz1 = charlieParker::isJazzMusician
    println("Charlie Parker is Jazz Musician? -> ${isJazz1("Jazz")}")

    val isJazz2 = geneSimmons::isJazzMusician
    println("Charlie Parker is Jazz Musician? -> ${isJazz2("Rock")}")
}

fun sum(vararg values: Int): Int {
    return values.sum()
}

fun operateSort(vararg values: Int) {
    values.sort()
    println(values.contentToString())
}

fun shallowCopy(vararg values: IntArray) {
    values[0][0] = 7
}

fun varargPrint(vararg values: Int, name: String, long: Long) {
    values.forEach {
        println("$it")
    }

    println("name is $name")
    println("long is $long")

}

fun stringPrintln(vararg values: String) {
    values.forEach {
        println("value is : $it")
    }
}

fun stringPrintln(str: String, value: String, item: String) {
    println("$str | $value | $item")

}

fun validUpperCase(str: String, validation: (String) -> Boolean): Boolean {
    return validation(str)
}

fun validation(str: String): Boolean {
    return str.first().isUpperCase()
}

data class Musician(
    val name: String,
    val age: Int,
) {

    fun isJazzMusician(genre: String): Boolean = genre == "Jazz"

}