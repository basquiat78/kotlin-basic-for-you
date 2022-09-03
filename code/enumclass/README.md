# 이넘! 잡았다!

미안하다. 아재 개그다.

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/enumclass/kotlin/EnumClass.kt)

테스트 코드는 설명과는 다르게 가장 간결하게 작성했다.

# 열거형 타입 enum

그러고 보니 줄구장창 enum을 활용해왔는데 정작 enum을 설명한 적이 없었다.

뭔가 순서가 잘못되었다는 것을 살짝 느껴서 enum에 대해서 소개해 보고자 한다.

사실 굳이 이걸 따로 소개할 필요가 있을까만은 그래도 조슈아 블로크의 [이펙티브 자바]에서도 상당 부분을 챕터로 할애할 정도이다.

자바와 관련된 내용을 전부 여기서 소개하는건 무리고 특징을 한번 알아보자.

    - 열거 타입 자체는 클래스이다. 즉, 생성자와 메소드를 가질 수 있다. 
    - 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개한다. 
    - 열거 타입은 밖에서 접근할 수 있는 생성자를 제공하지 않으므로 사실상 final이다. 
    - 이로 인해 클라이언트가 인스턴스를 직접 생성하거나 확장할 수 없으니 열거 타입 선언으로 만들어진 인스턴스들은 딱 하나씩만 존재함이 보장된다. 
    - 열거 타입은 싱글턴을 일반화한 형태라고 볼 수 있다.
    - 열거 타입은 컴파일타임 타입 안전성을 제공한다
    - 열거 타입 상수는 인스턴스 필드를 가질 수 있다.      

또한 오래 전 우아한형제의 테크 블로그에 소개되었던 내용들도 있고 고군분투기에서도 이것을 다뤘으니 자바와 관련된 것은 링크로 대체하겠다.

