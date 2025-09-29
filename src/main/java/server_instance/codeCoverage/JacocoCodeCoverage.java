package server_instance.codeCoverage;

import java.util.List;

/**
 * JaCoCo
 */
public class JacocoCodeCoverage implements CodeCoverage {
    private List<Integer> codeCoverageVector;

    public JacocoCodeCoverage(List<Integer> codeCoverageVector) {
        this.codeCoverageVector = codeCoverageVector;
    }

    @Override
    public void setCodeCoverageVector(List<Integer> codeCoverageVector) {
        this.codeCoverageVector = codeCoverageVector;
    }

    @Override
    public List<Integer> getCodeCoverageVector() {
        return codeCoverageVector;
    }

    @Override
    public int getCodeCoverageVectorSize() {
        return codeCoverageVector.size();
    }

    @Override
    public int getCoveredAmount() {
        return (int) codeCoverageVector.stream()
                .filter(i -> i != 0)
                .count();
    }

    @Override
    public double getPercent() {
        if (codeCoverageVector.isEmpty()) {
            return 0.0;
        }
        return (double) getCoveredAmount() / getCodeCoverageVectorSize() * 100.0;
    }

    @Override
    public void merge(CodeCoverage other) {
        if (other instanceof JacocoCodeCoverage) {
            JacocoCodeCoverage otherJacoco = (JacocoCodeCoverage) other;
            List<Integer> otherVector = otherJacoco.getCodeCoverageVector();
            
            // Maximize coverage
            for (int i = 0; i < Math.min(codeCoverageVector.size(), otherVector.size()); i++) {
                codeCoverageVector.set(i, Math.max(codeCoverageVector.get(i), otherVector.get(i)));
            }
        }
    }

    @Override
    public CodeCoverage xor(CodeCoverage other) {
        if (other instanceof JacocoCodeCoverage) {
            JacocoCodeCoverage otherJacoco = (JacocoCodeCoverage) other;
            List<Integer> otherVector = otherJacoco.getCodeCoverageVector();
            List<Integer> xorResult = new java.util.ArrayList<>(codeCoverageVector);
            
            // XOR
            for (int i = 0; i < Math.min(xorResult.size(), otherVector.size()); i++) {
                xorResult.set(i, (codeCoverageVector.get(i) > 0 && otherVector.get(i) == 0) ? 1 : 0);
            }
            
            return new JacocoCodeCoverage(xorResult);
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("JacocoCodeCoverage{size=%d, covered=%d, percent=%.2f%%}", 
                getCodeCoverageVectorSize(), getCoveredAmount(), getPercent());
    }
}