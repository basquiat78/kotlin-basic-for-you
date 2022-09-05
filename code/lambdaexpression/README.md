# Collection을 다루기 전에 알아야 할 몇 가지

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/lambdaexpression/kotlin/LambdaExpression.kt)

# 왜 컬렉션을 다루기 전에 알아야 하나요?

단순하게 컬렉션만을 위한 것은 아니다.

지금까지 여러분들은 let같은 스코프 함수등 코틀린을 공부해보면서 좀 독특한 것들을 경험했다.      

그중에 inline함수라는 녀석은 좀 특징이 있고 이것이 컬렉션에서 정말 많이 쓰이기 때문이다.       

여러분들이 자바 Stream API를 이용한다면 다음과 같은 코드를 자주 사용하게 된다.      

```java
public class Test {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream()
            .filter(i -> i < 5)
            .forEach(System.out::println);
    }
}
```

filter를 예로 들면 내부적으로 Predicate를 받는다.

filter내부에 직접적으로 람다를 사용해 Predicate를 구현할 수 있다.

만일 조건이 복잡하다면 Stream API에 작성되는 코드가 지저분해 질 소지가 있으니 이것을 functional interface로 익명 함수로 작성해서 넣을 수도 있다.

```java
public class Test {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Predicate<Integer> filter = (i) -> i < 5 || i > 8;
        list.stream()
            .filter(filter) // functional interface를 대입해도 되고
            // 다음과 같이 Predicate로 작성된 익명 함수를 실행하는 test를 
            // 메소드 레퍼런스로 작성을 할 수 있다.
            //.filter(filter::test)
            .forEach(System.out::println);
    }
}
```

[Kotlin in Action]에 고차 함수와 inline function에 대해서 상당한 내용을 담고 있다.

그리고 조슈아 블로크의 [이펙티브 자바]를 흉내낸 -실제로 책의 아이템 설명 단락은 아예 똑같이 만들었다. 저작권 문제 없나?- [이펙티브 코틀린에도] 언급되는 내용이다.

요지만 딱 잘라 말하면

```
인라인 클래스를 사용하면 성능적인 오버헤드 없이 타입을 래핑할 수 있다.

타입 시스템을 통해 실수로 코드를 잘못 작성하는 것을 막아준다.

이것은 코드의 안정성을 향상시켜준다.

의미가 명확하지 않은 타입, 특히 여러 측정 단위드을 함꼐 사용하는 경우에는 인라인 클래스를 꼭 활용해라
```

# 일급함수가 뭔지 들어봤어요?

주절이 주절이 했지만 함수형 프로그래밍을 지원하는 언어들이 공통으로 가지고 있는 것이 있는데 그것이 바로 first-class functions라는 것이다.

일급 함수라고 표현하는 것 같은데 자바8이전까지는 없었다가 자바8이후 이것을 지원했다고 한다.    

Stream API가 그 좋은 예이다.        

자바스크립트에서는 콜백 함수를 인자로 넘겨서 실행하는 방식이 가능한데 이유는 함수를 1급으로 다루고 있기 때문이라는 것이다.       

아시는 분들은 아시겠지만 이걸 모르고 자바8 이후의 람다와 함수형 프로그래밍을 했다면 알아두고 가자.            

~~나도 이런거 몰랐어~~

물론 자바에서의 일급 객체/일급 컬렉션이라는 개념은 알고 있었지만 나도 개념에 대해서는 잘 몰랐던 내용이다.     

모르고 사용했다는 예기라는 것이다. 뭐 될까 해서 작성해보니 "이게 되네?" 이러면서 ㅋㅋ          

사실 지금까지 여러분들이 코틀린을 작성하면서 다 경험하고 느꼈던 내용들이다.      

코틀린의 함수들은 바로 일급함수이다.        

그렇다면 일급함수의 정의가 뭔지 알아보자.

    - 일급함수는 함수의 객체로 취급 될 수 있다.

    - 일급 함수는 함수 객체를 인자로 넘길 수 있다.

    - 일급함수는 함수의 return 값이 될 수 있다.

    - 런타임에 생성될 수 있다.

# High-order function?

즉 위 자바 코드의 filter를 보면 람다나 function interface를 인자로 넘기는 것을 알 수 있다.

코틀린에서는 이렇게 람다나 함수 참조를 인자로 넘길 수 있거나 그 반대로 람다나 함수 참조로 반환하는 함수를 말한다.       

