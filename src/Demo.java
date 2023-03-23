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
    public static double[][] trainingData, testingData;
    public static int[] trainingTargets, testingTargets;
    public static int trainTestSplit;

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
        trainTestSplit = (int) (data.size() * .8);

        Perceptron p = new Perceptron(11, 0.01);

        // initialize training data
        trainingData = new double[trainTestSplit][11];
        trainingTargets =  new int[trainTestSplit];

        for(int i = 0; i < trainTestSplit; i++) {
            trainingData[i] = data.get(i).getAttributes();
            trainingTargets[i] = data.get(i).getActualQuality();
        }

        // initialize testing data
        testingData = new double[data.size() - trainTestSplit][11];
        testingTargets =  new int[data.size() - trainTestSplit];

        for(int i = 0; i < data.size() - trainTestSplit; i++) {
            testingData[i] = data.get(i + trainTestSplit).getAttributes();
            testingTargets[i] = data.get(i + trainTestSplit).getActualQuality();
        }

        p.train(trainingData, trainingTargets, 100);

        //TODO: test model

        
    }
}
