import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import org.w3c.dom.CDATASection

/**
 * ignoreElements : ignores 'Next' events
 * elementAt : subscribe to exact element at. Returns a Maybe, subscribe with onSuccess insetead of onNext
 * filter : filter by conditions
 *
 * skip : suppress the first n items emitted by an Observable
 * skipWhile : discard items emitted by an Observable until a specified condition becomes false
 * take : emit only the first n items emitted by an Observable
 * takeWhile : mirror items emitted by an Observable until a specified condition becomes false
 * skipuntil : discard items emitted by an Observable until a second Observable emits an item
 * takeuntil : discard any items emitted by an Observable after a second Observable emits an item or terminates
 * distinctUntilChanged : This method will surface values only if they are different from the previous value

 */
fun main(args: Array<String>) {
    exampleOf("ignoreElement") {
        val subscriptions = CompositeDisposable()
        val cannedProjects = PublishSubject.create<String>()

        subscriptions.add(
            cannedProjects
                .ignoreElements()
                .subscribeBy(
//                onNext = { println("onNext : $it" )},
                    onError = { println("onError : $it") },
                    onComplete = { println("comleted") }
                )
        )

        cannedProjects.onNext(landOfDroids)
        cannedProjects.onNext(wookieWorld)
        cannedProjects.onNext(detours)
        cannedProjects.onComplete()
    }

    exampleOf("elementAt") {
        val subs = CompositeDisposable()
        val quotes = PublishSubject.create<String>()

        subs.add(
            quotes.elementAt(2) //Returns a Maybe, subscribe with onSuccess insetead of onNext
                .subscribeBy(
                    onComplete = { println("onComplete") },
                    onSuccess = { println("onSuccess : $it") },
                    onError = { println("onError : $it") }
                )
        )
        quotes.onNext(mayTheOdds)
        quotes.onNext(liveLongAndProsper)
        quotes.onNext(mayTheForce)
    }

    exampleOf("filter") {
        val subs = CompositeDisposable()
        subs.add(
            Observable.fromIterable(tomatometerRatings)
                .filter { it.rating >= 90 }
                .subscribe { println(it) }
        )
    }

    exampleOf("skipWhile") {
        val subs = CompositeDisposable()
        subs.add(
            Observable.fromIterable(tomatometerRatings)
                .skipWhile { it.rating < 90 }
                .subscribe { println(it) }
        )
    }

    exampleOf("skipUntil") {
        val subscriptions = CompositeDisposable()

        val subject = PublishSubject.create<String>()
        val trigger = PublishSubject.create<Unit>()

        subscriptions.add(
            subject.skipUntil(trigger)
                .subscribe{
                    println(it)
                }
        )

        subject.onNext(episodeI.title)
        subject.onNext(episodeII.title)
        subject.onNext(episodeIII.title)

        trigger.onNext(Unit)

        subject.onNext(episodeIV.title)
    }

    exampleOf("takeUntil") {
        val subscriptions = CompositeDisposable()

        val subject = PublishSubject.create<String>()
        val trigger = PublishSubject.create<Unit>()

        subscriptions.add(
            subject.takeUntil(trigger)
                .subscribe{
                    println(it)
                }
        )

        subject.onNext(episodeI.title)
        subject.onNext(episodeII.title)
        subject.onNext(episodeIII.title)

        trigger.onNext(Unit)

        subject.onNext(episodeIV.title)
    }

    exampleOf("distinctUntilChanged") {
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            Observable.just(Droid2.R2D2, Droid2.C3PO,  Droid2.C3PO, Droid2.R2D2)
                .distinctUntilChanged()
                .subscribe { println(it) }
        )
    }
}