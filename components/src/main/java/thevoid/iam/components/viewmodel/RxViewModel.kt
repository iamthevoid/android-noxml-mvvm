package thevoid.iam.components.viewmodel

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class RxViewModel : LifecycleTrackingViewModel() {

    private val disposable by lazy { CompositeDisposable() }

    private fun unsubscribeOnInactive(disposable: Disposable, vararg disposables: Disposable) {
        this.disposable.add(disposable)
        if (disposables.isNotEmpty())
            this.disposable.addAll(*disposables)
    }

    override fun onActive() {

    }

    @CallSuper
    override fun onInactive() {
        disposable.clear()
    }
}