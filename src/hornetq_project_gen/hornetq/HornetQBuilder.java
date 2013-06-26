package hornetq_project_gen.hornetq;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class HornetQBuilder extends IncrementalProjectBuilder {

    @Override
    protected IProject[] build(int kind, Map<String, String> args,
            IProgressMonitor monitor) throws CoreException {
        if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            fullBuild(monitor);
         } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
               fullBuild(monitor);
            } else {
               incrementalBuild(delta, monitor);
            }
         }
         return null;
    }

    private void fullBuild(IProgressMonitor monitor) throws CoreException {
        getProject().accept(new HornetQResourceVisitor());
    }

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        delta.accept(new HornetQDeltaVisitor());
    }

    private class HornetQResourceVisitor implements IResourceVisitor {

        @Override
        public boolean visit(IResource resource) throws CoreException {
            System.out.println("full build, resource: " + resource);
            return false;
        }
        
    }
    
    private class HornetQDeltaVisitor implements IResourceDeltaVisitor {

        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
}
