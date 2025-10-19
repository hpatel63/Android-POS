package com.polaris.hospitalitypos.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.polaris.hospitalitypos.R
import com.polaris.hospitalitypos.feature.copilot.AiAssistantFab
import com.polaris.hospitalitypos.feature.dashboard.DashboardScreen
import com.polaris.hospitalitypos.feature.housekeeping.HousekeepingScreen
import com.polaris.hospitalitypos.feature.reports.ReportsScreen
import com.polaris.hospitalitypos.feature.rooms.RoomsScreen
import com.polaris.hospitalitypos.feature.search.SearchScreen
import com.polaris.hospitalitypos.feature.settings.SettingsScreen

enum class Destinations(val route: String) {
    DASHBOARD("dashboard"),
    ROOMS("rooms"),
    REPORTS("reports"),
    SETTINGS("settings"),
    SEARCH("search"),
    HOUSEKEEPING("housekeeping")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalityNavHost(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val backstackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backstackEntry?.destination?.route
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationRail {
                NavigationRailItem(
                    selected = currentRoute == Destinations.DASHBOARD.route,
                    onClick = {
                        navController.navigate(Destinations.DASHBOARD.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    label = { Text("Dashboard") },
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_dashboard), contentDescription = null) }
                )
                NavigationRailItem(
                    selected = currentRoute == Destinations.ROOMS.route,
                    onClick = {
                        navController.navigate(Destinations.ROOMS.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    label = { Text("Rooms") },
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_rooms), contentDescription = null) }
                )
                NavigationRailItem(
                    selected = currentRoute == Destinations.REPORTS.route,
                    onClick = {
                        navController.navigate(Destinations.REPORTS.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    label = { Text("Reports") },
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_reports), contentDescription = null) }
                )
                NavigationRailItem(
                    selected = currentRoute == Destinations.SETTINGS.route,
                    onClick = {
                        navController.navigate(Destinations.SETTINGS.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    label = { Text("Settings") },
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_settings), contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            floatingActionButton = { AiAssistantFab(navController) },
            topBar = {
                TopAppBar(title = { Text("Hospitality POS") })
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Destinations.DASHBOARD.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                composable(Destinations.DASHBOARD.route) { DashboardScreen(navController) }
                composable(Destinations.ROOMS.route) { RoomsScreen(navController) }
                composable(Destinations.REPORTS.route) { ReportsScreen(navController) }
                composable(Destinations.SETTINGS.route) { SettingsScreen(navController) }
                composable(Destinations.SEARCH.route) { SearchScreen(navController) }
                composable(Destinations.HOUSEKEEPING.route) { HousekeepingScreen(navController) }
            }
        }
    }
}
