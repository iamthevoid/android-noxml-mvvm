package iam.thevoid.recycler

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Flowable
import thevoid.iam.components.R
import thevoid.iam.components.mvvm.adapter.ItemBindings
import thevoid.iam.components.rx.fields.RxField
import thevoid.iam.components.rx.fields.RxList
import thevoid.iam.components.widget.ext.*

fun <T : Any> RecyclerView.setItems(
    items: RxList<T>,
    itemBindings: ItemBindings,
    diffCallbackFactory: ((old: List<T>, new: List<T>) -> DiffCallback<T>)? = null
) = addSetter(items.observe()) { setItems(it, itemBindings, diffCallbackFactory) }

fun <T : Any> RecyclerView.setItems(
    itemsFlowable: Flowable<List<T>>,
    itemBindings: ItemBindings,
    diffCallbackFactory: ((old: List<T>, new: List<T>) -> DiffCallback<T>)? = null
) = addSetter(itemsFlowable) { setItems(it, itemBindings, diffCallbackFactory) }

private fun <T : Any> RecyclerView.setItems(
    items: List<T>,
    itemBindings: ItemBindings,
    diffCallbackFactory: ((old: List<T>, new: List<T>) -> DiffCallback<T>)? = null
) {
    (adapter as? RxRecyclerAdapter<T>)?.apply {
        data = items.toMutableList()
    } ?: run {
        RxRecyclerAdapter(items).apply {
            this@apply.bindings = itemBindings
            this@apply.diffCallbackFactory = diffCallbackFactory
            adapter = this
        }
    }
}


private val RecyclerView.onRecyclerScroll
    get() = (getTag(R.id.recyclerScroll) as? OnRecyclerScrollDelegate) ?: OnRecyclerScrollDelegate()
        .also {
            setTag(R.id.recyclerScroll, it)
            addOnScrollListener(it)
        }


fun RecyclerView.onRecyclerScrolled(rxOnScroll: RxField<OnScrolled>) = onRecyclerScrolled(rxOnScroll) { it }

fun <T : Any> RecyclerView.onRecyclerScrolled(rxOnScroll: RxField<T>, mapper: (OnScrolled) -> T) =
    addGetter({
        onRecyclerScroll.addOnScrolled(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                it.invoke(mapper(OnScrolled(recyclerView, dx, dy)))
            }
        })
    }, rxOnScroll)



fun RecyclerView.onScrollStateChanged(rxOnScroll: RxField<OnScrollStateChanged>) = onRecyclerScrollStateChanged(rxOnScroll) { it }

fun <T : Any> RecyclerView.onRecyclerScrollStateChanged(rxOnScroll: RxField<T>, mapper: (OnScrollStateChanged) -> T) =
    addGetter({
        onRecyclerScroll.addOnScrolled(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                it.invoke(mapper(OnScrollStateChanged(recyclerView, newState)))
            }
        })
    }, rxOnScroll)


class OnScrolled(val recyclerView: RecyclerView, val dx: Int, val dy: Int)
class OnScrollStateChanged(val recyclerView: RecyclerView, val state: Int)