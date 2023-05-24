package com.xlg.commonlibs.utils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
class RefreshController(
    private val refreshLayout: SmartRefreshLayout,
    private val isEnableRefresh: Boolean,
    private val mCallback: (page: Int) -> Unit
) {
    private var mPage = 1

    init {
        refreshLayout.setEnableRefresh(isEnableRefresh)
        refreshLayout.setOnRefreshListener {
            mPage = 1
            mCallback.invoke(mPage)
        }
        refreshLayout.setOnLoadMoreListener {
            mPage += 1
            mCallback.invoke(mPage)
        }
    }

    fun refresh(): RefreshController {
        if (isEnableRefresh) {
            refreshLayout.autoRefresh()
        } else {
            mCallback.invoke(mPage)
        }
        return this
    }

    fun setEnableLoadMore(flag: Boolean) {
        refreshLayout.setEnableLoadMore(flag)
    }
}