package com.hyecheon.akkastudy.behavior

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import java.io.Serializable
import java.time.Duration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/26
 */
class RaceController private constructor(context: ActorContext<Command>) :
    AbstractBehavior<RaceController.Command>(context) {
    sealed class Command : Serializable {
        object Start : Command()
        data class RacerUpdate(val racer: ActorRef<Racer.Command>, val position: Int) : Command()
        data class RacerComplete(val racer: ActorRef<Racer.Command>) : Command()
        object GetPosition : Command()
    }

    companion object {
        fun create() = run {
            Behaviors.setup(::RaceController)
        }
    }

    private lateinit var currentPositions: HashMap<ActorRef<Racer.Command>, Int>
    private var start: Long = 0
    private val raceLength = 50
    private var TIMER_KEY: Any? = null
    private lateinit var timer: Behavior<Command>
    private fun displayRace() = run {
        val displayLength = 160
        repeat(50) { println() }
        println("Race has been running for ${(System.currentTimeMillis() - start) / 1000} second")
        println("=".repeat(displayLength))
        currentPositions.forEach { (k, v) ->
            println("${k.path()} : ${"*".repeat(v * displayLength / 100)}")
        }
    }

    override fun createReceive(): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage { message ->
                when (message) {
                    is Command.Start -> {
                        start = System.currentTimeMillis()
                        currentPositions = hashMapOf()
                        repeat(10) {
                            val racer = context.spawn(Racer.create(), "racer${it}")
                            currentPositions[racer] = 0
                            racer.tell(Racer.Command.Start(raceLength))
                        }

                        this.timer = Behaviors.withTimers { it ->
                            it.startTimerAtFixedRate(TIMER_KEY, Command.GetPosition, Duration.ofSeconds(1))
                            this
                        }
                        this.timer
                    }
                    is Command.RacerUpdate -> {
                        currentPositions[message.racer] = message.position
                        this
                    }
                    is Command.GetPosition -> {
                        currentPositions.forEach { (racer, u) ->
                            racer.tell(Racer.Command.Position(context.self))
                            displayRace()
                        }
                        this
                    }
                    is Command.RacerComplete -> {
                        val racer = message.racer
                        println("racer complete ${racer.path()} ${(System.currentTimeMillis() - start) / 1000} second")
                        currentPositions.remove(racer)
                        this
                    }
                }
            }
            .build()

    }

}