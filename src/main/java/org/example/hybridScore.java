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
    }
}
