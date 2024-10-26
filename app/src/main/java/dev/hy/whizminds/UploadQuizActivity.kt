package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class UploadQuizActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_quiz)

        val backButton = findViewById<Button>(R.id.btnBackTeacher)
        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        tvStatus = findViewById(R.id.tvStatus)

        // Set up buttons to upload quizzes
        findViewById<Button>(R.id.btnUploadEnglishQuiz).setOnClickListener {
            uploadQuiz("english_quiz_questions")
        }

        findViewById<Button>(R.id.btnUploadMathQuiz).setOnClickListener {
            uploadQuiz("math_quiz_questions")
        }
    }

    private fun uploadQuiz(filename: String) {
        // Retrieve the resource ID of the JSON file
        val resourceId = resources.getIdentifier(filename, "raw", packageName)
        if (resourceId == 0) {
            Log.e("UploadQuizActivity", "File not found: $filename in res/raw folder.")
            tvStatus.text = "Error: JSON file not found!"
            return
        }

        try {
            // Read the JSON file from the raw resources
            val inputStream = resources.openRawResource(resourceId)
            val reader = InputStreamReader(inputStream)
            val gson = Gson()

            // Convert JSON to map to allow flexible structure
            val quizType = object : TypeToken<Map<String, Any>>() {}.type
            val quizData: Map<String, Any> = gson.fromJson(reader, quizType)
            reader.close()

            // Upload to Firestore
            firestore.collection("quizQuestions").document(filename)
                .set(quizData)
                .addOnSuccessListener {
                    tvStatus.text = "$filename uploaded successfully!"
                    Log.d("UploadQuizActivity", "$filename uploaded successfully!")
                }
                .addOnFailureListener { e ->
                    Log.e("UploadQuizActivity", "Error uploading quiz: ", e)
                    tvStatus.text = "Error uploading quiz!"
                }

        } catch (e: Exception) {
            Log.e("UploadQuizActivity", "Error reading JSON file: ", e)
            tvStatus.text = "Error reading JSON file!"
        }
    }
}
