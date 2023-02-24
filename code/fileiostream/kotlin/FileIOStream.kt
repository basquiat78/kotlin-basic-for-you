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

    val binaryFilePath = "binary.bin"
    val binaryFile = File(binaryFilePath)

    binaryFile.outputStream().use {
        it.write("hi~ there!".toByteArray())
    }

    binaryFile.inputStream().use {
        println(String(it.readAllBytes()))
    }


    // bufferedWriter -> BufferedWriter
    FileOutputStream(filePath).bufferedWriter().use {
        it.write("is append")
        it.newLine()
        it.write("data ok!")
    }

    // buffered -> BufferedOutputStream
    // append에 true를 줘서 이어쓰기를 한다.
    FileOutputStream(filePath, true).buffered().use {
        it.write("\nhi".toByteArray())

    }

    // writer -> OutputStreamWriter
    // append에 true를 줘서 이어쓰기를 한다.
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

    // 하위 디렉터리를 포함한 디렉터리 생성
    File("root/my/name/is/basquiat").mkdirs()
    // 단일 디렉터리 생성
    File("test").mkdir()

    val root = File("root")
    val test = File("test")
    val rootIsDeleted = root.delete()
    val testIsDeleted = test.delete()

    println("root is deleted? : $rootIsDeleted")
    println("test is deleted? : $testIsDeleted")

    println("again root is deleted? : ${root.deleteRecursively()}")

    // 원본 파일
    val original = File("original.txt")
    original.writeText("this is original")

    // 원본파일을 copiedFile이라는 이름으로 복사본을 만든다.
    val copiedFile = original.copyTo(File("copiedFile"))
    println(copiedFile.readText())

    // test와 하위 디렉터리 sub 생성
    File("test/sub").mkdirs()
    // test/sub 디렉터리에 sub1/sub2 파일 생성
    File("test/sub/sub1.txt").run {
        writeText("sub1")
    }
    File("test/sub/sub21.txt").run {
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

    val tempDir = createTempDirectory("tmp")
    println(tempDir)


    val tempFile = createTempFile(directory = tempDir, prefix = "basquiat")
    println(tempFile)
}