package com.actia.myapplication.ui.main.layouts

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.actia.myapplication.R
import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.data.domain.model.ItemsState
import com.actia.myapplication.data.domain.model.Screen
import com.actia.myapplication.ui.main.viewmodel.MainViewModel1
import com.actia.myapplication.util.Constants.SHOW_ALL_YEARS
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel1,
    modifier: Modifier) {

    //--------------DATA------------------------------------------------
    val uiState = mainViewModel.uiState.collectAsState()
    //--------------------------------------------------------------------------
    // INNER STATE -----------------------------------------------------------
    var isVeloVisible by remember { mutableStateOf(false) }
    var isDataDialogOpened by remember { mutableStateOf(false) }
    var noDataDialogShown by remember { mutableStateOf(false) }
    //--------------------------------------------------------------------------
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val errorOnRequestText = stringResource(R.string.error_on_request)
    val noData = stringResource(R.string.no_data)

    LaunchedEffect(uiState.value) {
        when(val uiStateItems = uiState.value.items) {
            is ItemsState.Loading -> {
                isVeloVisible = true
            }

            is ItemsState.Error -> {
                isVeloVisible = false

                Toast.makeText(
                    context,
                    errorOnRequestText,
                    Toast.LENGTH_LONG
                ).show()
            }

            is ItemsState.Success -> {
                isVeloVisible = false

                if (uiStateItems.filteredItems.isEmpty() &&
                    !noDataDialogShown
                ) {
                    noDataDialogShown = true
                    isDataDialogOpened = true
                }
            }

            is ItemsState.Idle -> {
                isVeloVisible = false
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        ControlsToSearch(
            onSearchTextChanged = { textToSearch ->
                isVeloVisible = true
                noDataDialogShown = false
                isDataDialogOpened = false
                scope.launch {
                    mainViewModel.loadItems(textToSearch)
                }
            }
        )


        val uiItemsState = uiState.value.items
        if (uiItemsState is ItemsState.Success) {
            val yearsData = mainViewModel.getYears()

            if(uiItemsState.filteredItems.isNotEmpty()){
                SpinnerControl(
                    preselected = if (uiItemsState.selectedYear.isNullOrEmpty()) SHOW_ALL_YEARS else uiItemsState.selectedYear,
                    list = yearsData,
                    onSelectionChanged = { selectedYear ->
                        mainViewModel.setSelectedYear(selectedYear)
                    },
                    modifier = Modifier
                )


                ListOfResultItems(
                    listOfItems = uiItemsState.filteredItems,
                    onItemSelected = { itemSelected ->
                        scope.launch {
                            if (!mainViewModel.canGetDetail(itemSelected)) {
                                Toast.makeText(
                                    context,
                                    errorOnRequestText,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                navController.navigate(Screen.Detail.route)
                            }
                        }
                    }
                )
            }
        }
    }

    ShowVelo(isVeloVisible)

    if (isDataDialogOpened) {
        ShowAlertDialog(
            dialogText = noData,
            onDismissRequest = { isDataDialogOpened = false }
        )
    }
}

@Composable
private fun ControlsToSearch(
    onSearchTextChanged:  (String) -> Unit
){
    val textState = rememberTextFieldState(initialText = "")
   Row(modifier = Modifier.padding(top = 5.dp)){
       val btnSearchCaption = stringResource(R.string.btn_search_caption)
       val hintEdittextTitle = stringResource(R.string.hint_edittext_title)
       val titleMandatory = stringResource(R.string.title_mandatory)
       val context = LocalContext.current
       TextField(
           state = textState,
           label = { Text(hintEdittextTitle) }
       )

       Button(
           onClick = {
               val textToSearch = textState.text as String

               if (textToSearch.isEmpty()) {
                   Toast.makeText(context, titleMandatory, Toast.LENGTH_LONG).show()
                   return@Button
               }
               onSearchTextChanged(textState.text as String)
           }
       ) {
           Text(btnSearchCaption)
       }
   }
}

@Composable
private fun ListOfResultItems(
    listOfItems: List<Item>,
    onItemSelected: (Item) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(listOfItems) { item ->
            ResultItemRow(item,
                onItemSelected = { onItemSelected(item) })
        }
    }
}

@Composable
private fun ResultItemRow(
    item: Item,
    onItemSelected: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).clickable{
                onItemSelected()
            },
        verticalAlignment = Alignment.CenterVertically,

    ) {

        PicassoImage(
            url = item.poster,
            contentDescription = item.title,
            modifier = Modifier
                .width(60.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title.orEmpty(),
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = item.releaseYear.orEmpty(),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "IMDb: ${item.imdb.orEmpty()}",
                fontSize = 14.sp
            )
        }
    }
}
