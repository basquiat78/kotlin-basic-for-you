# 상속이 먼저다

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/inheritance/kotlin/Inheritance.kt)

data class는 그렇다쳐도 sealed class를 설명하자니 상속을 먼저 알아야 할 것 같아서 이 부분을 먼저 알아보자~

# 상속은 만악의 근원??

처음 IT에 입문하기 위해서 Head First로 자바를 공부했었다.

객체지향적으로 다형성 대해 많은 것을 설명하고 있는데 그것을 보면서

'오! 멋지다'

라는 생각을 했었다.

실제로 실무에서도 선배 개발자들이 자주 활용하기도 했었다.

그러다 조슈아 블로크의 [이펙티브 자바]를 읽으면서 혼돈에 빠졌다.

1. 상속은 캡슐화를 해친다.
2. 따라서 상속을 고려한 설계를 한다면 문서화를 하라. 그렇지 않다면 final로 제한하라
   - 상위/하위 클래스가 순수한 is-a관계일때만! 써라.
   - is-a도 안심할 수많은 없다.

아니 뭐야? 갑자기 상속에 대해 엄청 부정적으로 말하는 늬앙스를 가지고 있다.      

~~상속을 하라는 거야 말라는 거여????????~~

그럼에도 우리는 더 좋은 설계를 위해서 상속에 대해서 알아야만 한다.

# 코틀린은 Any where

자바는 기본적으로 최상위 클래스, 즉 Object를 기본적으로 상속하고 있는 형태라는 것을 자바를 사용하시는 분들이라면 다 아는 사실이다.

그렇다면 코틀린도 그런건가?

코틀린은 Object라는 단어보다는 이것을 Any로 명명하고 있다.

뭐 해석하자면 '어떤', '어느'이런 의미인데 아마도 코틀린은 의미를 전달하는데 있어서 이것이 객체라는 말보다 명료하다고 생각한 모양이다.

```Kotlin
/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlin

/**
 * The root of the Kotlin class hierarchy. Every Kotlin class has [Any] as a superclass.
 */
public open class Any {
   /**
    * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
    * requirements:
    *
    * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
    * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
    * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
    * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
    * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
    *
    * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
    */
   public open operator fun equals(other: Any?): Boolean

   /**
    * Returns a hash code value for the object.  The general contract of `hashCode` is:
    *
    * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
    * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
    */
   public open fun hashCode(): Int

   /**
    * Returns a string representation of the object.
    */
   public open fun toString(): String
}
```

실제 Any의 코드인데 자바의 Object보다는 좀 심플한 느낌이 든다.

finalize() - 물론 자바 9부터는 deprecated가 붙어 있지만-, 쓰레드를 고려한 wait() 같은 것들은 없고 딱 필요한 녀석들만 정의해 놨다.

여기서 open이라는 키워드가 눈에 띄는데 이것을 여러분들은 기억해 두면 좋다.

# 앞서 설명하지 않았던 부분

코틀린은 자바와 같이 클래스를 선언하게 되면 final public이 된다.

'어? final이면 상속을 못하지 않나요?'

~~맞습니다. 공부를 열심히 하셨군요~~

'어 그럼 지금 위에 언급한 open이 hoaxy???'

눈치가 빠른 당신! 아주 칭찬해!

# 코틀린에서 말하는 상속을 제어하는 변경자

4개의 키워드가 존재한다.

```
open
final
abstract
override
```

위에서 언급했듯이 기본은 final이다.

앞서 우리가 배웠던 내용중에 언급했던

```Kotlin
public final class JazzMusicianProperty public constructor(_name: kotlin.String, _age: kotlin.Int) {
   private final var _age: kotlin.Int /* compiled code */

   private final var _name: kotlin.String /* compiled code */

   public final var age: kotlin.Int /* compiled code */

   public final var name: kotlin.String /* compiled code */

   public final fun changeAge(_age: kotlin.Int): kotlin.Unit { /* compiled code */ }

   public final fun changeName(_name: kotlin.String): kotlin.Unit { /* compiled code */ }
}
```
를 보면 어떤가?

backing propertis를 위해 생성자에 private를 붙인 프로퍼티를 제외하면 클래스와 프로퍼티 public final이 기본으로 붙어 있다.

