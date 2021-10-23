package com.hyecheon.akkastudy.behavior

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import java.math.BigInteger
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
class WorkerBehavior(context: ActorContext<String>) : AbstractBehavior<String>(context) {
    companion object {
        fun create() = Behaviors.setup(::WorkerBehavior)
    }

    override fun createReceive(): Receive<String> {
        return newReceiveBuilder()
            .onAnyMessage { message ->
                when (message) {
                    "start" -> {
                        val bigInteger = BigInteger(2000, Random())
                        println(bigInteger.nextProbablePrime())
                    }
                }
                this
            }
            .build()
    }
}