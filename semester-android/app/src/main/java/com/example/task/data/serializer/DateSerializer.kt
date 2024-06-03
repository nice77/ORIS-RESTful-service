package com.example.task.data.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date

class DateSerializer : KSerializer<Date> {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        val formattedDate = decoder.decodeString()
        return simpleDateFormat.parse(formattedDate) ?: throw SerializationException("Invalid date format: $formattedDate")
    }

    override fun serialize(encoder: Encoder, value: Date) {
        val formattedDate = simpleDateFormat.format(value)
        encoder.encodeString(formattedDate)
    }
}