즉 상속 자체가 안된다는 것을 알게 되는 것이다.

# 초간단 클래스 상속

그렇다면 실제로 그런지 우리는 코드를 통해 눈으로 직접 확인해봐야한다.

난 음악을 좋아하니깐

다음 Instrumental이라는 최상위 클래스가 있고 Bass라는 클래스가 존재한다고 생각하자.

자바처럼 해보자

```Kotlin
fun main() {
   val bassGuitar = BassGuitar()
   bassGuitar.sound()
}

class BassGuitar extends Instrumental {
   @Override
   fun sound() {
      println("둥둥")
   }
}

class Instrumental{
   fun sound() {
      println("어떤 소리???")
   }
}
```

extends라는 부분에서부터 벌써 에러가 발생한다.

일반적으로 상속의 경우에는 extends 키워드를 그리고 인터페이스 구현 상속은 implemnets 키워드를 사용하게 되는데 코틀린을 만든 개발자는 엄청 게으른 사람이라서 다른 방식으로 처리했다.

만일 C라는 클래스가 A라는 클래스와 B와 F라는 인터페이스를 구현한다면

```java
public class C extends A implements B, F {

}
```
이런 형식이지만 코틀린은 다음과 같은 구조를 갖는다.


```Kotlin
class C: A, B, F {

}
```

다만 주의할 점은 클래스 상속인 경우에는 생성자를 명시하게 된다.

```Kotlin
class C: A(), B, F
```

그렇다면 위의 코드는 다음과 같이

```Kotlin
fun main() {
   val bassGuitar = BassGuitar()
   bassGuitar.sound()
}

class BassGuitar: Instrumental() {
   override fun sound() {
      //super.sound()
      println("베이스 둥둥둥!!!")
   }
   // 못하지롱
   //override fun original() {
   //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
   //}
}

// open 키워드가 없다면 상속받은 BassGuitar클래스에서
// This type is final, so it cannot be inherited from를 보게 된다.
open class Instrumental{
   open fun sound() {
      println("어떤 소리???")
   }

   fun original() {
      println("open이 없으니 override못하지롱")
   }
}
```

우리가 앞에서 공부했던 Visibility Modifiers가 누구에게 오픈(공개)를 할 것인가를 정할때 통용된다고 한다면 open을 통해서 혀용할지 안할지 결정하게 된다.


```
딱 이거만 기억해라.     

상속을 고려하고 설계한 클래스라면 하위 클래스에서 오버라이드를 허용하려면 open 키워드를 사용하고 그렇지 않다면 그대로 놔두면 된다.
```

즉 Instrumental의 내부 메소드인 fun sound()의 경우도 마찬가지로 open를 명시하지 않으면 하위 클래스인 BassGuitar에서는 재정의를 할 수가 없다.

# 근데 이런 간단한거 말고 생성자가 있으면 어떻게 하나요?

악기들은 해당 악기를 만드는 브랜드가 있을 것이다.

또한 악기자체는 현악기일수도 타악기일수도 있기 때문에 현이 있을 수도 있고 없을 수도 있다.

베이스 기타는 기본 4줄이나 5, 6현 베이스 기타가 있기 때문에 몇 현 베이스인지 받는 프로퍼티도 추가해 보자.

```Kotlin
fun main() {
	val bassGuitar = BassGuitar(brandName ="Marleaux")
    println("bassGuitar brandName is ${bassGuitar.brandName}")
    bassGuitar.sound()
    
}

class BassGuitar(
    var string: Int? = 4,
    brandName: String,
): Instrumental(brandName) {
    
    override fun sound() {
        //super.sound()
        println("베이스 둥둥둥!!!")
    }
    
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
}

open class Instrumental(
    val brandName: String,
) {
    open fun sound() {
        println("어떤 소리???")
    }

    fun original() {
        println("open이 없으니 override못하지롱")
    }
}
```
var가 선언된 'string: Int? = 4'는 BassGuitar가 보통 4현이 기준이기 때문에 베이스 기타를 위한 프로퍼티가 될것이다.       

하지만 이떄 BassGuitar에서는 Instrumental의 생성자의 프로퍼티를 자동으로 상속받게 되는데 그런 프로퍼티에 대해서는 var/val를 생략해서 상위클래스의 생성자에 위임에서 넘겨줘야 한다.

