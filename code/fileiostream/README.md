# File과 I/O Stream
코틀린 표준 라이브러리에는 자바에서 사용하는 I/O Stream에 대한 확장 함수들을 제공한다.    

이를 통해서 관련 API를 사용하는데 있어서 좀 더 단순하고 쉽게 사용할 수 있도록 도와준다.

예를 들면 자바의 경우

```java
import java.io.*;

public class Main {

    public static void main(String[] args) {

        try(
            FileWriter fr = new FileWriter("text.txt");
            BufferedWriter bw = new BufferedWriter(fr)
        ) {
            bw.write("test");
            bw.newLine();
            bw.write("is ok!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(
            FileReader fr = new FileReader("text.txt");
            BufferedReader br = new BufferedReader(fr)
        ) {
            br.lines()
              .forEach(System.out::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
```
처럼 작성을 할 수 있다. 

하지만 뭔가 코드가 간결하지 못하고 복잡한 느낌을 준다.     

물론 우리는 개발자이기 때문에 이런 것을 묶어서 API로 제공할 수 있도록 코드를 작성할 수 있다.      

하지만 코틀린에서는 이것을 좀 더 간결하게 사용할 수 있다.

`try-with-resources`를 사용하는 방식은 코틀린에서 `use`함수를 사용할 수 있는데 한번 위 코드를 바꿔보자.

# FileWriter And FileReader

```kotlin
fun main() {
    val filePath = "test.txt"
    
    // BufferedWriter를 사용해서 빠르게 파일을 써보자.
    FileWriter(filePath).buffered().use {
        it.write("test")
        it.newLine()
        it.write("is")
        it.newLine()
        it.write("ok")
        it.newLine()
    }

    // 기존 파일에 이어서 쓰기
    FileWriter(filePath, true).use {
        it.write("test\nis\nok")
    }

    FileReader(filePath).use {
        println(it.readText())
    }

    // BufferedReader를 사용해서 빠르게 파일을 읽어오자
    FileReader(filePath).buffered().use {
        println(it.readLine())
        // 읽어온 라인만큼 남아있는 라인을 전부 읽어온다.
        println(it.readText())
    }

    // BufferedReader를 사용해서 빠르게 파일을 읽어오자
    FileReader(filePath).buffered().use { br ->
        br.forEachLine { println(it) }
    }

    // 전체를 읽어와서 라인의 정보를 리스트로 담아낸다.
    // 이 경우에는 use를 사용하지 않아도 알아서 스트림을 닫아준다.
    println(FileReader(filePath).readLines())
}
```
코드가 어떻게 보이는지 한번 살펴보라.    

확실히 `try-catch`가 없는 것만으로도 얼마나 간결한지 보자.       

## copyTo를 이용해서 복사하기

`copyTo`함수를 활용해서 좀 더 쉽게 다른 스트림으로 데이터를 복사해서 넘겨줄 수 있다.

```kotlin
public fun InputStream.copyTo(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long
```

그렇다면 다음과 같이 작성을 할 수 있다.

```kotlin
fun main() {
    val filePath = "test.txt"

    val output = ByteArrayOutputStream()
    FileInputStream(filePath).use {
        it.copyTo(output)
    }
    println("output : ${output.toString("UTF-8")}")
}
```

# File

이와 관련해서 `FileReadWrite.kt`에 보면 다양한 확장 함수들이 있다.

## bufferedReader/bufferedWriter And reader/writer

그중에 `bufferedReader`와 `bufferedWriter`함수를 알아보자.

물론 버퍼가 없는 `FileReader`와 `FileWriter`객체를 생성해서 사용할 수 있는 확장함수도 같이 존재한다.

