package com.dn0ne.moneymateserver.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bson.types.ObjectId

class ObjectIdSerializer : KSerializer<ObjectId> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ObjectId") {
        element<String>("\$oid")
    }
    override fun deserialize(decoder: Decoder): ObjectId {
        return decoder.decodeStructure(descriptor) {
            var oid: String? = null
            loop@ while (true) {
                when(val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> oid = decodeStringElement(descriptor, 0)
                    else -> throw SerializationException("Unexpected index: $index")
                }
            }

            ObjectId(oid)
        }
    }

    override fun serialize(encoder: Encoder, value: ObjectId) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.toHexString())
        }
    }
}