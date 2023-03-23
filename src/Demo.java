import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import DocumentClasses.*;
import NeuralNetwork.*;
import com.opencsv.exceptions.CsvValidationException;

public class Demo {
    public static final String PATH = "./files/";
    public static ArrayList<Wine> data = new ArrayList<Wine>();
    public static ArrayList<Wine> positiveClass = new ArrayList<>();
    public static ArrayList<Wine> negativeClass = new ArrayList<>();
    public static double[][] trainingData, testingData;
    public static int[] trainingTargets, testingTargets;
    public static int trainTestSplit;
    public static double[] results;

    public static void processData(String filename) {
        try (CSVReader reader = new CSVReader(new FileReader(PATH+filename))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                lineInArray = lineInArray[0].replaceAll("\\s", "").split(";");
                Wine w = new Wine(lineInArray);
                data.add(w);
                if (w.quality > 5) {
                    positiveClass.add(w);
                } else {
                    negativeClass.add(w);
                }
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        processData("winequality-white.csv");
        trainTestSplit = (int) (data.size() * .8);

        Perceptron p = new Perceptron(11, 0.1);

        // initialize training data
        trainingData = new double[trainTestSplit][11];
        trainingTargets =  new int[trainTestSplit];

        int[] rand = new Random().ints(0, data.size()).distinct().limit(data.size()).toArray();
        int ones = 0;
        int zeroes = 0;
        for(int i = 0; i < trainTestSplit; i++) {
            trainingData[i] = data.get(rand[i]).getAttributes();
            int value = data.get(rand[i]).getActualQuality();
            trainingTargets[i] = value;
            if(Math.abs(ones - zeroes) > 100){
                ArrayList<Wine> sampleFrom = ones > zeroes ? negativeClass : positiveClass;
                int get = new Random().nextInt(sampleFrom.size());
                trainingData[i] = sampleFrom.get(get).getAttributes();
                value = sampleFrom.get(get).getActualQuality();
                trainingTargets[i] = value;
            }
            if(value == 0){
                zeroes += 1;
            }
            else{
                ones += 1;
            }
        }

        // initialize testing data
        testingData = new double[data.size() - trainTestSplit][11];
        testingTargets =  new int[data.size() - trainTestSplit];

        for(int i = 0; i < data.size() - trainTestSplit; i++) {
            testingData[i] = data.get(rand[i + trainTestSplit]).getAttributes();
            testingTargets[i] = data.get(rand[i + trainTestSplit]).getActualQuality();
        }

        p.train(trainingData, trainingTargets, 1000);

        results = new double[testingTargets.length];
        for(int i = 0; i < testingData.length; i++){
            results[i] = p.predict(testingData[i]);
        }

        evaluateClusters();
        confusionMatrix();
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
                    TNCluster0 += 1;
                }
                // result = 0, Actual = 0
                else{
                    TPCluster0 += 1;
                    TNCluster1 += 1;
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

        double prec = (double)TPCluster1 / (TPCluster1 + FPCluster1);
        double recall = (double)TPCluster1 / (TPCluster1 + FNCluster1);
        System.out.printf("Precision : %f\n", prec);
        System.out.printf("Recall : %f\n", recall);
        System.out.printf("F1 : %f\n\n", prec * recall / (prec + recall));

        System.out.println("Cluster 0");
        System.out.printf("True Positive : %d\n" +
                "True Negative : %d\n" +
                "False Positive : %d\n" +
                "False Negative : %d\n\n",
                TPCluster0, TNCluster0, FPCluster0, FNCluster0);

        prec = (double)TPCluster0 / (TPCluster0 + FPCluster0);
        recall = (double)TPCluster0 / (TPCluster0 + FNCluster0);
        System.out.printf("Precision : %f\n", prec);
        System.out.printf("Recall : %f\n", recall);
        System.out.printf("F1 : %f\n\n", prec * recall / (prec + recall));
    }

    public static void confusionMatrix() {
        int CM00 = 0;
        int CM01 = 0;
        int CM10 = 0;
        int CM11 = 0;

        for (int i = 0; i < testingTargets.length; i++) {
            if(results[i] == testingTargets[i]){
                // result = 1, Actual = 1
                if(results[i] == 1) {
                    CM11 += 1;
                }
                // result = 0, Actual = 0
                else{
                    CM00 += 1;
                }
            }
            else if(results[i] != testingTargets[i]){
                // result = 1, Actual = 0
                if(results[i] == 1){
                    CM10 += 1;
                }
                // result = 0, Actual = 1
                else{
                    CM01 += 1;
                }
            }
        }

        System.out.println("Confusion Matrix\n");
        System.out.println("\t\t\tActual\n");
        System.out.print("\t\t\t1\t0\n");
        System.out.printf("\t\t1\t%d\t%d\n", CM11, CM10);
        System.out.println("Pred");
        System.out.printf("\t\t0\t%d\t%d\n", CM01, CM00);
    }
}
