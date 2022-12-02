lateinit var top: String

fun initTop() {
    top = "TOP PROPERTY"
}

fun main() {

    val musician = Musician()
    println(musician.genreInitialized())
    musician.genre = "Jazz"
    println(musician.genreInitialized())
    println(musician.genre)

    initTop()

    println(top)

    val test = Test()

    println(test.code)
    println(test.code)
    println(test.code)

    val member = Member(1000)
    member.name = "Bas"
    member.name = "Bas!"
    member.name = "Bas!!"

    val custom = Custom(100)
    println(custom.name)
    custom.name = "TEST"
    println(custom.name)
    custom.name = "WORLD"
    println(custom.name)
    custom.name = "COMPLETED"
    println(custom.name)

}

class Musician {
    lateinit var genre: String
    fun genreInitialized(): Boolean = this::genre.isInitialized
}

class Test {

    // 고전적인 자바의 synchronized 방식
    //private var lock = Any()
    //val code: String by lazy(lock) { callAPI() }

    val code: String by lazy { callAPI() }
    //val code: String by lazy(LazyThreadSafetyMode.PUBLICATION) { callAPI() }
    //val code: String by lazy(LazyThreadSafetyMode.NONE) { callAPI() }

    private fun callAPI(): String {
        println("call api")
        return "my code"
    }
}

class Member(
    val id: Long? = null,
) {
    var name: String by observable("init") {_, prev, new ->
        changeProperties(prev, new)
    }

    private fun changeProperties(prev: String, new: String) {
        println("Member Id : ${this.id}, $prev changed to $new ")
    }

}

class Custom(
    val id: Long? = null,
) {
    var name: String by observerAndVeto(
        "Jazz",
        changeProperty()) { _, _, new ->
        validProperty(new)
    }

    private fun changeProperty() = { _: KProperty<*>, prev: String, new: String ->
        println("Member Id : ${this.id}, $prev changed to $new ")
    }

    private fun validProperty(new: String): Boolean {
        return new != "WORLD"
    }

}

inline fun <T> observerAndVeto(initialValue: T,
                               crossinline onChange : (property: KProperty<*>, oldValue: T, newValue: T) -> Unit,
                               crossinline onValid: (property: KProperty<*>, oldValue: T, newValue: T) -> Boolean): ReadWriteProperty<Any?, T> =
object : ObservableProperty<T>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(property, oldValue, newValue)
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean = onValid(property, oldValue, newValue)
}