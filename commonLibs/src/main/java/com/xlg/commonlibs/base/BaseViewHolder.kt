package com.xlg.commonlibs.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder : ViewHolder {

    constructor(
        context: Context,
        id: Int
    ) : super(
        if (id == 0) TextView(context) else LayoutInflater.from(context).inflate(id, null)
    )

    constructor(
        parent: ViewGroup,
        id: Int
    ) : super(
        if (id == 0) TextView(parent.context) else LayoutInflater.from(parent.context)
            .inflate(id, parent, false)
    )

    /**
     * 数据适配方法（抽象）
     * @param data          item数据对象
     * @param position      item在列表中所处的位置
     * @param onItemClicked 布局中点击事件的触发方法
     */
    abstract fun onBind(
        data: BaseListData,
        position: Int,
        onItemClicked: ((view: View, data: BaseListData, position: Int) -> Unit)? = null
    )
}