좀 더 복잡하게 만든 국가, 즉 원산지 표기도 해보자.

```Kotlin
fun main() {
	val bassGuitar = BassGuitar(brandName ="Marleaux", string = 5, origin = "Germany")
    with(bassGuitar) {
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin")
        sound()
    }
    
    val guitar = Guitar(brandName ="Fender", string = 6, origin = "USA")
    with(guitar) {
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin")
        sound()
    }
    
}

class Guitar(
    // 7현 8현기타도 있응께
    var string: Int? = 6,
    brandName: String,
    origin: String,
): Instrumental(brandName, origin) {
    
    override fun sound() {
        //super.sound()
        println("기타는 쫘~~아아앙~~~")
    }
    
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
}

class BassGuitar(
    var string: Int? = 4,
    brandName: String,
    origin: String,
): Instrumental(brandName, origin) {
    
    override fun sound() {
        //super.sound()
    	println("베이스 둥둥둥!!!")
    }
    
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
}

open class Instrumental(
    val brandName: String,
    var origin: String,
) {
    
    open fun sound() {
        println("어떤 소리???")
    }

    fun original() {
        println("open이 없으니 override못하지롱")
    }

}
```

또 좀 더 복잡하게 악기는 소리를 내는 방식에 따른 악기의 타입이 정해지니 상위 클래스인 Instrumental에 타입도 추가해보자.

이 방식은 생성자에 직접 작성하는 방식과 바디에 작성하는 방식 두가지를 사용할 수 있다.


```Kotlin
fun main() {
    val bassGuitar = BassGuitar(brandName ="Marleaux", string = 5, origin = "Germany")
    with(bassGuitar) {
        println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        sound()
        original()
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
}

class Drum(
    brandName: String,
    origin: String,
): Instrumental(brandName, origin) {
    
    override fun sound() {
    	//super.sound()
    	println("드럼은 두구두구두구두구구두")
    }
    
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
}

class Guitar(
    // 7현 8현기타도 있응께
    var string: Int? = 6,
    brandName: String,
    origin: String
): Instrumental(brandName, origin) {
    
    override var type = InstrumentalType.STRING
    
    override fun sound() {
    	//super.sound()
    	println("기타는 쫘~~아아앙~~~")
    }
   
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
}

class BassGuitar(
    var string: Int? = 4,
    brandName: String,
    origin: String,
    override var type: InstrumentalType = InstrumentalType.STRING,
): Instrumental(brandName, origin) {
   
    override fun sound() {
    	//super.sound()
    	println("베이스 둥둥둥!!!")
    }
    
    // 못하지롱
    //override fun original() {
    //	println("override할 수 있지롱") 진짜? 'original' in 'Instrumental' is final and cannot be overridden에러 나는데?
    //}
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
```
위 코드를 보면 enum 클래스인 InstrumentalType을 하나 만들고 Instrumental의 바디에 타입을 하나 작성했다.

악기의 가장 원시적인 형태는 아마도 타악기일 것이다. 따라서 기본으로 타악기라는 타입을 넣어두고 Drums클래스도 하나 만들었다.

상위 클래스의 생성자가 아닌 바디에 선언된 프로퍼티를 상속하는 방식은 두가지 방식이 있다.

생성자를 통해 프로퍼티를 상속하겠다면 BassGuitar처럼 생성자 안에서 override키워드로 작성을 한다.

또는 Guitar처럼 바디 내에서 상속이 가능하다.

Drum은 타악기이기 때문에 상위 클래스의 프로퍼티를 그대로 사용하는 방식이다.

여기서 중요한 부분이 있다.

Instrumental의 type 변수 타입이 val이라 해도 하위 클래스에서는 var/val로 선언해서 받을 수 있다.

하지만 그 반대는 불가능하다.

즉 Instrumental의 type 변수 타입이 만일 var라면 하위 클래스에서 val로 받을 수 없다.

만일 Instrumental에서 type을 var로 선언하고 BassGuitar에서 override val로 설정하면 밑에 에러를 만나게 될것이다.

