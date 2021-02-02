package com.appleby.breakingbad.view.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appleby.breakingbad.R
import com.appleby.breakingbad.networkmodel.Items
import com.appleby.breakingbad.view.adapter.CharacterListAdapter
import com.appleby.breakingbad.viewmodel.CharacterListViewModel
import com.appleby.breakingbad.viewmodel.CharacterStates
import kotlinx.android.synthetic.main.activity_main.*

class ActivityCharacterSelect : AppCompatActivity() {

    private val viewModel: CharacterListViewModel by viewModels()

    private val characterListAdapter =
        CharacterListAdapter(this) {
            startActivity(ActivityCharacterDetail.prepareIntent(this, it))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.result.observe(this, Observer {
            when(it) {
                is CharacterStates.NetworkSuccess -> updateRecyclerView(it.imageResults)
                is CharacterStates.NetworkFailure -> {
                    Toast.makeText(this, "Request failed", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val staggeredLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLayout.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvCharacterList.layoutManager = staggeredLayout
        rvCharacterList.adapter = characterListAdapter

        etSearch.doOnTextChanged { _, _, _, _ ->
            //viewModel.requestFilterRefresh()
        }

        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.performImageSearch(etSearch.text.toString())
                    return true
                }
                return false
            }
        })

        ivCross.setOnClickListener { etSearch.text.clear() }

        ivFilter.setOnClickListener {
            showSeasonFilter()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun updateRecyclerView(imageResults: List<Items>) {
        var filteredCharacters = imageResults

//        val nameFilter = etSearch.text
//        if (nameFilter.isNotEmpty()) {
//            filteredCharacters = filteredCharacters.filter { it.name.toLowerCase().contains(nameFilter) }
//        }
//
//        if (season > 0) {
//            filteredCharacters = filteredCharacters.filter { !it.appearance.isNullOrEmpty() }
//                .filter { it.appearance.contains(season) }
//        }

        characterListAdapter.updateData(filteredCharacters)
    }

    private fun showSeasonFilter() {
        val imageSizeCode = arrayOf("icon", "small", "medium", "large", "xlarge", "xxlarge", "huge")
        val imageSizeDisplay = arrayOf("Icon", "Small", "Medium", "Large", "Extra Large", "Super Large", "Huge")
        val seasonDialog = AlertDialog.Builder(this)
            .setTitle("Image Size")
            .setItems(imageSizeDisplay, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    viewModel.updateImageFilter(imageSizeCode[which])
                    viewModel.performImageSearch(etSearch.text.toString())
                }
            })

        seasonDialog.show()
    }

}