package com.io.root.killer.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.foundation.Image
import com.io.root.killer.data.AppInfo
import com.io.root.killer.data.RunState
import com.io.root.killer.data.StopResult
import com.io.root.killer.ui.theme.AccentAmber
import com.io.root.killer.ui.theme.AccentBlue
import com.io.root.killer.ui.theme.AccentGreen
import com.io.root.killer.ui.theme.AccentRed

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Header()
        RootStatusBanner(
            isChecking = state.isCheckingRoot,
            hasRoot = state.hasRoot,
            managerLabel = state.rootState?.manager?.label,
            message = state.rootState?.message,
            onRetry = viewModel::initialize
        )

        if (state.hasRoot) {
            StatsRow(total = state.totalAppsCount, background = state.backgroundAppsCount)

            SearchBar(query = state.searchQuery, onQueryChange = viewModel::updateSearch)

            TabRow(selectedTabIndex = if (state.selectedTab == AppTab.USER) 0 else 1) {
                Tab(
                    selected = state.selectedTab == AppTab.USER,
                    onClick = { viewModel.selectTab(AppTab.USER) },
                    text = { Text("User Apps (${state.userApps.size})") }
                )
                Tab(
                    selected = state.selectedTab == AppTab.SYSTEM,
                    onClick = { viewModel.selectTab(AppTab.SYSTEM) },
                    text = { Text("System Apps (${state.systemApps.size})") }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoadingApps) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppList(
                        apps = state.visibleApps,
                        selected = state.selectedPackages,
                        onToggle = viewModel::toggleSelection,
                        onForceStopOne = viewModel::forceStopSingle
                    )
                }
            }

            ActionBar(
                selectedCount = state.selectedPackages.size,
                onSelectAll = viewModel::selectAllVisible,
                onDeselectAll = viewModel::deselectAll,
                onStopSelected = viewModel::forceStopSelected,
                onStopAllBackground = viewModel::forceStopAllBackground
            )
        } else if (!state.isCheckingRoot) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Root access is required to list and stop background apps.",
                    modifier = Modifier.padding(32.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    if (state.lastResults.isNotEmpty()) {
        ResultsDialog(results = state.lastResults, onDismiss = viewModel::dismissResults)
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Shield, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text("Root App Killer", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun RootStatusBanner(
    isChecking: Boolean,
    hasRoot: Boolean,
    managerLabel: String?,
    message: String?,
    onRetry: () -> Unit
) {
    val (icon, tint, text) = when {
        isChecking -> Triple(null, AccentBlue, "Checking root access…")
        hasRoot -> Triple(Icons.Filled.CheckCircle, AccentGreen, "Rooted via ${managerLabel ?: "unknown manager"}")
        else -> Triple(Icons.Filled.Error, AccentRed, message ?: "Root access not available")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isChecking) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
        } else if (icon != null) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        if (!isChecking && !hasRoot) {
            TextButton(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun StatsRow(total: Int, background: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(label = "Installed", value = total.toString(), modifier = Modifier.weight(1f))
        StatCard(label = "Background", value = background.toString(), modifier = Modifier.weight(1f), accent = AccentAmber)
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier, accent: Color = AccentBlue) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = accent)
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        placeholder = { Text("Search apps or package name") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        singleLine = true
    )
}

@Composable
private fun AppList(
    apps: List<AppInfo>,
    selected: Set<String>,
    onToggle: (AppInfo) -> Unit,
    onForceStopOne: (AppInfo) -> Unit
) {
    if (apps.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No apps match.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(apps, key = { it.packageName }) { app ->
            AppRow(
                app = app,
                isSelected = selected.contains(app.packageName),
                onToggle = { onToggle(app) },
                onForceStop = { onForceStopOne(app) }
            )
            Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
        }
    }
}

@Composable
private fun AppRow(
    app: AppInfo,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onForceStop: () -> Unit
) {
    val canSelect = !app.isProtected && app.runState == RunState.BACKGROUND
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = canSelect, onClick = onToggle)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isSelected, onCheckedChange = { onToggle() }, enabled = canSelect)

        val bitmap = app.icon?.toBitmap(width = 96, height = 96)
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(horizontal = 4.dp)
            )
        } else {
            Box(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    app.appName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (app.isProtected) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Filled.Lock, contentDescription = "Protected", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
            }
            Text(
                app.packageName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            StatusChip(app = app)
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = onForceStop,
            enabled = !app.isProtected && app.runState != RunState.FOREGROUND,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed)
        ) {
            Text("Stop", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun StatusChip(app: AppInfo) {
    val (label, color) = when {
        app.isProtected -> "Protected" to MaterialTheme.colorScheme.onSurfaceVariant
        app.runState == RunState.FOREGROUND -> "Foreground" to AccentGreen
        app.runState == RunState.BACKGROUND -> "Background" to AccentAmber
        else -> "Stopped" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = color)
        if (app.isSystemApp) {
            Spacer(modifier = Modifier.width(8.dp))
            Text("System", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ActionBar(
    selectedCount: Int,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onStopSelected: () -> Unit,
    onStopAllBackground: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onSelectAll, modifier = Modifier.weight(1f)) { Text("Select All") }
            OutlinedButton(onClick = onDeselectAll, modifier = Modifier.weight(1f)) { Text("Deselect All") }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onStopSelected,
            enabled = selectedCount > 0,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
        ) {
            Text(if (selectedCount > 0) "Force Stop Selected ($selectedCount)" else "Force Stop Selected")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onStopAllBackground,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = AccentRed)
        ) {
            Text("Force Stop All Background Apps")
        }
    }
}

@Composable
private fun ResultsDialog(results: List<StopResult>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } },
        title = { Text("Force-stop results") },
        text = {
            Column {
                val succeeded = results.count { it.success }
                Text(
                    "$succeeded of ${results.size} completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(260.dp)) {
                    items(results) { r ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Icon(
                                if (r.success) Icons.Filled.CheckCircle else Icons.Filled.Error,
                                contentDescription = null,
                                tint = if (r.success) AccentGreen else AccentRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(r.appName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                Text(r.detail, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    )
}
