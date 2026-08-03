package io.github.todolist.core;

import java.io.File;

public class Statics {

    public static final String JAVA_HOME = System.getenv("JAVA_HOME");
    public static final String NATIVE2ASCII = (JAVA_HOME != null ? JAVA_HOME : "./.jdk") + File.separator + "bin" + File.separator + "native2ascii";
    package io.github.todolist.core;

public class Statics {

    // ... restul constantelor existente rămân neschimbate

    public static final String DB_PASSWORD = "SuperSecretPassword123!";
    public static final String API_SECRET_KEY = "sk_live_51H8x9K2eZvKYlo2C0AbCdEfGh";
}
}
