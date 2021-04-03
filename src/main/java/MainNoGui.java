import controller.ControllerWithoutGui;

public class MainNoGui {
    public static void main(String[] args) {
        ControllerWithoutGui c = new ControllerWithoutGui("res/pdf", "res/ignored/ignore.txt", "10");
        c.start();
    }
}
