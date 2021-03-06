package iam.thevoid.noxml.rx2.data.fields

import iam.thevoid.e.safe
import iam.thevoid.rxe.canPublish
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
open class RxItem<T>(initial: T, private val onChange: (T) -> Unit = {}) {

    private val subject = BehaviorProcessor.createDefault(initial)

    fun set(elem: T) {
        if (subject.canPublish())
            subject.onNext(elem)
        onChange(elem)
    }

    fun get(): T = subject.value ?: throw IllegalStateException("Value not provided")

    fun observe(): Flowable<T> = subject

    fun <E> map(mapper: (T) -> E): Flowable<E> = observe().map { mapper(it) }

    fun <E> mapSelf(mapper: T.() -> E): Flowable<E> = observe().map { it.mapper() }
}

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
open class RxCharSequence<T : CharSequence>(initial: T = "" as T, onChange: (T) -> Unit = {}) :
    RxItem<T>(initial, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxBoolean(initial: Boolean = false, onChange: (Boolean) -> Unit = {}) :
    RxItem<Boolean>(initial, onChange) {

    fun not() = observe().map { !it }
}

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxFloat(initial: Float = 0f, onChange: (Float) -> Unit = {}) :
    RxItem<Float>(initial, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxDouble(initial: Double = .0, onChange: (Double) -> Unit = {}) :
    RxItem<Double>(initial, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxInt(initial: Int = 0, onChange: (Int) -> Unit = {}) : RxItem<Int>(initial, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxLong(initial: Long = 0L, onChange: (Long) -> Unit = {}) : RxItem<Long>(initial, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxString(string: String = "", onChange: (String) -> Unit = {}) :
    RxCharSequence<String>(string, onChange)

@Deprecated("Items will be removed in release version, use realization with FlowableProcessor instead")
class RxList<T>(initial: List<T> = emptyList(), onChange: (List<T>) -> Unit = {}) :
    RxItem<List<T>>(initial, onChange) {

    fun add(items: List<T>) = set(get() + items)

    fun replace(predicate: (T) -> Boolean, item: T) {
        val list = get().toMutableList()
        val found = list.find(predicate)
        val indexOfFound = list.indexOf(found)
        if (indexOfFound == -1 || found == null) return
        list[indexOfFound] = item
        set(list)
    }

    fun update(predicate: (T) -> Boolean, change: (T) -> Unit) {
        val list = get().toMutableList()
        val found = list.find(predicate)
        val indexOfFound = list.indexOf(found)
        if (indexOfFound == -1 || found == null) return
        list[indexOfFound] = found.also {
            change(it)
        }
        set(list)
    }

    fun remove(predicate: (T) -> Boolean) {
        val list = ArrayList(get().safe())
        val found = list.find(predicate)
        val indexOfFound = list.indexOf(found)
        if (indexOfFound == -1 || found == null) return
        list.removeAt(indexOfFound)
        set(list)
    }

    fun isEmpty() = get().isEmpty()
}


