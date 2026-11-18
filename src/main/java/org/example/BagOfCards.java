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
}
