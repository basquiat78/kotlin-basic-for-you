# 제네릭스

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/generics/kotlin/Generics.kt)
 
이전에 코틀린의 컬렉션 함수들을 살펴보면서 실제 구현된 코드를 보면 이런 함수들이 대부분 제네릭으로 이뤄진 함수라는 것을 확인한 바 있다.      

자바를 알고 있는 여러분들은 이런 제네릭이 가지는 유연함을 잘 알것이다.        

코틀린도 다를 바가 없지만 몇 가지 차이점이 보인다.

특히 제네릭스와 관련해 코틀린 공식 사이트에서도 언급되기도 하는 상당히 중요하게 여기는 `varaiance`라는 개념도 있는 만큼 이런 부분도 짚고 넘어갈 필요가 있다. 

따라서 이 챕터는 상당히 긴 내용을 담고 있다.

# 참고할 만한 서적

코틀린 서적을 5권을 가지고 있는데 그 중에 괜찮은 내용들을 참조해서 설명하기 쉽게 풀어냈다.

~~많이도 샀다.....~~

따라서 나의 주관적인 생각으로 오류부분이 있을 수 있으니 관련 책을 사보는것도 괜찮다.

[코틀린 완벽 가이드], [코틀린 인 액션], [이펙티브 코틀린], [코틀린을 다루는 기술]

참고로 [코틀린을 다루는 기술]은 읽기만 했던 책이다.

한번 완독을 했지만 사실 머리가 하애졌던 책이라 이해될 때까지 읽고 있는 책인데 개인적으로 좀 어려운 책이 아닌가 싶다.

게다가 읽기만 해서는 이해하기 힘든 내용들이 수두룩하다.

아무래도 코틀린의 문법을 설명한 책이 아니기 때문인가 싶은데 제네릭스의 변성과 관련된 내용은 짤막하면서도 그나마 이해하기 쉬운 설명을 하고 있다.

게다가 이 책은 코틀린의 기본과 함수형 프로그래밍, 제네릭에 대한 빠싹한 지식이 없다면 한동안은 쉽게 넘어가다 중반부터는 한장 한장 넘기기가 쉽지 않다.

그만큼 생각을 많이 하게 만들고 따라서 코틀린을 처음 접하시는 분들에게는 권하고 싶지 않은 책이다.

# 자바에서 말하는 Generic의 두 가지 개념을 먼저 살펴보자.

코틀린의 공식 사이트에서 Generic에 대해서 중요한 개념들을 설명하긴 하지만 사실 자바를 알고 있다는 가정하에 작성된 느낌이 강하다.

그리고 밑에 펼쳐지는 내용은 조슈아 블로크의 [이펙티브 자바]의 내용을 상당수 인용하고 있는데 그것만으로는 부족할 수 있기 때문에 작성한 챕터이다.   

따라서 선지식으로 알고 가는 부분이 될 것이고 이에 잘 아시는 분들이라면 그냥 건너 띄어도 된다. 

하지만 만일 제네릭을 그냥 가볍게 지나쳤다면 한번 쯤 생각해볼 시간이 될 수 있을 것이다.

[이펙티브 자바]에서는 `공변 (covariant)`과 `불공변 (invariant)`에 대해서만 단어로 언급하고 있다.      

내용을 더 깊게 들어가면 실제로 한가지가 더 있는데 내용상으로 관련 내용을 설명하고 있지 실질적으로 `반공변, (contvariant)`을 직접 언급하진 않는다.       

하지만 코틀린의 공식 사이트에서는 이 부분을 언급하고 있다.

이와 관련해서 내용들을 살펴보면 가장 쉽게 설명하기 좋은 아이템이 바로 `Number`를 상속한 일렬의 Wrapper Class들이다. 

# 공변과 불공변, covariant and invariant

`Integer`, `Double`, `Float`, `BigDecimal`같은 녀석들은 `Number`를 `extends`, 즉 상속을 하고 있다. 

그리고 최상위 객체는 `Object`라는 것은 자바를 공부하면 자연스럽게 알게 된다.

아무튼 아래 코드를 보면 이것은 너무나 당연하게 느껴진다.

```java
public class Main {
    public static void main(String[] args) {
        Long value = 1_000L;
        Number longValue = value;
    }
}
```
그런데 이런 생각을 해볼 수 있다.

저런 관계가 성립하는데 그러면 다음 코드도 성립해야 하는거 아냐?

```java
public class Main {
    public static void main(String[] args) {
        List<Long> longList = List.of(1L, 2L, 3L);
        List<Number> numberList = longList;
        //List<Number> numberList = List.of(1L, 2L, 3L); 이건 가능하다.
    }
}
```
하지만 IDE는 여러분에게 화를 낼것이다. 타입이 맞지 않으니 numberList를 `List<Long>`로 바꾸라고!      

바로 조슈아 블로크의 [이펙티브 자바]의 `아이템 28 배열보다는 리스트를 사용하라`에서 언급하는 내용이다.

```
배열과 제네릭 타입에는 중요하 차이가 두 가지가 있다. 첫 번째, 배열은 공변(covariant)이다.
어려워 보이는 단어지만 뜻은 간단하다. Sub가 Super의 하위 타입이라면 배열 Sub[]는 배열 Super[]의 하위 타입이 된다.
(공변, 즉 함께 변한다는 뜻이다). 반면 제네릭은 불공변(invariant)다.

즉, 서로 다른 타입 T와 U가 있을 때, List<T>는 List<U>의 하위 타입도 아니고 상위 타입도 아니다.
.
.
.
중략
```
이에 대한 관련 테스트 코드는 다음과 같다.

```java
public class Main {
    public static void main(String[] args) {
        Number[] numbers = new Long[3];
        numbers[0] = 1_000L;
        numbers[1] = 1.1;
        for(int i = 0 ; i< numbers.length; i++) {
            System.out.println(numbers[i]); // but java.lang.ArrayStoreException: java.lang.Double
        }
        // 컴파일 에러
        // String은 Object의 하위 타입이다.
        // 하지만 책의 내용대로 List<String>은 제네릭의 하위 타입 규칙에 의해
        // List의 하위 타입이지만 List<Object>의 하위 타입이 아니다.
        List<String> strList = List.of("A", "B", "C");
        List<Object> list = strList;
        //List<Object> list = List.of("A", "B", "C"); // 이건 가능
    }
}
```
배열에 대한 테스트 코드를 보면 분명 책에서 언급한 대로 공변이다.    

그런데 위 책에서 언급한 `반면 제네릭은 불공변(invariant)다`에서 알 수 있듯이 `Number`와 `Long`이 상속 관계로 `Long`이 하위 타입 관계임에도 제네릭에서는 유지되지 않는다.       

