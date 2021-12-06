package com.vogella.android.devicefinder

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vogella.android.devicefinder.data.Device
import com.vogella.android.devicefinder.data.Employee
import com.vogella.android.devicefinder.data.Status

/**
 * A simple [Fragment] subclass.
 * Use the [ListSecondFragment] factory method to
 * create an instance of this fragment.
 */
class ListSecondFragment : Fragment(R.layout.fragment_list_second) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(this.context!!).apply {

            setContent {
                val devices = mutableListOf<Device>()
                for (i in 2..20) {

                    val employee = Employee(2, "Igor", "Ferbert")
                    val device = Device(1, "Samsung s10", employee,Status.Free)
                    devices.add(device)
                }
                Conversation(devices)

            }
        }
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Preview(showBackground = true, name = "Light Mode")
    @Composable
    fun PreviewMessageCard() {
        val employee = Employee(2, "Igor", "Ferbert")
        val device = Device(1, "Samsung s10", employee,Status.Free)
        deviceCard(device)
    }

    @Composable
    fun Conversation(device: List<Device>) {
        LazyColumn {
            items(device) { device ->
                deviceCard(device)
            }
        }
    }

    @Composable
    fun deviceCard(device: Device) {
        //(modifier = Modifier.padding(all = 8.dp) ) {
        // Add a horizontal space between the image and the column


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = device.model)
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = device.employee.firstname)
            }
            Spacer(modifier = Modifier.width(48.dp))
            //modifier = Modifier.fillMaxWidth()
            Column {
                Text(
                    "This is your device",
                    color = Color(R.color.yellow),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    style = TextStyle(fontWeight = FontWeight.Light)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier.width(4.dp))
                SwitchDemo()
            }
        }

    }

    @Composable
    fun SwitchDemo() {
        val checkedState = remember { mutableStateOf(true) }
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
        )
    }
}