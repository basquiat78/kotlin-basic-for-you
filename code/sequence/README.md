# 시퀀스

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/sequence/kotlin/Sequence.kt)

## 이터러블?

이터러블의 정의를 살펴보면 Iterable<T> 타입으로 표현된다고 설명하고 있다.      

일반적으로 즉시 계산되는 상태가 있는 컬렉션을 표현하는 말이 바로 이터러블이다.      

상태, 즉 Stateful하다는 말은 컬렉션이 원소를 필요하 때 생성하는 제너레이터 함수를 유지하지않고 원소를 저장한다는 의미를 지닌다.      

사실 말이 좀 모호한데 JPA를 예로 들면 EAGER와 LAZY를 알 것이다.    

즉시 계산이라는 말이 일종의 비동기처럼 필요한 시점에 초기화되는 것이 아니라 컬렉션을 최초로 생성할 때 초기화 되는 것을 의미한다.      

즉, JPA에서 EAGER는 쿼리 시점에서 이미 초기화되서 정보를 가져온다는 의미이다.      

아마도 JPA를 해보셨다면 이 의미가 어떤 의미인지 알 수 있다.

시퀀스라는 녀석도 이터러블처럼 iterator()를 제공한다. 동작하는 방식이 살짝 다르다.

자바에서 보면 일종의 stream()과 좀 비슷한 느낌을 많이 주는데 일단 시퀀스를 만들어 보는 것으로부터 시작해 보자.

## 별다를 거 없다.

지금까지 배운 컬렉션 함수와 크게 다르지 않다. 그냥 sequenceOf()함수를 사용하는 것이 가장 심플한 방법이다.     

또한 자바의 스트림처럼 어떤 컬렉션을 stream api를 이용하는 것처럼 기존 컬렉션 객체에서 asSequence()함수를 사용해서 시퀀스를 생성할 수 있다.

보통 컬렉션 함수의 경우에는 그 결과들이 중간 연산을 하든 뭐든 바로 확인할 수 있다. 하지만 시퀀스의 경우에는 최종 연산을 하기 전까지는 결과를 알 수 없다.     

다음 코드를 한번 살펴보자.

```kotlin
fun main() {
    val list = listOf(1, 2, 3)
    val filteredList  = list.filter {
        println("list in it -> $it")
        it > 2
    }
    println("filteredList : $filteredList")

    val sequence = sequenceOf(1, 2, 3)
    val filteredSequence  = sequence.filter {
        println("sequence in it -> $it")
        it > 2
    }
    println("filteredSequence : $filteredSequence")
}
```
위 코드를 실행해 보자. 그러면 좀 독특한 결과를 볼 수 있다.

리스트의 경우에는 아래 filteredList를 찍기 전에 filter가 돌면서 컬렉션의 원소를 찍고 결과가 나온다.     

하지만 아래 시퀀스의 경우에는 filteredSequence의 객체 주소는 찍히지만 filter내부에 원소 정보는 찍히지 않는다.

하지만 만일 

```kotlin
fun main() {
    val list = listOf(1, 2, 3)
    val filteredList  = list.filter {
        println("list in it -> $it")
        it > 2
    }
    println("filteredList : $filteredList")

    val sequence = sequenceOf(1, 2, 3)
    val filteredSequence  = sequence.filter {
        println("sequence in it -> $it")
        it > 2
    }
    println("filteredSequence : $filteredSequence")
    println("filteredSequence : ${filteredSequence.toList()}")
}
// result
// list in it -> 1
// list in it -> 2
// list in it -> 3
// filteredList : [3]
// filteredSequence : kotlin.sequences.FilteringSequence@5b37e0d2
// sequence in it -> 1
// sequence in it -> 2
// sequence in it -> 3
// filteredSequence : [3]
```
최종 연산인 toList()가 호출되어서야 계산이 진행되고 콘솔에 로그를 찍는 것을 확인 할 수 있다.       

즉 최종 연산을 하기 전까지 지연 계산을 한다는 것을 알 수 있다.    

## generateSequence()
시퀀스를 만드는 또 다른 방법도 존재한다.     

다만 이 함수는 인자로 받은 정보를 기준으로 마지 reduce처럼 람다식으로 연산된 값을 넘겨주는 방식으로 작동한다.     

따라서 무한 시퀀스 또는 특정 조건을 활용한 유한 시퀀스를 생성할 수 있다.

아래 코드를 살펴보자.

