package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.JsonStreamSerializer;

public class JsonPathStreamSerializationTest extends AbstractStorageTest {

    public JsonPathStreamSerializationTest() {
        super(new PathStorage(STORAGE.getAbsolutePath(), new JsonStreamSerializer()));
    }

}