```kotlin
fun main() {
    val filePath = "test.txt"

    val file = File(filePath)

    // buffer가 없는 경우 append를 통해서 작성한다.
    file.writer().use { it.append("hi!").appendLine().append("there!") }
    
    // BufferedWriter
    file.bufferedWriter().use {
        it.write("hi!!!!")
        it.newLine()
        it.write("there!!!!!")
    }

    // 버퍼가 없는 FileReader
    file.reader().use { println(it.readText()) }

    // BufferedReader
    file.bufferedReader().use { br ->
        br.forEachLine {
            println(it)
        }
    }
}
```
여기서 접두사에 `buffered`가 붙은 함수들은 기본적으로 다음과 같은 버퍼 사이즈가 잡혀있다.

```kotlin
public const val DEFAULT_BUFFER_SIZE: Int = 8 * 1024
```

그리고 공통적으로 `charset`은 `Charsets.UTF_8`로 잡혀있다.     

따라서 이것은 때에 따라서 세팅을 다른 값으로 해줄 수 있다.

파일의 경우에는 같은 파일이 있으면 덮어쓴다는 점은 주의하자.

만일 이어쓰기를 하겠다면 `FileWriter`객체에 넘겨서 옵션을 주어 사용해야 한다.

## 바이너리 파일

소위 이진 파일을 처리하고 싶은 경우에는 `inputStream`과 `outputStream`함수를 활용하면 된다.

```kotlin
fun main() {
    val binaryFilePath = "binary.bin"

    val file = File(binaryFilePath)

    file.outputStream().use {
        it.write("hi~ there!".toByteArray())
    }

    file.inputStream().use {
        println(String(it.readAllBytes()))
    }
}
```

## InputStream/OutputStream 확장 함수

코틀린 표준 라이브러리에는 `InputStream`, `OutputStream`관련해 단순화시킨 확장 함수 역시 포함되어 있다.

```kotlin
fun main() {
    val filePath = "test.txt"

    // bufferedWriter -> BufferedWriter
    FileOutputStream(filePath).bufferedWriter().use {
        it.write("is append")
        it.newLine()
        it.write("data ok!")
    }

    // buffered -> BufferedOutputStream
    FileOutputStream(filePath, true).buffered().use {
        it.write("\nhi".toByteArray())

    }

    // writer -> OutputStreamWriter
    FileOutputStream(filePath, true).writer().use {
        it.appendLine()
          .append("!is append!")
          .appendLine()
          .append("!!!!data ok!!!!!")
    }

    // bufferedReader -> BufferedReader
    val line = FileInputStream(filePath).bufferedReader().use {
        it.readText()
    }
    println("line : $line")

    // buffered -> BufferedInputStream
    val line1 = FileInputStream(filePath).buffered().use {
        String(it.readAllBytes())
    }
    println("line1 : $line1")

    
    // reader -> InputStreamReader
    val line2 = FileInputStream(filePath).reader().use {
        it.readText()
    }
    println("line2 : $line2")

}
```

## File Contents

지금까지 위에서 사용한 방식은 `InputStream`과 `OutputStream`을 상속한 `FileOutputStream`과 `FileInputStream`을 활용했다.

하지만 이런 `I/O Stream`을 사용하지 않고도 파일 콘테츠를 읽고 쓸수 있도록 코틀린 표준 라이브러리에서 함수를 제공한다.

```kotlin
fun main() {
    val filePath = "test.txt"
    val file = File(filePath)

    file.writeText("hi")
    file.appendText("\n")   // 이어 쓰기
    file.appendText("there")// 이어 쓰기
    //file.writeText("overwrite") // 덮어써버린다.

    // 파일의 내용을 한번에 가져온다.
    val readText = file.readText()
    println(readText)
    
    // reaLines() -> List<String>을 반환한다.
    file.readLines(Charsets.UTF_8)
        .forEach(System.out::println)

    // readLines와 forEach의 조합
    file.forEachLine(Charsets.UTF_8) {
        println(it)
    }

    // 여기서는 파일 전체의 라인의 정보들을 담아서 Sequnce<String>로 반환한다.
    // readLines()와 비슷하지만 sequence냐 list냐에 따른 차이가 있다.
    file.useLines(Charsets.UTF_8) { lines ->
        lines.forEach {
            println(it)
        }
    }
}
```
여기서 총 4개의 `API`를 활용해서 정보를 가져오는 방법을 볼 수 있는데 이는 상황에 따라 사용해야 한다.

