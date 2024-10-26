package dev.hy.whizminds

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class QuizDetailActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private var questionList = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_detail)

        firestore = FirebaseFirestore.getInstance()

        // Get the quiz ID passed from StudentActivitiesActivity
        val quizId = intent.getStringExtra("quizId") ?: return

        recyclerView = findViewById(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        questionAdapter = QuestionAdapter(questionList)
        recyclerView.adapter = questionAdapter

        loadQuizQuestions(quizId)

        // Back button functionality
        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            finish() // Close the current activity
        }
    }

    private fun loadQuizQuestions(quizId: String) {
        firestore.collection("quizQuestions")
            .document(quizId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val questions = document.get("questions") as? List<Map<String, Any>> ?: listOf()
                    questionList.clear()
                    for (questionMap in questions) {
                        val questionId = questionMap["id"] as? String ?: "No ID"
                        val questionText = questionMap["question"] as? String ?: "No Question Text"
                        val options = questionMap["options"] as? List<String> ?: listOf()
                        questionList.add(Question(questionId, questionText, options))
                    }
                    questionAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("QuizDetailActivity", "Error fetching questions", e)
                Toast.makeText(this, "Error fetching questions", Toast.LENGTH_SHORT).show()
            }
    }

    inner class QuestionAdapter(private val questions: List<Question>) :
        RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val view = layoutInflater.inflate(R.layout.item_questions, parent, false)
            return QuestionViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val question = questions[position]
            holder.bind(question)
        }

        override fun getItemCount(): Int = questions.size

        inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvQuestionText: TextView = itemView.findViewById(R.id.tvQuestionText)
            private val optionsContainer: ViewGroup = itemView.findViewById(R.id.optionsContainer)

            fun bind(question: Question) {
                tvQuestionText.text = question.text
                optionsContainer.removeAllViews() // Clear previous options

                question.options.forEach { option ->
                    val optionTextView = TextView(itemView.context).apply {
                        text = option
                        setPadding(16, 8, 16, 8) // Add some padding
                    }
                    optionsContainer.addView(optionTextView) // Add the option to the container
                }
            }
        }
    }
}

// Data class for Question information
data class Question(
    val id: String,
    val text: String,
    val options: List<String> // Assuming questions have options (for MCQs)
)
