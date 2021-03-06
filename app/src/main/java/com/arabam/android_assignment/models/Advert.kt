package com.arabam.android_assignment.models


import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.collections.ArrayList

@Entity
data class Advert(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var advertID: Int?,
    var title: String?,
    @Embedded
    var location: Location?,
    @Embedded
    var category: Category?,
    var modelName: String?,
    var price: Int?,
    var priceFormatted: String?,
    var date: String?,
    var dateFormatted: String?,
    var photo: String?,
    var properties: ArrayList<Property>?,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int? = 0

    fun photoUrl(): String? {
        return this.photo?.replace("{0}", "800x600").toString()
    }

    fun getKM(): String {
        return this.getProperty("km")
    }

    fun getYear(): String {
        return this.getProperty("year")
    }


    private fun getProperty(propertyName: String?): String {
        var value = "-"
        if (this.properties != null)
            for (item in this.properties!!) {
                if (item.name == propertyName)
                    value = item.value.toString()
            }

        return value
    }


}


