package com.example.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Button
import com.example.simplecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var lastNumber = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression : Expression

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnAC.setOnClickListener {
        onAllClearClick()
    }

    binding.btnEquality.setOnClickListener {
        onEqualClick()
    }

    binding.btnBack.setOnClickListener {
        onBackClick()
    }

    binding.btnClear.setOnClickListener {
        onClearClick()
    }

    val digitButtonList = mutableListOf(binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9, binding.btnPoint)
    val operatorButtonList = mutableListOf(binding.btnDivide, binding.btnMinus, binding.btnMultiply, binding.btnPlus, binding.btnModulo)

    for (buttonDigit in digitButtonList) {
        buttonDigit.setOnClickListener {
            onDigitClick(it)
        }
    }

    for (buttonOperator in operatorButtonList) {
        buttonOperator.setOnClickListener {
            onOperatorClick(it)
        }
    }


}

fun onAllClearClick() {

    binding.tvData.text = " "
    binding.tvResult.text = " "
    stateError = false
    lastDot = false
    lastNumber = false
    binding.tvResult.visibility = GONE

}

fun onEqualClick() {

    onEqual()
    binding.tvData.text = binding.tvResult.text.toString().drop(1)

}

fun onClearClick() {

    binding.tvData.text = " "
    lastNumber = false


}

fun onDigitClick(view : View) {

    if (stateError){

        binding.tvData.text = ((view as Button)).text
        stateError = false

    } else {

        binding.tvData.append((view as Button).text)

    }

    lastNumber = true
    onEqual()

}

fun onOperatorClick(view : View) {

    if (!stateError && lastNumber) {

        binding.tvData.append((view as Button).text)
        lastDot = false
        lastNumber = false
        onEqual()

    }
}

fun onBackClick() {

    binding.tvData.text =  binding.tvData.text.toString().dropLast(1)

    try {

        val lastChar = binding.tvData.text.toString().last()

        if (lastChar.isDigit()){
            onEqual()
        }

    } catch (e : Exception) {

        binding.tvResult.text = " "
        binding.tvResult.text = " "
        Log.e("last char error",e.toString())

    }

}

private fun onEqual(){

    if(lastNumber && !stateError){

        val txt = binding.tvData.text.toString()

        expression = ExpressionBuilder(txt).build()

        try {

            val result = expression.evaluate()
            binding.tvResult.visibility = VISIBLE
            binding.tvResult.text = "= $result"

        } catch (ex : java.lang.ArithmeticException){

            Log.e("Evaluate error", ex.toString())

            binding.tvResult.text = "Error"
            stateError = true
            lastNumber = false


        }
      }
    }
}