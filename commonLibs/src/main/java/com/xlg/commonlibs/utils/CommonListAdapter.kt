package com.xlg.commonlibs.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xlg.commonlibs.base.BaseListData
import com.xlg.commonlibs.base.BaseViewHolder
import com.xlg.commonlibs.ext.getHolder

@Suppress("UNCHECKED_CAST")
class CommonListAdapter<T : BaseListData>(private val itemClick: ((view: View, data: BaseListData, position: Int) -> Unit)? = null) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private val mDataList = ArrayList<BaseListData>()
    private var mEmptyListener: ((isEmpty: Boolean) -> Unit)? = null

    fun putData(dataList: ArrayList<T>) {
        if (mDataList.size > 0) {
            val size = mDataList.size
            mDataList.clear()
            notifyItemRangeRemoved(0, size)
        }
        if (dataList.isNotEmpty()) {
            mDataList.addAll(dataList)
            notifyItemRangeInserted(0, mDataList.size)
        }
        mEmptyListener?.invoke(dataList.isEmpty())
    }

    fun <S : BaseListData> addSingleData(data: S, position: Int) {
        mDataList.add(position, data)
        notifyItemInserted(position)
    }

    fun <S : BaseListData> updateData(data: S, position: Int) {
        if (mDataList[position].javaClass == data.javaClass) {
            mDataList.removeAt(position)
            mDataList.add(position, data)
            notifyItemChanged(position)
        }
    }

    fun <S : BaseListData> addDataListToEnd(dataList: ArrayList<S>) {
        mDataList.addAll(dataList)
        notifyItemRangeInserted(mDataList.size, dataList.size)
    }

    fun delSingleData(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun delDataRange(startIndex: Int, size: Int) {
        for (i in 0 until size) {
            mDataList.removeAt(startIndex)
        }
        notifyItemRangeRemoved(startIndex, size)
    }

    fun <S : BaseListData> getDataList(): ArrayList<S> {
        return mDataList as ArrayList<S>
    }

    override fun getItemViewType(position: Int): Int {
        return mDataList[position].layoutType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_DEFAULT -> {
                DefaultViewHolder(parent, 0)
            }
            else -> {
                getHolder(parent, viewType)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(mDataList[position], position, itemClick)
    }


    class DefaultViewHolder(parent: ViewGroup, id: Int) : BaseViewHolder(parent, id) {
        override fun onBind(
            data: BaseListData,
            position: Int,
            onItemClicked: ((view: View, data: BaseListData, position: Int) -> Unit)?
        ) {
            //TODO 无
        }

    }


    fun addEmptyListener(emptyListener: (isEmpty: Boolean) -> Unit) {
        mEmptyListener = emptyListener
    }
}