package com.hyecheon.akkastudy.behavior

import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import org.slf4j.LoggerFactory


/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
class FirstSimpleBehavior(context: ActorContext<String>) : AbstractBehavior<String>(context) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        fun create() = Behaviors.setup(::FirstSimpleBehavior)
    }

    override fun createReceive(): Receive<String> {
        return newReceiveBuilder()
            .onMessageEquals("say hello") {
                log.info("hello")
                this
            }
            .onMessageEquals("who are you") {
                log.info("My path is ${context.self.path()}")
                this
            }
            .onMessageEquals("create a child") {
                val secondActor = context.spawn(create(), "secondActor")
                secondActor.tell("who are you")
                this
            }
            .onAnyMessage { message ->
                log.info("I received the message : $message")
                this
            }
            .build()
    }
}