실제로 _Collection.kt에 정의된 함수들 중 이렇게 람다나 함수 참조로 반환하는 함수들은 대부분 inline으로 선언되어 있다.

일단 filter가 어떻게 생겨먹었는지 한번 확인해 보자.

```Kotlin
public inline fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T> {
    return filterTo(ArrayList<T>(), predicate)
}
```
predicate라는 변수명으로 람다를 받고 있다.       

찾아찾아 들어가보면 결국에는 조건에 맞으면 조건에 맞는 요소들을 List에 담는 로직인데 inline로 구성되어져 있다.      

코틀린의 filter는 람다를 인자로 넘길 수 있고 함수 참조를 넘기고 있는 것을 볼 수 있는데 위에 언급한 일급함수의 특징을 가지고 있다.

그렇다. filter는 고차함수인 것이다.      

# invoke() in Kotiln

인자가 predicate로 선언되었지만 뒤에 정의된 모습을 보면 람다라는 것을 알 수 있다.

실제로 이것을 디컴파일하게 되면 코틀린에서는 FuntionN으로 정의된 인터페이스를 보게 되고 최종적으로 invoke라는 인터페이스를 만나게 된다.

filter의 경우에는 저 predicate: (T) -> Boolean은 Function1으로 변경된 것을 알 수 있다.

```java
/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

// Auto-generated file. DO NOT EDIT!

package kotlin.jvm.functions

/** A function that takes 0 arguments. */
public interface Function0<out R> : Function<R> {
    /** Invokes the function. */
    public operator fun invoke(): R
}
/** A function that takes 1 argument. */
public interface Function1<in P1, out R> : Function<R> {
    /** Invokes the function with the specified argument. */
    public operator fun invoke(p1: P1): R
}
/** A function that takes 2 arguments. */
public interface Function2<in P1, in P2, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2): R
}
/** A function that takes 3 arguments. */
public interface Function3<in P1, in P2, in P3, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3): R
}
/** A function that takes 4 arguments. */
public interface Function4<in P1, in P2, in P3, in P4, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4): R
}
/** A function that takes 5 arguments. */
public interface Function5<in P1, in P2, in P3, in P4, in P5, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5): R
}
/** A function that takes 6 arguments. */
public interface Function6<in P1, in P2, in P3, in P4, in P5, in P6, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6): R
}
/** A function that takes 7 arguments. */
public interface Function7<in P1, in P2, in P3, in P4, in P5, in P6, in P7, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7): R
}
/** A function that takes 8 arguments. */
public interface Function8<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8): R
}
/** A function that takes 9 arguments. */
public interface Function9<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9): R
}
/** A function that takes 10 arguments. */
public interface Function10<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10): R
}
/** A function that takes 11 arguments. */
public interface Function11<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11): R
}
/** A function that takes 12 arguments. */
public interface Function12<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12): R
}
/** A function that takes 13 arguments. */
public interface Function13<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13): R
}
/** A function that takes 14 arguments. */
public interface Function14<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14): R
}
/** A function that takes 15 arguments. */
public interface Function15<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15): R
}
/** A function that takes 16 arguments. */
public interface Function16<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16): R
}
/** A function that takes 17 arguments. */
public interface Function17<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17): R
}
/** A function that takes 18 arguments. */
public interface Function18<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, in P18, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18): R
}
/** A function that takes 19 arguments. */
public interface Function19<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, in P18, in P19, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19): R
}
/** A function that takes 20 arguments. */
public interface Function20<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, in P18, in P19, in P20, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20): R
}
/** A function that takes 21 arguments. */
public interface Function21<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, in P18, in P19, in P20, in P21, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20, p21: P21): R
}
/** A function that takes 22 arguments. */
public interface Function22<in P1, in P2, in P3, in P4, in P5, in P6, in P7, in P8, in P9, in P10, in P11, in P12, in P13, in P14, in P15, in P16, in P17, in P18, in P19, in P20, in P21, in P22, out R> : Function<R> {
    /** Invokes the function with the specified arguments. */
    public operator fun invoke(p1: P1, p2: P2, p3: P3, p4: P4, p5: P5, p6: P6, p7: P7, p8: P8, p9: P9, p10: P10, p11: P11, p12: P12, p13: P13, p14: P14, p15: P15, p16: P16, p17: P17, p18: P18, p19: P19, p20: P20, p21: P21, p22: P22): R
}

```

~~깜짝 놀랐지?~~

근데 이 invoke는 operator로 선언되어 있는데 이 invoke는 특별한 연산자이다.

