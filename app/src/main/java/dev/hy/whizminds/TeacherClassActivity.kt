package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TeacherClassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class)

        val codeButton = findViewById<Button>(R.id.btnCode)
        val sectionButton = findViewById<Button>(R.id.btnSections)
        val backButton = findViewById<Button>(R.id.btnBackTeacher)

        codeButton.setOnClickListener {
            startActivity(Intent(this, TeacherCodeActivity::class.java))
            finish()
        }

        sectionButton.setOnClickListener {
            startActivity(Intent(this, TeacherSectionsActivity::class.java))
            finish()
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }
    }
}