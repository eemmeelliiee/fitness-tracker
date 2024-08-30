package model;
public class Exercise {

    private String name;
    private int duration;
    private String description;

    public Exercise() {
    }

    public Exercise(String name, int duration, String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int age) {
        this.duration = age;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String note){
        this.description = note;
    }
}
