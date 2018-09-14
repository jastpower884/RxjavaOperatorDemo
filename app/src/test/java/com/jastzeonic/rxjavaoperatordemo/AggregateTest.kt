package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import org.junit.Test


class AggregateTest {

    @Test
    fun concat() {

        val obser1 = Observable.just(1, 2, 3);
        val obser2 = Observable.just(4, 5, 6);
        val obser3 = Observable.just(7, 8, 9);
        Observable.concat(obser1, obser2, obser3)
                .subscribe {
                    println("The result is $it")
                }

    }

    @Test
    fun count() {

        val obser1 = Observable.just(1, 2, 3);
        val obser2 = Observable.just(4, 5, 6);
        val obser3 = Observable.just(7, 8, 9);
        Observable.concat(obser1, obser2, obser3).count().subscribe({
            println("The result is $it")
        }, {})

        val observable = Observable.just(5, 7, 9, 3)
        observable.count().subscribe({
            println("total count : $it")

        }, {})
        Thread.sleep(100)
        observable.subscribe {
            println("number : $it")
        }

    }

    @Test
    fun reduce() {
        val array = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8).reduce { x, y ->
            x + y
        }.subscribe {
            println("The result is $it")
        }


        Observable.just(0, 1, 2, 3, 4, 5, 6, 7, 8)
                .collect({
                    mutableListOf<Int>()
                }, { list, it2 ->
                    list.add(it2)
                }).subscribe({
                    println("The result is $it")
                }, {})
    }

}