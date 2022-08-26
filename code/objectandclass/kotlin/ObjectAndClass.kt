fun main() {

    var basquiatNo = Basquiat()
    basquiatNo.name = "john"
    basquiatNo.age = 20
    with(basquiatNo) {
        println("basquiat info : $name | $age")
    }

    val basquiat = Basquiat("basquiat", 46)
    with(basquiat) {
        println("basquiat info : $name | $age")
    }

    val BasquiatWithC = BasquiatWithC("basquiat", 46)
    with(BasquiatWithC) {
        println("basquiat info : $name | $age")
    }

    val test = Test("test", 10)
    with(test) {
        println("test info : $name | $age")
    }

    val test1 = Test()
    with(test1) {
        println("test info : $name | $age")
    }

    val test2 = Test(10)
    with(test2) {
        println("test info : $name | $age")
    }

    val musician1 = JazzMusician("John Coltrane")
    println("musician1 name is ${musician1.name} and genre is ${musician1.genre}")
    //musician1.name = "Charlie Parker" // val이기 때문에 불가능

    val musician2 = JazzMusicianVar("John Coltrane", 30)
    println("musician2 name is ${musician2.name}, age ${musician2.age} and genre is ${musician2.genre}")
    musician2.name = "Charlie Parker"
    // Cannot assign to 'genre': the setter is private in 'JazzMusicianVar'
    musician2.genre = "Jazz"
    musician2.age = 34
    // musician2.changeGenre("Jazz")
    println("musician2 name is ${musician2.name}, age ${musician2.age} and genre is ${musician2.genre}")

    val musician3 = JazzMusicianProperty("Jojn Coltane", 134)
    println("musician3 name is ${musician3.name}, age ${musician3.age}")
    // 오타 수정
    //Cannot access '_name': it is private in 'JazzMusicianProperty'
    //Cannot access '_age': it is private in 'JazzMusicianProperty'
    //musician3._name = "John Coltrane"
    //musician3._age = 34

    //Cannot assign to 'name': the setter is private in 'JazzMusicianProperty'
    //Cannot assign to 'age': the setter is private in 'JazzMusicianProperty'
    //musician3.name = "John Coltrane"
    //musician3.age = 34

    musician3.changeName("John Coltrane")
    musician3.changeAge(34)

    println("musician3 name is ${musician3.name}, age ${musician3.age}")

    var initClass = InitClass("test")
    println("initCalss call init, after ${initClass.name}")
}

class InitClass(
    var name: String
) {
    init {
        println("init name is $name")
        this.name = name.uppercase()
    }
}

class JazzMusicianProperty(
    private var _name: String,
    private var _age: Int,
) {
    var name = _name
        private set
    var age = _age
        private set

    fun changeName(_name: String) {
        this.name = _name
    }

    fun changeAge(_age: Int) {
        this.age = _age
    }
}

class JazzMusicianVar(
    var name: String,
    var age: Int,
) {


    var genre: String? = null
        //	private set
        //fun changeGenre(_genre: String?) {
        //	this.genre = _genre
        //}
        set(_genre) {
            // 전부 대문자로
            // 이건 stackoverflow 에러 발생
            // this.genre = _genre?.uppercase()
            // null일수 있으니 이 경우에는 safe call을 하고 그냥 변수만 세팅하면 _genre를 field에 할당하면 된다.
            field = _genre?.uppercase()
        }
}

class JazzMusician(
    val name: String,
) {
    val genre: String = "JAZZ"
        get() {
            return field.lowercase()
        }
}

class Test(
    //var name: String,
    //var age: Int
    var name: String?,
    var age: Int?,
) {
    constructor(): this(null, null)
    constructor(_age: Int): this(name = null, age = _age)
    //위 코드는 밑에서처럼 작성이 가능하다. 어떤 특정한 로직을 처리한다면 밑에 방식으로
    //constructor(_age: Int): this() {
    //	this.name = null
    //  this.age = _age
    //}


    // 이건 주성생자가 있기 때문에 이렇게 하면 Primary constructor call expected에러
    // 따라서 this를 통해 주생성자로 위임해야한다.
    //constructor(_age: Int) {
    //    this.name = null
    //    this.age = _age
    //}
}

class Basquiat {

    var name: String? = null

    var age: Int? = null

    constructor(_name: String, _age: Int) {
        this.name = _name
        this.age = _age
    }

    constructor() {}

}

// primary 생성자의 경우 constructor
// 특별한 어노테이션이나 private하게 만들지 않는 이상 생략가능
//class BasquiatWithC constructor (
class BasquiatWithC(
    var name: String,
    var age: Int,
)