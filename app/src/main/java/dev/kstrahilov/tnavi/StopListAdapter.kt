package dev.kstrahilov.tnavi

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class StopListAdapter(var context: Context, var arrayList: ArrayList<Stop>) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.count()
    }

    override fun getItem(p0: Int): Any {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.stop_layout, null)
        val ivStop: ImageView = view.findViewById(R.id.iv_stop)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)

        val stop: Stop = arrayList[p0]
        if (stop.isCurrent) {
            ivStop.setImageResource(R.drawable.current_stop)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.red))
        } else if (stop.isNext) {
            ivStop.setImageResource(R.drawable.next_stop)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.green))
        } else {
            ivStop.setImageResource(R.drawable.stop)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.black))
        }
        tvTitle.text = stop.displayTitle

        return view
    }
}