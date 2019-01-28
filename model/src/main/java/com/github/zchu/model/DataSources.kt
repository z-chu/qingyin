package com.github.zchu.model

enum class DataSources {
    MEMORY, DISK, REMOTE;

    companion object {

        fun isCache(dataSources: DataSources): Boolean {
            return dataSources == MEMORY || dataSources == DISK
        }
    }
}
