package com.sam.network_logger.ui.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.network_logger.data.source.local.LoggerDatabase
import com.sam.network_logger.data.source.local.entity.NetworkCall
import com.sam.network_logger.helpers.startListeningToAccelerometer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LogsSheet(
    materialTheme: MaterialTheme
) {
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedNetworkCall: NetworkCall? by remember { mutableStateOf(null) }
    val isDetailView by remember { derivedStateOf { selectedNetworkCall != null } }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val networkDao = remember { LoggerDatabase.getDatabase(context).networkCallDao() }

    val data by networkDao.getAllNetworkCallFlow()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    BackHandler(enabled = isDetailView) { selectedNetworkCall = null }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            startListeningToAccelerometer(context, event) { isSheetOpen = true }
            if (event == Lifecycle.Event.ON_CREATE) {
                scope.launch(Dispatchers.IO) {
                    if (!networkDao.getAllNetworkCall().isNullOrEmpty()) {
                        networkDao.nukeNetworkCalls()
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (isSheetOpen)
        ModalBottomSheet(
            onDismissRequest = { /*isSheetOpen = false*/ },
            sheetState = bottomSheetState,
        ) {
            Crossfade(
                targetState = isDetailView,
                label = "NetworkLoggingInterceptor"
            ) { shouldView ->
                if (shouldView) {
                    LogDetail(
                        materialTheme = materialTheme,
                        selectedNetworkCall = selectedNetworkCall,
                        onClick = { selectedNetworkCall = null }
                    )
                } else {
                    LogList(
                        materialTheme = materialTheme,
                        data = data,
                        onItemClick = { selectedNetworkCall = it }
                    )
                }
            }
        }
}