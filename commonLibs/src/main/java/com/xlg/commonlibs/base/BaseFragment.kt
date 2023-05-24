package com.xlg.commonlibs.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutId: Int): Fragment() {
    val contentView = lazy { LayoutInflater.from(requireContext()).inflate(layoutId, null) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView(contentView.value)
        return contentView.value
    }

    abstract fun initViewModel()

    abstract fun initView(view: View)

}