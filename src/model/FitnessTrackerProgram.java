package model;

import constants.Messages;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FitnessTrackerProgram {

    private Scanner myScanner;
    private Exercise[] exercises;
    private String workoutFileTitle;

    public void start(int length) {

        myScanner = new Scanner(System.in);
        exercises = new Exercise[length];

        clearTerminal();

        System.out.println(Messages.WELCOME);

        printWorkoutFiles();

        System.out.println("\nChoose an existing workout, or create a new one by assigning it an original title.");
        System.out.print("Title: ");
        workoutFileTitle = myScanner.nextLine();

        loadExercisesFromWorkoutFile();

        boolean running = true;
        while (running) {
            printChoices();
            try {
                int input = Integer.parseInt(myScanner.nextLine());
                switch (input) {
                    case 1:
                        createExercise();
                        break;
                    case 2:
                        removeExercise();
                        break;
                    case 3:
                        switchPlaceForExercise();
                        break;
                    case 4:
                        renameExercise();
                        break;
                    case 5:
                        countExercises();
                        break;
                    case 6:
                        printAllExercises();
                        break;
                    case 7:
                        saveExercisesToWorkoutFile();
                        exitProgram();
                        running = false;
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                clearTerminal();
                System.out.println(Messages.INPUT_ERROR);
            }

        }

        myScanner.close();
    }

    private void printWorkoutFiles() {
        File directory = new File("workouts");
        File[] workoutFilesList = directory.listFiles();

        if (workoutFilesList != null && workoutFilesList.length > 0) {
            System.out.println("\nThese are your existing workouts:");
            boolean fileFound = false;
            for (File file : workoutFilesList) {
                System.out.println(file.getName());
                fileFound = true;
            }
            if (!fileFound) {
                System.out.println("No existing workouts found.");
            }
        } else {
            System.out.println("No existing workouts found.");
        }
    }

    private void loadExercisesFromWorkoutFile() {
        File directory = new File("workouts");
        File file = new File(directory, workoutFileTitle);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int index = 0;
                while ((line = reader.readLine()) != null && index < exercises.length) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        Exercise exercise = new Exercise();
                        exercise.setName(parts[0].trim());
                        exercise.setDuration(Integer.parseInt(parts[1].trim()));
                        exercises[index++] = exercise;
                    }
                }
                clearTerminal();
                System.out.println("Workout '" + workoutFileTitle + "' was chosen.");
            } catch (IOException e) {
                clearTerminal();
                System.out.println("Error finding workout: " + workoutFileTitle);
            }
        } else if (!file.exists()) {
            try {
                file.createNewFile();
                clearTerminal();
                System.out.println("A new workout with title '" + workoutFileTitle + "' has been created!");
            } catch (IOException e) {
                System.out.println("Error creating workout: " + workoutFileTitle);
            }
        }
    }

    private void saveExercisesToWorkoutFile() {
        File directory = new File("workouts");
        File file = new File(directory, workoutFileTitle);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Exercise exercise : exercises) {
                if (exercise != null) {
                    writer.write(exercise.getName() + "," + exercise.getDuration());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving exercise data.");
        }
    }

    private void printChoices() {
        System.out.println("\nTo operate on your workout '" + workoutFileTitle + "', please choose desired option:");
        System.out.println("--------------------------------");
        System.out.println("## MAIN MENU ##\nFollowing are your options: \n");
        System.out.println("1: Insert an exercise");
        System.out.println("2: Remove an exercise");
        System.out.println("3: Switch place for exercise");
        System.out.println("4: Rename an exercise");
        System.out.println("5: Count exercises");
        System.out.println("6: Display all exercises for workout");
        System.out.println("7: Exit the program");
        System.out.println("--------------------------------");

        System.out.print("\nPlease choose an option: ");
    }

    private static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private boolean printList(Exercise[] exercises) {
        int numberOfExercises = 0;
        for (Exercise exerciseToCount : exercises) {
            if (exerciseToCount != null) {
                numberOfExercises++;
            }
        }
        if (numberOfExercises > 0) {
            System.out.println("Place:\tName:\t\tDuration:");
            int i = 1;
            for (Exercise tempExercise : exercises) {
                if (tempExercise != null) {
                    System.out.println(i + ".\t" + tempExercise.getName() + "\t\t" + tempExercise.getDuration());
                } else {
                    System.out.println(i + ".");
                }
                i++;
            }
            return true;
        } else {
            System.out.println("No exercises exist yet. " + Messages.RETURN_TO_MAIN_MENU);
            return false;
        }
    }

