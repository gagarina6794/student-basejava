package com.urise.webapp.model;

import java.util.ArrayList;

public class ListContent implements Section{
    private ArrayList<String> content = new ArrayList<>();

    public ListContent(String information) {
        StringBuilder data = new StringBuilder(information);
        while (data.length() > 0){
            content.add(data.substring(0, data.indexOf("\n")));
            data.delete(0, data.indexOf("\n")+1);
        }
    }
}
