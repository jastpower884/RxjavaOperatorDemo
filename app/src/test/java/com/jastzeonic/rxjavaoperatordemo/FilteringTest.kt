package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import org.junit.Test
import org.reactivestreams.Subscriber
import java.util.concurrent.TimeUnit

class FilteringTest {
    @Test
    fun debounce() {

        val debounceObservable = Observable.create(ObservableOnSubscribe<String> {
            for (i in 1..100) {
                if (!it.isDisposed) {
                    it.onNext("The number is $i")
                }

                var sleepTime = 100
                //this will not be received
                if (i % 2 == 0) {
                    sleepTime = 300
                    //this will be received
                }
                it.onNext("Z")
                Thread.sleep(sleepTime.toLong())

            }
            it.onComplete()
        })


        // same as throttleWithTimeout(
        debounceObservable
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe {
                    println(it)
                }

        println("Next")
        debounceObservable.debounce { text ->
            Observable.create(ObservableOnSubscribe<String> {
                if (!text.contains("Z")) {
                    it.onNext(text)
                    it.onComplete()
                }

            })
        }.subscribe {
            println(it)
        }

    }


    @Test
    fun distinct() {

        Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1).distinct()
                .subscribe {
                    println("This is result = $it")
                }

        println("Next")

        Observable.just(1, 2, 3, 3, 3, 1, 2, 3, 3).distinctUntilChanged()
                .subscribe {
                    println("This is result = $it")
                }

    }

    @Test
    fun elementAt() {
        Observable.just(0, 1, 2, 3, 4, 5).elementAt(2)
                .subscribe {
                    println("This is result = $it")
                }
    }

    @Test
    fun filter() {
Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter { result -> result in 4..7 }
        .subscribe {
            println("This is result = $it")
        }

    }

    @Test
    fun first() {

        Observable.just(0, 1, 2, 3, 4, 5).first(0).subscribe(
                Consumer { println("This is result = $it") }

        )


    }

    @Test
    fun last() {

        Observable.just(0, 1, 2, 3, 4, 5).last(20).subscribe(
                Consumer { println("This is result = $it") }

        )

    }

    @Test
    fun skip() {

        // skip start of count
        Observable.just(0, 1, 2, 3, 4, 5).skip(2)
                .subscribe {
                    println("This is result = $it")
                }
    }

    @Test
    fun take() {

        // take first of count
        Observable.just(0, 1, 2, 3, 4, 5).take(3)
                .subscribe {
                    println("This is result = $it")
                }
    }


    @Test
    fun sample() {

        val sampleObservable = Observable.create(ObservableOnSubscribe<String> {
            for (i in 1..10) {
                if (!it.isDisposed) {
                    it.onNext("The number is $i")
                }

                var sleepTime = 100
                //this will not be received
                if (i % 2 == 0) {
                    sleepTime = 300
                    //this will be received
                }
                Thread.sleep(sleepTime.toLong())

            }
            it.onComplete()
        })


        //find the emitter that close to time
        sampleObservable.sample(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("This is result = $it")
                }

        //find the emitter that first one in time
        sampleObservable.throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("This is result = $it")
                }

    }
}