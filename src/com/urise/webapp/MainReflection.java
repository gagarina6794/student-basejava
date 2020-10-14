package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
       Resume resume = new Resume("AAA");
        Method reflectionMethod = resume.getClass().getMethod("toString");
        System.out.println(reflectionMethod.invoke(resume));
        System.out.println(resume.toString());
    }
}
