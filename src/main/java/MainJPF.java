import controller.ControllerJPF;

public class MainJPF {
    public static void main(String[] args) throws InterruptedException {
        ControllerJPF con = new ControllerJPF("10");
        con.start();
    }
}