```
Var-property public open val type: InstrumentalType defined in BassGuitar cannot be overridden by val-property public open var type: InstrumentalType defined in Instrumental
```
다음 표를 통해서 한눈에 쉽게 표현해봤다.

| open super class | sub class | possible? |
| :---: | :---: | :---: |
| val | val | possible |
| var | var | possible |
| val | var | possible |
| var | val | impossible |

어떤 느낌이 드는가? 자바에서 클래스 상속은 아무 생각없이 extends로 참 쉽게 했던거 같다.    

하지만 틀린은 간결함을 추구하는 언어인데도 불구하고 상속을 하는게 생각보다 쉽지 않고 고민을 해야 한다.      

이유가 뭘까?

어쩌면 코틀린에서는 자바처럼 쉽게 상속이 가능하게 하는 것보다는 상속을 고려한 설계를 유도하고 있는게 아닌가 싶다.

코틀린도 자바처럼 클래스(추상 클래스포함) 상속의 경우에는 단일 상속이다.

그래서 OOP의 다형성을 위해서 인터페이스를 통해서 다중 상속을 허용한다.

자바와 관련 내용은 여기까지만 하고 추상 클래스 상속을 해보자.

# 그렇다면 추상 클래스는 어떻게?

똑같다. 다만 추상 클래스는 몸통만 존재하는 녀석이라 약간의 차이가 있는데 코드로 보자

```Kotlin
fun main() {
    val main = Main("test", "main")
    with(main) {
        play("guitar")
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
```

추상 클래스도 클래스인지라 상속을 고려한 설계를 해야만 한다.

위 예제는 복합적인 방식으로 상속하도록 생성자와 바디 내부에 프로퍼티를 선언했고 play라는 추상 메소드를 하나 작성한 예제이다.

역시 상속을 목적으로 설계하도록 구현 방식이 다소 복잡한 느낌을 준다.

자바처럼 히위 클래스에 부 생성자가 존재한다면 상위 클래스위 primary constructor를 super를 통해서 위임할 수도 있다.

# 인터페이스를 통한 구현 상속

사실 자바와 크게 다르지 않다.

다만 조슈아 블로크의 [이펙티브 자바]에서는 인터페이스 안티패턴과 관련해서

```java
//상수 인터페이스 안티패턴은 인터페이스를 잘못 사용한 예다. 
public interface PhysicalConstants {
    // 아보가드로 수 (1/몰)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    // 볼츠만 상수 (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    // 전자 질량 (kg)
    static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```
에 대해 언급하고 있다.

목적에 사용하라는 이야기겠지.         

일단 기초적인 방식으로 하나 예제를 보자.

```Kotlin
fun main() {
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
}

// 인자 없이 객체 생성을 고려해서 인터페이스의 프로퍼티를 상속할 때는 초기값을 할당해 준 방식
class Parent(override var myIntProp: Int = 0): MyInterface {

    // 구현 상속한 곳에서는 backing field 접근이 가능
    // 인터페이스의 값을 그대로 사용하겠다면 super로 접근 가능
    override var myStrProp: String = super.myStrProp
        // 인터페이스는 backing field를 가질 수 없다.
        // 하지만 상속한 하위 클래스에서느느 backing field를 사용할 수 있다.
        //get() {
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
    // 다만 get는 지원한다.
    val myIntProp: Int
    	get() {
            return 0
        }
    
    // backing feild를 지원히자 않는다.
    // 또한 초기화한다면 
    // Property initializers are not allowed in interfaces, 즉 인터페이스에서는 프로퍼티를 초기화 할 수 없다.
    // 다만 get는 지원한다.
    val myStrProp: String
    	get() {
            return "before"
        }

    
    fun myFun()
    fun myDefaultFun() {
        println("default method")
    }
}
```
다음과 같은 2개의 프로퍼티와 한개의 메소드와 자바 8이후 실제 구현을 가지는 default 메소드를 가지는 인터페이스가 있다.

그것을 상속구현하는 방식은 앞서 보아왔던 클래스 상속과 비슷하다.

다만 여기서 몇가지 주의 사항들을 알아야 한다.

코틀린의 인터페이스 내의 프로퍼티들은 backing field를 가질 수 없고 초기화로 값을 활당할 수 없다.

