package com.aidencipher.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setOnClickListener {
            onBackPressed()
        }

        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()

        setUpRestView()
    }

    override fun onDestroy() {
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar(){
        progressBar.progress = restProgress
        restTimer = object: CountDownTimer(10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10-restProgress
                tvTimer.text = (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setUpExerciseView()
            }
        }.start()
    }

    private fun setUpRestView(){

        llReset.visibility = View.VISIBLE
        llExercise.visibility = View.GONE

        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setExerciseProgressBar(){
        progressBarExercise.progress = exerciseProgress
        exerciseTimer = object: CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = 30-exerciseProgress
                tvTimerExercise.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1){
                    setUpRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity, "Congratulations on Completing the 7 minute workout!", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun setUpExerciseView(){

        llReset.visibility = View.GONE
        llExercise.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "This language is not supported")
            }
        }else{
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}