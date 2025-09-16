package server_instance;

import server_instance.codeCoverage.CodeCoverageCollector;
import server_instance.codeCoverage.CodeCoverageHelper;
import server_instance.codeCoverage.IstanbulCodeCoverageCollector;
import server_instance.codeCoverage.NoCodeCoverageCollector;
import util.CommandHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class KimaiServer extends ServerInstanceManagement {
    private final int MAXIMUM_WAITING_COUNT = 120;
    private final String dockerFolder = "./src/main/java/server_instance/dockerFile/";
    private String compose_file;

    public KimaiServer(String appName, int server_port) {
        super(appName, server_port);
        createDockerComposeFile();
        copyVE();
        CodeCoverageCollector codeCoverageCollector = new NoCodeCoverageCollector();
        this.codeCoverageHelper = new CodeCoverageHelper(codeCoverageCollector);
    }

    private void createDockerComposeFile() {
        createDockerFileFolder();

        String compose_file_content = "version: '3.5'\n" +
                "services:\n" +
                "\n" +
                "  sqldb:\n" +
                "    image: mysql:5.7\n" +
                "    volumes:\n" +
                "      - kimai-mysql:/var/lib/mysql\n" +
                "    environment:\n" +
                "      - MYSQL_DATABASE=kimai\n" +
                "      - MYSQL_USER=kimaiuser\n" +
                "      - MYSQL_PASSWORD=kimaipassword\n" +
                "      - MYSQL_ROOT_PASSWORD=changemeplease\n" +
                "    command: --default-storage-engine innodb\n" +
                "    restart: unless-stopped\n" +
                "    healthcheck:\n" +
                "      test: mysqladmin -p$$MYSQL_ROOT_PASSWORD ping -h localhost\n" +
                "      interval: 20s\n" +
                "      start_period: 10s\n" +
                "      timeout: 10s\n" +
                "      retries: 3\n" +
                "\n" +
                "  kimai:\n" +
                "    image: kimai/kimai2:apache\n" +
                "    volumes:\n" +
                "      - kimai-var:/opt/kimai/var\n" +
                "    ports:\n" +
                "      - %d:8001\n" +
                "    environment:\n" +
                "      - ADMINMAIL=vector@selab.com\n" +
                "      - ADMINPASS=selab1623\n" +
                "      - DATABASE_URL=mysql://kimaiuser:kimaipassword@sqldb/kimai\n" +
                "      - TRUSTED_HOSTS=nginx,localhost,127.0.0.1\n" +
                "    restart: unless-stopped\n" +
                "\n" +
                "volumes:\n" +
                "  kimai-var:\n" +
                "  kimai-mysql:";

        compose_file_content = String.format(compose_file_content, server_port);
        compose_file = dockerFolder + "docker_compose_kimai_" + (server_port % 3000) + ".yml";
        try {
            FileWriter fw = new FileWriter(compose_file);
            fw.write(compose_file_content);
            fw.flush();
            fw.close();
        }catch (IOException e){
            System.out.println("Error!!!");
            e.printStackTrace();
            throw new RuntimeException("Write docker file error!!");
        }
    }

    private void createDockerFileFolder() {
        File file = new File("./src/main/java/server_instance/dockerFile");
        boolean bool = file.mkdir();
        if(bool){
            System.out.println("Directory created successfully");
        }else{
            System.out.println("Folder is exist, not going to create it...");
        }
    }

    private void findBusyProcessAndKillIt() {
        String containerID = findBusyProcess();
        killBusyProcess(containerID);
    }

    private String findBusyProcess() {
        String containerID = CommandHelper.executeCommand("docker-compose", "-f", compose_file, "ps", "-q");
        System.out.println("find the container id is :" + containerID);
        return containerID;
    }

    private void killBusyProcess(String containerID) {
        String fixDeviceErrorScript = dockerFolder + "find-busy-mnt.sh";
        if(System.getProperty("os.name").toLowerCase().matches("(.*)windows(.*)")){
            CommandHelper.executeCommand("powershell.exe", fixDeviceErrorScript, containerID);
        }
        else {
            CommandHelper.executeCommand("sh", fixDeviceErrorScript, containerID);
        }
    }

    @Override
    public void createServerInstance() {
        int waitingCount = 0;
        createServer();
        String url = "http://127.0.0.1:%d";
        while(!isServerActive(String.format(url, server_port), 200)) {
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
            waitingCount += 1;
            if(waitingCount == MAXIMUM_WAITING_COUNT){
                findBusyProcessAndKillIt();
                recreateServer();
            }
            else if(waitingCount == MAXIMUM_WAITING_COUNT * 2) throw new RuntimeException("Something went wrong when creating Kimai...");
        }
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void recreateServer() {
        System.out.println("recreate Server");
        closeServerInstance();
        createServer();
    }

    private void createServer() {
        long startTime = System.nanoTime();
        CommandHelper.executeCommand("docker-compose", "-f", compose_file, "up", "-d");
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1000000000.0;
        System.out.println("\nServer Port is " + server_port + ", Starting server instance waiting time is :" + timeElapsed);
    }

    @Override
    public void closeServerInstance() {
        String url = "http://127.0.0.1:%d";
        boolean isFirst = true;
        long startTime = System.nanoTime();
        while(isServerActive(String.format(url, server_port), 200)) {
            if (isFirst){
                try {
                    Thread.sleep(1000);
                    isFirst = false;
                }catch (InterruptedException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
            CommandHelper.executeCommand("docker-compose", "-f", compose_file, "rm", "-svf");
        }
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1000000000.0;
        System.out.println("\nServer Port is " + server_port + ", Closing server instance waiting time is :" + timeElapsed);
    }

    @Override
    public void restartServerInstance() {
        closeServerInstance();
        createServerInstance();
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void resetCoverage() {
        codeCoverageHelper.resetCoverage();
    }

    public boolean isServerActive(String url, int expectedStatusCode) {
        int httpStatusCode = getResponseStatusCode(url);
        return httpStatusCode == expectedStatusCode;
    }

    private int getResponseStatusCode(String url) {
        int code;
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)targetUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
        }
        catch (UnknownHostException e){
            code = -1;
        }
        catch (SocketException e){
            code = -2;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unknown response status code!!");
        }
        return code;
    }

    private Integer[] covertToOneHot(Integer[] coverageVector) {
        for(int i = 0; i < coverageVector.length; i++){
            if(coverageVector[i] != 0) coverageVector[i] = 300;
        }
        return  coverageVector;
    }
}
