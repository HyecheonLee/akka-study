package com.hyecheon.akkastudy.thread

import java.math.BigInteger

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/23
 */
class PrimeGenerator(
    private val result: Results,
) : Runnable {
    override fun run() {
        val bigInteger = BigInteger(2000, java.util.Random())
        result.primes.add(bigInteger.nextProbablePrime())
    }
}