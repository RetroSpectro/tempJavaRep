package com.company.model;

import java.util.ArrayList;

public class Uni {
    private int id;
    private String name;
    private ArrayList<Faculty> faculties;

    public Uni(String name) {
        this.name = name;
    }
    public Uni() {

    }

    public String getName() {
        return name;
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(ArrayList<Faculty> faculties) {
        this.faculties = faculties;
    }
    public void addFaculty(Faculty faculty)
    {
        faculties.add(faculty);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
