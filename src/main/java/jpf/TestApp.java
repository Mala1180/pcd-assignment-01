package jpf;

import app.controller.Controller;
import app.model.Model;
import app.utils.Event;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class TestApp {


    /**
     * Main method for testing with java pathfinder
     */
    static public void main(String[] args) {
        Model model = new Model();
        model.setParameters(System.getProperty("user.dir"), 5, 1000);
        /*Controller controller = new Controller(model);
        controller.processEvent(Event.START);
*/
        //Path p = new File(".").toPath();
        File f = new File(".");
        //File testDirectory = new File(this.path);

        File[] e = f.listFiles(pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".java") && pathname.isFile();
        });

        System.out.println(Arrays.toString(e));
        /*System.out.println(Arrays.toString(e));
        try (Stream<Path> stream = Files.find(Paths.get(model.getDirectoryPath()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())) {
            System.out.println(model.getDirectoryPath());
            Set<Path> files = stream.filter(file -> file.toString().endsWith(".java")).collect(toSet());
            System.out.println(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
