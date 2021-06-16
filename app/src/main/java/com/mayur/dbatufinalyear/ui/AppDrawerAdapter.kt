package com.mayur.dbatufinalyear.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.dbatufinalyear.data.AppModel
import com.mayur.R
import com.mayur.dbatufinalyear.data.Constants
import kotlinx.android.synthetic.main.adapter_app_drawer.view.*


class AppDrawerAdapter(
    private var flag: Int,
    private val appLabelGravity: Int,
    private val clickListener: (AppModel) -> Unit,
    private val appInfoListener: (AppModel) -> Unit,
    private val appHideListener: (Int, AppModel) -> Unit
) : RecyclerView.Adapter<AppDrawerAdapter.ViewHolder>(), Filterable {

    var appsList: MutableList<AppModel> = mutableListOf()
    var appFilteredList: MutableList<AppModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_app_drawer, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (appFilteredList.size == 0) return
        val appModel = appFilteredList[holder.adapterPosition]
        holder.bind(flag, appLabelGravity, appModel, clickListener, appInfoListener)

        holder.appHideButton.setOnClickListener {
            appFilteredList.removeAt(holder.adapterPosition)
            appsList.remove(appModel)
            notifyItemRemoved(holder.adapterPosition)
            appHideListener(flag, appModel)
        }
        try { // Automatically open the app when there's only one search result
            if ((itemCount == 1) and (flag == Constants.FLAG_LAUNCH_APP))
                clickListener(appFilteredList[position])
        } catch (e: Exception) {

        }
    }

    override fun getItemCount(): Int = appFilteredList.size

    // Filter app search results
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchChars = constraint.toString()
                appFilteredList = (if (searchChars.isEmpty()) appsList
                else appsList.filter { app -> appLabelMatches(app.appLabel, searchChars) } as MutableList<AppModel>)

                val filterResults = FilterResults()
                filterResults.values = appFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appFilteredList = results?.values as MutableList<AppModel>
                notifyDataSetChanged()
            }
        }
    }

    private fun appLabelMatches(appLabel: String, searchChars: String): Boolean {
        return (appLabel.contains(searchChars, true) or
                appLabel.replace(Regex("[-_+,. ]"), "")
                    .contains(searchChars, true))
    }

    fun setAppList(appsList: MutableList<AppModel>) {
        this.appsList = appsList
        this.appFilteredList = appsList
        notifyDataSetChanged()
    }

    fun launchFirstInList() {
        if (appFilteredList.size > 0)
            clickListener(appFilteredList[0])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appHideButton: TextView = itemView.appHide
        val iv_app_image: ImageView = itemView.iv_app_image

        fun bind(flag: Int, appLabelGravity: Int, appModel: AppModel, listener: (AppModel) -> Unit, appInfoListener: (AppModel) -> Unit) =
            with(itemView) {
                appHideLayout.visibility = View.GONE
                appHideButton.text = (if (flag == Constants.FLAG_HIDDEN_APPS) "SHOW" else "HIDE")
                appTitle.text = appModel.appLabel
                appTitle.gravity = appLabelGravity

                val icon: Drawable = context.packageManager.getApplicationIcon(appModel.appPackage)
                iv_app_image.setImageDrawable(icon)

                if (appModel.user == android.os.Process.myUserHandle())
                    otherProfileIndicator.visibility = View.GONE
                else otherProfileIndicator.visibility = View.VISIBLE

                appTitle.setOnClickListener { listener(appModel) }
                iv_app_image.setOnClickListener { listener(appModel) }
                appTitle.setOnLongClickListener {
                    appHideLayout.visibility = View.VISIBLE
                    true
                }

                appInfo.setOnClickListener { appInfoListener(appModel) }
                appHideLayout.setOnClickListener { appHideLayout.visibility = View.GONE }
            }
    }
}