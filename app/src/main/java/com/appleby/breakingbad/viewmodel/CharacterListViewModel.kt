package com.appleby.breakingbad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appleby.breakingbad.BreakingBadApi
import com.appleby.breakingbad.model.CharacterRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterListViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _result: MutableLiveData<CharacterStates> = MutableLiveData()
    val result: LiveData<CharacterStates> = _result

    fun requestCharacters() {
        BreakingBadApi.service
            .getCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ characterResponse ->
                if (characterResponse.isSuccessful) {
                    characterResponse.body()?.let {
                        CharacterRepo.characters.clear()
                        CharacterRepo.characters.addAll(it)
                        _result.value =
                            CharacterStates.NetworkSuccess(
                                CharacterRepo.characters,
                                CharacterRepo.season
                            )
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

    fun refreshSeasonFilter(season: Int) {
        CharacterRepo.season = season
        _result.value =
            CharacterStates.SeasonFilter(season)
    }

    fun requestFilterRefresh() {
        _result.value = CharacterStates.SeasonFilter(
            CharacterRepo.season
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}