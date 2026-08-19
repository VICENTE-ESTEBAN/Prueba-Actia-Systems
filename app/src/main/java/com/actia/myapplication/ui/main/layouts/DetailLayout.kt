package com.actia.myapplication.ui.main.layouts

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.actia.myapplication.R
import com.actia.myapplication.data.domain.model.DetailItem
import com.actia.myapplication.data.domain.model.DetailState
import com.actia.myapplication.data.domain.model.Screen
import com.actia.myapplication.ui.main.viewmodel.MainViewModel1

@Composable
fun DetailScreen(navController: NavHostController,
                 mainViewModel: MainViewModel1, modifier: Modifier){
    //--------------DATA------------------------------------------------
    val uiState = mainViewModel.uiState.collectAsState()
    // INNER STATE -----------------------------------------------------------
    var isVeloVisible by remember { mutableStateOf(false) }
    var canDrawDetails by remember { mutableStateOf(false) }
    //--------------------------------------------------------------------------
    val errorOnRequestText = stringResource(R.string.error_on_request)
    val context = LocalContext.current

    LaunchedEffect(uiState.value) {
        val details = uiState.value.detail
        when(details)
        {
            is DetailState.Success -> {
                canDrawDetails = true
                isVeloVisible = false
            }
            is DetailState.Error -> {
                isVeloVisible = false
                canDrawDetails = false

                Toast.makeText(
                    context,
                    errorOnRequestText,
                    Toast.LENGTH_LONG
                ).show()

                navController.navigate(Screen.Detail.route)
            }
            is DetailState.Loading -> {
                isVeloVisible = true
                canDrawDetails = false
            }

            DetailState.Idle -> {
                isVeloVisible = false
                canDrawDetails = false
            }
        }

    }

    val details = uiState.value.detail
    if (canDrawDetails && details is DetailState.Success) {
        DetailLayout(modifier = modifier.fillMaxSize(), detailItem = details.item)
    }

    ShowVelo(isVeloVisible)
}

@Composable
fun DetailLayout(modifier: Modifier = Modifier, detailItem: DetailItem){
    val lblTitle = stringResource(R.string.lbl_title)
    val lblDirector = stringResource(R.string.lbl_director)
    val lblReleaseYear = stringResource(R.string.lbl_releaseYear)
    val lblDuration = stringResource(R.string.lbl_duration)
    val lblDescription = stringResource(R.string.lbl_description)
    val lblScore = stringResource(R.string.lbl_score)

    @Composable
    fun RowItem(modifier: Modifier, label:String, value:String?){
        Row(modifier = modifier) {
            Text(
                text = label,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 5.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if(value.isNullOrEmpty()) "-" else value,
                fontSize = 18.sp
            )
        }
    }

    Column(modifier = modifier) {
        RowItem(modifier = Modifier.padding(start = 5.dp), label = lblTitle, value = detailItem.title)
        RowItem(modifier = Modifier.padding(5.dp), label = lblDirector, value = detailItem.director)
        RowItem(modifier = Modifier.padding(5.dp), label = lblReleaseYear, value = detailItem.releaseYear)
        RowItem(modifier = Modifier.padding(5.dp), label = lblDuration, value = detailItem.duration)
        RowItem(modifier = Modifier.padding(5.dp), label = lblDescription, value = detailItem.description)
        RowItem(modifier = Modifier.padding(5.dp), label = lblScore, value = detailItem.score)

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PicassoImage(
                url = detailItem.poster,
                contentDescription = detailItem.title,
                modifier = Modifier.width(160.dp)
            )
        }

    }
}