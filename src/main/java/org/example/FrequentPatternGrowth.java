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
    {//page 188
        Node root = new Node(null, null);
        HashMap<String, List<Node>> nodes = new HashMap<>();
        HashMap<String, Integer> total = new HashMap<>();

        void insert(List<String> path, int count)
        {
            Node cur = root;
            for(String card : path)
            {
                Node child = cur.children.get(card);
                //https://dl.acm.org/doi/pdf/10.1145/342009.335372
                if(child == null)
                {
                    child = new Node(card, cur);
                    cur.children.put(card, child);
                    nodes.computeIfAbsent(card, k -> new ArrayList<>()).add(child);
                }
                child.count += count;
                total.merge(card, count, Integer::sum);
                cur = child;
            }
        }
    }
}
