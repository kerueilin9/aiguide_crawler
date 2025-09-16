package util;

import learning_data.LearningPool;
import py4j.GatewayServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GatewayHelper {
    private List<GatewayServer> gatewayServerList;
    private String server_ip;

    public GatewayHelper(String server_ip, List<Map<String, String>> ipConfig, LearningPool pool) {
        this.server_ip = server_ip;
        this.gatewayServerList = new ArrayList<>();
        for(Map<String, String> data: ipConfig){
            this.gatewayServerList.add(createServer(data.get("ip"), data.get("python port"), data.get("java port"), pool));
        }
    }

    public GatewayHelper(String server_ip, Map<String, String> ipConfig, LearningPool pool) {
        this.server_ip = server_ip;
        this.gatewayServerList = new ArrayList<>();
        this.gatewayServerList.add(createServer(ipConfig.get("ip"), ipConfig.get("python port"), ipConfig.get("java port"), pool));
    }

    public void startGateway() {
        for(GatewayServer g: gatewayServerList){
            g.start();
        }
    }

    /***
     * @param sleepTime: second
     * @throws InterruptedException
     */
    public void closeGateway(int sleepTime) throws InterruptedException {
        Thread.sleep(sleepTime * 1000);
        for(GatewayServer g: gatewayServerList){
            g.shutdown();
        }
    }

    private GatewayServer createServer(String python_ip, String python_port, String java_port, LearningPool pool) {
        try {
            GatewayServer server = new GatewayServer.GatewayServerBuilder(pool)
                    .javaPort(Integer.parseInt(java_port))
                    .javaAddress(InetAddress.getByName(server_ip))
                    .callbackClient(Integer.parseInt(python_port), InetAddress.getByName(python_ip))
                    .build();
            return server;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not find host name");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong...");
        }
    }


}
