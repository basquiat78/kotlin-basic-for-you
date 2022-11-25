# 클래스 위임

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/delegation/kotlin/Delegation.kt)     

자바에서 상속은 분명 중요한 요소이다.       

하지만 아무런 생각없이 한 상속이 주는 피햬를 경험하게 되면 마치 자바계의 악이 아닌가 싶은 생각을 하게 되고 실제로 이런 내용들을 많이 볼 수 있다.      

이러한 단점을 보완하기 위해 위임이라는 디자인 패턴이 있다.             

지금 생각해보면 자바의 경우에는 이런 위임이라는 것을 하기 위해서는 많은 노력을 기울여야 한다.      

하지만 코틀린에서는 이것을 유연하게 제공한다. 

## 그럼 언제 위임을 하지??

자바의 경우에는 final 키워드를 붙이지 않으면 상속이 가능하지만 코틀린은 기본적으로 final이다.     

상속을 지원하기 위해서는 open 키워드를 사용하게 된다.        

보통 상속을 고려할 때는 상속할 클래스가 다른 클래스에 포함되어야 하는 경우라면 상속을 사용하게는 맞을 것이다.      

조슈아 블로크의 [이펙티브 자바]에서도 이런 말을 언급한다.

```
상속은 강력하지만 캡슐화를 깬다. 상속은 상위 클래스와 하위 클래스가 순수한 is-a관계일 때만 서야 한다.
..이하 중략
```
하지만 기존 클래스를 확장하거나 변경해야 하는 상황에 직면했는데 해당 클래스를 상속할 수 없다면?        

이때 위임 방식을 써야 한다.       

사실 이것은 [이펙티브 자바]에서 말하는 상속 대신 컴포지션 패턴을 이용하는 Delegation Pattern이라는 것을 알 수 있다.      

만일 이 책의 해당 챕터를 따라하거나 이 부분을 직접 구현하신 분들이라면 몇 가지 느낄 수 있는 것이 있을텐데 boilerplate code가 발생한다는 것이다.      

이게 무슨 말인지 코드로 한번 살펴보는게 최고다.

또한 이것은 자바가 되었든 코틀린이 되었든 상관없다.

## 시나리오는 역시 밴드 멤버로 한번 해보겠다!

밴드에는 기타리스트, 보컬리스트, 베이시스트, 드러머, 키보드등 다양한 뮤지션들의 합이다.     

그러다면 이것을 설계할 때 이런 생각을 할 수 있다.

```
BandMember가 있고 여기에는 위 언급한 악기를 다루는 뮤지션들이 존재한다.
```
가만히 분석해 보면 공통적으로 뮤지션들은 이름이 있고 악기를 연주하고 포퍼먼스를 펼친다.     

또한 악기 특성마다 연주하는 방식과 포퍼먼스가 달라질테니 이 부분을 인터페이스로 빼기로 결정한다.

```kotlin
interface Player {
    val name: String
    fun play()
    fun actionPerformance()
}
```
좋다. 아주 그럴싸하다.       

그중에 이것을 구현한 기타리스트와 베이시스트를 만들어보자.

```kotlin
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
        println("손가락이 보이지 않는 빠른 속주와 펑키 리듬 충만 만땅 느낌 쨉쨉이!!!!")
    }

    override fun actionPerformance() {
        println("여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!")
    }

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

}

```
나름대로 좋다. 우리는 인터페이스를 구현해서 각각의 뮤지션들을 정의했다.       

부할은 김태원이 리더이다.       

따라서 밴드의 리더를 정의할 필요가 있다.

여러분들이 생각할 수 있는 것은 무엇일까????

나 또한 이런 생각을 하게 된다.

'리더라는 객체를 만들고 기타리스트가 리더니까 기타리스트를 상속하면 되겠네?'

~~???: 좋아 이대로 진행시켜!~~

이 때 우리가 코틀린에서 배운것은 모든 클래스는 기본적으로 final이니 Guitarist클래스에 open을 붙여서 상속가능하게 만들자.