[enum을 활용하는 것이 이득이라면 적극적으로 활용해라](https://github.com/basquiat78/refactoring-gogunbuntu/tree/2-usage-positively-enums)

# Enum Class In Kotlin

코틀린 내에서 enum class를 활용하는 방식은 자바와 크게 다르지 않다.

가장 기본적인 스타일 예제인데 자바와 다를게 없다.
```Kotlin
fun main() {
    val myEnum = MyEnum.valueOf("ENUM_ONE")
    with(myEnum) {
        println("enum name: $name, ordinal: $ordinal")
    }
}

enum class MyEnum {
    ENUM_ONE,
    ENUM_TWO,
    ENUM_THREE,
}
```

기본적으로 valueOf, name, ordinal은 사용 형태가 살짝 다를뿐이지 제공하는 메소드와 개념은 똑같다.

그래서 사실 내용상 짧을 것이지만 좀 더 길게 작성하고자 게임을 예로 들어보고자 한다.

여러분들도 한번 주위에서 이런 것들을 적용할 수 있는 아이템이 있는지 찾아보고 그에 맞춰서 프로그래밍을 해보는 방법을 찾아보자.

피지컬 딸리지만 나름 뇌지컬로 롤을 하고 있지만 와우라는 게임은 이 프로그래밍 관련 설명할때 상당히 괜찮다. ㅋㅋㅋㅋㅋ

일단 예전에 와우를 엄청 많이 했었는데 와우라는 게임에는 탈것이라는 게 존재한다.

~~와우 해보셨어요?~~

40레벨이 되면 속도가 60%로의 이동 속도를 가지는 탈것을 얻을 수 있고 60렙이 되면 120%로의 이동 속도를 가지는 탈것을 얻을 수 있다.

또한 70레빌이 되면 날것도 가질 수 있다.

자 이것을 enum에서 어떻게 활용할지 한번 생각을 해보자.

일단 어거지로 만들어 보자.

~~어거지이긴 하지만 그래도 예제로는 제법 쓸만하다고 생각을 해본다.~~

심플하게 40레벨미만이면 탈것을 얻을 수 없고 40레벨이상 60레벨미만이면 60%로 이동 속도를 얻을 수 있다.

60레벨이상에서 70레벨 미만이면 120% 탈것, 70레벨이상이면 날것을 얻을 수 있다고 보자.

```Kotlin
/**
 * 게임 캐릭터
 * 처음 생성시 레벨은 1일테니 1로 기본값 세팅
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
    var level: Int = 1,
)

/**
 * 심플하게 전사,사냥꾼,마법사,힐러,도둑만....
 */
enum class CharacterJob {
    WARRIOR,
    HUNTER,
    MAGICIAN,
    CLERIC,
    ROGUE
}
```

일단 게임 캐릭터와 게임 내의 캐릭터 직업을 정의해 보자.

아이디와 이름, 직업은 한번 결정되면 변경할 수 없으니 val로 선언하고 레벨은 변경될 수 있으니 var로 선언하자.

근데 CharacterJob에 대한 한글 이름이 필요할 수 있겠다 싶으니 우리는 자바처럼 각 열거 상수에 인스턴스 필드를 설정할 수 있다.

따라서 CharacterJob을 다음과 같이 수정할 수 있다.

```Kotlin
/**
 * 심플하게 전사,사냥꾼,마법사,힐러,도둑만....
 */
enum class CharacterJob(
    val jobName: String,
) {
    WARRIOR("전사"),
    HUNTER("사냥꾼"),
    MAGICIAN("마법사"),
    CLERIC("성직자"),
    ROGUE("도둑")
}

```

자 그렇다면 이제 나의 캐릭터는 사냥을 해서 레벨업을 한다고 생각해 보자.

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    // 30마리를 잡았다.
    for(level in 1..30) {
        warrior.level = ++warrior.level
    }
    with(warrior) {
        // 30마리를 잡았으니 31렙
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }

    // 어떤 탈것을 얻을 수 있는지 체크
    if(warrior.level in 1.until(40)) {
        println("아직 탈것을 얻을 수 없습니다.")
    } else if(warrior.level in 40.until(60)) {
        println("이동 속도 60프로의 탈것을 얻을 수 있습니다.")
    } else if(warrior.level in 60.until(70)) {
        println("이동 속도 120프로의 탈것을 얻을 수 있습니다.")
    } else {
        println("날것을 얻을 수 있습니다.")
    }

}

/**
 * 게임 캐릭터
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
    var level: Int = 1,
)

/**
 * 심플하게 전사,사냥꾼,마법사,힐러,도둑만....
 */
enum class CharacterJob(
    val jobName: String,
) {
    WARRIOR("전사"),
    HUNTER("사냥꾼"),
    MAGICIAN("마법사"),
    CLERIC("성직자"),
    ROGUE("도둑")
}
```
타우렌 탱커매니아라는 이름의 전사를 만들고 30마리를 사냥했으니 레벨이 31이 되었다.

그리고 해당 레벨을 체크해서 탈것을 얻을 수 있는지 없는지 체크하는 로직을 만들었다.

일단 문제가 될것이 없지만 만에 하나 레벨이 무한정으로 올라가고 그에 따라 탈것의 종류가 늘어나면 어떻게 될까?

코드가 if나 when으로 지저분해질 것이다.

지금이야 저게 전부일테지만 좀 더 멋진 방식이 없을까?

우선 탈것을 하나 만들어보자.

이전 스텝에서 배운 sealed class를 활용해서 sealed interface Vehicle를 하나 만들고 그것을 상속구현한 각각의 탈것을 정의하자.

interface도 상관없고 추상 클래스도 상관없고 어떤 방식이든 상관없다.

Vehicle에서 강제할 메소드를 선언해서 구현하도록 해도 상관없지만 여기서는 그게 중심이 아니니 정말 심플하게 콘솔만 찍어보고자 한다.

```Kotlin
/**
 * 60프로의 이동속도를 가지는 탈것
 */
class NoVehicle: Vehicle {
    override fun vehicleType() {
        println("탈것을 소지할 수 없습니다.")
    }
}

/**
 * 60프로의 이동속도를 가지는 탈것
 */
