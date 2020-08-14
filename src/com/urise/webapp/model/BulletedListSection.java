package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class BulletedListSection implements Section {
    private List<String> content = new ArrayList<>();

    public BulletedListSection(String information) {
        StringBuilder data = new StringBuilder(information);
        while (data.length() > 0) {
            content.add(data.substring(0, data.indexOf("\n")));
            data.delete(0, data.indexOf("\n") + 1);
        }
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
