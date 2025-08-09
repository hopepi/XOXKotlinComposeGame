package com.hope.xoxgamekotlin.view.gamescreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.xoxgamekotlin.R
import com.hope.xoxgamekotlin.ui.theme.Pink200
import com.hope.xoxgamekotlin.ui.theme.Pink300
import com.hope.xoxgamekotlin.ui.theme.Pink500
import com.hope.xoxgamekotlin.ui.theme.Yellow200
import com.hope.xoxgamekotlin.ui.theme.Yellow500
import com.hope.xoxgamekotlin.utils.getTaw
import com.hope.xoxgamekotlin.viewmodel.GameViewModel

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
){
    BackHandler { onBack() }
    var win by remember {
        mutableStateOf<Int?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Pink200,
                        Pink300,
                        Pink300
                    )
                )
            )
    ) {
        val gameLevel by viewModel.gameLevel.collectAsState()
        val gameTurn by viewModel.gameTurn.collectAsState()
        val listOfMovements = viewModel.listOfMovements


        Column(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileItem(
                    text = "You",
                    avatar = R.drawable.ic_profile,
                    taw = R.drawable.ic_circle,
                    tawTint = Color.White
                )

                ProfileItem(
                    text = "System",
                    avatar = R.drawable.ic_robot,
                    taw = R.drawable.ic_robot,
                    tawTint = Yellow200
                )
            }

            Spacer(modifier = Modifier.height(120.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Yellow500)
                        .clickable {
                            if (listOfMovements.count { it.filled } <= 0 && gameLevel in 3..24) {
                                viewModel.changeGameLevel(gameLevel + 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(140.dp, 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Yellow500),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = gameLevel,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInVertically() + fadeIn()).togetherWith(
                                    slideOutVertically() + fadeOut()
                                )
                            } else {
                                (slideInVertically() + fadeIn()).togetherWith(
                                    slideOutVertically() + fadeOut()
                                )
                            }.using(SizeTransform(false))
                        }
                    ) { target ->
                        Text(text = target.toString(), color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Yellow500)
                        .clickable {
                            if (listOfMovements.count { it.filled } <= 0 && gameLevel in 4..25) {
                                viewModel.changeGameLevel(gameLevel - 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(315.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .drawBehind {
                        drawRoundRect(
                            color = Pink500,
                            style = Stroke(
                                width = 10f,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 10f),
                                    0f
                                ),
                            ),
                            cornerRadius = CornerRadius(20.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    columns = GridCells.Fixed(gameLevel)
                ) {
                    itemsIndexed(listOfMovements){index,movement->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Pink500)
                                .aspectRatio(1f)
                                .clickable {
                                    if (win == null && !viewModel.listOfMovements[index].filled && viewModel.gameTurn.value == 0) {
                                        viewModel.newMovement(index)
                                        viewModel.checkWin(index) { result -> win = result }

                                        if (win == null) {
                                            viewModel.changeTurn()
                                            viewModel.randomMovement { winner ->
                                                if (winner != null) {
                                                    win = winner
                                                }
                                            }
                                        }
                                    }
                                }

                                .drawBehind{
                                    drawLine(
                                        color = Color.White,
                                        Offset(size.width,15f),
                                        Offset(size.width,size.height - 15),
                                        strokeWidth = 7f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(20f,10f), 0f
                                        )
                                    )
                                    drawLine(
                                        color = Color.White,
                                        Offset(15f,size.height),
                                        Offset(size.width - 15 ,size.height),
                                        strokeWidth = 7f,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(20f,10f), 0f
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ){
                            val imageRes = getTaw(movement.turn)
                            val animDuration = 500
                            Column {
                                AnimatedVisibility(
                                    visible = movement.filled,
                                    enter = scaleIn(tween(animDuration)) + fadeIn(
                                        tween(animDuration)
                                    ),
                                    exit = scaleOut(tween(animDuration)) + fadeOut(
                                        tween(animDuration)
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = imageRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(if (gameLevel >= 7) 20.dp else 30.dp),
                                        tint = if (movement.turn != 0) Yellow200 else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = win == null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                val turnBoxColor by animateColorAsState(
                    targetValue = if (gameTurn == 0) Pink500 else Yellow500,
                    animationSpec = tween(500)
                )
                Box(
                    modifier = Modifier
                        .size(140.dp,40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(turnBoxColor),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = gameTurn == 0,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            Text(text = "Your turn")
                        }
                    }
                    Row {
                        AnimatedVisibility(
                            visible = gameTurn == 1,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            Text(text = "System Turn")

                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = win != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(140.dp,40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (win == 0) Pink500 else Yellow500),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (win == 0) "You Won" else "System Won")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(140.dp,40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (win == 0) Pink500 else Yellow500)
                            .clickable {
                                viewModel.resetGame()
                                win = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Reset Game")
                    }
                }
            }
        }
    }
}



