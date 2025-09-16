package adpater.learningPool;

import py4j.GatewayServer;
import usecase.learningPool.ILearningPool;
import usecase.learningPool.action.dto.HighLevelActionDTO;
import usecase.learningPool.action.dto.HighLevelActionDTOBuilder;
import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningResult.dto.LearningResultDTO;
import usecase.learningPool.learningResult.dto.LearningResultDTOBuilder;
import usecase.learningPool.learningTask.LearningTask;
import usecase.learningPool.learningTask.dto.LearningTaskDTO;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Py4JLearningPool implements ILearningPool {
    private final String javaIp;
    private final String pythonIp;
    private final int javaPort;
    private final int pythonPort;
    private GatewayServer server;
    private boolean isAgentDone;
    private boolean isPauseAgent;


    private final Queue<LearningTaskDTO> learningTaskDTOQueue;
    private final Queue<LearningResultDTO> learningResultDTOQueue;

    public Py4JLearningPool(String javaIp, String pythonIp, int javaPort, int pythonPort){
        this.javaIp = javaIp;
        this.pythonIp = pythonIp;
        this.javaPort = javaPort;
        this.pythonPort = pythonPort;
        this.isAgentDone = false;
        this.isPauseAgent = false;

        learningTaskDTOQueue = new ArrayDeque<>();
        learningResultDTOQueue = new ArrayDeque<>();
    }

    public Py4JLearningPool(String pythonIp, int javaPort, int pythonPort){
        this("127.0.0.1", pythonIp, javaPort, pythonPort);
    }

    @Override
    public void startLearningPool() {
        try {
            this.server = new GatewayServer.GatewayServerBuilder(this)
                    .javaPort(this.javaPort)
                    .javaAddress(InetAddress.getByName(this.javaIp))
                    .callbackClient(this.pythonPort, InetAddress.getByName(this.pythonIp))
                    .build();
            server.start();
            System.out.println("Java ip: " + this.javaIp +
                    "\nJava Port: " + this.javaPort +
                    "\nPython ip: " + this.pythonIp +
                    "\nPython port: " + this.pythonPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not find host name");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong...");
        }
    }

    @Override
    public void stopLearningPool() {
        server.shutdown();
    }

    @Override
    public synchronized boolean getAgentDone() {
        return this.isAgentDone;
    }

    @Override
    public synchronized void setAgentDone(boolean isAgentDone) {
        this.isAgentDone = isAgentDone;
    }

    @Override
    public synchronized void enQueueLearningTaskDTO(LearningTaskDTO learningTaskDTO) {
        this.learningTaskDTOQueue.add(learningTaskDTO);
    }

    @Override
    public synchronized boolean isLearningTaskDTOQueueEmpty() {
        return this.learningTaskDTOQueue.isEmpty();
    }

    @Override
    public synchronized LearningResultDTO deQueueLearningResultDTO() {
        return this.learningResultDTOQueue.poll();
    }

    @Override
    public synchronized boolean isLearningResultDTOQueueEmpty() {
        return this.learningResultDTOQueue.isEmpty();
    }

    public synchronized LearningTaskDTO deQueueLearningTaskDTO(){
        return learningTaskDTOQueue.poll();
    }

    public synchronized void enQueueLearningResultDTO(LearningResultDTO learningResultDTO){
        learningResultDTOQueue.add(learningResultDTO);
    }

    public LearningResultDTOBuilder getLearnResultDTOBuilder(){
        return new LearningResultDTOBuilder();
    }

    public HighLevelActionDTOBuilder getHighLevelActionDTOBuilder(){
        return new HighLevelActionDTOBuilder();
    }

    @Override
    public synchronized boolean getPauseAgent() {
        return isPauseAgent;
    }

    @Override
    public synchronized void setPauseAgent(boolean isPauseAgent) {
        this.isPauseAgent = isPauseAgent;
    }
}