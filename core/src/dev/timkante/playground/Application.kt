package dev.timkante.playground

import kotlinx.coroutines.*
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import dev.timkante.playground.entity.Tag
import dev.timkante.playground.entity.weUser

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