그 이유는 역시 책에서도 언급되고 코틀린의 공식 사이트에서도 언급되는데 컴파일 시점에 바로 `타입 소거`가 이뤄지기 때문이다.      

## 타입 소거

`타입 소거`에 대한 메카니즘에 대한 배경은 [이펙티브 자바]의 `아이템 26. raw type은 사용하지 말라`에 아주 잘 나와 있다.

여기에서 자바가 타입 소거를 사용하는 이유도 함께 설명하고 있다.

간략하게 설명하면

```
제네릭이 도래하기 전 코드와 호환되도록 하기 위한 궁여지책.....
```

이런 이유로 아래 코드를 살펴보면
```java
public class Main {

    public static void main(String[] args) {

        List<String> strList = List.of("A", "B");
        
        //if(strList instanceof List<String>) { 에러
        //if(strList instanceof List<?>) { 비한정적 와일드 카드 ?를 사용한는 것은 가능하다
        if(strList instanceof List) { // Ok right syntax
            
        }
        
    }
}
```
위와 같다. 

위 코드의 내용을 볼때 `List<String>`이라고 명시적으로 객체를 생성했다 할지라도 실제 `instanceof`의 코드를 보면 그냥 단지 `List`일 뿐이다.

즉 `타입 소거`가 이뤄지면서 어떤 타입의 원소를 저장했는지는 실행시점에서는 알 수 없다는 의미이다.

이런 흔적은 또한 `ObjectMapper`를 활용해 `json String <--> Object` 변환을 시도할 때 볼 수 있다.     

여기서 만일 여러분들이 리스트 형식, 예를 들면 `List<MyEntity>`를 json 스트링 형식으로 변환할 때는 장담컨데 여러분들은 다음과 같이 코드를 작성할 수 없다.    

```
mapper.readValue(content, List<MyEntity>.class);
```
`타입 소거`문제로 이것을 우회하기 위해 `TypeReference<T>`같은 걸 사용하게 된다.

결국 위에서 훝어본 테스트 코드들을 보면 이런 이유로 JVM 입장에서는 List라는 객체의 존재만 알지 파라미터로 넘어온 타입이 `Long`인지 `Number`인지 알지 못한다는 것이다.     

# 공변, covariant

책에서는 배열을 예로 들며 이 단어를 설명하고 있지만 만일 자바에서 제네릭으로 이런 공변성을 가지고 싶은 경우가 있다.     

이 때는 `한정적 와일드 카드 타입`으로 선언하면 된다. 위 코드에서 `?`를 볼 수 있는데 일반적으로 이것을 `와일드 카드`라고 한다.

`extends`와 `와일드 카드`의 조합을 통해서 `한정적 와일드 카드 타입`으로 선언이 가능하다.


```java
public class Main {
    public static void main(String[] args) {
        List<Long> longList = List.of(1L, 2L, 3L);
        //List<Number> numberList = longList;
        List<? extends Number> numberList = longList;
    }
}
```
이렇게 선언하게되면 클래스의 하위 타입 관계가 제네릭스에서도 유지되는데 이것을 `공변성`이라고 한다.

말로 풀면 이것은 `Number의 하위 타입이어야 한다`는 의미를 가지고 있고 그것에 대한 표현 방법이다.

근데 곰곰히 생각을 해보자.

이런 경우에는 우리가 `numberList`로부터 요소를 가져오는 것은 문제없이 잘 작동할 것이다.     

단지 가져오는 행위만 할테니까.

```java
public class Main {
    public static void main(String[] args) {
        List<Long> longList = List.of(1L, 2L, 3L);
        //List<Number> numberList = longList;
        List<? extends Number> numberList = longList;
        numberList.stream()
                  .forEach(System.out::println);
        //numberList.add(100L); numberList type to List<Long>
        //numberList.add(10); numberList type to List<Integer>
    }
}
```
위에서 알 수 있듯이 리스트에 요소를 추가하려는 순간 컴파일시점에서 위와 같은 에러 메세지를 보여준다.      

물론 `List.of` (또는 Arrays.asList)가 불변 리스트, 즉 `ImmutableCollections`이기에 저 코드 자체는 컴파일 오류가 발생하지 않았도 런타임시 에러가 발생할 것이다.

uoe, UnsupportedOperationException말이다.       

하지만 이미 그 전에 컴파일 시점에서 타입 오류가 발생한다.    

이유가 무엇일까? 그것은 마법같은 `?`을 이용해 설정했지만 리스트입장에서는 좀 다르다.

비록 `Long`이 `Number`를 상속받았지만 파라미터로 들어온 타입이 뭔지 알 수 없기 때문이다.        

여기서 우리는 가변 또는 불변 컬렉션에 대해 생각해 볼 문제가 하나 존재하게 된다.       

공변적으로 선언한 제네릭으로 활당한 객체의 요소를 가져오는 일종의 `read`에 대해서는 가능하지만 요소를 추가하거나 하는 `write`에 대해서는 문제가 발생한다.

가만히 이런 행위나 개념을 살펴보면 이것은 producer, 즉 생산자에 해당한다.

위 예제에서도 알 수 있듯이 넘어온 파라미터를 소비해서 리스트 객체에 담지 못한다는 것이다.   

이런 고민은 앞으로 코틀린에서 제네릭스를 설명할 때 중요한 요소이기 때문에 기억해 둘 필요가 있다.

## 반공변성, contravariant

콘트라스트라는 용어가 뭔지 잘 알것이다. 대비라는 의미인데 즉 이것은 반대의 개념으로 볼 수 있다.     

자바를 하다보면 여러분들은 이런 반공변적으로 선언된 제네릭을 볼 수 있다. 

공변성과 비슷하지만 `extends`가 아닌 `super`로 설정한 코드들을 볼 수 있다.       

이것은 `super`키워드 뒤에 오는 타입의 상위 타입이어야 한다는 의미이다.

위 공변성 테스트 코드에서 보면 `read`는 가능한데 `write`를 못하는 것이 좀 억울해 보일 수 있다.    

이런 경우 반공변성으로 선언해서 가능하게 할 수 있다.

```java
public class Main {

    public static void main(String[] args) {
        List<Number> anyNumberList = new ArrayList<>(List.of(BigDecimal.ZERO, 100L, 1.1, 10F));
        List<? super Long> superNumberList = anyNumberList;
        superNumberList.stream()
                       .forEach(System.out::println);
        superNumberList.add(1000L);
    }

}
```
위에서 언급했듯 `Number`를 상속한 wrapper class를 담은 리스트가 존재한다고 보자.

물론 forEach를 통해 Iterater로 돌면서 콘솔에 찍는 것은 잘 될 것이다.     

근데 위 코드를 가만히 살펴보면 관계가 역전된 것처럼 보인다.

마치 `Number`가 `Long`의 하위 타입인것처럼 보이기 때문이다.

