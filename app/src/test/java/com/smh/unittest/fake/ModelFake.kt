package com.smh.unittest.fake

import com.smh.unittest.domain.CoinModel

internal val btc = CoinModel(
    id = "bitcoin",
    rank = 1,
    image = "https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png?1696501400",
    symbol = "BTC",
    name = "BITCOIN",
    price = 66199.0,
    priceChangePercentage = -0.84324,
    holdingAmount = null
)

internal val eth = CoinModel(
    id = "ethereum",
    rank = 2,
    image = "https://coin-images.coingecko.com/coins/images/279/large/ethereum.png?1696501628",
    symbol = "ETH",
    name = "ETHEREUM",
    price = 3319.38,
    priceChangePercentage = -1.14169,
    holdingAmount = null
)