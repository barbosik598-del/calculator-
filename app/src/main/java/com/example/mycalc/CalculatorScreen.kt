package com.example.mycalc

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalc.ui.theme.liquidGlass

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val dynamicPrimary = MaterialTheme.colorScheme.primaryContainer
    val dynamicTertiary = MaterialTheme.colorScheme.tertiaryContainer

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        dynamicTertiary.copy(alpha = 0.40f),
                        MaterialTheme.colorScheme.surface
                    ),
                    radius = 1400f
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Стеклянный экран вывода
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .liquidGlass(
                        shapeRadius = 32.dp,
                        tintColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = viewModel.expression.ifEmpty { "0" },
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = viewModel.result,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Кнопки калькулятора
            val buttons = listOf(
                listOf("C", "÷", "×", "DEL"),
                listOf("7", "8", "9", "-"),
                listOf("4", "5", "6", "+"),
                listOf("1", "2", "3", "="),
                listOf("0", ".", "", "")
            )

            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { symbol ->
                        if (symbol.isNotEmpty()) {
                            GlassCalcButton(
                                text = symbol,
                                modifier = Modifier
                                    .weight(if (symbol == "0") 2f else 1f)
                                    .aspectRatio(if (symbol == "0") 2.1f else 1f),
                                onClick = {
                                    when (symbol) {
                                        "C" -> viewModel.onAction(CalculatorAction.Clear)
                                        "DEL" -> viewModel.onAction(CalculatorAction.Delete)
                                        "=" -> viewModel.onAction(CalculatorAction.Calculate)
                                        "+" -> viewModel.onAction(CalculatorAction.Operation('+'))
                                        "-" -> viewModel.onAction(CalculatorAction.Operation('-'))
                                        "×" -> viewModel.onAction(CalculatorAction.Operation('×'))
                                        "÷" -> viewModel.onAction(CalculatorAction.Operation('÷'))
                                        "." -> viewModel.onAction(CalculatorAction.Decimal)
                                        else -> viewModel.onAction(CalculatorAction.Number(symbol))
                                    }
                                }
                            )
                        } else if (symbol.isEmpty() && row.count { it.isEmpty() } == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlassCalcButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isAction = text in listOf("+", "-", "×", "÷", "=")
    val buttonColor = if (isAction) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.28f)
    }

    Box(
        modifier = modifier
            .liquidGlass(
                shapeRadius = 26.dp,
                tintColor = buttonColor,
                borderColor = if (isAction) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.25f)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (text == "DEL") {
            Icon(
                Icons.Filled.Backspace,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = text,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isAction) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
