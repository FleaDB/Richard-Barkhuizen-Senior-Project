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

    private void addCardFeature(List<deckLoader.DeckPlacements> training)
    {
        TreeSet<String> cards = new TreeSet<>();
        for(deckLoader.DeckPlacements deck : training)
        {
            cards.addAll(deck.cards);
        }
        for(String card : cards)
        {
            cardFeature.put(card, featureNames.size());
            featureNames.add(card);
        }
    }

    private void addPairfeatures(List<AprioriAlgorithm.Itemset> itemsets, int maxPairs)
    {
        List<AprioriAlgorithm.Itemset> pairs = new ArrayList<>();
        for(AprioriAlgorithm.Itemset s : itemsets)
        {
            if(s.cards.size() == 2 && cardFeature.containsKey(s.cards.get(0)) && cardFeature.containsKey(s.cards.get(1)))
            {
                pairs.add(s);
            }
        }
        pairs.sort((a, b) ->{
            if(a.count != b.count)
            {
                return b.count - a.count;
            }
            return a.cards.toString().compareTo(b.cards.toString());
        });
        for(int i = 0; i < pairs.size() && i < maxPairs; i++)
        {
            List<String> cards = new ArrayList<>(pairs.get(i).cards);
            Collections.sort(cards);
            pairFeatures.add(new String[]{cards.get(0), cards.get(1)});
            pairFeatureNum.add(featureNames.size());
            featureNames.add(cards.get(0) + " + " + cards.get((1)));
        }
    }

    private int[] activeFeatures(Set<String> cards)
    {
        List<Integer> active = new ArrayList<>();
        for(String card : cards)
        {
            Integer feature = cardFeature.get(card);
            if(feature != null)
            {
                active.add(feature);
            }
        }
        for(int i = 0; i < pairFeatures.size(); i++)
        {
            String[] pair = pairFeatures.get(i);
            if(cards.contains(pair[0]) && cards.contains(pair[1]))
            {
                active.add(pairFeatureNum.get(i));
            }
        }

        int[] result = new int[active.size()];
        for(int i = 0; i < result.length; i++)
        {
            result[i] = active.get(i);
        }
        return result;
    }

    public void train(List<deckLoader.DeckPlacements> training, int topCut)
    {
        int n = training.size();
        int[][] active = new int [n][];
        double[] label = new double[n];
        for(int i = 0; i < n; i++)
        {
            active[i] = activeFeatures(training.get(i).cards);
            label[i] = training.get(i).placement <= topCut ? 1 : 0;
        }

        weights = new double[featureNames.size()];
        bias = 0;

        for(int train_pass = 0; train_pass < TRAINING_PASSES; train_pass++)
        {
            double [] gradient = new double[weights.length];
            double biasGradient = 0;

            for(int i = 0; i < n; i++)
            {
                double z = bias;
                for(int f : active[i])
                {
                    z += weights[f];
                }
                double p = 1.0/ (1.0 + Math.exp(-z));

                double error = p - label[i];
                biasGradient += error;
                for(int f : active[i])
                {
                    gradient[f] += error;
                }
            }

            for(int f = 0; f < weights.length; f++)
            {
                weights[f] -= LEARNING_RATE * (gradient[f] / n + LAMBDA * weights[f]);
            }
            bias -= LEARNING_RATE * biasGradient / n;
        }
    }

    public double rating(Set<String> cards)
    {
        double sum = 0;
        for(int f: activeFeatures(cards))
        {
            sum += weights[f];
        }
        return sum;
    }

    public List<String> topFeatures(int n)
    {
        List<Integer> order = new ArrayList<>();
        for(int i = 0; i < weights.length; i++)
        {
            order.add(i);
        }
        order.sort((a, b) -> Double.compare(weights[b], weights[a]));
        List<String> result = new ArrayList<>();

        for(int j = 0; j < n && j < order.size(); j++)
        {
            int i = order.get(j);
            result.add(String.format("%.3f %s", weights[i], featureNames.get(i)));
        }
        return result;
    }

    public int featureCount()
    {
        return featureNames.size();
    }

    public BagOfCards(List<deckLoader.DeckPlacements> trainingData, List<AprioriAlgorithm.Itemset> itemsets, int topCut, int maxPairs)
    {
        addCardFeature(trainingData);
        addPairfeatures(itemsets, maxPairs);
        train(trainingData, topCut);
    }
}
