package org.example;
import java.util.*;

public class BagOfCards
{
    private static final int TRAINING_PASSES = 200;
    private static final double LEARNING_RATE = 0.5;
    private static final double LAMBDA = 0.1;

    private List<String> featureNames = new ArrayList<>();
    private HashMap<String, Integer> cardFeature = new HashMap<>(); //this is card id, in the sample game its different
    private List<String[]> pairFeatures = new ArrayList<>(); //the actual pair
    private List<Integer> pairFeatureNum = new ArrayList<>(); //the number of occurences of that given pair

    private double[] weights;
    private double bias;
    
}