```
List<? super Long> 나 List<? super Integer>
```

위 코드 표기법은 `Long`이나 `Integer`의 상위 타입이어야 한다는 것을 의미한다.

물론 지금 자바 버전에서는 `BigInteger`, `BigDecimal`를 제외하고 나머지는 final class라 상속할 수 없겠지만 테스트로 한번 바꿔서 해보길 바란다.

여기서 우리는 `write`가 가능한 반공변성의 특징을 기억해 둘 필요가 있다. 

그리고 `contra`의 의미로 producer가 아닌 consumer, 즉 소비자에 해당하게 된다.

# 펙스(PECS)
조슈아 블로크의 [이펙티브 자바]에서는 `아이템 31 한정적 와일드카드를 사용해 API 유연성을 높이라`에서 이와 관련 공식같은 이야기를 한다.

```
펙스(PECS): producer-extends, consumer-super
즉, 매개변수화 타입 T가 생산자라면 <? extends T>를 사용하고, 소비자라면 <? super T>를 사용하라.
```

또한 `Comparable`과 `Comparator`에 대해서는 소비자로 설명하고 있다.        

그리고 위에 `ObjectMapper`에 대한 이야기로 `닐 캐프터`의 글과 `슈퍼 타입 토큰`을 언급한다.

`슈퍼 타입 토큰`에 대해 구글링해보자. 바로 `TypeReference`에 대한 내용을 얻을 수 있다.      

또한 이와 관련 `RestTemplate`에서 Spring에서 제공하는 `ParamterizedTypeReference`에 대해서도 구글링 해보시길 바란다.

뭐... open-feign-client를 사용하면 `RestTemplate`을 사용할 일이 있을까만은 레가시는 분명 존재하니깐...

어째든 제네릭을 포함한 이 챕터의 내용은 사실 여기서 전부 소개하기에는 방대한 내용이기에 이정도만 소개해 본다.        

분명 조슈아 블로크의 [이펙티브 자바]는 사야할 이유가 많다.

# Kotlin Generics

위에서 훓어 본 내용들이나 여러분들이 자바 서적에서 배운 제네릭스를 토대로 코틀린의 제네릭스를 한번 살펴 보는게 목적이다.     

## 타입 파라미터 

일반적인 제네릭 선언은 자바와 다르지 않다.     

다만 컬렉션 관련 내용에서 확인할 수 있듯이 자바는 `<>`를 호출하는 객체 뒤에 붙여서 표현하지만 코틀린은 `listOf<String>`처럼 함수 뒤에 표시하는 것이 차이가 있다.     

자바는 위에서 내용에서 언급했듯이 여전히 `raw type`을 허용하지만 코틀린은 반드시 타입 인자로 받은 타입을 지정해야한다.

다음 자바 코드와 코틀린의 차이를 살펴보자.


```java
public class Main {
    public static class Generics<T> {
    }

    public static void main(String[] args) {
        Generics generics = new Generics();
        List list = new ArrayList();
    }
}
```
Generics 클래스나 List를 인스턴스화 할때 타입 파라미터를 선언하지 않으면 `raw type`으로 받아친다.       

하지만 코틀린은 위에 언급한대로 

```kotlin
class Generics<T>(
    val data: T,
)

class GenericsOne<T>

fun main() {
    val generics = Generics("String value")
    val genericsOne = GenericsOne<String>()
    // Not enough information to infer type variable T
    // val genericsOne = GenericsOne() 
}
```
생성자가 있는 클래스의 경우에는 생성자에 들어오는 값 타입으로 타입 파라미터라 무엇인지 추론이 가능하다.      

하지만 어찌되었든 코틀린은 `raw type`은 허용하지 않는다.

다만 코틀린의 생성자를 통해서 상위 클래스 생성자에 대한 위임 호출은 예외이다.

```kotlin
class ConcreteType<T>(
    data: T,
): Normal(data) // One type argument expected for class Normal<T>

open class Normal<T>(
    val data: T,
)
```
`ConcreteType`이 `Normal`을 상속할 때 생성자로 들어온 `data`의 타입을 통해서 추론이 가능할 것처럼 보이지만 그렇지 않다.

따러서 다음과 같이 타입 인자를 명시적으로 표시해서 타입 파라미터를 상위 타입의 타입 인자로 전달해야 한다. 

```kotlin
class ConcreteType<T>(
    data: T,
): Normal<T>(data)

open class Normal<T>(
    val data: T,
)
```
즉, 타입 파라미터를 상속하지 않는다는 것을 확인할 수 있다.       

이것을 통해 알 수 있는 것은 `ConcreteType`와 `Normal`의 T는 서로 다른 선언이라는 것이다.     

굳이 저 두개의 클래스가 T로 같을 필요는 없다.

```kotlin
class ConcreteType<T>(
    data: T,
): Normal<T>(data)

open class Normal<U>(
    val data: U,
)
```
위 코드는 잘 작동한다.

## Bounded Type Parameter

[이펙티브 자바]에서는 `한정적 타입 매개 변수`라고 하는 것에 대해 설명하고 있다.

어찌되었든 저 위에 코드를 살펴보면 재미있는 것은 마법같은 `?`을 사용했다 해도 들어오는 타입 파라미터에는 제한이 된다.     

그보다는 `정규 타입 매개변수`를 이용해서 이를 좀더 제네릭하게 표현하게 되는데 일반적으로 다음과 같은 형식을 띄게 된다.

```
List<E extends Number>
```

다음 코드를 보자.

```java
public class Main {

    public static class Generics<T extends Number> {
        private final T generic;
        
        public Generics(T generic) {
            this.generic = generic;
        }

        public T getGeneric() {
            return generic;
        }
    }

    public static void main(String[] args) {
        Generics<Long> generics = new Generics(1000L);
        Long getValue = generics.getGeneric();

        // Type parameter 'java.lang.String' is not within its bound; should extend 'java.lang.Number'
        // Generics<String> stringGenerics = new Generics("String");
    }
}
```
타입 파라미터에 제약을 걸게 된다.

코틀린은 그렇다면 어떻게 표현할까?

```kotlin
class Generics<T: Number>(
    generic: T,
) {
    private val generic: T = generic

    fun getGeneric(): T = this.generic
}

fun main() {
    val generics = Generics(1_000L)
    val generic = generics.getGeneric()
}
```
코틀린은 `:`를 통해서 바운드에 제약을 두는 방식이다.       

만일 이 `Number`로 바운드 제약 이외에도 다른 `Number`타입이랑 비교를 하고 싶은 경우가 있다.       

자바에 경우에는 다음과 같이 여러 개의 바운드 제약을 거는 방식을 선택할 수 있다.

