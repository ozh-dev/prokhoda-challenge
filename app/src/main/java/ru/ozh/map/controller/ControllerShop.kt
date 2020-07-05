package ru.ozh.map.controller

import android.view.ViewGroup
import ru.ozh.map.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class ControllerShop : BindableItemController<Int, ControllerShop.Holder>() {
    override fun getItemId(data: Int): String {
        return data.toString()
    }

    override fun createViewHolder(parent: ViewGroup): Holder {
        return Holder(parent)
    }

    class Holder(viewGroup: ViewGroup) : BindableViewHolder<Int>(viewGroup, R.layout.item_controller_shop) {
        override fun bind(data: Int) {

        }

    }
}