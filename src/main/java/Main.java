package main.java;

import main.java.controller.Controller;
import main.java.model.RankMonitorImpl;
import main.java.view.View;

public class Main {
    public static void main(String[] args) {
        RankMonitorImpl model = new RankMonitorImpl();
        Controller controller = new Controller(model);
    }
}
