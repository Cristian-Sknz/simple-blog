package me.sknz.simpleblog.domain.util

import com.fasterxml.jackson.databind.ObjectMapper
import me.sknz.simpleblog.api.request.SyncObjectRequest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

object SyncPushObject {

    private val mapper = Jackson2ObjectMapperBuilder.json()
        .failOnUnknownProperties(false)
        .build<ObjectMapper>()

    fun getValidChanges(): SyncObjectRequest {
        return mapper.readValue(
            ClassPathResource("syncpush.json").inputStream,
            SyncObjectRequest::class.java
        )
    }

    fun getInvalidChanges(): SyncObjectRequest {
        return mapper.readValue(
            ClassPathResource("invalidsyncpush.json").inputStream,
            SyncObjectRequest::class.java
        )
    }
}