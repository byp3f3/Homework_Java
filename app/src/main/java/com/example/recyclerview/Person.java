package com.example.recyclerview;

public class Person {
    private String name;
    private String age;
    private int photo;
    private String description;

    public Person(String name, String age, int photo, String description){
        this.name = name;
        this.age = age;
        this.photo = photo;
        this.description = description;
    }

    public String getName(){return this.name;}
    public String setName(String name){return this.name = name;}

    public String getAge(){return this.age;}

    public  String setAge(String age){return  this.age = age;}

    public  int getPhoto(){return  this.photo;}
    public  int setPhoto(int photo){return  this.photo = photo;}

    public String getDescription(){return this.description;}

    public  String setDescription(String description){return  this.description = description;}
}
