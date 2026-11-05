package org.example;

import java.util.*;

public class FrequentPatternGrowth
{
    private static class Node
    {
        String card;
        int count;
        Node parent;
        HashMap<String, Node> children = new HashMap<>();

        Node(String card, Node parent)
        {
            this.card = card;
            this.parent = parent;
        }
    }

    private static class Tree
    {

    }
}
