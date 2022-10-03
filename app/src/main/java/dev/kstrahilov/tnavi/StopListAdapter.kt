package dev.kstrahilov.tnavi

import android.content.Context
import android.util.Log
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
        Log.d("stop data current", stop.isCurrent.toString())
        Log.d("stop data stop", stop.isNext.toString())
        if (stop.isCurrent) {
            ivStop.setImageResource(R.drawable.tekushta_spirka)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.red))
        } else if (stop.isNext) {
            ivStop.setImageResource(R.drawable.sledvashta_spirka)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.green))
        } else {
            ivStop.setImageResource(R.drawable.spirka)
            tvTitle.setTextColor(tvTitle.context.getColor(R.color.black))
        }
        tvTitle.text = stop.title

        return view
    }
}