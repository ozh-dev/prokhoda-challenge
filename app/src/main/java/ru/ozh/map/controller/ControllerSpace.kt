package ru.ozh.map.controller

import android.view.ViewGroup
import ru.ozh.map.R
import ru.surfstudio.android.easyadapter.controller.NoDataItemController
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

class ControllerSpace : NoDataItemController<ControllerSpace.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder {
        return Holder(parent)
    }

    class Holder(viewGroup: ViewGroup) : BaseViewHolder(viewGroup, R.layout.item_controller_empty)
}