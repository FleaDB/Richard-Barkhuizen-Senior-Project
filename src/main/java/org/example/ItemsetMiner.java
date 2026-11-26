package org.example;
import java.util.*;

public abstract class ItemsetMiner
{
    protected abstract List<AprioriAlgorithm.Itemset> findItemsets(List<List<String>> transaction, int minCount);

    public List<AprioriAlgorithm.Itemset> mine(List<List<String>> transaction, double minSup)
    {
        int minCount = Math.max(1, (int)Math.ceil((minSup*transaction.size()/100)));
        return findItemsets(transaction, minCount);
    }

    public abstract String name();
}