```java
public class Main {

    public static class Generics<T extends Number & Comparable<T>> {
        
        private final T generic;

        public Generics(T generic) {
            this.generic = generic;
        }

        public T getGeneric() {
            return generic;
        }
        
        public boolean gt(T target) {
            return generic.compareTo(target) > 0;
        }
        
    }

    public static void main(String[] args) {
        Generics<Long> longGenerics = new Generics(1000L);
        System.out.println(longGenerics.gt(100L));
    }

}
```
이 때는 `&`를 통해 바운드 제약을 여러 개 둘 수 있다.

코틀린은 이와는 다르게 상위 바운드 제약을 하나만 지정할 수 있게 되어 있다. 그래서 아래와 같이 조금은 복잡한 방식으로 `where`로 처리한다.

```kotlin
class Generics<T: Number>(
    generic: T,
) where T: Comparable<T>, T: AnyInterface<T>, T: OtherInterface<T> {
    private val generic: T = generic

    fun getGeneric(): T = generic

    fun gt(target: T): Boolean = generic > target
    
}

fun main() {
    val generics = Generics(1_000L)
    println(generics.gt(10_000L))
}
```
단 이 때는 `where`라는 키워드 뒤에 `,`를 통해 한 건씩 바운드 제약을 걸어줘야 한다. 

어떤 면에서는 자바쪽이 좀 괜찮아 보인다.         

## 타입 소거

코틀린 역시 주요 플랫폼이 JVM 이기에 이런 문제에서 자유로울 수는 없다.        

하지만 자바와는 좀 다른 경향이 있다.

```Kotlin
fun main() {
    val list = listOf(1,2,3,4)
    println(list is List)
    println(list is List<Int>)
    // 당연하겠지만 아래 코드는 Cannot check for instance of erased type: List<String>
    //println(list is List<String>)
    println(list is List<*>)
}
```
근데 맨 아래 코드에서 `*`를 사용한 것을 볼 수 있다.      

여기에서 `*`은 알지 못하는 타입을 의미하고 타입 인자를 대신하게 된다.     

이 코드는 원소 타입에는 관심이 없고 단지 리스트인지만 확인할 때 사용할 수 있다.       

어떻게 보면 `?`와 상당히 비슷해 보인다.

이 부분은 `프로젝션`이라는 특별한 경우로 차후 따로 설명을 해볼까 한다.       

## as 또는 as?를 활용한 제네릭 타입 캐스팅?

이 `타입 소거`로 인해 독특한 현상이 발생한다.       

다음 코드를 살펴보자.

```kotlin
fun main() {
    val stringList = listOf(1,2,3,4) as List<String> // Unchecked cast: List<Int> to List<String>
    val longList = listOf(1,2,3,4) as List<Long> // Unchecked cast: List<Int> to List<Long>
}
```
어떤 오류도 발생하지 않는다. 바로 `타입 소거`때문이다.      

다만 IDE에서는 `as`부분에 노란 표시가 뜨며 `unchecked cast`이라는 그저 경고만 보여줄 뿐이다. 

마치 제네릭스의 이런 한계를 우회하는 것처럼 보일 수 있지만 실제로는 런타임까지 오류를 미루는 효과가 발생한다.

```kotlin
fun main() {
    val stringList = listOf(1,2,3,4) as List<String>
    val longList = listOf(1,2,3,4) as List<Long>

    val value = stringList[0]
    println(value) // Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String 
}
```
하지만 실행하고 나면 런타임에 `ClassCastException`이 발생한다.        

주의해야 하는 부분으로 이런 경우에는 오류를 찾기가 쉽지 않다.     

코드상으로 그냥 비검사로 경고만 보여주기 때문에 특별한 컴파일 오류를 보여주지 않고 런타임시점에서야 오류가 나기 때문이다.    

# 변성

코틀린에서 예를 들면 코틀린 내 Array클래스나 가변 컬렉션, 그리고 앞서 만들었던 Generics클래스는 무공변이다.       

코드를 보면 알 수 있듯이 타입 인자들 사이에 하위 타입 관계가 있더라도 서로 아무 관계도 없는 타입으로 간주한다.


```kotlin
class Generics<T>(
    val data: T,
)

fun main() {
    // 배열 및 가변 컬렉션
    val strArray = arrayOf("a", "b", "c")
    val anyArray: Array<Any> = strArray // Type mismatch.

    val mutableList = mutableListOf("a", "b", "c")
    val mList: MutableList<Any> = mutableList // Type mismatch.

    val mutableSet = mutableSetOf("A", "B")
    val mutableAnySet: MutableSet<Any> = mutableSet // Type mismatch.
    
    val stringGenerics = Generics("")
    val anyGenerics: Generics<Any> = stringGenerics // Type mismatch.
    
    // 불변 컬렉션    
    val immutableList = listOf("a", "b", "c")
    val imList: List<Any> = immutableList

    val immutableSet = setOf("A", "B")
    val immutableAnySet: Set<Any> = mutableSet
}
```
위 코드를 보면 알 수 있다.      

코틀린에서 이런 이유는 제네릭 타입이 자신의 타입 파라미터를 어떻게 취급하는지에 따라 결정된다.

이미 이 부분은 [이펙티브 자바]에서도 설명한 것으로 다음 세 가지로 분류가 된다.

1. producer: 타입 파라미터의 값을 반환하는 연산만 제공하고 타입 파라미터의 값을 입력으로 받는 연산은 제공하지 않는 제네릭 타입인 생산자
2. consumer: 타입 파라미터의 값을 입력으로 받기만 하고 결코 타입 파라미터의 값을 반환하지 않는 소비자
3. nothing: 1과 2경우에 해당하지 않는 타입들

여기서 3번에 해당하는 경우에는 좀 독특하다.

생산자도 아니고 소비자도 아닌 타입의 경우에는 제네릭에서 타입 안정성을 깨지 않고는 하위 타입 관계를 유지할 수 없다는 것이다.

이 내용은 [이펙티브 자바]의 `아이템 28 배열보다는 리스트를 사용하라`에 이 부분을 설명하고 있는데 이 내용을 기준으로 살펴보자.

```java
// [이펙티브 자바]에서는 이 코드가 된다는 가정에서 설명하고 있다.
// 이와 관련 책을 한번 구입해 보고나 검색을 해보길 바란다.
public class Main {
    public static void main(String[] args) {
        List<String>[] stringLists = new List<String>[1]; // (1)
        List<Integer> intList = List.of(42);              // (2)
        Object[] objects = stringLists;                   // (3)
        objects[0] = intList;                             // (4)
        String s = stringLists[0].get(0);                 // (5)
    }
}
```

그러면 위 내용을 우리가 이전에 만든 Generic클래스를 좀 확장해서 설명을 해보자.

