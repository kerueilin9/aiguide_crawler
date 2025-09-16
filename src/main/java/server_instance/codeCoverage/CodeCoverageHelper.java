package server_instance.codeCoverage;

public class CodeCoverageHelper {

    private CodeCoverageCollector codeCoverageCollector;
    private CodeCoverage branchCoverage;
    private CodeCoverage statementCoverage;

    public CodeCoverageHelper(CodeCoverageCollector codeCoverageCollector){
        this.codeCoverageCollector = codeCoverageCollector;
        this.resetCumulativeCoverage();
    }

    public void recordCoverage(){
        if(!this.isCodeCoverageRecorded()){
            this.branchCoverage = this.getBranchCoverage();
            this.statementCoverage = this.getStatementCoverage();
        }
        else{
            this.branchCoverage.merge(this.getBranchCoverage());
            this.statementCoverage.merge(this.getStatementCoverage());
        }

    }

    public void resetCumulativeCoverage(){
        this.branchCoverage = null;
        this.statementCoverage = null;
    }

    public CodeCoverage getBranchCoverage(){
        return this.codeCoverageCollector.getBranchCoverage();
    }

    public CodeCoverage getStatementCoverage(){
        return this.codeCoverageCollector.getStatementCoverage();
    }

    public CodeCoverage getCumulativeBranchCoverage(){
        return this.branchCoverage;
    }

    public CodeCoverage getCumulativeStatementCoverage(){
        return this.statementCoverage;
    }

    public void resetCoverage(){
        this.codeCoverageCollector.resetCoverage();
    }

    private Boolean isCodeCoverageRecorded(){
        return (this.branchCoverage != null) || (this.statementCoverage != null);
    }
}