class Land60PercentVehicle: Vehicle {
    override fun vehicleType() {
        println("60프로의 이동속도를 가지는 탈것")
    }
}

/**
 * 120프로의 이동속도를 가지는 탈것
 */
class Land120PercentVehicle: Vehicle {
    override fun vehicleType() {
        println("120프로의 이동속도를 가지는 탈것")
    }
}

/**
 * 날것
 */
class FlyingVehicle: Vehicle {
    override fun vehicleType() {
        println("150프로의 이동속도를 가지는 날것")
    }
}

sealed interface Vehicle {
    fun vehicleType()
}

```

최종적으로

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    // 30마리를 잡았다.
    for(level in 1..30) {
        warrior.level = ++warrior.level
    }
    with(warrior) {
        // 30마리를 잡았으니 31렙
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }

    // 어떤 탈것을 얻을 수 있는지 체크
    val possibleVehicle = if(warrior.level in 1.until(40)) {
        NoVehicle()
    } else if(warrior.level in 40.until(60)) {
        Land60PercentVehicle()
    } else if(warrior.level in 60.until(70)) {
        Land120PercentVehicle()
    } else {
        FlyingVehicle()
    }
    possibleVehicle.vehicleType()

}
```

## 여러분들은 의심을 해야한다.!!

자 근데 이게 가만 보니 좀 이상하다.

Character라는 것은 일종의 전체적인 총합이고 enum을 통해 구분하는 것이 아니라 각기 직업별로 객체가 있어야 하지 않을까?

물론 sealed로 정의할 수 있겠지만 각각의 직업들을 패키지별로 따로 처리를 해야하는 경우를 생각해서 Character를 추상 클래스나 open으로 만들 수 있다.

이런 의심을 해봐야 하는 것이 개발자의 의무이다. 물론 지금 코드로도 충분하지만 예제라고 해도 좀 더 OOP적인 생각을 해보자.

## 그러나 지금 스텝은 enum class를 활용하는 것이다!

절대 귀찮아서가 아니다. 그 몫은 여러분들에게 맡긴다.

어째든 이런 방식이라면 탈것도 마찬가지이다.

각각의 레벨별 이동속도를 가진 탈것들은 와우 세계에서는 다양한 탈것들을 제공하기에 이것을 구현상속하는 구체적인 클래스로 작성하는 것이 맞다.

하지만 이것도 이 스텝에서 메인이 아니기 때문에 이대로 진행한다.

~~절대 귀찮아서가 아니다. 그 몫은 여러분들에게 맡긴다.~~

# 계속 진행해보자.

~~다시 한번 말하지만 절대 귀찮아서가 아니다.~~

레벨상태를 체크하는 enum을 한번 만들어 보자.

```Kotlin
enum class LevelStatus (
    val description: String,
) {
    LT40("40레벨미만"),
    GTE40LT60("40레벨이상이고 60레벨 미만"),
    GTE60LT70("60레벨이상이고 70레벨 미만"),
    GTE70("70레벨이상")
}

/**
 * 게임 캐릭터
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
) {
    // 바디로 옮김
    var level = 1

    var levelStatus = LevelStatus.LT40

    fun levelUp() {
        ++level
        levelStatus = when(level) {
            in 1.until(40) -> {
                LevelStatus.LT40
            }
            in 40.until(60) -> {
                LevelStatus.GTE40LT60
            }
            in 40.until(70) -> {
                LevelStatus.GTE60LT70
            }
            else -> {
                LevelStatus.GTE70
            }
        }
        /* if보다는 when를 추천. 코드가 간결해진다.
        levelStatus = if(level < 40) {
            LevelStatus.LT40
        } else if(level in 40.until(60)) {
            LevelStatus.GTE40LT60
        } else if(level in 40.until(70)) {
            LevelStatus.GTE60LT70
        } else {
            LevelStatus.GTE70
        }
        */
    }
}

```
그리고 Character에 레벨 상태를 가지는 프로퍼티를 갖을 것이다. 그리고 레벨업이라는 메소드를 통해 레벨을 올리고 상태도 변경해 보자.

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    // 
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    // 30마리를 잡았다.
    for(level in 1..40) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 어떤 탈것을 얻을 수 있는지 체크
    val possibleVehicle = when(warrior.levelStatus) {
        LevelStatus.LT40 -> NoVehicle()
        LevelStatus.GTE40LT60 -> Land60PercentVehicle()
        LevelStatus.GTE60LT70 -> Land120PercentVehicle()
        LevelStatus.GTE70 -> FlyingVehicle()
    }

    println("possibleVehicle : $possibleVehicle")

}