```kotlin
class Generics<T>{
    
    private val genericList = mutableListOf<T>()
    
    fun getGenerics(): List<T> {
        return genericList
    }
    
    fun addGeneric(element: T) {
        genericList.add(element)
    }
    
}

fun main() {
    val stringGenerics = Generics<String>()
    val anyGenerics: Generics<Any> = stringGenerics     // (1)
    anyGenerics.addGeneric(1_000L)                      // (2)
    stringGenerics.getGenerics().first()                // (3)
}
```
실제로는 위 코드는 컴파일 오류를 내겠지만 (1)이 된다고 가정을 해보자.      

이제는 (1)처럼 `Generics<Any>`가 성립되기 때문에 (2)에서 처럼 `Long`타입의 요소를 넣을 수 있게 된다.   

이제부터가 문제다. 

스트링만 담겠다고 선언한 stringGenerics에는 지금 내부에 존재하는 genericList `Long`이 저장돼 있다.

그리고 (3)는 이 객체의 저장된 리스트에서 첫 원소를 꺼내려한다.

컴파일러는 꺼낸 원소를 자동으로 String으로 형변환하는데, 이 원소는 `Long`이므로 런타임에 `ClassCastException`이 발생한다.

이런 일을 방지하려면 (제네릭 배열이 생성되지 않도록) (1)에서 컴파일 오류를 내야 한다.

위 내용은 [이펙티브 자바]에서 설명하는 것을 훙내내서 해 본 것인데 자바에서는 배열 대입으로 발생할 수 있는 `ArrayStoreExcpetion`이 있다.

그런데 코틀린에서 배열 타입은 자바와는 좀 다르다. 

실제로 코틀린에 대한 예제 코드로 알 수 있듯이 코틀린의 배열 타입은 [이펙티브 자바]에서 설명한 배열의 `공변성`이 성립하지 않는다. 

## 공변성, covariance

```kotlin
fun main() {
    val strArray = arrayOf("a", "b", "c")
    val anyArray: Array<Any> = strArray // type mismatch
}
```

아래 코드는 참 지겹게도 보는 코드이다.
```java
public class Main {

    public static void main(String[] args) {
        List<String> strings = List.of("A", "B", "C");
        strings.add("test");
    }
}
```
`List.of`는 `ImmutableCollections`로 생성하기 때문에 불변 리스트이다.      

하지만 불변 리스트는 소비할 수 없는 리스트이다. 

그럼에도 컴파일 시점에서는 저 위의 코드가 잘 작동한다.     

다만 런타임 시점에서야 `uoe`가 발생한다는 것은 불변 리스트를 아는 사람이라면 잘 아는 사실이다.

그런데 코틀린에서 불변 컬렉션은 생산만 할 수 있다.

아래 코드를 살펴보자.

```kotlin
fun main() {
    val mutableList = mutableListOf("a", "b", "c")
    mutableList.add("test")

    val immutableList = listOf("a", "b", "c")
    // Unresolved reference: add
    //immutableList.add("test")
}
```
애초에 코틀린에서는 불변 컬렉션은 생산만 하고 소비하지 않는다.     

그래서 자바와 달리 `add`함수 자체를 제공하지 않는다.     

이 이야기는 코틀린 관련 책에서도 잘 설명하는 부분이기도 하다.

불변 컬렉션 `List<Any>`는 `Any`타입의 값을 돌려주고 `List<String>`는 `String`타입의 값을 돌려준다.    

`String`은 `Any`의 하위 타입이라는 것은 너무나 당연하 사실이다.     

이로 인해 `List<String>`은 자동적으로 `Any`의 타입의 값을 돌려줄 수 있다.    

좀 어려운 말이지만 컴파일러 입장에서는 불변 리스트 `List<String>`이 `safe-type, 타입 안정성`을 해치지 않고 `List<Any>`쓸 수 있다고 보는 것이다.

왜냐하면 소비하지 않고 생산만 하니까!!!!!

이 개념이 이해가 가는가?

이것을 제네릭 타입이 타입 인자에 대해 공변적 (covariant)라고 말한다.    

지금까지 설명하면서 결국 똑같은 말이 되는데 생산자 역할을 하는 타입은 모두 공변적이라고 말할 수 있다.       

대표적인 예로 `Pair`, `Triple`, `Iterable`, `Iterator`등 코틀린의 불변 타입들이 이에 해당한다.

그리고 다음과 같은 코틀린의 람다 표현식처럼 함수 타입은 반환되는 타입에 대해서도 공변적이다.

```kotlin
fun main() {
    val stringProducer: () -> String = { "hello" }
    val anyProducer: () -> Any = stringProducer
}
```

## 가변 타입이 공병적으로 동작할 수 없는거니?

이 내용은 [코틀린 완벽 가이드]의 내용을 발췌한 것으로 설명하기 좋다고 판단되서 그 내용을 토대로 작성해 본 것이다.

위의 내용을 잘 생각해보면 `공변성` 또는 `공변적이다`라는 것은 타입 파라미터를 입력으로 사용하지 못하게 한다는 것을 알 수 있다.     

불변 컬렉션의 `add()`함수를 떠올려 보자. 자바와는 코틀린에서는 함수 자체를 제공하지 않는다.

하지만 가변 타입 또는 가변 컬렉션도 공변적으로 만들 수 있지 않을까?

만일 다음과 같은 인터페이스가 있다고 생각해 보자.

```kotlin
interface OnlyReturn<T> {
    fun getSize(): Int
}
```
이 인터페이스를 구현하는 객체의 타입 파라미터 T를 받긴 하겠지만 실제로 이 인터페이스의 역활은 어떤 size정보를 주기만 할 수 있다.    

`OnlyReturn<String>`이든 `OnlyReturn<Int>`든 간에 최상위 타입 `OnlyReturn<Any>`역할을 모두 할 수 있는 구조이다.    

근데 불변 컬렉션 또는 불변 객체는 공변적이면서도 어떤 때는 공변적이 아닐 수도 있다.

```kotlin
interface Set<T> {
    fun contains(element: T): Boolean
}
```
위 타입은 불변적이지만 생산자는 아니다. 따라서 Set의 타입 파라미터 T는 하위 타입 관계를 유지하지 않는다.     

예들 들면 `Set<Any>`는 어떤 값도 받을 수 있지만 `Set<String>`나 `Set<Int>`같은 경우에는 함수 `contains(element: T)`에서도 알 수 있듯이 해당 타입 파라미터만 받을 수 있다.

그리고 그 해당 파라미터의 값이 포함되어 있는지 알려 줄 뿐이다.

## 캐스팅 문제로 인한 하위 타입 관계 유지를 못한다?

소비자 역할을 하는 타입은 분명 파입 파라미터의 하위 타입 관계를 유지해주지 못한다는 말은 결국 업/다운 캐스팅이 벌어진다는 의미로 받아드릴 수도 있다.

