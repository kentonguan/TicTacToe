package com.kenton.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kenton.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        private const val TIC_TAC_TOE_BOARD_SIZE = 4
    }

    private lateinit var viewbinding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private val ticTacToeAdapter: TicTacToeAdapter = TicTacToeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(TIC_TAC_TOE_BOARD_SIZE)).get(MainViewModel::class.java)
            .apply {
                currentPiece.observe {
                    viewbinding.currentPieceImage.setImageResource(it.pieceDrawable)
                }

                ticTacToeBoard.observe {
                    ticTacToeAdapter.submitList(it)
                }
            }

        setupTicTacToeBoard()
        viewbinding.newGameButton.setOnClickListener { viewModel.onNewGameClick() }
    }

    private fun setupTicTacToeBoard() {
        viewbinding.ticTacToeBoard.layoutManager = TicTacToeGridLayoutManager(this, TIC_TAC_TOE_BOARD_SIZE)
        viewbinding.ticTacToeBoard.adapter = ticTacToeAdapter
    }

    private fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(this@MainActivity, Observer { observer.invoke(it) })
    }
}

