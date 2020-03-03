package cn.dubby.maven.shade.plugin;

import org.joda.time.DubbyTest;
import org.joda.time.LocalDateTime;

/**
 * Created by yangzheng03 on 2018/1/22.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, this is a application packaged by maven-shade-plugin");

        LocalDateTime localDateTime = new LocalDateTime();
        System.out.println(localDateTime);

        System.out.println(new DubbyTest().toString());
    }

}
