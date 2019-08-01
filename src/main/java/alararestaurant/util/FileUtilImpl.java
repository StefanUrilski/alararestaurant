package alararestaurant.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtilImpl implements FileUtil {

    @Override
    public String readFile(String filePath) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            fileContent.append(line).append(System.lineSeparator());
        }

        return fileContent.toString();
    }
}
