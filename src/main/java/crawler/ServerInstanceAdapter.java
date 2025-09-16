package crawler;

import ntut.edu.aiguide.crawljax.plugins.ServerInstanceManagement;

import java.util.Arrays;

public class ServerInstanceAdapter implements ServerInstanceManagement {
    private final server_instance.ServerInstanceManagement serverInstanceManagement;

    public ServerInstanceAdapter(server_instance.ServerInstanceManagement serverInstanceManagement) {
        this.serverInstanceManagement = serverInstanceManagement;
    }

    @Override
    public void recordCoverage() {
        serverInstanceManagement.recordCoverage();
    }

    void resetRecordCoverage() {
        serverInstanceManagement.resetTotalCoverage();
    }

    Integer[] getTotalStatementCoverage() {
        return serverInstanceManagement.getTotalStatementCoverageVector();
    }

    Integer[] getTotalBranchCoverage() { return serverInstanceManagement.getTotalBranchCoverageVector(); }

    @Override
    public void createServerInstance() {
        serverInstanceManagement.createServerInstance();
    }

    @Override
    public void closeServerInstance() {
        serverInstanceManagement.closeServerInstance();
    }
}
