package com.hope.xoxgamekotlin.view.mainmenuscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hope.xoxgamekotlin.utils.Difficulty
import com.hope.xoxgamekotlin.ui.theme.Pink200
import com.hope.xoxgamekotlin.ui.theme.Pink300
import com.hope.xoxgamekotlin.ui.theme.Pink500
import com.hope.xoxgamekotlin.ui.theme.Yellow500

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainMenuScreen(
    onStartGame: () -> Unit,
    onDifficultyChange: (Difficulty) -> Unit = {}
) {
    var showSettings by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
    var showHowTo by remember { mutableStateOf(false) }

    val pulse = rememberInfiniteTransition(label = "bgPulse").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bgAnim"
    ).value

    val start = Pink200
    val mid   = Pink300
    val end   = Pink500
    val lineColor = Pink500.copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f   to start,
                    0.5f to mid,
                    1f   to end
                )
            )
            .drawBehind {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height * (0.25f + 0.02f * pulse)),
                    end = Offset(size.width, size.height * (0.2f + 0.02f * pulse)),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Pink200.copy(alpha = 0.6f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(Modifier.blur(0.dp)) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NeonTitle()
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Hızlı, sade ve keyifli XOX",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Pink500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = onStartGame,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink500,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Oyuna Başla", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(Modifier.height(10.dp))

                    FilledTonalButton(
                        onClick = { showSettings = true },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Yellow500,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Zorluk Seç")
                    }

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = { showHowTo = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = Pink500)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Spacer(Modifier.size(6.dp))
                        Text("Nasıl Oynanır?")
                    }

                    if (showHowTo) {
                        AlertDialog(
                            onDismissRequest = { showHowTo = false },
                            confirmButton = {
                                TextButton(
                                    onClick = { showHowTo = false },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Pink500)
                                ) {
                                    Text("Tamam")
                                }
                            },
                            title = { Text("Nasıl Oynanır?", fontWeight = FontWeight.SemiBold, color = Pink500) },
                            text = {
                                Text(
                                    "Oyuna başlamak için 'Oyuna Başla' butonuna tıklayın. " +
                                            "Hamle yapmak için boş karelere dokunun. Amacınız, yatay," +
                                            " dikey veya çapraz şekilde üç sembolü hizalamaktır.",
                                    color = Color.Black
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            containerColor = Pink200
                        )
                    }
                }
            }
        }

        if (showSettings) {
            DifficultyDialog(
                difficultyState = remember { mutableStateOf(difficulty) },
                onDismiss = { showSettings = false },
                onApply = { newDiff ->
                    difficulty = newDiff
                    onDifficultyChange(difficulty)
                    showSettings = false
                }
            )
        }
    }
}

@Composable
private fun NeonTitle() {
    val pulse by rememberInfiniteTransition(label = "titlePulse").animateFloat(
        initialValue = 0.8f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "titleAnim"
    )
    val textColor = Pink500.copy(alpha = 0.12f)
    Text(
        text = "X O X",
        fontSize = (42 * pulse).sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 6.sp,
        color = Pink500,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 4.dp)
            .drawBehind {
                drawCircle(
                    color = textColor,
                    radius = size.minDimension * 0.7f
                )
            }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DifficultyDialog(
    difficultyState: MutableState<Difficulty>,
    onDismiss: () -> Unit,
    onApply: (Difficulty) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onApply(difficultyState.value) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Pink500,
                    contentColor = Color.White
                )
            ) {
                Text("Uygula")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = Pink500)) {
                Text("İptal")
            }
        },
        title = { Text("Zorluk", fontWeight = FontWeight.SemiBold, color = Pink500) },
        text = {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Difficulty.values().forEach { d ->
                    ChoiceChip(
                        selected = difficultyState.value == d,
                        onClick = { difficultyState.value = d },
                        label = { Text(
                            when (d) {
                                Difficulty.EASY -> "Kolay"
                                Difficulty.MEDIUM -> "Orta"
                                Difficulty.HARD -> "Zor"
                            }
                        ) }
                    )
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Pink200
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        shape = RoundedCornerShape(14.dp),
        leadingIcon = if (selected) {
            { Box(Modifier.size(10.dp).clip(CircleShape).background(Pink500)) }
        } else null
    )
}
