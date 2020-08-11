package com.urise.webapp.model;

import java.util.ArrayList;

public class ListContent implements Section{
    private ArrayList<String> content = new ArrayList<>();

    @Override
    public void fillSection(String information) {
        StringBuilder data = new StringBuilder(information);
        while (data.length() > 0){
            content.add(data.substring(0, data.indexOf("\n")));
            data.delete(0, data.indexOf("\n")+1);
        }
    }

    @Override
    public void clearSection() {
        content = null;
    }
}
