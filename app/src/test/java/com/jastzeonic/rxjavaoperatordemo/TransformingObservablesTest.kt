package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import org.junit.Test
import java.util.concurrent.TimeUnit

class TransformingObservablesTest {

    @Test
    fun buffer() {


        val bufferObservable = Observable.just("0", "1", "2", "3", "4", "5", "6").buffer(3)

        bufferObservable.subscribe {
            println("The result is $it")
        }
        println("next")

        val bufferSkipObservable = Observable.just("0", "1", "2", "3", "4", "5", "6").buffer(3, 2)

        bufferSkipObservable.subscribe {
            println("The result is $it")
        }


        val bufferIntervalObservable = Observable.interval(1, TimeUnit.SECONDS).buffer(3, 2)

        bufferIntervalObservable.subscribe {
            println("The result is $it")
        }

        Thread.sleep(10000)

    }

    @Test
    fun flatMap() {

        val flatMapObservable = Observable.just("0", "1", "2", "3", "4", "5", "6").flatMap {
            Observable.just("The observable after change: $it")
        }

        flatMapObservable.subscribe {
            println("Here is the result [$it]")
        }


        val result = mutableListOf<String>()
        val flatMapIterableObservable = Observable.just("0", "1", "2", "3", "4", "5", "6").flatMapIterable {
            val result = mutableListOf(it)
            result.add("Test1")
            result.add("Test2")
            result
        }

        flatMapIterableObservable.subscribe {
            println("Here is the result [$it]")
        }

    }

    @Test
    fun test() {
Observable.just(0, 1, 2, 3, 4, 5, 6).flatMap {
    Observable.just("The result is $it")
}.subscribe { result ->
    println(result)
}
    }


    @Test
    fun groupBy() {
        val groupByObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy<Any> { integer ->
                    integer % 2
                    // This will be key.
                }

        groupByObservable.subscribe {
            if (it.key == 0) {
                it.subscribe {
                    println("The result is $it")
                }
            }
        }
    }

    @Test
    fun map() {

        // map is same like flatmap. but it don't need to observable anything.

        val mapObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).map<Any> { integer -> "This is test ${integer * 10}" }
        mapObservable.subscribe {
            println("This result = $it")
        }
    }

    @Test
    fun cast() {

        val animal: Animal = Dog()


        val castObservable = Observable.just(animal, animal, animal).cast(Dog::class.java)

        castObservable.subscribe {
            println("This result = $it")
            println("and the type is  = ${it.javaClass.typeName}")
        }

    }

    internal open inner class Animal
    internal inner class Dog : Animal()

    @Test
    fun scan() {
        val scanObservable = Observable.just("Hello", " World", " This", " is", " Wonderful", " World").scan { t1: String, t2: String -> t1 + t2 }


        scanObservable.subscribe {
            println(it)
        }
    }

    @Test
    fun window() {
        val windowObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).window(3)

        windowObservable.subscribe {
            it.subscribe {
                println("this is just result : $it")
            }
        }

        val windowIntervalObservable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS)

        windowIntervalObservable.subscribe {
            it.subscribe {
                println("this is interval result : $it")
            }

        }

        Thread.sleep(10000)
    }


}