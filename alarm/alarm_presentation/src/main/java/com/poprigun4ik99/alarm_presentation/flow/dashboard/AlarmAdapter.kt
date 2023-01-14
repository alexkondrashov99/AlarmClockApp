package com.poprigun4ik99.alarm_presentation.flow.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.poprigun4ik99.alarm_presentation.R
import com.poprigun4ik99.alarm_presentation.flow.dashboard.AlarmDashboardViewModel.AlarmUiItem
import com.poprigun4ik99.domain.model.AlarmRecord
import com.poprigun4ik99.domain.toRegularTimeString

class AlarmAdapter(
    private val onItemClick: (AlarmRecord) -> Unit,
    private val onItemLongClick: (AlarmRecord) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var items: List<AlarmUiItem> = emptyList()
        set(value) {
            val diffUtil = object : DiffUtil.Callback() {
                override fun getOldListSize() = field.size

                override fun getNewListSize() = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = field[oldItemPosition]
                    val new = value[newItemPosition]

                    return when {
                        old is AlarmUiItem.AddNewAlarm && new is AlarmUiItem.AddNewAlarm -> true
                        old is AlarmUiItem.AlarmItem && new is AlarmUiItem.AlarmItem -> old.alarmRecord.id == new.alarmRecord.id
                        else -> false
                    }
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val old = field[oldItemPosition]
                    val new = value[newItemPosition]

                    return when {
                        old is AlarmUiItem.AddNewAlarm && new is AlarmUiItem.AddNewAlarm -> true
                        old is AlarmUiItem.AlarmItem && new is AlarmUiItem.AlarmItem -> old.alarmRecord == new.alarmRecord
                        else -> false
                    }
                }
            }

            val changes = DiffUtil.calculateDiff(diffUtil)
            changes.dispatchUpdatesTo(this)

            field = value
        }

    abstract inner class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class AlarmItemViewHolder(view: View) : AlarmViewHolder(view) {
        fun bind(alarmItem: AlarmUiItem.AlarmItem) {
            itemView.findViewById<TextView>(R.id.tvAlarmDescription).text =
                alarmItem.alarmRecord.description
            itemView.findViewById<TextView>(R.id.tvAlarmTime).text =
                alarmItem.alarmRecord.timeStamp.toRegularTimeString()
            itemView.findViewById<View>(R.id.vTop).isVisible = alarmItem.topDividerIsVisible
            itemView.findViewById<View>(R.id.vBottom).isVisible = alarmItem.bottomDividerIsVisible
            itemView.findViewById<ConstraintLayout>(R.id.clRoot).apply {
                setOnClickListener {
                    onItemClick(alarmItem.alarmRecord)
                }
                setOnLongClickListener {
                    onItemLongClick(alarmItem.alarmRecord)
                    true
                }
            }
        }
    }

    inner class AddNewAlarmViewHolder(view: View) : AlarmViewHolder(view) {
        fun bind() {
            itemView.findViewById<ConstraintLayout>(R.id.clRoot).setOnClickListener {
                onAddClick()
            }
        }
    }

    fun submitList(alarmList: List<AlarmUiItem>) {
        items = alarmList
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AlarmUiItem.AlarmItem -> ALARM_ITEM_VIEW_TYPE
            AlarmUiItem.AddNewAlarm -> ADD_NEW_ALARM_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return when (viewType) {
            ALARM_ITEM_VIEW_TYPE -> AlarmItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
            )

            else -> AddNewAlarmViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_alarm, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        when (holder) {
            is AlarmItemViewHolder -> (items[position] as? AlarmUiItem.AlarmItem)?.let {
                holder.bind(it)
            }
            is AddNewAlarmViewHolder -> holder.bind()
            else -> Unit
        }
    }

    override fun getItemCount() = items.size

    companion object {
        const val ADD_NEW_ALARM_VIEW_TYPE = 0
        const val ALARM_ITEM_VIEW_TYPE = 1
    }
}