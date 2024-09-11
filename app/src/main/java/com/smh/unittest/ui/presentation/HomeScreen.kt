package com.smh.unittest.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smh.unittest.domain.CoinModel.Companion.btc
import com.smh.unittest.ui.theme.UnitTestTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchCoins()
    }

    HomeContent(uiState = uiState)
}

@Composable
private fun HomeContent(
    uiState: HomeUiState
) {
    Scaffold {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(
                uiState.coins,
                key = { coin -> coin.id }
            ) { coin ->
                Column {
                    HomeCoinItem(
                        data = coin,
                        showHolding = false,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    UnitTestTheme {
        HomeContent(
            uiState = HomeUiState(coins = listOf(btc))
        )
    }
}