package com.gb.material_1507_1544_3_1.view.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gb.material_1507_1544_3_1.databinding.ActivityRecyclerBinding

class RecyclerActivity:AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = arrayListOf(
            Data("Earth",type= TYPE_EARTH),
            Data("Earth",type= TYPE_EARTH),
            Data("Mars", "",type= TYPE_MARS),
            Data("Earth",type= TYPE_EARTH),
            Data("Earth",type= TYPE_EARTH),
            Data("Earth",type= TYPE_EARTH),
            Data("Mars", null,type= TYPE_MARS)
        )


        binding.recyclerView.adapter = RecyclerActivityAdapter(data,
        object : MyCallback{
            override fun onClick(position: Int) {
                Toast.makeText(this@RecyclerActivity,"РАБОТАЕТ ${data[position].someText} ${data[position].someDescription}",
                    Toast.LENGTH_SHORT).show()
            }

        })

    }
}