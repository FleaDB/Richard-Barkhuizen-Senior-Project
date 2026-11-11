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

        List<Node> singlePath()
        {
            List<Node> path = new ArrayList<>();
            Node cur = root;

            while(!cur.children.isEmpty())
            {
                if(cur.children.size()>1)//check for 2 branches
                {
                    return null;
                }
                cur = cur.children.values().iterator().next();
                path.add(cur);
            }
            return path;
        }
    }

    private HashMap<String, Integer> rankFrequent(List<List<String>> transaction, int minCount)
    {
        HashMap<String, Integer> count = new HashMap<>();
        for(List<String> deck : transaction)
        {
            for(String card : deck)
            {
                count.merge(card, 1, Integer::sum);
            }
        }
        List<String> frequent = new ArrayList<>();
        for(String card : count.keySet())
        {
            if(count.get(card) >= minCount)
            {
                frequent.add(card);
            }
        }
        frequent.sort(Comparator.comparing((String s) -> count.get(s)).reversed().thenComparing(Comparator.naturalOrder()));

        HashMap<String, Integer> rank = new HashMap<>();
        for(int i = 0; i < frequent.size(); i++)
        {
            rank.put(frequent.get(i), i);
        }
        return rank;
    }

    private Tree buildTree(List<List<String>> transaction, HashMap<String, Integer> rank)
    {
        Tree tree = new Tree();
        for(List<String>deck : transaction)
        {
            List<String> cards = new ArrayList<>();
            for(String card : deck)
            {
                if(rank.containsKey(card))
                {
                    cards.add(card);
                }
            }
            cards.sort(Comparator.comparingInt(rank::get));
            tree.insert(cards, 1);
        }
        return tree;
    }
}
