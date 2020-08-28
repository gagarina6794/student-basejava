package com.urise.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapStorageTest.class,
        MapResumeStorageTest.class,
        ObjectFileStreamSerializationTest.class,
        ObjectFileStreamSerializationTest.class,
        XmlPathStreamSerializationTest.class,
        JsonPathStreamSerializationTest.class,
        DataStreamSerializationTest.class

})
public class AllStorageTests {
}