그래서 초기값을 할당하기 위해서는 getter로 할당한다.

단 상속구현하는 하위 클래스에서는 오버라이딩한 프로퍼티는 backing field가 지원된다.

근데 재미있는 건 var로 선언히거 get를 만들면 'Property in an interface cannot have a backing field' 메세지를 볼 게 된다.

결국 var타입으로는 인터페이스에서는 프로퍼티를 만들 수 없다.

뭔가 상당히 복잡한데 결국 프로퍼티의 값을 초기화 해서 반환하거나 하위 클래스에서 무조건 상속이후 초기값을 설정해 줘야 한다.

자바와는 달리 상속을 고려한 설계를 하도록 유도한다는 것은 똑같다.

결국 [이펙티브 자바]의 '아이템 19. 상속을 고려해 설계하고 문서화하라' 이 부분을 그냥 코드레벨에서 하게 만드는게 아닌가 생각을 하게 된다.

자 그럼 이제는 지금까지 만들었던 코드에서 이런 상황을 상상해보자.       

좀 위의 코드와 맞지 않지만 어떤 밴드가 있는데 솔로 타임이 와서 포퍼먼스를 한다고 하자.

베이스, 기타는 무대를 종횡무진하면서 퍼포먼스를 펼칠 수 있고 제자리에서 화려한 솔로 라인을 연주할 수 있다.

근데 드럼과 피아노는 그게 가능한거냐?????????

드럼과 피아노를 들고 무대를 종횡무진할수 없다는 것이다.

~~만일 할 수 있다면 그건 사람이 아니다.~~

```Kotlin
// 무대를 왔다갔다하고 관객 다이빙등 액티브한 퍼포먼스
interface ActionPerformance {
    
    fun movablePerformance()
    
    fun beforePerformance() {
        println("관객들의 환호 소리가 들리며 포퍼먼스를 할 준비를 한다.")
    }
}

// 제자리에서 자기 발만보고 퍼포먼스를 한다.
interface PlacePerformance {
    
    fun placePerformance()
    
    fun beforePerformance() {
        println("관객들의 환호 소리가 들리며 포퍼먼스를 할 준비를 한다.")
    }
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

fun main() {
    val actionGuitar = Guitar(brandName ="Fender", string = 6, origin = "USA")
    with(actionGuitar) {
         movablePerformance()
    }
    
    val actionDrum = Drum(brandName ="Perl", origin = "USA")
    with(actionDrum) {
         placePerformance()
    }
}
//관객들의 환호 소리가 들리며 포퍼먼스를 할 준비를 한다.
//기타리스트가 악기를 돌리기도 하고 무대를 종횡무진하며 기타 솔로를 펼친다.
//관객들의 환호 소리가 들리며 포퍼먼스를 할 준비를 한다.
//현란한 드럼 스틱 돌리기와 화려한 드러밍 솔로가 관객을 압도하기 시작한다.
```

자 근데 여기 포퍼먼스 관련 인터페이스에 같은 이름의 메소드가 있어서 베이스, 일렉기타, 보컬의 경우에는 이 두개를 다 사용할 수 있다.       

그렇게 되면 이 같은 메소드에 대해서 어떤 일이 벌어질까???

default메소드든 아니든

```Kotlin
fun main() {
    val actionBass = BassGuitar(brandName ="Marleaux", string = 5, origin = "USA")
    with(actionDrum) {
         // 발을 보고 솔로 포퍼먼스
         placePerformance()
         
         println("관객의 환호가 좀 더 커진다!!!!")
         
         movablePerformance()
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
        super.beforePerformance()
        println("기타리스트가 악기를 돌리기도 하고 무대를 종횡무진하며 기타 솔로를 펼친다.")
    }
    
    override fun placePerformance() {
        super.beforePerformance()
        println("현란한 드럼 스틱 돌리기와 화려한 드러밍 솔로가 관객을 압도하기 시작한다.")
    }
}
```
저렇게 구현을 하게 되면 beforePerformance()부분에

```
Many supertypes available, please specify the one you mean in angle brackets, e.g. 'super<Foo>'
```
이런 에러를 보게 된다. 아무래도 여러개의 인터페이스에 같은 메소드가 존재하니 뭘 실행해야 할지 아리송 할 것이다.       

