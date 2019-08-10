package iam.thevoid.ankoviews.widget.mvvm

import android.view.View
import androidx.lifecycle.ViewModel
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import iam.thevoid.common.view.MvvmFragment

abstract class AnkoMvvmFragment<VM: ViewModel> : MvvmFragment<VM>(), AnkoComponent<AnkoMvvmFragment<VM>> {

    override fun provideContentView(): View? = context?.let { createView(AnkoContext.create(it, this, false)) }

}