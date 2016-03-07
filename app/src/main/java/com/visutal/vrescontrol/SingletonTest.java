package com.visutal.vrescontrol;

/**
 * Created by ksp on 05/03/2016.
 */
public class SingletonTest {
    //private static SingletonTest ourInstance;
    private static SingletonTest ourInstance = new SingletonTest();

    public static SingletonTest getInstance() {
        //if (ourInstance == null) { ourInstance = new SingletonTest(); }
        return ourInstance;
    }

    private SingletonTest() { }

    public int i = 0;
}
