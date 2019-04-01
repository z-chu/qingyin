package live.qingyin.talk.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.zchu.base.CommonAdapter
import com.github.zchu.base.CommonViewHolder
import live.qingyin.talk.base.BaseFragment
import live.qingyin.talk.widget.SettingRow

abstract class RegionSelectionFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RecyclerView(inflater.context)
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        onViewCreated(recyclerView, savedInstanceState)
    }

    abstract fun onViewCreated(view: RecyclerView, savedInstanceState: Bundle?)


    class ProvincesFragment : RegionSelectionFragment() {
        override fun onViewCreated(view: RecyclerView, savedInstanceState: Bundle?) {
            val arguments = arguments!!
            val provinces: ArrayList<Province> = arguments.getParcelableArrayList("provinces")!!
            val string = arguments.getString("current_province")
            view.adapter = object : CommonAdapter<Province>(provinces) {
                override fun convert(helper: CommonViewHolder, item: Province) {
                    val settingRow = helper.itemView as SettingRow
                    settingRow.tag = item
                    settingRow.setTitle(item.name)
                    if (string == item.name) {
                        settingRow.setSubtitle("当前选择")
                    } else {
                        settingRow.setSubtitle("")
                    }
                }

                override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
                    val settingRow = SettingRow(parent.context)
                    settingRow.setDividerVisibility(true)
                    settingRow.setOnClickListener {
                        val tag = it.tag
                        val activity = activity
                        if (tag is Province && activity is OnProvinceSelectedListener) {
                            activity.onProvinceSelected(tag)
                        }
                    }
                    return CommonViewHolder(settingRow)
                }

            }
        }
    }

    class CitiesFragment : RegionSelectionFragment() {
        override fun onViewCreated(view: RecyclerView, savedInstanceState: Bundle?) {
            val arguments = arguments!!
            val cities: ArrayList<City> = arguments.getParcelableArrayList("cities")!!
            val string = arguments.getString("current_city")

            view.adapter = object : CommonAdapter<City>(cities) {
                override fun convert(helper: CommonViewHolder, item: City) {
                    val settingRow = helper.itemView as SettingRow
                    settingRow.tag = item
                    settingRow.setTitle(item.name)
                    if (string == item.name) {
                        settingRow.setSubtitle("当前选择")
                    } else {
                        settingRow.setSubtitle("")
                    }
                }

                override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
                    val settingRow = SettingRow(parent.context)
                    settingRow.setDividerVisibility(true)
                    settingRow.setOnClickListener {
                        val tag = it.tag
                        val activity = activity
                        if (tag is City && activity is OnCitySelectedListener) {
                            activity.onCitySelected(tag)
                        }
                    }
                    return CommonViewHolder(settingRow)
                }

            }
        }


    }

    class AreasFragment : RegionSelectionFragment() {
        override fun onViewCreated(view: RecyclerView, savedInstanceState: Bundle?) {
            val arguments = arguments!!
            val areas: ArrayList<String> = arguments.getStringArrayList("areas")!!
            val string = arguments.getString("current_area")

            view.adapter = object : CommonAdapter<String>(areas) {
                override fun convert(helper: CommonViewHolder, item: String) {
                    val settingRow = helper.itemView as SettingRow
                    settingRow.tag = item
                    settingRow.setTitle(item)
                    if (string == item) {
                        settingRow.setSubtitle("当前选择")
                    } else {
                        settingRow.setSubtitle("")
                    }
                }

                override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
                    val settingRow = SettingRow(parent.context)
                    settingRow.setDividerVisibility(true)
                    settingRow.setArrowVisibility(false)
                    settingRow.setOnClickListener {
                        val tag = it.tag
                        val activity = activity
                        if (tag is String && activity is OnAreaSelectedListener) {
                            activity.onAreaSelected(tag)
                        }
                    }
                    return CommonViewHolder(settingRow)
                }

            }
        }

    }

    interface OnProvinceSelectedListener {
        fun onProvinceSelected(province: Province)
    }

    interface OnCitySelectedListener {
        fun onCitySelected(city: City)
    }

    interface OnAreaSelectedListener {
        fun onAreaSelected(area: String)
    }

    companion object {


        fun newProvinces(provinces: ArrayList<Province>, current: String?): RegionSelectionFragment {
            val args = Bundle()
            args.putParcelableArrayList("provinces", provinces)
            args.putString("current_province", current)
            val fragment = ProvincesFragment()
            fragment.arguments = args
            return fragment
        }

        fun newCities(cities: ArrayList<City>, current: String?): RegionSelectionFragment {
            val args = Bundle()
            args.putParcelableArrayList("cities", cities)
            args.putString("current_city", current)
            val fragment = CitiesFragment()
            fragment.arguments = args
            return fragment
        }

        fun newAreas(areas: ArrayList<String>, current: String?): RegionSelectionFragment {
            val args = Bundle()
            args.putStringArrayList("areas", areas)
            args.putString("current_area", current)
            val fragment = AreasFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

