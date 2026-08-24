package org.ensosurei.trophytrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.ensosurei.trophytrack.ui.theme.pink
import org.ensosurei.trophytrack.ui.theme.surface
import org.ensosurei.trophytrack.ui.theme.white
import org.jetbrains.compose.resources.vectorResource
import trophytrack.shared.generated.resources.Res
import trophytrack.shared.generated.resources.ic_home
import trophytrack.shared.generated.resources.ic_inbox
import trophytrack.shared.generated.resources.ic_library
import trophytrack.shared.generated.resources.ic_plus
import trophytrack.shared.generated.resources.ic_profile

@Composable
fun BarNavigation(
    currentStatus: Int,
    onStatusChange: (Int) -> Unit,
    onFabClick: () -> Unit,
    showFab: Boolean,
    content: @Composable (PaddingValues) -> Unit,
){
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(surface)
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(80.dp)
                        .fillMaxWidth(),
                    color = surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-10).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            NavigationBarItem(
                                selected = (currentStatus == 0),
                                onClick = { onStatusChange(0) },
                                icon = {
                                    Icon(
                                        vectorResource(Res.drawable.ic_home),
                                        contentDescription = "Home"
                                    )
                                },
                                label = { Text("Home") }
                            )
                        }

                        Spacer(Modifier.width(72.dp))

                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            NavigationBarItem(
                                selected = (currentStatus == 1),
                                onClick = { onStatusChange(1) },
                                icon = {
                                    Icon(
                                        vectorResource(Res.drawable.ic_library),
                                        contentDescription = "Library"
                                    )
                                },
                                label = { Text("Library") }
                            )
                        }
                    }
                }
                if(showFab){
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-24).dp)
                            .shadow(elevation = 8.dp, shape = CircleShape)
                            .size(64.dp)
                            .background(pink, shape = CircleShape)
                            .clickable {onFabClick()},
                    ){
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            imageVector = vectorResource(Res.drawable.ic_plus),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ){ paddingValues ->
        content(paddingValues)
    }
}