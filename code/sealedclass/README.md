# 봉인 해제!

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/sealedclass/kotlin/Sealedclass.kt)

# Java의 Sealed Class

JDK15에서 preview로 등장했던 녀석이다. 사실 이 때만 해도 이런 걸 왜 만들었을까 하는 생각을 했다.

'봉인된 클래스?'

그냥 내용만 대충 보고 넘어갔는데 JDK17에서 정식으로 등록된 기능이다.

재미있는 것은 원래 상속 가능한 클래스나 인터페이스는 원하면 제한없이 언제든지 구현상속할 수 있다.

OOP세계에서는 그렇다치고 현실내에서는 이것은 좀 문제가 있다.

왜냐하면 뭐든지 인터페이스를 구현할 수 있는데 노트북을 한번 생각해 보자.

노트북중에는 터치 스크린 기능이 있고 없는 녀석들도 있는데

```java

public interface TouchableScreen {
    void touchScreen();
}

/**
 * 터치 스크린 기능이 있는 고 스펙 노트북
 */
public class HighSpecNotebook implements TouchableScreen {

    // do something?

    @Override
    public void touchScreen() {
        System.out.println("터치하셨습니다.");
    }
}

/**
 * 터치 스크린 기능이 없는 저스펙 노트
 */
public class LowSpecNotebook implements TouchableScreen {

    // do something?

    @Override
    public void touchScreen() {
        System.out.println("터치하셨습니다.? 뭐니???");
    }
}
```
위 코드를 보면 터치 스크린 기능이 없더라도 상속구현하는데 제한이 없기 때문에 저게 가능하다는 것이다.

인터페이스뿐만일까? 상속이 가능한 클래스나 추상 클래스라면 개발자는 어떻게든 상속/구현을 할수 있다는 것이다.

문득 그렇다면 해당 인터페이스나 추상 클래스나 클래스를 특정 클래스만 상속/구현 할수 있도록 제한하면 저런 상황이 안나오지 않을까??

그래서! 등장한 녀석이 바로 Sealed Class이다.

다만 몇가지 제한 사항이 존재한다.

    - class앞에 sealed 키워드를 사용한다. e.g public abstract sealed class XXX, public sealed class YYY
    - interface앞에 sealed 키워드를 사용한다. e.g public sealed interface ZZZ
    - permits라는 키워드를 통해 구현할 클래스를 지정한다. e.g public sealed interface ZZZ permits A, B, C
    - permits에 정의된 클래스외에는 상속/구현이 불가능하다.   
    - 단 permits에 정의된 하위 클래스는 Sealed Class와 같은 모듈 또는 같은 패키지에 존재해야 한다.
    - permits에 정의된 하위 클래스는 final, non-sealed, sealed여야만 한다.
    - permits에 정의된 하위 클래스는 무조건 해당 Sealed Class 또는 Sealed interface를 구현해야 한다.      

당연히 sealed class를 상속받은 하위 클래스가 final이면 상속이 불가능하니 그렇다 치고 sealed역시 제한되어 있지만 non-sealed의 경우에는 좀 특이하다.

non-sealed로 선언된 하위 클래스는 또 일반적으로 상속/구현이 가능하다. 이때는 이것을 상속받는 클래스는 final이 아니어도 상관없다는 것이다.

따라서 상속/구현된 하위 클래스를 또 상속구현하는 경우에는 설계를 잘해야 한다는 것이다.

그렇지만 이 장점은 이렇게 설계하게 해서 위와 같은 터치 스크린 기능이 없는 저스펙 노트가 터치스크린 인터페이스를 상속할 수 없게 제한하게 된다.

# Sealed Class in Kotlin

sealed class가 등장한 이유가 문득 궁금해 지는데 위 예제를 보고 생각을 해보자.

인터페이스를 구현상속을 통해서 어떤 클래스가 터치스크린 기능이 있는 노트북인지 없는 노트북인지 알수 있다.

다만 해당 인터페이스를 상속한 객체가 뭔지는 알 수가 없다.

