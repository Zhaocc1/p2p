package com.zcc.p2p.service.tool;

public interface RedisService {

    void set(String key, String value);

    Object get(String key);

    Long getOnlyNumber();
}
