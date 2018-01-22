>介绍`maven-jar-plugin`，`maven-assembly-plugin`和`maven-shade-plugin`的使用，和他们之间的区别。原文出自:[https://blog.dubby.cn/detail.html?id=9091](https://blog.dubby.cn/detail.html?id=9091)

## 1.maven-jar-plugin

首先，需要配置这个plugin:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib/</classpathPrefix>
                <mainClass>cn.dubby.maven.jar.plugin.Main</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

编写启动类:

```
public static void main(String[] args) {
    System.out.println("Hello, this is a application packaged by maven-jar-plugin");
      LocalDateTime localDateTime = new LocalDateTime();
      System.out.println(localDateTime);
}
```

启动:

```
➜  target git:(master) ✗ java -jar maven-jar-plugin-1.0-SNAPSHOT.jar
Hello, this is a application packaged by maven-jar-plugin
```

但是，如果加上一个依赖呢？

我们加上`joda-time`的依赖:

```
<dependencies>
    <!-- https://mvnrepository.com/artifact/joda-time/joda-time -->
    <dependency>
        <groupId>joda-time</groupId>
        <artifactId>joda-time</artifactId>
        <version>2.9.9</version>
    </dependency>
</dependencies>
```

修改启动类:

```
public static void main(String[] args) {
    System.out.println("Hello, this is a application packaged by maven-jar-plugin");
    LocalDateTime localDateTime = new LocalDateTime();
    System.out.println(localDateTime);
}
```

再运行:

```
➜  target git:(master) ✗ java -jar maven-jar-plugin-1.0-SNAPSHOT.jar
Hello, this is a application packaged by maven-jar-plugin
Exception in thread "main" java.lang.NoClassDefFoundError: org/joda/time/LocalDateTime
        at cn.dubby.maven.jar.plugin.Main.main(Main.java:13)
Caused by: java.lang.ClassNotFoundException: org.joda.time.LocalDateTime
        at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
        at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:331)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
        ... 1 more
```

发现我们增加的依赖并没有找到，怎么办呢？

请注意观察我们上面配置`maven-jar-plugin`时的另外两个配置项:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib/</classpathPrefix>
                <mainClass>cn.dubby.maven.jar.plugin.Main</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

`addClasspath`和`classpathPrefix`表明会把`lib`下的jar作为第三方依赖加入package里，所以我们需要把依赖拷贝到`lib`目录下，我们可以使用`maven-dependency-plugin`:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>copy</id>
            <phase>compile</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>
                    ${project.build.directory}/lib
                </outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

这样我们重新打包再运行:

```
➜  target git:(master) ✗ java -jar maven-jar-plugin-1.0-SNAPSHOT.jar
Hello, this is a application packaged by maven-jar-plugin
2018-01-22T14:48:00.915
```

去target目录下也可以发现多了个lib目录。

原文出自:[https://blog.dubby.cn/detail.html?id=9091](https://blog.dubby.cn/detail.html?id=9091)

## 2.maven-assembly-plugin

先配置plugin:

```
<!-- Maven Assembly Plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>2.4.1</version>
    <configuration>
        <!-- get all project dependencies -->
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
        <!-- MainClass in mainfest make a executable jar -->
        <archive>
            <manifest>
                <mainClass>cn.dubby.maven.assembly.plugin.Main</mainClass>
            </manifest>
        </archive>
    </configuration>
    <executions>
        <execution>
            <id>make-assembly</id>
            <!-- bind to the packaging phase -->
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

启动类同上:

```
public static void main(String[] args) {
    System.out.println("Hello, this is a application packaged by maven-assembly-plugin");
    LocalDateTime localDateTime = new LocalDateTime();
    System.out.println(localDateTime);
}
```

打包运行结果:

```
➜  target git:(master) ✗ java -jar maven-assembly-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
Hello, this is a application packaged by maven-assembly-plugin
2018-01-22T15:44:05.166
```

原文出自:[https://blog.dubby.cn/detail.html?id=9091](https://blog.dubby.cn/detail.html?id=9091)

## 3.maven-shade-plugin

配置如下:

```
<!-- maven-shade-plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>cn.dubby.maven.shade.plugin.Main</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

启动类同上:

```
public static void main(String[] args) {
    System.out.println("Hello, this is a application packaged by maven-shade-plugin");
    LocalDateTime localDateTime = new LocalDateTime();
    System.out.println(localDateTime);
}
```

结果:

```
➜  target git:(master) ✗ java -jar maven-shade-plugin-1.0-SNAPSHOT.jar
Hello, this is a application packaged by maven-shade-plugin
2018-01-22T15:42:19.662
```

原文出自:[https://blog.dubby.cn/detail.html?id=9091](https://blog.dubby.cn/detail.html?id=9091)

## 4.package比较

```
-rw-r--r--  1 teeyoung  staff   622K  1 22 15:43 maven-assembly-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
-rw-r--r--  1 teeyoung  staff   3.1K  1 22 14:47 maven-jar-plugin-1.0-SNAPSHOT.jar
-rw-r--r--  1 teeyoung  staff   622K  1 22 15:42 maven-shade-plugin-1.0-SNAPSHOT.jar
```