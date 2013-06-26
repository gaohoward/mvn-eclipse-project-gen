package hornetq_project_gen.ui;

import org.eclipse.core.runtime.IProgressMonitor;

public class GenericProgressMonitor implements IProgressMonitor {

    ProgressNotifier notifier;
    boolean canceled = false;
    
    public GenericProgressMonitor() {
        notifier = new DummyNotifier();
    }

    @Override
    public void beginTask(String name, int totalWork) {
        notifier.updateStatus(name + "...");
    }

    @Override
    public void done() {
        notifier.updateStatus("done.");
    }

    @Override
    public void internalWorked(double work) {
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean value) {
        canceled = value;
    }

    @Override
    public void setTaskName(String name) {
        notifier.updateStatus(name + "...");
    }

    @Override
    public void subTask(String name) {
        notifier.updateStatus(name + "...");
    }

    @Override
    public void worked(int work) {
    }

    public class DummyNotifier implements ProgressNotifier {

        @Override
        public void updateStatus(String status) {
            // TODO Auto-generated method stub
            
        }
    }
}
