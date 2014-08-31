package com.jab.proxy.web_service.core;

public enum StorageClient {
    INSTANCE;

    // TODO: Add support for permanent data storage provider

    public DataProvider getDataProvider() {
//        return InMemoryDataProvider.INSTANCE;
        return RedisDataProvider.INSTANCE;
    }
}