`readText`의 경우에는 파일 콘텐츠를 전부 읽어서 가져오는 방식이기 때문에 용량이 큰 파일의 경우에는 맞지 않다.

`readLines`와 `forEach`의 조합과 `forEachLine`은 같은 방식으로 동작하는데 파일 전부를 읽는게 아니라 라인별로 처리하게 도와준다.

`useLines`는 시퀀스를 받아서 처리하는 방식으로 사용하게 된다.

필요하다면 `Charsets`정보를 기본 값 `UTF-8`이 아닌 상황에 따라 설정할 수도 있다.

우리는 앞서 이진 파일에 대한 케이스도 살펴보았는데 당연히 이와 관련된 함수 역시 제공한다.

```kotlin
fun main() {
    val binaryFilePath = "binary.bin"
    val binaryFile = File(binaryFilePath)

    binaryFile.writeBytes("hi".toByteArray())
    binaryFile.appendBytes("\n".toByteArray())// 이어 쓰기
    binaryFile.appendBytes("there".toByteArray())// 이어 쓰기

    // byteArray -> [B@26a1ab54
    println(binaryFile.readBytes())

    // string content -> hi there
    println(String(binaryFile.readBytes()))
    // byteArray to string list format -> [104, 105, 10, 116, 104, 101, 114, 101]
    println(binaryFile.readBytes().contentToString())

    // byteArray를 순회한다.
    binaryFile.readBytes().forEach {
        println(it)
    }

    // forEachBlock을 활용한다.
    // byteRead는 ByteArray로 넘어오는 buffer의 사이즈이다.
    // 따라서 이 코드는 위에서 사용한 코드와 같은 결과를 가져온다.
    binaryFile.forEachBlock { buffer, bytesRead ->
        (0 until bytesRead).forEach {
            println(buffer[it])
        }
    }
}
```
`forEachBlock`함수는 다음과 같이 생겼다.

```kotlin
public fun File.forEachBlock(action: (buffer: ByteArray, bytesRead: Int) -> Unit): Unit = forEachBlock(DEFAULT_BLOCK_SIZE, action)

public fun File.forEachBlock(blockSize: Int, action: (buffer: ByteArray, bytesRead: Int) -> Unit): Unit {
    val arr = ByteArray(blockSize.coerceAtLeast(MINIMUM_BLOCK_SIZE))
    // do something
}
```
`API`문서에는 다음과 같은 설명을 볼 수 있다.

```
action - function to process file blocks.
blockSize - size of a block, replaced by 512 if it's less, 4096 by default.
```
기본값은 `4096바이트`로 최소 크기는 `512바이트`로 지정하고 있다는 것을 알 수 있다.

## 아이템 49. 하나 이상의 처리 단계를 가진 경우에는 시쿼스를 하용하라.

언젠가는 [이펙티브 코틀린]에 대한 내용을 자세하게 풀어서 설명해 볼까 하는데 일단 여기서 `useLines`는 시퀀스로 넘어오는지에 대해 설명하고 있다.

