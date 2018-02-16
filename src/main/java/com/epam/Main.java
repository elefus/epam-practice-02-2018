package com.epam;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("123.txt"))) {
            writer.write("Hello world");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
