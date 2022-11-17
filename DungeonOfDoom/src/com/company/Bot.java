package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Bot{
    private int x;
    private int y;
    private int look;
    private boolean seen;
    private boolean optimal;
    private int[] lastPos;

    public Bot(int x, int y, int look, boolean seen, boolean optimal, int[] lastPos){
        this.x = x;
        this.y = y;
        this.look = look;
        this.seen = seen;
        this.optimal = optimal;
        this.lastPos = lastPos;
    }

    //Initialises the bot's position on the map nd makes sure it is not on a wall or on the player
    //returns the original map and the new state of the map.
    public ArrayList<ArrayList<String>> initPos(int maxX, int maxY, ArrayList<String> mapFileContent, ArrayList<String> originalMap){
        Random randomX = new Random();
        Random randomY = new Random();
        boolean valid = false;
        while (!valid){
            this.x = randomX.nextInt(maxX + 1);//range from 0 to maxX
            this.y = randomY.nextInt((maxY - 2) + 1) + 2;//range from 2 to maxY
            if (mapFileContent.get(this.y).charAt(this.x) != '#' && (mapFileContent.get(this.y).charAt(this.x) != 'G')
                && mapFileContent.get(this.y).charAt(this.x) != 'P'){
                valid = true;
            }
        }
        StringBuilder botPos = new StringBuilder(mapFileContent.get(this.y));
        botPos.setCharAt(this.x, 'B');
        mapFileContent.set(this.y, botPos.toString());
        ArrayList<ArrayList<String>> maps = new ArrayList<>();
        maps.add(mapFileContent);
        maps.add(originalMap);
        return maps;
    }

    //Generates the bot's input. It moves randomly and looks every 3 turns, until it sees the player.
    //When it sees the player, it will move towards the position that it last saw them.
    //Returns the original map and the new state of the map.
    public ArrayList<ArrayList<String>> action(ArrayList<ArrayList<String>> maps, Player player) {
        this.look += 1;
        if (this.look > 2){
            this.look = 0;
        }
        ArrayList<Character> actionList = new ArrayList<>();
        actionList.add('N');
        actionList.add('S');
        actionList.add('W');
        actionList.add('E');
        boolean valid = false;
        Random act = new Random();
        char movement = ' ';
        while (!valid) {
            if (!this.seen || !this.optimal){
                int move = act.nextInt(4);
                if (look != 2) {
                    movement = actionList.get(move);
                }
                else{
                    movement = 'L';
                }
                switch (movement) {
                    case 'N':
                        if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                            maps = moveN(maps, player);
                            valid = true;
                            return maps;
                        }
                        break;
                    case 'S':
                        if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                            maps = moveS(maps, player);
                            valid = true;
                            return maps;
                        }
                        break;
                    case 'W':
                        if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                            maps = moveW(maps, player);
                            valid = true;
                            return maps;
                        }
                        break;
                    case 'E':
                        if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                            maps = moveE(maps, player);
                            valid = true;
                            return maps;
                        }
                        break;
                    case 'L':
                        //If the bot is on the bottom
                        if ((this.y == maps.get(0).size() - 2) && (this.x < maps.get(0).get(this.y).length() - 2) &&
                                (this.x > 1)){
                            for (int i = this.y - 2; i < this.y + 2; i++){
                                for (int j = this.x - 2; j < this.x + 3; j++){
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is at the top
                        else if ((this.y == 3) && (this.x < maps.get(0).get(this.y).length() - 2) &&
                                (this.x > 1)){
                            for (int i = this.y - 1; i < this.y + 3; i++){
                                for (int j = this.x - 2; j < this.x + 3; j++){
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the right
                        else if ((this.x == maps.get(0).get(this.y).length() - 2) && (this.y > 3) && (this.y < maps.get(0).size() - 2)){
                            for (int i = this.y - 2; i < this.y + 3; i++) {
                                for (int j = this.x - 2; j < this.x + 2; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the left
                        else if ((this.x == 1) && (this.y > 3) && (this.y < maps.get(0).size() - 2)){
                            for (int i = this.y - 2; i < this.y + 3; i++) {
                                for (int j = this.x - 1; j < this.x + 3; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the bottom right
                        else if ((this.y == maps.get(0).size() - 2) && this.x == maps.get(0).get(this.y).length() - 2){
                            for (int i = this.y - 2; i < this.y + 2; i++) {
                                for (int j = this.x - 2; j < this.x + 2; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.optimal = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the bottom left
                        else if ((this.y == maps.get(0).size() - 2) && (this.x == 1)){
                            for (int i = this.y - 2; i < this.y + 2; i++) {
                                for (int j = this.x - 1; j < this.x + 3; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the top left
                        else if ((this.x == 1) && (this.y == 3)){
                            for (int i = this.y - 1; i < this.y + 3; i++) {
                                for (int j = this.x - 1; j < this.x + 3; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is on the top right
                        else if ((this.y == 3) && (this.x == maps.get(0).get(this.y).length() - 2)){
                            for (int i = this.y - 1; i < this.y + 3; i++) {
                                for (int j = this.x - 2; j < this.x + 2; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        //If the bot is not on any edge
                        else {
                            for (int i = this.y - 2; i < this.y + 3; i++) {
                                for (int j = this.x - 2; j < this.x + 3; j++) {
                                    char var = maps.get(0).get(i).charAt(j);
                                    if (var == 'P'){
                                        this.seen = true;
                                        this.lastPos[0] = i;
                                        this.lastPos[1] = j;
                                    }
                                }
                            }
                        }
                        return maps;
                }
                
            }
            else {
                if (this.x == this.lastPos[0] && this.y == this.lastPos[1]){
                    this.seen = false;
                }
                int distX = this.lastPos[0] - this.x;
                int distY = this.lastPos[1] - this.y;

                if (distX < 0){
                    if (distY < 0){
                        if (Math.abs(distX) < Math.abs(distY)){
                            if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                                maps = moveN(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                                maps = moveW(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else if (Math.abs(distY) < Math.abs(distX)){
                            if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                                maps = moveW(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                                maps = moveN(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else{
                            int optMove = act.nextInt(2);
                            if (optMove == 0){
                                if (maps.get(0).get(this.y).charAt(this.x - 1) != '#') {
                                    maps = moveW(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                            else{
                                if (maps.get(0).get(this.y - 1).charAt(this.x) != '#') {
                                    maps = moveN(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                        }
                    }
                    else if (distY > 0){
                        if (Math.abs(distX) < Math.abs(distY)){
                            if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                                maps = moveS(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                                maps = moveW(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else if (Math.abs(distX) > Math.abs(distY)){
                            if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                                maps = moveW(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                                maps = moveS(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else{
                            int optMove = act.nextInt(2);
                            if (optMove == 0){
                                if (maps.get(0).get(this.y).charAt(this.x - 1) != '#') {
                                    maps = moveW(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                            else{
                                if (maps.get(0).get(this.y + 1).charAt(this.x) != '#') {
                                    maps = moveS(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                        }
                    }
                    else{
                        if (maps.get(0).get(this.y).charAt(this.x - 1) != '#'){
                            maps = moveW(maps, player);
                            valid = true;
                        }
                        else{
                            this.optimal = false;
                        }
                    }
                }
                else if (distX > 0){
                    if (distY < 0){
                        if (Math.abs(distX) < Math.abs(distY)){
                            if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                                maps = moveN(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                                maps = moveE(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else if (Math.abs(distX) > Math.abs(distY)){
                            if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                                maps = moveE(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                                maps = moveN(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else{
                            int optMove = act.nextInt(2);
                            if (optMove == 0){
                                if (maps.get(0).get(this.y).charAt(this.x + 1) != '#') {
                                    maps = moveE(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                            else{
                                if (maps.get(0).get(this.y - 1).charAt(this.x) != '#') {
                                    maps = moveN(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                        }
                    }
                    else if (distY > 0){
                        if (Math.abs(distX) < Math.abs(distY)){
                            if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                                maps = moveS(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                                maps = moveE(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else if (Math.abs(distX) > Math.abs(distY)){
                            if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                                maps = moveE(maps, player);
                                valid = true;
                            }
                            else if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                                maps = moveS(maps, player);
                                valid = true;
                            }
                            else{
                                this.optimal = false;
                            }
                        }
                        else {
                            int optMove = act.nextInt(2);
                            if (optMove == 0){
                                if (maps.get(0).get(this.y).charAt(this.x + 1) != '#') {
                                    maps = moveE(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                            else{
                                if (maps.get(0).get(this.y + 1).charAt(this.x) != '#') {
                                    maps = moveS(maps, player);
                                    valid = true;
                                }
                                else{
                                    this.optimal = false;
                                }
                            }
                        }
                    }
                    else{
                        if (maps.get(0).get(this.y).charAt(this.x + 1) != '#'){
                            maps = moveE(maps, player);
                            valid = true;
                        }
                        else{
                            this.optimal = false;
                        }
                    }
                }
                else{
                    if (distY < 0){
                        if (maps.get(0).get(this.y - 1).charAt(this.x) != '#'){
                            maps = moveN(maps, player);
                            valid = true;
                        }
                        else{
                            this.optimal = false;
                        }
                    }
                    else if (distY > 0){
                        if (maps.get(0).get(this.y + 1).charAt(this.x) != '#'){
                            maps = moveS(maps, player);
                            valid = true;
                        }
                        else{
                            this.optimal = false;
                        }
                    }
                    else{
                        this.seen = false;
                    }
                }
            }
        }
            return maps;
    }

    //If it is a valid move, moves the bot up one position and returns the new state of the map, nd the original map.
    public ArrayList<ArrayList<String>> moveN(ArrayList<ArrayList<String>> maps, Player player){
        this.y -= 1;
        if (maps.get(0).get(this.y).charAt(this.x) == 'P') {
            System.out.println("LOSE");
        }
        StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y + 1));
        if (player.getY() == this.y + 1){
            aftermath.setCharAt(player.getX(), 'P');
        }
        maps.get(0).set(this.y + 1, aftermath.toString());

        StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
        changePos.setCharAt(this.x, 'B');
        maps.get(0).set(this.y, changePos.toString());
        return maps;
    }

    //If it is a valid move, moves the bot down one position and returns the new state of the map, nd the original map.
    public ArrayList<ArrayList<String>> moveS(ArrayList<ArrayList<String>> maps, Player player){
        this.y += 1;
        if (maps.get(0).get(this.y).charAt(this.x) == 'P') {
            System.out.println("LOSE");
        }
        StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y - 1));
        if (player.getY() == this.y - 1){
            aftermath.setCharAt(player.getX(), 'P');
        }
        maps.get(0).set(this.y - 1, aftermath.toString());


        StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
        changePos.setCharAt(this.x, 'B');

        maps.get(0).set(this.y, changePos.toString());
        return maps;
    }

    //If it is a valid move, moves the bot left one position and returns the new state of the map, nd the original map.
    public ArrayList<ArrayList<String>> moveW(ArrayList<ArrayList<String>> maps, Player player){
        this.x -= 1;
        if (maps.get(0).get(this.y).charAt(this.x) == 'P') {
            System.out.println("LOSE");
        }
        StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y));
        maps.get(0).set(this.y, aftermath.toString());

        StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
        changePos.setCharAt(this.x, 'B');
        if (player.getY() == this.y){
            changePos.setCharAt(player.getX(), 'P');
        }
        maps.get(0).set(this.y, changePos.toString());
        return maps;
    }

    //If it is a valid move, moves the bot right one position and returns the new state of the map, nd the original map.
    public ArrayList<ArrayList<String>> moveE(ArrayList<ArrayList<String>> maps, Player player){
        this.x += 1;
        if (maps.get(0).get(this.y).charAt(this.x) == 'P') {
            System.out.println("LOSE");
        }
        StringBuilder aftermath = new StringBuilder(maps.get(1).get(this.y));
        maps.get(0).set(this.y, aftermath.toString());

        StringBuilder changePos = new StringBuilder(maps.get(0).get(this.y));
        changePos.setCharAt(this.x, 'B');
        if (player.getY() == this.y){
            changePos.setCharAt(player.getX(), 'P');
        }
        maps.get(0).set(this.y, changePos.toString());
        return maps;
    }


    public int getY(){
        return this.y;
    }
    public int getX() {
        return this.x;
    }



}
