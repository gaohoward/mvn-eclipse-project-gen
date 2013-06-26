package hornetq_project_gen.hornetq;

import hornetq_project_gen.MavenModelHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class VersionedModelHelperFactory {

    public static MavenModelHelper createModelHelper(File sourceRoot, File pom) throws IOException, XmlPullParserException {

        String homedir = System.getProperty("user.home");
        File home = new File(homedir);
        File repoBase = new File(home, ".m2/repository");
        if (!repoBase.exists()) {
            throw new IllegalStateException("Maven local repository not exist, install maven please");
        }
        System.out.println("---> repo base: " + repoBase.getAbsolutePath());
        if (!pom.exists()) {
            throw new IllegalStateException("Pom file not exist in " + pom.getAbsolutePath());
        }
        File gitBrFile = new File(sourceRoot, ".git/FETCH_HEAD");
        if (!gitBrFile.exists()) {
            throw new IllegalStateException("Cannot find git file: " + gitBrFile.getAbsolutePath());
        }
        
        BufferedReader reader = null;
        String versionAffix = "master";
        try {
            reader = new BufferedReader(new FileReader(gitBrFile));
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("branch")) {
                    if (line.contains("2.3.x")) {
                        versionAffix = "23x";
                    } else if (line.contains("master")) {
                        versionAffix = "master";
                    }
                    break;
                }
                line = reader.readLine();
            }
        } finally {
            reader.close();
        }
        
        if (versionAffix.equals("master")) {
            System.out.println("it's a branch from master");
            return new MavenModelHelperVmaster(sourceRoot, pom, repoBase);
        } else if (versionAffix.equals("23x")) {
            System.out.println("it's a branch from 2.3.x");
            return new MavenModelHelperV23x(sourceRoot, pom, repoBase);
        }
        return null;
    }

}
