package fr.celestgames.fts.utIls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtil {
    public static String readFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static void writeFile(String path, String content) {
        writeFile(new File(path), content);
    }

    public static void writeFile(File file, String content) {
        String s = file.getPath();

        String[] split = s.split("\\\\");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            sb.append(split[i]);
            sb.append("/");
        }
        File dir = new File(sb.toString());

        try {
            if (dir.mkdirs()) {
                if (file.createNewFile()) {
                    System.out.println("File created");
                }
            }
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}