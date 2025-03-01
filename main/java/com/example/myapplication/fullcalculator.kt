package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat

class fullcalculator : ComponentActivity() {
    private lateinit var input: TextView
    private lateinit var output: TextView
    private lateinit var acBtn: Button
    private lateinit var cBtn: Button
    private lateinit var num0: Button
    private lateinit var num1: Button
    private lateinit var num2: Button
    private lateinit var num3: Button
    private lateinit var num4: Button
    private lateinit var num5: Button
    private lateinit var num6: Button
    private lateinit var num7: Button
    private lateinit var num8: Button
    private lateinit var num9: Button
    private lateinit var addBtn: Button
    private lateinit var subBtn: Button
    private lateinit var mulBtn: Button
    private lateinit var divBtn: Button
    private lateinit var dot: Button
    private lateinit var equalBtn: Button

    private var currentExpression = ""
    private var operand1 = ""
    private var operand2 = ""
    private var operator = ""
    private var lastResult = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fullcalculator)


        // Handle window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize views
        input = findViewById(R.id.equationTextView) // Changed to equationTextView
        output = findViewById(R.id.resultTextView) // Changed to resultTextView
        num0 = findViewById(R.id.zeroButton) // Changed to zeroButton
        num1 = findViewById(R.id.oneButton) // Changed to oneButton
        num2 = findViewById(R.id.twoButton) // Changed to twoButton
        num3 = findViewById(R.id.threeButton) // Changed to threeButton
        num4 = findViewById(R.id.fourButton) // Changed to fourButton
        num5 = findViewById(R.id.fiveButton) // Changed to fiveButton
        num6 = findViewById(R.id.sixButton) // Changed to sixButton
        num7 = findViewById(R.id.sevenButton) // Changed to sevenButton
        num8 = findViewById(R.id.eightButton) // Changed to eightButton
        num9 = findViewById(R.id.nineButton) // Changed to nineButton
        acBtn = findViewById(R.id.acButton) // Changed to acButton
        cBtn = findViewById(R.id.clearButton) // Changed to clearButton
        addBtn = findViewById(R.id.addButton) // Changed to addButton
        subBtn = findViewById(R.id.subtractButton) // Changed to subtractButton
        mulBtn = findViewById(R.id.multiplyButton) // Changed to multiplyButton
        divBtn = findViewById(R.id.divideButton) // Changed to divideButton
        equalBtn = findViewById(R.id.equalsButton) // Changed to equalsButton
        dot = findViewById(R.id.decimalButton) // Changed to decimalButton

        // Set click listeners
        setNumberListeners()
        setOperatorListeners()
        setUtilityListeners()
    }

    private fun setNumberListeners() {
        num0.setOnClickListener { appendNumber("0") }
        num1.setOnClickListener { appendNumber("1") }
        num2.setOnClickListener { appendNumber("2") }
        num3.setOnClickListener { appendNumber("3") }
        num4.setOnClickListener { appendNumber("4") }
        num5.setOnClickListener { appendNumber("5") }
        num6.setOnClickListener { appendNumber("6") }
        num7.setOnClickListener { appendNumber("7") }
        num8.setOnClickListener { appendNumber("8") }
        num9.setOnClickListener { appendNumber("9") }
        dot.setOnClickListener { appendNumber(".") }
    }

    private fun appendNumber(number: String) {
        // Prevent multiple dots in a single number
        if (number == "." && (operand2.contains(".") || (operator.isEmpty() && operand1.contains(".")))) {
            return
        }

        currentExpression += number
        input.text = currentExpression

        if (operator.isEmpty()) {
            operand1 += number
        } else {
            operand2 += number
        }
        calculateResult()
    }

    private fun setOperatorListeners() {
        addBtn.setOnClickListener { appendOperator("+") }
        subBtn.setOnClickListener { appendOperator("-") }
        mulBtn.setOnClickListener { appendOperator("*") }
        divBtn.setOnClickListener { appendOperator("/") }
    }

    private fun appendOperator(op: String) {
        if (operand1.isNotEmpty() && operator.isEmpty()) {
            operator = op
            currentExpression += op
            input.text = currentExpression
        } else if (operand2.isNotEmpty()) {
            // If there's already an operation, calculate and then set the new operator
            calculateResult()
            operand1 = output.text.toString()
            operand2 = ""
            operator = op
            currentExpression = operand1 + op
            input.text = currentExpression
        } else if (operand1.isNotEmpty() && operand2.isEmpty() && operator.isNotEmpty()){
            operator = op
            currentExpression = operand1 + op
            input.text = currentExpression
        }
    }

    private fun setUtilityListeners() {
        acBtn.setOnClickListener {
            clearAll()
        }

        cBtn.setOnClickListener {
            clearLast()
        }

        equalBtn.setOnClickListener {
            if (operand1.isNotEmpty() && operand2.isNotEmpty() && operator.isNotEmpty()) {
                calculateResult()
                lastResult = output.text.toString()
                input.text = lastResult
                currentExpression = lastResult
                operand1 = lastResult
                operand2 = ""
                operator = ""
            }
        }
    }

    private fun clearAll() {
        currentExpression = ""
        operand1 = ""
        operand2 = ""
        operator = ""
        input.text = ""
        output.text = ""
    }

    private fun clearLast() {
        if (currentExpression.isNotEmpty()) {
            currentExpression = currentExpression.dropLast(1)
            input.text = currentExpression

            if (operand2.isNotEmpty()) {
                operand2 = operand2.dropLast(1)
            } else if (operator.isNotEmpty()) {
                operator = ""
            } else if (operand1.isNotEmpty()) {
                operand1 = operand1.dropLast(1)
            }
            calculateResult()
        }
    }

    private fun calculateResult() {
        if (operand1.isNotEmpty() && operand2.isNotEmpty() && operator.isNotEmpty()) {
            try {
                val num1 = operand1.toDouble()
                val num2 = operand2.toDouble()
                val result = when (operator) {
                    "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                    "*" -> num1 * num2
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    else -> 0.0
                }
                val formattedResult = formatResult(result)
                output.text = formattedResult
            } catch (e: NumberFormatException) {
                output.text = "Error"
            }
        }
    }

    private fun formatResult(result: Double): String {
        return if (result.isNaN()) {
            "Error"
        } else if (result % 1 == 0.0) {
            DecimalFormat("#").format(result)
        } else {
            DecimalFormat("#.#######").format(result)
        }
    }
}