```
코드가 이전보다 간결해진거 같고 enum을 활용하고 있기 때문에 when 표현식을 사용할때 else구분을 추가하지 않아도 오류가 발생하지 않는다.

근데 여기서 멈추면 게을러 질수 없다.

Character내에서 레벌업할때 levelStatus를 체크하는 로직도 눈에 거슬리고 levelStatus에 상태를 일일히 체크해서 그에 맞는 탈것/날것을 반환하는 코드가 상당히 거슬린다.

어떻게 해볼 수 있을까?

자바의 enum class의 특징중 첫 번째를 보면 메소드를 가질 수 있다.

그럼 LevelStatus에 메소드를 추가해서 레벨정보를 받고 그에 맞는 상태를 반환하도록 만들면 어딸까?

enum의 열거타입만 작성하면 맨 뒤에 후행 컴마나 세미콜론 ';'를 넣지 않아도 무방하지만 메소드를 추가할려면 마지막에 ';'를 넣어줘야 한다.

## 고전적인 방식으로 활용하기

자바에서는 functional interface를 통해서 어떤 작업을 할 수 있다는 것을 알고 있다.

코틀린에서는 익명 함수를 생성자에 넘겨줄 수 있는데 다음과 같이 LevelStatus를 수정해 보자.

```Kotlin
enum class LevelStatus(
    val description: String,
    val levelCheck: (level:Int) -> Boolean,
) {
    LT40("40레벨미만", { level -> level in 0.until(40) }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { level -> level in 40.until(60)}),
    GTE60LT70("60레벨이상이고 70레벨 미만", { level -> level in 60.until(70)}),
    GTE70("70레벨이상", { level -> level in 70..Int.MAX_VALUE})
}
```
코드를 보면 생성자에 int를 받아서 Boolean을 반환하는 익명 함수를 넘겨주고 있다.

명시적으로 'val levelCheck: (level:Int) -> Boolean,'로 작성해도 되고 'val levelCheck: (Int) -> Boolean,'도 상관없다.

나는 명시적인걸 좋아해서 첫 번째 방법으로 넘겨준다.

그리고 코틀린의 코드 블록 {}를 통해 람다로 받아서 조건에 맞으면 true를 아니면 false를 반환할 것이다.

이때 코틀린의 코드 블록 {}으로 처리하면 it으로 받을 수 있지만 역시 명시적으로 level이 넘어온다가 작성했다.

물론 최대한의 간결함을 추구한다면 밑에서처럼

```Kotlin
enum class LevelStatus(
    val description: String,
    val levelCheck: (Int) -> Boolean,
) {
    LT40("40레벨미만", { it in 0.until(40) }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { it in 40.until(60)}),
    GTE60LT70("60레벨이상이고 70레벨 미만", { it in 60.until(70)}),
    GTE70("70레벨이상", { it in 70..Int.MAX_VALUE})
}
```
해도 상관없다.

자 그럼 우리는 정적 메소드를 하나 만들어서 레벨을 받으면 그에 맞는 열거 타입을 반환하도록 만들어 보자

```Kotlin
enum class LevelStatus(
    val description: String,
    val levelCheck: (level:Int) -> Boolean,
) {
    LT40("40레벨미만", { level -> level in 0.until(40) }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { level -> level in 40.until(60)}),
    GTE60LT70("60레벨이상이고 70레벨 미만", { level -> level in 60.until(70)}),
    GTE70("70레벨이상", { level -> level in 70..Int.MAX_VALUE});


    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) }
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
```
values()를 통해 선언된 열거타입을 돌면서 넘겨 받은 level을 통해 levelCheck를 하고 맞다면 해당 타입을 반환하게 만들었다.

그렇다면 Character의 코드는 다음과 같이

```Kotlin
/**
 * 게임 캐릭터
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
) {
    var level = 1
    
    var levelStatus = LevelStatus.LT40

    fun levelUp() {
        ++level
        levelStatus = LevelStatus.levelStatusFromLevel(level)
    }
}
```
위와 같이 엄청 간결하게 만들 수 있다.

## 생성자에 익명함수보다는 인터페이스를 활용하기

인터페이스를 활용하는 방식이다.

일단 코드로 보자.

```Kotlin
interface LevelCheckable {
    fun levelCheck(level:Int): Boolean
}

enum class LevelStatus(
    val description: String,
): LevelCheckable {
    LT40("40레벨미만") {
        override fun levelCheck(level: Int): Boolean = level in 0.until(40)
    },
    GTE40LT60("40레벨이상이고 60레벨 미만") {
        override fun levelCheck(level: Int): Boolean = level in 40.until(60)
    },
    GTE60LT70("60레벨이상이고 70레벨 미만") {
        override fun levelCheck(level: Int): Boolean = level in 60.until(70)
    },
    GTE70("70레벨이상") {
        override fun levelCheck(level: Int): Boolean = level in 70..Int.MAX_VALUE
    };

    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) } // 간결하게 하고 싶다면 
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
```
다음과 같이 LevelCheckable인터페이스를 만들고 그것을 LevelStatus가 구현한다.

이 때 각 열거타입들이 해당 인터페이스의 levelCheck를 오버라이딩해서 마치 맨 위의 방식처러 타입별로 구현할 수 있도록 작성할 수 있다.

코드를 보면 상당히 생소할 수 있는데 실제로 LT40이나 GTE40LT60은 중첩된 인스턴스로 볼 수 있다.

즉 각각의 열거타입들이 하나의 클래스로 볼 수 있고 바디 내에 인터페이스에 정의된 메소드를 오버라이드하고 있는 모습이라는 것을 알 수 있다.

즉 타입별로 description이라는 프로퍼티를 가지고 있으며 바디내에 인터페이스를 구현한 메소드들을 가지고 있는 각각의 인스턴스라는 것을 인지하면 된다.

## 추상 메소드를 활용하기

enum도 클래스고 추상 메소드를 가질 수 있다.

인터페이스를 구현상속한 enum의 경우 다형성을 가질 수 있다.

이러한 다형성을 활용해 제너릭으로 구현하는 무언가를 할 수 있다.

그런데 만일 해당 enum이 다형성을 추구해야 할 이유가 없다고 판단된다면 이 방법을 사용할 수 있는데 인터페이스를 활용하는 방식과 똑같다.

```Kotlin
/**
 * 추상 메소드 활용
 */
