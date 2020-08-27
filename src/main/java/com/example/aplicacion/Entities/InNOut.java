package com.example.aplicacion.Entities;

import javax.persistence.*;

//tupla q contiene el nombre y texto de una answer
@Entity
public class InNOut {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Lob
    private String text;

    public InNOut() {
    }

    public InNOut(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public InNOut(long id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString(){
        return this.name+this.text;
    }
}