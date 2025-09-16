package server_instance.codeCoverage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoCodeCoverage implements CodeCoverage {

    private List<Integer> codeCoverageVector;

    public NoCodeCoverage() {
        this.codeCoverageVector = Arrays.asList(0);
    }

    @Override
    public void setCodeCoverageVector(List<Integer> codeCoverageVector) {
    }

    @Override
    public List<Integer> getCodeCoverageVector() {
        return Arrays.asList(0);
    }

    @Override
    public int getCodeCoverageVectorSize() {
        return this.codeCoverageVector.size();
    }

    @Override
    public int getCoveredAmount() {
        return this.codeCoverageVector.stream().filter(i -> i!=0).collect(Collectors.toList()).size();
    }

    @Override
    public double getPercent() {
        return 1.0 * this.getCoveredAmount() / this.getCodeCoverageVectorSize();
    }

    @Override
    public void merge(CodeCoverage codeCoverage) {
    }

    @Override
    public CodeCoverage xor(CodeCoverage codeCoverage) {
        return new NoCodeCoverage();
    }
}
