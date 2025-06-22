package com.diegoferbp.kotlin.webflux.router

import com.diegoferbp.kotlin.webflux.handler.MessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class FunctionalRouter {
    @Bean
    fun routes(handler: MessageHandler) = router {
        "/functional".nest {
            GET("/hello", handler::hello)
            GET("/messages", handler::getMessages)
            POST("/message", handler::createMessage)
        }
    }

}