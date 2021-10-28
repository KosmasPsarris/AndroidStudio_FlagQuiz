package com.p17191.ergasies.flagquiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.p17191.ergasies.flagquiz.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuizQuestionsBinding

    private var quizCurrentPosition : Int = 0
    private var quizQuestionList : ArrayList<Question>? = null
    private var quizSelectedOptionPosition : Int = 0 //default value
    private var quizCorrectAnswers : Int = 0
    private var quizUsername : String? = null
    private var hasPicked : Boolean = false
    private var canPickOption : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizUsername = intent.getStringExtra(Constants.USER_NAME)

        quizQuestionList = Constants.getQuestions() //get it here so it won't be shuffled each time we call setQuestion()
        setQuestion()

        //make our class inherit from on click listener so by putting this, we subscribe to this class
        binding.tvOption1.setOnClickListener(this)
        binding.tvOption2.setOnClickListener(this)
        binding.tvOption3.setOnClickListener(this)
        binding.tvOption4.setOnClickListener(this)
        binding.buttonSubmit.setOnClickListener(this)
    }

    private fun setQuestion(){
        val question: Question = quizQuestionList!![quizCurrentPosition]

        defaultOptionsView() //reset options/buttons

        binding.progressBar.progress = quizCurrentPosition + 1
        binding.tvProgressBar.text = getString(R.string.progressBar_dynamic, (quizCurrentPosition+1).toString(), binding.progressBar.max.toString())

        binding.tvQuestion.text = question.question
        binding.ivFlag.setImageResource(question.image)
        binding.tvOption1.text = question.optionOne
        binding.tvOption2.text = question.optionTwo
        binding.tvOption3.text = question.optionThree
        binding.tvOption4.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()

        options.add(0, binding.tvOption1)
        options.add(1, binding.tvOption2)
        options.add(2, binding.tvOption3)
        options.add(3, binding.tvOption4)

        for(option in options){
            option.setTextColor(Color.parseColor("#a9a9a9"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.flag_background)
        }
        hasPicked = false
        canPickOption = true
    }

    // check which view was clicked by id
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvOption1 ->{
                if(canPickOption) {
                    selectedOptionView(binding.tvOption1, 1)
                    hasPicked = true
                }
            }
            R.id.tvOption2 ->{
                if(canPickOption) {
                    selectedOptionView(binding.tvOption2, 2)
                    hasPicked = true
                }
            }
            R.id.tvOption3 ->{
                if(canPickOption) {
                    selectedOptionView(binding.tvOption3, 3)
                    hasPicked = true
                }
            }
            R.id.tvOption4 ->{
                if(canPickOption) {
                    selectedOptionView(binding.tvOption4, 4)
                    hasPicked = true
                }
            }
            R.id.buttonSubmit -> {
                //user can only submit if there is an option selected, if an option is submitted user cannot pick another option!
                if (hasPicked) {
                    canPickOption = false

                    if (quizSelectedOptionPosition == 0) {
                        quizCurrentPosition++ // so we get next question

                        //if there are more questions, reset everything and show it, else go to score screen
                        when {
                            quizCurrentPosition < quizQuestionList!!.size -> {
                                setQuestion()
                                binding.buttonSubmit.setText(R.string.submit_button) // reset submit text
                            }
                            else -> {
                                val intent = Intent(this, ResultActivity::class.java)
                                intent.putExtra(Constants.USER_NAME, quizUsername)
                                intent.putExtra(Constants.CORRECT_ANSWERS, quizCorrectAnswers)
                                intent.putExtra(
                                    Constants.TOTAL_QUESTIONS,
                                    quizQuestionList!!.size
                                ) //when something is nullable, we use !! to force a non nullable value
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        //get the current question object and if it's answer is not the same
                        //as the selected option, it becomes red. In any case correct option becomes green
                        val question = quizQuestionList?.get(quizCurrentPosition)
                        if (question!!.correctOption != quizSelectedOptionPosition) {
                            answerView(
                                quizSelectedOptionPosition,
                                R.drawable.flag_incorrect_background
                            )
                        } else quizCorrectAnswers++ //increment correct answers

                        answerView(question.correctOption, R.drawable.flag_correct_background)

                        //if it's the last question text becomes "finish" and clicking will take us to the score screen
                        //else to the next question
                        if (quizCurrentPosition == quizQuestionList!!.size - 1)
                            binding.buttonSubmit.setText(R.string.finish_button)
                        else
                            binding.buttonSubmit.setText(R.string.next_question_button)

                        quizSelectedOptionPosition =
                            0 //reset so by pressing the button again it will take us to the next step
                    }
                }
                else
                    Toast.makeText(this,"Please pick an option to submit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //fun that sets background drawable to a textView option
    private fun answerView(answer : Int, drawableView : Int){
        when(answer){
            1 -> {
                binding.tvOption1.background = ContextCompat.getDrawable(this, drawableView)
                if(drawableView == R.drawable.flag_correct_background) //set text color to black if it's the correct option otherwise to dark gray
                    binding.tvOption1.setTextColor(Color.parseColor("#000000"))
                else
                    binding.tvOption1.setTextColor(Color.parseColor("#a9a9a9"))
            }
            2 -> {
                binding.tvOption2.background = ContextCompat.getDrawable(this, drawableView)
                if(drawableView == R.drawable.flag_correct_background)
                    binding.tvOption2.setTextColor(Color.parseColor("#000000"))
                else
                    binding.tvOption2.setTextColor(Color.parseColor("#a9a9a9"))
            }
            3 -> {
                binding.tvOption3.background = ContextCompat.getDrawable(this, drawableView)
                if(drawableView == R.drawable.flag_correct_background)
                    binding.tvOption3.setTextColor(Color.parseColor("#000000"))
                else
                    binding.tvOption3.setTextColor(Color.parseColor("#a9a9a9"))
            }
            4 -> {
                binding.tvOption4.background = ContextCompat.getDrawable(this, drawableView)
                if(drawableView == R.drawable.flag_correct_background)
                    binding.tvOption4.setTextColor(Color.parseColor("#000000"))
                else
                    binding.tvOption4.setTextColor(Color.parseColor("#a9a9a9"))
            }
        }
    }

    private fun selectedOptionView (optionTextView : TextView, selectedOptionNumber : Int){
        defaultOptionsView() //reset again

        quizSelectedOptionPosition = selectedOptionNumber

        optionTextView.setTextColor(Color.parseColor("#000000"))
        optionTextView.setTypeface(optionTextView.typeface, Typeface.BOLD)
        optionTextView.background = ContextCompat.getDrawable(this, R.drawable.flag_selected_background)
    }
}