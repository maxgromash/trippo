package edu.project.trippo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import edu.project.trippo.R
import edu.project.trippo.databinding.ActivityMainBinding
import edu.project.trippo.extensions.isOnline
import edu.project.trippo.ui.info.NoInternetActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.bottomNavigationViewBNV
        val navController = findNavController(R.id.containerRCV)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        if (!isOnline(this)) {
            val intent = Intent(this, NoInternetActivity::class.java)
            startActivity(intent)
        }
    }
}