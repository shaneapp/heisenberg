package com.appleby.breakingbad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
            when(it) {
                is CharacterStates.NetworkSuccess -> updateRecyclerView(it.season)
                is CharacterStates.SeasonFilter -> updateRecyclerView(it.season)
                is CharacterStates.NetworkFailure -> {
                    Toast.makeText(this, "Request failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        rvCharacterList.layoutManager = GridLayoutManager(this, 2)
        rvCharacterList.adapter = characterListAdapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestCharacters()
    }

    private fun updateRecyclerView(season: Int) {
        characterListAdapter.updateData(CharacterRepo.characters)
    }

}