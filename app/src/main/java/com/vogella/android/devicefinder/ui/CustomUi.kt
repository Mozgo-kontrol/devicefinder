
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Title(string: String) {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {Text(
        string,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(bottom = 4.dp)
    )}

}

@Composable
fun ErrorMessage(message: String){
    Text(
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 16.dp),
        text = message,
        style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
    )
}

@Composable
fun Message(message: String){
    Text(
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 16.dp),
        text = message,
        style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onPrimary)
    )
}
@Composable
fun CustomToolBar(){
    TopAppBar(title = {
             Text("Device Finder")
           })

}

