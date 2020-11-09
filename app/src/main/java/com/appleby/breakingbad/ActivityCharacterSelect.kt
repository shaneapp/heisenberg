package com.appleby.breakingbad

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class ActivityCharacterSelect : AppCompatActivity() {

    private val viewModel: CharacterListViewModel by viewModels()

    private val characterListAdapter = CharacterListAdapter(this) {
        startActivity(ActivityCharacterDetail.prepareIntent(this, it))
    }

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

        etSearch.doOnTextChanged { _, _, _, _ ->
            viewModel.requestFilterRefresh()
        }

        ivCross.setOnClickListener { etSearch.text.clear() }

        ivFilter.setOnClickListener {
            showSeasonFilter()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestCharacters()
    }

    private fun updateRecyclerView(season: Int) {
        var filteredCharacters = CharacterRepo.characters.sortedBy { it.name }

        val nameFilter = etSearch.text
        if (nameFilter.isNotEmpty()) {
            filteredCharacters = filteredCharacters.filter { it.name.toLowerCase().contains(nameFilter) }
        }

        if (season > 0) {
            filteredCharacters = filteredCharacters.filter { !it.appearance.isNullOrEmpty() }
                .filter { it.appearance.contains(season) }
        }

        characterListAdapter.updateData(filteredCharacters)
    }

    private fun showSeasonFilter() {
        val seasonDialog = AlertDialog.Builder(this)
            .setTitle("Filter by season")
            .setItems(arrayOf("All Seasons", "Season 1", "Season 2", "Season 3", "Season 4", "Season 5"), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    viewModel.refreshSeasonFilter(which)
                }
            })

        seasonDialog.show()
    }

}