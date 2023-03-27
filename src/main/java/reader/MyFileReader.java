package reader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MyFileReader {

    private final String path;

    public MyFileReader(String path) {
        this.path = path;
    }

   /* gli N sorgenti con il numero maggiore di linee di codice
    La distribuzione complessiva relativa a quanti sorgenti hanno un numero di linee di codice che ricade in un certo intervallo, considerando un certo numero di intervalli NI  e un numero massimo MAXL  di linee di codice per delimitare l'estremo sinistro dell'ultimo intervallo.
    Esempio: se NI = 5 e MAXL è 1000, allora il primo intervallo è [0,249],il secondo è  [250,499], il terzo è  [500,749], il quarto è [750,999], l'ultimo è [1000,infinito]. La distribuzione determina quanti sorgenti ci sono per ogni intervallo
*/
    public void printJavaFiles() throws IOException {
        File testDirectory = new File(this.path);
        File[] files = testDirectory.listFiles(pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".java") && pathname.isFile();
        });
        System.out.println(files);
    }

    public File[] getJavaFiles() throws IOException {
        File testDirectory = new File(this.path);
        return testDirectory.listFiles(pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".java") && pathname.isFile();
        });
    }

    public void printFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(this.path))) {
            paths.filter(Files::isRegularFile).forEach(System.out::println);
        }
    }
}
