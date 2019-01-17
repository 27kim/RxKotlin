import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.util.HalfSerializer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

fun main(args: Array<String>) {
    exampleOf("Creating observable") {
        val mostPopular = Observable.just(episodeV, episodeI, episodeII, episodeV, episodeI, episodeII)

        mostPopular.subscribe { element ->
            println(element)
        }
        println("======================")
        mostPopular.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("onComplete") },
            onError = { println("onError") }
        )
    }

    exampleOf("empty") {
        val mostPopular = Observable.empty<Unit>()
        mostPopular.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("onComplete") },
            onError = { println("onError") }
        )
    }

    exampleOf("never") {
        val mostPopular = Observable.never<Any>()
        mostPopular.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("onComplete") },
            onError = {}
        )
    }

    exampleOf("dispose") {
        val mostPopular = Observable.just(episodeV, episodeI, episodeII, episodeV, episodeI, episodeII)
        val subs = mostPopular.subscribe { element ->
            println(element)
        }

        subs.dispose()
    }

    exampleOf("compositeDisposable") {
        val mostPopular = Observable.just(episodeV, episodeI, episodeII, episodeV, episodeI, episodeII)
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            listOf(episodeV, episodeI, episodeII, episodeV, episodeI, episodeII)
                .toObservable()
                .subscribe {
                    println(it)
                }
        )
    }
}