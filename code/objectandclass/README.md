# 오브젝트 그리고 클라~~쓰

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/objectandclass/kotlin/ObjectAndClass.kt)

# 먼저 선행되어야 할 Visibility Modifiers

자바에서는 접근 제한자가 있다.

뭐 다들 아시겠지만 접근제어자 (Access Modifier)라고 해서

```
    private
    default
    protected
    public     
```
여런 것이다.

여러분 다들 아시는거죠? 그렇죠????

하지만 코틀린에서는 다음과 같은 4가지를 가질 수 있다.

```
    public
    private
    protected
    internal 
```

Access Modifier와 Visibility Modifiers의 어감이 좀 다르긴 한데 이런 느낌이다.

'접근 권한을 어떻게 설정할 것인가와 누구에게 오픈(공개)를 할 것인가'

private과 public의 경우에는 자바와 같지만 public은 기본 제한자로 생략이 가능하다.

그래서 지금까지 fun이나 클래스를 작성한 코드를 보면 여러분은 public이 붙은 걸 본적이 없을 것이다.

다만 protected의 경우에는 탑 레벨에서 사용할 수 없고 클래스 내에서만 사용이 가능하다.

그리고 default대신 internal이라는 첨 보는 녀석이 있는데 이녀석은 그냥 같은 모듈내에서라면 모두 접근이 가능하다는 의미를 가지고 있다.

이와 관련 코틀린의 공식 사이트에 보면 다음과 같이 설명한다.


```
Modules

The internal visibility modifier means that the member is visible within the same module. More specifically, a module is a set of Kotlin files compiled together, for example:

An IntelliJ IDEA module.

A Maven project.

A Gradle source set (with the exception that the test source set can access the internal declarations of main).

A set of files compiled with one invocation of the <kotlinc> Ant task.
```
그냥 길게 생각할 것 없이 해당 프로젝트내에서라면 전부 접근가능하다라고 보면 된다.

특이한 점은 지금까지 코드를 작성하면서 느꼈을 수 도 있지만 지역 변수나 이런 것에 붙어 있는 것을 본 적이 없을 것이다.

```
Local declarations

Local variables, functions, and classes can't have visibility modifiers.
```
말 그대로 로컬변수 로컬 함수, 로컬 클래스에는 사용할 수 없다고 적혀 있다.

그렇다고 한다

# 자바의 클래스

자바의 클래스는 뭐 다들 아실 것이다.

다음과 같은 Data라는 클래스를 만들어보자.

```java
public class Data {

    private String privateName;
    String defaultName;
    public String publicName;
    protected String protectedName;

}

public class Main {
    public static void main(String[] args) {
        Data d = new Data();
        // 다른 제한자의 경우에는 아예 접근하지도 못하고 IDE에서 뜨지도 않는다.
        System.out.println(d.publicName);
        d.publicName = "TEST";
        System.out.println(d.publicName);
    }
}

```
더 이상의 자세한 설명이 필요할까?

자바에서 기본 생성자라는 것이 존재하는데 우리가 특별히 어떤 생성자도 만들지 않는다면 이것은 자동으로 생성하게 되어 있다.

하지만 인자가 있는 생성자를 만들게 되면

```java
public class Data {

    private String privateName;
    String defaultName;
    public String publicName;
    protected String protectedName;

    public Data(String name) {
        this.publicName = name;
        this.defaultName = name;
        this.publicName = name;
        this.protectedName = name;

    }

}
```
위의 테스트 코드에 에러가 날 것이다. 왜냐하면 인자가 있는 생성자가 있기 때문에 기본 생성자가 만들어지지 않기 때문이다.

따라서 이 경우에는 기본 생성자를 만들어 줘야 한다.

그래서 롬복을 사용한다면 기본 생성자와 모든 인자에 대한 생성자 및 요구되는 인자에 대한 생성자를 자동으로 만들어주는 어노테이션을 사용하게 된다.

또한 위에서 먼저 언급했던 접근 제한자의 예로 들면 public으로 선언된 변수에 대해서는 테스트코드처럼 setter/getter가 없이도 접근 및 할당/수정이 가능하다.