// Insert reps and sets instead of duration when creating: (make sure to update printList()-methods as well)

    private void createExercise() {
        clearTerminal();
        System.out.println("\n1. ## CREATE EXERCISE ##\n");
        Exercise exercise = new Exercise();
        System.out.print("Insert name of exercise: ");
        exercise.setName(myScanner.nextLine());
        System.out.print("Insert duration of exercise (in minutes): ");
        exercise.setDuration(Integer.parseInt(myScanner.nextLine()));

        int occupiedElements = 0;
        for (int i = 0; i < exercises.length; i++) {
            if (exercises[i] != null) {
                occupiedElements++;
            } else if (exercises[i] == null) {
                exercises[i] = exercise;

                String name = exercise.getName();
                int duration = exercise.getDuration();

                clearTerminal();
                System.out.println("\nExercise with name '" + name + "', and duration " + duration
                        + " mins added!" + Messages.RETURN_TO_MAIN_MENU);
                break;
            }
        }

        if (occupiedElements >= exercises.length) {
            System.out
                    .println("\nThe list is full. \n\n" + Messages.RETURN_TO_MAIN_MENU);
        }

        saveExercisesToWorkoutFile();
    }

    private boolean noExerciseFound() {
        boolean exit;
        System.out.println("\nNo exercise has that place in this workout. Try again or exit the operation.");
        System.out.println("\nTo try again, choose: 1\nChoose any other number to exit.");
        System.out.print("\nChoice: ");
        int choice = Integer.parseInt(myScanner.nextLine());
        if (choice != 1) {
            clearTerminal();
            System.out.println("You chose to exit the operation. You will now return to the main menu.");
            exit = true;
        } else {
            exit = false;
        }
        return exit;
    }

    private void removeExercise() {
        boolean notFound = true;
        while (notFound) {
            clearTerminal();
            System.out.println("\n2. ## REMOVE EXERCISE ##\n");
            boolean exercisesExist = printList(exercises);
            if (exercisesExist) {
                System.out.print("\nInsert place in list of exercise to be removed: ");
                int placeOfExerciseToBeRemoved = Integer.parseInt(myScanner.nextLine());
                if (exercises[placeOfExerciseToBeRemoved - 1] != null) {
                    notFound = false;
                    String exerciseName = exercises[placeOfExerciseToBeRemoved - 1].getName();
                    exercises[placeOfExerciseToBeRemoved - 1] = null;
                    clearTerminal();
                    System.out.println(
                            "\n" + exerciseName + " was removed." + Messages.RETURN_TO_MAIN_MENU);
                } else {
                    boolean exit = noExerciseFound();
                    if (exit == true) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        saveExercisesToWorkoutFile();
    }

    private void switchPlaceForExercise() {
        boolean notFound = true;
        while (notFound) {
            clearTerminal();
            System.out.println("\n3. ## SWITCH PLACES IN WORKOUT ##\n");
            boolean exercisesExist = printList(exercises);
            if (exercisesExist) {
                System.out.print("\nInsert place of exercise to be switched: ");
                int place1 = Integer.parseInt(myScanner.nextLine());
                if (exercises[place1 - 1] != null) {
                    notFound = false;
                    Exercise exercise1 = exercises[place1 - 1];
                    String exerciseName = exercise1.getName();
                    System.out.println("Place for " + exerciseName + " has been chosen");

                    System.out.print("\nChoose place of exercise to switch with: ");
                    int place2 = Integer.parseInt(myScanner.nextLine()) - 1;
                    exercises[place1 - 1] = exercises[place2];
                    exercises[place2] = exercise1;
                    clearTerminal();
                    System.out.println("Places have been switched!\n");
                    System.out.println("## UPDATED LIST ##");
                    printList(exercises);
                    System.out.println("\n" + Messages.RETURN_TO_MAIN_MENU);
                } else {
                    boolean exit = noExerciseFound();
                    if (exit == true) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        saveExercisesToWorkoutFile();
    }

    private void renameExercise() {
        boolean notFound = true;
        while (notFound) {
            clearTerminal();
            System.out.println("\n4. ## RENAME EXERCISE ##\n");
            boolean exercisesExist = printList(exercises);
            if (exercisesExist) {
                System.out.print("\nInsert place of exercise to be renamed: ");
                int place1 = Integer.parseInt(myScanner.nextLine());
                if (exercises[place1 - 1] != null) {
                    notFound = false;
                    Exercise exerciseToRename = exercises[place1 - 1];
                    String exerciseOldName = exerciseToRename.getName();
                    System.out.println("Exercise " + exerciseOldName + " has been chosen.");
                    System.out.print("\nWrite new name: ");
                    String newName = myScanner.nextLine();
                    exerciseToRename.setName(newName);
                    String exerciseNewName = exerciseToRename.getName();
                    clearTerminal();
                    System.out.println("\nExercise has been renamed from " + exerciseOldName + " to "
                            + exerciseNewName + "!" + Messages.RETURN_TO_MAIN_MENU);
                } else {
                    boolean exit = noExerciseFound();
                    if (exit == true) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        saveExercisesToWorkoutFile();
    }

    private void countExercises() {
        clearTerminal();
        System.out.println("5. ## COUNT EXERCISES ## ");
        int numberOfExercises = 0;
        for (Exercise exerciseToCount : exercises) {
            if (exerciseToCount != null) {
                numberOfExercises++;
            }
        }
        System.out.println("\nTotal number of exercises: " + numberOfExercises
                + "." + Messages.RETURN_TO_MAIN_MENU);
    }

    private void printAllExercises() {
        clearTerminal();
        System.out.println("\n6. ## ALL EXERCISES IN WORKOUT ##\n");
        printList(exercises);
    }

    private void exitProgram() {
        System.out.println("\nExiting the program...\n");
    }
}
