package com.koleo.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.koleo.mylibrary.R
import com.koleo.core.R as CoreR

@Composable
fun SearchView(
    searchViewActions: SearchScreenActions,
    viewState: State<SearchViewState>
) {
    val isSearchInProgress =
        viewState.value.startDestinationSearchInProgress || viewState.value.endDestinationSearchInProgress

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Header()
        AnimatedVisibilityDefault(!isSearchInProgress) {
            MainView(searchViewActions, viewState)
        }
        AnimatedVisibilityDefault(isSearchInProgress) {
            Search(searchViewActions, viewState)
        }
    }
}

@Composable
fun MainView(
    searchViewActions: SearchScreenActions,
    viewState: State<SearchViewState>
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
            text = viewState.value.startDestination?.name
                ?: stringResource(R.string.start_destination))
        Spacer(modifier = Modifier.height(16.dp))
        Text(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                searchViewActions.onEndDestinationClicked()
            }
            .background(Color(0x60ABABAB))
            .padding(16.dp),
            text = viewState.value.endDestination?.name
                ?: stringResource(R.string.end_destination))

        viewState.value.distance?.let { distance ->
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x60ABABAB))
                    .padding(16.dp),
                text = stringResource(
                    R.string.distance_between_selected_destinations_km,
                    distance
                )
            )
        }
    }
}

@Composable
fun Search(
    searchViewActions: SearchScreenActions,
    viewState: State<SearchViewState>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        val hint = if (viewState.value.startDestinationSearchInProgress) {
            stringResource(R.string.select_start_destination)
        } else if (viewState.value.endDestinationSearchInProgress) {
            stringResource(R.string.select_end_destination)
        } else "..."
        SearchBar(searchViewActions, hint)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            if (viewState.value.searchMessage.isEmpty()) {
                items(viewState.value.results.count()) { index ->
                    val result = viewState.value.results.get(index)
                    Text(
                        text = result.name,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .fillMaxSize()
                            .clickable {
                                searchViewActions.onKeywordSelected(result)
                            })
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )

                }
            } else if (viewState.value.searchMessage.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_results_found),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .fillMaxSize(),
                        textAlign = TextAlign.Center,
                    )
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
        modifier = modifier.height(64.dp)
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

        ),
        leadingIcon = {
            Image(
                painterResource(R.drawable.arrow_back_24),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(24.dp)
                    .clickable { searchScreenActions.onBackFromSearch() }
            )
        },
        trailingIcon = {
            Image(
                painterResource(R.drawable.clear_icon_24),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(24.dp)
                    .clickable {
                        searchScreenActions.onClearInput()
                        text = TextFieldValue("")
                    }
            )
        }
    )
    LaunchedEffect(key1 = null) {
        focusRequester.requestFocus()
    }
}

@Composable
fun AnimatedVisibilityDefault(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        content()
    }
}