package com.hyecheon.akkastudy.behavior

import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
class ManagerBehavior(context: ActorContext<String>) : AbstractBehavior<String>(context) {

    companion object {
        fun create() = run {
            Behaviors.setup(::ManagerBehavior)
        }
    }

    override fun createReceive(): Receive<String> {
        return newReceiveBuilder()
            .onMessageEquals("start") {
                repeat(20) {
                    val worker = context.spawn(WorkerBehavior.create(), "worker-${it}")
                    worker.tell("start")
                }
                this
            }
            .build()
    }
}