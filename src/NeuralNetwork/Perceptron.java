package NeuralNetwork;

import java.util.Random;

/**
 * Perceptron class...
 * Restrictions:
 *      Input data must be numeric
 *      Can only handle binary classification
 */
public class Perceptron {
    private double[] weights;
    private double bias;
    private double learningRate;

    /**
     *
     * @param numInputs the amount of attributes in the dataset
     * @param learningRate dictates how quickly our weights and bias update
     */
    public Perceptron(int numInputs, double learningRate) {
        weights = new double[numInputs];
        bias = 0.0;
        this.learningRate = learningRate;
        Random rand = new Random();

        // initialize weights to random values for first iteration
        for (int i = 0; i < numInputs; i++) {
            weights[i] = rand.nextDouble();
        }
    }

    /**
     *
     * @param input the values of attributes for one entry from the dataset
     * @return the probability that the entry belongs to the positive class
     */
    public double predict(double[] input) {
        double sum = bias;

        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * input[i];
        }

        return sigmoid(sum);
    }

    /**
     *
     * @param inputs an array representing all the entries in the dataset
     * @param targets the correct classification for the corresponding entry (should be 1 or 0)
     * @param numEpochs the number of iterations to train
     */
    public void train(double[][] inputs, int[] targets, int numEpochs) {
        for (int epoch = 0; epoch < numEpochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                double[] input = inputs[i];
                int y = targets[i];
                double delta = predict(input);
                double error = y - delta;

                if(error == 0)
                    break;

                bias += learningRate * error;

                for (int j = 0; j < weights.length; j++) {
                    weights[j] += learningRate * error * input[j];
                }
            }
        }
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}
