package com.actia.myapplication.ui.main.layouts

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun PicassoImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    var imageBitmap by remember(url) {
        mutableStateOf<ImageBitmap?>(null)
    }

    var aspectRatio by remember(url) {
        mutableStateOf<Float?>(null)
    }

    var hasError by remember(url) {
        mutableStateOf(false)
    }

    DisposableEffect(url) {
        if (url.isNullOrBlank()) {
            imageBitmap = null
            hasError = true

            onDispose { }
        } else {
            imageBitmap = null
            hasError = false

            val target = object : Target {

                override fun onBitmapLoaded(
                    bitmap: Bitmap,
                    from: Picasso.LoadedFrom
                ) {
                    imageBitmap = bitmap.asImageBitmap()
                    aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                    hasError = false
                }

                override fun onBitmapFailed(
                    e: Exception?,
                    errorDrawable: Drawable?
                ) {
                    imageBitmap = null
                    aspectRatio = null
                    hasError = true
                }

                override fun onPrepareLoad(
                    placeHolderDrawable: Drawable?
                ) {
                    imageBitmap = null
                    aspectRatio = null
                }
            }

            Picasso.get()
                .load(url)
                .into(target)

            onDispose {
                Picasso.get().cancelRequest(target)
            }
        }
    }

    val defaultAspectRatio = 0.67f
    val ratio = aspectRatio ?: defaultAspectRatio
    Box(
        modifier = modifier.aspectRatio(ratio).background(
            if (hasError) Color.Red else Color.Blue
        )
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun SpinnerControl(
    modifier: Modifier = Modifier,
    preselected:String = "",
    list: List<String>,
    onSelectionChanged: (String) -> Unit,
) {

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    //.wrapContentWidth()  //optional instad of fillMaxWidth
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ShowVelo(isVisible: Boolean){
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    enabled = false,
                    onClick = {}
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit = {},
    dialogTitle: String = "",
    dialogText: String
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
    )
}