package server_instance.codeCoverage;

import java.util.List;

public interface CodeCoverage {
    void setCodeCoverageVector(List<Integer> codeCoverageVector);
    List<Integer> getCodeCoverageVector();
    int getCodeCoverageVectorSize();
    int getCoveredAmount();
    double getPercent();

    void merge(CodeCoverage codeCoverage);
    CodeCoverage xor(CodeCoverage codeCoverage);
}