```kotlin
fun main() {
    // 무한 시퀀스 1부터 - 무한대로 생성한다.
    val infiniteSequence = generateSequence(1) { it + 1 }
    // you see Evaluation stopped while log size exceeded max size (stackoverflow)
    //infiniteSequence.iterator().forEach {
	//	println("it : $it")        
    //}
    // 무한시퀀스라고 해도 take를 통해 10개정보만 가져와보자
    val take10 = infiniteSequence.take(10)
    take10.iterator().forEach {
		println("take10 it : $it")        
    }
    
    // 유한 시퀀스이다. 어떤 특정한 조건이 될때까지 시퀀스를 생성하고 null을 통해서 작동을 멈추게 하는 방식으로 생성한다.
    val normalSequence = generateSequence(1) { if(it < 1000) it*2 else null }
    normalSequence.iterator().forEach {
		println("normalSequence it : $it")        
    }
}
// result
// take10 it : 1
// take10 it : 2
// take10 it : 3
// take10 it : 4
// take10 it : 5
// take10 it : 6
// take10 it : 7
// take10 it : 8
// take10 it : 9
// take10 it : 10
// normalSequence it : 1
// normalSequence it : 2
// normalSequence it : 4
// normalSequence it : 8
// normalSequence it : 16
// normalSequence it : 32
// normalSequence it : 64
// normalSequence it : 128
// normalSequence it : 256
// normalSequence it : 512
// normalSequence it : 1024
```

## 빌더를 사용한 시퀀스 생성

키워드는 yield()와 yieldAll()이다.      

yield()의 경우에는 하나의 원소를 시퀀스에 추가할 때 사용하고 yieldAll()은 이터레이터, 이터러블, 또는 시퀀스에 모든 원소를 추가하는 방식이다.     

코틀린의 공식 문서를 보면 유예 가능 계산 (suspendable computation)이라고 표현하는 코틀린의 기능이라고 하는데 멀티 스레드 환경에서 아주 유용하다고 한다.      

사실 시퀀스를 적극적으로 사용하지 않아서 체감하기 힘들지만 동시성 부분에서 다시 한번 다뤄볼 생각이다.

어째든 다음 코드를 통해 확인해 보자.

```kotlin
fun main() {
    val list = listOf(2, 4, 5, 6, 7)
    val genSequence = generateSequence(10) { if(it < 100) it + 1 else null }

    val useBuilder = sequence {
        yield(1)
        yieldAll(list.iterator())
        yieldAll(genSequence)
    }

    useBuilder.iterator().forEach {
        println("it : $it")
    }
    
}
```

## 물론 filter, map도 사용가능하다.

위에서 filter를 사용한 예제가 있긴 하다.

다음 코드를 살펴보자.

```kotlin
fun main() {
    val exampleSeq = sequenceOf(1, 2, 3, 4, 5, 6, 7)

    val result = exampleSeq.filter { it > 4}
                           .map { it * 10 }

    result.iterator().forEach {
        println("result it : $it")
    }
    
}
// result
// result it : 50
// result it : 60
// result it : 70
```

## 기본적인 first/last()함수 사용가능

위 코드를 예로 사용해 보자.

```kotlin
fun main() {
    
    val exampleSeq = sequenceOf(1, 2, 3, 4, 5, 6, 7)
    
    val result = exampleSeq.filter { it > 4}
    				       .map { it * 10 }
                          
  	println(result.first())
    println(result.last())

}
```
## sequence에서 특정 인덱스 요소를 가져올 수 없는거니???

물론 elementAt()이라는 함수가 있다. 실제로 이것은 리스트 컬렉션의 get()같은 함수를 일반화한 함수로 알려져 있다.     

그래서 배열, 이터러블, 시퀀스같은 컬렉션에서 사용할 수 있다.      

단 공식 사이트에서 설명하기로는 임의 접금 컬렉션(random access list)이 아닌 컬렉션에서 사용하면 인덱스 값에 비례해서 느려진다고 한다.      

아마도 LinkedList같은 데서 사용하게 되면 느려질 것이라는 의미일 것이다.      

왜냐하면 ArrayList처럼 Ramdon access가 불가능한 리스트가 LinkedList이기 대문이다.      

따라서 어떤 특정 요소를 접근할려면 첫번째 노드부터 접근해야하는 단점이 존재하기 때문일 것이다.       

어쨰든 코드는 다음과 같다.

