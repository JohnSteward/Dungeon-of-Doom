package com.company;

import java.io.*;
import java.util.ArrayList;

public class Map {
    private String choice;

    public Map(String choice) {
        this.choice = choice;
    }

    //Reads the text file containing the map and places each line into an arraylist.
    //Gets the text file with the absolute path of the input file name.
    public ArrayList<ArrayList<String>> BuildMap(String choice, Player player, Bot bot) {
        File map = new File(this.choice + ".txt");
        ArrayList<String> mapFileContent = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(map.getAbsolutePath()));
            String newRow = reader.readLine();
            while (newRow!= null) {
                mapFileContent.add(newRow);
                newRow = reader.readLine();
            }
        }
        catch (FileNotFoundException e){
           System.out.println("No file found, using default map.");
           mapFileContent.add("name Very Small Labyrinth of Doom");
           mapFileContent.add("win 2");
           mapFileContent.add("###################");
           mapFileContent.add("#.................#");
           mapFileContent.add("#......G........E.#");
           mapFileContent.add("#.................#");
           mapFileContent.add("#..E..............#");
           mapFileContent.add("#..........G......#");
           mapFileContent.add("#.................#");
           mapFileContent.add("#.................#");
           mapFileContent.add("###################");
        }
        catch (IOException e){
            ;
        }
        int goldToWin = Integer.parseInt(mapFileContent.get(1).substring(4));
        int maxY = mapFileContent.size() - 1;
        int maxX = mapFileContent.get(3).length() - 1;

        ArrayList<ArrayList<String>> maps = player.initPos(maxX, maxY, goldToWin, mapFileContent, bot);
        return maps;

    }


    public String getChoice(){
        return choice;
    }
}


