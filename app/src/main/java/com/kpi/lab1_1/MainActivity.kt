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
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    private var calculationResult by mutableStateOf("Показники ще не обчислено")

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
        val delta = 0.01

        val hp = inputs["Hp"]?.toDoubleOrNull() ?: .0
        val cp = inputs["Cp"]?.toDoubleOrNull() ?: .0
        val sp = inputs["Sp"]?.toDoubleOrNull() ?: .0
        val np = inputs["Np"]?.toDoubleOrNull() ?: .0
        val op = inputs["Op"]?.toDoubleOrNull() ?: .0
        val wp = inputs["Wp"]?.toDoubleOrNull() ?: .0
        val ap = inputs["Ap"]?.toDoubleOrNull() ?: .0
        if(abs(hp + cp + sp + np + op + wp + ap - 100) > delta) {
            calculationResult = "Помилка введення\nСума введених значень повинна дорівнювати 100"
            return
        }

        val kpc = 100/(100 - wp)
        val kpg = 100/(100 - wp - ap)
        val hc = hp*kpc
        val cc = cp*kpc
        val sc = sp*kpc
        val nc = np*kpc
        val oc = op*kpc
        val ac = ap*kpc
        val hg = hp*kpg
        val cg = cp*kpg
        val sg = sp*kpg
        val ng = np*kpg
        val og = op*kpg
        val qrn = 339*cp + 1030*hp - 108.8*(op - sp) - 25*wp
        val qsn = (qrn + 25*wp)*100/(100 - wp)
        val qgn = (qrn + 25*wp)*100/(100 - wp - ap)
        calculationResult =
            """
                Qрс = %.2f, Qрг = %.2f
                Hc = %.2f%%, Cc = %.2f%%
                Sc = %.2f%%, Nc = %.2f%%
                Oc = %.2f%%, Ac = %.2f%%
                Hг = %.2f%%, Cг = %.2f%%
                Sг = %.2f%%, Nг = %.2f%%
                Oг = %.2f%%
                Qрн = %.2f КДж/кг
                Qсн = %.2f КДж/кг
                Qгн = %.2f КДж/кг
            """.trimIndent()
                .format(
                    kpc, kpg,
                    hc, cc, sc, nc, oc, ac,
                    hg, cg, sg, ng, og,
                    qrn, qsn, qgn
                )
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
