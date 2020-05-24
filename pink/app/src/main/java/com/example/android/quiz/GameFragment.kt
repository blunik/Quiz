package com.example.android.quiz

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.quiz.databinding.FragmentGameBinding

//Фрагмент игры
class GameFragment : Fragment() {
    data class Question(
        val text: String,
        val answers: List<String>)


//База данных вопрос и ответов.Первый ответ правильный,но они генеирируются
    private val questions: MutableList<Question> = mutableListOf(
        Question(text = "Начнем.Когда мы расстались и сошлись во второй раз?",
            answers = listOf("17 сентября и 26 октября", "16 сентября и 27 октября", "16 сентября и 25 октября", "16 сентября и 26  октября ")),
        Question(text = "Самый первый наш мем",
            answers = listOf("Рыбеха", "Наполеон", "Пуська", "Тортилья-ниндзя")),
        Question(text = "Когда мы в первый раз поцеловались?",
            answers = listOf("27 марта", "16 февраля", "3 апреля", "4 апреля")),
        Question(text = "На каком языке программирования я написала программу,в которой призналась тебе в любви?",
            answers = listOf("Lua", "JS", "C++", "Python")),
        Question(text = "Что мы любим больше всего?",
            answers = listOf("Приключения", "Самостоятельность", "Пивасик", "Неожиданность")),
        Question(text = "Кто больше пуська?(сложно)",
            answers = listOf("Юляша", "Пуська", "Хаба", "Кирилл")),
        Question(text = "Какой породы мой прошлый котик?",
            answers = listOf("У него нет породы", "Шотландец", "Британец", "Пусячной")),
        Question(text = "Что мы ценим больше всего в людях?",
            answers = listOf("Доверие", "Отзывчивость", "Доброту", "Ум")),
        Question(text = "Что я забыла у тебя дома в первый раз?",
            answers = listOf("Перчатки", "Наушники", "Майку", "Лифчик")),
        Question(text = "Как долго мы будем вместе?",
            answers = listOf("Бесконечность пусичек", "Вечность", "Бесконечность", "До самой смерти"))
    )


    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 1
    private var popt = 3
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        )

        randomizeQuestions()

        binding.game = this

        binding.submitButton.setOnClickListener { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController().navigate(
                            GameFragmentDirections.actionGameFragmentToGameWonFragment()
                        )
                    }
                } else if (popt > 0)
                {
                    popt -= 1
                    val builder = AlertDialog.Builder(this!!.activity!!)
                    builder.setMessage("Оставшихся попыток: $popt").create().show()

                } else {
                    view.findNavController()
                        .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }

        return binding.root
    }


    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}