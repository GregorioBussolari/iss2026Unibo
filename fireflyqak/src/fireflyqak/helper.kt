package fireflyqak

import it.unibo.kactor.*
import unibo.basicomm23.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.delay
import unibo.basicomm23.interfaces.*


class helper(name: String) : ActorBasic(name, confined = true) {

    override suspend fun actorBody(msg: IApplMessage) {
        if (msg.msgId() == "start") {
        	var payload = msg.msgContent()
        	val num = payload.substringAfter("(").substringBefore(")").toInt()
        	CommUtils.outgreen("$name|  msg: $msg, to build $num flies")
        	
            logger.info("$name | sending build $num messages to creator")
            for (i in 1..num) {
                forward("build", "build($i)", "creator")
                logger.info("$name | sent build($i) to creator")
            }
            logger.info("$name | done sending all build messages")
        }
    }
}