이름없이 호출될 수 있는 함수인 invoke를 연산자 오버로딩을 통해 각각 구현된 형태라는 것을 알 수 있다.

그래서 invoke를 통해 들어온 람다 형식의 익명 함수를 실행하고 있다는 것을 알 수 있다.

코틀린에서는 SAM(Single Abstract Method)이라 해서 하나의 추상 메소드를 가지는 인터페이스를 의미한다.

물론 인터페이스를 자바처럼 정의해서 사용할 수도 있다.

이건 나중에 한번 소개하기로 하자.

아무튼 람다라는 녀석은 이 invoke를 가지고 있다는 것을 알 수 있다.

# 근데 차이가 뭔데?

자 다음 코드를 보자

```Kotlin
fun main() {
    val noInline = noInline({ "test".uppercase() })
    val useInline = useInline({ "test".uppercase() })
    println("noInline is $noInline")
    println("useInline is $useInline")
}

// 인자없는 익명 함수 block을 받아서
fun <R> noInline(block: () -> R): R {
    return block()
}

// 인자없는 익명 함수 block을 받아서
inline fun <R> useInline(block: () -> R): R {
    return block()
}
```

저 두개의 코드는 같은 동작을 하지만 실제 디컴파일된 코드를 한번 살펴보자.

```java
public final class TestKt {
   public static final void main() {
      String noInline = (String)noInline((Function0)null.INSTANCE);
      int $i$f$useInline = false;
      int var3 = false;
      String var4 = "test";
      Intrinsics.checkNotNullExpressionValue(var4.toUpperCase(Locale.ROOT), "this as java.lang.String).toUpperCase(Locale.ROOT)");
   }

   // $FF: synthetic method
   public static void main(String[] var0) {
      main();
   }

   public static final Object noInline(@NotNull Function0 block) {
      Intrinsics.checkNotNullParameter(block, "block");
      return block.invoke();
   }

   public static final Object useInline(@NotNull Function0 block) {
      int $i$f$useInline = 0;
      Intrinsics.checkNotNullParameter(block, "block");
      return block.invoke();
   }
}
```
invoke도 보인다!

noInline의 경우에는 위에 소개한 겁나 긴 FunctionN중 인자가 없으니 Function0의 인터페이스를 구현하는 클래스 인스턴스를 만들고 있다.

'인라인 클래스를 사용하면 성능적인 오버헤드 없이 타입을 래핑할 수 있다.'

위에 요지중 맨 첫번째 내용이 떠오를 것이다.

근데 두 번째 inline을 사용한 녀석의 경우에는 메소드가 호출된 시점에서 코드를 풀어서 작성하고 있다.

인스턴스를 생성하지 않기 때문에 성능이 좋다는 이유가 이런 것이다.

그래서 _Collections.kt의 고차 함수들은 이런 이유로 성능의 향상을 위해 inline으로 선언된 것이라는 점을 알자.

# 그럼 코틀린에서 람다와 무명(익명?) 함수란?

공식 사이트에서는 람다 표현식과 무명 함수를 표현하는 방식은 자바와 비슷하다.

위에 예제로 썼던 코드를 한번 보자.

```Kotlin

fun main() {
    // param이 없다면
    val anyFuntion: () -> String = { "Hello Any Function" }
    println(anyFuntion())
    
    // one parame
    //val oneParamFuntion: (String) -> String = {message ->  "Hello Any Function $message" }
    val oneParamFuntion: (String) -> String = { "Hello Any Function $it" }
    println(oneParamFuntion("with One Param"))
    
    val overTwoParamFunction: (String, String) -> String = { p1, p2 -> "$p1 그리고 $p2"}
    println(overTwoParamFunction("Param1", "Param2"))
}

//result
//Hello Any Function
//Hello Any Function with One Param
//Param1 그리고 Param2

```
위와 같이 표현이 가능하다.

람다 표현식으로 어떤 인자가 들어와서 어떤 타입이 반환되는지를 정의하고 {..}에 코드를 작성한다.

파라미터가 1개라면 {..}바디내에서 넘어온 파라미터는 it으로 접근가능하다.

it보다는 어떤 정보가 넘어오는지 이름을 주고 싶다면 주석처리된 부분처럼 이름을 주고 람다 표현식으로 처리하면 된다.

2개 이상의 파라미터가 넘어온다면 {..}내에 람다 표현식에 나열한 순서대로 넘어온다.

