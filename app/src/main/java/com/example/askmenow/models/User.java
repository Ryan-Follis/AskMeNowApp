package com.example.askmenow.models;

import java.io.Serializable;

public class User implements Serializable{
    public String name, username, image, email, token, id;
    public double latitude,longitude;
    public int[] weekScore;
}


