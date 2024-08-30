package model;
public class Exercise {

    private String name;
    private int reps;
    private int sets;
    private String description;

    public Exercise() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int age) {
        this.reps = age;
    }

    public int getSets(){
        return sets;
    }

    public void setSets(int sets){
        this.sets = sets;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String note){
        this.description = note;
    }
}
