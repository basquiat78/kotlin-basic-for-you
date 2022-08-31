# data class 어디까지 알아보고 오셨어요?

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/doyouknowdataclass/kotlin/Doyouknowdataclass.kt)

일단 여기서는 실제로 코틀린 파일이 어떻게 자바로 디컴파일되는 보려면 인텔리제이의 도움을 많이 받아야 해서 인텔리제이로 진행한 부분이 많다.       

참고 하시라~

~~아마도 다들 인테리제이에서 하고 있을듯....~~

# Java의 record 키워드?

JDK14에서 preview로 등장했을 때만 해도 이게 뭘까 했다가 JDK16에 공식적으로 등장한 녀석이다.

우리가 기존에 DTO나 VO같이 어떤 정보를 담아서 전달하기 위한 객체를 정의해서 사용해왔다.

이것을 위해서 hashCode나 equals, toString같은 메소드를 재정의해서 사용하기도 하고 불변성을 유지하기 위해서 getter만 정의해서 사용하곤 한다.

하지만 롬복이 등장하면서 어노테이션으로만로도 쉽게 만들었고 불변객체로 만들기 위해 @Value같은 어노테이션을 사용하게 된다.       

그래서 이런 것은 어노테이션으로 쉽게 만들든 IDE의 기능을 활용해서 재정의할 코드를 자동으로 만들어서 사용하든 결국 보일러플레이트 코드이다.

지루하고 반복되는 코드는 개발자를 귀찮게 만들어요~~~~

이거라는 것이다. 그래서 그것을 쉽게 만들어서 사용할 수 있도록 등장한게 record다.

일단 자바의 JEP에서 이와 관련 feature는 다음과 같다.

    - abstract로 선언할 수 없고 기본적으로 final이기 때문에 다른 클래스가 상속할 수 없다.
    - 또한 다른 클래스를 상속할 수 없다.
    - 다만 인터페이스는 구현 상속은 가능하다.
    - 접근 제어자는 public, package-private(default) 만 가능하다.
    - 레코드의 컴포넌트는 암시적으로 final이다.
    - 레코드의 컴포넌트에 어노테이션을 사용할 수 있다.
    - 클래스와 비슷하게 정적 필드와 생성자, 메소드, 중첩 클래스를 선언할 수 있다.


보면 각 필드를 컴포넌트라고 표현하는데 이는 실제 코틀린의 record 클래스를 디컴파일하면 나오는 componentN을 보면 어느정도 감을 잡을 수 있다.

어찌되었든 이런 특징이 있다는 것이다.

```java
public class Main {

    public record Musician(String name, int age, String genre) {}

    public static void main(String args[]) {

        Musician musician = new Musician("John Coltrane", 30, "jazz");

        Musician musician1 = new Musician("John Coltrane", 30, "jazz");

        System.out.println(musician.name());
        System.out.println(musician.age());
        System.out.println(musician.genre());
        System.out.println(musician.toString());

        System.out.println(musician1.name());
        System.out.println(musician1.age());
        System.out.println(musician1.genre());
        System.out.println(musician1.toString());

        System.out.println(musician.equals(musician1));


    }
}

```
다만 특이한 것은 전통적인 방식의 getter의 형식이 아니고 조금은 심플해진 정도???

어째든 equals로 동치성 비교시에 record가 가지는 특성으로 equals와 hashCode를 자동으로 재정의하기 때문에 true가 나온다.

심심하면 record가 아닌 일반 클래스로 해보시라.

그런데 위 예제 코드를 보면 문득 그런 생각이 들것이다.     

'어??? 코틀린이랑 뭔가 비슷비슷?'

# 코틀린에는 data class가 있다~

코를린에서는 이를 위해 data라는 키워드를 제공하고 있다.

data class는 다음과 같은 특징을 가지고 있다.

    - 하나 이상의 프로퍼티를 가지는 primary constructor가 무조건 있어야 한다.
    - 프로퍼티는 val 또는 var로 선언해야 한다.
    - abstract, open, sealed, inner 키워드를 허용하지 않는다.
    - sealed class를 상속할 수 있다. 

