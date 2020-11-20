package dev.timkante.playground.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

typealias Tag = String

@Serializable
data class WeUser(
    @Contextual @BsonId val id: ObjectId,
    val branchID: String = "loadTest",
    val isLoadTest: Boolean = true
);
