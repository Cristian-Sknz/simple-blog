package me.sknz.simpleblog.infra.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
class MongoOffsetDateTimeConverter {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(OffsetDateTimeReader, OffsetDateTimeWriter))
    }

    object OffsetDateTimeReader: Converter<OffsetDateTime, Date> {
        override fun convert(source: OffsetDateTime): Date? {
            return Date.from(source.toInstant().atZone(ZoneOffset.UTC).toInstant())
        }
    }

    object OffsetDateTimeWriter: Converter<Date, OffsetDateTime> {
        override fun convert(source: Date): OffsetDateTime? {
            return source.toInstant().atOffset(ZoneOffset.UTC)
        }
    }
}