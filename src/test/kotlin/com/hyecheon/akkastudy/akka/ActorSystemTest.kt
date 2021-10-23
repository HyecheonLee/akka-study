package com.hyecheon.akkastudy.akka

import akka.actor.typed.ActorSystem
import com.hyecheon.akkastudy.config.AkkaConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
@SpringBootTest
@Import(AkkaConfig::class)
class ActorSystemTest {

    @Autowired
    lateinit var actorSystem: ActorSystem<String>


    @DisplayName("1. tell")
    @Test
    internal fun test_1() {
        actorSystem.tell("Hello are you there?")
        actorSystem.tell("This is the second message.")
    }

}