하지만 이런 타입은 그 관계의 역방향으로 유지해준다는 것을 알 수 있다. 

예를 들면 위 `Set<T>`인터페이스를 보면 `Set<Number>`와 그 `Set<Long/Int...>`와의 관계에서 알 수 있다.      

`Int`나 `Long`같은 객체는 `Number`를 상속하고 있는 하위 타입이다. 

이미 위의 자바의 `반공변성, contraviant`설명한 이야기이다.

여기서 `Set<Number>`는 `Int`나 `Long`값을 처리할 수 있다.       

오히려 `Set<Number>`가 마치 `Set<Long/Int...>`의 하위 타입처럼 동작하는 것을 알 수 있다.      

이런 경우를 반공변적이라고 말할 수 있다.

다음은 함수 타입은 인자 타입에 대해 반공변적인 예를 보여주는 코드이다.      

```kotlin
fun main() {
    //val anyConsumer: (Any) -> Unit = ::println
    val anyConsumer: (Any) -> Unit = { println(it) }
    // Any가 마치 String의 하위 타입으로 동작하는 것처럼 보인다.
    val stringConsumer: (String) -> Unit = anyConsumer
    stringConsumer("HI! There~")
}
```

지금까지의 내용을 토대로 다음과 같이 정리해 볼 수 있다.

1. 어떤 객체 X가 생산자 역할을 한다. 이 경우 X<T>의 타입 파라미터 T를 공변적으로 선언할 수 있고, T가 U의 하위 타입이면 `X<T>`도 `X<U>`의 하위 타입이 된다.
2. 어떤 객체 X가 소지바 역할을 한다. 이 경우 X<T>의 타입 파라미터 T를 반공변적으로 선언할 수 있고, U가 T의 하위 타입이면 `X<T>`가 `X<U>`의 하위 타입이 된다.
3. 어떤 객체 X가 1과 2에 해당하지 않으면 X<T>에 대해서 X는 타입 파라미터 T에 대해 무공변이다.

참고로 2번 반공변을 설명한는 부분은 타입 파라미터들간의 관계를 주의 깊게 살펴봐야 한다.

아무튼 일반적으로 변성을 표현하는 데는 두 가지 방식이 있다.      

하나는 타입 파마리터의 변성을 선언 자체로 지정하거나 타입 인자를 치환하는 사용 지점에서 지정하는 방식이다.

## 선언 지점 변성

위에 두 가지 방식중 `타입 파마리터의 변성을 선언 자체로 지정`하는 방식이 `선언 지점 변성`이다.

일단 위의 자바에서 사용하던 방법과 코틀린의 방법을 요약해서 먼저 알아보자.

```
1. 공변성
자바: X<? extends T>
코틀린: X<out T>

2. 반공변성
자바: X<? super T>
코틀린: X<in T>
```
자바에서는 우리는 위에서 설명한 내용을 토대로 보면 `?`와 `extends/super` 이 두개의 키워드를 통해서 변성을 지정한다.

위에서 설명한 것처럼 X라는 클래스가 있다면 `class X<out T>`, `class X<in T>`처럼 사용하게 되는데 이것은 클래스 전역에 변성을 설정하게 된다.

즉, 이것을 `in`, `out`라는 개념으로 쉽게 풀어간다.

또한 `선언 지점 변성`은 단 한번만 표시하면 해당 클래스를 사용하는 곳에서 전역적으로 변성에 대해 신경쓰지 않아도 되기 때문에 간결함을 유지할 수 있다.   

이제 이런 내용을 이해했다면 다음 코드를 통해 재미있는 현상을 볼 수 있다.

```kotlin
fun main() {
    val mutableList: MutableList<Long> = mutableListOf(1L, 2L, 3L)
    mutableList.add(100L)
    
    val mutableListOut: MutableList<out Long> = mutableListOf(1L, 2L, 3L)
    // The integer literal does not conform to the expected type Nothing
    //mutableListOut.add(100L)
}
```
맨 위 코드는 가변 컬렉션을 이용한 코드로 `add`함수를 통해 T값 즉, 롱 타입의 100L을 소비하는 것은 자연스러운 일이다.      

하지만 밑에 코드는 `out`키워드를 통해 제한을 두는 코드이다.        

배열과 가변 컬렉션은 불공변인 것을 알 것이다. 

하지만 이 가변 컬렉션의 타입 파라미터를 공변적으로 만들면 소비하는 부분에서 컴파일 오류가 발생한다.     

이유는 타입 파라미터를 값으로 입력받아 소비하는 `add()`함수가 문제가 되기 때문이다. 

```
타입 파라미터 T가 항상 out 위치에서 쓰이는 경우에만 공변적으로 선언할 수 있다.      

out 위치는 기본적으로 값을 만들어내는 위치다. 프로퍼티나 함수의 반환값 타입이나 제네릭 타입의 공변적인 타입 파라미터 위치가 out 위치다.
```
말이 어려운데 밑에 코드를 보면 타입 파라미터 T가 항상 out 위치에서만 사용되기 때문에 문제가 없다.

```kotlin
fun main() {
    val mutableListOut: MutableList<out Long> = mutableListOf(1L, 2L, 3L)
    // The integer literal does not conform to the expected type Nothing
    //mutableListOut.add(100L)
    println(mutableListOut[0])
    println(mutableListOut.subList(1, 3))
}
```

`out`과 마찬가지로 아래처럼 반공변인 타입 파라미터 앞에 in 키워드를 붙여서 사용한다.    

```kotlin
fun main() {

    val mutableNumberList: MutableList<Number> = mutableListOf(1L, 2L, 3L, 100, 1.1)
    mutableNumberList.add(100L)

    //val mutableListLong: MutableList<Long> = mutableNumberList -> type mismatch
    // 2. 어떤 객체 X가 소지바 역할을 한다. 이 경우 X<T>의 타입 파라미터 T를 반공변적으로 선언할 수 있고, U가 T의 하위 타입이면 X<T>가 X<U>의 하위 타입이 된다.
    // Long이 Number의 하위 타입이기 때문에 MutableList<Number>가 MutableList<Long>의 하위 타입이 된다. 
    val mutableListIn: MutableList<in Long> = mutableNumberList
    mutableListIn.add(100L)
    println(mutableListIn[2])
    println(mutableListIn.subList(1, 6))
}
```

## 프로젝션을 사용한 사용 지점 변성

`선언 지점 변성`과는 다르게 제네릭 타입을 사용하는 위치에서 특정 타입 인자 앞에 `in/out`붙이는 `프로젝션`이라는 방식이 있다.    

보통 무공변인 타입일 때 함수의 내용에 따라 생산자나 소비자로만 쓰고 싶을 때 유용한 방법이다.     