```Kotlin
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

open class Guitarist(
    override val name: String
): Player {

    override fun play() {
        println("손가락이 보이지 않는 빠른 속주와 펑키 리듬 충만 만땅 느낌 필!!!!")
    }

    override fun actionPerformance() {
        println("여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!")
    }

}

class BandLeader(name: String) : Guitarist(name)

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

    val bandLeader = BandLeader("김태원")
    with(bandLeader) {
        println("$name")
        play()
        actionPerformance()
    }
}
```
자 여기까지는 내가 생각해도 잘 한거 같다.       

하지만 이 코드는 확장하는데 있어서 상당한 문제를 야기한다.      

만일 해당 밴드의 리더가 기타리스트가 아니라면??

베이시스트가 리더인 밴드도 상당수 있다.      

예를 들면 'Red Hot Chili Peppers'는 베이시스트 Flea가 리더이다.       

그렇다면 이 구조는 밴드 리더가 기타리스트인 밴드에만 적용할 수 밖에 없다.       

```
그러면 BandLeaderGuitarist, BandLeaderBassist등 악기별로 상속해서 만들면 되잖아?
```
어? 이건 생각못했네?        

하지만 이건 좀 거시기하다. 쓸데없고 반복되는 클래스만 늘어날 게 뻔하기 때문이다.      

또한 결국에는 모든 클래스에 open을 붙여야 한다.       

이게 무슨 말일까? Bassist나 Guitarist 또는 Drummer같은 모든 클래스들을 어디선가 상속할 수 있다는 의미가 된다.      

상속을 허용한다면 [이펙티브 자바]에서 언급한 것처럼 그에 대한 Doc를 확실히 작업을 해줘야 한다.      

할 이유가 없는 클래스에 무언가를 한다는 것은 고통이다.!!!! ~~할 이유가 없다고!!!~~

또한 그것을 허용한다처도 지금이야 예제지만 만일 이런 식으로 세균처럼 번식하는 코드스멜이 나면 언제까지 반복되는 클래스를 늘려갈 순 없다.      

그래서 보통은 setter나 생성자를 통해서 필요한 인스턴스를 주입하는 방식으로 가게 된다.    

예를 들면

```Kotlin
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

    val bandLeaderGuitar = BandLeader(guitarist)
    with(bandLeaderGuitar) {
        println("$name")
        play()
        actionPerformance()
    }

    val bandLeaderBass = BandLeader(bassist)
    with(bandLeaderBass) {
        println("$name")
        play()
        actionPerformance()
    }
}
```
자 이렇게 한다면 어느 특정 악기 연주자에 커플링되지 않고 유연한 방식으로 구조를 가져갈 수 있다.       

사실 여기까지는 자바의 SOLID나 스프링의 DI와 관련된 내용을 공부하다 보면 쉽게 볼 수 있는 코드이고 OOP를 공부했다면 다 알 수 있는 부분이다.      

이제부터는 이 부분을 코틀린에서 말하는 클래스 수준의 위임을 통해서 코드를 작성해 보도록 하자.     

## boilerplate 코드를 줄여라.

BandLeader의 내부 코드 역시 어떻게 보면 boilerplate코드일 수 있다.     

실제로 작성된 함수나 프로퍼티는 override가 붙어있지 않는 것을 알 수 있다.        

즉 이 객체를 위한 함수를 작성했다는 것을 알 수 있는 것이다.      

코틀린에서는 'by'라는 키워드를 통해 이것을 줄일 수 있다.      

다음 코드를 보자.

```Kotlin
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

class BandLeader(override val name: String): Player by Guitarist(name)

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

    val leader = BandLeader("김태원")
    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }
}
```
이 코드를 보면 BandLeader클래스 내부의 함수가 아닌 'by'키워드 뒤에 선언된 Guitarist클래스로 위임을 하고 있다.         

이 코드를 실행하면 실행이 잘 되는 것을 알 수 있다.       

하지만 여전히 처음 상속을 통한 방식과 달라지지 않고 Guitarist에 커플링되버린다.       

