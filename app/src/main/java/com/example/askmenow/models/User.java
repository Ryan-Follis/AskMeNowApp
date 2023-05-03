package com.example.askmenow.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
    public String name, username, image, email, token, id;
    public int age;
    public double longitude,latitude;
    public int[] weekScore;
    public List<String> interests;
}