enum class LevelStatus(
    val description: String,
) {
    LT40("40레벨미만") {
        override fun levelCheck(level: Int): Boolean = level in 0.until(40)
    },
    GTE40LT60("40레벨이상이고 60레벨 미만") {
        override fun levelCheck(level: Int): Boolean = level in 40.until(60)
    },
    GTE60LT70("60레벨이상이고 70레벨 미만") {
        override fun levelCheck(level: Int): Boolean = level in 60.until(70)
    },
    GTE70("70레벨이상") {
        override fun levelCheck(level: Int): Boolean = level in 70..Int.MAX_VALUE
    };

    abstract fun levelCheck(level: Int): Boolean

    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) }
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
```
모양은 똑같다~

# 여러분의 선택사항은?

고전적인 방식이 좀 편해보이긴 하지만 생성자에 익명 함수를 넘기는게 싫은 분도 있을 것이고 그보다는 추상 메소드를 이용하는게 이득이라고 생각한다면 추상 메소드를 이용하면 된다.

enum타입의 다형성을 고려해야 하는 경우라면 interface를 활용하면 된다.

자 그럼 일단 Character의 코드를 더 간결하게 만들었는데 다음 코드를 다시 보자.

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    for(level in 1..65) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 어떤 탈것을 얻을 수 있는지 체크
    val possibleVehicle = when(warrior.levelStatus) {
        LevelStatus.LT40 -> NoVehicle()
        LevelStatus.GTE40LT60 -> Land60PercentVehicle()
        LevelStatus.GTE60LT70 -> Land120PercentVehicle()
        LevelStatus.GTE70 -> FlyingVehicle()
    }

    possibleVehicle.vehicleType()

}
```
나는 저 when으로 처리하는 코드 자체도 너무 싫다.

