package main.java;

import main.java.controller.ControllerWithoutGui;

public class MainNoGui {
    public static void main(String[] args) {
            new ControllerWithoutGui("res/pdf", "res/ignored/ignore.txt", "10").start();
    }
}
