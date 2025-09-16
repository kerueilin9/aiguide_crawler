package server_instance.codeCoverage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class IstanbulCodeCoverage implements CodeCoverage {

    private List<Integer> codeCoverageVector;

    public IstanbulCodeCoverage(){}

    public IstanbulCodeCoverage(Integer[] codeCoverageVector){
        this.codeCoverageVector = Arrays.asList(codeCoverageVector.clone());
    }

    public IstanbulCodeCoverage(List<Integer> codeCoverageVector){
        this.codeCoverageVector = codeCoverageVector;
    }

    @Override
    public void setCodeCoverageVector(List<Integer> codeCoverageVector) {
        this.codeCoverageVector = codeCoverageVector;
    }

    @Override
    public List<Integer> getCodeCoverageVector() {
        return convertToOneHot();
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
        if (codeCoverage.getCodeCoverageVectorSize() != this.getCodeCoverageVectorSize())
            System.out.println("Warning: Origin code coverage size is " + this.getCodeCoverageVectorSize() +
                                        ", New code coverage size is " + codeCoverage.getCodeCoverageVectorSize());
        List<Integer> targetCodeCoverageVector = codeCoverage.getCodeCoverageVector();
        for(int i = 0; i < codeCoverage.getCodeCoverageVectorSize(); i++){
            if (i > this.getCodeCoverageVectorSize() - 1){
                this.codeCoverageVector.add(targetCodeCoverageVector.get(i));
            }
            else if (this.codeCoverageVector.get(i)==0)
                this.codeCoverageVector.set(i, targetCodeCoverageVector.get(i));
        }
        System.out.println("Debug: New Origin code coverage size is " + this.getCodeCoverageVectorSize());
    }

    @Override
    public CodeCoverage xor(CodeCoverage codeCoverage) {
        if (codeCoverage.getCodeCoverageVectorSize() != this.getCodeCoverageVectorSize()) throw new RuntimeException();

        List<Integer> targetCodeCoverageVector = codeCoverage.getCodeCoverageVector();
        List<Integer> exclusiveCodeCoverageVector = new ArrayList<>();

        for(int i = 0; i < this.getCodeCoverageVectorSize(); i++){
            int source = this.codeCoverageVector.get(i), target = targetCodeCoverageVector.get(i);
            boolean isSourceCovered = source!=0, isTargetCovered = target!=0;

            if (isSourceCovered && !isTargetCovered) exclusiveCodeCoverageVector.add(source);
            if (!isSourceCovered && isTargetCovered) exclusiveCodeCoverageVector.add(target);
            if ((isSourceCovered && isTargetCovered) || (!isSourceCovered && !isTargetCovered) )
                exclusiveCodeCoverageVector.add(0);
        }

        return new IstanbulCodeCoverage(exclusiveCodeCoverageVector);
    }

    private List<Integer> convertToOneHot() {
        return this.codeCoverageVector.stream()
                .map(i -> {
                    if (i!=0) i = 300;
                    return i;
                })
                .collect(Collectors.toList());
    }
}
