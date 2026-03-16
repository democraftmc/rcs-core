package fr.democraft.rcs.api;

public interface SmartEventPublisher {
    void onCreateRequested(ProvisionRequest request);

    void onCreateCompleted(ProvisionRequest request, ProvisionResult result);

    void onDeleteRequested(DeleteRequest request);

    void onDeleteCompleted(DeleteRequest request, ProvisionResult result);
}

