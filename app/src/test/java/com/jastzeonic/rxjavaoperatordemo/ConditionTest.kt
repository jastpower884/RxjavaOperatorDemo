package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import org.junit.Test
import android.R.attr.delay
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class ConditionTest {


    @Test
    fun all() {
        Observable.just(1, 2, 3, 4, 5, 6).all {
            it < 6
        }.subscribe({
            println("is small than 6: $it")
        }, {

        })
    }

    @Test
    fun amb() {
        val delay3 = Observable.just(1, 2, 3).delay(3000, TimeUnit.MILLISECONDS)
        val delay2 = Observable.just(4, 5, 6).delay(2000, TimeUnit.MILLISECONDS)
        val delay1 = Observable.just(7, 8, 9).delay(1000, TimeUnit.MILLISECONDS)

        Observable.amb(mutableListOf(delay1, delay2, delay3))
                .subscribe {
                    println("the result is: $it")
                }

        Thread.sleep(5000)
    }

    @Test
    fun contain() {

        Observable.just(1, 2, 3).contains(3)
                .subscribe({
                    println("The result is : $it")
                }, {})

    }

    @Test
    fun isEmpty() {
        Observable.create<Int> {
            it.onComplete()
        }.isEmpty.subscribe({
            println("The result is : $it")
        }, {})
    }

    @Test
    fun defaultEmpty() {
        Observable.create<Int> {
            it.onComplete()
        }.defaultIfEmpty(10).subscribe({
            println("The result is : $it")
        }, {})
    }


    @Test
    fun sequenceEqual() {
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3))
                .subscribe({
                    println("The result is : $it")
                }, {})

        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2))
                .subscribe({
                    println("The result is : $it")
                }, {})
    }


    @Test
    fun skipUntil() {
        Observable.interval(1, TimeUnit.SECONDS).skipUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe {
                    println("The result is : $it")
                }
        Thread.sleep(7000)
    }


    @Test
    fun skipWhile() {
        Observable.interval(1, TimeUnit.SECONDS).skipWhile { it < 5 }
                .subscribe {
                    println("The result is : $it")
                }

        Thread.sleep(7000)
    }

    @Test
    fun takeUntil() {
        Observable.interval(1, TimeUnit.SECONDS).takeUntil(Observable.timer(5, TimeUnit.SECONDS))
                .subscribe {
                    println("The result is : $it")
                }

        Thread.sleep(7000)
    }

    @Test
    fun takeWhile() {
        Observable.interval(1, TimeUnit.SECONDS).takeWhile {
            it < 5
        }.subscribe {
            println("The result is : $it")
        }

        Thread.sleep(7000)
    }


}