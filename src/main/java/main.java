import adpater.controller.Controller;
import util.Config;
import util.LogHelper;


public class main {

    public static void main(String[] args) throws Exception {
        LogHelper.info("AI GUIDE Start...");

//        Py4JSingletonTest py4JSingletonTest = Py4JSingletonTest.getInstance();
//        py4JSingletonTest.start();

        Config config = new Config("./configuration/configuration.json");
        new Controller(config).execute();

//        py4JSingletonTest.stop();

        LogHelper.info("AI GUIDE Close...");
        LogHelper.writeAllLog();
    }
}