IDE기능을 이용해서 어떤 객체가 구현상속하는지 찾아볼 수는 있다. 다만 이건 IDE의 기능일 뿐이다.

물론 enum을 통해서 뭔가를 해 볼 수 있을 것이다.

하지만 enum클래스는 사실 상속이 불가능한 불변 객체이다. 이유는 잘 알겠지만 생성자가 private이기 때문이다.

그리고 실제로 코틀린의 공식 사이트에서 sealed class에 대해서 설명 내용중

```
In some sense, sealed classes are similar to enum classes: the set of values for an enum type is also restricted, 
but each enum constant exists only as a single instance, whereas a subclass of a sealed class can have multiple instances, each with its own state.
```
내용의 요지는 enum과 유사하지만 enum은 싱글 인스턴스로 존재한다면 sealed class는 하위 클래스가 sealed class를 통해 여러 상태를 가질 수 있다는 것이다.

enum이 가지는 몇 가지 제약사항을 돌파하기 위해 등장한 녀석이 sealed class라는 것을 말하고 있는 것이다.

그렇다면 코틀린의 sealed class의 특징이 뭔지 알아야 한다.

    - 최근 코틀린에서는 같은 파일내에서만 가능했던 이전 버전의 제약사항이 같은 모듈이나 패캐지에 존재하면 되는 것으로 변경됨
    - sealed class는 특성은 추상 클래스이고 private 생성자를 가진다.
    - sealed interface는 첫 번째 제약 사항에서 동작한다.

```Kotlin
sealed class SubClass

/*
public abstract class SubClass {
   private SubClass() {
   }

   // $FF: synthetic method
   public SubClass(DefaultConstructorMarker $constructor_marker) {
      this();
   }
}
*/

sealed interface SubInterface
/*
public interface SubInterface {
}
*/
```
위 디컴파일된 코드를 보면 잘 알 수 있을 것이다.

다만 interface의 경우에는 특별한 것이 없지만 실제로 SubInterface를 상속구현시 다른 패키지에 있으면 에러를 보여주게 된다.

자바에서는 permits를 통해 타겟 클래스를 명시적으로 정의하는 것과는 좀 다르긴 하다.

차후 코틀린에서도 이것이 어떻게 변할지 모르겠지만 어쨰든 자바에 비해서는 엄격하지 않다는 느낌을 준다.

그럼에도 같은 패키지/모듈에 있어야 한다는 제약사항만으로도 충분할 것이다.

# sealed class는 싱글톤 객체나 data class와 궁합이 참 좋다.

실제로 자바에서 Sealed Class를 소개할 떄 record와의 궁합에 대한 이야기를 한다.

물론 그건 궁합의 대한 이야기이다.

다만 이 챕터의 경우에는 IDE에서 진행할 예정이다. 이유는 좀 특징이 있기 때문이다.

지금까지 음악과 관련된 테스트를 했으니 다음과 같이 생각을 해보자.     

여러분들도 현실 세계에서 적용할 만한 아이템이 있다면 그것을 구현해 보는 것도 좋다.     

최대한 현실 세계에서 관찰한 것들을 프로그램으로 옮기는 것이 가장 중요하기 때문이다.    

자 그럼 밴드가 존재하고 밴드에는 멤버들이 존재한다.

연주자를 뭉뜽그려서 Player라는 추상 클래스를 하나 정의하고 그것을 상속한

```
Vocalist, Bassist, Guitarist, Drummer, Keyboardist          
```
요렇게 5개의 객체를 만들어 보자

```Kotlin
/**
 * 연주자가 있다.
 */
abstract class Player

class Vocalist: Player()
class Keyboardist: Player()
class Guitarist: Player()
// 은 베이스
class Bassist: Player()
// 금 드럼
class Drummer: Player()

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
```
자 저 위의 코드는 심플하니 어떤 녀석인지 알 수 있댜.

그럼 이제 테스트 코드를 진행해 보자.

