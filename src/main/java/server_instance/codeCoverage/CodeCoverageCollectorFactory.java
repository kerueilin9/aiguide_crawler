package server_instance.codeCoverage;

public class CodeCoverageCollectorFactory {
    
    public static CodeCoverageCollector createCollector(String coverageType, int port) {
        if (coverageType == null) {
            coverageType = "none";
        }
        
        switch (coverageType.toLowerCase()) {
            case "jacoco":
                System.out.println("Creating JaCoCo code coverage collector for port " + port);
                return new JacocoCodeCoverageCollector(port);
                
            case "istanbul":
                System.out.println("Creating Istanbul code coverage collector for port " + port);
                return new IstanbulCodeCoverageCollector(port);
                
            case "none":
            default:
                System.out.println("Creating no-op code coverage collector");
                return new NoCodeCoverageCollector();
        }
    }
    
    public static CodeCoverageCollector createJacocoCollector(int port) {
        return new JacocoCodeCoverageCollector(port);
    }
    
    public static CodeCoverageCollector createIstanbulCollector(int port) {
        return new IstanbulCodeCoverageCollector(port);
    }
}