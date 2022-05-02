package com.myexample.mymusicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myexample.mymusicplayer.databinding.ActivityPlayListBinding

class PlayListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPlayListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtnPLst.setOnClickListener {
            finish()
        }
    }
}