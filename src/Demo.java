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
    public static double[] results;

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

        results = new double[testingTargets.length];
        for(int i = 0; i < testingData.length; i++){
            results[i] = p.predict(testingData[i]) >= 0 ? 0 : 1;
        }

        evaluateClusters();
    }

    public static void evaluateClusters() {
        // 2 clusters

        // Cluster 1, which contains indices where result = 1
        int TPCluster1 = 0;
        int FPCluster1 = 0;
        int TNCluster1 = 0;
        int FNCluster1 = 0;

        // Cluster 0, which contains indices where result = 0
        int TPCluster0 = 0;
        int FPCluster0 = 0;
        int TNCluster0 = 0;
        int FNCluster0 = 0;

        for(int i = 0; i < testingTargets.length; i++){
            if(results[i] == testingTargets[i]){
                // result = 1, Actual = 1
                if(results[i] == 1) {
                    TPCluster1 += 1;
                    FPCluster0 += 1;
                }
                // result = 0, Actual = 0
                else{
                    TPCluster0 += 1;
                    FPCluster1 += 1;
                }
            }
            else if(results[i] != testingTargets[i]){
                // result = 1, Actual = 0
                if(results[i] == 1){
                    FPCluster1 += 1;
                    FNCluster0 += 1;
                }
                // result = 0, Actual = 1
                else{
                    FPCluster0 += 1;
                    FNCluster1 += 1;
                }
            }
        }

        System.out.println("Cluster 1");
        System.out.printf("True Positive : %d\n" +
                "True Negative : %d\n" +
                "False Positive : %d\n" +
                "False Negative : %d\n\n",
                TPCluster1, TNCluster1, FPCluster1, FNCluster1);

        System.out.println("Cluster 0");
        System.out.printf("True Positive : %d\n" +
                "True Negative : %d\n" +
                "False Positive : %d\n" +
                "False Negative : %d\n\n",
                TPCluster0, TNCluster0, FPCluster0, FNCluster0);
    }
}
