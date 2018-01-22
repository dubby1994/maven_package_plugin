package cn.dubby.maven.jar.plugin;

import org.joda.time.LocalDateTime;

/**
 * Created by yangzheng03 on 2018/1/22.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, this is a application packaged by maven-jar-plugin");

        LocalDateTime localDateTime = new LocalDateTime();
        System.out.println(localDateTime);
    }

}
