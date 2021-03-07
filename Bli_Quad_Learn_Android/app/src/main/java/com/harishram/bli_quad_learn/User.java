package com.harishram.bli_quad_learn;

public class User {
    String Name,Address;
    public User(){

    }
    public User(String name,String address){
        this.Name = name;
        this.Address = address;
    }
    public String getName(){
        return this.Name;
    }
    public String getAddress(){
        return this.Address;
    }
}
