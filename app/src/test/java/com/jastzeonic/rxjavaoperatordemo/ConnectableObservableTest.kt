package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.util.*

class ConnectableObservableTest {

    @Test
    fun connect() {


        val observable = Observable.create(ObservableOnSubscribe<Int> {
            for (number in 1..20) {
                it.onNext(number)
                Thread.sleep(100)
            }
            it.onComplete()
        }).subscribeOn(Schedulers.newThread()).publish()

        observable.connect()

        Thread.sleep(100)

        observable.subscribe {
            println(it)
        }



        Thread.sleep(2000)


    }

}