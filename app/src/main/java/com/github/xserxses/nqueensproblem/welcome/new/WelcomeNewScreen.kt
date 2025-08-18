package com.github.xserxses.nqueensproblem.welcome.new

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.ui.shared.NumberPickerComposable
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.utils.GameBoardSizeValueValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeHomeNew(
    onNavigateBoard: (Int) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        var size: Int by remember { mutableIntStateOf(INITIAL_SIZE) }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_new_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.welcome_new_subtitle),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            NumberPickerComposable(
                initialValue = INITIAL_SIZE,
                valueValidator = GameBoardSizeValueValidator,
                onValueChange = { newValue ->
                    size = newValue
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current

            Button(
                onClick = {
                    if (GameBoardSizeValueValidator(size)) {
                        onNavigateBoard(size)
                    } else {
                        Toast.makeText(
                            context,
                            R.string.welcome_new_size_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.welcome_new_start)
                )
            }
        }
    }
}

private const val INITIAL_SIZE = 8

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun WelcomeHomeNewPreview() {
    NQueensProblemTheme {
        WelcomeHomeNew(
            onNavigateBoard = {},
            onDismiss = {},
            sheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded
            )
        )
    }
}