```kotlin
fun main() {
    val exampleSeq = sequenceOf(1, 2, 3, 4, 5, 6, 7)

    val result = exampleSeq.filter { it > 4 }
                           .map { it * 10 }

    println(result.elementAt(1))
}
```

물론 코틀린은 편의를 위해 별별 함수를 만들어줬기 때문에 OutOfArrayIndex같은 익셉션을 방어하기 위한 함수도 사용가능하다.

```kotlin
fun main() {
    
    val exampleSeq = sequenceOf(1, 2, 3, 4, 5, 6, 7)
    
    val result = exampleSeq.filter { it > 4}
    				       .map { it * 10 }

    // 인덱스에서 벗어났기 때문에 null
    println(result.elementAtOrNull(1111))
    // null이 아닌 후행 람다에 정의된 값 -1이 결과값으로 나온다.
    println(result.elementAtOrElse(10000000) { -1 })
}
```

## 여러분들이 생각할 수 있는 가능성

❓시퀀스의 카운터가 알고 싶어요?      
👉 count()를 쓰세요.     


❓Number형식의 시퀀스의 합을 구하고 싶다고요?     
👉 sum()이 있습니다.     


❓특정 조건의 시퀀스에서 합을 구하고 싶다고요?     
👉 sumOf { /** do somethiing */ } 이런 것도 있어요!      

❓평균값도요?     
👉 average() 당연히 쓸 수 있습니다.

❓특정 객체의 멤버 변수의 최대값/최소값도 가능하냐구요?     
👉 minByOrNull/maxByOrNull() 아 물론! minWith/maxWith() 계열도 가능합니다.

❓스트링 조인 함수도 가능하겠네요?       
👉 당연히 joinToString()같은 함수도 쌉가능!

❓fold/reduce도 쌉가능??     
👉 쌉가능!

❓partition()도 가능하죠?      
👉가능합니다. 다만 주의점은 결과가 filter/map과는 다르게 리스트의 쌍을 반환하는 점에 유의해야함.

## 주의할 사항

시퀀스도 sort()같은 함수를 사용할 수 있지만 이렇게 되면 반환되는 시퀀스가 상태를 갖는다고 한다.

리스트에 특화된 asReversed()나 shuffled()는 리스트에 특화된 함수로 배열/시퀀스에는 사용할 수 없다.

[이펙티브 코틀린]의 내용중 [8장. 효율적인 컬렉션 처리 (아이템 49)]의 내용을 토대로 본다면 이런 내용이 있다.

```
시퀀스가 빠르지 않은 경우

컬렉션 전체를 기반으로 처리해야하는 연산의 경우로 대표적으로 sorted가 있다.
문제는 이러한 변환 처리로 인해서, 시퀀스가 컬렉션처리보다 느려진다는 것이다. 
참고로 무한 시퀀스처럼 시퀀스의 다음 요소를 lazy하게 구하는 시퀀스에 sorted를 적용하면 무한 반복에 빠지는 문제 발생.

sorted는 시퀀스보다 컬렉션이 더 빠른 희귀한 예.

```

## 다른 컬렉션과의 차이점

중간 연산과정에서 시퀀스는 지연 계산을 한다는 것을 알 수 있다.     

실제로 이러한 이유에는 다른 컬렉션들은 새로운 컬렉션 함수를 반환하는 방식이기 때문이다.      

지금 소개하는 코드의 일부는 실제로 [이펙티브 코틀린]의 내용중 [8장. 효율적인 컬렉션 처리]에 있는 내용이다.      

```
아이템 49. 하나 이상의 처리 단계를 가진 경우에는 시퀀스를 하용하라
```
물론 여기에는 자바 8의 스트림과 비교하는 내용도 있다.      

요약하면....    

~~코틀린이 사용하기 더 쉬워!!~~         

어째든 [이펙티브 코틀린]책 내용이 조슈아 블로크의 [이펙티브 자바]를 모티브로 한 책이라 곁치는 부분도 있다.     

하지만 코틀린 관련 내용은 결이 좀 달라서 한번 읽어보는 것을 추천한다.

# At a Glance       

사실 시퀀스를 적극적으로 사용하지 않았다는 점에서 내 자신부터 먼저 반성을 해보자.       

적재적소에 효율적인 컬렉션 처리를 위해서는 상황에 따라 선택을 할 수 있는 통찰력이 필요한 부분이 아닌가 싶다.