물론 다음과 같이 람다 표현식을 정의하지 않고 {..} 바디내에서 정의해도 된다.

```Kotlin
fun main() {
    val anyFuntionDirect = { "Hello Any Function Direct" }
    println(anyFuntionDirect())
    
    val oneParamFuntionDirect = {it: String ->  "Hello Any Function $it" }
    //val oneParamFuntion: (String) -> String = {message ->  "Hello Any Function Direct $message" }
    println(oneParamFuntionDirect("with One Param"))
    
    val overTwoParamFunctionDirect = { p1: String, p2: String -> "Direct $p1 그리고 $p2"}
    println(overTwoParamFunctionDirect("Param1", "Param2"))
}

```

# Passing trailing lambdas

후행 람다 전달 방식이라는 게 있다.

이게 참 코틀린의 컬렉션의 고차함수를 사용하면서 처음에는 이해를 못했는데 코틀린 사이트에서는 이런 언급이 있다.

```
According to Kotlin convention, if the last parameter of a function is a function, 
then a lambda expression passed as the corresponding argument can be placed outside the parentheses
```
어떤 의미냐면 여러개의 파라미터를 받는 고차함수의 마지막 파라미터가 람다 표현식이라면 {}를 통해 따로 표현할 수 있다는 것이다.

예제로 한번 보자

```Kotlin
fun main() {

    val first = 100
    val second = 200
    val result = forTrailing(10, { first + second })
    println("result is $result")
     
}

inline fun forTrailing(value: Int, block: () -> Int): Int {
   return value + block()
}
```

'{ first + second }' 이 코드는 사실 'val noname: () -> Int = { first + second }'와 같은 형식이다.

forTrainlin 함수는 Int값과 무명 함수를 인자로 받고 두개의 결과를 더해서 반환한다.

지금 코드로 보면 결과는 310이 나온다.

근데 해당 함수의 마지막 파라미터가 람다 표현식이기 때문에 이 후행 람다 전달 방식을 이용하면 코드가 다음과 같이 변경할 수 있다.

```Kotlin
fun main() {

    val first = 100
    val second = 200
    val resultForTrailing = forTrailing(10) { first + second }
    println("resultForTrailing is $resultForTrailing")
     
}

inline fun forTrailing(value: Int, block: () -> Int): Int {
   return value + block()
}
```
어랏? forTrailinig(10) 이후에 {..}바디로 표현하는 독특한 문법을 볼 수 있다.

왜냐하면 forTrailing의 마지막 인자는 람다 표현식이기 때문에 이것이 가능하다.

실제로 위에 우리가 살펴본 filter를 보면 하나의 람다 표현식을 받고 있다.

결국 이건 첫 번째이자 마지막 파라미터가 람다 표현식이라는 말인데 코드상으로 보면 다음과 같은 형식이다.

```Kotlin
fun main() {
    val filteredNums = (1..10).filter({it in 4..8})
    println("filteredNums is $filteredNums")
}
```
하지만 IDE에서 'Lambda argument should be moved out of parentheses'라고 해서 경고 하나 띄워준다.

맨 위에 후행 람다 전달 방식에 대한 코틀린 사이트의 내용을 이행하라고 하는 것이다.

```Kotlin
fun main() {
    val filteredList = (1..10).filter {it in 4..8}.toList()
    println("filteredNums is $filteredList")
}
```
위와 같은 표현이 가능한 것이다.

물론 자바처럼

```Kotlin
fun main() {
    val filter: (Int) -> Boolean = { it in 4..8}
    val filteredWithList = (1..10).filter(filter).toList()
    println("filteredWithList is $filteredWithList")
}
```
위 코드처럼 익명 함수로 만들고 filter에 넘길 수도 있다.     

내부적으로 반환해 주는 it이라는 단어가 불분명하다면 '{num -> num in 4..8}'도 가능하다.      

사실 이런 방식이 별거 아닌것 같지만 자바에서 '.filter(i -> i < 5)' 요렇게 작성하는 것과 코틀린의 '.filter{it in 4..8}'로 작성하는 것은 상당히 큰 차이가 난다.

코딩하는데 손이 덜 가고 간결함을 유지할 수 있는 장점이 있다.

# At a Glance

컬렉션을 사용할 때 자주 볼 수 있는 후행 람다 전달 방식과 그와 관련 몇 가지 사전 지식들을 알아 봤다.

차후에 따로 이 부분은 좀 더 자세하게 소개해 볼까 한다.        