[17. 컬렉션 함수 (Sequence 편)](https://github.com/basquiat78/kotlin-basic-for-you/tree/main/code/sequence#%EB%8B%A4%EB%A5%B8-%EC%BB%AC%EB%A0%89%EC%85%98%EA%B3%BC%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90)

여기서도 설명을 한 적이 있는데 리스트와는 다르게 시퀀스는 지연 계산을 한다는 것을 여러분들은 알것이다.

일반적으로 `List`를 통해 컬렉션 함수를 수행하게 되면 각 단계마다 컬렉션이 생길 수 있다.

이 책에서는 이와 관련 다음과 같은 예제를 제공한다.

```kotlin
fun main() {
    val numbers = (1..1000).toList()
    numbers.filter { it % 10 == 0 } // 여기서 컬렉션 하나
           .map { it*2 }            // 여기에서도 컬렉션 하나
           .sum()                   
    // 전체적으로 2개의 컬렉션이 생성된다.

    numbers.filter { it % 10 == 0 } // 여기서 컬렉션 하나
           .sumOf { it*2 }          // 컬렉션 처리 단계 수를 줄여 효율성을 높인다.
    
    numbers.asSequence()
           .filter { it % 10 == 0 }
           .map { it*2 }
           .sum()
    // 시퀀스를 사용하면 컬렉션이 만들어지지 않는다.
}
```
크거나 무거운 컬렉션을 처리한다면 새로운 컬렉션을 생성할 때마다 많은 리소스가 들 것이다.

물론 `아이템 50. 컬렉션 처리 단계 수를 제한하라`에 의거해서 `map().sum()`보다는 `sumOf`를 통해 효율성을 높일 수는 있다.

어째든 만일 용량이 큰 파일을 읽는다고 생각해 보자.

그래서 일반적으로 파일을 처리할 때는 시퀀스를 활용한다고 이 책에서는 설명하고 있다.

테스트로는 1.5기가정도의 데이터를 갖는 파일로 `readLines`와 `useLines`에 대해 테스트 결과를 담고 있다.

결과적으로 이 책의 저자의 경험으로 하나 이상의 처리 단계를 포함하는 컬렉션 처리는 시퀀스를 사용하면 20에서 40% 정도의 성능이 향상된다고 한다.

# File System Utility

자바에서는 파일 복사와 삭제, 그리고 디렉터리를 순회한다던가 디렉터리 생성등 다양한 방법들이 존재한다.      

코틀린 표쥰 라이브러리에서 역시 이것을 좀 더 쉽게 사용할 수 있도록 제공하는 함수들이 존재한다.

하나씩 알아보자.

## delete file or directory

자바에서는 디렉터리 내에 하위 디렉터리나 파일이 있으면 재귀호출로 하위 디렉터리의 파일을 지우고 디렉터리를 지우는 방식으로 작성해야 한다.

왜냐하면 디렉터리 삭제시 파일이 있으면 삭제가 되지 않기 때문이다.

그래서 하위 디렉터리가 있는지 확인하고 하위 디렉터리에 파일이 있는지 확인하고 또 하위 디렉터리가 있는지...... 이거 반복하면서 해야한다.

예전에 짜둔 코드가 존재해서 한번 이런 방식으로 짜야 한다는 것을 보여주고자....

```java
public class FileUtils {
    public static void deleteRecursively(String path) {
        File root = new File(path);
        try {
            // root가 일단 존재하는지 확인하자.
            if(root.exists()) {
                // root가 디렉터리라면 디렉터리 내의 파일 정보를 리스트로 가져온다.
                File[] directories = root.listFiles();
                for(File target : directories) {
                    if(target.isFile()) {
                        target.delete();
                    } else {
                        // 디렉터리라면 재귀호출로 다시 한번 순회를 한다. 
                        deleteRecursively(target.getPath());
                    }
                }
                // 루프를 돌고 하위 디렉터리의 파일과 하위 디렉터리를 전부 다 지운후
                // root 디렉터리를 지운다.
                root.delete();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
```

위와 같이 재귀 호출을 사용해야 하는데 코틀린에서는 `deleteRecursively`라는 함수를 제공한다.

딱 함수명만 봐도 알 수 있듯이 이 함수는 이 모든 것을 한번에 처리해 준다.

```kotlin
fun main() {
    // 하위 디렉터리를 포함한 디렉터리 생성
    File("root/my/name/is/basquiat").mkdirs()
    // 단일 디렉터리 생성
    File("test").mkdir()

    val root = File("root")
    val test = File("test")
    val testIsDeleted = test.delete()               // (1)
    val rootIsDeleted = root.delete()               // (2)

    println("root is deleted? : $rootIsDeleted")            
    println("test is deleted? : $testIsDeleted")
    println("again root is deleted? : ${root.deleteRecursively()}")  // (3)
}
```
(1)의 결과를 보면 `test`디렉터리는 지워진 것을 확인할 수 있다.     

하지만 (2)의 경우에는 `root`디렉터리는 하위 디렉터리를 가지고 있기 때문에 이 방식으로 지우면 `false`가 떨어진 것을 알 수 있다.

(3)처럼 호출하면 `true`가 떨어지면서 하위 디렉터리를 포함해 `root`디렉터리가 삭제된 것을 확인할 수 있다.

실제로 `FileTreeWalk`라는 객체를 통해서 `FileWalkDirection.BOTTOM_UP`방식으로 하위 디렉터리로부터 위로 올라가면서 삭제해 나가는 방식으로 구현되어 있다.

이와 관련 `copyRecursively`라는 함수도 존재하는데 반대로 `FileWalkDirection.TOP_DOWN`방식으로 상위 디렉터리에서 하위로 내려가면서 복사하는 방식이다.

일단 가장 기본적인 파일 복사부터 알아보자.

## 파일 복사

이미 위에서 `Stream`을 다루면서 사용해 봤는데 파일 복사 역시 이 함수를 활용해서 복사를 한다.

```kotlin
fun main() {
    // 원본 파일
    val original = File("original.txt")
    original.writeText("this is original")

    // 원본파일을 copiedFile이라는 이름으로 복사본을 만든다.
    val copiedFile = original.copyTo(File("copiedFile"))
    println(copiedFile.readText())
}
```

다만 `copyTo`를 이용해 파일 복사를 할 때는 이미 존재하는 파일이 있으면 

```
Exception in thread "main" kotlin.io.FileAlreadyExistsException: original.txt -> copiedFile: The destination file already exists.
```
과 같은 에러가 발생한다.

```kotlin
public fun File.copyTo(target: File, overwrite: Boolean = false, bufferSize: Int = DEFAULT_BUFFER_SIZE): File
```
`copyTo`라는 함수를 보면 `overwrite`라는 인자를 받을 수 있다. 기본값이 `false`인데 이 값을 `true`로 넘겨주면 덮어쓰기가 가능하다.

```kotlin
fun main() {
    // overwirte라는 파일을 하나 만든다.
    val overwrite = File("overwrite.txt")
    overwrite.writeText("will be overwrote")

    val overwroteFile = overwrite.copyTo(File("copiedFile"), overwrite = true)
    // 앞에서 복사한 파일의 내용이 덮어써진다.
    println(overwroteFile.readText())
}
```

## copyRecursively 함수

해당 함수의 존재 이유는 분명하다.

`copyTo`를 사용할 때 파일의 경우에는 상관없지만 만일 복사할 대상이 디렉터리인 경우에는 우리의 생각과는 다르게 동작한다.

그것은 그냥 디렉터리만 복사하고 해당 디렉터리안에 있는 파일과 디렉터리는 복사를 하지 않는다는 것이다.

이것은 디렉터리를 새로 생성하는 거랑 차이가 없다.

그래서 이 함수를 사용해 순회를 하면서 모든 파일/디렉터리를 보고 복사해야 한다.

```kotlin
public fun File.copyRecursively(
    target: File,
    overwrite: Boolean = false,
    onError: (File, IOException) -> OnErrorAction = { _, exception -> throw exception }
): Boolean
```
복사하는 행위이기 때문에 `overwrite`를 받는 것을 알 수 있다.    

또한 복사하는 도중 에러가 발생했을 때 이것을 어떻게 처리할지에 대한 람다를 받는 것도 확인할 수 있다.

```kotlin
public enum class OnErrorAction {
    /** Skip this file and go to the next. */
    SKIP,

    /** Terminate the evaluation of the function. */
    TERMINATE
}
```
여기서 `OnErrorAction`에 정의된 값을 보면 아주 명료하다.       

복사하다 에러가 나면 해당 부분은 그냥 무시하고 다음 복사를 진행할지 또는 복사를 중단할지 결정할 수 있다.

```kotlin
fun main() {
    // test와 하위 디렉터리 sub 생성
    File("test/sub").mkdirs()
    // test/sub 디렉터리에 sub1/sub2 파일 생성
    File("test/sub/sub1.txt").run {
        writeText("sub1")
    }
    File("test/sub/sub2.txt").run {
        writeText("sub2")
    }

    val test = File("test")
    val copied = File("copied")
    // test하위 디렉터리의 모든 파일과 디렉터리를 copied 디렉터리로 복사한다.
    test.copyRecursively(copied, oeverwrite = true) { 
        // file, exception -> OnErrorAction.SKIP
        // 사용하지 않으면 언더스코어로
        // 에러 메세지와 어떤 파일을 복사하다 났는지 알고 싶다면 
        // {
        //    println("error file name: ${file.name}")
        //    println("error message : ${exception.message}")
        // }
        _, _ -> OnErrorAction.SKIP
    }
}
```
다음과 같이 `copyRecursively`함수를 통해 디렉터리의 모든 파일과 디렉터리를 복사할 수 있다.

## walk함수로 디렉터리 순회하기
디렉터리를 순회하면서 하위 디렉터리와 파일을 순회하는 방법이 있다.

앞서 `FileTreeWalk`객체를 언급한 적이 있다.

이 객체에서 제공하는 `walk`함수를 통해 디렉터리를 순회할 수 있다.      

```kotlin
public fun File.walk(direction: FileWalkDirection = FileWalkDirection.TOP_DOWN): FileTreeWalk
```
기본적으로는 상위 디렉터리에서 하위 디렉터리로 순회하는 방식이지만 상위 디렉터리를 기준으로 하위 디렉터리에서 상위 디렉터리로 순회하는 방식을 지정할 수 있다.

이 함수는 반환값이 `Sequence<File>`이기 때문에 관련 컬렉션 함수를 사용할 수 있다. 

일단 우리가 앞서 테스트한 `test/sub`를 순회 방식에 따라 어떻게 다른지 한번 확인해 보자.

```kotlin
import kotlin.io.FileWalkDirection.*

fun main() {
    val top = File("test")

    println("============TOP_DOWN============")
    // 기본 topdown
    top.walk().forEach {
        println(it.name)
    }
    top.walk(TOP_DOWN).forEach {
        println(it.name)
    }
    top.walkTopDown().forEach {
        println(it.name)
    }

    println("============BOTTOM_UP============")
    // bottomup
    top.walk(BOTTOM_UP).forEach {
        println(it.name)
    }
    top.walkBottomUp().forEach {
        println(it.name)
    }
}
```
여기서 `TOP_DOWN`과 `BOTTOM_UP`을 위한 함수인 `walkTopDown`과 `walkBottomUp`를 사용할 수도 있다.

위 결과는 다음과 같다.

```
============TOP_DOWN============
test
sub
sub1.txt
sub2.txt
============BOTTOM_UP============
sub1.txt
sub2.txt
sub
test
```
순회 방향을 생각해 보면 결과는 예측이 가능하다.

순회를 할 때 순회할 최대 깊이를 결정할 수 있는 `maxDepth`함수도 존재한다.

이 함수의 기본 최대 깊이가 `Int.MAX_VALUE`로 설정되어 있어 있기 때문에 아마도 실무에서는 거의 대부분의 디렉터리를 순환하게 된다.

만일 여기서 위 코드에서 이 최대 깊이를 1로 설정하면 어떻게 될까?

```kotlin
fun main() {
    val top = File("test")

    println("============TOP_DOWN============")
    top.walkTopDown().maxDepth(1).forEach {
        println(it.name)
    }

    println("============BOTTOM_UP============")
    top.walkBottomUp().maxDepth(1).forEach {
        println(it.name)
    }
}
```
최대 깊이를 1로 설정했기 때문에 상위 디렉터리를 기준으로 `sub`디렉터리까지만 순회하고 끝날 것이다.

```
============TOP_DOWN============
test
sub
============BOTTOM_UP============
sub
test
```
순회할때 특정 디렉터리를 순회할지 않할지 결정할 수 있는 `onEnter`도 존재한다.

또한 `copyRecursively`처럼 순회하다 에러가 발생했을 때 처리하도록 도와주는 `onFail`이 있다.

그리고 모든 디렉터리를 순회한 이후 해당 디렉터리 쉰회를 끝내는 순간 특정 동작을 지정할 수 있다.

`onEnter`는 `(File) -> Boolean` 람다를 받는데 기본적으로 `true`이며 `false`인 경우 순회를 하지 않는다.

`onLeave`는 `(File) -> Unit` 람다를 받smsek.

다음 코드를 통해 특징을 확인해 보자.

여기서 테스트한 디렉터리 구조는 다음과 같이 설정을 해놨다.

```
├── test
│   ├── another
│   │   ├── another1.txt
│   │   ├── another2.txt
│   ├── other
│   │   ├── other1.txt
│   │   ├── other2.txt
│   ├── sub
│   │   ├── sub1.txt
│   │   ├── sub2.txt
```

```kotlin
fun main() {

    val top = File("test")
    top.walkTopDown()
       // sub 디렉터리는 순회하지 않는다.
       .onEnter { it.name != "sub"  }
       .onFail { _, _ -> OnErrorAction.SKIP }
       .onLeave {
            println("onLeave : ${it.name}")
       }
       .forEach {
            println("forEach : ${it.name}")
       }

}
```

```
forEach : test
forEach : other
forEach : other1.txt
forEach : other2.txt
onLeave : other
forEach : another
forEach : another1.txt
forEach : another2.txt
onLeave : another
onLeave : test
```
위 코드를 보면 `sub`디렉터리는 `onEnter`함수에서 제외했기 때문에 순회하지 않았다.

처음 `other`디렉터리를 전부 순회하고 난 이후 `onLeave`에서 해당 디렉터리 이름을 찍은 것을 확인할 수 있다.

`another`디렉터리 역시 마찬가지이다. 

그리고 최종적으로 `test`디렉터리를 다 순환했으니 마지막으로 `onLeave`에 해당 로그가 남긴 것을 확인할 수 있다.

## createTempDirectory() And createTempFile()

자바에 `createTempFile`이라는 메서드가 있는데 코틀린에서도 이와 비슷한 함수를 제공한다.

```kotlin
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile

fun main() {
    val tempDir = createTempDirectory("tmp")
    println(tempDir)


    val tempFile = createTempFile(directory = tempDir, prefix = "basquiat")
    println(tempFile)
}
```
`macOS`를 기준으로 다음과 같은 결과를 볼 수 있다.

```
/var/folders/rv/f1v3qzqs52zg78kz8mq8tm580000gn/T/tmp1007978042466927932
/var/folders/rv/f1v3qzqs52zg78kz8mq8tm580000gn/T/tmp1007978042466927932/basquiat9273271773484321598.tmp
```
디렉터리의 경우에는 `tmp`이루 임시 채번이 따진 것을 알 수 있으며 파일의 경우에도 `prefix`옵션을 주게 되면 `basquiat`이후 채번된 숫자와 함께 `.tmp`확장자로 파일이 생성된 것을 볼 수 있다.

참고로 `deprecated`된 `createTempDir`와 `createTempFile`함수도 있다.

공식 사이트에 다음 관련 내용이 있으니 확인해 보자.

[createTempDir](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/create-temp-dir.html)

[createTempFile](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/create-temp-file.html)

이 두개의 패키지 정보를 확인해 보면 `kotlin.io`에 있는 함수인 것을 알 수 있다.

이 보다는 위 예제 코드의 임포트된 패키지 경로를 확인해 보면 `kotlin.io.path`의 함수를 사용한 것을 확인할 수 있다.

# At a Glance

지금까지 코틀린 표준 라이브러리가 제공하는 함수를 활용한 파일과 `I/O Stream`을 다뤄봤다.

다음에는 애너테이션과 리플렉션을 한번 다뤄보고자 한다.


