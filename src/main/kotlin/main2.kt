import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.File
import kotlin.text.Charsets.UTF_8


/**
 * Single : One next event or error event
 * Completable : Completed event or error event
 * Maybe : One next, completed or error event
 */
sealed class Droid : Throwable() {
    class OU812 : Droid()
}

sealed class FileReadError : Throwable() {
    class FileNotFound : FileReadError()
}

fun main(args: Array<String>) {

    exampleOf("create") {

        val subscriptions = CompositeDisposable()

        val droids = Observable.create<String> { emitter ->
            emitter.onNext("R2-D2")
//      emitter.onError(Droid.OU812())
            emitter.onNext("C-3PO")
            emitter.onNext("K-2SO")
//      emitter.onComplete()
        }

        val observer = droids.subscribeBy(
            onNext = { println(it) },
            onError = { println("Error, $it") },
            onComplete = { println("Completed") })

        //여기가 없으면 memory leaking 됨
        subscriptions.add(observer)
    }

    exampleOf("Single") {
        val subscriptions = CompositeDisposable()

        fun loadText(filename: String): Single<String> {
            return Single.create create@{ emitter ->
                val file = File(filename)

                if (!file.exists()) {
                    emitter.onError(FileReadError.FileNotFound())
                    return@create
                }
                val contents = file.readText(UTF_8)
                emitter.onSuccess(contents)
            }
        }

        val observer = loadText("ANewHope.txt")
            .subscribe(
                { println(it) },
                { println("Error, $it") }
            )

        subscriptions.add(observer)

        exampleOf("doOperator") {
            val subs = CompositeDisposable()

            val doTest = Observable.never<Any>()
                .doOnSubscribe { println("doOnSubscribe") }
                .doOnDispose { println("doOnDispose") }
                .doOnNext { println("doOnNext") }
                .doOnError { println("doOnError") }
                .doOnComplete { println("doOnComplete") }
                .subscribe()

            subs.add(doTest)
            //clear를 해야 onDispose 탐
            subs.clear()
        }

        exampleOf("doOperator-2") {
            val subs = CompositeDisposable()

            val doTest = Observable.never<Any>()
                .doOnSubscribe {
                    println("doOnSubscribe")
                    println(it.isDisposed)
                }
                .doOnDispose { println("doOnDispose") }
                .subscribeBy (
                    onNext = { println(it)},
                    onComplete = { println("completed")}
                )

            subs.add(doTest)
            //clear를 해야 onDispose 탐
            subs.clear()
        }
    }
    /**
     * PublishSubject
     * BehaviorSubject
     * ReplaySubject
     */
}