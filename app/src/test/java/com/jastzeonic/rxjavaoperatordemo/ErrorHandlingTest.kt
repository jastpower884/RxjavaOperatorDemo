package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import org.junit.Test
import java.util.concurrent.TimeUnit

class ErrorHandlingTest {


    private fun createObserver(): Observable<String> {


        return Observable.create {
            for (i in 1..6) {
                if (i < 3) {
                    it.onNext("next is : $i")
                } else if (i < 4) {
                    it.onError(Exception("Throw error"))
                } else {
                    it.onError(Throwable("Throw error"))
                }
            }

        }

    }

    private fun createObserver2(): Observable<String> {


        return Observable.create {
            for (i in 1..6) {
                if (i == 3) {
                    it.onError(Exception("error"))
                } else {
                    it.onNext("next is : $i")
                }
            }

        }

    }


    @Test
    fun catch() {

        val someProcess = Observable.create<String> {
            for (i in 1..10) {
                if (i in 3..5) {
                    it.onError(Exception("Throw error"))
                } else {
                    it.onNext("next is : $i")
                }
            }

        }

        someProcess.onErrorReturn { "do something else" }
                .subscribe {
                    println(it)
                }
//
//        // it will return error in final
//        createObserver().onErrorReturn { "onErrorReturn" }
//                .subscribe({
//                    println(it)
//                }, { println(it.message) }, { println("complete") })
//
//
//        createObserver().onErrorResumeNext(Observable.just("7", "8", "9"))
//                .subscribe({
//                    println(it)
//                }, { println(it.message) }, { println("complete") })
//
//
//        // throw the Exception or throw a throw has different way.
//        createObserver().onExceptionResumeNext(Observable.just("7", "8", "9"))
//                .subscribe({
//                    println("onExceptionResumeNext:$it")
//                }, { println(it.message) }, { println("complete") })

    }


    @Test
    fun retry() {

        createObserver2().retry(3)
                .subscribe {
                    println("retry:$it")
                }

        createObserver2().retryWhen {
            Observable.timer(1000, TimeUnit.MILLISECONDS)
        }.subscribe({
            println("retry when:$it")
        }, { println(it.message) }, { println("retry when:complete") })

        Thread.sleep(2000)

    }

}