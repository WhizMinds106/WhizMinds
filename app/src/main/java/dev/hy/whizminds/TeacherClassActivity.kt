package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class TeacherClassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app fullscreen
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_teacher_class)

        val codeButton = findViewById<Button>(R.id.btnCode)
        val sectionButton = findViewById<Button>(R.id.btnSections)
        val backButton = findViewById<Button>(R.id.btnBackTeacher)

        codeButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    codeButton.setBackgroundResource(R.drawable.create_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    codeButton.setBackgroundResource(R.drawable.create)
                }
            }
            false
        }
        codeButton.setOnClickListener {
            startActivity(Intent(this, TeacherCodeActivity::class.java))
            finish()
        }

        sectionButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    sectionButton.setBackgroundResource(R.drawable.sections_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    sectionButton.setBackgroundResource(R.drawable.sections)
                }
            }
            false
        }
        sectionButton.setOnClickListener {
            startActivity(Intent(this, TeacherSectionsActivity::class.java))
            finish()
        }

        backButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    backButton.setBackgroundResource(R.drawable.btn_back_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    backButton.setBackgroundResource(R.drawable.btn_back)
                }
            }
            false
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }
    }
}