결과 지금까지 해왔던 방식을 조합해서 '상속보다는 합성'이라는 방식을 선택해야 한다.      

즉 자바나 코틀린등 OOP가 가지는 다형성을 적극적으로 활용해야 한다.

```kotlin
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

class BandLeader(private val player: Player): Player by player

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
}
```
위 코드를 보면 초반 클래스에 인스턴스를 주입하는 방식과 크게 다르지 않다.       

하지만 주입된 객체로 by를 통해 주입된 인스턴스로 위임하면서 불필요한 코드들이 사라진 것을 알 수 있다.       

즉 주입된 객체의 함수들을 접근할 수 있게 만든다.       

여기서 여러분들이 주의할 점은 player는 프로퍼티 정보가 아니다.      

생성자를 통해 주입받는 파라미터라는 것을 잊지 말아야 한다. 이 부분은 밑에 설명할 생각이다.       

또한 만일 BandLeader의 특징을 갖는 함수를 따로 작성해야한다면 해당 함수를 오버라이드하면 된다.

```kotlin
class BandLeader(
    private val player: Player
): Player by player {
    override fun play() {
        println("밴드리더 ${player.name}의 연주가 시작됩니다.")
        player.play()
    }
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
}
```
위 코드처럼 사용할 수 있다는 장점이 있다.      

물론 BandLeader만을 위한 함수를 따로 작성할 수도 있다.     

## 그럼에도 불구하고

이것은 비단 위임이라는 한 부분에만 국한되는 문제는 아닐 것이다.     

예를 들면 인터페이스를 활용하다보면 생길 수 있는 문제를 한번 예로 들어볼까 한다.       

위에서 어찌되었든 모든 연주자, 즉 밴드리더 포함 Player라는 인터페이스를 구현하고 있다.

```kotlin
fun main() {

    val guitarist = Guitarist("김태원")
    val leader = BandLeader(guitarist)
    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }
    
    // 나 밴드리더아니다!
    val notBandLeader: Player = leader
    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }
}
```
위 코드는 아무 문제가 없을까?         

실제로 실행을 하면 아무 문제없이 작동을 한다. 이유는 'is-a'라는 관계를 보면 결국 모두 Player이기 떄문이다.      

따라서 비단 위임뿐만 아니라 인터페이스를 구현한 경우에는 이런 부분을 주의해야 한다.       

또한 위에서 'class BandLeader(private val player: Player): Player by player'이 코드를 설명할때 by키워드 뒤의 player는 파라미터라는 말을 했었다.

다음 코드를 보자.

```kotlin
class BandLeader(
    var player: Player
): Player by player {
    override fun play() {
        println("밴드리더 ${player.name}의 연주가 시작됩니다.")
        player.play()
    }
}

fun main() {

    val guitarist = Guitarist("김태원")
    val bassist = Bassist("서재혁")
     
    val leader = BandLeader(guitarist)
    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }

    println(leader.player)
    leader.player = bassist
    println(leader.player)

    with(leader) {
        println("$name")
        play()
        actionPerformance()
    }
}
```
BandLeader생성자로 주입받는 부분을 private val이 아닌 var로 변경한 경우에는 접근이 가능하게 된다.       

그러면 위 코드의 하단은 bassist로 변경했기 때문에 콘솔에 찍히는 로그도 변경될 것이라고 생각할 수 있다.        

하지만 결과는 다음과 같이 찍힌다.

```
김태원
밴드리더 김태원의 연주가 시작됩니다.
손가락이 보이지 않는 빠른 속주와 펑키 리듬 충만 만땅 느낌 필!!!!
여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!

io.basquiat.musicstore.Guitarist@2e0fa5d3
io.basquiat.musicstore.Bassist@5010be6

김태원
밴드리더 서재혁의 연주가 시작됩니다.
둥둥둥둥 슬랩 촤라랍촵촵 쿠당 둥땅 띠디딩
여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!
```
확실히 player의 정보는 변경되었다는 것을 알 수 있다. 