```Kotlin
fun main() {
    Band.addMember(Keyboardist())
    Band.addMember(Bassist())
    Band.addMember(Vocalist())
    Band.addMember(Guitarist())
    Band.addMember(Drummer())

    // it이지만 player라는 이름을 주고 람다식으로 표현함
    // 밴드가 완성되고 하나씩 찍어보자
    Band.members().forEach {player -> 
        when(player) {
            is Keyboardist -> println("Keyboardist")
            is Drummer -> println("Drummer")
        }
    }
}
```
위 코드는 좀 독특하게 동작하는데 어떤 에러도 나지 않는다.

사실 이것은 후에 코틀린에 대한 기본적인 것이 파악되면 따로 다룰려고 했던 내용인데 여기서 잠깐 언급한다.

```
when expresssion Used as a Statement

In this case, we don’t need to cover every possible value for the argument, and the value computed in each case block, if any, is just ignored. As a statement, we can use the when block similarly to how we use the switch statement in Java.
it is not mandatory to cover all possible argument values when we are using when as a statement.
```
즉 상태만을 체크하는 즉 println처럼 Unit을 반환하고 끝나는 경우에는 다 체크할 필요가 없어서 오류가 발생하지 않는다.

그리고 표현식이 아닌 그냥 문인 경우에도 같은 현상이 발생한다는 것을 알아두면 좋다.

또한 is라는 키워드에 대해서 설명한 적이 없으니 이것도 한번 미리 언급해 보자.

자바에서는 캐스팅과 관련 타입을 알기 위해서는 instanceof를 사용한다.

그냥 다 짜르고 대충 예제를 보면

```java
public class Main {
    public void test(Object obj) {
        if(obj instanceof TestClass) {
            // 캐스팅을 해야 한다.
            TestClass testClass = (TestClass)obj;
            testClass.doSomething();
        }
        //...
    }
}
```
위에서 보면 instanceof로 타입체크하고 맞다면 캐스팅을 해줘야 한다.

코틀린은 instanceof대신 is를 사용하고 스마트 캐스팅이라 해서 아래처럼 간결하게 사용할 수 있다.

```Kotlin
fun test(any: Any) {
    if(any is TestClass) {
        // any -> 자동으로 TestClass로 캐스팅되서 그냥 사용하면 된다.
        any.doSomething()
    }

}
```

자 그럼 이런 생각을 해보자. 루프를 돌면서 나오는 플레이어가 누구냐에 따라서 그에 따른 연주자 타입을 반환해 보자.

```Kotlin
fun main() {
    // 밴드에 멤버들을 추가한다.
    Band.addMember(Keyboardist())
    Band.addMember(Bassist())
    Band.addMember(Vocalist())
    Band.addMember(Guitarist())
    Band.addMember(Drummer())

    // it이지만 player라는 이름을 주고 람다식으로 표현함
    Band.members().forEach {player ->
        val playerType = when(player) {
            is Keyboardist -> "Keyboardist"
            is Drummer -> "Drummer"
        }
        println("playerType : $playerType")
    }
}
```
저렇게 하면 어떤 일이 벌어질까? 바로 when에 빨간 밑줄로 'add else branch'라고 오류정보가 나올것이다.

자 그럼 그걸 클릭하는 순간

```Kotlin
fun main() {
    // 밴드에 멤버들을 추가한다.
    Band.addMember(Keyboardist())
    Band.addMember(Bassist())
    Band.addMember(Vocalist())
    Band.addMember(Guitarist())
    Band.addMember(Drummer())

    // 밴드가 완성되고 하나씩 찍어보자
    // it이지만 player라는 이름을 주고 람다식으로 표현함
    Band.members().forEach {player ->
        /*
         when using as a Statement
        when(it) {
            is Keyboardist -> println("Keyboardist")
            is Drummer -> println("Drummer")
        }
        */
        val playerType = when(player) {
            is Keyboardist -> "Keyboardist"
            is Drummer -> "Drummer"
            else -> "who are you??"
        }
        println("playerType : $playerType")
    }
}
```
자 그럼 이제 오류가 싹 사라진다. 하지만 실행을 하는 순간 어떤 일이 벌어질까?

