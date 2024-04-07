package com.koleo.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.koleo.core.R as CoreR

@Composable
fun SearchView(
    searchViewActions: SearchScreenActions,
    viewState: State<SearchViewState>
) {
    val isSearchInProgress =
        viewState.value.startDestinationSearchInProgress || viewState.value.endDestinationSearchInProgress

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        AnimatedVisibility(
            visible = !isSearchInProgress,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        searchViewActions.onStartDestinationClicked()
                    }
                    .background(Color(0x60ABABAB))
                    .padding(16.dp),
                    text = viewState.value.startDestination?.name ?: "Start Destination")
                Spacer(modifier = Modifier.height(16.dp))
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        searchViewActions.onEndDestinationClicked()
                    }
                    .background(Color(0x60ABABAB))
                    .padding(16.dp),
                    text = viewState.value.endDestination?.name ?: "End Destination")

                viewState.value.distance?.let { distance ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x60ABABAB))
                            .padding(24.dp),
                        text = "Distance between selected destinations: $distance km"
                    )
                }
            }

        }
        AnimatedVisibility(
            visible = isSearchInProgress,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                val hint = if (viewState.value.startDestinationSearchInProgress) {
                    "Select start destination"
                } else if (viewState.value.endDestinationSearchInProgress) {
                    "Select end destination"
                } else "..."
                SearchBar(searchViewActions, hint)
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewState.value.keywordsResults.count()) { index ->
                        val result = viewState.value.keywordsResults.get(index)
                        Text(
                            text = result.keyword,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                                .clickable {
                                    searchViewActions.onKeywordSelected(result)
                                })

                    }
                }
            }
        }

    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Image(
        painterResource(CoreR.drawable.koleo_logo),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.height(60.dp)
    )
}

@Composable
fun SearchBar(
    searchScreenActions: SearchScreenActions,
    searchHint: String,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val focusRequester = FocusRequester()
    TextField(
        value = text,
        onValueChange = {
            text = it
            searchScreenActions.onSearchInputChange(it.text)
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(text = searchHint)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color(0x60ABABAB),
            focusedLabelColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent

        )
    )
    LaunchedEffect(key1 = null) {
        focusRequester.requestFocus()
    }
}