package ar.edu.itba.ss;

import java.io.FileReader;
import java.nio.file.Path;
import com.google.gson.Gson;

/**
 * Hello world!
 */

public class App {

    private static final String absPath = System.getProperty("user.dir");
    private static final String confPath = "../config.json";
    private static final Gson Gson = new Gson();

    public Config setUp() {
        String filePath = Path.of(absPath, confPath).toString();
        Config conf = null;
        try (FileReader reader = new FileReader(filePath)) {
            conf = Gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return conf;
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        App app = new App();
        Field field = new Field(app.setUp());
        field.run();
        System.out.println("Hello World!");

    }
}