아주 심플하게 티셔츠라는 클래스가 있다

```Kotlin
fun main() {

    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    with(tshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("tshirts is ${tshirts.toString()}")
    }
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

class Tshirts(
    val brand: String,
    val type: String,
    val size: Int,
    val material: String,
)

//result
//brand: NIKE, type: HOOD_LONG, size : 100
//tshirts is Tshirts@439f5b3d
```
위를 보면 toString()의 경우에는 클래스내에서 재정의를 하지 않아서 객채의 주소가 찍힐 뿐이다.

또한 다음과 같이 동치성 비교를 하면 자바와 똑같은 결과를 알 수 있다.


```Kotlin
fun main() {

    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    with(tshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("tshirts is ${toString()}")
    }

    val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    with(sameTshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("sameTshirts is ${toString()}")
    }

    println("true or false? : ${tshirts.equals(sameTshirts)}")

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

class Tshirts(
    val brand: String,
    val type: TshirtsType,
    val size: Int,
)

//result
//brand: NIKE, type: HOOD_LONG, size : 100
//tshirts is Tshirts@439f5b3d
//brand: NIKE, type: HOOD_LONG, size : 100
//sameTshirts is Tshirts@1d56ce6a
//true or false? : false
```
당연한 결과일 것이다.

하지만 Tshirts클래스 앞에 data를 붙여보자

```Kotlin
fun main() {

    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    with(tshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("tshirts is ${toString()}")
    }

    //val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 105)
    val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG, 100)
    with(sameTshirts) {
        println("brand: $brand, type: $type, size : $size")
        println("sameTshirts is ${toString()}")
    }

    println("true or false? : ${tshirts.equals(sameTshirts)}")

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

data class Tshirts(
    val brand: String,
    val type: TshirtsType,
    val size: Int,
)
//result
//brand: NIKE, type: HOOD_LONG, size : 100
//tshirts is Tshirts(brand=NIKE, type=HOOD_LONG, size=100)
//brand: NIKE, type: HOOD_LONG, size : 100
//sameTshirts is Tshirts(brand=NIKE, type=HOOD_LONG, size=100)
//true or false? : true
```
물론 당연히 사이즈가 다르면 true or false에서는 false가 나온다.

단순하게 playkotlin사이트에서는 왜 그런지 알수 없으니 인텔리제이의 기능을 이용해서 자바로 디컴파일하면 어떤 모양일지 궁금해진다.


```java
public final class Tshirts {
    @NotNull
    private final String brand;
    @NotNull
    private final TshirtsType type;
    private final int size;

    @NotNull
    public final String getBrand() {
        return this.brand;
    }

    @NotNull
    public final TshirtsType getType() {
        return this.type;
    }

    public final int getSize() {
        return this.size;
    }

    public Tshirts(@NotNull String brand, @NotNull TshirtsType type, int size) {
        Intrinsics.checkNotNullParameter(brand, "brand");
        Intrinsics.checkNotNullParameter(type, "type");
        super();
        this.brand = brand;
        this.type = type;
        this.size = size;
    }

    @NotNull
    public final String component1() {
        return this.brand;
    }

    @NotNull
    public final TshirtsType component2() {
        return this.type;
    }

    public final int component3() {
        return this.size;
    }

    @NotNull
    public final Tshirts copy(@NotNull String brand, @NotNull TshirtsType type, int size) {
        Intrinsics.checkNotNullParameter(brand, "brand");
        Intrinsics.checkNotNullParameter(type, "type");
        return new Tshirts(brand, type, size);
    }

    // $FF: synthetic method
    public static Tshirts copy$default(Tshirts var0, String var1, TshirtsType var2, int var3, int var4, Object var5) {
        if ((var4 & 1) != 0) {
            var1 = var0.brand;
        }

        if ((var4 & 2) != 0) {
            var2 = var0.type;
        }

        if ((var4 & 4) != 0) {
            var3 = var0.size;
        }

        return var0.copy(var1, var2, var3);
    }

    @NotNull
    public String toString() {
        return "Tshirts(brand=" + this.brand + ", type=" + this.type + ", size=" + this.size + ")";
    }

    public int hashCode() {
        String var10000 = this.brand;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        TshirtsType var10001 = this.type;
        return (var1 + (var10001 != null ? var10001.hashCode() : 0)) * 31 + Integer.hashCode(this.size);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof Tshirts) {
                Tshirts var2 = (Tshirts)var1;
                if (Intrinsics.areEqual(this.brand, var2.brand) && Intrinsics.areEqual(this.type, var2.type) && this.size == var2.size) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
```
hashCode, toString, equals가 자동으로 재정의된 것을 알 수 있으며 위에서 언급했던 componentN이라는 메소드가 생성자의 프로퍼티수만큼 생성되었다.

