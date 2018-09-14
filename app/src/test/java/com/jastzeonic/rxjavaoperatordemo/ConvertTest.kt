package com.jastzeonic.rxjavapresentation

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.junit.Test
import java.util.*

class ConvertTest {


    @Test
    fun blockingIterable() {

  val observable = Observable.create(ObservableOnSubscribe<Int> {
      for (number in 1..20) {
          it.onNext(Random().nextInt(number))
          Thread.sleep(100)
      }
      it.onComplete()
  })

  observable.toSortedList()
          .subscribe({ list ->
              for (number in list) {
                  println(number)
              }
          }, {})

        val iterator = observable.blockingIterable()

//        for (number in iterator) {
//            println(number)
//        }

        observable.toSortedList()
                .subscribe({ list ->
                    for (number in list) {
                        println(number)
                    }
                }, {})

    }


}