예를 들면 우리가 앞서 확장에서 만든 Generics클래스를 좀더 확장해서 다시 한번 살펴보자.

```kotlin
class Generics<T>{
    
    private val genericList = mutableListOf<T>()
    
    fun getGenerics(): List<T> {
        return genericList
    }
    
    fun addGeneric(element: T) {
        genericList.add(element)
    }

    fun addSubGenerics(generics: Generics<T>) {
        generics.getGenerics().forEach { genericList.add(it) }
    }
    
}

fun main() {

    val rootGen = Generics<String>()
    rootGen.addGeneric("test")

    val subGen = Generics<String>()
    subGen.addGeneric("test2")


    rootGen.addSubGenerics(subGen)
    println(rootGen.getGenerics())


    val intRootGen = Generics<Number>()
    intRootGen.addGeneric(1)
    intRootGen.addGeneric(2)

    val intSubGen = Generics<Int>()
    intSubGen.addGeneric(3)
    intSubGen.addGeneric(4)
    // Type mismatch.
    //intRootGen.addSubGenerics(intSubGen)
}
```
당연히 같은 타입 파라미터를 가진 Generics에 대해서는 어떤 문제도 발생하지 않지만 아래 `Number`와 `Int`의 경우에는 컴파일 오류가 발생한다.

Generics클래스는 이도 저도 아닌 무공변 클래스이다.     

이제부터 Number와 그 하위 타입인 `Long`이나 `Int`의 대해서 얘기를 해볼까 한다.      

위 코드에서 `addSubGenerics()`함수를 문제없이 사용하고 싶으면 어떻게 해야 할까?     

실제로 해당 메소드로 들어오는 intSubGen의 타입 파라미터 T는 `Int`로  intRootGen의 타입 파라미터인 `Number`의 하위 타입이다.        

이런 관계를 유지하기 위해서 공변적으로 변경하므로써 해당 함수의 파라미터인 `generics: Generics<T>`를 사용하게 만들면 된다.     

따라서 사용 시점에 해당 함수의 파라미터로 들어오는 T를 `out`을 통해 공변적으로 만들면 된다.

```kotlin
class Generics<T>{

    private val genericList = mutableListOf<T>()

    fun getGenerics(): List<T> = genericList

    fun addGeneric(element: T) {
        genericList.add(element)
    }

    fun addSubGenerics(generics: Generics<out T>) {
        generics.getGenerics().forEach { addGeneric(it) }
    }
}
```
이렇게 `Generics<out T>`를 `타입 프로젝션`이라고 부른다. 이것은 명료함을 더한다.

무슨 이야기인고 하면 실제 타입 파라미터의 실제 타입이 뭔지 알 수 없지만 이 타입 인자는 타입 파라미터 T의 하위 타입이어야 한다는 것을 뜻하기 때문이다.

어째든 이것은  또 이렇게 생각해 볼 수 있다. 하위 타입을 바운드 제약으로 규정해버리면 된다. 

`generics: Generics<U>`로 선언을 하고 함수 앞에 제네릭 표기법으로 U는 T의 하위 타입이라는 것을 명시하면 어떻게 될까?

분명 제네릭 입장에서는 성립하기 때문에 해당 코드는 컴파일 오류 없이 작성할 수 있다.  

```kotlin
class Generics<T>{

    private val genericList = mutableListOf<T>()

    fun getGenerics(): List<T> = genericList

    fun addGeneric(element: T) {
        genericList.add(element)
    }

    fun <U: T> addSubGenerics(generics: Generics<U>) {
        generics.getGenerics().forEach { genericList.add(it) }
    }
}
```
이 두개는 역시 잘 작동한다.        

확실히 타입 파라미터를 추가하는 방식보다는 `타입 프로젝션`으로 정의하는 것이 간결하면서도 그 의미를 알면 좀 더 명확해진다는 것을 알 수 있다.      

그럼 `in`은 어떻게 활용해 볼 수 있을까?

반공변성은 일종의 역관계로 볼 수 있다.      

위에서 `Set<T>`와 관련된 내용을 다시 한번 살펴보자.   

즉, intSubGen와 intRootGen의 하위 타입 관계는 `Int`와 `Number`이다.        

```
2. 어떤 객체 X가 소지바 역할을 한다. 이 경우 X<T>의 타입 파라미터 T를 반공변적으로 선언할 수 있고, U가 T의 하위 타입이면 X<T>가 X<U>의 하위 타입이 된다.
```

```kotlin
class Generics<T>{

    private val genericList = mutableListOf<T>()

    fun getGenerics(): List<T> = genericList

    fun addGeneric(element: T) {
        genericList.add(element)
    }

    fun addSubGenerics(generics: Generics<out T>) {
        generics.getGenerics().forEach { genericList.add(it) }
    
    }
    /*
    fun <U: T> addSubGenerics(generics: Generics<U>) {
        generics.getGenerics().forEach { genericList.add(it) }
    }*/

    fun addToRoot(root: Generics<in T>) {
        root.addSubGenerics(this)
    }

}

fun main() {

    val intRootGen = Generics<Number>()
    intRootGen.addGeneric(1)
    intRootGen.addGeneric(2)

    val intSubGen = Generics<Int>()
    intSubGen.addGeneric(3)
    intSubGen.addGeneric(4)
    // Int는 Number의 하위 타입이라는 것을 공변적으로 만든 제네릭 함수를 활용 
    intRootGen.addSubGenerics(intSubGen)
    println(intRootGen.getGenerics())
    
    // Number가 마치 Int의 하위 타입인것처럼 작동하도록 반공변적으로 만든 제네릭 함수를 활용
    intSubGen.addToRoot(intRootGen)
    println(intRootGen.getGenerics())

}
// result
//[1, 2, 3, 4]
//[1, 2, 3, 4, 3, 4]
```
위 `addToRoot()`함수와 `addSubGenerics()`함수의 타입 파라미터들간의 관계를 잘 살펴보길 바란다.     

어찌되었든 이렇게 프로젝션을 사용하면 프로젝션이 적용된 타입 인자에 대해서는 `선언 지점 변성`이 의미가 없다고 한다.

## 스타 프로젝션

앞서 말했듯이 내용을 살펴보다가 `*`를 봤을 것이다. ~~varage변경자 말고!~~

자바의 와일드 카드 `?`에 대응하는 것으로 어떤 타입이나 될 수 있다는 것을 표현하는 방식이다.

일단 코틀린에서 타입 파라미터는 상위 바운드만 허용한다. 

```kotlin
fun main() {
    val anyList: List<*> = listOf(1, "a", 100L)
    val anyComparable: Comparable<*> = "AAAABBCCC"
    val any: Any = ""
    any is Generics<*>
    any is Generics<out Any?>
}
```
여기서 알 수 있는 것은 스타 프로젝션은 일종의 `out`프로젝션을 최상위 객체인 Any?로 타입 파라미터 바운드에 적용한 것처럼 동작한다는 것이다.

