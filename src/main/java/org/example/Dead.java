package org.example;

import java.io.IOException;

public class Dead extends Human{

    private String death;
    static Log my_log;

    static {
        try{
            my_log = new Log("dead.log");
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Dead(String surname, String name, String middleName, String birth) {
        super(surname, name, middleName, birth);
        my_log.logger.info("Труп был захоронен");
    }

    public void setDeath(String death){
        try {
            this.death = death;
            my_log.logger.info("Дата смерти установлена");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось установить дату смерти");
        }
    }

    String getDeath(){
        try {
            my_log.logger.info("Дата смерти была выведена");
            return death;
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось вывести дату смерти");
            return null;
        }
    }

    @Override
    void addHuman() {
        try {
            System.out.println("----------------------\n" + getName() + " "
                    + getSurname() + " " + getMiddleName() + "\n" + getBirth() + " - "
                    + getDeath() + "\nПокойся с миром\n----------------------\n");
            my_log.logger.info("Труп успешно похоронен.Данные о нем были выведены");
        }
        catch (Exception e){
            my_log.logger.severe("Не удалось похоронить труп");
        }
    }
}
