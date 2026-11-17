/*
Basic class to load the data, and print it.

Attribute names must match the corresponding key names in the JSON file exactly,
since Gson matches JSON keys to Java fields by name.
Ref: https://medium.com/@alexandre.therrien3/personalized-serializer-and-deserializer-using-java-gson-library-c079de3974d4
*/

package org.example;

import com.google.gson.Gson;

import javax.smartcardio.Card;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class deckLoader
{
    static class CardData
    {
        String card_id;
        int count;
    }

    static class DeckData{
        String leader_card_code;
        List<CardData> deck;
        String date;
        String format;
        String placement;
    }

    public static class DeckPlacements
    {
        Set<String> cards;
        int placement;

        DeckPlacements(Set<String> cards, int placement)
        {
            this.cards = cards;
            this.placement = placement;
        }
    }

    private final List<DeckData> decks;

    public deckLoader(String path) throws IOException
    {
        Gson gson = new Gson();

        try(FileReader reader = new FileReader(path)){
            DeckData[] deckArray = gson.fromJson(reader, DeckData[].class);
            decks = new ArrayList<>(Arrays.asList(deckArray));
        }
    }

    public void printDecks()
    {
        for(int i = 0; i < decks.size(); i++){
            DeckData d = decks.get(i);
            System.out.println("\nDeck "+(i+1)+"\nLeader: "+d.leader_card_code+"\nFormat: "+d.format+
                    "\nPlayer Placement: "+d.placement+"\nDate of Tournament: "+d.date);
            for(CardData c : d.deck)
            {
                System.out.println(" "+c.count+"x"+c.card_id);
            }
        }
    }

    public HashMap<String, Integer> getLeaderCounts(){
        HashMap<String, Integer> counts = new HashMap<>();

        for(DeckData d : decks)
        {
            String curLeader = d.leader_card_code;
            if(counts.containsKey(curLeader))
            {
                counts.put(curLeader, counts.get(curLeader)+1);
            }
            else
            {
                counts.put(curLeader, 1);
            }
        }

        return counts;
    }

    //return unique cards for itemsets
    public List<List<String>> getTransaction(String leader)
    {
        List<List<String>> transaction = new ArrayList<>();

        for(DeckData d : decks)
        {
            if(leader.equals(d.leader_card_code))
            {
                List<String> id = new ArrayList<>();
                for(CardData c : d.deck)
                {
                    if(!id.contains(c.card_id))
                    {
                        id.add(c.card_id);
                    }
                }
                transaction.add(id);
            }
        }
        return transaction;
    }

    public List<DeckPlacements> getPlacementDeck(String leader)
    {
        List<DeckPlacements> result = new ArrayList<>();

        for(DeckData d : decks)
        {
            if(leader.equals(d.leader_card_code) && d.placement != null)
            {
                int place;
                try
                {
                    place = Integer.parseInt(d.placement.trim());
                }
                catch(NumberFormatException e)
                {
                    continue;
                }

                Set<String> cards = new HashSet<>();
                for(CardData c : d.deck)
                {
                    cards.add(c.card_id);
                }
                result.add(new DeckPlacements(cards, place));
            }
        }
        return result;
    }
}
