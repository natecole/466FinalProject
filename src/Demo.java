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
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        processData("winequality-white.csv");
        trainTestSplit = (int) (data.size() * .8);

        Perceptron p = new Perceptron(11, 0.1);

        int[] rand = new Random().ints(0, data.size()).distinct().limit(data.size()).toArray();
        TrainTestSplit(rand);

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
            results[i] = p.predict(testingData[i]) >= 0.5 ? 1 : 0;
        }

        evaluateClusters();
        confusionMatrix();
    }

    public static void TrainTestSplit(int[] rand){
        ArrayList<Wine> negativeClass = new ArrayList<>();
        ArrayList<Wine> positiveClass = new ArrayList<>();

        for(int i = 0; i < trainTestSplit; i++){
            Wine dataPt = data.get(rand[i]);
            if(dataPt.getActualQuality() == 0){
                negativeClass.add(dataPt);
            }
            else{
                positiveClass.add(dataPt);
            }
        }

        int sizeNeg = negativeClass.size();
        int sizePos = positiveClass.size();
        int sizeOfTraining = Math.max(sizeNeg, sizePos);
        int[] randSamples = new Random().ints(0, sizeOfTraining)
                .distinct().limit(sizeOfTraining).toArray();

        // initialize training data
        trainingData = new double[sizeOfTraining * 2][11];
        trainingTargets =  new int[sizeOfTraining * 2];

        int j = 0;
        for(int i = 0; i < sizeOfTraining; i++) {
            trainingData[j] = positiveClass.get(randSamples[i] % sizePos).getAttributes();
            trainingTargets[j] = positiveClass.get(randSamples[i] % sizePos).getActualQuality();
            j += 1;
            trainingData[j] = negativeClass.get(randSamples[i] % sizeNeg).getAttributes();
            trainingTargets[j] = negativeClass.get(randSamples[i] % sizeNeg).getActualQuality();
            j += 1;
        }
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
