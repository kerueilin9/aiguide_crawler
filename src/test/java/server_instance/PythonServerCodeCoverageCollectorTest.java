package server_instance;
import org.junit.Test;
import server_instance.codeCoverage.CodeCoverage;
import server_instance.codeCoverage.CodeCoverageCollector;
import server_instance.codeCoverage.IstanbulCodeCoverage;
import server_instance.codeCoverage.PythonServerCodeCoverageCollector;

import static org.junit.Assert.assertEquals;

public class PythonServerCodeCoverageCollectorTest {
    @Test
    public void test_get_statement_coverage(){
//        assertEquals(2, codeCoverage.getCoveredAmount());
        CodeCoverageCollector codeCoverageCollector = new PythonServerCodeCoverageCollector(3001);
        CodeCoverage codeCoverage = codeCoverageCollector.getStatementCoverage();
        System.out.println(codeCoverage.getCoveredAmount());
        System.out.println(codeCoverage.getCodeCoverageVectorSize());
    }

}
