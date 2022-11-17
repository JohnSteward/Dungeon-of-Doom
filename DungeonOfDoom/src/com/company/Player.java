package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int x;
    private int y;
    private int gold;

    public Player(int x, int y, int gold){
        this.x = x;
        this.y = y;
        this.gold = gold;
    }

    //Gives the player a random position on the map at the start of the game, and makes an arraylist containing
    //the original map.
    public ArrayList<ArrayList<String>> initPos(int maxX, int maxY, int goldToWin, ArrayList<String> mapFileContent, Bot bot){
        ArrayList<String> originalMap = new ArrayList<>();
        for (String element : mapFileContent){
            originalMap.add(element);
        }
        Random randomX = new Random();
        Random randomY = new Random();
        boolean valid = false;
        while (!valid){
            this.x = randomX.nextInt(maxX + 1);//range from 0 to maxX
            this.y = randomY.nextInt((maxY - 2) + 1) + 2;//range from 2 to maxY
            if (originalMap.get(this.y).charAt(this.x) != '#' && (originalMap.get(this.y).charAt(this.x) != 'G')){
                valid = true;
            }
        }
         StringBuilder playerPos = new StringBuilder(mapFileContent.get(this.y));
         playerPos.setCharAt(this.x, 'P');
         mapFileContent.set(this.y, playerPos.toString());
         ArrayList<ArrayList<String>> maps = bot.initPos(maxX, maxY, mapFileContent, originalMap);
         return maps;

    }

    //Reads the player's input each time it is there turn, and does the command that has been input. Returns a boolean
    // depending on whether or not the game has ended.
    public boolean playerInput(ArrayList<ArrayList<String>> maps, String goldToWin, Bot bot){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String command = "0";
        try{
            command = br.readLine();
            command = command.toUpperCase();
        }
        catch (IOException e){
            return true;
        }
        switch(command){
            case ("HELLO"):
                System.out.println("Gold to win: " + goldToWin);
                break;
            case ("GOLD"):
                System.out.println("Gold owned: " + this.gold);
                break;
            case ("PICKUP"):
                if (maps.get(1).get(this.y).charAt(this.x) == 'G'){
                    this.gold += 1;
                    System.out.println("Success. Gold owned: " + this.gold);
                    StringBuilder noGold = new StringBuilder(maps.get(1).get(this.y));
                    noGold.setCharAt(this.x, '.');
                    maps.get(1).set(this.y, noGold.toString());
                }
                else{
                    System.out.println("Fail");
                }
                break;
            case ("QUIT"):
                if ((maps.get(1).get(this.y).charAt(this.x) == 'E') && (this.gold >= Integer.parseInt(goldToWin))){
                    System.out.println("WIN");
                }
                else{
                    System.out.println("LOSE");
                }
                return true;
            case ("MOVE N"):
                if (maps.get(0).get(this.y - 1).charAt(this.x) == '#'){
                    System.out.println("Fail");
                }
                else if (maps.get(0).get(this.y - 1).charAt(this.x) == 'B'){
                    System.out.println("Success");
                    System.out.println("LOSE");
                    return true;
                }
                else{
                    System.out.println("Success");
                    this.y -= 1;
                    StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y + 1));
                    if (bot.getY() == this.y + 1){
                        aftermath.setCharAt(bot.getX(), 'B');
                    }
                    maps.get(0).set(this.y + 1, aftermath.toString());

                    StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
                    changePos.setCharAt(this.x, 'P');
                    maps.get(0).set(this.y, changePos.toString());

                }
                break;
            case ("MOVE S"):
                if (maps.get(0).get(this.y + 1).charAt(this.x) == '#'){
                    System.out.println("Fail");
                }
                else if (maps.get(0).get(this.y + 1).charAt(this.x) == 'B'){
                    System.out.println("Success");
                    System.out.println("LOSE");
                    return true;
                }
                else{
                    System.out.println("Success");
                    this.y += 1;
                    StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y - 1));
                    if (bot.getY() == this.y - 1){
                        aftermath.setCharAt(bot.getX(), 'B');
                    }
                    maps.get(0).set(this.y - 1, aftermath.toString());


                    StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
                    changePos.setCharAt(this.x, 'P');

                    maps.get(0).set(this.y, changePos.toString());
                }
                break;
            case ("MOVE E"):
                if (maps.get(0).get(this.y).charAt(this.x + 1) == '#'){
                    System.out.println("Fail");
                }
                else if (maps.get(0).get(this.y).charAt(this.x + 1) == 'B'){
                    System.out.println("Success");
                    System.out.println("LOSE");
                    return true;
                }
                else{
                    System.out.println("Success");
                    this.x += 1;
                    StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y));
                    maps.get(0).set(this.y, aftermath.toString());

                    StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
                    changePos.setCharAt(this.x, 'P');
                    if (bot.getY() == this.y){
                        changePos.setCharAt(bot.getX(), 'B');
                    }
                    maps.get(0).set(this.y, changePos.toString());
                }
                break;
            case ("MOVE W"):
                if (maps.get(0).get(this.y).charAt(this.x - 1) == '#'){
                    System.out.println("Fail");
                }
                else if (maps.get(0).get(this.y).charAt(this.x - 1) == 'B'){
                    System.out.println("Success");
                    System.out.println("LOSE");
                    return true;
                }
                else{
                    System.out.println("Success");
                    this.x -= 1;
                    StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y));
                    maps.get(0).set(this.y, aftermath.toString());

                    StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
                    changePos.setCharAt(this.x, 'P');
                    if (bot.getY() == this.y){
                        changePos.setCharAt(bot.getX(), 'B');
                    }
                    maps.get(0).set(this.y, changePos.toString());

                }
                break;
            case ("LOOK"):
                //If the player is at the bottom
                if ((this.y == maps.get(0).size() - 2) && (this.x < maps.get(0).get(this.y).length() - 2) &&
                        (this.x > 1)){
                    for (int i = this.y - 2; i < this.y + 2; i++){
                        for (int j = this.x - 2; j < this.x + 3; j++){
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                    System.out.println("#####");
                }
                //If the player is at the top
                else if ((this.y == 3) && (this.x < maps.get(0).get(this.y).length() - 2) &&
                        (this.x > 1)){
                    System.out.println("#####");
                    for (int i = this.y - 1; i < this.y + 3; i++){
                        for (int j = this.x - 2; j < this.x + 3; j++){
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                }
                //If the player is on the right
                else if ((this.x == maps.get(0).get(this.y).length() - 2) && (this.y > 3) && (this.y < maps.get(0).size() - 2)){
                    for (int i = this.y - 2; i < this.y + 3; i++) {
                        for (int j = this.x - 2; j < this.x + 2; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println('#');
                    }
                }
                //If the player is on the left
                else if ((this.x == 1) && (this.y > 3) && (this.y < maps.get(0).size() - 2)){
                    for (int i = this.y - 2; i < this.y + 3; i++) {
                        System.out.print('#');
                        for (int j = this.x - 1; j < this.x + 3; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                }
                //If the player is on the bottom right
                else if ((this.y == maps.get(0).size() - 2) && this.x == maps.get(0).get(this.y).length() - 2){
                    for (int i = this.y - 2; i < this.y + 2; i++) {
                        for (int j = this.x - 2; j < this.x + 2; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println('#');
                    }
                    System.out.println("#####");
                }
                //If the player is on the bottom left
                else if ((this.y == maps.get(0).size() - 2) && (this.x == 1)){
                    for (int i = this.y - 2; i < this.y + 2; i++) {
                        System.out.print('#');
                        for (int j = this.x - 1; j < this.x + 3; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                    System.out.println("#####");
                }
                //If the player is on the top left
                else if ((this.x == 1) && (this.y == 3)){
                    System.out.println("#####");
                    for (int i = this.y - 1; i < this.y + 3; i++) {
                        System.out.print('#');
                        for (int j = this.x - 1; j < this.x + 3; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                }
                //If the player is on the top right
                else if ((this.y == 3) && (this.x == maps.get(0).get(this.y).length() - 2)){
                    System.out.println("#####");
                    for (int i = this.y - 1; i < this.y + 3; i++) {
                        for (int j = this.x - 2; j < this.x + 2; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("#");
                    }
                }
                //If the player is not on any edge
                else {
                    for (int i = this.y - 2; i < this.y + 3; i++) {
                        for (int j = this.x - 2; j < this.x + 3; j++) {
                            System.out.print(maps.get(0).get(i).charAt(j));
                        }
                        System.out.println("");
                    }
                }
                break;
            default:
                System.out.println("Fail");
        }
        maps = bot.action(maps, this);
        if (this.y == bot.getY() && this.x == bot.getX()){
            return true;
        }


        return false;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }


}
