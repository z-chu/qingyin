package live.qingyin.talk.presentation.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import live.qingyin.talk.presentation.main.MainActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.start(this)
        finish()
    }

}