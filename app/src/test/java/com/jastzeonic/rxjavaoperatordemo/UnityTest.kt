package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class UnityTest {

    private fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
    }

    private fun createObserver(): Observable<String> {

        return Observable.create {
            for (i in 1..6) {
                if (i == 3) {
                    Thread.sleep(1000)
                }
                it.onNext("Time is : ${getCurrentTime()}")
            }

        }

    }


    @Test
    fun delay() {
        println("start with : ${getCurrentTime()}")
        createObserver().delay(2000, TimeUnit.MILLISECONDS)
                .subscribe {
                    println(it)
                }

        val observable = createObserver().delaySubscription(2000, TimeUnit.MILLISECONDS)

        println("start with : ${getCurrentTime()}")
        Thread.sleep(1000)

        observable.subscribe {
            println(it)
        }
        Thread.sleep(1000)


    }

    @Test
    fun doSomething() {
        createObserver()
                .doOnEach {

                }
                .doOnNext {

                }
                .doOnSubscribe {

                }
                .doOnDispose {

                }
                .doOnError {

                }
                .doOnComplete {

                }
                .doOnTerminate {

                }
                .doFinally {

                }
    }

    @Test
    fun materialize() {// 物質化 G-.- 三小阿？

        createObserver().materialize()
                .subscribe {
                    println(it.isOnComplete)
                    println(it.value)
                }

        createObserver().materialize().dematerialize<String>()
                .subscribe {
                    println(it)
                }

    }


    @Test
    fun subscribeOnAndObserverOn() {
        createObserver().subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())

        //bla bla
    }

    @Test
    fun time() {
        createObserver().timeInterval()
                .subscribe {
                    println(it.value())
                    println(it.time())
                }

        createObserver().timestamp()
                .subscribe {
                    println(it.value())
                    println(it.time())
                }
    }

    @Test
    fun timeout() {
        createObserver().timeout(200, TimeUnit.MILLISECONDS)
                .subscribe({
                    println(it)
                }, {
                    println("Throwable:${it.message}")
                })
    }

    @Test
    fun using() {
        Observable.using({
            // ObservableFactory
            Animal()
        }, {
            // resourceFactory
            Observable.timer(5000, TimeUnit.MILLISECONDS)
        }, {
            // DisposeFunction
            it.release()
        }).subscribe({
            println("onNext")

        }, {
            println("onError")
        }, {
            println("onComplete")})
        Thread.sleep(3000)

    }


    class Animal {
        private var observer: Observer<Long> = object : Observer<Long> {
            override fun onNext(t: Long) {
                println("onNext : $t")
            }

            override fun onSubscribe(d: Disposable) {
            }


            override fun onComplete() {
            }


            override fun onError(e: Throwable) {

            }

        }

        init {
            println("create animal");
            Observable.interval(500, TimeUnit.MILLISECONDS)
                    .subscribe(observer)
        }

        fun release() {
            println("release animal");

        }


    }

}