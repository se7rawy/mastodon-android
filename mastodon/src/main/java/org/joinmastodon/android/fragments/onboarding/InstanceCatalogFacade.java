package org.joinmastodon.android.fragments.onboarding;

public class InstanceCatalogFacade extends InstanceCatalogFragment{
    

    public void loadInstanceInfo(String _domain, boolean isFromRedirect) {
        loadInstanceInfo(_domain, isFromRedirect, null);
    }
}