참고로 component는 생성자의 들어온 순서대로 넘버링 되며 또한 copy라는 것을 제공하는 것도 흥미롭다.

다만 여러분들은 자바의 롬복에서 @ToString나 @@EqualsAndHashCode의 경우에는 재정의시에 of나 exclude를 통해서 특정 필드만으로 재정의하거나 제외시킬수 있었다.

그렇다면 이 data class에서는 그게 가능한건가? 라는 생각을 할 수 있는데 당연히 있다.

다만 자바의 롬복처럼 @ToString 따로 @@EqualsAndHashCode따로 각기 다르게 설정할 수는 없다.

또한 여기에는 몇 가지 주의할 점이 있기 때문에 그 특징을 알아야 한다.

# 코틀린의 재정의되는 컴포넌트와 copy메소드는 생성자에 정의된 컴포넌트에 대해서만 가능하다.

이게 무슨 말일까???

어쨰든 티셔츠에서 사실 동치성을 판단할 때 사이즈가 꼭 필요할까?

어떤 요구사항에서 브랜드와 티셔츠 타입이 같다면 이것은 동치 관계다라고 표현할 수 있다.

즉 사이즈가 다르더라도 그 티셔츠는 같은 모델이라고 볼 수 있을 것이다.      

사이즈는 부수적인 요소라는 거지.        

그럼 위의 제목처럼 브랜드와 타입만 생성자에 정의하고 나머지 컴포넌트는 바디에 넣어보자.

```Kotlin
fun main() {
    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG)
    tshirts.sizeOf(100)
    println("$tshirts")
    println("${tshirts.size}")
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
//result
//Tshirts(brand=NIKE, type=HOOD_LONG)
//100
```
어랏? toString()을 하면 사이즈가 안나온다.

그럼 디컴파일된 자바 코드를 보면

```java
public final class Tshirts {
    private int size;
    @NotNull
    private final String brand;
    @NotNull
    private final TshirtsType type;

    public final int getSize() {
        return this.size;
    }

    public final void sizeOf(int _size) {
        this.size = _size;
    }

    @NotNull
    public final String getBrand() {
        return this.brand;
    }

    @NotNull
    public final TshirtsType getType() {
        return this.type;
    }

    public Tshirts(@NotNull String brand, @NotNull TshirtsType type) {
        Intrinsics.checkNotNullParameter(brand, "brand");
        Intrinsics.checkNotNullParameter(type, "type");
        super();
        this.brand = brand;
        this.type = type;
    }

    @NotNull
    public final String component1() {
        return this.brand;
    }

    @NotNull
    public final TshirtsType component2() {
        return this.type;
    }

    @NotNull
    public final Tshirts copy(@NotNull String brand, @NotNull TshirtsType type) {
        Intrinsics.checkNotNullParameter(brand, "brand");
        Intrinsics.checkNotNullParameter(type, "type");
        return new Tshirts(brand, type);
    }

    // $FF: synthetic method
    public static Tshirts copy$default(Tshirts var0, String var1, TshirtsType var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.brand;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.type;
        }

        return var0.copy(var1, var2);
    }

    @NotNull
    public String toString() {
        return "Tshirts(brand=" + this.brand + ", type=" + this.type + ")";
    }

    public int hashCode() {
        String var10000 = this.brand;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        TshirtsType var10001 = this.type;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof Tshirts) {
                Tshirts var2 = (Tshirts)var1;
                if (Intrinsics.areEqual(this.brand, var2.brand) && Intrinsics.areEqual(this.type, var2.type)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
```
hashCode, equals, toString, copy에서도 해당 size가 제외되어 있다.

