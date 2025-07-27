package com.github.xserxses.nqueensproblem.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme

@Composable
fun NumberPickerComposable(
    modifier: Modifier = Modifier,
    initialValue: Int = 8,
    valueValidator: (Int) -> Boolean = { true },
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var text by remember { mutableStateOf(initialValue.toString()) }
        var isError by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.weight(0.5f))
        IconButton(onClick = {
            val number = text.toIntOrNull()
            isError = number == null || !valueValidator(number)
            number?.let {
                val newValue = it - 1
                text = newValue.toString()
                onValueChange.invoke(newValue)
            }
        }) {
            Icon(
                Icons.Filled.Remove,
                contentDescription = stringResource(R.string.number_picker_minus_cd)
            )
        }
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                val number = newText.toIntOrNull()
                isError = number == null || !valueValidator(number)
                number?.let { onValueChange.invoke(it) }
            },
            singleLine = true,
            textStyle = TextStyle(
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(0.5f)
        )
        IconButton(onClick = {
            val number = text.toIntOrNull()
            isError = number == null || !valueValidator(number)
            number?.let {
                val newValue = it + 1
                text = newValue.toString()
                onValueChange.invoke(newValue)
            }
        }) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.number_picker_plus_cd)
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@PreviewLightDark
@Composable
private fun NumberPickerComposablePreview() {
    NQueensProblemTheme {
        NumberPickerComposable(
            onValueChange = {}
        )
    }
}