그래서 위 코드의 Generics클래스를 보면 마지막 부분처럼 사용하기도 한다.

하지만 재미있는건 이걸 또 다른 타입으로 바꿔서 작성하면 `타입 소거`로 인한 타입 체크가 불가능해지기 때문에 컴파일 오류가 난다.

또한 만일 타입 파마리터가 바운드 제약이 둘 이상 걸린 경우에는 `*`로 명시적인 out 프로젝션을 대신할 수 없다.

이유는 스타 프로젝션으로 어떤 값의 타입을 지정할 경우 컴파일러가 아예 타입 파라미터의 바운드를 무시하기 때문이다.

이 `스타 프로젝션`에는 또 한가지 특징이 있다. 그것은 `in/out`이 붙은 변성 지점에 사용하는 경우이다.

`out`으로 정의된 타입에 `스타 프로젝션`을 사용하게 되면 우리가 위에서 봤던 `out Any?`로 지정한 것과 같다.

그리고 `in`으로 정의된 타입에 사용하게 되면 우리가 위에서 봤던 `Nothing` 타입으로 지정한 것과 같게 된다.

일단 실제 코드로 한번 확인해 보자.

```kotlin
interface StarProjection<in T> {
    fun consume(_value: T)
}

class StringConsumer: StarProjection<String> {
    override fun consume(_value: String) {
        println(_value.uppercase())
    }
}

fun main() {
    val myProjection = StringConsumer()
    myProjection.consume("ok this is right!!!")
}
```
위 코드는 각각의 변성에 맞춰 오류없이 잘 작동하게 되어 있다.     

하지만 만일 `스타 프로젝션`을 사용하면 어떻게 될까?

```kotlin
fun main() {
    val myProjection = StringConsumer()
    myProjection.consume("ok this is right!!!")
    val starProjection: StarProjection<*> = myProjection
    // type mismatch
    //starProjection.consume("test")
}
```
`스타 프로젝션`으로 치환하는 코드까지는 아무 문제가 없다. 

하지만 `in`으로 선언해 소비하는 `consume()`함수에 `Nothing`타입이 아니니 `Nothing`으로 캐스팅하라고 IDE가 친절하게 알려준다.      

여기에도 공식이 존재한다.

```
`out`으로 공변적으로 지정된 곳에 스타 프로젝션을 사용하면 <out Any?>처럼 동작하고
`in`으로 반공변적으로 지정된 곳에 스타 프로젝션을 사용하면 Nothing 타입을 인자로 지정한 것과 같다.

```

그런데 여기서 문제가 발생할 수 있다. `스타 프로젝션`는 모든 타입을 표현하기 때문에 다음과 같이 `as`를 통한 캐스팅이 가능하게 된다.

```kotlin
fun main() {
    val myProjection = StringConsumer()
    myProjection.consume("ok this is right!!!")
    val starProjection: StarProjection<*> = myProjection
    //starProjection.consume("test")
    val castProjection = starProjection as StarProjection<Int>
    castProjection.consume(111111)
}
```
다만 캐스팅 하는 부분에 노란 표시가 되면서 `Unchecked cast: StarProjection<*> to StarProjection<Int>`라는 경고만 보여줄 뿐이다.

위 코드는 컴파일 오류도 나지 않는다. 하지만 런타임 시점에서야 `ClassCastException`이 발생하기 때문에 오류를 찾기도 힘들어 진다.

여러 책에서도 언급되는 내용이긴 하지만 제네릭 클래스의 타입 인자가 어떤 타입인지 정보가 없거나 타입인자가 어떤 타입인지 중요하지 않을때만 `스타 프로젝션`을 사용해야한다.

# 타입 별명

[코틀린 완벽 가이드]에서 제네릭스 마지막 챕터에 짧게 소개하는 내용이 타입 별명이다.

일반적으로 제네릭 타입이나 함수 타입의 긴 코드나 이름을 짧고 간결하게 도와주는 키워드로 `typealias`에 대해 소개한다.

일단 제약사항먼저 소개하자면 최상위에서만 선언이 가능하고 클래스나 함수 내에서는 사용이 불가능하다.      

또한 제네릭을 표현할 수 있지만 바운드 제약은 사용할 수 없다.    

다음 코드를 한번 살펴 보자.

```kotlin
inline fun checkedUpperCase(name: String, predicate: (String) -> Boolean): Boolean {
    return predicate(name)
}
fun main() {
    //val isUpper: (String) -> Boolean = { it.first().isUpperCase() } 
    println(checkedUpperCase("T"){ it.first().isUpperCase() })
}
```
코드는 단순하다.  

하지만 `checkedUpperCase`함수의 파라미터 정의가 간결하지 못한 느낌을 준다.     

또한 지금 주석된 부분을 살펴보자.       

후행 람다로 직접 작성하는 방법도 있지만 만일 저 형식을 재활용한다면 주석처럼 정의하고 사용하는 방법이 있는데 일단 코드가 간결하지 못한 느낌을 준다.    

이때 `typealias`를 통해 이것을 좀 더 간결하게 처리할 수 있다.

```kotlin
typealias StrPredicate = (String) -> Boolean

inline fun checkedUpperCase(name: String, predicate: StrPredicate): Boolean {
    return predicate(name)
}

fun main() {
    //val isUpper: StrPredicate = { it.first().isUpperCase() }
    println(checkedUpperCase("T"){ it.first().isUpperCase() })
}
```
좀 더 간결해진다.     

하지만 제네릭 선언을 사용해서 사용해 보자.

```kotlin
typealias Predicate<T> = (T) -> Boolean

inline fun checkedUpperCase(name: String, predicate: Predicate<String>): Boolean {
    return predicate(name)
}

inline fun checkedTenOver(value: Int, predicate: Predicate<Int>): Boolean {
    return predicate(value)
}

fun main() {
    //val isUpper: Predicate<String> = { it.first().isUpperCase() }
    println(checkedUpperCase("T"){ it.first().isUpperCase() })

    //val tenOver: Predicate<Int> = { it > 10 }
    println(checkedTenOver(11) { it > 10 })
}
```

# At a Glance

사실 제네릭스는 자연스럽게 사용하는게 쉽지 않다. 많은 생각과 고민을 해야 하는 부분이기 때문이다.       

API를 제공하는 입장에서는 사용자가 이런 부분을 알지 못해도 사용하는데 어려움이 없도록 하는게 중요하기 때문이다.       

그래서 아직도 연습을 많이 하고 있고 관련 내용을 꾸준히 읽으면서 지속적으로 이해하는 방법뿐이 없는거 같다.       