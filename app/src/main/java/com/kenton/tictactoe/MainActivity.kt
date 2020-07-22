package com.kenton.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kenton.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var viewbinding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)
            .apply {
                currentPiece.observe {
                    viewbinding.currentPieceImage.setImageResource(it.pieceDrawable)
                }

                newGameEvent.observe {
                    // TODO -- clear board
                }
            }
    }

    private fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(this@MainActivity, Observer { observer.invoke(it) })
    }
}

