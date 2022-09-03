/**
 * 연주자가 있고 연주자에게는 이름이 있다.
 */
sealed class Player(val name: String)

class Vocalist(name: String): Player(name)
class Keyboardist(name: String): Player(name)
class Guitarist(name: String): Player(name)
class Bassist(name: String): Player(name)
class Drummer(name: String): Player(name)

/**
 * 밴드는 싱글톤 객체로 만든다.
 */
object Band {
    // 상속을 했으니 당연히 is-a관계
    // 현재 코틀린의 Collection framework를 배우진 않았지만
    // mutable list로 만든다.
    private val members = mutableListOf<Player>()

    // 오디션을 통과한 멤버를 영입한다.
    fun addMember(player: Player) = members.add(player)

    // 멤버 리스트를 가져온다.
    fun members() = members
}

fun main() {
    // 밴드에 멤버들을 추가한다.
    Band.addMember(Keyboardist("Chick Corea"))
    Band.addMember(Bassist("John Pattitucci"))
    Band.addMember(Vocalist("Bobby McFerrin"))
    Band.addMember(Guitarist("Frank Gambale"))
    Band.addMember(Drummer("Dave Weckl"))

    // 밴드가 완성되고 하나씩 찍어보자
    // it이지만 player라고 명시적으로 이름을 주고 람다식으로 표현함
    Band.members().forEach { player ->
        /*
         when using as a Statement
        when(it) {
            is Keyboardist -> println("Keyboardist")
            is Drummer -> println("Drummer")
        }
        */
        /*
        val playerType = when(player) {
            is Keyboardist -> "Keyboardist"
            is Drummer -> "Drummer"
            is Vocalist -> "Keyboardist"
            is Guitarist -> "Drummer"
            is Bassist -> "Drummer"
            else -> "who are you?" // 이게 전부인데 왜 이걸 또?
        }
        println("playerType : $playerType")
        */
        // 밑에 is중 하나라도 주석처리해보자. add else branch가 뜰것이다.
        val playerName = when(player) {
            is Keyboardist -> player.name // ((Keyboardist) player).name 이렇게 안하는게 어디냐?
            is Drummer -> player.name
            is Vocalist -> player.name
            is Guitarist -> player.name
            is Bassist -> player.name
        }
        println("playerName : $playerName")
    }
}