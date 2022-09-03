fun main() {
    // 난 무조건 탱커 전사를 좋아한다.
    val warrior = Character("basquiat", "타우렌 탱커매니아", CharacterJob.WARRIOR)
    with(warrior) {
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level")
    }
    // 몬스터를 사냥할때마다 레벨 1씩 증가한다고 하자.
    for(level in 1..40) {
        //warrior.level = ++warrior.level
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 어떤 탈것을 얻을 수 있는지 체크 40레벨이 넘었기 때문에
    val possibleVehicle = warrior.levelStatus.possibleVehicle("Fire Horse")
    possibleVehicle.vehicleType()
    warrior.getVehicle(possibleVehicle)

    for(level in 1..20) {
        //warrior.level = ++warrior.level
        warrior.levelUp()
    }
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 위 로직을 지나면 60레벨이 넘었을 것이다.
    val possibleVehicle2 = warrior.levelStatus.possibleVehicle("Super Fire Horse")
    possibleVehicle2.vehicleType()
    warrior.getVehicle(possibleVehicle2)

    warrior.levelUp()
    with(warrior) {
        // 레벨업
        println("탱커매니아 정보: id : $id, name : $name, job : ${job.jobName}, level : $level, levelStatus : $levelStatus")
    }

    // 레벨업을 한번 했지만 레벨이 60대일 것이다.
    // 같은 이름을 가진 탈것을 얻어서 넣어보자.
    val possibleVehicle3 = warrior.levelStatus.possibleVehicle("Super Fire Horse")
    possibleVehicle3.vehicleType()
    warrior.getVehicle(possibleVehicle3)

    for(level in 1..20) {
        //warrior.level = ++warrior.level
        warrior.levelUp()
    }

    // 70레벨이 넘었다.
    val possibleVehicle4 = warrior.levelStatus.possibleVehicle("Fire Dragon")
    possibleVehicle4.vehicleType()
    warrior.getVehicle(possibleVehicle4)

    // 같은 이름의 탈것이 중간에 들어갔지만 set에 의해 중복 제거되고 3개의 탈것을 소지할 것이다.
    println(warrior.possibleVehicles())

}

/**
 * 게임 캐릭터
 */
class Character(
    val id: String,
    val name: String,
    val job:CharacterJob,
) {
    private val vehicles = mutableSetOf<Vehicle>()

    var level = 1
    var levelStatus = LevelStatus.LT40

    fun levelUp() {
        ++level
        levelStatus = LevelStatus.levelStatusFromLevel(level)
        /*
        levelStatus = when(level) {
            in 1.until(40) -> {
                LevelStatus.LT40
            }
            in 40.until(60) -> {
                LevelStatus.GTE40LT60
            }
            in 40.until(70) -> {
                LevelStatus.GTE60LT70
            }
            else -> {
                LevelStatus.GTE70
            }
        }*/
        /* if보다는 when를 추천. 코드가 간결해진다.
        levelStatus = if(level in 1.until(40)) {
            LevelStatus.LT40
        } else if(level in 40.until(60)) {
            LevelStatus.GTE40LT60
        } else if(level in 40.until(70)) {
            LevelStatus.GTE60LT70
        } else {
            LevelStatus.GTE70
        }
        */
    }

    fun getVehicle(vehicle: Vehicle) = vehicles.add(vehicle)

    fun possibleVehicles() = vehicles
}

/**
 * 60프로의 이동속도를 가지는 탈것
 */
data class NoVehicle(val name: String = "nothing"): Vehicle {
    override fun vehicleType() {
        println("탈것을 소지할 수 없습니다.")
    }
}

/**
 * 60프로의 이동속도를 가지는 탈것
 */
data class Land60PercentVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("60프로의 이동속도를 가지는 탈것, 이름은 $name")
    }
}

/**
 * 120프로의 이동속도를 가지는 탈것
 */
data class Land120PercentVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("120프로의 이동속도를 가지는 탈것, 이름은 $name")
    }
}

/**
 * 날것
 */
data class FlyingVehicle(val name: String): Vehicle {
    override fun vehicleType() {
        println("150프로의 이동속도를 가지는 날것, 이름은 $name")
    }
}

