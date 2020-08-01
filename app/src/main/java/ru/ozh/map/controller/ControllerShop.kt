package ru.ozh.map.controller

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_controller_shop.view.*
import ru.ozh.map.Place
import ru.ozh.map.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class ControllerShop : BindableItemController<Place, ControllerShop.Holder>() {
    override fun getItemId(data: Place): String {
        return data.hashCode().toString()
    }

    override fun createViewHolder(parent: ViewGroup): Holder {
        return Holder(parent)
    }

    class Holder(viewGroup: ViewGroup) : BindableViewHolder<Place>(viewGroup, R.layout.item_controller_shop) {
        override fun bind(place: Place) {
            
            with(itemView) {
                place_name_tv.text = place.name
                place_address_tv.text = place.address
                place_open_time_tv.text = place.time
                place_status_tv.text = if (place.isOpen) "Open" else "Close"
                place_status_tv.isEnabled = place.isOpen
                place_favorite_iv.isEnabled = place.isFavorite
            }

        }

    }
}