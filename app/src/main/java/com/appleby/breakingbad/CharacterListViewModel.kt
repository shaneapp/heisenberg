package com.appleby.breakingbad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class CharacterListViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _result: MutableLiveData<CharacterStates> = MutableLiveData()
    val result: LiveData<CharacterStates> = _result

}