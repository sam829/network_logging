package com.sam.network_logger.ui.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.network_logger.data.source.local.LoggerDatabase
import com.sam.network_logger.data.source.local.entity.NetworkCall
import com.sam.network_logger.helpers.startListeningToAccelerometer

@Composable
fun LogsSheet(
    materialTheme: MaterialTheme
) {
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedNetworkCall: NetworkCall? by remember { mutableStateOf(null) }
    val isDetailView by remember { derivedStateOf { selectedNetworkCall != null } }

//    val scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val networkDao = remember { LoggerDatabase.getDatabase(context).networkCallDao() }

    val data by networkDao.getAllNetworkCallFlow()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    BackHandler(enabled = isDetailView || isSheetOpen) {
        if (!isDetailView) {
            isSheetOpen = false
        }
        if (isDetailView) {
            selectedNetworkCall = null
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            startListeningToAccelerometer(context, event) { isSheetOpen = true }
            if (event == Lifecycle.Event.ON_CREATE) {
                /*scope.launch(Dispatchers.IO) {
                    if (!networkDao.getAllNetworkCall().isNullOrEmpty()) {
                        networkDao.nukeNetworkCalls()
                    }
                }*/
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (isSheetOpen)
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Network Calls") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                isSheetOpen = false
                                selectedNetworkCall = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Crossfade(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                targetState = isDetailView,
                label = "NetworkLoggingInterceptor",
            ) { shouldView ->
                if (shouldView) {
                    LogDetail(
                        materialTheme = materialTheme,
                        selectedNetworkCall = selectedNetworkCall,
                        onClick = { selectedNetworkCall = null }
                    )
                } else {
                    var query by rememberSaveable { mutableStateOf("") }
                    var active by rememberSaveable { mutableStateOf(false) }

                    Column {
                        SearchBar(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 12.dp)
                                .zIndex(1F),
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = { active = false },
                            active = active,
                            onActiveChange = { active = it },
                            placeholder = { Text("Search URL") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                        ) {
                            LogList(
                                materialTheme = materialTheme,
                                data = data.filter { it.requestUrl?.contains(query) == true }
                                    .reversed(),
                                onItemClick = { selectedNetworkCall = it }
                            )
                        }
                        LogList(
                            materialTheme = materialTheme,
                            data = data.reversed(),
                            onItemClick = { selectedNetworkCall = it }
                        )
                    }
                }
            }
        }
}