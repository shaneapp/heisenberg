package com.appleby.breakingbad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appleby.breakingbad.BreakingBadApi
import com.appleby.breakingbad.model.DataStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterListViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _result: MutableLiveData<CharacterStates> = MutableLiveData()
    val result: LiveData<CharacterStates> = _result

    fun performImageSearch(searchQuery : String) {
        BreakingBadApi.service
            .getGoogleImageResults(searchQuery.replace("\\s".toRegex(), "+"), 10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ characterResponse ->
                if (characterResponse.isSuccessful) {
                    characterResponse.body()?.let {

                        _result.value = CharacterStates.NetworkSuccess(it.items)

                    } ?: run {
                        _result.value =
                            CharacterStates.NetworkFailure
                    }
                } else {
                    _result.value =
                        CharacterStates.NetworkFailure
                }
            }, {
                _result.value =
                    CharacterStates.NetworkFailure
            }).addTo(compositeDisposable)
    }

    fun updateImageFilter(imageFilter : String) {
        DataStore.imageFilter = imageFilter
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}