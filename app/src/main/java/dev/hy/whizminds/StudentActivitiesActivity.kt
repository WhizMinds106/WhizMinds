package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentActivitiesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var quizAdapter: QuizAdapter
    private var quizzesList = mutableListOf<Quiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_activities)

        val backButton = findViewById<Button>(R.id.btnBack)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewQuizzes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        quizAdapter = QuizAdapter(quizzesList) { quiz -> onQuizSelected(quiz) }
        recyclerView.adapter = quizAdapter

        loadAvailableQuizzes()

        backButton.setOnClickListener {
            startActivity(Intent(this, StudentDashboardActivity::class.java))
            finish()
        }
    }

    private fun loadAvailableQuizzes() {
        // Replace with your logic for retrieving quizzes for a specific student
        val studentId = auth.currentUser?.uid ?: return
        firestore.collection("quizQuestions")
            .get()
            .addOnSuccessListener { documents ->
                quizzesList.clear()
                for (document in documents) {
                    val quizId = document.id
                    val quizTitle = document.getString("title") ?: "Untitled Quiz"
                    quizzesList.add(Quiz(quizId, quizTitle))
                }
                quizAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("StudentActivitiesActivity", "Error fetching quizzes", e)
                Toast.makeText(this, "Error fetching quizzes", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onQuizSelected(quiz: Quiz) {
        // Navigate to QuizDetailActivity
        val intent = Intent(this, QuizDetailActivity::class.java)
        intent.putExtra("quizId", quiz.id) // Pass the quiz ID here
        startActivity(intent)
    }

    inner class QuizAdapter(private val quizzes: List<Quiz>, private val onClick: (Quiz) -> Unit) :
        RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quiz, parent, false)
            return QuizViewHolder(view)
        }

        override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
            val quiz = quizzes[position]
            holder.bind(quiz, onClick)
        }

        override fun getItemCount(): Int = quizzes.size

        inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvQuizTitle: TextView = itemView.findViewById(R.id.tvQuizTitle)

            fun bind(quiz: Quiz, onClick: (Quiz) -> Unit) {
                tvQuizTitle.text = quiz.title
                itemView.setOnClickListener { onClick(quiz) }
            }
        }
    }
}

// Data class for Quiz information
data class Quiz(
    val id: String,
    val title: String
)
