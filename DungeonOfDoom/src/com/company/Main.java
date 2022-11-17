package com.company;


import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Main game = new Main();
        game.initPlayers(game);
    }

    //Creates an instance of the player and the bot classes, and gives the player the amount of gold it needs to win.
    public void initPlayers(Main game){
        Player player = new Player(0,0,0);
        Bot bot = new Bot(0, 0, 0, false, true, new int[]{0, 0});
        ArrayList<ArrayList<String>> maps = game.initMap(player, bot);
        boolean finished = false;
        String goldToWin = maps.get(0).get(1).substring(4);
        while (!finished) {
            finished = player.playerInput(maps, goldToWin, bot);
        }
    }

    //Initialises the map by going into the BuildMap method in the Map class, returns a list containing
    //the current state of the map, and the map in its original state.
    public ArrayList<ArrayList<String>> initMap(Player player, Bot bot){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "0";
            System.out.println("Please input the file name of your map file (without extension): ");
            try {
                input = br.readLine();
                Map map1 = new Map(input);
                ArrayList<ArrayList<String>> maps = map1.BuildMap(map1.getChoice(), player, bot);
                return maps;
                }
             catch (IOException e) {
                System.out.println("");
            }
            return new ArrayList<>();
        }

    }

