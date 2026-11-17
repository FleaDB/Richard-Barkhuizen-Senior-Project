package org.example;

import java.util.*;

public class hybridScore
{
    public class HybridScore
    {
        private List<Set<String>> decks;
        private double alpha;
        private HashMap<String, Integer> single = new HashMap<>();
        private HashMap<String, HashMap<String, Integer>> pairs = new HashMap<>(); //double hash for card id and how many decks play them

        //https://dl.acm.org/doi/pdf/10.1145/170036.170072
        public HybridScore(List<Set<String>> decks, double alpha)
        {
            this.decks = decks;
            this. alpha = alpha;

            for(Set<String> deck : decks)
            {
                for(String a : deck)
                {
                    single.merge(a, 1, Integer::sum);
                    HashMap<String, Integer> row = pairs.computeIfAbsent(a, k_->new HashMap());
                    for(String b : deck)
                    {
                        if(!a.equals(b))
                        {
                            row.merge(b, 1, Integer::sum);
                        }
                    }
                }
            }
        }
    }
}
