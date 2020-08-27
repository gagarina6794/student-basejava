package com.urise.webapp.model;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Writable{
    void writeCollection(DataOutputStream dos) throws IOException;
}
