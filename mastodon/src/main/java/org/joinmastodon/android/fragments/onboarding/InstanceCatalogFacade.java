package org.joinmastodon.android.fragments.onboarding;

public class InstanceCatalogFacade{
    
protected void loadInstanceInfo(String _domain, boolean isFromRedirect){
		loadInstanceInfo(_domain, isFromRedirect, null);
	}

	protected void loadInstanceInfo(String _domain, boolean isFromRedirect, Consumer<Object> onError){
		if(TextUtils.isEmpty(_domain) || _domain.indexOf('.')==-1)
			return;
		String domain=normalizeInstanceDomain(_domain);
		Instance cachedInstance=instancesCache.get(domain);
		if(cachedInstance!=null){
			for(CatalogInstance ci : filteredData){
				if(ci.domain.equals(domain) && ci!=fakeInstance)
					return;
			}
			CatalogInstance ci=cachedInstance.toCatalogInstance();
			filteredData.add(0, ci);
			adapter.notifyItemInserted(0);
			return;
		}
		if(loadingInstanceDomain!=null){
			if(loadingInstanceDomain.equals(domain)){
				return;
			}else{
				cancelLoadingInstanceInfo();
			}
		}
		try{
			new URI("https://"+domain+"/api/v1/instance"); // Validate the host by trying to parse the URI
		}catch(URISyntaxException x){
			if(onError!=null)
				onError.accept(x);
			else
				showInstanceInfoLoadError(domain, x);
			if(fakeInstance!=null){
				fakeInstance.description=getString(R.string.error);
				if(filteredData.size()>0 && filteredData.get(0)==fakeInstance){
					if(list.findViewHolderForAdapterPosition(1) instanceof BindableViewHolder<?> ivh){
						ivh.rebind();
					}
				}
			}
			return;
		}
		loadingInstanceDomain=domain;
		loadingInstanceRequest=new GetInstance();
		loadingInstanceRequest.setCallback(new Callback<>(){
			@Override
			public void onSuccess(Instance result){
				loadingInstanceRequest=null;
				loadingInstanceDomain=null;
				result.uri=domain; // needed for instances that use domain redirection
				instancesCache.put(domain, result);
				if(instanceProgressDialog!=null || onError!=null)
					proceedWithAuthOrSignup(result);
				if(instanceProgressDialog!=null){
					instanceProgressDialog.dismiss();
					instanceProgressDialog=null;
				}
				if(Objects.equals(domain, currentSearchQuery) || Objects.equals(currentSearchQuery, redirects.get(domain)) || Objects.equals(currentSearchQuery, redirectsInverse.get(domain))){
					boolean found=false;
					for(CatalogInstance ci:filteredData){
						if(ci.domain.equals(domain) && ci!=fakeInstance){
							found=true;
							break;
						}
					}
					if(!found){
						CatalogInstance ci=result.toCatalogInstance();
						if(filteredData.size()==1 && filteredData.get(0)==fakeInstance){
							filteredData.set(0, ci);
							adapter.notifyItemChanged(0);
						}else{
							filteredData.add(0, ci);
							adapter.notifyItemInserted(0);
						}
					}
				}
			}
			
			protected void onNextClick(View v){
		String domain=chosenInstance.domain;
		Instance instance=instancesCache.get(domain);
		if(instance!=null){
			proceedWithAuthOrSignup(instance);
		}else{
			showProgressDialog();
			if(!domain.equals(loadingInstanceDomain)){
				loadInstanceInfo(domain, false);
			}
		}
	}
			
}