그리고 size에 대한 componentN메소드도 생성되지 않았다.

자바의 롬복처럼 hashCode, equals, toString을 각기 다르게 지정할 수 없지만 생성자에 정의하느냐 바디에 정의하느냐에 따라서 달라진다는 것을 알 수 있다.      

그렇다면

```Kotlin
fun main() {

    val tshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG)
    tshirts.sizeOf(100)
    println("$tshirts")
    println("${tshirts.size}")

    val sameTshirts = Tshirts("NIKE", TshirtsType.HOOD_LONG)
    sameTshirts.sizeOf(110)
    println("$sameTshirts")
    println("${sameTshirts.size}")

    println("true or false? : ${tshirts == sameTshirts}")
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
//result
//Tshirts(brand=NIKE, type=HOOD_LONG)
//100
//Tshirts(brand=NIKE, type=HOOD_LONG)
//110
//true or false? : true
```
사이즈가 달라도 true가 찍힌다.

# copy는 언제 쓰는건가?

디컴파일된 코드를 보면 바로 알 수 있다.

copy메소드에서 내부적으로 new Tshirts()를 통해서 객체를 만들어서 반환하고 있는 형태이다.

독특하게도 copy를 사용할때 인자가 없으면 그냥 해당 객체를 복사한다.

생성자의 순서에 맞춰서 값을 넣어주면 그에 따른 객체를 반환하며 특정 프로퍼티만 변경하겠다고 하면 Named Arguments를 사용하면 된다.

앞서 설명했듯이 프로퍼티를 어디에 선언하느냐에 따라서 copy메소드도 그에 따라 인자가 바뀐다는 것을 인지하자.

여기서는 그냥 생성자에 다 때려박은 data class로 테스트 해본다.

참고로 위에서 진행했던 방식으로 size를 바디에 선언하고 copy를 할때 Named Arguments로 size만 변경할려고 한다면

```
Cannot find a parameter with this name: size
```
이런 에러가 발생할 것이다. 한번 테스트 해보길 바란다.

```Kotlin
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

data class Tshirts(
    val brand: String,
    val type: TshirtsType,
    val size: Int,
)
```

문득 JPA에서 entity를 dto로 변환하는 것을 생각해 보자.

뭐 팀의 코드 컨벤션 스타일에 따라 이것을 entity에 만들지 dto에서 받아서 처리할지 고민을 할 것이다.

일반적으로 static을 통해서 해당 객체에서 정적 메소드를 통해 무언가를 해보고 싶어진다.

하지만 코틀린에서는 static이라는 키워드가 없다. 그렇다면 어떻게 해야할것인가?

# 동반자 객체? 아니 동반객체~

코틀린에서는 그것을 위해 제공하는 것이 있는데 그것이 바로 companion object, 일명 동반 객체를 지원한다.

클래스 내부에 선언해서 사용하는데 data class에서도 사용가능하다.

예제로 다음과 같은 엔티티가 있다고 가정해 보자.

```Kotlin
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

    companion object {
        fun of(entity: TshirtsEntity) = with(entity) {
            Tshirts(brand = brand, type = type, size = size)
        }
    }

}
```
위 코드처럼 companion object를 선언하고 그 안에 메소드를 정의한다.

이 안에 정의된 메소드들은 자바에서 말하는 정적 메소드가 된다.

코드는 단순하다. of라는 정적 메소드가 있어서 엔티티 객체를 받고 그 값으로 Tshirts로 반환하는 심플한 코드이다.

```Kotlin
fun main() {
    val entity = TshirtsEntity("NIKE", TshirtsType.HOOD_LONG, 100)

    //Companion을 명시적으로 사용할 수 있다.
    //val tshirts = Tshirts.Companion.of(entity)

    // Companion은 이름을 줄 수 있다.
    // 하지만 이름을 줘도 생략해서 사용이 가능하다.
    //val tshirts = Tshirts.TshirtsCompanion.of(entity)
    val tshirts = Tshirts.of(entity)
    with(tshirts) {
        println(toString())
    }

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

    //companion object TshirtsCompanion {
    companion object {
        fun of(entity: TshirtsEntity) = with(entity) {
            Tshirts(brand = brand, type = type, size = size)
        }
    }

}
// result
// Tshirts(brand=NIKE, type=HOOD_LONG, size=100)
```

