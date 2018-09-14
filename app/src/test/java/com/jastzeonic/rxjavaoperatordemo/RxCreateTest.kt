package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.junit.Test

import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RxCreateTest {
    @Test
    fun create() {

        Observable.create(ObservableOnSubscribe<String> {
            it.onNext("onNext 1")
            it.onNext("onNext 2")
            it.onNext("onNext 3")
            it.onComplete()
//            it.onError(Throwable("Test Throwable"))
        }).subscribe({
            System.out.println(it)
        }, {
            System.out.println(it.message)
        }, {
            System.out.println("on Complete")
        })


        // if do got an onError call , the method will not be call after onError
        Observable.create(ObservableOnSubscribe<String> {
            it.onNext("onNext 1")
            it.onNext("onNext 2")

            it.onError(Throwable("Test Throwable"))

            it.onNext("onNext 3")
            it.onComplete()
        }).doOnError {
            println("1")
        }.doOnError {
            println("2")
        }.doOnError {
            println("3")
        }.subscribe()

//        }).subscribe({
//            System.out.println(it)
//        }, {
//            System.out.println(it.message)
//        }, {
//            System.out.println("on Complete")
//        })


    }

    @Test
    fun range() {
        Observable.range(0, 10).subscribe {
            println("number is: $it")
        }

        println("Next")

        Observable.range(1, 20).subscribe {
            println("number is: $it")
        }


        println("Next")
        Observable.range(-10, 10).subscribe {
            println("number is: $it")
        }
    }

    @Test
    fun just() {

        val content = "The time is ${System.currentTimeMillis()}"
        val justObservable = Observable.just(content)

        //The time should be same.

        justObservable.subscribe {
            println("time is: $it")
        }

        Thread.sleep(1000)

        justObservable.subscribe {
            println("time is: $it")
        }

        Thread.sleep(1000)


        justObservable.subscribe {
            println("time is: $it")
        }


        Observable.just("This is test").subscribe { string ->
            println(string)
        }

    }


    @Test
    fun defer() {
        //The observable will be created when observable has been subscribe.
        //So that observable data will always be newest.

        val deferObservable = Observable.defer {
            val content = "The time is ${System.currentTimeMillis()}"
            Observable.just(content)
        }

        deferObservable.subscribe {
            println("time is: $it")
        }

        Thread.sleep(1000)

        deferObservable.subscribe {
            println("time is: $it")
        }

        Thread.sleep(1000)


        deferObservable.subscribe {
            println("time is: $it")
        }

    }

    @Test
    fun interval() {

        val intervalObservable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("time is: $it")
                }


        Thread.sleep(10100)

        // This will count down forever, so it need to dispose.
        intervalObservable.dispose()

    }


    @Test
    fun repeat() {


        val repeatObservable = Observable.just("Test").repeat(5)

        repeatObservable.subscribe {
            println(it)
        }


        repeatObservable.subscribe {
            println(it)
        }


        //it will print ten time.

    }

    @Test
    fun timer() {


        //Send to subscriber after 100 milliseconds

        val timeObservable = Observable.timer(100, TimeUnit.MILLISECONDS)
        timeObservable.subscribe {
            println("The second ${System.currentTimeMillis()}")
        }

        Thread.sleep(110)

        timeObservable.subscribe {
            println("The second ${System.currentTimeMillis()}")
        }

        Thread.sleep(510)


    }


}
