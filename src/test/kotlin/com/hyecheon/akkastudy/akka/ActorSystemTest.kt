package com.hyecheon.akkastudy.akka

import akka.actor.typed.ActorSystem
import com.hyecheon.akkastudy.behavior.ManagerBehavior
import com.hyecheon.akkastudy.behavior.WorkerBehavior
import com.hyecheon.akkastudy.config.AkkaConfig
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.lang.Thread.sleep

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/24
 */
@SpringBootTest
@Import(AkkaConfig::class)
class ActorSystemTest {

    @Autowired
    @Qualifier("firstActorSystem")
    lateinit var actorSystem: ActorSystem<String>

    @Autowired
    @Qualifier("workerActorSystem")
    lateinit var workActorSystem: ActorSystem<WorkerBehavior.Command>

    @DisplayName("1. tell")
    @Test
    internal fun test_1() {
        actorSystem.tell("Hello are you there?")
        actorSystem.tell("This is the second message.")
    }


    @DisplayName("2. 메시지 분리 테스트")
    @Test
    internal fun test_2() {
        actorSystem.tell("say hello")
        actorSystem.tell("who are you")
        actorSystem.tell("This is the second message.")
    }

    @DisplayName("3. 자식 생성")
    @Test
    internal fun test_3() {
        actorSystem.tell("create a child")
    }


    @DisplayName("4. worker ActorSystem")
    @Test
    internal fun test_4() {
        workActorSystem.tell(WorkerBehavior.Command.Start())
        sleep(1000)
    }

    @DisplayName("5. manager ActorSystem")
    @Test
    internal fun test_5() {
        val bigPrimes = ActorSystem.create(ManagerBehavior.create(), "BigPrime")
        bigPrimes.tell(ManagerBehavior.Command.Start)
        sleep(2000)
    }
}