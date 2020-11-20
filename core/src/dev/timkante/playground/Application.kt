package dev.timkante.playground

import com.mongodb.CursorType
import kotlinx.coroutines.*
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import dev.timkante.playground.entity.WeUser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import org.litote.kmongo.ascending
import org.litote.kmongo.gt
import java.time.Year
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

suspend fun main() {
    getUsers()
}

suspend fun getUsers() {
    val db = with(KMongo.createClient().coroutine) {
        getDatabase("we")
    }

    val col = db.getCollection<WeUser>()

    var count = 0;

    withContext(Dispatchers.IO) {
        var timeSum = 0L
        var lastKnownObjectId: ObjectId = ObjectId.getSmallestWithDate(Date(0))
        var items = 0
        do {
            val time = measureTimeMillis {
                items = col.find(WeUser::id gt lastKnownObjectId)
                    .sort(ascending(WeUser::id))
                    .limit(1000)
                    .toFlow().onEach { lastKnownObjectId = it.id }.count()
            }
            count += items
            println("$count,$time")
            timeSum += time
        } while (items > 0)
        println("time sum: $timeSum")
    }

    println(count)

}

suspend fun main_() {

    val db = with(KMongo.createClient().coroutine) {
        getDatabase("we")
    }

    val col = db.getCollection<WeUser>()

    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            col.ensureIndex(WeUser::isLoadTest)

            repeat(1_000_000) {
                col.insertOne(
                    WeUser(
                        id = ObjectId(),
                    )
                )
            }
        }.onFailure {
            println("An error occured while creating indices and inserting documents [e=$it]")
        }.onSuccess {
            println("Done, yay")
        }
    }
}
