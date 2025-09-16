package server_instance.codeCoverage;

import util.JsonFileParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PythonServerCodeCoverageCollector  implements CodeCoverageCollector {
    private String url;

    public PythonServerCodeCoverageCollector(int port) {
        this.url = "http://localhost:" + port;
    }

    private List<Integer> getStatementCoverageVector() {
        JsonFileParser parser = new JsonFileParser(this.url + "/coverage-app/object");
        List<Integer> coverageVector = new ArrayList<>();
        for(String key: parser.getAllKeys("files")) {
            List<Integer> executedLines = this.stringListToIntList(parser.getJsonFileArray("files", key, "executed_lines"));
            List<Integer> missingLines = this.stringListToIntList(parser.getJsonFileArray("files", key, "missing_lines"));
            List<Integer> excludedLines = this.stringListToIntList(parser.getJsonFileArray("files", key, "excluded_lines"));

            List<Integer> codeLineVector = new ArrayList<>(executedLines);
            codeLineVector.addAll(missingLines);
            codeLineVector.addAll(excludedLines);
            Collections.sort(codeLineVector);


            List<Integer> fileCodeCoverageVector = new ArrayList<>();
            for(int codeLine: codeLineVector){
                if (executedLines.contains(codeLine))
                    fileCodeCoverageVector.add(codeLine);
                else
                    fileCodeCoverageVector.add(0);
            }
//            System.out.println(key + ": " + fileCodeCoverageVector.size());
//            System.out.println(executedLines);
//            System.out.println(missingLines);
//            System.out.println(excludedLines);
            coverageVector.addAll(fileCodeCoverageVector);
        }
        return coverageVector;
    }

    @Override
    public CodeCoverage getBranchCoverage() {
        return new IstanbulCodeCoverage(this.getStatementCoverageVector());
    }

    @Override
    public CodeCoverage getStatementCoverage() {
        return new IstanbulCodeCoverage(this.getStatementCoverageVector());
    }

    public void resetCoverage() {
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(url + "/coverage/reset").openConnection();
            http.setRequestMethod("GET");
            InputStream input = http.getInputStream();
            byte[] data = new byte[1024];
            int idx = input.read(data);
            String str = new String(data, 0, idx);
            System.out.println("Reset coverage: " + str);
            input.close();
            http.disconnect();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Reset coverage error!!");
        }
    }

    private List<Integer> stringListToIntList(List<String> stringList){
        List<Integer> intList = new ArrayList<>();
        for (String stringElement: stringList){
            intList.add(Integer.parseInt(stringElement));
        }
        return intList;
    }
}
