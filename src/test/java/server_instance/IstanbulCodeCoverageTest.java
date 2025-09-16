package server_instance;

import org.junit.Test;
import server_instance.codeCoverage.CodeCoverage;
import server_instance.codeCoverage.IstanbulCodeCoverage;

import static org.junit.Assert.assertEquals;

public class IstanbulCodeCoverageTest {
    @Test
    public void test_get_covered_amount(){
        Integer[] codeCoverageVector = new Integer[]{0,0,1,1,0};
        CodeCoverage codeCoverage = new IstanbulCodeCoverage(codeCoverageVector);
        assertEquals(2, codeCoverage.getCoveredAmount());
    }

    @Test
    public void test_get_coverage(){
        Integer[] codeCoverageVector = new Integer[]{0,0,1,1,0};
        CodeCoverage codeCoverage = new IstanbulCodeCoverage(codeCoverageVector);
        assertEquals("0.4", String.valueOf(codeCoverage.getPercent()));
    }

    @Test
    public void test_merge(){
        Integer[] sourceCodeCoverageVector = new Integer[]{0,0,1,1,0};
        Integer[] targetCodeCoverageVector = new Integer[]{1,1,1,0,0};
        CodeCoverage sourceCodeCoverage = new IstanbulCodeCoverage(sourceCodeCoverageVector);
        CodeCoverage targetCodeCoverage = new IstanbulCodeCoverage(targetCodeCoverageVector);

        sourceCodeCoverage.merge(targetCodeCoverage);

        assertEquals("0.8", String.valueOf(sourceCodeCoverage.getPercent()));
    }
    @Test
    public void test_xor(){
        Integer[] sourceCodeCoverageVector = new Integer[]{0,0,1,1,0};
        Integer[] targetCodeCoverageVector = new Integer[]{1,0,1,1,0};
        CodeCoverage sourceCodeCoverage = new IstanbulCodeCoverage(sourceCodeCoverageVector);
        CodeCoverage targetCodeCoverage = new IstanbulCodeCoverage(targetCodeCoverageVector);

        CodeCoverage increaseCodeCoverage = sourceCodeCoverage.xor(targetCodeCoverage);

        assertEquals("0.2", String.valueOf(increaseCodeCoverage.getPercent()));
    }
}
