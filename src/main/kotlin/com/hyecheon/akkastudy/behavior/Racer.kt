package com.hyecheon.akkastudy.behavior

import akka.actor.typed.ActorRef
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import scala.util.Random
import java.io.Serializable
import kotlin.math.roundToInt

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/26
 */
class Racer private constructor(context: ActorContext<Command>) : AbstractBehavior<Racer.Command>(context) {
    sealed class Command : Serializable {
        data class Start(val raceLength: Int) : Command()
        data class Position(val controller: ActorRef<RaceController.Command>) : Command()
    }

    companion object {
        fun create() = run {
            Behaviors.setup(::Racer)
        }
    }

    private val defaultAverageSpeed = 48.2
    private var averageSpeedAdjustmentFactor: Int = 0
    private lateinit var random: Random

    private var currentSpeed = 0
    private var currentPosition = 0
    private var raceLength = 0


    private fun getMaxSpeed() = run {
        defaultAverageSpeed * (1 + (averageSpeedAdjustmentFactor / 100.0))
    }

    private fun getDistanceMovedPerSecond() = run {
        currentSpeed * 1000 / 3600
    }

    private fun determineNextSpeed() = run {
        currentSpeed = if (currentPosition < (raceLength / 4)) {
            (currentPosition + (((getMaxSpeed() - currentSpeed) / 10) * random.nextDouble())).roundToInt()
        } else {
            (currentPosition * (0.5 + random.nextDouble())).roundToInt()
        }
        if (currentSpeed > getMaxSpeed()) {
            currentSpeed > getMaxSpeed()
        }
        if (currentSpeed < 5) {
            currentSpeed = 5

        }
        if (currentPosition > (raceLength / 2) && currentSpeed < getMaxSpeed() / 2) {
            currentSpeed = (getMaxSpeed() / 2).roundToInt()
        }
    }

    override fun createReceive(): Receive<Command> {
        return newReceiveBuilder()
            .onAnyMessage { message ->
                when (message) {
                    is Command.Start -> {
                        this.raceLength = message.raceLength
                        this.random = Random()
                        this.averageSpeedAdjustmentFactor = random.nextInt(30) - 10
                        this
                    }
                    is Command.Position -> {
                        determineNextSpeed()
                        currentPosition += getDistanceMovedPerSecond()
                        val controller = message.controller
                        if (currentPosition > raceLength) {
                            controller.tell(RaceController.Command.RacerComplete(context.self))
                        } else {
                            controller.tell(RaceController.Command.RacerUpdate(context.self, currentPosition))
                        }
                        this
                    }
                }
            }
            .build()
    }
}