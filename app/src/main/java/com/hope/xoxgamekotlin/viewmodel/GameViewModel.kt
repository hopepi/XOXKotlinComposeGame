package com.hope.xoxgamekotlin.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.xoxgamekotlin.model.Movement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _gameLevel = MutableStateFlow(savedStateHandle["gameLevel"] ?: 3)
    val gameLevel = _gameLevel.asStateFlow()

    private val _gameTurn = MutableStateFlow(savedStateHandle["gameTurn"] ?: 0)
    val gameTurn = _gameTurn.asStateFlow()

    private val initialMovements: List<Movement>
        get() = MutableList(gameLevel.value * gameLevel.value) { Movement() }

    val listOfMovements = mutableStateListOf(*initialMovements.toTypedArray())

    fun resetGame() {
        listOfMovements.clear()
        _gameTurn.value = 0
        listOfMovements.addAll(initialMovements)
        persist()
    }

    fun checkWin(index: Int, onWin: (Int?) -> Unit) {
        val size = _gameLevel.value

        val rowIndexes = buildList {
            val rowStart = index - (index % size)
            for (i in 0 until size) add(rowStart + i)
        }

        val colIndexes = buildList {
            val col = index % size
            for (i in 0 until size) add(i * size + col)
        }

        val mainDiagonalIndexes = buildList {
            for (i in 0 until size) add(i * size + i)
        }

        val antiDiagonalIndexes = buildList {
            for (i in 0 until size) add((i + 1) * (size - 1))
        }.filter { it < size * size }

        checkLine(rowIndexes, onWin)
        checkLine(colIndexes, onWin)
        checkLine(mainDiagonalIndexes, onWin)
        checkLine(antiDiagonalIndexes, onWin)

        val hasWinner = listOf(rowIndexes, colIndexes, mainDiagonalIndexes, antiDiagonalIndexes).any { indexes ->
            val turns = indexes.map { listOfMovements[it].turn }
            turns.none { it == null } && turns.distinct().count() == 1
        }

        if (!hasWinner && listOfMovements.all { it.filled }) {
            onWin(null)
        }
    }

    private fun checkLine(indexes: List<Int>, onWin: (Int?) -> Unit) {
        val turns = indexes.map { listOfMovements[it].turn }
        if (turns.none { it == null } && turns.distinct().count() == 1) {
            onWin(turns.first())
        }
    }

    fun newMovement(index: Int, turn: Int? = gameTurn.value) {
        if (!listOfMovements[index].filled) {
            listOfMovements[index] = listOfMovements[index].copy(
                filled = true,
                turn = turn
            )
        }
    }

    fun randomMovement(onWin: (Int?) -> Unit) {
        viewModelScope.launch {
            delay(500)
            if (gameTurn.value == 1) {
                val index = getFreeIndex()
                if (index != null) {
                    newMovement(index, turn = 1)
                    checkWin(index) { winner -> onWin(winner) }
                    changeTurn()
                }
            }
        }
    }

    fun getFreeIndex(): Int? {
        val indexes = listOfMovements.mapIndexedNotNull { index, movement ->
            if (!movement.filled) index else null
        }
        return indexes.randomOrNull()
    }

    fun changeTurn() {
        _gameTurn.update { if (it == 1) 0 else 1 }
        persist()
    }

    fun changeGameLevel(newLevel: Int) {
        val clamped = newLevel.coerceIn(3, 25)
        _gameLevel.value = clamped
        _gameTurn.value = 0
        listOfMovements.clear()
        listOfMovements.addAll(initialMovements)
        persist()
    }

    private fun persist() {
        savedStateHandle["gameLevel"] = _gameLevel.value
        savedStateHandle["gameTurn"]  = _gameTurn.value
    }
}
