package com.example.fast2lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val addButton by lazy {
        findViewById<Button>(R.id.btn_add)
    }

    private val clearButton : Button by lazy {
        findViewById(R.id.btn_clear)
    }

    private val runButton : Button by lazy {
        findViewById(R.id.btn_run)
    }

    private val numberPicker : NumberPicker by lazy {
        findViewById(R.id.number_picker)
    }

    private var didRun = false
    private val pickNumberSet = mutableSetOf<Int>()

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.tv_one),
            findViewById<TextView>(R.id.tv_two),
            findViewById<TextView>(R.id.tv_three),
            findViewById<TextView>(R.id.tv_four),
            findViewById<TextView>(R.id.tv_five),
            findViewById<TextView>(R.id.tv_six)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initclearBytton()

    }

    private fun initclearBytton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach{
                it.isVisible = false // 추가한 텍스트를 숨긴다
            }
            didRun = false
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            //pickNumberSet 데이터는 아래에서 정의
            if (didRun) {
                Toast.makeText(this,"초기화 후에 시도해주세요. ",Toast.LENGTH_SHORT).show()
                return@setOnClickListener //setOnClickLister만 다시 호출
            }
            if (pickNumberSet.size >= 5) {
                Toast.makeText(this,"번호는 5개까지만 선택할 수 있습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this,"이미 포함되어 있는 번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numberTextViewList.get(pickNumberSet.size)
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            backColor(numberPicker.value,textView)

            pickNumberSet.add(numberPicker.value)


        }
    }

    //번호생성버튼 geRandomNumber를 받음
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true

                backColor(number,textView)
            }
        }
    }
    //랜덤번호 생성
    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for( i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue  // 이미 선택된 숫자가 있으면 넘어간다.
                    }
                    this.add(i)
                }
            }
        numberList.shuffle()
        val subList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size) // 이미 선택된 번호를 리스트로 만들고 선택된 데이터 수만큼 제외하고 뽑음
        return subList.sorted()
    }

    private fun backColor(number:Int, textView: TextView) {
        when(number) {
            in 1.. 10 ->  textView.background = ContextCompat.getDrawable(this,R.drawable.back_yellow)
            in 11.. 20 ->  textView.background = ContextCompat.getDrawable(this,R.drawable.back_blue)
            in 21.. 30 ->  textView.background = ContextCompat.getDrawable(this,R.drawable.back_red)
            in 31.. 40 ->  textView.background = ContextCompat.getDrawable(this,R.drawable.back_gray)
            in 41.. 45 ->  textView.background = ContextCompat.getDrawable(this,R.drawable.back_green)
        }
    }


}