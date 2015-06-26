package com.suun.publics.jCaptcha;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

public class suunGenericManageableCaptchaService extends GenericManageableCaptchaService {

	public suunGenericManageableCaptchaService(CaptchaEngine captchaEngine,
			int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
			int captchaStoreLoadBeforeGarbageCollection) {
		super(captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize,
				captchaStoreLoadBeforeGarbageCollection);
	}
	@Override
	public Boolean validateResponseForID(String ID, Object response)
            throws CaptchaServiceException {
        if (!store.hasCaptcha(ID)) {
            throw new CaptchaServiceException("Invalid ID, could not validate unexisting or already validated captcha");
        } else {
            Boolean valid = store.getCaptcha(ID).validateResponse(response);
            return valid;
        }
    }
    // zyfang 新增，为了解决Ajax，提交后移除
    public void removeCaptchaForId(String Id){
    	this.store.removeCaptcha(Id);
    	return ;
    }

}
