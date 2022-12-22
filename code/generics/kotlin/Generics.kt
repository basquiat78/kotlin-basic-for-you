fun main() {

    val generics = Generics(1_000L)
    val generic = generics.getGeneric()


    val list = listOf(1,2,3,4)
    println(list is List)
    println(list is List<Int>)
    // 당연하겠지만 아래 코드는 Cannot check for instance of erased type: List<String>
    //println(list is List<String>)
    println(list is List<*>)

    val stringList = listOf(1,2,3,4) as List<String> // Unchecked cast: List<Int> to List<String>
    val longList = listOf(1,2,3,4) as List<Long> // Unchecked cast: List<Int> to List<Long>

    val value = stringList[0]
    // 하지만 오류가 지연되는 효과로 인해 런타임 시전에서야 ClassCastException이 발생한다.
    println(value) // Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String

    // 배열 및 가변 컬렉션
    val strArray = arrayOf("a", "b", "c")
    //val anyArray: Array<Any> = strArray // Type mismatch.

    val mutableList = mutableListOf("a", "b", "c")
    //val mList: MutableList<Any> = mutableList // Type mismatch.

    val mutableSet = mutableSetOf("A", "B")
    //val mutableAnySet: MutableSet<Any> = mutableSet // Type mismatch.

    val stringGenerics = Generics("")
    //val anyGenerics: Generics<Any> = stringGenerics // Type mismatch.

    // 불변 컬렉션
    val immutableList = listOf("a", "b", "c")
    val imList: List<Any> = immutableList

    val immutableSet = setOf("A", "B")
    val immutableAnySet: Set<Any> = mutableSet

    /**
     [이펙티브 자바]를 흉내낸 코드
    val stringGenerics = Generics<String>()
    val anyGenerics: Generics<Any> = stringGenerics     // (1)
    anyGenerics.addGeneric(1_000L)                      // (2)
    stringGenerics.getGenerics().first()                // (3)
    */

    val strArray = arrayOf("a", "b", "c")
    // 배열은 자바에서 공변적인데 코틀린에서는 성립하지 않는다. 이유는??
    //val anyArray: Array<Any> = strArray // type mismatch

    val intRootGen = Generics<Number>()
    intRootGen.addGeneric(1)
    intRootGen.addGeneric(2)

    val intSubGen = Generics<Int>()
    intSubGen.addGeneric(3)
    intSubGen.addGeneric(4)
    // Int는 Number의 하위 타입이라는 것을 공변적으로 만든 제네릭 함수를 활용
    intRootGen.addSubGenerics(intSubGen)
    println(intRootGen.getGenerics())

    // Number가 마치 Int의 하위 타입인것처럼 작동하도록 반공변적으로 만든 제네릭 함수를 활용
    intSubGen.addToRoot(intRootGen)
    println(intRootGen.getGenerics())

    val myProjection = StringConsumer()
    myProjection.consume("ok this is right!!!")
    // 스타 프로젝션
    val starProjection: StarProjection<*> = myProjection
    // type mismatch
    //starProjection.consume("test")
    //Unchecked cast: StarProjection<*> to StarProjection<Int>라는 경고만 보여줄 뿐 컴파일 에러는 발생하지 않는다.
    val castProjection = starProjection as StarProjection<Int>
    castProjection.consume(111111)
}

interface StarProjection<in T> {
    fun consume(_value: T)
}

class StringConsumer: StarProjection<String> {
    override fun consume(_value: String) {
        println(_value.uppercase())
    }
}

class Generics<T: Number>(
    generic: T,
)
// 자바와는 다르게 상위 바운드는 하나만 가능하고 여러 개의 바운드 제약을 위해서는
// where라는 키워드로 아래와 같이 구분자 ','를 통해서 하나씩 거는 방식을 채택한다.
// where T: AnyInterface<T>, T: OtherInterface<T>
{
    //private val generic: T = generic

    //fun getGeneric(): T = this.generic

    private val genericList = mutableListOf<T>()

    fun getGenerics(): List<T> {
        return genericList
    }

    fun addGeneric(element: T) {
        genericList.add(element)
    }

    /*
    fun addSubGenerics(generics: Generics<T>) {
        generics.getGenerics().forEach { genericList.add(it) }
    }*/

    // 사용 지점 변성을 통해 공변적으로 만든다.
    fun addSubGenerics(generics: Generics<out T>) {
        generics.getGenerics().forEach { addGeneric(it) }
    }

    /*
    // 물론 이렇게 바운드 제약으로 하위 타입 관계를 선언해도 공변적으로 만들 수 있다.
    fun <U: T> addSubGenerics(generics: Generics<U>) {
        generics.getGenerics().forEach { genericList.add(it) }
    }*/

    fun addToRoot(root: Generics<in T>) {
        root.addSubGenerics(this)
    }

}

class ConcreteType<T>(
    data: T,
//): Normal(data) // One type argument expected for class Normal<T>
): Normal<T>(data)

open class Normal<T>(
    val data: T,
)

/*
//ConcreteType이 Nomral을 상속한다 해서 Normal의 파라미터 타입을 똑같이 T로 둘 필요는 없다.
//이유는 서로 다른 선언이기 때문이다.
open class Normal<U>(
    val data: U,
)
*/