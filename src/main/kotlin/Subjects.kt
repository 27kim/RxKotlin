import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import java.io.File
import kotlin.text.Charsets.UTF_8

/**
 * PublishSubject
 * BehaviorSubject
 *  Stateful
 *  Maintains state in value property
 *  Can access the value at any time
 * ReplaySubject
 */
fun main(args: Array<String>) {

    exampleOf("PublishSubject") {

        val quotes = PublishSubject.create<String>()
        quotes.onNext(itsNotMyFault)

        val subscriptionOne = quotes.subscribeBy(
            onNext = { printWithLabel("1)", it) },
            onComplete = { printWithLabel("1)", "Completed") }
        )

        quotes.onNext(doOrDoNot)

        val subscriptionTwo = quotes.subscribeBy(
            onNext = { printWithLabel("2)", it) },
            onComplete = { printWithLabel("2)", "Completed") }
        )

        quotes.onNext(lackOfFaith)

        subscriptionOne.dispose()

        quotes.onNext(eyesCanDeceive)

        quotes.onComplete()

        val subscriptionThree = quotes.subscribeBy(
            onNext = { printWithLabel("3)", it) },
            onComplete = { printWithLabel("3)", "Completed") }
        )

        quotes.onNext(stayOnTarget)

        subscriptionTwo.dispose()
    }

    exampleOf("BehaviorSubject") {
        val subscriptions = CompositeDisposable()
        val quotes = BehaviorSubject.createDefault<String>(iAmYourFather)
        val subscriptionOne = quotes.subscribeBy(
            onNext = { printWithLabel("1)", it) },
            onError = { printWithLabel("1)", it) },
            onComplete = { printWithLabel("1)", "Completed") }
        )

        //Complete and Error events will be replayed to new subscribers.
        quotes.onError(Quote.NeverSaidThat())
        println("Complete and Error events will be replayed to new subscribers.")
        subscriptions.add(quotes.subscribeBy(
            onNext = { printWithLabel("2)", it) },
            onError = { printWithLabel("2)", it) },
            onComplete = { printWithLabel("2)", "Completed") }
        ))
    }

    exampleOf("BehaviorSubject state") {
        val subscriptions = CompositeDisposable()
        val quotes = BehaviorSubject.createDefault<String>(mayTheForceBeWithYou)

        println(quotes.value)

        subscriptions.add(quotes.subscribeBy {
            printWithLabel("1)", it)
        })

        quotes.onNext(mayThe4thBeWithYou)
        println(quotes.value)
    }
    /**
     * Replay Subject
     *  Starts empty, with a buffer size
     *  replays buffer to new subscribers
     */

    exampleOf("ReplaySubject ") {
        val subscriptions = CompositeDisposable()
        val subject = ReplaySubject.createWithSize<String>(2)

        subject.onNext(useTheForce)

        subscriptions.add(subject.subscribeBy(
            onNext = { printWithLabel("1)", it) },
            onError = { printWithLabel("1)", it) },
            onComplete = { printWithLabel("1)", "Completed") }
        ))

        subject.onNext(theForceIsStrong)

        subscriptions.add(subject.subscribeBy(
            onNext = { printWithLabel("2)", it) },
            onError = { printWithLabel("2)", it) },
            onComplete = { printWithLabel("2)", "Completed") }
        ))
    }

}