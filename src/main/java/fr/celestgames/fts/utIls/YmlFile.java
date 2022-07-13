package fr.celestgames.fts.utIls;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class YmlFile {
    private final HashMap<String, HashSet<String>> options = new HashMap<>();

    public YmlFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            int count = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                String[] split = line.split(":");
                if (split.length == 2) {
                    String value = split[1].trim();
                    HashSet<String> values = new HashSet<>();

                    if (value.startsWith("[")) {
                        value = value.substring(1, value.length() - 1);
                        values.addAll(Arrays.asList(value.split(",")));
                    } else {
                        values.add(value);
                    }

                    options.put(split[0], values);
                } else {
                    System.out.println("Error in line " + count + ": " + line);
                }
                count++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public YmlFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            int count = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                String[] split = line.split(":");
                if (split.length == 2) {
                    String value = split[1].trim();
                    HashSet<String> values = new HashSet<>();

                    if (value.startsWith("[")) {
                        value = value.substring(1, value.length() - 1);
                        values.addAll(Arrays.asList(value.split(",")));
                    } else {
                        values.add(value);
                    }

                    System.out.println(split[0] + ": ");
                    for (String s : values) {
                        System.out.println(s);
                    }

                    options.put(split[0], values);
                } else {
                    System.out.println("Error in line " + count + ": " + line);
                }
                count++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public YmlFile() {

    }

    public HashSet<String> getValues(String key) {
        return options.get(key);
    }

    public String getStringValue(String key) {
        HashSet<String> values = options.get(key);
        StringBuilder s = new StringBuilder();
        if (values != null) {
            if (values.size() > 1) {
                s.append("[");
                for (String value : values) {
                    s.append(value).append(", ");
                }
                s.append("]");
            } else {
                s = new StringBuilder();
                s.append(values.toArray()[0]);
            }
        }
        return s.toString();
    }

    public void addValue(String key, String value) {
        if (options.containsKey(key)) {
            options.get(key).add(value);
        } else {
            HashSet<String> values = new HashSet<>();
            values.add(value);
            options.put(key, values);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String key : options.keySet()) {
            s.append(key).append(": ").append(getStringValue(key)).append("\n");
        }
        return s.toString();
    }

    public HashMap<String, HashSet<String>> getOptions() {
        return options;
    }
}
