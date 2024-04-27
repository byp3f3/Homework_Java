package org.example;

public class Dead extends Human{

    private String death;
    public Dead(String surname, String name, String middleName, String birth) {
        super(surname, name, middleName, birth);
    }

    public void setDeath(String death){
        this.death = death;
    }

    String getDeath(){return death;}

    @Override
    void addHuman() {
        System.out.println("----------------------\n" + getName()+" "
                +getSurname()+" "+getMiddleName()+"\n"+getBirth()+" - "
                + getDeath()+ "\nПокойся с миром\n----------------------\n");

    }
}
