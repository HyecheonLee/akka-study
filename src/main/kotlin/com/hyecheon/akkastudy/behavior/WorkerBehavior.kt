package com.hyecheon.akkastudy.behavior

import akka.actor.typed.ActorRef
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import com.hyecheon.akkastudy.behavior.ManagerBehavior.*
import com.hyecheon.akkastudy.behavior.ManagerBehavior.Command.Result
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
class WorkerBehavior(context: ActorContext<Command>) : AbstractBehavior<WorkerBehavior.Command>(context) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        fun create() = Behaviors.setup(::WorkerBehavior)
    }

    sealed class Command : Serializable {
        data class Start(val sender: ActorRef<ManagerBehavior.Command>? = null) : Command()
    }

    private var prime: BigInteger? = null


    override fun createReceive() = run {
        newReceiveBuilder()
            .onAnyMessage { receiveMsg ->
                when (receiveMsg) {
                    is Command.Start -> {
                        if (prime == null) {
                            val bigInteger = BigInteger(2000, Random())
                            prime = bigInteger.nextProbablePrime()
                        }
                        prime?.let {
                            receiveMsg.sender?.tell(Result(it))
                        }
                    }
                }
                this
            }
            .build()
    }
}