package thevoid.iam.rx.widget.ext

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import iam.thevoid.rxe.combine
import io.reactivex.Flowable
import thevoid.iam.rx.rxdata.RxLoading
import thevoid.iam.rx.rxdata.fields.RxBoolean


fun SwipeRefreshLayout.setRefreshing(loading : RxLoading) =
        addSetter(loading.observe()) { isRefreshing = it }

fun SwipeRefreshLayout.setRefreshing(loading1: RxLoading, loading2: RxLoading) =
        addSetter(combine(loading1.observe(), loading2.observe())) { (f, s) ->
                isRefreshing = f || s
        }

fun SwipeRefreshLayout.setRefreshing(loading : RxBoolean) =
        addSetter(loading.observe()) { isRefreshing = it }

fun SwipeRefreshLayout.setRefreshing(loading : Flowable<Boolean>) =
        addSetter(loading) { isRefreshing = it }