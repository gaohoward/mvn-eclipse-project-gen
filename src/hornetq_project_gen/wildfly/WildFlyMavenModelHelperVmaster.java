package hornetq_project_gen.wildfly;

import hornetq_project_gen.MavenModelHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class WildFlyMavenModelHelperVmaster extends MavenModelHelper {

    public WildFlyMavenModelHelperVmaster(File sourceRoot, File pomFile, File repoBase) throws IOException, XmlPullParserException {
        super(sourceRoot, pomFile, repoBase);
        ignoredLinks.add("jsf/injection/target/generated-sources/annotations");
        ignoredLinks.add("pojo/src/test/_java");
        ignoredLinks.add("sar/src/test/_java");
        ignoredLinks.add("clustering/web/spi/src/test/main");
        ignoredLinks.add("jsf/injection/target/generated-sources/annotations");
        ignoredLinks.add("jsf/injection/src/main/java");
        ignoredLinks.add("core-model-test/test-controller-7.1.2");
        ignoredLinks.add("testsuite/integration/osgi/src/test/java");
        ignoredLinks.add("subsystem-test/test-controller-7.1.2/src/main/java");
        
    }

    @Override
    public String generateProjectName() throws IOException {
       String version = model.getVersion();
       String branch = gitHelper.getBranch();
       if (version != null) {
           return "WF-" + version + "<" + branch + ">";
       }
       return "WildFly";
   }

    /**
     * Those need to copy manually:
     * ./hornetq-jms/target/classes/org/hornetq/jms/HornetQJMSLogger.class
     * @throws CoreException 
     * @throws  
     */
    public void findMissingEntries(IProject project, Map<String, IClasspathEntry> sources) throws CoreException {
       IFolder folder = project.getFolder("generated-classes");

       if (!sources.containsKey(folder.getFullPath().toOSString())) {
          folder.create(false, true, null);
          IClasspathEntry newPath = JavaCore.newLibraryEntry(folder.getFullPath(), null, null);
          sources.put(folder.getFullPath().toOSString(), newPath);
       }
       
       addSingleClassFromTargetDir("clustering/web-infinispan/target/classes/org/jboss/as/clustering/web/infinispan/InfinispanWebLogger.class", folder);
       addSingleClassFromTargetDir("clustering/web-infinispan/target/classes/org/jboss/as/clustering/web/infinispan/InfinispanWebMessages.class", folder);
       addSingleClassFromTargetDir("clustering/web-spi/target/classes/org/jboss/as/clustering/web/impl/ClusteringWebLogger.class", folder);
       addSingleClassFromTargetDir("clustering/web-spi/target/classes/org/jboss/as/clustering/web/impl/ClusteringWebMessages.class", folder);
       addSingleClassFromTargetDir("jpa/spi/target/classes/org/jboss/as/jpa/JpaLogger.class", folder);
       addSingleClassFromTargetDir("jpa/spi/target/classes/org/jboss/as/jpa/JpaMessages.class", folder);
    }

    /**
     * srcRoot/hornetq-core-client/target/classes/hornetq-version.properties
     */
    public void copyResources(IProject project, List<IClasspathEntry> sources) throws CoreException {
    }

    @Override
    protected void addExtraDependencies(List<Dependency> deps) {
       addDep(deps, "org.jboss.shrinkwrap.descriptors", "shrinkwrap-descriptors-api-base", "2.0.0-alpha-3");
       addDep(deps, "org.powermock", "powermock-api-support", "1.5");
       addDep(deps, "org.powermock", "powermock-module-junit4-common", "1.5");
       addDep(deps, "org.powermock", "powermock-core", "1.5");
       addDep(deps, "org.jboss.arquillian.test", "arquillian-test-api", "1.0.3.Final");
       addDep(deps, "org.jboss.arquillian.config", "arquillian-config-api", "1.0.3.Final");
       addDep(deps, "org.hamcrest", "hamcrest-core", "1.3");
       addDep(deps, "org.sonatype.sisu", "sisu-inject-plexus", "2.2.3");
       addDep(deps, "org.apache.directory.shared", "shared-i18n", "1.0.0-M12");
       addDep(deps, "org.apache.directory.shared", "shared-util", "1.0.0-M12");
       addDep(deps, "org.apache.directory.shared", "shared-ldap-model", "1.0.0-M12");
       addDep(deps, "org.apache.directory.server", "apacheds-server-annotations", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-core-annotations", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-core-annotations", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-protocol-kerberos", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-protocol-ldap", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-core-api", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-interceptor-kerberos", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-xdbm-partition", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-kerberos-codec", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-protocol-shared", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-server-config", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-core", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-interceptors-authn", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-jdbm-partition", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-interceptors-changelog", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-interceptors-journal", "2.0.0-M7");
       addDep(deps, "org.apache.directory.server", "apacheds-i18n", "2.0.0-M7");
       addDep(deps, "org.apache.mina", "mina-core", "2.0.4");
       addDep(deps, "bouncycastle", "bcprov-jdk15", "140");
       addDep(deps, "org.jboss.arquillian.config", "arquillian-config-impl-base", "1.0.3.Final");
    }
}
