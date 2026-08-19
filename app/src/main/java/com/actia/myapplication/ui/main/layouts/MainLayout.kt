package com.actia.myapplication.ui.main.layouts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.actia.myapplication.data.domain.model.Screen
import com.actia.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel
import com.actia.myapplication.R
import com.actia.myapplication.ui.main.viewmodel.MainViewModel1

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true, locale = "es")
@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel1 = koinViewModel()

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val isMainScreen = currentRoute == Screen.Main.route

    val topBarTitle = getTopBarTitle(currentRoute)

    MyApplicationTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(
                scrollBehavior.nestedScrollConnection
            ),

            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),

                    title = {
                        Text(
                            text = topBarTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },

                    navigationIcon = {
                        if (!isMainScreen) {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    },

                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->

            MainNavGraph(
                navController = navController,
                innerPadding = innerPadding,
                mainViewModel = mainViewModel
            )
        }
    }
}

@Composable
private fun getTopBarTitle(currentRoute: String?): String {

    val topBarTitle = when (currentRoute) {
        Screen.Main.route -> stringResource(R.string.main_screen_title)
        Screen.Detail.route -> stringResource(R.string.detail_screen_title)
        else -> ""
    }
    return topBarTitle
}

@Composable
fun MainNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    mainViewModel: MainViewModel1
){
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ){
        composable(Screen.Main.route){
            MyApplicationTheme {
                SearchScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable(Screen.Detail.route){
            MyApplicationTheme {
                DetailScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
