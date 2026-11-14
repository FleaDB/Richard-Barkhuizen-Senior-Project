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

    private void addItemset(List<String> cards, int count, int decks, List<AprioriAlgorithm.Itemset> result){
        List<String> sorted = new ArrayList<>(cards);
        Collections.sort(sorted);
        result.add(new AprioriAlgorithm.Itemset(sorted, count, (100.0*count)/decks));
    }

    private void genFromPath(List<Node> path, int from, List<String> chosen,
                             int smallest, List<String> alpha, int decks, List<AprioriAlgorithm.Itemset> result)
    {
        for(int i = 0; i < path.size(); i++)
        {
            Node node = path.get(i);
            chosen.add(node.card);
            int support = Math.min(smallest, node.count);

            List<String> pattern = new ArrayList<>(chosen);
            pattern.addAll(alpha);
            addItemset(pattern, support, decks, result);

            genFromPath(path, i+1, chosen, support, alpha, decks, result);
            chosen.remove(chosen.size()-1);
        }
    }

    private static class Prefixpath//path above node
    {
        List<String> cards;
        int count;

        Prefixpath(List<String> cards, int count){
            this.cards = cards;
            this.count = count;
        }
    }

    private List<Prefixpath> conditionalPatternBase(Tree tree, String card)//need to check incase root check is needed
    {
        List<Prefixpath> base = new ArrayList<>();

        for(Node node: tree.nodes.get(card))
        {
            List<String> cards = new ArrayList<>();
            for(Node p = node.parent; p.card != null; p = p.parent)//go up tree
            {
                cards.add(p.card);
            }
            Collections.reverse(cards);//sets it in order from before
            if(!cards.isEmpty()){
                base.add(new Prefixpath(cards, node.count));
            }
        }
        return base;
    }

    private void fpgrowth (Tree tree, List<String> alpha, int minCount, int decks,
                           List<AprioriAlgorithm.Itemset> result)
    {
        List<Node> path = tree.singlePath();
        if(path != null)
        {
            genFromPath(path, 0, new ArrayList<>(), Integer.MAX_VALUE, alpha, decks, result);
        }
        else
        {
            for(String card: tree.nodes.keySet())
            {
                List<String> beta = new ArrayList<>(alpha);
                beta.add(card);
                addItemset(beta, tree.total.get(card), decks, result);

            }
        }
    }
}
