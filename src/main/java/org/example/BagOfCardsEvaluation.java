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

    public static double accuracy(BagOfCards model, List<deckLoader.DeckPlacements> test)
    {
        double[] rating = new double[test.size()];
        double[] goodness = new double[test.size()];
        for(int i = 0; i< test.size(); i++)
        {
            rating[i] = model.rating(test.get(i).cards);
            goodness[i] = -test.get(i).placement;
        }

    }
}
