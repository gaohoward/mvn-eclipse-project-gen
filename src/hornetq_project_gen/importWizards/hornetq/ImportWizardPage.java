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
import hornetq_project_gen.hornetq.VersionedModelHelperFactory;

import java.io.File;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ImportWizardPage extends WizardPage {

    protected ImportWizardPage(String pageName, IStructuredSelection selection) {
        super(pageName);
        // TODO Auto-generated constructor stub
    }

    protected DirectoryFieldEditor editor;
    protected Text projectNameText;
    protected Text labelStatus;
    private MavenModelHelper helper;

    public MavenModelHelper getHelper() {
        return helper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls
     * (org.eclipse.swt.widgets.Composite)
     */
    protected void createAdvancedControls(Composite root) {
        Composite parent = new Composite(root, SWT.NONE);
        GridData parentData = new GridData(GridData.FILL_BOTH);
        GridLayout parentLayout = new GridLayout();
        parentLayout.numColumns = 1;
        parentLayout.makeColumnsEqualWidth = true;
        parentLayout.marginWidth = 0;
        parentLayout.marginHeight = 0;
        parent.setLayoutData(parentData);
        parent.setLayout(parentLayout);

        Composite fileSelectionArea = new Composite(parent, SWT.NONE);
        GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.FILL_HORIZONTAL);
        fileSelectionArea.setLayoutData(fileSelectionData);

        GridLayout fileSelectionLayout = new GridLayout();
        fileSelectionLayout.numColumns = 3;
        fileSelectionLayout.makeColumnsEqualWidth = false;
        fileSelectionLayout.marginWidth = 0;
        fileSelectionLayout.marginHeight = 0;
        fileSelectionArea.setLayout(fileSelectionLayout);

        editor = new DirectoryFieldEditor("dirSelect", "Select Src Root: ",
                fileSelectionArea); // NON-NLS-1 //NON-NLS-2
        editor.getTextControl(fileSelectionArea).addModifyListener(
                new ModifyListener() {
                    public void modifyText(ModifyEvent e) {
                        File sourceRoot = new File(ImportWizardPage.this.editor
                                .getStringValue());
                        File pom = new File(sourceRoot, "pom.xml");

                        if (pom.exists()) {
                            try {
                                helper = VersionedModelHelperFactory
                                        .createModelHelper(sourceRoot, pom);
                                Model model = helper.getModel();
                                if (!"org.hornetq".equals(model.getGroupId())
                                        || !"hornetq-pom".equals(model
                                                .getArtifactId())) {
                                    setErrorMessage("Not a HornetQ source root!");
                                }
                                projectNameText.setText(helper.generateProjectName());
                                validatePage();
                                if (isPageComplete()) {
                                    showPom(helper.getPom());
                                }
                            } catch (IOException e1) {
                                setErrorMessage("Error: " + e1);
                            } catch (XmlPullParserException e1) {
                                setErrorMessage("Error: " + e1);
                            }
                            setErrorMessage(null);
                        } else {
                            setErrorMessage("POM not exist: "
                                    + pom.getAbsolutePath());
                        }
                    }
                });
        fileSelectionArea.moveAbove(null);

        Composite projectNameArea = new Composite(parent, SWT.NONE);
        GridData projectNameData = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.FILL_HORIZONTAL);
        projectNameArea.setLayoutData(projectNameData);

        GridLayout projectNameLayout = new GridLayout();
        projectNameLayout.numColumns = 2;
        projectNameLayout.makeColumnsEqualWidth = false;
        projectNameLayout.marginWidth = 0;
        projectNameLayout.marginHeight = 0;
        projectNameArea.setLayout(projectNameLayout);
        
        Label projectNameLabel = new Label(projectNameArea, SWT.HORIZONTAL);
        projectNameLabel.setText("Project Name: ");
        projectNameText = new Text(projectNameArea, SWT.LEFT | SWT.BORDER);
        GridData textData = new GridData(GridData.FILL_HORIZONTAL);
        projectNameText.setLayoutData(textData);
        
        projectNameText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                validatePage();
            }
            
        });

        Composite statusArea = new Composite(parent, SWT.NONE);
        GridData statusData = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.FILL_BOTH);
        statusArea.setLayoutData(statusData);

        GridLayout statusLayout = new GridLayout();
        statusLayout.numColumns = 1;
        statusLayout.makeColumnsEqualWidth = false;
        statusLayout.marginWidth = 0;
        statusLayout.marginHeight = 0;
        statusArea.setLayout(statusLayout);
        
        labelStatus = new Text(statusArea, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
        GridData labelData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        labelStatus.setLayoutData(labelData);
//        labelStatus.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        
        labelStatus.setText("");
    }
    
    private void showPom(String pom) {
        labelStatus.setText(pom);
    }

    @Override
    public void createControl(Composite parent) {
        this.createAdvancedControls(parent);
        setControl(parent);
    }

    public String getProjectName() {
        return projectNameText.getText().trim();
    }
    
    private void validatePage() {
        String value = projectNameText.getText().trim();
        IWorkspaceRoot rootWs = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = rootWs.getProjects();
        boolean conflict = false;
        for (IProject p : projects) {
            if (value.equals(p.getName())) {
                conflict = true;
                break;
            }
        }
        if (conflict) {
            Display.getCurrent().asyncExec(new Runnable() {

                @Override
                public void run() {
                    setErrorMessage("Project name already exists in the workspace, please use a different name");   
                }
                
            });
        } else {
            setErrorMessage(null);
        }        
    }

}
