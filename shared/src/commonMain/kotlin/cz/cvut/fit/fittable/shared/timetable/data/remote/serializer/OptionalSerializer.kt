package cz.cvut.fit.fittable.shared.timetable.data.remote.serializer

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

object OptionalSerializer : JsonTransformingSerializer<String>(String.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonObject) {
            JsonPrimitive("")
            // Ignore the property if it's an object
        } else {
            element
        }
    }
}