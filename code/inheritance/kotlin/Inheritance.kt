/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val bassGuitar = BassGuitar(brandName ="Marleaux", string = 5, origin = "Germany")
    with(bassGuitar) {
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        sound()
        original()
    }

    val bass = Bass(brandName = "Fodera", origin = "USA", string = 5)
    with(bass){
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        original()
        sound()
    }

    val guitar = Guitar(brandName ="Fender", string = 6, origin = "USA")
    with(guitar) {
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        sound()
        original()
        type = InstrumentalType.WIND
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
    }

    val drum = Drum(brandName ="Perl", origin = "USA")
    with(drum) {
        println("brand is $brandName, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        sound()
        original()
    }

    val main = Main("test", "main")
    with(main) {
        play("guitar")
    }

    val parent = Parent()
    // 인자값을 넣어도 된다.
    //val parent = Parent(10000)
    with(parent) {
        println("myIntProp : $myIntProp, myStrProp : $myStrProp")
        myFun()
        myDefaultFun()
        myIntProp = 10000000
        myStrProp = "overrided after"
        println("myIntProp : $myIntProp, myStrProp : $myStrProp")
    }

    val actionGuitar = Guitar(brandName ="Fender", string = 6, origin = "USA")
    with(actionGuitar) {
        movablePerformance()
    }

    val actionDrum = Drum(brandName ="Perl", origin = "USA")
    with(actionDrum) {
        placePerformance()
    }

    val actionBass = BassGuitar(brandName ="Marleaux", string = 5, origin = "USA")
    with(actionBass) {
        // 발을 보고 솔로 포퍼먼스
        placePerformance()

        println("관객의 환호가 좀 더 커진다!!!!")

        movablePerformance()
    }

}

// 무대를 왔다갔다하고 관객 다이빙등 액티브한 퍼포먼스
interface ActionPerformance {

    fun movablePerformance()

    fun beforePerformance() {
        println("관객들의 환호 소리가 들리며 액션 포퍼먼스를 할 준비를 한다.")
    }
}

// 제자리에서 자기 발만보고 퍼포먼스를 한다.
interface PlacePerformance {

    fun placePerformance()

    fun beforePerformance() {
        println("관객들의 환호 소리가 들리며 제자리 포퍼먼스를 할 준비를 한다.")
    }
}

// 객체 생성시 인자 없는 방식으로 생성할 수 있으니
// 인터페이스의 프로퍼티를 상속할 때는 무조건 초기값을 할당해 줘야 한다.
class Parent(override var myIntProp: Int = 0): MyInterface {

    // 구현 상속한 곳에서는 backing field 접근이 가능
    override var myStrProp: String = super.myStrProp
    // 인터페이스는 backing field를 가질 수 없다.
    // 하지만 상속한 하위 클래스에서느느 backing field를 사용할 수 있다.
    // get() {
    //    return field.uppercase()
    //}

    override fun myFun() {
        println("MY")
    }

}

interface MyInterface {

    // backing feild를 지원히자 않는다.
    // 또한 초기화한다면
    // Property initializers are not allowed in interfaces, 즉 인터페이스에서는 프로퍼티를 초기화 할 수 없다.
    val myIntProp: Int
        get() {
            return 0
        }

    // backing feild를 지원히자 않는다.
    // 또한 초기화한다면
    // Property initializers are not allowed in interfaces, 즉 인터페이스에서는 프로퍼티를 초기화 할 수 없다.
    val myStrProp: String
        get() {
            return "before"
        }

    fun myFun()
    fun myDefaultFun() {
        println("default method")
    }
}

// 추상 클래스인 AbstractMain의 바디내부의 프로퍼티도 abstract이다.
// 따라서 일반 클래스와는 달리 구현을 강제화 하기 때문에 클래스와 다르게 다음처럼 생성자안에 강제로 해당 프로퍼티를 override해야한다.
class Main(name: String, override var type: String): AbstractMain(name) {
    override fun play(inst: String) {
        println("type is $type and play $inst")
    }
}

// 추상 클래스
abstract class AbstractMain(
    var name: String
) {
    abstract val type: String
    abstract fun play(inst: String)
}

class Drum(
    brandName: String,
    origin: String,
): Instrumental(brandName, origin), PlacePerformance {
    override fun sound() {
        //super.sound()
        println("드럼은 두구두구두구두구구두")
    }
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}

    override fun placePerformance() {
        super.beforePerformance()
        println("현란한 드럼 스틱 돌리기와 화려한 드러밍 솔로가 관객을 압도하기 시작한다.")
    }
}

class Guitar(
    // 7현 8현기타도 있응께
    var string: Int? = 6,
    brandName: String,
    origin: String
): Instrumental(brandName, origin), ActionPerformance {

    override var type = InstrumentalType.STRING

    override fun sound() {
        //super.sound()
        println("기타는 쫘~~아아앙~~~")
    }
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
    //

    override fun movablePerformance() {
        super.beforePerformance()
        println("기타리스트가 악기를 돌리기도 하고 무대를 종횡무진하며 기타 솔로를 펼친다.")
    }
}

class Bass: Instrumental {

    var string: Int

    constructor(brandName: String, origin: String, string: Int) : super(brandName, origin) {
        this.string = string
    }

    override val type: InstrumentalType = InstrumentalType.STRING

    override fun sound() {
        println("1000만원이 넘는 악기의 고급진 사운드!!!")
    }
}

class BassGuitar(
    var string: Int? = 4,
    brandName: String,
    origin: String,
    override var type: InstrumentalType = InstrumentalType.STRING,
): Instrumental(brandName, origin), ActionPerformance, PlacePerformance {
    override fun sound() {
        //super.sound()
        println("베이스 둥둥둥!!!")
    }

    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}

    override fun movablePerformance() {
        super<ActionPerformance>.beforePerformance()
        println("베이시스트가 악기를 돌리기도 하고 무대를 종횡무진하며 베이스 슬랩를 펼친다.")
    }

    override fun placePerformance() {
        super<PlacePerformance>.beforePerformance()
        println("제자리에서 빠른 핑거링으로 속주 솔로를 펼친다.")
    }

    override fun beforePerformance() {
        super<PlacePerformance>.beforePerformance()
        super<ActionPerformance>.beforePerformance()
    }
}

enum class InstrumentalType{
    STRING,
    WIND,
    PERCUSSION
}

open class Instrumental(
    val brandName: String,
    var origin: String,
) {

    val classType: String = "악기"

    open val type: InstrumentalType = InstrumentalType.PERCUSSION

    open fun sound() {
        println("어떤 소리???")
    }
    fun original() {
        println("open이 없으니 공개하지 않을것이고 override못하지롱")
    }
}
