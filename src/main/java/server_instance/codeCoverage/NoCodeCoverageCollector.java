package server_instance.codeCoverage;

public class NoCodeCoverageCollector implements CodeCoverageCollector {

    @Override
    public CodeCoverage getBranchCoverage() {
        return new NoCodeCoverage();
    }

    @Override
    public CodeCoverage getStatementCoverage() {
        return new NoCodeCoverage();
    }

    @Override
    public void resetCoverage() {
    }
}
