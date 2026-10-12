package com.example.mycalc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var expression by mutableStateOf("")
        private set
    var result by mutableStateOf("0")
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> expression += action.number
            is CalculatorAction.Operation -> {
                if (expression.isNotEmpty() && !isLastOp()) {
                    expression += action.op
                }
            }
            is CalculatorAction.Clear -> {
                expression = ""
                result = "0"
            }
            is CalculatorAction.Delete -> {
                if (expression.isNotEmpty()) expression = expression.dropLast(1)
            }
            is CalculatorAction.Decimal -> {
                if (expression.isEmpty() || isLastOp()) {
                    expression += "0."
                } else if (!expression.split('+', '-', '×', '÷').last().contains('.')) {
                    expression += "."
                }
            }
            is CalculatorAction.Calculate -> calculateResult()
        }
    }

    private fun isLastOp(): Boolean =
        expression.isNotEmpty() && expression.last() in listOf('+', '-', '×', '÷')

    private fun calculateResult() {
        if (expression.isEmpty()) return
        try {
            val sanitized = expression.replace('×', '*').replace('÷', '/')
            val eval = simpleEval(sanitized)
            result = if (eval.isInfinite() || eval.isNaN()) {
                "Ошибка"
            } else if (eval % 1.0 == 0.0) {
                eval.toLong().toString()
            } else {
                "%.4f".format(eval).trimEnd('0').trimEnd('.')
            }
        } catch (_: Exception) {
            result = "Ошибка"
        }
    }

    private fun simpleEval(expr: String): Double {
        val numbers = mutableListOf<Double>()
        val ops = mutableListOf<Char>()
        var i = 0
        while (i < expr.length) {
            if (expr[i].isDigit() || expr[i] == '.') {
                val start = i
                while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) i++
                numbers.add(expr.substring(start, i).toDouble())
            } else {
                ops.add(expr[i])
                i++
            }
        }
        var idx = 0
        while (idx < ops.size) {
            if (ops[idx] == '*' || ops[idx] == '/') {
                val res = if (ops[idx] == '*') numbers[idx] * numbers[idx + 1] else numbers[idx] / numbers[idx + 1]
                numbers[idx] = res
                numbers.removeAt(idx + 1)
                ops.removeAt(idx)
            } else idx++
        }
        var total = numbers.getOrElse(0) { 0.0 }
        for (j in ops.indices) {
            if (ops[j] == '+') total += numbers[j + 1]
            if (ops[j] == '-') total -= numbers[j + 1]
        }
        return total
    }
}

sealed class CalculatorAction {
    data class Number(val number: String) : CalculatorAction()
    data class Operation(val op: Char) : CalculatorAction()
    data object Clear : CalculatorAction()
    data object Delete : CalculatorAction()
    data object Decimal : CalculatorAction()
    data object Calculate : CalculatorAction()
}
