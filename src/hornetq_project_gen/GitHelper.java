package hornetq_project_gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GitHelper {

    File gitLoc;
    
    public GitHelper(File sourceRoot) {
        gitLoc = new File(sourceRoot, ".git");
    }

    public String getBranch() throws IOException {
        File gitBrFile = new File(gitLoc, "HEAD");
        if (!gitBrFile.exists()) {
            throw new IllegalStateException("Cannot find git file: " + gitBrFile.getAbsolutePath());
        }
        
        BufferedReader reader = null;
        String branch = "unknown";
        try {
            reader = new BufferedReader(new FileReader(gitBrFile));
            String line = reader.readLine();
            while (line != null) {
                int index = line.lastIndexOf("/");
                if (index != -1) {
                    branch = line.substring(index + 1);
                    break;
                }
                line = reader.readLine();
            }
        } finally {
            reader.close();
        }
        return branch;
    }

}