/*
/**
 * 60프로의 이동속도를 가지는 탈것
 */
class NoVehicle: Vehicle {
    override fun vehicleType() {
        println("탈것을 소지할 수 없습니다.")
    }
}

/**
 * 60프로의 이동속도를 가지는 탈것
 */
class Land60PercentVehicle: Vehicle {
    override fun vehicleType() {
        println("60프로의 이동속도를 가지는 탈것")
    }
}

/**
 * 120프로의 이동속도를 가지는 탈것
 */
class Land120PercentVehicle: Vehicle {
    override fun vehicleType() {
        println("120프로의 이동속도를 가지는 탈것")
    }
}

/**
 * 날것
 */
class FlyingVehicle: Vehicle {
    override fun vehicleType() {
        println("150프로의 이동속도를 가지는 날것")
    }
}
*/

sealed interface Vehicle {
    fun vehicleType()
}

/**
 * 심플하게 전사,사냥꾼,마법사,힐러,도둑만....
 */
enum class CharacterJob(
    val jobName: String,
) {
    WARRIOR("전사"),
    HUNTER("사냥꾼"),
    MAGICIAN("마법사"),
    CLERIC("성직자"),
    ROGUE("도둑")
}

// 고전적인 방식으로
enum class LevelStatus(
    val description: String,
    val levelCheck: (Int) -> Boolean,
    val possibleVehicle: (String) -> Vehicle,
) {
    LT40("40레벨미만", { it in 1.until(40) }, { NoVehicle() }),
    GTE40LT60("40레벨이상이고 60레벨 미만", { it in 40.until(60)}, { Land60PercentVehicle(it) }),
    GTE60LT70("60레벨이상이고 70레벨 미만", { it in 60.until(70)}, { Land120PercentVehicle(it) }),
    GTE70("70레벨이상", { it in 70..Int.MAX_VALUE}, { FlyingVehicle(it) });


    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ it.levelCheck(level) }
        //values().firstOrNull{ it.levelCheck(level) }
            ?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}

// 인터페이스를 활용한 방식
/*
interface LevelCheckable {
    fun levelCheck(level:Int): Boolean
}

enum class LevelStatus(
	val description: String,
): LevelCheckable {
    LT40("40레벨미만") {
    	override fun levelCheck(level: Int): Boolean = level in 1.until(40)
    },
    GTE40LT60("40레벨이상이고 60레벨 미만") {
     	override fun levelCheck(level: Int): Boolean = level in 40.until(60)
    },
    GTE60LT70("60레벨이상이고 70레벨 미만") {
 		override fun levelCheck(level: Int): Boolean = level in 60.until(70)
    },
    GTE70("70레벨이상") {
 		override fun levelCheck(level: Int): Boolean = level in 70..Int.MAX_VALUE
    };


    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
                                                            //values().firstOrNull{ it.levelCheck(level) }
        													?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
*/

//추상 메소드 활용
/*
enum class LevelStatus(
	val description: String,
) {
    LT40("40레벨미만") {
    	override fun levelCheck(level: Int): Boolean = level in 1.until(40)
    },
    GTE40LT60("40레벨이상이고 60레벨 미만") {
     	override fun levelCheck(level: Int): Boolean = level in 40.until(60)
    },
    GTE60LT70("60레벨이상이고 70레벨 미만") {
 		override fun levelCheck(level: Int): Boolean = level in 60.until(70)
    },
    GTE70("70레벨이상") {
 		override fun levelCheck(level: Int): Boolean = level in 70..Int.MAX_VALUE
    };

    abstract fun levelCheck(level: Int): Boolean

    companion object {
        /**
         * 레벨로 열거타입을 반환하는 정적 메소드
         */
        fun levelStatusFromLevel(level: Int): LevelStatus = values().firstOrNull{ levelStatus -> levelStatus.levelCheck(level) }
                                                            //values().firstOrNull{ it.levelCheck(level) }
        													?: throw IllegalArgumentException("캐릭터의 레벨 정보가 이상합니다.")

    }
}
*/