package com.example.transaction.core.util;

import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

public final class UuidModuloShardingAlgorithm implements StandardShardingAlgorithm<UUID> {
    private int shardingCount;

    @Override
    public void init(Properties props) {
        shardingCount = Integer.parseInt(props.getProperty("sharding-count"));
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<UUID> shardingValue) {
        var userId = shardingValue.getValue().toString();
        var shard = Math.abs(userId.hashCode()) % shardingCount;
        for (var shards : availableTargetNames) {
            if (shards.endsWith(String.valueOf(shard))) {
                return shards;
            }
        }
        throw new UnsupportedOperationException("Cannot find shard for user_id: " + userId);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<UUID> shardingValue) {
        return availableTargetNames;
    }

    @Override
    public String getType() {
        return "UUID_MOD";
    }
}