실제로 companion object에 대해서 디컴파일한 코드를 보면

```java
public static final class TshirtsCompanion {
    @NotNull
    public final Tshirts of(@NotNull TshirtsEntity entity) {
        Intrinsics.checkNotNullParameter(entity, "entity");
        int var3 = false;
        return new Tshirts(entity.getBrand(), entity.getType(), entity.getSize());
    }

    public final void doSomething() {
        String var1 = "doSomething";
        System.out.println(var1);
    }

    private TshirtsCompanion() {
    }

    // $FF: synthetic method
    public TshirtsCompanion(DefaultConstructorMarker $constructor_marker) {
        this();
    }
}
```
Thisrts클래스 내부의 내부클래스로 정의하고 있다.

static이 붙어 있는 것을 볼 수 있으며 companion object내에 함수를 정의하면 위 코드에서 알수 있듯이 doSomething처럼 클래스 내부에 추가되는 것을 알 수 있다.

# 문득 [이펙티브 자바]에서 언급하는 정적 메소드로 구성된 유틸성 클래스를 코틀린에서는 어떻게?

일단 두가지 방법이 있다.

## 1. 탑 레벨에 정의한다.

코틀린은 자바스크립트랑 상당히 유사한 부분이 많아서 탑 레벨내에서 함수를 정의할 수 있다.

예를 들면

다음과 같이 특정 패키지에 CommonUtils.kt를 하나 만들어보자.

```Kotlin
package io.basquiat.common.util

/**
 * 메세지가 없는 경우
 */
fun objectEmpty(): Nothing {
    throw ObjectEmptyException()
}

/**
 * 메세지가 있는 경우
 */
fun objectEmpty(message: String?): Nothing {
    if(message == null) {
        objectEmpty()
    } else {
        throw ObjectEmptyException(message)
    }
}

/**
 * 메세지가 없는 경우
 */
fun mandatoryParam(): Nothing {
    throw MandatoryArgumentException()
}

/**
 * 메세지가 있는 경우
 */
fun mandatoryParam(message: String?): Nothing {
    if(message == null) {
        mandatoryParam()
    } else {
        throw MandatoryArgumentException(message)
    }
}
```
해당 코틀린 파일의 내용을 보면 어떤 클래스 내에 선언된 함수들이 아니라는 것을 알 수 있다.

실제로 이것을 사용하게 되면

```Kotlin
import io.basquiat.common.util.mandatoryParam

class SimpleTest {

    @Test
    @DisplayName("enum test")
    fun enum_TEST() {
        // 오류가 날테지만 일단 예제용 코드
        val musicianId = request.id ?: mandatoryParam("변경할 뮤지션의 id는 필수 입니다.");
        // do something
    }

}

```
처럼 import한 부분을 보면 알 수 있듯이 쉽게 사용할 수 있다.

실제로 디컴파일된 자바 코드를 보면 public final class CommonUtilsKt로 생성되지만 접근할 방법이 없다.         

그래서

```kotlin
val util = CommonUtil()
```
저렇게 사용할 수 없고 상속도 할 수 없다.       

## 2. 싱글톤 객체로 유틸 클래스를 정의하자.

조슈아 블로크의 [이펙티브 자바]에서는 '아이템 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라'와 '아이템 4. 인스턴스화를 막으려거든 private 생성자를 사용하라'와 연관된 내용들이다.