근데 에러에 e.g. 뒤에 코드 예제를 보면 그 해답을 알려준다.        

하지만 다음과 같이 하면

```Kotlin
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
 
}
```

밑에 에러가 발생한다.

```
Class 'BassGuitar' must override public open fun beforePerformance(): Unit defined in ActionPerformance because it inherits multiple interface methods of it
```

그래서 코틀린에서는 저 두개를 묶어서 BassGuitar내에서 어떤 것을 호출할수 있는지 알려주는 방식을 제공한다.        

```Kotlin
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
 
    // 인터페이스 수만큼 같은 메소드를 가지고 있다면 그 이름으로 override를 하고
    // 다음과 같이 선언해 둔다.
    // 그렇게 되면 해당 객채내에서는 내가 어떤 것을 호출할 수 있는지 알게 된다.       
    override fun beforePerformance() {
        super<PlacePerformance>.beforePerformance()
        super<ActionPerformance>.beforePerformance()
        //super<SomeInterface>.beforePerformance()
    }

}
```

자그럼 기존에 만들었던 beforePerformance의 메세지를 좀 바꾸고 실제로 어떻게 찍히는지 확인해 보자.


```Kotlin
fun main() {
    
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

//result
//관객들의 환호 소리가 들리며 제자리 포퍼먼스를 할 준비를 한다.
//제자리에서 빠른 핑거링으로 속주 솔로를 펼친다.
//관객의 환호가 좀 더 커진다!!!!
//관객들의 환호 소리가 들리며 액션 포퍼먼스를 할 준비를 한다.
//베이시스트가 악기를 돌리기도 하고 무대를 종횡무진하며 베이스 슬랩를 펼친다.
```

## 번외로 잘 쓰지 않을 것 같은 second constructor를 이용한 상속 방식

위에서 잠깐 언급하기도 했는데 이 경우에는 좀 색다른 느낌을 준다.

```Kotlin
fun main() {
    val bass = Bass(brandName = "Fodera", origin = "USA", string = 5)
    with(bass){
    	println("brand is $brandName, 스트링 수 : $string, 원산지 : $origin, 악기형태 : $type, classType: $classType")
        original()
        sound()
    }
}

class Bass: Instrumental {
    
    // Bass가 가지는 고유한 프로퍼티로 현수
    var string: Int
    
    // super로 상위 클래스의 생성자를 호출한다.
    constructor(brandName: String, origin: String, string: Int) : super(brandName, origin) {
        this.string = string
    }
    
    override val type: InstrumentalType = InstrumentalType.STRING
    
    override fun sound() {
        // 베이시스트들의 현실
        println("저 기타는 왜 4줄이야???????? 소리나는 악기야??????")
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
```
좀 이상하지 않나?

다음과 같이 Bass객체에서 주 생성자가 없고 부 생성자로만 객체를 생성할때 Instrumental의 생성자 없이 상속하고 있다.

```
Bass: Instrumental() 요게 아니고 -> Bass: Instrumental
```
이 방식은 하위 클래스가 부 생성자를 통해서 상위 클래스의 주 생성자를 호출해서 설정하는 방식이기 때문에 가능하다.        

그 외에는 클래스 상속 방식과 전부 똑같다.       

알아두면 유용하게 사용할 수 있는 방법이다.        

# At a Glance

상속에 대해 아주 깊은 이야기를 한 건 아니지만 적어도 코틀린에서 상속을 어떻게 구현해야하는지에 대한 단초가 될것이라고 생각한다.      

또한 여기서는 내 개인적은 취미를 기준으로 작성해 봤는데 여러분들은 주위에 이런 것들을 적용할 만한 취미와 좋은 아이템이 있다면 그것을 통해서 작성해 보길 바란다.       

결국 OOP든 함수형 프로그래밍이든 현실의 어떤 조건과 요구 사항을 표현하는 것이 프로그래머니깐~ 

~~나만의 개똥철학이다~~

이와 관련 스마트 캐스팅을 지원하는 몇가지 기법들은 코틀린의 기본적인 지식이 쌓는 과정을 거친 이후 후에 진행될 것이다.        

또한 부족한 부분이 보이는대로 계속 보강할 생각이다.      