좀 더 enum으로 극한으로 처리할 수 없을까?

고전적인 방식으로 계속진행하면서 프로퍼티를 통해서 해당 열거타입이 그에 맞는 탈것을 반환하면 끝나지 않을까?

```Kotlin
// 고전적인 방식으로 
enum class LevelStatus(
    val description: String,
    val levelCheck: (level:Int) -> Boolean,
    val possibleVehicle: () -> Vehicle,
) {
    LT40("40레벨미만", { level -> level in 0.until(40) }, { NoVehicle() }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { level -> level in 40.until(60)}, { Land60PercentVehicle() }),
    GTE60LT70("60레벨이상이고 70레벨 미만", { level -> level in 60.until(70)}, { Land120PercentVehicle() }),
    GTE70("70레벨이상", { level -> level in 70..Int.MAX_VALUE}, { FlyingVehicle() });


    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) }
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
```
이러면 끝나지 않을까?

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    for(level in 1..66) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 어떤 탈것을 얻을 수 있는지 체크
    val possibleVehicle = warrior.levelStatus.possibleVehicle()

    possibleVehicle.vehicleType()

}
//result
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 1
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 67, levelStatus : GTE60LT70
//120프로의 이동속도를 가지는 탈것
```
# 아직 끝난거 아니다!

sealed class를 구현하고 있는 각 탈것들은 현재 equals나 hashCode를 재정의하지 않았고 어떤 프로퍼티도 없기 때문에 노란 경고를 볼 수 있다.

캐릭터는 여러개의 탈것을 가질 수 있는데 이것을 세분화해서 각 실제 구현체로 하자니 너무 귀찮다.

그래서 탈것마다 고유한 이름을 가지는 data class로 변경할 것이다.

```Kotlin
/**
 * 60프로의 이동속도를 가지는 탈것
 */
data class NoVehicle(val name: String = "nothing"): Vehicle {
    override fun vehicleType() {
        println("탈것을 소지할 수 없습니다.")
    }
}

/**
 * 60프로의 이동속도를 가지는 탈것
 */
data class Land60PercentVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("60프로의 이동속도를 가지는 탈것, 이름은 $name")
    }
}

/**
 * 120프로의 이동속도를 가지는 탈것
 */
data class Land120PercentVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("120프로의 이동속도를 가지는 탈것, 이름은 $name")
    }
}

/**
 * 날것
 */
