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

        return ranks;
    }
}
