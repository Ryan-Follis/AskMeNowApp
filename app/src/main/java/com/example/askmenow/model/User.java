package com.example.askmenow.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{
    public String name, username, image, email, token, id;
    public int age;
    public List<String> interests;
}
