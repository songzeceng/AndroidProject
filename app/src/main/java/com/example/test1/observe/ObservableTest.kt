package com.example.test1.observe

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

object ObservableTest {
    @JvmStatic
    fun testConcat(list: List<String> = listOf()) {
        if (list.isEmpty()) {
            return
        }

        val disposable = Observable.concat(list.map { it ->
            Observable.create<String> { emitter ->
                process(it, {
                    emitter.onNext(it)
                    emitter.onComplete()
                }, {
                    s, error ->
                    if (error) {
                        emitter.onError(ObservableException())
                    }
                    emitter.onComplete()
                })

            }
        }).observeOn(AndroidSchedulers.mainThread())
    }

    @JvmStatic
    private fun process(s: String, success: (s: String) -> Unit, fail: (s: String, error: Boolean) -> Unit) {
        if (s.startsWith("_")) {
            success(s)
        } else {
            fail(s, s.startsWith("="))
        }
    }

    @JvmStatic
    public fun testObserve(liveData: MutableLiveData<*>, lifecycleOwner: LifecycleOwner) {
        kotlin.runCatching {
            (liveData as? MutableLiveData<Int>)?.observe(lifecycleOwner, MyIntegerObserver())
        }.onFailure {
            it.printStackTrace()
        }
    }
}

class MyIntegerObserver: Observer<Int> {
    override fun onChanged(t: Int?) {
        t ?: return
        println("MyIntegerObserver#onChanged() with value $t")
    }

}

class ObservableException: Throwable() {

}