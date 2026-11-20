package org.example;
import java.util.*;

public class BagOfCardsEvaluation
{

    private static double[] ranks(double[] values)
    {
        int n = values.length;
        Integer[] order = new Integer[n];
        double[] ranks = new double[n];

        for(int i = 0; i < n; i++)
        {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Double.compare(values[a], values[b]));

        int i =0;
        while(i < n)
        {
            int j = i;
            while(j+1 < n && values[order[j+1]] == values[order[i]])
            {
                j++;
            }
            double avg = (i+j)/2.0+1;
            for(int k = i; k <= j; k++)
            {
                ranks[order[k]] = avg;
            }
            i = j + 1;
        }
        return ranks;
    }

    static double spearman(double[] a, double [] b)
    {
        double[] rankA = ranks(a);
        double[] rankB = ranks(b);

        double meanA = 0;
        double meanB = 0;
        for(int i = 0; i < rankA.length; i++)
        {
            meanA += rankA[i];
            meanB += rankB[i];
        }
        meanA /= rankA.length;
        meanB /= rankB.length;

        double agreement = 0;
        double varA = 0;
        double varB = 0;
        for(int i = 0; i < rankA.length; i++)
        {
            agreement += (rankA[i] - meanA) * (rankB[i] - meanB);
            varA += (rankA[i] - meanA) * (rankA[i] - meanA);
            varB += (rankB[i] - meanB) * (rankB[i] - meanB);
        }

        if(varA == 0 || varB == 0)
        {
            return 0;
        }
        return agreement/Math.sqrt(varA*varB);
    }

    public static double accuracy(BagOfCards model, List<deckLoader.DeckPlacements> test)
    {
        double[] rating = new double[test.size()];
        double[] goodness = new double[test.size()];
        for(int i = 0; i< test.size(); i++)
        {
            rating[i] = model.rating(test.get(i).cards);
            goodness[i] = -test.get(i).placement;
        }
        return spearman(rating, goodness);
    }

    public static List<List<deckLoader.DeckPlacements>> split (List<deckLoader.DeckPlacements> decks, double testShare, long seed)
    {
        List<deckLoader.DeckPlacements> shuffled = new ArrayList<>(decks);
        Collections.shuffle(shuffled, new Random(seed));
        int testSize = (int)Math.round(shuffled.size()*testShare);
        List<deckLoader.DeckPlacements> test = new ArrayList<>(shuffled.subList(0, testSize));
        List<deckLoader.DeckPlacements> training = new ArrayList<>(shuffled.subList(testSize, shuffled.size()));

    }
}
