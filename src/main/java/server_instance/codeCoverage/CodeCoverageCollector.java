package server_instance.codeCoverage;

import java.util.List;

public interface CodeCoverageCollector {
    CodeCoverage getBranchCoverage();

    CodeCoverage getStatementCoverage();

    void resetCoverage();
}
