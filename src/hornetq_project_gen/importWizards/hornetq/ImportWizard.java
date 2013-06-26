/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package hornetq_project_gen.importWizards.hornetq;

import hornetq_project_gen.MavenModelHelper;
import hornetq_project_gen.ui.GenericProgressMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ImportWizard extends Wizard implements IImportWizard {
	
	ImportWizardPage mainPage;

	public ImportWizard() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
        MavenModelHelper helper = mainPage.getHelper();
        
        if (helper == null)
            return false;
        
        IProgressMonitor progressMonitor = new GenericProgressMonitor();
        IWorkspaceRoot rootWs = ResourcesPlugin.getWorkspace().getRoot();
        try {
            IProject project = rootWs.getProject(mainPage.getProjectName());
            project.create(progressMonitor);
            project.open(progressMonitor);
            //add nature
            IProjectDescription description = project.getDescription();
            description.setNatureIds(new String[] { JavaCore.NATURE_ID });
            project.setDescription(description, progressMonitor);
            
            IJavaProject javaProject = JavaCore.create(project);
            //setup bin output folder
            if (javaProject.getOutputLocation() == null) {
               IFolder binFolder = project.getFolder("bin");
               binFolder.create(false, true, null);
               javaProject.setOutputLocation(binFolder.getFullPath(), null);
            }
            
            //all source files
            Map<String, IClasspathEntry> sources = new HashMap<String, IClasspathEntry>();
            helper.getProjectSources(project, sources);
            
            //all external jars
            helper.getDependencies(sources);

            //jre lib
            IClasspathEntry[] jreEntries= PreferenceConstants.getDefaultJRELibrary();
            for (int i = 0; i < jreEntries.length; i++) {
                sources.put(jreEntries[i].getPath().toOSString(), jreEntries[i]);
            }

            //copy missing entries
            helper.findMissingEntries(project, sources);
            
            //copy resources
            helper.copyResources(project, sources);
            
            //set project classpath
            IClasspathEntry[] allEntries = sources.values().toArray(new IClasspathEntry[0]);
            javaProject.setRawClasspath(allEntries, progressMonitor);
            
            //add a builder
            final String BUILDER_ID = "hornetq_project_gen.hqbuilder";
            IProjectDescription desc = project.getDescription();
            ICommand[] commands = desc.getBuildSpec();
            boolean found = false;

            for (int i = 0; i < commands.length; ++i) {
               if (commands[i].getBuilderName().equals(BUILDER_ID)) {
                  found = true;
                  break;
               }
            }
            if (!found) { 
               //add builder to project
               ICommand command = desc.newCommand();
               command.setBuilderName(BUILDER_ID);
               ICommand[] newCommands = new ICommand[commands.length + 1];

               // Add it before other builders.
               System.arraycopy(commands, 0, newCommands, 1, commands.length);
               newCommands[0] = command;
               desc.setBuildSpec(newCommands);
               project.setDescription(desc, null);
            }
            
        } catch (CoreException e) {
            e.printStackTrace();
            return false;
        }

        return true;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("File Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ImportWizardPage("Import File",selection); //NON-NLS-1
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);        
    }
}
