package com.vogella.android.dev

import com.vogella.android.devicefinder.data.*

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vogella.android.devicefinder.R

class DeviceAdapter(
    private val currentEmployee: Employee,
    private val deviceListViewModel: DeviceListViewModel
) : ListAdapter<Device, DeviceAdapter.DeviceViewHolder>(DeviceDiffCallback) {

    private val TAG: String = DeviceAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vh_device, parent, false)
        return DeviceViewHolder(view, currentEmployee, deviceListViewModel)
    }


    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = getItem(position)
        holder.bind(device)
    }

    /**
     *  ViewHolder class to show a Employee view
     *  with all elements from the vh_epml
     */
    class DeviceViewHolder(
        itemView: View,
        private var currentEmployee: Employee,
        private var deviceListViewModel: DeviceListViewModel
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvModel: TextView = itemView.findViewById(R.id.tv_model_name_vh)
        private val tvCurrentBy: TextView = itemView.findViewById(R.id.tv_currently_vh)
        private val tvYouDevice: TextView = itemView.findViewById(R.id.tv_your_device_vh)
        private val btSwitch: SwitchCompat = itemView.findViewById(R.id.bt_switch_vh)
        private val TAG: String = DeviceViewHolder::class.java.simpleName

        private var currentDevice: Device? = null

        init {

            btSwitch.setOnClickListener {
                Log.wtf(TAG, "Switch button listener für! ${currentDevice?.model}")

                if (btSwitch.isChecked) {
                    currentDevice?.let { it1 ->
                        deviceListViewModel.updateDeviceEmployee(
                            it1,
                            currentEmployee
                        )
                    }
                    Log.wtf(TAG, "Switch isChecked update employee! ${currentDevice?.model}")
                } else {
                    currentDevice?.let { it1 ->
                        deviceListViewModel.updateDeviceEmployee(
                            it1,
                            imOfficeEmployee()
                        )
                    }
                    Log.wtf(TAG, "Switch isChecked false update employee to ImOffice! ${currentDevice?.model}")
                }

            }
        }
        /* Bind employee name and image. */
        fun bind(device: Device) {
            currentDevice = device
            tvModel.text = currentDevice!!.model
            tvCurrentBy.text = "Currently @${device.employee.firstname} ${device.employee.lastname}"
            tvYouDevice.text ="This is your Device"

            btSwitch.isClickable = !(currentEmployee.id != currentDevice?.employee?.id&&
                    currentDevice!!.currentStatus!=Status.Free)


            if (currentEmployee.id == currentDevice!!.employee.id) {
                tvYouDevice.visibility = VISIBLE
                //set the current state of a Switch
                btSwitch.isChecked = true
            }
            else {
                tvYouDevice.visibility = INVISIBLE
                btSwitch.isChecked = false
            }


        }


    }

    object DeviceDiffCallback : DiffUtil.ItemCallback<Device>() {
        override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {

            return oldItem.employee==newItem.employee&&oldItem.currentStatus==newItem.currentStatus
        }

        override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
            return oldItem.id == newItem.id &&oldItem.employee.id == newItem.employee.id
        }
    }
}