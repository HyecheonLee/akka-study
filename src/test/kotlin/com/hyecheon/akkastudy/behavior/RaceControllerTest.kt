package com.hyecheon.akkastudy.behavior

import akka.actor.typed.ActorSystem

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/27
 */
fun main() {
    val system = ActorSystem.create(RaceController.create(), "RaceSimulation")
    system.tell(RaceController.Command.Start)
}