# 예외처리를 어떻게 하나?

[테스트 코드](https://github.com/basquiat78/kotlin-basic-for-you/blob/main/code/functionyouwantmake/kotlin/HowToExceptions.kt)

# 자바의 에러

자바의 에러와 관련해서 예외와 관련된 선조격인 클래스가 하나 있다.

그것은 바로 Throwable이다.

[oracle java doc: Throwable](https://docs.oracle.com/javase/7/docs/api/java/lang/Throwable.html)

구조를 보면 최상위는 역시 Object이다.

그리고 문서의 Direct Known Subclasses보면 Error와 Exception을 볼 수 있다.

'아하! 이넘이 바로 에러개의 Obeject, 예외의 조상이구나!!'

쭉 각각 따라가다보면 하위 IOError같은 Error들도 있고 Exception으로 가지를 타고 가면 우리가 흔히 알고 있는 익셉션들이 잔뜩 나열되어 있다.

사실 Error는 우리가 어떻게 할 수 있는 부분이 아니다. 메모리 부족이나 StackOverFlow같은 에러들은 JVM, 즉 가상머신에서 문제가 생겼을 때 발생하는 예외이다.

어플리케이션의 코드레벨에서 발생하는 에외가 아니기 때문에 예외 발생시 복구 할 수 없는 경우이다.

결국 우리가 봐야하는 부분은 결국 Exception쪽이다.

밸둥 사이트에서 이와 관련 글들이 있는데 한번 읽어보기 바란다.

[Common Java Exceptions](https://www.baeldung.com/java-common-exceptions)

[Checked and Unchecked Exceptions in Java](https://www.baeldung.com/java-checked-unchecked-exceptions)

위의 오라클의 자바 독의 Exception에서 RuntimeException이 존재한다.

자바에서는 확인된 예외 (checked exception)과 확인되지 않는 예외 (unchecked exception)로 분류하는데 RuntimeException은 상당히 중요한 위치를 가지고 있다.

[이펙티브 자바]에서는 이것을 검사 에외(checked exception) 비검사 예외(unchecked exception)로 표현하고 있다.

바로 이 RuntimeException의 하위 클래스들은 컴파일 시점에서 unchecked 즉, 예외를 체크하지 않는다.

이게 무슨 말인지 잘 모르는 주니어분들을 봐서 한번 설명을 해보고자 한다.

객체를 json형식의 텍스트로 변환하는 코드를 예로 들어보자.

```java
/**
 * commmon utils
 * created by basquiat
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
```
밑에 코드처럼 작성하게 되면 어떤 일이 벌어질까?

여러분의 IDE에는 'writeValueAsString'부분에 빨간 줄로 밑줄 쫙! 그어 줄 것이다.

IDE기능을 이용해 수정을 하게 되면 어떤 일이 벌어질까?

```java
/**
 * commmon utils
 * created by basquiat
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {
    /**
     * 객체를 json string으로 변환한다.
     * @param object
     * @return String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper().writeValueAsString(object);
    }
}
```
요렇게 변경될 것이다.

실제 writeValueAsString메소드를 따라가면 내부적으로 JsonProcessingException와 이것을 상속한 JsonMappingException를 던지고 있다.

JsonProcessingException을 따라가면 잭슨아저씨 익셉션을 볼 수 있고 그 아저씨는 IOException을 상속하고 있는데 이것은 전형적인 checked exception이다.

호출한 메소드가 저렇게 익셉션을 던지면 해당 메소드를 사용하는 메소드들 역시 저 에러를 던져야 한다.

즉 위로 예외가 전파가 된다.       

호출된 메소드로가 위로 익셉션을 던지면 받아서 try-catch로 처리할 지 아니면 또 위로 던져주거나 이렇게 될것이다.       

사실 이것은 코드를 좀 지저분하게 만드는 경향이 있다.     

왜냐하면 자신을 사용하는 곳이 있다면 그 곳에서도 둘 중 하나를 강제하기 때문이다.       

그래서 메소드 내에서 예외처리를 try catch문으로 감싸서 catch쪽에 보상 로직을 처리할 수 있다.

이런 이유에는 컴파일 시점에서 해당 메소드를 사용하는데 있어서 확인된 예외를 알 수 있기 때문에 가능하다.

하지만 이런 걸 생각해 보자.

```java
public class Main {

    public static void main(String[] args) {
        String numberStr = "1000";
        println(Inteter.parseInt(numberStr));

        String str = "test";
        println(str.toUpperCase());
    }

    public static int divide(int a, int b) {
        return a/b;
    }

}
```
위 코드와 메소드를 보면 눈썰미 있는 여러분들은 바로 알 수 있을 것이다.

'아 numberStr이 숫자 형식이 아니면 NumberFormatException, str이 null이면 NPE, 밑에 메소드는 b가 0이 들어오면 ArithmeticException가 발생하겠구나'

하지만 그 어디에도 익셉션을 던지는 곳이 없다.

물론 필요한 경우 저 상황에서 발생할 수 있는 위에 언급한 익셉션들을 능동적으로 던질 수 있다.      

만일 마땅한게 없다면 RuntimeException을 확장한 custom unchecked exception을 만들어 익셉션을 던질 수도 있다.     

당연히 try catch로 처리가 가능하다.     

즉 unchecked exception이기 때문이다.      

이제 이 두개의 차이를 알 수 있을거라고 생각한다.        

자 그럼 코틀린은 좀 다른가? 라고 생각할 수 있는데 아니다.

똑같다. 다만 몇가지 특징을 알아두면 좋다.

# Handle Exception in Kotlin

신기하게도 코틀린내에서는 checked exception을 강제하지 않는다.

~~왜????~~

그래서 코틀린의 코드를 살펴 보면 메소드에 throws XXException을 던지는 코드를 볼 수 없다.

```Kotlin
fun exception() throw IOException() {
    //do something
}
```
위와 같은 저런 코드가 없다.

실제로 코틀린 공식 사이트에 가면 떡하니 이런 문구가 있다.

```
Kotlin does not have checked exceptions. 
There are many reasons for this, but we will provide a simple example that illustrates why it is the case.
```

나에겐 일종의 자바 바이블로 가지고 있는 [Thinking in Java]의 저자인 Bruce Eckel의 말을 인용하고 있다.

```
Examination of small programs leads to the conclusion that requiring exception specifications could both enhance developer productivity and enhance code quality, 
but experience with large software projects suggests a different result – decreased productivity and little or no increase in code quality.
```

대충 해석해 보면

```
예외처리를 하게 하는 것이 개발자의 생산성을 향상시키고 코드 품질을 높여줄 것이라는 결론에 이른다.   
그러나 엔터프라이즈급의 프로젝트의 경험에서는 오히려 생상성이 떨어지고 코드 품질 역시 증가하지 않는다.
```
이것이 주 요지이다.

조슈아 블로그의 [이펙티브 자바] 역시 언급하기도 하는데 만일 여러분이 이 책을 가지고 있다면 '10장 예외'부터 보시면 된다.

특히 이 부분을 주목해 보자. 책에서도 볼드체로 굵게 써놨다.

```
예외는 (그 이름이 말해주듯) 오직 예외 상황에서만 써야 한다. 

절대로 일상적인 제어 흐름용으로 쓰여선 안 된다.

.
.
.

이 원칙은 API 설계에도 적용된다. 

잘 설계된 API라면 클라이언트가 정상적인 제어 흐름에서 예외를 사용할 일이 없게 해야 한다. 

핵심 정리
예외는 예외 상황에서 쓸 의도로 설계되었다. 
정상적인 제어 흐름에서 사용해서는 안 되며, 이를 프로그래머에게 강요하는 API를 만들어서도 안 된다.
```

위에 json converter메소드를 통해서 checked exception의 경우에는 위 코드에서 알 수 있듯이 API가 throw를 던지도록 강요하게 된다는 것을 알 수 있다.

그 이후 RuntimeException을 활용하고 주옥같은 이야기들을 많이 한다.

어째든 한 장의 챕터를 할애할 만큼 많은 내용을 담고 있기 때문에 전부 소개하기는 힘들다.

자 어째든 다음과 같이 우리가 자바에서 테스트할 때 자주 사용하는 코드를 한번 보자.


```java
public class Main {

    public static void main(String[] args) throws InterruptedException {

        String message = "Hello! Friend";
        consoleMessage(message);
    }

    public static void consoleMessage(String message) throws InterruptedException {
        System.out.println("before console");
        Thread.sleep(10000);
        System.out.println(message);

    }
}

```
어떤 메세지를 받아서 10초 후에 콘솔을 찍는 초간단 코드가 있다.

Thread클래스의 sleep메소드는 InterruptedException를 던지고 있다.

InterruptedException은 checked exception이다.

그래서 저렇게 throws InterruptedException를 하지 않으면 컴파일 오류가 난다.

근데 저 메소드를 호출하는 main메소드에도 저것을 던지라고 오류가 난다.

예외가 전파가 된다. 그래서 보통 테스트 코드를 만들 때는

```java
public class Main {

    public static void main(String[] args) {

        String message = "Hello! Friend";
        consoleMessage(message);
    }

    public static void consoleMessage(String message) {
        System.out.println("before console");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(message);
    }
}
```
저렇게 try catch문을 사용하면 InterruptedException를 던지지 않아도 되고 main에서는 사용하지 않는 예외이기 때문에 회색으로 변경된다.

그래서 지우라고 IDE가 알려준다.

근데 코틀린은 어떨까?

```Kotlin
fun main() {
    val message = "Hi! There~"
    console(message)
}

fun console(message: String) {
    println("before console")
    Thread.sleep(1000)
    println("message is $message")
}
```
checked exception을 강제하지 않기 때문에 어떤 오류도 나지 않는다.

물론! 자바처럼 try catch를 통해서 예외에 대한 방어 코드를 만들 수 있다.

```Kotlin
fun main() {
    val message = "Hi! There~"
    console(message)
}

fun console(message: String) {
    println("before console")
    try {
        Thread.sleep(1000)
    } catch(e: InterruptedException) {
        println("에러야!")
    } finally {
        println("무조건 실행 finally")
    }
    println("message is $message")
}
```
finally는 필요하면 작성할 수 있고 안할 수도 있다.

자 여기까지는 코틀린과 자바의 차이점이 어느 정도 보인다.

또한 독특하게도 try는 표현식이다.

이전부터 if, when같은 표현식을 보면 리턴값을 갖을 수 있다는 것을 알 수 있는데 try도 그게 가능하다.

```Kotlin
fun main() {
    val str: String = "10008"
    val convertInt: Int = strToInt(str)
    println("message is $convertInt")

}

fun strToInt(strValue: String): Int {
    return try {
        strValue.toInt()
    } catch(e: Exception) {
        0
    }
}

/*
이렇게도 가능하다.
fun strToInt(strValue: String) = try {
    strValue.toInt()
} catch(e: Exception) {
    0
}
 */
```

# 코틀린에서도 try-finally보다는 try-with-resources?

조슈아 블로크의 [이펙티브 자바]의 내용중 '아이템 9. try-finally보다는 try-with-resources를 사용하라'가 있다.

파일을 읽고 쓰는 InputStream, OutputStream이나 Connection같은 녀석들은 사용한 자원을 회수해야 하는 경우가 많다.

대부분 이런 것들은 제대로 처리를 해주지 않으면 메모리 누수 - Memory leak -을 발생하기 때문에 어떤 자원을 이용하고 나면 finally에서 닫아주는 코드를 작성하곤 한다.

예전에 PreparedStatement를 이용한 sql 코드를 사용할 때 이런 경우를 하도 많이 당했던 기억이 난다.

어째든 
```java
public class Test {

    // 코드 9-4 복수의 자원을 처리하는 try-with-resources - 짧고 매혹적이다!
    static void copy(String src, String dst) throws IOException {
        try(
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dst)
        ) {

            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }

    // 코드 9-5 try-with-resources를 catch 절과 함께 쓰는 모습
    static String firstLineOfFile(String path, String defaultVal) {
        try( BufferedReader br = new BufferedReader(new FileReader(path)) ) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }

}

```
java7에서 등장한 녀석으로 어쨰든 위와 같이 try-with-resource를 활용해 이득을 보는 방법을 소개하고 있다.

AutoCloseable 인터페이스를 구현한 클래스와 인터페이스로 확장되었다면 자동으로 자원을 닫아주기 때문에 코드가 간결해 지는 것이다.

```
핵심 정리
꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자.
예외는 없다. 
코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. 
try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-withresources로는 정확하고 쉽게 자원을 회수할 수 있다.
```

하자만 코틀린에서는 저런 구문식을 제공하지 않는다.

~~뭐라고? 말도 안돼!!!!~~

대신 use라는 함수를 제공한다.       

~~병주고 약주기~~

그렇다면 저 위의 코드 9-5의 코드를 use를 사용해서 한번 써보고자 한다.

```Kotlin

fun firstLineOfFile(path: String?, defaultVal: String?): String? {
    /*
    try {
        return BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        return defaultVal
    }
    */
    return try {
        BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        defaultVal
    }
    
}

fun firstLineOfFile(path: String?, defaultVal: String?): String? = try {
BufferedReader(FileReader(path)).use { br -> return br.readLine() }
} catch (e: IOException) {
defaultVal
}

```
다만 try-with-resource와는 다르게 catch문이나 finally중 하나는 꼭 있어야 한다.     

이런 차이점이 있다는 것 정도 알고 가자.       

하지만 예외를 던지는데 있어서 능동적으로 던질 필요가 있기도 하다.

# 코틀린에는 Nothing이 있다.

코틀린 공식 사이트의 Nothing에 대한 설명은 다음과 같다.


```
Nothing has no instances. 
You can use Nothing to represent "a value that never exists": 
for example, if a function has the return type of Nothing, it means that it never returns (always throws an exception).
```

실제로 Nothing class는 위 설명을 그대로 써놨다.

```Kotlin

/**
 * Nothing has no instances. You can use Nothing to represent "a value that never exists": for example,
 * if a function has the return type of Nothing, it means that it never returns (always throws an exception).
 */
public class Nothing private constructor()
```
private constructor를 가지고 있는 클래스기 때문에 인스턴스를 생성할 수 없다.

특히 이 녀석은 모든 클래스의 하위 클래스라는 특징을 가지고 있다.

또한 코틀린에서는 throw를 하나의 표현식으로 보고 있는데 이 녀석의 타입이 Nothing이다.

즉 이 녀석이 반환 타입으로 끝나면 그 지점으로부터 더 이상 진행되지 않는다. 마치 break걸린 듯한 느낌이다.

그래서 throw를 통한 예외처리시에는 엘비스 연산자와 같이 가장 많이 사용된다.

어색하지만 다음 코드를 보자.

```Kotlin
fun main() {

    val strValue: String? = null

    var checkValue = strValue ?: throw IllegalArgumentException("strValue가 null로 넘어왔습니다.")
    println(checkValue)

}
```

저렇게 어떤 조건에 따라서 예외를 능동적으로 던질 수 있지만 일단 코드 자체가 간결하지 못한 느낌을 준다.

위에서 말했듯이 throw는 표현식이 Nothing 타입이라는 것을 알 수 있으니 메소드로 빼서 사용할 수 있을 것이다.


```Kotlin
fun main() {

    val strValue: String? = null

    val checkValue = strValue ?: fail()
    println(checkValue)
    println(test("test"))
}
fun main() {
    val message = "Hi! There~"
    console(message)

    val str: String = "10008"
    val convertInt: Int = strToInt(str)
    println("message is $convertInt")

    val strValue: String? = "null"

    val checkValue = strValue ?: fail()
    println(checkValue)
    // 밑에 코드는 checkValue에서 fail()이 호출된다면 Nothing이 반환되기 때문에 도달하지 못한다.
    println(test("test"))
}

/**
 * 코틀린은 checked exception을 강제하지 않기 때문에
 * throws InterruptedException를 명시하지 않아도 된다.
 */
fun console(message: String) {
    println("before console")

    Thread.sleep(1000)

    println("message is $message")
}

/**
 * 일반적인 try-catch-finally 형식
 */
fun consoleWithTry(message: String) {
    println("before console")
    try {
        Thread.sleep(1000)
    } catch(e: InterruptedException) {
        println("에러야!")
    } finally {
        println("무조건 실행 finally")
    }
    println("message is $message")
}

fun strToInt(strValue: String) = try {
    strValue.toInt()
} catch(e: Exception) {
    0
}

/*
fun strToInt(strValue: String): Int {
    return try {
        strValue.toInt()
    } catch(e: Exception) {
        0
    }

    /*
    try {
        return strValue.toInt()
    } catch(e: Exception) {
        return 0
    }

    */
}
*/

// 조슈아 블로크의 [이펙티브 자바]의 try-with-resource를 코틀린에서 use를 사용해서 변환한 코드
fun firstLineOfFile(path: String?, defaultVal: String?): String? {
    /*
    try {
        return BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        return defaultVal
    }
    */
    return try {
        BufferedReader(FileReader(path)).use { br -> br.readLine() }
    } catch (e: IOException) {
        defaultVal
    }

}

fun firstLineOfFile(path: String?, defaultVal: String?): String? = try {
    BufferedReader(FileReader(path)).use { br -> return br.readLine() }
} catch (e: IOException) {
    defaultVal
}

// Nothng의 경우에는 타입추론을 사용하면 안된다.
// 일반적으로 Unit의 경우에는 생략을 하게 되는데 만일 생략하게 되면 Unit으로 인식하기 때문에 오류가 발생한다.
fun fail(message: String = "파라미터값을 확인해 보세요."): Nothing = throw IllegalArgumentException(message)

// Nothng의 경우에는 타입추론을 사용하면 안된다.
// 일반적으로 Unit의 경우에는 생략을 하게 되는데 만일 생략하게 되면 Unit으로 인식하기 때문에 오류가 발생한다.
fun failWithBody(message: String = "파라미터값을 확인해 보세요."): Nothing {
    throw IllegalArgumentException(message)
}

// 타입추론에 의해 반환타입이 String이라는 것을 명시해주지 않아도 된다.
fun test(test: String) = test.uppercase()
```
또한 Nothing의 특징을 이 코드에서도 알 수 있는데 strValue이 null이면 fail()를 호출할 것이고 Nothing이 반환되기 때문에 밑의 콘솔을 찍는 로직에 도달하지 못한다.        

# At a Glance

기본적인 예외처리를 알아 봤다.       

자바와 크게 다르지 않지만 throw를 통한 예외처리의 경우 엘비스 연산자를 통한 간결하게 처리할 수 있다.      

역시 이 부분도 놓치거나 보강할 내용이 있다면 업데이트 할 예정이다.
