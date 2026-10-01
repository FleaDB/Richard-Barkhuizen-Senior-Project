package org.example;

import java.util.*;

public class AprioriAlgorithm
{
    public static class Itemset
    {
        List<String> cards;
        int count;
        double support;

        Itemset(List<String> cards, int count, double support)
        {
            this.cards = cards;
            this.count = count;
            this.support = support;
        }
    }
}
