interface Player {
    val name: String
    fun play()
    fun actionPerformance()
}

class Bassist(
    override val name: String
): Player {

    override fun play() {
        println("둥둥둥둥 슬랩 촤라랍촵촵 쿠당 둥땅 띠디딩")
    }

    override fun actionPerformance() {
        println("자기의 발을 보고 열심히 베이스를 팅기다가 베이스 악기 돌리기!")
    }
}

class Guitarist(
    override val name: String
): Player {

    override fun play() {
        println("손가락이 보이지 않는 빠른 속주와 펑키 리듬 충만 만땅 느낌 필!!!!")
    }

    override fun actionPerformance() {
        println("여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!")
    }

}

//class BandLeader: Player by Guitarist()
/*
class BandLeader(
    private val player: Player
) {
    val name = player.name

    fun play() {
        println("밴드 리더 ${player.name} 연주 시작")
        player.play()
    }
    fun actionPerformance() {
        println("밴드 리더 ${player.name} 포퍼먼스 시작")
        player.actionPerformance()
    }
}
*/
//class BandLeader(override val name: String): Player by Guitarist(name)

class BandLeader(
    private val player: Player
    //var player: Player
): Player by player {
    override fun play() {
        println("밴드리더 ${player.name}의 연주가 시작됩니다.")
        player.play()
    }
}

fun Player.leader(): Player {
    // 확장 함수 내부의 this는 reciever type
    return BandLeader(this)
}

fun Player.leader() = object : Player by this {
    override fun play() {
        println("밴드리더 ${this@leader.name}의 연주가 시작됩니다.")
        this@leader.play()
    }
}

/*
data class Book(
    val author: String,
    val title: String,
) {
    companion object {
        fun fromMap(bookInfo: Map<String, String>): Book {
            return Book(author = bookInfo.getValue("author"), title = bookInfo.getValue("title"))
        }
    }
}
*/

data class Book(
    val author: String,
    val title: String,
) {
    companion object {
        fun fromMap(bookInfo: Map<String, String>): Book {
            val author: String by bookInfo
            val title: String by bookInfo
            return Book(author = author, title = title)
        }
    }
}

data class MapToBook(
    val bookInfo: Map<String, String>,
) {
    val author: String by bookInfo
    val title: String by bookInfo
}

fun main() {

    val guitarist = Guitarist("김태원")
    val bassist = Bassist("서재혁")
    with(guitarist) {
        println("$name")
        play()
        actionPerformance()
    }

    with(bassist) {
        println("$name")
        play()
        actionPerformance()
    }

    val leader = BandLeader(guitarist)
    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }

    val guitarLeader = guitarist.leader();
    with(guitarLeader) {
        println("GUITAR!!!!!!!!!!!!!!!!!!!!!")
        println("$name")
        play()
        actionPerformance()
        println("END")
    }

    val bassLeader = bassist.leader();
    with(bassLeader) {
        println("BASS!!!!!!!!!!!!!!!!!!!!!")
        println("$name")
        play()
        actionPerformance()
        println("END")
    }

    val bookInfo = mapOf(
        "author" to "파울로 코엘료",
        "title" to "연금술사"
    )

    val book = Book.fromMap(bookInfo)
    println("book author is ${book.author} / book title is ${book.title}")

    val mapToBook = MapToBook(bookInfo)
    println("mapToBook author is ${mapToBook.author} / mapToBook title is ${mapToBook.title}")

}