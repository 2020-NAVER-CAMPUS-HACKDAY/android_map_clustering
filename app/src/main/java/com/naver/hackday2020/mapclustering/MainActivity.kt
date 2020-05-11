package com.naver.hackday2020.mapclustering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.map.MapFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpMap()
    }

    private fun setUpMap() {
        (map as MapFragment?) ?: MapFragment.newInstance().also {
            supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
        }
    }
}
