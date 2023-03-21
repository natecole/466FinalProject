package com.mkyong.io.csv.opencsv;

import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import NeuralNetwork.*;
import project.FinalProject466.DocumentClasses.*;

public class Demo {
    public static final String PATH = "";
    public static ArrayList<Wine> data = new ArrayList<Wine>();
    public static double[] targets;

    public static void processData(String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(PATH+filename))) {
            String[] lineInArray;
            while ((lineInArray = reader.nextLine().replaceAll
            ("\\s", "").split(";")) != null) {
                data.add(new Wine(lineInArray));
            }
        }
    }

    public static void main(String[] args) {
        processData("winequality-white.csv");
        
    }
}
