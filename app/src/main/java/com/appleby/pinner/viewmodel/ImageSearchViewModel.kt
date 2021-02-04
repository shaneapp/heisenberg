package com.appleby.pinner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appleby.pinner.GoogleSearchApi
import com.appleby.pinner.model.DataStore
import com.appleby.pinner.model.ObjectBox
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ImageSearchViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _result: MutableLiveData<ImageSearchState> = MutableLiveData()
    val result: LiveData<ImageSearchState> = _result

    fun clearPreviousSearchCache() {
        DataStore.lastSearch.clear()
    }

    fun performImageSearch(searchQuery : String, offsetIndex: Int) {
        GoogleSearchApi.service
            .getGoogleImageResults(searchQuery.replace("\\s".toRegex(), "+"), 10, offsetIndex)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ characterResponse ->
                if (characterResponse.isSuccessful) {
                    characterResponse.body()?.let {

                        val settings = ObjectBox.settingsBox[1]
                        settings.apiRequestCount -= 1
                        ObjectBox.settingsBox.put(settings)

                        DataStore.lastSearch.addAll(it.items)
                        _result.value = ImageSearchState.NetworkSuccess

                    } ?: run {
                        _result.value =
                            ImageSearchState.NetworkFailure
                    }
                } else {
                    _result.value =
                        ImageSearchState.NetworkFailure
                }
            }, {
                _result.value =
                    ImageSearchState.NetworkFailure
            }).addTo(compositeDisposable)
    }

    fun updateImageFilter(imageFilter : String) {
        DataStore.imageFilter = imageFilter
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}