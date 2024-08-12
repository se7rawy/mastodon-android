package org.joinmastodon.android.fragments.onboarding;

public class InstanceCatalogFacade {
    private InstanceCatalogFragment instanceCatalogFragment = new InstanceCatalogFragment();

    public void loadInstanceInfo(String _domain, boolean isFromRedirect) {
        instanceCatalogFragment.loadInstanceInfo(_domain, isFromRedirect, null);
    }
}