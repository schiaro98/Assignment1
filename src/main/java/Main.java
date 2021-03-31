package main.java;

import main.java.controller.Controller;
import main.java.controller.ControllerWithoutGui;

public class Main {
    public static void main(String[] args) {
        if(args.length == 3){
            new ControllerWithoutGui(args[0], args[1], args[2]).start();
        } else {
            new Controller();
        }
    }
}
