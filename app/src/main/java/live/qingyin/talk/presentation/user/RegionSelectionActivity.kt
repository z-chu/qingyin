package live.qingyin.talk.presentation.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.zchu.common.help.initToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import live.qingyin.talk.R
import live.qingyin.talk.base.BaseActivity
import java.io.InputStreamReader

class RegionSelectionActivity : BaseActivity()
    , RegionSelectionFragment.OnProvinceSelectedListener
    , RegionSelectionFragment.OnCitySelectedListener
    , RegionSelectionFragment.OnAreaSelectedListener {

    private lateinit var provincesFragment: RegionSelectionFragment

    private var provinceName: String? = null
    private var cityName: String? = null
    private var areaName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regin)
        initToolbar("选择地区")

        val region = intent.getStringExtra("region")
        if (region != null) {
            val split = region.split(' ')
            if (split.size == 2) {
                provinceName = split[0]
                cityName = split[0]
                areaName = split[1]
            } else if (split.size == 3) {
                provinceName = split[0]
                cityName = split[1]
                areaName = split[2]
            }
        }

        val inputStream = assets.open("region.json")
        val streamReader = InputStreamReader(inputStream, "UTF-8")
        val gson = Gson()
        val adapter = gson.getAdapter(object : TypeToken<ArrayList<Province>>() {})
        val provinceList = adapter.fromJson(streamReader)
        provincesFragment = RegionSelectionFragment.newProvinces(provinceList, provinceName)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, provincesFragment)
                .commit()
            supportFragmentManager
        }

    }

    override fun onProvinceSelected(province: Province) {
        val city = province.city
        provinceName = province.name
        if (city.size > 1) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_right_in,
                    R.anim.slide_left_out,
                    R.anim.slide_left_in,
                    R.anim.slide_right_out
                )
                .replace(R.id.container, RegionSelectionFragment.newCities(province.city, cityName))
                .addToBackStack(null)
                .commit()
        } else {
            onCitySelected(city[0])
        }
    }

    override fun onCitySelected(city: City) {
        cityName = city.name
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_right_in,
                R.anim.slide_left_out,
                R.anim.slide_left_in,
                R.anim.slide_right_out
            )
            .replace(R.id.container, RegionSelectionFragment.newAreas(city.area, areaName))
            .addToBackStack(null)
            .commit()
    }

    override fun onAreaSelected(area: String) {
        areaName = area
        val region = if (provinceName != cityName) {
            "$provinceName $cityName $area"
        } else {
            "$cityName $area"
        }
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra("region", region)
        )
        finish()
    }

    override fun onBackPressed() {
        val supportFragmentManager = this.supportFragmentManager
        if (supportFragmentManager.backStackEntryCount <= 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    companion object {

        fun newIntent(context: Context, region: String?): Intent {
            val intent = Intent(context, RegionSelectionActivity::class.java)
            intent.putExtra("region", region)
            return intent
        }

        fun getRegion(intent: Intent?): String? {
            return intent?.getStringExtra("region")
        }
    }
}