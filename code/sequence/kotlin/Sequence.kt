fun main() {

    val list = listOf(1, 2, 3)
    val filteredList = list.filter {
        println("list in it -> $it")
        it > 2
    }
    println("filteredList : $filteredList")

    val sequence = sequenceOf(1, 2, 3)
    val filteredSequence = sequence.filter {
        println("sequence in it -> $it")
        it > 2
    }
    println("filteredSequence : $filteredSequence")
    println("filteredSequence : ${filteredSequence.toList()}")


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

    // use sequence builder example
    val genSequence = generateSequence(10) { if(it < 100) it + 1 else null }

    val useBuilder = sequence {
        yield(1)
        yieldAll(list.iterator())
        yieldAll(genSequence)
    }

    useBuilder.iterator().forEach {
            println("it : $it")
    }

    // 물론 filter, map도 가능
    val exampleSeq = sequenceOf(1, 2, 3, 4, 5, 6, 7)

    val result = exampleSeq.filter { it > 4}
                           .map { it * 10 }

    result.iterator().forEach {
            println("result it : $it")
    }

    println(result.first())
    println(result.last())
    println(result.elementAt(1))
    // 인덱스에서 벗어났기 때문에 null
    println(result.elementAtOrNull(1111))
    // null이 아닌 후행 람다에 정의된 값 -1이 결과값으로 나온다.
    println(result.elementAtOrElse(10000000) { -1 })

}