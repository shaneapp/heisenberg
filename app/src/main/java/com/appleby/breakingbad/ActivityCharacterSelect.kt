package com.appleby.breakingbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class ActivityCharacterSelect : AppCompatActivity() {

    private val viewModel: CharacterListViewModel by viewModels()

    private val characterListAdapter = CharacterListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.result.observe(this, Observer {

        })

        rvCharacterList.layoutManager = GridLayoutManager(this, 2)
        rvCharacterList.adapter = characterListAdapter
    }

    private fun updateRecyclerView() {
        characterListAdapter.updateData(CharacterRepo.characters)
    }

}