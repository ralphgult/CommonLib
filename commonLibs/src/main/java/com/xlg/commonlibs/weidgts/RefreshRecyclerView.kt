package com.xlg.commonlibs.weidgts

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xlg.commonlibs.ext.bindController
import com.xlg.commonlibs.ext.getController
import com.xlg.commonlibs.ext.setCanLoadMore
import com.xlg.commonlibs.ext.setGone
import com.xlg.commonlibs.ext.showOrGone
import com.xlg.commonlibs.ext.unBindController
import com.xlg.commonlibs.utils.CommonListAdapter

open class RefreshRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val mRefreshLayout = SmartRefreshLayout(context)
    val mRecycler = RecyclerView(context)
    private val mEmptyContainer = FrameLayout(context)

    private lateinit var mAdapter: CommonListAdapter<*>

    fun init(
        enableRefresh: Boolean,
        adapter: CommonListAdapter<*>,
        request: (page: Int) -> Unit
    ) {
        mAdapter = adapter
        val paramsRefresh = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mRefreshLayout.setRefreshHeader(MaterialHeader(context))
        mRefreshLayout.setRefreshFooter(ClassicsFooter(context))
        mRefreshLayout.bindController(enableRefresh, request)
        mRefreshLayout.layoutParams = paramsRefresh
        val recyclerPar = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mRecycler.layoutParams = recyclerPar
        mRecycler.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        onRecyclerViewCreated()
        mRecycler.adapter = mAdapter
        mRefreshLayout.addView(mRecycler)
        addView(mRefreshLayout)
    }

    fun setBgColor(color: Int) {
        mRefreshLayout.setBackgroundColor(color)
    }

    open fun setLoadMore(isLoadMore: Boolean) {
        mRefreshLayout.setCanLoadMore(isLoadMore)
    }

    fun finishLoad(){
        mRefreshLayout.finishRefresh()
        mRefreshLayout.finishLoadMore()
    }

    fun initEmptyView(empty: View, onEmptyShow: (emptyView: View) -> Unit) {
        val emptyPar = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mEmptyContainer.layoutParams = emptyPar
        mEmptyContainer.setGone()
        mEmptyContainer.setBackgroundColor(
            ContextCompat.getColor(
                context,
                android.R.color.transparent
            )
        )
        mRefreshLayout.addView(mEmptyContainer)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        empty.layoutParams = params
        mEmptyContainer.addView(empty)
        mAdapter.addEmptyListener {
            mEmptyContainer.showOrGone(it)
            if (it){
                onEmptyShow.invoke(empty)
            }
        }
    }

    fun refresh() {
        mRefreshLayout.getController()?.refresh()
    }

    fun setLayoutManager(layoutManager: LayoutManager) {
        mRecycler.layoutManager = layoutManager
    }

    fun setDecoration(decoration: ItemDecoration){
        mRecycler.addItemDecoration(decoration)
    }

    open fun onRecyclerViewCreated(){}

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRefreshLayout.unBindController()
    }
}