하지만 파라미터로 넘어온 시점의 인스턴스를 참조해 위임을 처리하기 때문에 위와 같은 현상이 벌어진다.       

자세히 보면 오버라이드한 play()함수의 경우에는 원하는 대로 작동하지만 actionPerformance()는 전혀 다르게 동작한다는 것을 알 수 있다.     

이 부분은 다음 사이트에서 확인 가능한 내용이다.

[delegation-pattern](https://www.baeldung.com/kotlin/delegation-pattern)

마지막 [Delegation Is Not Inheritance] 단락에서 이에 대한 단초를 가늠할 수 있다.      

또한 이렇게 되면 메모리 관련 문제도 발생할 수 있다고 한다.         

따라서 생성자 파라미터의 타입을 val로 선언해서 사용하는 것이 이것을 방지할 수 있다.

## 위 예제에서 확장 함수를 한번 이용해서 좀더 간결하게 사용해 보자.

함장확수는 [5. 당신이 원하는 함수를 만들어보자](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/functionyouwantmake)여기서 한번 훝어본 내용이다.

만일 Player의 인터페이스를 변경할 때 발생할 수 있는 사이드 이펙트를 고려해서 확장 함수를 생각해본다고 해보자.    

그럼 Player에 leader()라는 확장 함수를 만들어 볼 수 있다.

```kotlin
fun Player.leader(): Player {
    // 확장 함수 내부의 this는 reciever type
    return BandLeader(this)
}
```
확장함수 내부에서 this는 reciever type으로 BandLeader 인스턴스를 생성해서 반환할 수 있다.

```kotlin
fun main() {

    val guitarist = Guitarist("김태원")
    val bassist = Bassist("서재혁")
    val guitarLeader = guitarist.leader()
    println(guitarLeader)
    with(guitarLeader) {
        println("GUITAR LEADER!!!!!!!!!!!!!!!!!!!!!")
        println("$name")
        play()
        actionPerformance()
        println("END")
    }

    val bassLeader = bassist.leader()
    println(bassLeader)
    with(bassLeader) {
        println("BASS LEADER!!!!!!!!!!!!!!!!!!!!!")
        println("$name")
        play()
        actionPerformance()
        println("END")
    }
}
// rusult
// io.basquiat.musicstore.BandLeader@3b22cdd0
// GUITAR LEADER!!!!!!!!!!!!!!!!!!!!!
// 김태원
// 손가락이 보이지 않는 빠른 속주와 펑키 리듬 충만 만땅 느낌 필!!!!
// 여기거지 뛰당기면서 화려한 기타 솔로를 막 뿌려되기!!!!
// END

// io.basquiat.musicstore.BandLeader@1e81f4dc
// BASS LEADER!!!!!!!!!!!!!!!!!!!!!
// 서재혁
// 둥둥둥둥 슬랩 촤라랍촵촵 쿠당 둥땅 띠디딩
// 자기의 발을 보고 열심히 베이스를 팅기다가 베이스 악기 돌리기!
// END
```
위 코드는 인스턴스가 BandLeader라는 것을 콘솔 로그를 통해 확인할 수 있다.

다음과 같이 BandLeader가 아닌 해당 객체에 마치 밴드리더인듯 처리를 할 수도 있다.

```kotlin
fun Player.leader() = object : Player by this {
    override fun play() {
        println("밴드리더 ${this@leader.name}의 연주가 시작됩니다.")
        this@leader.play()
    }
}
```

다만 확장함수의 특징으로 오러라이딩이 불가능하다는 점만 유의하면 조합 또는 구성을 원하는 방식으로 가져갈 수 있다.

## 프로퍼티에 위임 사용하기

[delegated-properties](https://kotlinlang.org/docs/delegated-properties.html)

위 코틀린 공식 사이트의 이와 관련된 내용이 있는데 그 중에 쓸만한 것들을 한번 소개해 보고자 한다.        

밴드 구인 모집 커뮤니티를 보면 보통 금베이스, 은드럼이라는 표현을 많이 쓴다.       

그만큼 귀하다는 이야기겠지.... 이유는 베이스는 뭔가 멋이 없거나 재미가 없어보이고 드럼은 집에서 연습하기가...

그래서 기타, 보컬은 진짜 차고 넘쳐도 베이스, 드럼은 수요대비 공급이 부족하다는 말이다.      

```kotlin
import kotlin.reflect.KProperty

class BandJob {

    private lateinit var instrumental: String

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return when(instrumental) {
            "drums" -> {
                "그냥 드럼 아니죠? 은드럼 급구"
            }
            "bass" -> {
                "그냥 베이스 아니죠? 금베이스 급구"
            }
            else -> {
                "$instrumental 급구!!"
            }
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        instrumental = value
        println("$value 급구!!")
    }

}

fun main() {
    var bandJob: String by BandJob()
    bandJob = "bass"
    println("bass job : $bandJob")

    bandJob = "drums"
    println("drum job : $bandJob")

    bandJob = "guitars"
    println("guitars job : $bandJob")
}
// result
// bass 급구!!
// bass job : 그냥 베이스 아니죠? 금베이스 급구
// drums 급구!!
// drum job : 그냥 드럼 아니죠? 은드럼 급구
// guitars 급구!!
// guitars job : guitars 급구!!
```
operator는 [13. Array And Collection Framework feat. operator overloading](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/arryanacollection)에서도 소개한 바 있다.     

하지만 operator에 대한 더 자세한 내용은 다음 공식 사이트를 참조하자.

[operator-overloading](https://kotlinlang.org/docs/operator-overloading.html)

위에 언급한 사이트에서도 getValue와 setValue에 사용한 operator에 대해 언급하고 있다.      

일반적으로 set/get과 동일하며 String의 경우에는 '='로 대처할 수 있다.      

즉 위 코드에서 bandJob을 BandJob()로 위임하고 있다는 것을 알 수 있고 이 연산자 오버로딩을 통해서 '='을 통해 값을 활당한다.      

그래서 'bass', 'drums', 'guitars'를 대입하면 setValue()가 동작하고 콘솔에 찍을 때 getValue()에 정의된 로직에 따라 문자열을 반환한다.      

## like JSON

json이나 특정 맵에 대한 정보를 객체로 매핑할 때 유용한 방법도 존재한다.      

예를 들면 책이라는 객체를 하나 만들어 보자.

```kotlin
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
```
그리고 책의 정보를 담은 맵이 있다고 가정한다면

```kotlin
fun main() {
    val bookInfo = mapOf(
        "author" to "파울로 코엘료",
        "title" to "연금술사"
    )

    val book = Book.fromMap(bookInfo)
    println(book)
}
```
이런 방식으로 풀어나가야 한다.       

하지만 변수명이 Map의 키와 값다면 다음과 같이 by를 통해서 fromMap 함수를 수정할 수 있다.

```kotlin
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
```

만일 이 Book 클래스가 맵을 받는 특수한 클래스이기 때문에 메인 생성자에서 Map을 직접 받게다고 한다면

```kotlin
data class MapToBook(
    val bookInfo: Map<String, String>,
) {
    val author: String by bookInfo
    val title: String by bookInfo
}

fun main() {
    val bookInfo = mapOf(
        "author" to "파울로 코엘료",
        "title" to "연금술사"
    )

    val book = Book.fromMap(bookInfo)
    println("book author is ${book.author} / book title is ${book.title}")

    val mapToBook = MapToBook(bookInfo)
    println("mapToBook author is ${mapToBook.author} / mapToBook title is ${mapToBook.title}")
}
// result
// book author is 파울로 코엘료 / book title is 연금술사
// mapToBook author is 파울로 코엘료 / mapToBook title is 연금술사
```

# At a Glance 

여기서 지연초기화 관련 내용은 포함하지 않았는데 다음 챕터에서는 이 지연초기화에 관련된 내용을 소개해 볼까 한다.