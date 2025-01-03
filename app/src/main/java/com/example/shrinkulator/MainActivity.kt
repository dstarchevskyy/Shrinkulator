package com.example.shrinkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shrinkulator.ui.theme.ShrinkulatorTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private var langs: List<CalcModel> = emptyList()

    private val myFlow: MutableStateFlow<List<CalcModel>> = MutableStateFlow(langs)

    var weightState: MutableState<String> = mutableStateOf("")
    var priceState: MutableState<String> = mutableStateOf("")

    private val myOnClick: () -> Unit = {
        println("@@@myOnClick")

        val newList: MutableList<CalcModel> = myFlow.value.toMutableList()
        newList.add(
            CalcModel(
                weight = weightState.value,
                price = priceState.value,
                calc = ""
            )
        )
        myFlow.value = newList
        weightState.value = ""
        priceState.value = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            weightState = remember { mutableStateOf("") }

            ShrinkulatorTheme {
                MyLayout(
                    myFlow = myFlow,
                    myOnClick = myOnClick,
                    weightState = weightState,
                    priceState = priceState
                )
            }
        }
    }
}

@Composable
fun InputForm(
    weightState: MutableState<String>,
    priceState: MutableState<String>,
) {
    Box(modifier = Modifier.fillMaxWidth()
        .background(color = Color.LightGray)
        .padding(all = 12.dp)
    ) {
        Row {
            TextField(
                value = weightState.value,
                placeholder = {
                    Text(text = "Weight")
                },
                modifier = Modifier.weight(1f)
                    .padding(2.dp),

                onValueChange = {
                    println("onChange: $it")
                    weightState.value = it
                }
            )

            TextField(
                value = priceState.value,
                placeholder = {
                    Text(text = "Price")
                },
                modifier = Modifier.weight(1f)
                    .padding(2.dp),
                onValueChange = {
                    println("onChange: $it")
                    priceState.value = it
                }
            )
        }
    }
}

@Composable
fun MyLayout(
    myFlow: StateFlow<List<CalcModel>>,
    myOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    weightState: MutableState<String>,
    priceState: MutableState<String>,
) {
    val collected: State<List<CalcModel>> = myFlow.collectAsState()

    Column(
        modifier = modifier.background(color = Color.Blue)
            .fillMaxSize()

    ) {
        LazyColumn(
            modifier = modifier.weight(1f),
        ) {
            itemsIndexed(
                items = collected.value,
                itemContent = { index, value ->
                    Text(text = "$index ${value.weight} / ${value.price} ")
                }
            )
        }

        Box(modifier = Modifier.fillMaxWidth()
            .background(color = Color.Gray)
            .padding(all = 12.dp)
        ) {
            Column {
                InputForm(
                    weightState = weightState,
                    priceState = priceState
                )

                Button(
                    onClick = myOnClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "ADD")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShrinkulatorTheme {
//        MyLayout(myState = State<List>(emptyList()), langs = listOf("ASD","QWER","QWER"), {})
    }
}