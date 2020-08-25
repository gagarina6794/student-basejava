package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;
import com.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerializer implements StorageSerialization{
    private XmlParser xmlParser;

    public XmlStreamSerializer(){
        xmlParser = new XmlParser(Resume.class, Organization.class, OrganizationSection.class, Organization.Experience.class,
                BulletedListSection.class, SimpleTextSection.class, Link.class);
    }

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try(Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)){
            xmlParser.marshall(resume,writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)){
            return xmlParser.unmarshall(reader);
        }
    }
}
