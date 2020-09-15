package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.DataStreamSerializer;

public class DataStreamSerializationTest extends AbstractStorageTest {

    public DataStreamSerializationTest() {
        super(new PathStorage(STORAGE.getAbsolutePath(), new DataStreamSerializer()));
    }

}