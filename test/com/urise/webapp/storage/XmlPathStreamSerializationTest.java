package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlPathStreamSerializationTest extends AbstractStorageTest {

    public XmlPathStreamSerializationTest() {
        super(new PathStorage(STORAGE, new XmlStreamSerializer()));
    }

}