package edu.project.trippo.ui.info

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.project.trippo.R
import edu.project.trippo.extensions.isOnline
import edu.project.trippo.ui.login.StartActivity

class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_no_internet)

        val x = findViewById<Button>(R.id.updateB)
        x.setOnClickListener {
            if (isOnline(this)) {
                val intent = Intent(this, StartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //onBackPressed()
            } else
                Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show()
        }
    }
}