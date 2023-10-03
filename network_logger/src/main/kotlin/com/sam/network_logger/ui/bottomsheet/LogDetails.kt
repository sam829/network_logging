package com.sam.network_logger.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sam.network_logger.data.source.local.entity.NetworkCall

@Composable
fun LogDetail(
    materialTheme: MaterialTheme,
    selectedNetworkCall: NetworkCall? = null,
    onClick: () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.navigationBarsPadding(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Row {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    text = selectedNetworkCall?.requestUrl ?: "0",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = materialTheme.colorScheme.background)
                    .padding(12.dp),
                text = "Request"
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DataCell(
                    materialTheme = materialTheme,
                    title = "Request URL",
                    data = selectedNetworkCall?.requestUrl
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Request Method",
                    data = selectedNetworkCall?.requestMethod
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Request Headers",
                    data = selectedNetworkCall?.requestHeader
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Request Body",
                    data = selectedNetworkCall?.requestBody
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Content Length",
                    data = selectedNetworkCall?.requestContentLength?.toString()
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Content Type",
                    data = selectedNetworkCall?.requestContentType
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Tag",
                    data = selectedNetworkCall?.requestTag
                )
            }
        }
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = materialTheme.colorScheme.background)
                    .padding(12.dp),
                text = "Response"
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DataCell(
                    materialTheme = materialTheme,
                    title = "Response Code",
                    data = selectedNetworkCall?.responseCode
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Response Headers",
                    data = selectedNetworkCall?.responseHeaders
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Response Message",
                    data = selectedNetworkCall?.responseMessage
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Response Protocol",
                    data = selectedNetworkCall?.responseProtocol
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Content Length",
                    data = selectedNetworkCall?.responseContentLength?.toString()
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Content Type",
                    data = selectedNetworkCall?.responseContentType
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Challenge",
                    data = selectedNetworkCall?.responseContentType
                )
                DataCell(
                    materialTheme = materialTheme,
                    title = "Response Body",
                    data = selectedNetworkCall?.responseBody
                )
            }
        }
    }
}

@Composable
private fun DataCell(materialTheme: MaterialTheme, title: String?, data: String?) {
    Text(text = title ?: "null", style = materialTheme.typography.titleMedium)
    Text(text = data ?: "null", style = materialTheme.typography.bodyMedium)
}