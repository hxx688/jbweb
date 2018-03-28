package com.rrgy.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rrgy.core.base.controller.BladeController;
import com.rrgy.core.constant.ConstCache;
import com.rrgy.core.constant.ConstCacheKey;
import com.rrgy.core.constant.ConstCurd;

/**
 * 用于拓展controller类
 */
public class BaseController extends BladeController implements ConstCurd, ConstCache, ConstCacheKey {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

}
