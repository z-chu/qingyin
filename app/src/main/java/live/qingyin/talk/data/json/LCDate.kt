package live.qingyin.talk.data.json

import java.text.SimpleDateFormat
import java.util.*

class LCDate {

    val date: Date
    val iso: String
        get() {
            return bf.format(date)
        }
    val __type: String = "Date"

    constructor(date: Date) {
        this.date = date
    }

    constructor(iso: String) {
        this.date = bf.parse(iso)
    }


    companion object {
        private var bf = SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss", Locale.getDefault())
    }

}