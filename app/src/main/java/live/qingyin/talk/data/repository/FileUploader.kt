package live.qingyin.talk.data.repository

import com.avos.avoscloud.AVFile
import io.reactivex.Observable
import java.io.File

class FileUploader {

    fun uploadImage(name: String, file: File): Observable<String> {
        return Observable.fromCallable {
            val avFile = AVFile.withFile(name, file)
            avFile.save()
            avFile.url
        }
    }

}