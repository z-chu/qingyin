package com.github.zchu.model

data class DataBody<T>(val key: String, val dataSources: DataSources, val body: T?)