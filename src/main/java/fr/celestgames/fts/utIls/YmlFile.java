package fr.celestgames.fts.utIls;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class YmlFile {
    private final HashMap<String, ArrayList<String>> options = new HashMap<>();

    public YmlFile() {
    }

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
                    String value = split[1];
                    ArrayList<String> values = new ArrayList<>();


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
                    String value = split[1];
                    ArrayList<String> values = new ArrayList<>();


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

    public ArrayList<String> getValues(String key) {
        return options.get(key);
    }

    public String getStringValue(String key) {
        ArrayList<String> values = options.get(key);
        StringBuilder s = new StringBuilder();
        if (values != null) {
            if (values.size() > 1) {
                s.append("[");
                for (int i = 0; i < values.size(); i++) {
                    s.append(values.get(i));
                    if (i < values.size() - 1) {
                        s.append(",");
                    }
                }
                s.append("]");
            } else {
                s = new StringBuilder(values.get(0));
            }
        }
        return s.toString();
    }

    public void addValue(String key, String value) {
        if (options.containsKey(key)) {
            options.get(key).add(value);
        } else {
            ArrayList<String> values = new ArrayList<>();
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
}
