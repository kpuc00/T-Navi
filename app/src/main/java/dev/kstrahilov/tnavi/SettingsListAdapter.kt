package dev.kstrahilov.tnavi

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SettingsListAdapter(var context: Context, private var array: Array<String>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return array.count()
    }

    override fun getItem(p0: Int): Any {
        return array[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.settings_item_layout, null)
        val ivIcon: ImageView = view.findViewById(R.id.iv_settings_item_icon)
        val tvTitle: TextView = view.findViewById(R.id.tv_settings_item_title)

        when (val item: String = array[p0]) {
            "stopManager" -> {
                ivIcon.setImageResource(R.drawable.spirka)
                tvTitle.text = context.getString(R.string.stops_manager)
            }
            "lineManager" -> {
                ivIcon.setImageResource(R.drawable.ic_baseline_format_list_numbered_24)
                tvTitle.text = context.getString(R.string.lines_manager)
            }
            "exportData" -> {
                ivIcon.setImageResource(R.drawable.ic_baseline_download_24)
                tvTitle.text = context.getString(R.string.export_data)
            }
            "importData" -> {
                ivIcon.setImageResource(R.drawable.ic_baseline_file_upload_blue_24)
                tvTitle.text = context.getString(R.string.import_data)
            }
            "resetData" -> {
                ivIcon.setImageResource(R.drawable.ic_baseline_refresh_24)
                tvTitle.text = context.getString(R.string.reset_data)
            }
            else -> {
                ivIcon.visibility = View.INVISIBLE
                tvTitle.text = item
            }
        }

        return view
    }
}