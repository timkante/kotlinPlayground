package dev.timkante.playground.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

typealias Tag = String

@Serializable
data class weUser(
    @Contextual @BsonId val id: ObjectId,
    val branchID: String,
    val tags: List<Tag>,
    val isElasticTest: Boolean = true
) {
    companion object Randomizer {
        val randomTag: Tag
            get() = arrayOf(
                "abcde@gmail.com",
                "test.user@staffbase.com",
            ).random()
        val randomBranch: String
            get() = arrayOf("branch1", "branch2", "branch3").random()
    }
}