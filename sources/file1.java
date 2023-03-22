package reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileReader {

    private final String path;

    public FileReader(String path) {
        this.path = path;
    }

    public void printFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(this.path))) {
            paths.filter(Files::isRegularFile).forEach(System.out::println);
        }
    }
}
