package com.taihaoli.statisticssdk.utils.net;

/**
 * author: Gzp
 * Create on 2018/6/12
 * Description:请求回调接口
 */
public interface ICommCallBack<T> {
    /**
     * 请求成功回调
     * @param tData
     */
    public void onSuccess(Object tData);

    /**
     * 请求失败回调
     * @param e
     */
    public void onFailed(Throwable e);

}
