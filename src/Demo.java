import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import DocumentClasses.*;
import NeuralNetwork.*;
import com.opencsv.exceptions.CsvValidationException;

public class Demo {
    public static final String PATH = "./files/";
    public static ArrayList<Wine> data = new ArrayList<Wine>();
    public static double[] targets;

    public static void processData(String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(PATH+filename))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                lineInArray = lineInArray[0].replaceAll("\\s", "").split(";");
                data.add(new Wine(lineInArray));
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        processData("winequality-white.csv");

        Perceptron p = new Perceptron(11, 0.01);
        double[][] inputs = new double[data.size() - 1][11];
        int[] targets =  new int[data.size() - 1];

        for(int i = 0; i < data.size() - 1; i++){
            inputs[i] = data.get(i).getAttributes();
            targets[i] = data.get(i).getActualQuality();
        }

        p.train(inputs, targets, 100);
    }
}
