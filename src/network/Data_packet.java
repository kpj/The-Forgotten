package network;

import game.Entity.Char;
import game.Entity.Place;

import java.io.*;
import java.util.*;

import parser.Map_parser;

public class Data_packet implements Serializable
{
    public ArrayList<ArrayList> field = null;
    public HashMap<Integer, Place> changes = null;
    
    public boolean my_turn = false;
    public int num;
    
    public int field_width;
	public int field_height;
    public String path2bg;

    Map_parser mapper;
    
    public boolean on_the_fly = true;
    
    public ArrayList<Char> ini_t = null;
    
    @SuppressWarnings("unchecked")
    public Data_packet(ArrayList<ArrayList> f, boolean turn, int w, int h, String p2b, int n)
    {
        field = (ArrayList<ArrayList>)f.clone();
        my_turn = turn;
        field_width = w;
        field_height = h;
        path2bg = p2b;
        num = n;
    }
    @SuppressWarnings("unchecked")
    public Data_packet(HashMap<Integer, Place> c, boolean turn, int n)
    {
        changes = (HashMap<Integer, Place>)c.clone();
        my_turn = turn;
        num = n;
    }
}
