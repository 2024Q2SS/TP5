package ar.edu.itba.ss;

import java.io.FileReader;
import java.nio.file.Path;
import com.google.gson.Gson;

/**
 * Hello world!
 */

import java.io.FileWriter;

public class App2 {

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
        App2 app = new App2();
        try (FileWriter writer = new FileWriter("obs_output.csv")) {
            writer.write("try_count, mx\n");
            int try_count = 0;
            for (int i = 0; i < 1000; i++) {
                Field field = new Field(app.setUp());
                field.run();
                double pos = field.getMaradona().getPosition().getX();
                double radii = field.getMaradona().getRadius();
                try_count += pos - radii < 0 ? 1 : 0;
                writer.write(try_count + "," + pos + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
