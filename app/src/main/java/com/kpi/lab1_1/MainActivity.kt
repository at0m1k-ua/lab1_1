package com.kpi.lab1_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private var calculationResult by mutableStateOf("Calculation results will be shown here")

    private var inputs by mutableStateOf(mapOf<String, String>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InputsScreen(
                inputs = inputs,
                calculationResult = calculationResult,
                onInputsChange = { updatedInputs ->
                    inputs = updatedInputs
                },
                onCalculate = {
                    calculate()
                }
            )
        }
    }

    private fun calculate() {
        // TODO implement business logic
        val hpValue = inputs["Hp"]?.toDoubleOrNull() ?: 0.0
        val cpValue = inputs["Cp"]?.toDoubleOrNull() ?: 0.0
        calculationResult = "Result: ${hpValue + cpValue}"
    }
}

@Composable
fun InputsScreen(
    inputs: Map<String, String>,
    calculationResult: String,
    onInputsChange: (Map<String, String>) -> Unit,
    onCalculate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            listOf("Hp", "Cp", "Sp", "Np", "Op", "Wp", "Ap").forEach { inputName ->
                Input(
                    label = inputName,
                    units = "%",
                    value = inputs[inputName] ?: "",
                    onValueChange = { newValue ->
                        onInputsChange(inputs.toMutableMap().apply { put(inputName, newValue) })
                    }
                )
            }
        }

        Text(calculationResult)

        Column {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    onCalculate()
                }
            ) {
                Text(
                    "Calculate",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(label: String, units: String, value: String, onValueChange: (String) -> Unit) {
    val regex = Regex("^\\d*\\.?\\d*\$")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
    ) {
        Text("$label, ")
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.isEmpty() || it.matches(regex)) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.height(48.dp).padding(horizontal = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        Text(units)
    }
}
