import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

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

@InternalCoroutinesApi
@ExperimentalTime
suspend fun main() {
    val db = with(KMongo.createClient().coroutine) {
        getDatabase("we")
    }

    val col = db.getCollection<weUser>()

    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            col.ensureIndex(weUser::isElasticTest)

            repeat(100) {
                col.insertOne(
                    weUser(
                        id = ObjectId(),
                        branchID = weUser.randomBranch,
                        tags = (0..5).map { weUser.randomTag }
                    )
                )
            }
        }.onFailure {
            println("An error occured while creating indices and inserting documents [e=$it]")
        }.onSuccess {
            col.distinct<Tag>(
                fieldName = "tags",
                filter = weUser::branchID eq weUser.randomBranch
            ).consumeEach(::println)
        }
    }
}
