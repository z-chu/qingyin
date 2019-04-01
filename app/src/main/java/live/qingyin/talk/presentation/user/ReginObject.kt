package live.qingyin.talk.presentation.user

import android.os.Parcel
import android.os.Parcelable

data class Province(
    val city: ArrayList<City>,
    val name: String
) : Parcelable {
    constructor(source: Parcel) : this(
        ArrayList<City>().apply { source.readList(this, City::class.java.classLoader) },
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeList(city)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Province> = object : Parcelable.Creator<Province> {
            override fun createFromParcel(source: Parcel): Province = Province(source)
            override fun newArray(size: Int): Array<Province?> = arrayOfNulls(size)
        }
    }
}

data class City(
    val area: ArrayList<String>,
    val name: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.createStringArrayList()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeStringList(area)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
            override fun createFromParcel(source: Parcel): City = City(source)
            override fun newArray(size: Int): Array<City?> = arrayOfNulls(size)
        }
    }
}