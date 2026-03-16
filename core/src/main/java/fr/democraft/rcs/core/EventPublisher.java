package fr.democraft.rcs.core;

import fr.democraft.rcs.api.DeleteRequest;
import fr.democraft.rcs.api.ProvisionRequest;
import fr.democraft.rcs.api.ProvisionResult;
import fr.democraft.rcs.api.SmartEventPublisher;

public final class EventPublisher implements SmartEventPublisher {
    @Override
    public void onCreateRequested(ProvisionRequest request) {
    }

    @Override
    public void onCreateCompleted(ProvisionRequest request, ProvisionResult result) {
    }

    @Override
    public void onDeleteRequested(DeleteRequest request) {
    }

    @Override
    public void onDeleteCompleted(DeleteRequest request, ProvisionResult result) {
    }
}

