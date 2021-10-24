package com.hyecheon.akkastudy.behavior

import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import java.io.Serializable
import java.math.BigInteger
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
class ManagerBehavior(context: ActorContext<Command>) : AbstractBehavior<ManagerBehavior.Command>(context) {
    companion object {
        fun create() = run {
            Behaviors.setup(::ManagerBehavior)
        }
    }

    sealed class Command : Serializable {
        object Start : Command()
        data class Result(val prim: BigInteger) : Command()
    }

    val primes = TreeSet<BigInteger>()


    override fun createReceive() = run {
        newReceiveBuilder()
            .onAnyMessage {
                when (it) {
                    is Command.Start -> {
                        repeat(20) {
                            val worker = context.spawn(WorkerBehavior.create(), "worker-${it}")
                            worker.tell(WorkerBehavior.Command.Start(context.self))
                        }
                        this
                    }
                    is Command.Result -> {
                        primes.add(it.prim)
                        println("I have received ${primes.size} prime numbers")
                        if (primes.size == 20) {
                            primes.forEach(System.out::println)
                        }
                        this
                    }
                }
            }
            .build()
    }
}