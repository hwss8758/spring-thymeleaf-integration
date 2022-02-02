package com.example.springthymeleafintegration.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
class SessionController {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/session-info")
    fun sessionInfo(request: HttpServletRequest): String {
        val session = request.getSession(false) ?: return "no session!!"

        // session data 출력
        session.attributeNames.asIterator().forEachRemaining { name ->
            logger.info { "session name=$name, value=${session.getAttribute(name)}" }
        }

        logger.info { "sessionId=${session.id}" }
        logger.info { "maxInactiveInterval=${session.maxInactiveInterval}" }
        logger.info { "creationTime=${Date(session.creationTime)}" }
        logger.info { "lastAccessedTime=${Date(session.lastAccessedTime)}" }
        logger.info { "isNew=${session.isNew}" }

        return "session print"
    }
}