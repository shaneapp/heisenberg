package com.appleby.breakingbad

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appleby.breakingbad.model.CharacterRepo
import com.appleby.breakingbad.viewmodel.CharacterListViewModel
import com.appleby.breakingbad.viewmodel.CharacterStates
import io.mockk.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = RxImmediateSchedulerRule()

    lateinit var myViewModel: CharacterListViewModel
    private val observer: Observer<CharacterStates> = mockk(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(CharacterRepo)
        myViewModel = CharacterListViewModel()
        myViewModel.result.observeForever(observer)
    }

    @Test
    fun seasonFilterEmitsCorrectState() {
        myViewModel.refreshSeasonFilter(2)
        val state = myViewModel.result.value
        assertEquals(CharacterStates.SeasonFilter(2) ,state)
        verify(exactly = 1) { observer.onChanged(any()) }
    }

    @Test
    fun seasonFilterRefreshesCorrectly() {
        myViewModel.refreshSeasonFilter(3)
        myViewModel.requestFilterRefresh()
        assertEquals(CharacterStates.SeasonFilter(3), myViewModel.result.value)
        verify(exactly = 2) { observer.onChanged(any()) }
    }

    @Test
    fun characterRequestSuccess() {
        myViewModel.requestCharacters()
        every { CharacterRepo.characters } returns mutableListOf()
        assert(myViewModel.result.value is CharacterStates.NetworkSuccess)
    }

}