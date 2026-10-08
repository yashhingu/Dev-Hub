package dev.yashh.devhub

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.yashh.devhub.core.designsystem.theme.DevHubTheme
import dev.yashh.devhub.navigation.DevHubNavHost
import dev.yashh.devhub.navigation.TopLevelDestination

@Composable
fun DevHubApp() {
    DevHubTheme {
        val navController = rememberNavController()
        
        // Track current destination for bottom nav state
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            bottomBar = {
                DevHubBottomBar(
                    currentDestination = currentDestination,
                    onNavigateToDestination = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                DevHubNavHost(navController = navController)
            }
        }
    }
}

@Composable
private fun DevHubBottomBar(
    currentDestination: NavDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = destination.titleTextId)
                    )
                },
                label = { Text(stringResource(id = destination.titleTextId)) },
                alwaysShowLabel = true
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination): Boolean {
    val routeQualifiedName = destination.route::class.qualifiedName ?: destination.route.toString()
    val routeSimpleName = destination.route::class.simpleName ?: destination.route.toString()
    return this?.hierarchy?.any { navDest ->
        val destRoute = navDest.route ?: ""
        destRoute == destination.route.toString() ||
        destRoute.contains(routeQualifiedName) ||
        destRoute.contains(routeSimpleName)
    } ?: false
}
