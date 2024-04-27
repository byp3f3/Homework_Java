package org.example;

abstract class Human {
    private String name;
    private String surname;
    private String middleName;
    private String birth;

    public Human(String surname, String name, String middleName, String birth){
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        this.birth = birth;
    }

    public String getName(){return name;}
    public String getSurname(){return surname;}
    public String getMiddleName(){return middleName;}
    public String getBirth(){return birth;}

    abstract void addHuman();
}
