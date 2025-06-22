package com.diegoferbp.kotlin.webflux.handler

import com.diegoferbp.kotlin.webflux.model.Message
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong


@Component
class MessageHandler {

    private val counter = AtomicLong()

    fun hello(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .bodyValue("Hello from Functional WebFlux!")
    }

    fun getMessages(request: ServerRequest): Mono<ServerResponse> {
        val messages = Flux.range(1, 5)
            .map { Message(it.toLong(), "Functional message $it") }
            .delayElements(Duration.ofMillis(300))

        return ServerResponse.ok()
            .body(messages, Message::class.java)
    }

    fun createMessage(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<String>()
            .map { content -> Message(counter.incrementAndGet(), content) }
            .flatMap { message ->
                ServerResponse.ok().bodyValue(message)
            }
    }
}