package com.sam.network_logger.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.network_logger.data.source.local.LoggerDatabase

@Composable
fun LogsSheet(
    materialTheme: MaterialTheme
) {
    var isSheetOpen by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val data by LoggerDatabase.getDatabase(LocalContext.current).networkCallDao()
        .getAllNetworkCallFlow()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    ModalBottomSheet(
        onDismissRequest = { isSheetOpen = false },
        sheetState = bottomSheetState,
    ) {
        LazyColumn(
            modifier = Modifier.navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = data) { networkCall ->
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = materialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val (timeRef, urlRef, methodRef, typeRef) = createRefs()

                        Column(
                            modifier = Modifier
                                .constrainAs(timeRef) {
                                    start.linkTo(parent.start, margin = 16.dp)
                                    top.linkTo(timeRef.top)
                                    bottom.linkTo(methodRef.bottom)
                                }
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    color = materialTheme.colorScheme.primary.copy(alpha = 0.6F),
                                    shape = RoundedCornerShape(24.dp),
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                text = networkCall.responseCode ?: "0"
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                text = "${networkCall.timeTaken} ms",
                            )
                        }

                        Text(
                            modifier = Modifier.constrainAs(urlRef) {
                                top.linkTo(parent.top, margin = 16.dp)
                                end.linkTo(parent.end, margin = 16.dp)
                                start.linkTo(timeRef.end, margin = 16.dp)
                                bottom.linkTo(methodRef.top, margin = 12.dp)
                                width = Dimension.fillToConstraints
                            },
                            text = networkCall.requestUrl ?: "0",
                            textAlign = TextAlign.Start,
                        )

                        Text(
                            modifier = Modifier.constrainAs(methodRef) {
                                start.linkTo(timeRef.end, margin = 16.dp)
                                bottom.linkTo(parent.bottom, margin = 16.dp)
                                top.linkTo(urlRef.bottom, margin = 12.dp)
                            },
                            text = networkCall.requestMethod ?: "0",
                            fontWeight = FontWeight.W800,
                        )

                        Text(
                            text = networkCall.responseContentType ?: "0",
                            modifier = Modifier.constrainAs(typeRef) {
                                end.linkTo(parent.end, margin = 16.dp)
                                bottom.linkTo(parent.bottom, margin = 16.dp)
                            },
                        )
                    }
                }
            }
        }
    }
}