분명 보컬리스트, 베이시스트, 기타리스트가 멤버임에도 불구하고

'누구냐 넌?"

이라고 찍힐 것이다.

'어랏? 보니깐 휴먼 실수가 발생했네요?'

그렇다. 그리고

```Kotlin
fun main() {
    // 밴드에 멤버들을 추가한다.
    Band.addMember(Keyboardist())
    Band.addMember(Bassist())
    Band.addMember(Vocalist())
    Band.addMember(Guitarist())
    Band.addMember(Drummer())

    // 밴드가 완성되고 하나씩 찍어보자
    // it이지만 player라는 이름을 주고 람다식으로 표현함
    Band.members().forEach {player ->
        /*
         when using as a Statement
        when(it) {
            is Keyboardist -> println("Keyboardist")
            is Drummer -> println("Drummer")
        }
        */
        val playerType = when(player) {
            is Keyboardist -> "Keyboardist"
            is Drummer -> "Drummer"
            is Vocalist -> "Keyboardist"
            is Guitarist -> "Drummer"
            is Bassist -> "Drummer"
        }
        println("playerType : $playerType")
    }
}
```
Player를 상속구현한 모든 객체를 다 적어도 어떤 일이 발생할까??

아니 다 적어서 더이상 체크할 게 없음에도 'add else branch'라고 오류정보가 나온다.

당연하게도 컴파일러 입장에서는 Player를 상속구현한 하위 클래스를 알수 가 없다.

어째든 결코 실행되지 않을 쓸데없는 else를 추가해야 한다.

자 그럼 sealed class는 그 특성이 추상 클래스라는 것을 위에서 배웠다.

지금 코드에서 abstract class Player를 sealed로 변경하자.

# 어 근데 바꾸자 마자 상속받은 class에 노랗게 경고가 떠요. 엄청 거슬리는데?

사실 경고일 뿐이라 문제가 없지만 궁금해서 찾아봤더니 몇가지 이야기를 하는데 그 중 하나가

'''
It's highly recommended to override equals to provide comparison stability or convert class to object with the same effect.
'''
즉 타입 안정성과 관련된 내용이 포함되어져 있다.

즉 상태(프로퍼티)를 가지거나 equals와 hashCode를 재정의하는 data class, 그냥 class를 사용한다면 저 두개를 오버라이딩하면 된다.

아니면 싱글톤객체임을 보장하는 object로 선언하면 된다.

자 어쨰든 연주자들 자신의 이름을 가질 수 있다.

따라서 Player에 속성을 추가해서 사용해 보자.

```Kotlin
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
    // it이지만 player라는 이름을 주고 람다식으로 표현함
    Band.members().forEach {player ->
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
```
위에서 처럼 작성이 가능하다.

is 와 스마트 캐스팅으로 인해 player를 각 객체로 캐스팅 할 필요없이 바로 코드를 간결하게 사용할 수 있다.

~~((Keyboardist) player).name 이렇게 안하는게 어디냐?~~

컴파일러 입장에서는 하위 클래스가 뭐가 있는지 알기 떄문에 else를 추가하라는 오류가 사라진다.

또한 위 is 중에 하나를 주석처리하면 추가하지 않는 하위 클래스를 추가하라는 오류가 생길 것이다.

즉, 어떻게 보면 개발자에게 강제적으로 사용해서 개발자의 실수를 미리 감지함으로써 버그를 줄일 수 있게 된다.     

# At a Glance
자바17의 Sealed Class를 보면 참 멋지다는 생각이 들었고 코틀린의 sealed class역시 멋지다.      

Spring boot에서 가볍게 사용한다면 사실 이런 sealed class를 얼마나 자주 사용할지 잘 모르겠다.       

하지만 적어도 객체를 설계하는데 있어서 좀 더 유연하고 탄탄한 설계를 도와주는 강력한 개념중 하나라고 생각한다.      

생각나는데로 또는 놓친 부분이 있다면 계속 보강할 생각이다~ 👊
