package com.github.xserxses.nqueensproblem.game.board.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardElementUi
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardElementUi.Cell
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.lazyTableDimensions
import eu.wewox.lazytable.lazyTablePinConfiguration

@Composable
fun GameBoardComposable(
    cells: List<GameBoardElementUi>,
    onCellClick: (Cell) -> Unit
) {
    if (cells.isEmpty()) {
        return
    }

    Surface {
        LazyTable(
            pinConfiguration = lazyTablePinConfiguration(columns = 1, rows = 1),
            dimensions = lazyTableDimensions(
                { index ->
                    if (index == 0) {
                        BORDER_WIDTH
                    } else {
                        CELL_SIZE
                    }
                },
                { index ->
                    if (index == 0) {
                        BORDER_WIDTH
                    } else {
                        CELL_SIZE
                    }
                }
            )
        ) {
            items(
                count = cells.size,
                key = { index -> cells[index].id },
                layoutInfo = { index ->
                    LazyTableItem(
                        row = cells[index].y,
                        column = cells[index].x
                    )
                },
                contentType = { index ->
                    when (cells[index]) {
                        is GameBoardElementUi.Border -> 0
                        is Cell -> 1
                        is GameBoardElementUi.Placeholder -> 2
                    }
                }
            ) { index ->
                val item = cells[index]
                when (item) {
                    is GameBoardElementUi.Border -> BorderComposable(item)
                    is Cell -> CellComposable(
                        cell = item,
                        onClick = {
                            onCellClick(item)
                        }
                    )

                    is GameBoardElementUi.Placeholder -> PlaceholderComposable()
                }
            }
        }
    }
}

@Composable
private fun CellComposable(
    cell: Cell,
    onClick: () -> Unit
) {
    val backgroundColor = when (cell.color) {
        Cell.CellColor.DARK -> colorResource(R.color.chess_board_dark)
        Cell.CellColor.LIGHT -> colorResource(R.color.chess_board_light)
    }
    val borderColor =
        if (cell.state == Cell.CellState.DANGER) Color.Red else Color.Transparent

    Box(
        modifier = Modifier
            .size(CELL_SIZE)
            .background(backgroundColor)
            .border(4.dp, borderColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (cell.hasQueen) {
            Image(
                painter = painterResource(id = R.drawable.chess_queen),
                contentDescription = stringResource(R.string.game_queen_cd),
                modifier = Modifier.size(CELL_SIZE * 0.8f)

            )
        }
    }
}

@Composable
fun BorderComposable(border: GameBoardElementUi.Border) {
    Box(
        modifier = Modifier
            .width(BORDER_WIDTH)
            .height(CELL_SIZE)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(text = border.text)
    }
}

@Composable
fun PlaceholderComposable() {
    Box(
        modifier = Modifier
            .width(BORDER_WIDTH)
            .height(CELL_SIZE)
    )
}

private val CELL_SIZE = 48.dp
private val BORDER_WIDTH = 32.dp
