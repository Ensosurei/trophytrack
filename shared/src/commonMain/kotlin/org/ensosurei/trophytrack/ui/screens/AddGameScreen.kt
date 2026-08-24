package org.ensosurei.trophytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.ensosurei.trophytrack.database.GameDao
import org.ensosurei.trophytrack.database.GameEntity
import org.ensosurei.trophytrack.ui.components.CategoryChip
import org.ensosurei.trophytrack.ui.theme.surface
import org.ensosurei.trophytrack.ui.theme.surfaceVariant
import org.ensosurei.trophytrack.ui.theme.white
import org.jetbrains.compose.resources.vectorResource
import trophytrack.shared.generated.resources.Res
import trophytrack.shared.generated.resources.ic_arrowBack
import trophytrack.shared.generated.resources.ic_camera
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameScreen(
    gameId: Int,
    gameDao: GameDao,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier
) {
    val isNewGame = gameId == -1

    var titleText by remember { mutableStateOf("") }
    var hoursText by remember { mutableStateOf("0") }
    var platformText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("PLAYING") }
    var gameFromDb by remember { mutableStateOf<GameEntity?>(null) }

    var finalCover by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single
    ){ file ->
        if(file != null){
            scope.launch {
                val selectedBytes = file.readBytes()
                finalCover = saveImageLocalStorage(selectedBytes)
            }
        }
    }

    LaunchedEffect(gameId){
        if (!isNewGame) {
            val game = gameDao.getGameById(gameId)
            if(game != null){
                gameFromDb = game
                titleText = game.title
                hoursText = game.hoursPlayed.toString()
                selectedStatus = if(game.status == "NONE") "PLAYING" else game.status
                finalCover = game.coverUrl
                platformText = game.platforms ?: ""
            }
        }
    }

    val isManualGame = isNewGame|| gameFromDb?.origin == "MANUAL"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = surface)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrowBack),
                    contentDescription = null,
                    tint = white
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isNewGame || (gameFromDb?.status == "NONE")) "Add New Game" else "Edit Game",
                color = white,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (finalCover.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .width(180.dp)
                    .height(240.dp)
                    .then(
                        if (isManualGame) Modifier.clickable { launcher.launch() } else Modifier
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    model = finalCover,
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .width(180.dp)
                    .height(240.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                        .then(
                            if (isManualGame) Modifier.clickable { launcher.launch() } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_camera),
                            contentDescription = null
                        )
                        Text(
                            text = if (isManualGame) "Upload Image" else "No Cover",
                            color = white,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = titleText,
            onValueChange = { if (isManualGame) titleText = it },
            label = { Text("Game title") },
            enabled = isManualGame,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = hoursText,
            onValueChange = { hoursText = it },
            label = { Text("Played Hours") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        if (isManualGame) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Platforms",
                    color = white,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val availablePlatforms = listOf("PC", "PS5", "Xbox Series", "Nintendo Switch", "Mobile")
                    availablePlatforms.forEach { platform ->
                        val isSelected = platformText.contains(platform)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) surfaceVariant else surfaceVariant.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (isSelected) {
                                        platformText = platformText
                                            .split(", ")
                                            .filter { it != platform && it.isNotEmpty() }
                                            .joinToString(", ")
                                    } else {
                                        platformText = if (platformText.isEmpty()) platform else "$platformText, $platform"
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = platform,
                                color = if (isSelected) white else white.copy(0.6f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = platformText,
                onValueChange = {},
                label = { Text("Platforms") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Select a status",
                style = MaterialTheme.typography.bodyMedium,
                color = white
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val statuses = listOf("PLAYING", "COMPLETED")
                statuses.forEach { status ->
                    CategoryChip(
                        text = status,
                        isSelected = selectedStatus == status,
                        onClick = { selectedStatus = status }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val hours = hoursText.toFloatOrNull() ?: 0f
                val actualTime = Clock.System.now().toEpochMilliseconds()
                val updateDate = if (isNewGame) actualTime else (gameFromDb?.addedAt ?: actualTime)

                val updatedGame = GameEntity(
                    id = if (isNewGame) 0 else gameId,
                    coverUrl = finalCover,
                    title = titleText,
                    hoursPlayed = hours,
                    status = selectedStatus,
                    platforms = platformText,
                    origin = if (isNewGame) "MANUAL" else (gameFromDb?.origin ?: "MANUAL"),
                    externalId = if (isNewGame) "" else (gameFromDb?.externalId ?: ""),
                    addedAt = updateDate,
                    updateAt = actualTime
                )

                scope.launch {
                    if (isNewGame) {
                        gameDao.insertGame(updatedGame)
                    } else {
                        gameDao.saveGame(updatedGame)
                    }
                    onSaveSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Save Collection")
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

fun saveImageLocalStorage(bytes: ByteArray): String {
    val fileName = "cover_${System.currentTimeMillis()}.png"
    val tmpdir = System.getProperty("java.io.tmpdir") ?: "."
    val targetFile = java.io.File(tmpdir,fileName)
    targetFile.writeBytes(bytes)
    return targetFile.absolutePath
}