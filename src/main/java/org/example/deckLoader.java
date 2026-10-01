/*
Basic class to load the data, and print it.

Attribute names must match the corresponding key names in the JSON file exactly,
since Gson matches JSON keys to Java fields by name.
Ref: https://medium.com/@alexandre.therrien3/personalized-serializer-and-deserializer-using-java-gson-library-c079de3974d4
*/

package org.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    //return unique cards for itemsets
    public List<String> getTransaction()
    {
        List<String> transaction = new ArrayList<>();

        for(DeckData d : decks){
            for(CardData c : d.deck)
            {
                if(!transaction.contains(c.card_id))
                {
                    transaction.add(c.card_id);
                }
            }
        }
        return transaction;
    }
}
