package com.hyecheon.akkastudy.thread

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/23
 */
class BigPrimes {
    private val log = LoggerFactory.getLogger(this::class.java)

    @DisplayName("1. 싱글스레드에서 소수 찾기")
    @Test
    internal fun test_1() {

        val primes = TreeSet<BigInteger>()
        val elapseTime = measureTimeMillis {
            while (primes.size < 20) {
                val bigInteger = BigInteger(2000, Random())
                primes.add(bigInteger.nextProbablePrime())
            }
        }
        log.info("수행 시간 : $elapseTime ms.")
    }


    @DisplayName("2. 멀티스레드에서 소수 찾기")
    @Test
    internal fun test_2() {
        val result = Results()

        val elapseTime = measureTimeMillis {
            val task = PrimeGenerator(result)
            val threadPool = mutableListOf<Thread>()
            for (i in 1..20) {
                val t = Thread(task)
                t.start()
                threadPool.add(t)
            }
            threadPool.forEach { it.join() }
        }
        log.info("수행 시간 : $elapseTime ms.")
    }

    @DisplayName("3. kotlin 코루틴으로 작업")
    @Test
    internal fun test_3() {
        val elapseTime = measureTimeMillis {
            runBlocking {
                (1..20).map {
                    async(Dispatchers.Default) {
                        BigInteger(2000, Random())
                    }
                }.map { deferred -> deferred.await() }
            }
        }
        log.info("수행 시간 : $elapseTime ms.")
    }
}