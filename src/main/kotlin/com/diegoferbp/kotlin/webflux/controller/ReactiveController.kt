package com.diegoferbp.kotlin.webflux.controller

import com.diegoferbp.kotlin.webflux.model.Message
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api")
class ReactiveController {

    private val counter = AtomicLong()

    @GetMapping("/hello")
    fun hello(): Mono<String> {
        return Mono.just("Hello, WebFlux World!")
            .delayElement(Duration.ofMillis(100)) // Simulate async operation
    }

    @GetMapping("/message/{id}")
    fun getMessage(@PathVariable id: Long): Mono<Message>{
        return Mono.just(Message(id, "Hello from message $id"))
            .delayElement(Duration.ofMillis(200))
    }

    @GetMapping("/messages")
    fun getMessages(): Flux<Message> {
        return Flux.range(1, 5)
            .map { Message(it.toLong(), "Message number $it") }
            .delayElements(Duration.ofMillis(500)) // Simulate streaming data
    }

    @GetMapping(value = ["/stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamMessages(): Flux<Message> {
        return Flux.interval(Duration.ofSeconds(1))
            .map { Message(counter.incrementAndGet(), "Streaming message at ${System.currentTimeMillis()}") }
            .take(10) // Limit to 10 messages
    }

    @PostMapping("/message")
    fun createMessage(@RequestBody content: String): Mono<Message> {
        return Mono.just(Message(counter.incrementAndGet(), content))
            .delayElement(Duration.ofMillis(150)) // Simulate processing time
    }

    @GetMapping("/slow-operation")
    fun slowOperation(): Mono<String> {
        return Mono.fromCallable {
            // Simulate a slow blocking operation
            Thread.sleep(2000)
            "Slow operation completed"
        }
            .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
    }

    @GetMapping("/parallel-processing")
    fun parallelProcessing(): Flux<String> {
        return Flux.range(1, 5)
            .parallel(3) // Process with 3 parallel rails
            .runOn(reactor.core.scheduler.Schedulers.parallel())
            .map { num ->
                Thread.sleep(1000) // Simulate work
                "Processed item $num on thread ${Thread.currentThread().name}"
            }
            .sequential()
    }
}