package cn.huacloud.taxpreference.services.wwx.ase;

import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;

/**
 * @author wangkh
 */
public interface MessageHelper {

    String verifyURL(CallbackQueryDTO queryDTO);

    String decrypt(String encrypt);

    void verifySignature(CallbackQueryDTO queryDTO, String encrypt);
}
