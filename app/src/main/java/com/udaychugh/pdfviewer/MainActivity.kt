package com.udaychugh.pdfviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var storage : android.widget.Button
    lateinit var internet : android.widget.Button
    lateinit var documentation : android.widget.Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        storage = findViewById(R.id.buttonStorage)
        internet = findViewById(R.id.buttonInternet)
        documentation = findViewById(R.id.buttonAssests)

        storage.setOnClickListener {
            val intent = Intent(this, pdfview::class.java)
            intent.putExtra("ViewType", "storage")
            startActivity(intent)
        }

        internet.setOnClickListener {
            val intent = Intent(this, pdfview::class.java)
            intent.putExtra("ViewType", "internet")
            startActivity(intent)
        }

        documentation.setOnClickListener {
            val intent = Intent(this, pdfview::class.java)
            intent.putExtra("ViewType", "assests")
            startActivity(intent)
        }


    }

}