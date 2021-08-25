package com.scuti.youtube.utils;

import com.google.gson.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class YoutubeAPI
{
    private static final String API_URL;
    private static final String KEY;
    private static final String PART;
    
    public static void main(final String[] array) throws Exception {
        System.out.println(obterDadosVideo("https://www.youtube.com/watch?v=g5Z-Bzwqt70").items.get(0).statistics.likeCount);
    }
    
    public static RespostaYoutube obterDadosVideo(final String s) {
        final Gson gson = new Gson();
        String lerUrl;
        try {
            lerUrl = lerUrl("https://www.googleapis.com/youtube/v3/videos?id=" + s.split("v=")[1] + "&key=" + "AIzaSyBYZEMh_dzM31b1zpiHdCRdZAHfUhYFjaE" + "&part=" + "snippet,contentDetails,statistics,status");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return (RespostaYoutube)gson.fromJson(lerUrl, (Class)RespostaYoutube.class);
    }
    
    private static String lerUrl(final String s) throws Exception {
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(s).openStream()));
            final StringBuffer sb = new StringBuffer();
            final char[] array = new char[1024];
            int read;
            while ((read = reader.read(array)) != -1) {
                sb.append(array, 0, read);
            }
            return sb.toString();
        }
        finally {
            if (reader != null) {
                ((BufferedReader)reader).close();
            }
        }
    }
    
    static {
        API_URL = "https://www.googleapis.com/youtube/v3/videos?";
        PART = "snippet,contentDetails,statistics,status";
        KEY = "AIzaSyBYZEMh_dzM31b1zpiHdCRdZAHfUhYFjaE";
    }
    
    public class RespostaYoutube
    {
        public List<Items> items;
        
        public RespostaYoutube() {
            this.items = new ArrayList<Items>();
        }
    }
    
    public class Items
    {
        public Snippet snippet;
        public Statistics statistics;
        public String id;
    }
    
    public class Snippet
    {
        public String title;
        public String description;
        public Thumbnails thumbnails;
        public String channelTitle;
        public List<String> tags;
        
        public Snippet() {
            this.tags = new ArrayList<String>();
        }
    }
    
    public class Statistics
    {
        public long likeCount;
        public long dislikeCount;
        public long viewCount;
    }
    
    public class Thumbnails
    {
        Medium medium;
        High high;
    }
    
    public class Medium
    {
        String url;
        long width;
        long height;
    }
    
    public class High
    {
        String url;
        long width;
        long height;
    }
}
