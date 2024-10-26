package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherActivitiesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var sectionAdapter: SectionAdapter
    private var sectionsList = mutableListOf<Section>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_activities)

        val backButton = findViewById<Button>(R.id.btnBack)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.rvSections) // Updated ID to match layout XML
        recyclerView.layoutManager = LinearLayoutManager(this)
        sectionAdapter = SectionAdapter(sectionsList)
        recyclerView.adapter = sectionAdapter

        loadTeacherSections()

        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }
    }

    private fun loadTeacherSections() {
        val teacherId = auth.currentUser?.uid ?: return
        firestore.collection("sections")
            .whereEqualTo("teacherId", teacherId)
            .get()
            .addOnSuccessListener { documents ->
                sectionsList.clear()
                for (document in documents) {
                    val sectionName = document.getString("sectionName") ?: "Unknown Section"
                    val sectionId = document.id
                    val grade = document.getString("grade") ?: "grade1"
                    sectionsList.add(Section(sectionId, sectionName, grade))
                }
                sectionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("TeacherActivitiesActivity", "Error fetching sections", e)
            }
    }

    inner class SectionAdapter(private val sections: List<Section>) :
        RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_section, parent, false)
            return SectionViewHolder(view)
        }

        override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
            val section = sections[position]
            holder.bind(section)
        }

        override fun getItemCount(): Int = sections.size

        inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvSectionName: TextView = itemView.findViewById(R.id.tvSectionName)
            private val switchesByWeek = listOf(
                itemView.findViewById<Switch>(R.id.switchQuarter1Week1),
                itemView.findViewById<Switch>(R.id.switchQuarter1Week2),
                itemView.findViewById<Switch>(R.id.switchQuarter1Week3),
                itemView.findViewById<Switch>(R.id.switchQuarter2Week1),
                itemView.findViewById<Switch>(R.id.switchQuarter2Week2),
                itemView.findViewById<Switch>(R.id.switchQuarter2Week3),
                itemView.findViewById<Switch>(R.id.switchQuarter3Week1),
                itemView.findViewById<Switch>(R.id.switchQuarter3Week2),
                itemView.findViewById<Switch>(R.id.switchQuarter3Week3),
                itemView.findViewById<Switch>(R.id.switchQuarter4Week1),
                itemView.findViewById<Switch>(R.id.switchQuarter4Week2),
                itemView.findViewById<Switch>(R.id.switchQuarter4Week3)
            )

            fun bind(section: Section) {
                tvSectionName.text = "${section.name} (${section.grade.capitalize()})"

                // Initialize switches and listeners
                setupSwitchListeners(section)
            }

            private fun setupSwitchListeners(section: Section) {
                // Iterate over the weeks and set up the switch listeners
                switchesByWeek.forEachIndexed { index, switch ->
                    val quarter = index / 3 + 1 // Determine the quarter (1 to 4)
                    val week = index % 3 + 1 // Determine the week (1 to 3)

                    val firestorePath = "${section.grade}.quarter$quarter.week$week"

                    // Fetch current availability and set initial state
                    firestore.collection("sections").document(section.id)
                        .get()
                        .addOnSuccessListener { document ->
                            val isAvailable = document.getBoolean(firestorePath) ?: false
                            switch.isChecked = isAvailable
                        }

                    // Save updated availability on switch toggle
                    switch.setOnCheckedChangeListener { _, isChecked ->
                        firestore.collection("sections").document(section.id)
                            .update(firestorePath, isChecked)
                            .addOnFailureListener { e ->
                                Log.e("TeacherActivitiesActivity", "Error updating availability", e)
                            }
                    }
                }
            }
        }
    }
}

// Data class for Section information
data class Section(
    val id: String,
    val name: String,
    val grade: String
)
