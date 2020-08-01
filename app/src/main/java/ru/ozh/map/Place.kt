package ru.ozh.map

import com.google.android.gms.maps.model.LatLng

data class Place(
    val name: String,
    val address: String,
    val time: String,
    val isOpen: Boolean,
    val isFavorite: Boolean,
    val placeLatLng: LatLng
)

object PlaceConstants {

    val BOUNDS_TOP_LEFT = LatLng(51.715547, 39.140565)
    val BOUNDS_BOTTOM_RIGHT = LatLng(51.702424, 39.159931)

    val MAP_PLACES = listOf(
        Place(
            name = "Media Markt",
            address = "Sierra Gamon, 134",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = false,
            placeLatLng = LatLng(51.712195, 39.147895)
        ),
        Place(
            name = "Sony",
            address = "Green Street, 666",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = true,
            placeLatLng = LatLng(51.710526, 39.151264)
        ),
        Place(
            name = "Mvideo",
            address = "Tilatry street, 2",
            time = "9:00 - 21:00",
            isOpen = false,
            isFavorite = false,
            placeLatLng = LatLng(51.710087, 39.153024)
        ),
        Place(
            name = "Eldorado",
            address = "Lake street, 54",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = true,
            placeLatLng = LatLng(51.708232, 39.148228)
        ),
        Place(
            name = "Apple store",
            address = "Hole avenue, 44",
            time = "9:00 - 21:00",
            isOpen = false,
            isFavorite = true,
            placeLatLng = LatLng(51.707584, 39.152141)
        ),
        Place(
            name = "Sviasnoy",
            address = "Grand Street, 12",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = false,
            placeLatLng = LatLng(51.706530, 39.149539)
        ),
        Place(
            name = "MTS",
            address = "Wall street, 4",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = true,
            placeLatLng = LatLng(51.704991, 39.148514)
        ),
        Place(
            name = "Beeline",
            address = "Park Avenue, 9",
            time = "9:00 - 21:00",
            isOpen = false,
            isFavorite = true,
            placeLatLng = LatLng(51.705752, 39.146068)
        ),
        Place(
            name = "Fresh Bread",
            address = "West End Avenue, 55",
            time = "9:00 - 21:00",
            isOpen = false,
            isFavorite = false,
            placeLatLng = LatLng(51.708630, 39.150289)
        ),
        Place(
            name = "Meat and Vegetables",
            address = "Madison Avenue, 47",
            time = "9:00 - 21:00",
            isOpen = true,
            isFavorite = true,
            placeLatLng = LatLng(51.708012, 39.150514)
        )
    )
}