package com.hyecheon.akkastudy.config

import akka.actor.typed.ActorSystem
import com.hyecheon.akkastudy.behavior.FirstSimpleBehavior
import com.hyecheon.akkastudy.behavior.WorkerBehavior
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
@Configuration
class AkkaConfig {

    @Bean
    fun firstActorSystem() = run {
        ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem")
    }

    @Bean
    fun workerActorSystem() = run {
        ActorSystem.create(WorkerBehavior.create(), "workerActorSystem")
    }
}