일단 그렇다면 자바에서는 어떻게 해볼 수 있을까? [이펙티브 자바]의 예를 들어서 엘비스 클래스를 싱글톤으로 만들어 보자.

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() { 
        return INSTANCE; 
    }
}
```
내용을 보면 인스턴스화 방지를 위해 생성자는 private로 둔다.

그리고 private static final로 제공되는 엘비스의 인스턴스는 정적 메소드는 getInstance()로 반환하도록 만들어져 있다.

다만 여기서 'private static final Elvis INSTANCE = new Elvis();'은 어플리케이션이 올라올 때 메모리에도 같이 올라오는 구조이다.

그래서 내부 중첩 클래스를 통해서 호출하는 시점에 반환하도록 만드는 것이 좋은 방법이다.

예전 내가 공개했던 데모용 심플 어플리케이션에서는 스프링 컨테이너를 가져오기 위해 싱글톤 방식으로 제공하도록 만든게 있는데 이것이 바로 그 벙법이다.

[ApplicationContextProvider](https://github.com/basquiat78/interface-bbuljit/blob/main/src/main/java/io/basquiat/interfacebbuljit/noodle/provider/ApplicationContextProvider.java)

아무래도 ApplicationContextAware를 상속 구현한 클래스라 좀 복잡하긴 하지만 요지는 위에 언급한 것에 불과하다.     

이것을 참고해서 싱글톤으로 만든 저 엘비스 클래스를 코딩을 해야 하는 수고로움이 있지만 더 좋은 방식으로 처리하자.        

```java
public class Elvis {

    private Elvis() {}

    public static Elvis getInstance() {
        return ElvisHolder.INNER_ELVIS_INSTANCE;
    }

    // ContextHolder라든가 하는 이런 말들 많이 봤을뗴테 Holder라는 말은 일종의 관용처럼 쓰인다.      
    // 즉 바로 어떤 객체를 메모리로 올리지 않고 이 holder가 호출되는 시점에 보유한 객체를 생성해서 반환한다는 의미로 봐도 될까?
    private static class ElvisHolder {

        private static final Elvis INNER_ELVIS_INSTANCE = new Elvis();

        private ElvisHolder() {
            super();
        }
    }

}
```
뭔가 복잡하긴 하지만 실제로 이렇게 하면 해당 객체에 대한 마구잡이 접근을 막고 안정성과 싱글톤임을 보장하게 된다.        

하지만 코틀린은 키워드 하나로 싱글톤 객체를 생성할 수 있다.

~~대단하네~~

기존과 같이 클래스를 만드는데 class대신 object로 선언하면 싱글톤 객체가 된다.

```Kotlin
object CommonUtils {

    fun test() {
        println("test")
    }

}
```

디컴파일된 자바 코드를 보면

```java
public final class CommonUtils {
   @NotNull
   public static final CommonUtils INSTANCE;

   public final void test() {
      String var1 = "test";
      System.out.println(var1);
   }

   private CommonUtils() {
   }

   static {
      CommonUtils var0 = new CommonUtils();
      INSTANCE = var0;
   }
}
```
코드를 보면 static block을 통해 생성시 초기화 작업이 진행되면서 new를 통해 객체를 만들고 메모리에 올리는 것을 알 수 있다.     

즉 object로 클래스를 만들면 보일러플레이트 코드가 사라지고 싱글톤 객체를 쉽게 만들 수 있으며 우리가 원하는 작업에만 집중할수 있다.

```Kotlin
fun main() {
    val numStr = "10000"
    //실제 개발할 때는 import를 통해서 생략도 할 수 있다.
    //val number = stringToInt(numStr)
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
```

# At a Glance

data class를 진행하다가 문득 문득 필요성이 느껴지는 동반 객체와 싱글톤 객체를 만드는 것도 같이 진행을 해 봤다.

여러 면에서 보일러플레이트 코드를 지향하고 다른 부분에 좀 더 집중할 수 있도록 설계된 것이 참 좋다.

물론 자바도 17부터 많은 변화가 있고 앞으로 어떻게 바뀔지 모르겠지만 현재로써는 코틀린이 사용하기 좋지 않나 싶다.     

어서 동료들을 꼬셔서 코틀린의 세계로 빠지게 만들어야 하는데 여러 이유로 인해 아직 갈길이 멀다.   

아마도 결국 지금은 코틀린 기본을 위해서 잠시 멈춘 스프링 부트와 함께 개발하는 단계로 진행로 넘어가야 하지 않을까하는 생각이 든다.        