data class FlyingVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("150프로의 이동속도를 가지는 날것, 이름은 $name")
    }
}
```

LevelStatus에서 살짝 수정을 하자.

```Kotlin
// 고전적인 방식으로 
enum class LevelStatus(
    val description: String,
    val levelCheck: (level:Int) -> Boolean,
    val possibleVehicle: (name:String) -> Vehicle,
) {
    LT40("40레벨미만", { level -> level in 0.until(40) }, { NoVehicle() }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { level -> level in 40.until(60)}, { name -> Land60PercentVehicle(name) }),
    GTE60LT70("60레벨이상이고 70레벨 미만", { level -> level in 60.until(70)}, { name -> Land120PercentVehicle(name) }),
    GTE70("70레벨이상", { level -> level in 70..Int.MAX_VALUE}, { name -> FlyingVehicle(name) });


    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) }
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
```

Character는 여러개의 탈것을 가질 수 있다.

하지만 같은 정보를 가진 탈것을 중복으로 가질 이유가 없고 탈것은 data class로 변경되었고 name으로 equals, hashCode가 재정의될 것이기 때문에 중복 제거를 하기 위해서 Set를 이용할 것이다.

```Kotlin
/**
 * 게임 캐릭터
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
) {
    private val vehicles = mutableSetOf<Vehicle>()

    var level = 1

    var levelStatus = LevelStatus.LT40

    fun levelUp() {
        ++level
        levelStatus = LevelStatus.levelStatusFromLevel(level)
    }

    fun getVehicle(vehicle: Vehicle) = vehicles.add(vehicle)

    fun possibleVehicles() = vehicles
}
```

이제 테스트를 해보자.

```Kotlin
fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    for(level in 1..40) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 어떤 탈것을 얻을 수 있는지 체크 40레벨이 넘었기 때문에
    val possibleVehicle = warrior.levelStatus.possibleVehicle("Fire Horse")
    possibleVehicle.vehicleType()
    warrior.getVehicle(possibleVehicle)

    for(level in 1..20) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 위 로직을 지나면 60레벨이 넘었을 것이다.
    val possibleVehicle2 = warrior.levelStatus.possibleVehicle("Super Fire Horse")
    possibleVehicle2.vehicleType()
    warrior.getVehicle(possibleVehicle2)

    warrior.levelUp()
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 레벨업을 한번 했지만 레벨이 60대일 것이다.
    // 같은 이름을 가진 탈것을 얻어서 넣어보자.
    val possibleVehicle3 = warrior.levelStatus.possibleVehicle("Super Fire Horse")
    possibleVehicle3.vehicleType()
    warrior.getVehicle(possibleVehicle3)

    for(level in 1..20) {
        //warrior.level = ++warrior.level     
        warrior.levelUp()
    }

    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }
    
    // 70레벨이 넘었다.
    val possibleVehicle4 = warrior.levelStatus.possibleVehicle("Fire Dragon")
    possibleVehicle4.vehicleType()
    warrior.getVehicle(possibleVehicle4)

    // 같은 이름의 탈것이 중간에 들어갔지만 set에 의해 중복 제거되고 3개의 탈것을 소지할 것이다.
    println(warrior.possibleVehicles())

}
//result
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 1
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 41, levelStatus : GTE40LT60
//60프로의 이동속도를 가지는 탈것, 이름은 Fire Horse
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 61, levelStatus : GTE60LT70
//120프로의 이동속도를 가지는 탈것, 이름은 Super Fire Horse
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 62, levelStatus : GTE60LT70
//120프로의 이동속도를 가지는 탈것, 이름은 Super Fire Horse
//탱커매니아 정보: id : basquiat, name : 타우렌 탱커매니아, job : 전사, level : 82, levelStatus : GTE70
//150프로의 이동속도를 가지는 날것, 이름은 Fire Dragon
//[Land60PercentVehicle(name=Fire Horse), Land120PercentVehicle(name=Super Fire Horse), FlyingVehicle(name=Fire Dragon)]
```

사실 이 예제는 미완성이다. 40레벨미만인 경우 NoVehicle를 반환하게 되는데 이것을 이렇게 다룰 필요가 있을까 생각이 든다.

그리고 70레벨이 넘으면 자신이 가지고 있는 어떤 탈것과 날것을 사용할 수 있기 때문에 이동 수단을 선택할 수 있고 느린 탈것이든 아니든 탈 수 있도록 만들수 있다.

하지만 목표는 enum을 소개하고 다루는 방법을 알아보는 챕터이기에 여기까지 진행할 것이다.       

# At a Glance
enum class를 활용하는 방법을 배웠다.

자바와 크게 다르지 않기 때문에 크게 어려움은 없었으리라 생각한다.       

또한 놓친 것들이 있을 수 있으니 생각나는데로 보강할 부분이 있으면 보강해 나갈 생각이다.     