이것이 로직상에서 이슈를 만들어 내기 때문에 public이 아닌 다른 제한자 (일반적으로 private)로 선언하고 setter/getter 메소드를 통해서 접근 및 할당/수정을 하게 된다.

뭐 자바의 기본적인 내용이니....

![실행이미지](https://www.newiki.net/w/images/5/53/More_details_be_omitted.jpg)

코틀린도 크게 다르지 않지만 이와 관련 독특한 점들이 많다.

코틀린에서는 바와 같은 방식으로 프로퍼티를 작성할 수 있으며 생성자를 통해서 프로퍼티를 작성하는 독특한 방식을 제공한다.

또한 지금까지 클래스를 만들고 사용해 왔는데 자바만 했던 분이라면 상당히 이질감이 들었을 것이다.     

new 키워드 없이 객체를 생성해서 사용했기 때문이다.      

그렇다 new 키워드가 필요없다.     

~~코틀린 개발자는 분명 이것조차 치기가 싫었던 엄청난 게으름쟁이였을 것이다라고 나는 확신한다.~~

그중 우리가 알아야 하는 것은 코틀린의 primary constructor(주 생성자)와 second constructor(부 생성자)개념이다.

이것은 객체를 만들 때 그 특징이 구분지어진다.

먼저 간결함을 살펴보자. 객체를 만들때는 다음과 같이 2개의 방식이 가능하다.


```Kotlin
// 바디{..}가 없어도 된다.
class Test

// 바디{..}가 있어도 상관없다.
class Test{}
```

위와 같이 바디가 있어도 없어도 상관없는 특성을 기억하자.

만일 어떤 객체에 name과 age라는 프로퍼티가 있다면 다음과 같이 작성이 가능하다.

```Kotlin
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
    
}

// second constructor로만 생성해본 클래스
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
```
부 생성자로만 만드는 경우는 없긴 하지만 예를 들어서 만들어 본것이다.

그리고 저렇게 부 생성자를 만들 경우에는 내부 프로퍼티의 이름곽 같은면 가독성 차원에서 관례적으로 _를 붙여준다.

뭐 안붙여 줘도 상관없다.

그럼에도 부 생성자로 자바처럼 하겠다고 하면 몇가지 이슈가 발생할 수 있어서 저런식으로 잘 만들지 않는다.

또한 이런 생각도 해볼 것이다.

'그럼 주 생성자로 인자있는 생성자로 만들고 부 생성자로 인자가 없는 기본 생성자처럼 흉내내도 되겠네요?'

과연 그럴까?

```Kotlin
fun main() {
    val test = Test("test", 10)
    with(test) {
        println("test info : $name | $age")
    }

    val testNoConstructor = Test()
    with(test) {
        println("test info : $name | $age")
    }

    
}

class Test(
    var name: String,
    var age: Int,
) {
    constructor() {}
}
```

아마도 'Primary constructor call expected'이런 오류가 발생할 것이다.

부 생성자만 있었을때는 문제가 없었다.

하지만 주 생성자가 존재하게 되면 부 생성자는 그 기능을 주 생성자로 위임을 해야 한다.

즉 이걸 자바처럼 흉내내 보려면 (이걸 왜 하고 있는지 모르겟지만 ㅋ)

두가지 방법으로 처리해야 한다.

일단 첫번 째 방법은 프로퍼티가 non null type이기에

```Kotlin
fun main() {
    val test = Test("test", 10)
    with(test) {
        println("test info : $name | $age")
    }

    val testNoConstructor = Test()
    with(test) {
        println("test info : $name | $age")
    }

    
}

class Test(
    var name: String,
    var age: Int,
) {
    constructor(): this(name = "", age = 0) {}
}
```

this를 통해 주 생성자에 위임을 한다. 만일 null이고자 한다면

```Kotlin
class Test(
    //var name: String,
    //var age: Int,
    var name: String?,
    var age: Int?,
) {
    constructor(): this(null, null) {}
}
```

즉 주 생성자와 부 생성자는 상황에 따라서 다르게 작성해야 한다.

나이는 있고 이름이 없는 요상한 부 생성자이긴 하지만

```Kotlin
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
    
}

class Test(
    //var name: String,
    //var age: Int,
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
```
이 특징을 언급하는 이유는 상속에서도 매우 중요한 개념이기 때문이다.

따라서 이 부분은 이렇게도 해보고 저렇게도 해보면서 특성을 파악해야 한다.

대부분은 주 생성자를 통해서 프로퍼티를 설정하기 때문에 이제부터는 이 주 생성자를 이용한 객체 생성을 해 볼것이다.

## 그 전에 후행 콤마(Trailing Comma) 처리 방식을 알아보자.

스크립트에서는 다음과 같이 작성을 해도 문제가 없다.

```javascript
let json = {
    name : "",
    age  : "",
    height: "",
}

```
json 속성을 정의할 때 뒤에 ','가 있어도 에러가 발생하지 않는다.     

그렇다면 장점이 뭔가 생각할 것이다. 쓸데없이 ,를 붙여야하는 수고로움을 추가해야하는 이유말이다.    

차후 작업을 하면서 속성이 추가될 수 있기 때문에 편의를 위해서 미리 찍는데 다음과 같은 상황을 생각해 보자.

```javascript
let json = {
    name : "",
    age  : "",
    height: "" // ,를 붙이지 않으면 어떨까?
    //새로 추가
    weight: ""
}

```
기존에 ',' 를 찍지 않고 작업한 json에 새로운 속성이 추가 되면 발생할 수 있다.      
height뒤에 ','를 찍어야 하는데 작업하다보면 놓칠 수 있는 부분이며 이것이 syntax에러를 발생시킨다.

sql에서는 이런 것을 좀 방지하기 위해 선행 콤마를 붙인다..


```sql
SELECT name
       , age
       , height
       // 새로운 컬럼 추가
       , weigth
    FROM PERSON

SELECT name,
       age,
       height
       // 새로운 컬럼 추가
       weigth
    FROM PERSON
```
차이점이 보이는가? 만일 후행 콤마를 붙이면 테스트할때 뒤에 ','를 붙이고 새로 추가하고 필요없으면 새로 추가한 컬럼을 지우고 마지막 오는 컬럼의 ','도 지워야 한다.

귀찮은 것이다. ㅎㅎㅎㅎㅎㅎㅎ

따라서 선행 컴마를 통해서 ', your column' 처럼 추가만 하면 좀 편하다. 삭제하더라도 해당 행만 지우면 되니깐

코틀린에서는 주 생성자를 통해 객체를 생성할 때 이 후행콤마를 허용한다.

기술 블로그나 해외 글들을 보면 이것이 깃에서도 비교 분석할때 편하다고 한다.

이런 특성이 있다는 것을 먼저 알아두자.

## var와 val

앞서 이 두개의 특징을 우리는 알고 있다.

이것은 프로퍼티에서도 중요한 부분인데 var의 경우에는 접근 및 재할당이 가능하고 val은 한번 할당되면 재할당이 불가능하다.

이것이 객체에서는 이에 따라 자동으로 setter/getter를 생성해준다.

즉 var은 자동으로 setter/getter가 그리고 val은 getter만 존재하게 된다.

## val로 선언된 프로퍼티

```Kotlin
class JazzMusician(
    val name: String? = null
)
```
이런 객체가 있다고 생각해 보자.

```Kotlin
fun main() {
    val musician1 = JazzMusician("John Coltrane")
    println("musician1 name is ${musician1.name}")
    // musician1.name = "Charlie Parker" Val cannot be reassigned
}
```
즉 setter가 작동하지 않는다.

그렇다면 이제 클래스 내부 바디안에 프로퍼티를 하나 만들어보자.


```Kotlin
class JazzMusician(
    val name: String,
) {
    val genre: String
}
```
이대로 실행하면 'Property must be initialized or be abstract'이 발생한다.

이것으로 알 수 있는 사실은 객체의 바디 {..} 안에 생성한 프로퍼티는 기본값을 가져야 한다는 것을 알 수 있다.

```Kotlin
class JazzMusician(
    val name: String,
) {
    val genre: String = "JAZZ"
}
```
하지만 val의 경우에는 지연 초기화를 사용할 수 있다.

즉 커스텀 Getter를 만들어보자.

방식은 해당 프로퍼티 밑에 그냥 get()으로 선언하면 된다.

```Kotlin

class JazzMusician(
    val name: String,
) {
    val genre: String
        get() {
           return "Jazz"
       }
}
```
콘솔에 해당 뮤지션의 장르는 "Jazz"로 찍힐 것이다.

## Backing Field

근데 만일 이미 할당된 값을 어떤 후처리를 통해 반환하고 싶다는 생각이 들수 있다.

가령 간단하게 소문자로 출력하고자 한다면

```Kotlin
class JazzMusician(
    val name: String,
) {
    val genre: String = "JAZZ"
        get() {
            return this.genre.lowercase()
        }
}
```
이렇게 작성하면 여러분은 'Initializer is not allowed here because this property has no backing field'을 보게 된다.

또는 'Exception in thread "main" java.lang.StackOverflowError'를 만날 수 있다.

어? backing field????

값이 할당되어 있을 경우에는 setter등 getter등 이런 상황을 만날 수 있는데 이 코드를 코틀린의 입장에서 생각해 봐야한다.

가령 여기서 this는 JazzMusician객체 자신일 것이다.

근데 이렇게 커스텀 getter/setter를 만들어서 사용하게 되면 재귀호출이 되는데 다음 상황을 잘 읽어보면 될것이다.


```
일단 반환할때 genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

'this.gerne' -> custome getter를 호출한다.      

    genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

    'this.gerne' -> custome getter를 호출한다.      

        genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

        'this.gerne' -> custome getter를 호출한다.      

            genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

            'this.gerne' -> custome getter를 호출한다.      

                genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                'this.gerne' -> custome getter를 호출한다.      

                    genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                    'this.gerne' -> custome getter를 호출한다.      

                        genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                        'this.gerne' -> custome getter를 호출한다.      

                            genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                            'this.gerne' -> custome getter를 호출한다.      

                                genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                                'this.gerne' -> custome getter를 호출한다.      

                                    genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                                    'this.gerne' -> custome getter를 호출한다.      

                                        genre라는 값을 소문자로 반환할려고 this즉 내 자신의 genre에 접근한다.

                                        'this.gerne' -> custome getter를 호출한다.   

                                            ..
                                                ..
                                                    ..   

```
~~뭔 말인지 알지??????~~

this.genre 그 자체가 getter를 호출하게 될 것이고 뮤지션의 객체의 커스텀 getter를 호출하게 되고 그게 무한 재귀로 빠져든다.

이게 setter의 경우에도 마찬가지이다.

코틀린에서는 그래서 이것을 위해서 backing field라는 것을 제공한다.

즉 위 코드는

```Kotlin
class JazzMusician(
    val name: String,
) {
    val genre: String = "JAZZ"
        get() {
            return field.lowercase()
        }
}
```
field로 접근하면 된다.


# var로 선언된 프로퍼티

var는 자동으로 getter와 setter를 만들어준다.

```Kotlin
fun main() {
    val musician2 = JazzMusicianVar("John Coltrane")
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")
    musician2.name = "Charlie Parker"
    musician2.genre = "Jazz"
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")
}

class JazzMusicianVar(
    var name: String,
) {
    var genre: String? = null
}
```

바디 내에 선언되는 프로퍼티는 val과 같이 초기값을 저렇게 설정해줘야 한다.

그렇지 않으면 'Property must be initialized or be abstract'에러메세지가 뜬다.

문득 그렇다면 setter를 허용하고 싶지 않을 수 있다. 예를 들면 JPA의 엔티티를 만들 경우라면 말이다.

이때는 set를 private으로 변경한다.

```Kotlin
fun main() {
    val musician2 = JazzMusicianVar("John Coltrane")
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")
    musician2.name = "Charlie Parker"
    // Cannot assign to 'genre': the setter is private in 'JazzMusicianVar'
    // musician2.genre = "Jazz" 
    musician2.changeGenre("Jazz")
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")

}

class JazzMusicianVar(
    var name: String,
) {
    var genre: String? = null
        private set
    fun changeGenre(_genre: String?) {
        this.genre = _genre
    }
    
}
```
set을 아주 닫을 수 없고 JPA의 엔티티에서처럼 changeGenre 메소드를 만들어서 변경할 수 있도록 한다.
그리고 프로퍼티 밑에 'private set'으로 해주면 위와 같이 setter로 변경할 때 private setter라고 알려준다.

만일 커스텀 setter를 사용하겠다고 하면 코드는 다음과 같이 변경된다. 위에서 언급했던 것처럼 backing field를 이용해야 한다.

참고로 getter와 setter는 프로퍼티 다음 라인에 아래와 같이 선언하면 된다.
이때도 getter 역시 후처리후 반환할때는 backing field를 사용해야 무한 재귀가 발생하지 않는다.

```Kotlin
fun main() {
    val musician2 = JazzMusicianVar("John Coltrane")
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")
    musician2.name = "Charlie Parker"
    // Cannot assign to 'genre': the setter is private in 'JazzMusicianVar'
    musician2.genre = "Jazz" 
    // musician2.changeGenre("Jazz")
    println("musician2 name is ${musician2.name} and genre is ${musician2.genre}")
}

class JazzMusicianVar(
    var name: String,
) {
    var genre: String? = null
    //	private set
    //fun changeGenre(_genre: String?) {
    //	this.genre = _genre
    //}
        get() {
            // 소문자로 반환하겠다면
            // 아래는 안된다는 것을 이제 알것이다.
            // return this.genre?.lowercase()
            return field?.lowercase()
        }
    	set(_genre) {
            // 전부 대문자로
            // 이건 stackoverflow 에러 발생
            // this.genre = _genre?.uppercase()
            // null일수 있으니 이 경우에는 safe call을 하고 그냥 변수만 세팅하면 _genre를 field에 할당하면 된다.
            field = _genre?.uppercase()
        }
    
}

```

그렇다면 생성자의 프로퍼티에 대해서 private set을 적용하고 싶은 경우이다.

사실 val로 하면 될 것이지만 위에서 바디내에 var로 선언된 프로퍼티에 대해서 변경할 수 있는 함수를 만들고 싶은 경우이다.

이 때는 Backing Properties를 활용해야 한다.

```Kotlin
fun main() {
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
```

실제로 IDE에서 컴파일된 파일을 한번 확인해 보면

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
다음과 같이 변환되는 것을 볼 수 있다.

backing properties는 코틀린 공식 사이트에 다음과 같이 설명하고 있다.

```
If you want to do something that does not fit into this “implicit backing field” scheme, you can always fall back to having a backing property
```

이런 방식을 사용해 볼 수 있겠지만 이것은 사실 코드 컨벤션을 어떻게 가져가냐에 따라 달라질것이다.

실제로 자바를 사용할 때도 통용되는 방식이긴 하다.

1. 우리는 모두 open setter/getter를 허용한다.      
   만일 JPA의 엔티티라면 setter를 사용하지 않고 메소드를 통해서 변경 행위를 한다.      
   팀 내의 코드 컨벤션 문화가 잡혀있다면 코틀린의 간결함을 유지하기 위해 메소드 조차 만들지 않는다.

2. 아니다. setter를 허용하지 않고 메소드를 제공한다.     
   팀내의 코드 컨벤션 문화가 잡혀있더라도 실수는 언제나 있을 수 있다.        
   그것을 아예 차단하고자 한다.

여러분의 생각은 어떤가??

# 클래스 생성시 초기화 작업을 할 수 있는가?
코틀린에서는 그것을 위해 init이라는 것을 제공한다.

이것은 생성자가 호출되고 난 이후 실행되는 키워드로

```Kotlin
fun main() {
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
```
즉 생성자가 호출된 이후 찍으로 대문자로 변경된다.

또는 이 안에서 어떤 프로퍼티는 null이 아니라는 조건을 수행하고 에러를 뱉어낼수 있도록 만들 수 있다.

# At a Glance

다음으로 알아 볼 것은 data class와 sealed class 그리고 중첩 클래스에 대해 알아보고자 한다.
