package com.andreasgift.kutipanalkitab.ui

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.andreasgift.kutipanalkitab.R

//Next version implementation
class AlarmDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.alarm_dialog, null)

            val cancelBtn = view.findViewById<Button>(R.id.cancel_btn)
            cancelBtn.setOnClickListener(cancelBtnClickListener)

            val okBtn = view.findViewById<Button>(R.id.ok_btn)
            okBtn.setOnClickListener(okBtnClickListener)

            builder.setView(view)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private val cancelBtnClickListener: View.OnClickListener = View.OnClickListener {
        this.dismiss()
    }

    private val okBtnClickListener: View.OnClickListener = View.OnClickListener {
        setAlarm()
    }

    private fun setAlarm() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    }
}
