package com.hyecheon.akkastudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AkkaStudyApplication

fun main(args: Array<String>) {
	runApplication<AkkaStudyApplication>(*args)
}
