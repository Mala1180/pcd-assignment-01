package reader;

import java.io.*;
import java.util.List;

public class Entry {
    public static void main (String[] args) throws IOException {

        String javaFilesPath = System.getProperty("user.dir") + "/sources";
        System.out.println("Java Files Directory => " + javaFilesPath);
        MyFileReader reader = new MyFileReader(javaFilesPath);
        reader.printFiles();
        System.out.println("Java Files Number => " + reader.getJavaFiles().length);
        System.out.println("Java Files Number => " + reader.getJavaFiles().length);
        File[] files = reader.getJavaFiles();
        List<File> list = List.of(files);
        list.forEach(file -> {
            BufferedReader buffReader = null;
            int lines = 0;

            try {
                buffReader = new BufferedReader(new FileReader(file.getPath()));
                while (buffReader.readLine() != null) lines++;
                buffReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Java File Lines Number => " + lines);
        });

    }
}
