package com.appleby.breakingbad.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appleby.breakingbad.model.CharacterRepo
import com.appleby.breakingbad.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityCharacterDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

    }

    override fun onResume() {
        super.onResume()

        val selectedCharId = intent.getIntExtra(EXTRA_CHARACTER_ID, 0)
        displayCharacerDetail(selectedCharId)
    }

    private fun displayCharacerDetail(characterId: Int) {
        val character = CharacterRepo.characters.find { it.char_id == characterId }
        character?.let {
            Glide.with(this).load(it.img).into(ivDetailImage)
            tvNickname.text = it.nickname.toUpperCase()
            tvSeason.text = if (it.appearance.isNullOrEmpty()) "" else "Season ${it.appearance.first()}"
            tvName.text = it.name
            tvOccupation.text = it.occupation.first()
            tvStatus.text = it.status
        }
    }

    companion object {
        private const val EXTRA_CHARACTER_ID = "EXTRA_CHAR_ID"

        fun prepareIntent(context: Context, characterId: Int) : Intent {
            val intent = Intent(context, ActivityCharacterDetail::class.java)
            intent.putExtra(EXTRA_CHARACTER_ID, characterId)
            return intent
        }
    }

}