package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class CombiningTest {

    private fun createObserver(): Observable<String> {
        return Observable.create {
            Thread.sleep(1000)
            it.onNext("The number is 12")
            it.onComplete()
        }
    }


    private fun createObserver2(): Observable<String> {
        return Observable.create {
            Thread.sleep(3000)
            it.onNext("The number is 10")
            it.onComplete()
        }
    }

    private fun createObserver3(): Observable<String> {
        return Observable.create {
            for (i in 1..5) {
                it.onNext("index:$i")
                Thread.sleep(1000)
            }

            it.onComplete()
        }
    }

    private fun createObserver４(i: Int): Observable<String> {
        return Observable.create {
            it.onNext("index:$i")
            Thread.sleep(1000)
            it.onComplete()
        }
    }

    @Test
    fun combiningLatest() {

        val sdf = SimpleDateFormat("HH:mm:ss")

        println("Time is : ${sdf.format(System.currentTimeMillis())}")


        // The subscribe should be call after 4 second(all observable done)

        Observable.combineLatest(createObserver(), createObserver2(),
                BiFunction { n: String, a: String -> "$n - age:$a" })
                .subscribe {
                    println(it)
                    println("Time is : ${sdf.format(System.currentTimeMillis())}")
                }


        println("Time is : ${SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())}")
        Observable.combineLatest(arrayOf(createObserver(), createObserver2())) {
            val n = it[0] as String
            val a = it[1] as String
            "$n - age:$a"

        }.subscribe {
            println("The result is : $it")
            println("Time is : ${sdf.format(System.currentTimeMillis())}")

        }
    }


    @Test
    fun join() {

        val sdf = SimpleDateFormat("HH:mm:ss")

        println("Time is : ${sdf.format(System.currentTimeMillis())}")



        Observable.just("Test").join(createObserver(), Function<String, ObservableSource<Long>> {

            // 這個 observable source 決定了是否下傳 onNext，否則會直接呼叫 onComplete
            Observable.timer(1000, TimeUnit.MILLISECONDS)

        }, Function<String, ObservableSource<Long>> {
            // 這個 observable source 決定了是否下傳 onNext，否則會直接呼叫 onComplete
            Observable.timer(1000, TimeUnit.MILLISECONDS)
        }, BiFunction { n: String, x: String -> "$n : $x" })
                .doOnNext {
                    println("The result is : $it")
                    println("Time is : ${sdf.format(System.currentTimeMillis())}")
                }.doOnError {
                    println("The result is : $it")
                    println("Time is : ${sdf.format(System.currentTimeMillis())}")

                }.doOnComplete {
                    println("onComplete")
                    println("Time is : ${sdf.format(System.currentTimeMillis())}")
                }
                .subscribe()


        Observable.just("Test").groupJoin(createObserver(), Function<String, ObservableSource<Long>> {

            // 這個 observable source 決定了是否下傳 onNext，否則會直接呼叫 onComplete
            Observable.timer(100, TimeUnit.MILLISECONDS)

        }, Function<String, ObservableSource<Long>> {
            // 這個 observable source 決定了是否下傳 onNext，否則會直接呼叫 onComplete
            Observable.timer(1000, TimeUnit.MILLISECONDS)
        }, BiFunction { n: String, x: ObservableSource<String> -> "$n : $x" })
                .subscribe {
                    println("The result is : $it")
                    println("Time is : ${sdf.format(System.currentTimeMillis())}")
                }
    }


    @Test
    fun merge() {


        Observable.merge(Observable.just(1, 2, 3), Observable.just("Test", "Test", "Test"))
                .subscribe {
                    println("The result is : $it")
                }

        Observable.mergeDelayError(Observable.create<Int> {
            for (i in 1..10) {
                if (i % 2 == 0) {
                    it.onError(Throwable("error"))
                }
                it.onNext(i)
            }
        }, Observable.create<String> {
            for (i in 1..10) {
                it.onNext("String")
            }
        }).subscribe({
            println(it)
        }, {
            println(it.message)

        })


    }


    @Test
    fun startWith() {
        Observable.just(1, 2, 3).startWith(-1)
                .subscribe {
                    println(it)
                }

    }


    @Test
    fun switch() {


        Observable.switchOnNext(Observable.create<Observable<String>> {
            for (i in 1..3) {
                println("Next:$i")
                it.onNext(createObserver3().subscribeOn(Schedulers.newThread()))
                if (i % 2 == 0) {
                    Thread.sleep(3000)
                }
            }
        }).subscribe {
            println(it)
        }

    }

    @Test
    fun zip() {


        val api1 = Observable.create(ObservableOnSubscribe<String> {

            Thread.sleep(1000)
            it.onNext("api 1 response.")
            it.onComplete()

        })

        val api2 = Observable.create(ObservableOnSubscribe<String> {

            Thread.sleep(500)
            it.onNext("api 2 response.")
            it.onComplete()

        })


        Observable.zip<String, String, String>(api1, api2, BiFunction<String, String, String> { t1, t2 ->
            "「$t1」:「$t2」"
        }).subscribe {
            println(it)
        }


        Observable.zip<String, String, String, String>(createObserver４(0), createObserver４(2), createObserver４(4),
                Function3<String, String, String, String> { t1, t2, t3 -> "$t1+$t2+$t3" })
                .subscribe {
                    println(it)
                }
    }

    @Test
    fun zipWith() {
        createObserver４(0).zipWith<String, String>(createObserver４(5), BiFunction<String, String, String> { x: String, y: String ->
            "$x+$y"
        }).subscribe {
            println(it)
        }

    }

}