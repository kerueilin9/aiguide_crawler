package server_instance;

import server_instance.codeCoverage.CodeCoverageCollector;
import server_instance.codeCoverage.CodeCoverageHelper;
import server_instance.codeCoverage.JacocoCodeCoverageCollector;
import server_instance.codeCoverage.NoCodeCoverageCollector;
import util.CommandHelper;
import util.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Spring PetClinic Server Management Class (with JaCoCo Coverage Support)
 * 
 * Supports two modes:
 * 1. spring_petclinic_with_coverage - Uses JaCoCo coverage collection
 * 2. spring_petclinic_with_no_coverage - No coverage collection
 */
public class SpringPetclinicWithCoverageServer extends ServerInstanceManagement {

    private final int MAXIMUM_WAITING_COUNT = 20;
    private final int JACOCO_PORT = 6300;
    private final int API_PORT = 6301;
    private final String dockerFolder = "./src/main/java/server_instance/dockerFile/";
    private String compose_file;

    public SpringPetclinicWithCoverageServer(String appName, int server_port) {
        super(appName, server_port);
        createDockerComposeFile();
        copyVE();
        
        CodeCoverageCollector codeCoverageCollector;
        codeCoverageCollector = new JacocoCodeCoverageCollector("localhost", JACOCO_PORT, "http://localhost:" + API_PORT);
        System.out.println("SpringPetclinic with JaCoCo coverage enabled on TCP port: " + JACOCO_PORT + ", HTTP API port: " + API_PORT);
        this.codeCoverageHelper = new CodeCoverageHelper(codeCoverageCollector);
    }

    private void createDockerComposeFile() {
        createDockerFileFolder();     
        String compose_file_content;
        compose_file_content = createJaCoCoDockerCompose(); 
        compose_file = dockerFolder + "docker_compose_spring_petclinic_" + (server_port % 3000) + ".yml";
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

    private String createJaCoCoDockerCompose() {
        String compose_content =
                "services:\n" +
                "  spring-petclinic-jacoco_%d:\n" +
                "    image: lidek213/spring-petclinic_for_experiment:latest\n" +
                "    command: |\n" +
                "      bash -c \"\n" +
                "      mkdir -p /jacoco\n" +
                "      wget -q -O /jacoco/jacocoagent.jar https://repo1.maven.org/maven2/org/jacoco/org.jacoco.agent/0.8.8/org.jacoco.agent-0.8.8-runtime.jar\n" +
                "      ls -la /jacoco/\n" +
                "      java -javaagent:/jacoco/jacocoagent.jar=destfile=/jacoco/coverage.exec,output=tcpserver,port=%d,address=0.0.0.0 -jar /spring-petclinic/build/libs/spring-petclinic-2.6.0.jar\n" +
                "      \"\n" +
                "    ports:\n" +
                "      - \"%d:8080\"\n" +      // Spring Boot application
                "      - \"%d:%d\"\n" +        // JaCoCo TCP port
                "    volumes:\n" +
                "      - jacoco-data_%d:/jacoco\n" +
                "    networks:\n" +
                "      - petclinic-network_%d\n" +
                "\n" +
                "  coverage-api_%d:\n" +
                "    image: python:3.9-slim\n" +
                "    working_dir: /app\n" +
                "    volumes:\n" +
                "      - jacoco-data_%d:/jacoco\n" +
                "      - ./coverage-api:/app\n" +
                "    command: |\n" +
                "      bash -c \"\n" +
                "      apt-get update -y >/dev/null 2>&1 && apt-get install -y --no-install-recommends openjdk-17-jre-headless ca-certificates wget >/dev/null 2>&1 && rm -rf /var/lib/apt/lists/*\n" +
                "      pip install -q flask requests\n" +
                "      mkdir -p /opt/jacoco/lib\n" +
                "      wget -q -O /opt/jacoco/lib/jacococli.jar https://repo1.maven.org/maven2/org/jacoco/org.jacoco.cli/0.8.8/org.jacoco.cli-0.8.8-nodeps.jar\n" +
                "      python coverage_api.py\n" +
                "      \"\n" +
                "    ports:\n" +
                "      - \"%d:8091\"\n" +      // Coverage API
                "    depends_on:\n" +
                "      - spring-petclinic-jacoco_%d\n" +
                "    networks:\n" +
                "      - petclinic-network_%d\n" +
                "    environment:\n" +
                "      - JACOCO_DATA_PATH=/jacoco\n" +
                "      - JACOCO_TCP_HOST=spring-petclinic-jacoco_%d\n" +
                "      - JACOCO_TCP_PORT=%d\n" +
                "\n" +
                "volumes:\n" +
                "  jacoco-data_%d:\n" +
                "\n" +
                "networks:\n" +
                "  petclinic-network_%d:\n" +
                "    driver: bridge\n";
        
        int instanceId = server_port % 3000;
        
        return String.format(compose_content, 
            instanceId,           // service name ID
            JACOCO_PORT,           // JaCoCo agent port parameter
            server_port,          // Spring Boot application port mapping
            JACOCO_PORT, JACOCO_PORT, // JaCoCo TCP port mapping
            instanceId,           // Volume name ID
            instanceId,           // Network name ID
            instanceId,           // Coverage API service name ID
            instanceId,           // Coverage API Volume ID
            API_PORT,              // Coverage API port (JaCoCo port + 1)
            instanceId,           // Dependency service ID
            instanceId,           // Coverage API Network ID
            instanceId,           // JaCoCo TCP Host ID
            JACOCO_PORT,           // JaCoCo TCP port environment variable
            instanceId,           // Volume ID
            instanceId            // Network ID
        );
    }

    private void createDockerFileFolder() {
        File folder = new File(dockerFolder);
        boolean bool = folder.mkdir();
        if(bool){
            System.out.println("Directory created successfully");
        }else{
            System.out.println("Folder is exist, not going to create it...");
        }
    }

    @Override
    public void createServerInstance() {
        int waitingCount = 0;
        createServer();
        String url = "http://localhost:%d";
        while(!isServerActive(String.format(url, server_port), 200)) {
            System.out.println("waiting for server to start...");
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                throw new RuntimeException();
            }
            waitingCount += 1;
            if(waitingCount == MAXIMUM_WAITING_COUNT){
                findBusyProcessAndKillIt();
                recreateTimeOffManagement();
            }
            else if(waitingCount == MAXIMUM_WAITING_COUNT * 2) throw new RuntimeException("Something went wrong when creating Timeoff-management...");
        }
        System.out.println("Spring PetClinic server created successfully!");
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

    private void recreateTimeOffManagement() {
        System.out.println("recreate Server");
        closeServerInstance();
        createServer();
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
        System.out.println("Restarting Spring PetClinic server instance...");
        closeServerInstance();
        createServerInstance();
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void recordCoverage() {
        this.codeCoverageHelper.recordCoverage();
    }

    @Override
    public void resetCoverage() {
        codeCoverageHelper.resetCoverage();
    }

    private void createServer() {
        long startTime = System.nanoTime();
        CommandHelper.executeCommand("docker-compose", "-f", compose_file, "up", "-d");
        long endTime = System.nanoTime();
        double timeElapsed = (endTime - startTime) / 1000000000.0;
        System.out.println("\nServer Port is " + server_port + ", Starting server instance waiting time is :" + timeElapsed);
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
}