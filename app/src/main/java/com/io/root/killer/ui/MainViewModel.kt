package com.io.root.killer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.io.root.killer.data.AppInfo
import com.io.root.killer.data.AppRepository
import com.io.root.killer.data.ForceStopEngine
import com.io.root.killer.data.RunState
import com.io.root.killer.data.StopResult
import com.io.root.killer.root.RootManager
import com.io.root.killer.root.RootState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab { USER, SYSTEM }

data class MainUiState(
    val rootState: RootState? = null,
    val isCheckingRoot: Boolean = true,
    val isLoadingApps: Boolean = false,
    val apps: List<AppInfo> = emptyList(),
    val selectedTab: AppTab = AppTab.USER,
    val searchQuery: String = "",
    val selectedPackages: Set<String> = emptySet(),
    val lastResults: List<StopResult> = emptyList(),
    val errorMessage: String? = null
) {
    val hasRoot: Boolean get() = rootState?.granted == true

    val userApps: List<AppInfo> get() = apps.filter { !it.isSystemApp }
    val systemApps: List<AppInfo> get() = apps.filter { it.isSystemApp }

    val visibleApps: List<AppInfo>
        get() {
            val base = if (selectedTab == AppTab.USER) userApps else systemApps
            if (searchQuery.isBlank()) return base
            val q = searchQuery.trim().lowercase()
            return base.filter { it.appName.lowercase().contains(q) || it.packageName.lowercase().contains(q) }
        }

    val backgroundAppsCount: Int get() = apps.count { it.runState == RunState.BACKGROUND }
    val totalAppsCount: Int get() = apps.size
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        initialize()
    }

    fun initialize() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingRoot = true, errorMessage = null) }
            val rootState = RootManager.requestRoot(getApplication())
            _uiState.update { it.copy(rootState = rootState, isCheckingRoot = false) }
            if (rootState.granted) {
                refreshApps()
            }
        }
    }

    fun refreshApps() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingApps = true, errorMessage = null) }
            try {
                val apps = repository.loadApps()
                _uiState.update {
                    it.copy(
                        apps = apps,
                        isLoadingApps = false,
                        // Drop selections for packages that no longer exist or are no longer background.
                        selectedPackages = it.selectedPackages.intersect(
                            apps.filter { a -> a.runState == RunState.BACKGROUND }.map { a -> a.packageName }.toSet()
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoadingApps = false, errorMessage = "Couldn't load app list: ${e.message}")
                }
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleSelection(app: AppInfo) {
        if (app.isProtected || app.runState != RunState.BACKGROUND) return
        _uiState.update { state ->
            val newSet = state.selectedPackages.toMutableSet()
            if (!newSet.add(app.packageName)) newSet.remove(app.packageName)
            state.copy(selectedPackages = newSet)
        }
    }

    /** Selects every eligible (unprotected, background) app in the current tab + search filter. */
    fun selectAllVisible() {
        _uiState.update { state ->
            val eligible = state.visibleApps
                .filter { !it.isProtected && it.runState == RunState.BACKGROUND }
                .map { it.packageName }
            state.copy(selectedPackages = state.selectedPackages + eligible)
        }
    }

    fun deselectAll() {
        _uiState.update { it.copy(selectedPackages = emptySet()) }
    }

    fun forceStopSingle(app: AppInfo) {
        viewModelScope.launch {
            val result = ForceStopEngine.forceStop(app)
            _uiState.update { it.copy(lastResults = listOf(result)) }
            refreshApps()
        }
    }

    fun forceStopSelected() {
        viewModelScope.launch {
            val state = _uiState.value
            val targets = state.apps.filter { it.packageName in state.selectedPackages }
            if (targets.isEmpty()) return@launch
            val results = ForceStopEngine.forceStopMany(targets)
            _uiState.update { it.copy(lastResults = results, selectedPackages = emptySet()) }
            refreshApps()
        }
    }

    fun forceStopAllBackground() {
        viewModelScope.launch {
            val targets = _uiState.value.apps.filter {
                it.runState == RunState.BACKGROUND && !it.isProtected
            }
            if (targets.isEmpty()) return@launch
            val results = ForceStopEngine.forceStopMany(targets)
            _uiState.update { it.copy(lastResults = results, selectedPackages = emptySet()) }
            refreshApps()
        }
    }

    fun dismissResults() {
        _uiState.update { it.copy(lastResults = emptyList()) }
    }
}
