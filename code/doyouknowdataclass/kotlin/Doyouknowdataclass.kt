/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {

    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    //val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG)
    with(tshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("tshirts is ${toString()}")
    }

    val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 105)
    //val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG)
    with(sameTshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("sameTshirts is ${toString()}")
    }

    println("true or false? : ${tshirts.equals(sameTshirts)}")

    // 통채로 복사
    val copied = tshirts.copy()
    with(copied) {
        println("brand: $brand, type: $type, size : $size")
        println("copied is ${toString()}")
    }

    val copiedSizeChange = tshirts.copy(size = 200)
    with(copiedSizeChange) {
        println("brand: $brand, type: $type, size : $size")
        println("copiedSizeChange is ${toString()}")
    }

    val copiedTypeChange = tshirts.copy(type = TshirtsType.HOOD_SHORT)
    with(copiedTypeChange) {
        println("brand: $brand, type: $type, size : $size")
        println("copiedTypeChange is ${toString()}")
    }

    val copiedMultipleVal = tshirts.copy(brand = "ADIDAS", size = 110)
    with(copiedMultipleVal) {
        println("brand: $brand, type: $type, size : $size")
        println("copiedTypeChange is ${toString()}")
    }

    val entity = TshirtsEntity("NIKE", TshirtsType.HOOD_LONG, 100)

    //Companion을 명시적으로 사용할 수 있다.
    //val tshirts = Tshirts.Companion.of(entity)

    // Companion은 이름을 줄 수 있다.
    // 하지만 이름을 줘도 생략해서 사용이 가능하다.
    //val tshirts = Tshirts.TshirtsCompanion.of(entity)
    val entitiyToTshirts = Tshirts.of(entity)
    with(entitiyToTshirts) {
        println(toString())
    }

    //val numStr = "100aaa"
    val numStr = "100"

    //실제 개발할 때는 import를 통해서 생략도 할 수 있다.
    //val number: Int? = stringToInt(numStr)
    val number = CommonUtils.stringToInt(numStr)
    println(number)

}

object CommonUtils {
    // 굳이 만들 이유가 없지만 예제긴 한데 좋은 코드는 아니라고 생각한다.
    // 잘못된 넘버타입의 스트링이 넘어왔다면
    // 0이나 null을 반환하는게 아니라 NumberFormatException를 통해
    // 잘못된 정보가 넘어왔는지 알려줘야하지 않을까??
    fun stringToInt(str: String) = str.toIntOrNull() ?: 0
}

/**
 * 긴팔 라운드/반팔 라운드도 있겠지만
 * 단순하게 티셔츠의 타입을 정의한 클래스
 */
enum class TshirtsType(val description: String) {
    SHORT("반팔티"),
    LONG("긴팔티"),
    HOOD_SHORT("반팔 후드티"),
    HOOD_LONG("긴팔 후드티")
}

class TshirtsEntity(
    val brand: String,
    val type: TshirtsType,
    val size: Int,
)

data class Tshirts(
    val brand: String,
    val type: TshirtsType,
    val size: Int,
) {

    // 이름을 줘볼까????
    //companion object TshirtsCompanion {
    companion object {
        fun of(entity: TshirtsEntity) = with(entity) {
            Tshirts(brand = brand, type = type, size = size)
        }
    }

}

/*
data class Tshirts(
    val brand: String,
    val type: TshirtsType,
) {

    var size: Int = 0
    	private set

    fun sizeOf(_size: Int) {
        this.size = _size
    }

}
*/