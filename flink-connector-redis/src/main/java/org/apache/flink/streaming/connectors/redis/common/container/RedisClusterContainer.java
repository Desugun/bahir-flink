/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flink.streaming.connectors.redis.common.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * Redis command container if we want to connect to a Redis cluster.
 */
public class RedisClusterContainer implements RedisCommandsContainer, Closeable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterContainer.class);

    private transient JedisCluster jedisCluster;

    /**
     * Initialize Redis command container for Redis cluster.
     *
     * @param jedisCluster JedisCluster instance
     */
    public RedisClusterContainer(JedisCluster jedisCluster) {
        Objects.requireNonNull(jedisCluster, "Jedis cluster can not be null");

        this.jedisCluster = jedisCluster;
    }

    @Override
    public void open() throws Exception {

        // echo() tries to open a connection and echos back the
        // message passed as argument. Here we use it to monitor
        // if we can communicate with the cluster.

        jedisCluster.echo("Test");
    }

    @Override
    public void hset(final String key, final String hashField, final String value) {
        try {
            jedisCluster.hset(key, hashField, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HSET to hash {} error message {}",
                    key, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hset(final byte[] key, final byte[] hashField, final byte[] value) {
        try {
            jedisCluster.hset(key, hashField, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HSET to hash {} error message {}",
                    key, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hincrby(String key, String hashField, String value) {
        try {
            jedisCluster.hincrBy(key, hashField, Integer.parseInt(value));
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HINCRBY to hash {} value{} error message {}",
                    key, value, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hincrby(byte[] key, byte[] hashField, int value) {
        try {
            jedisCluster.hincrBy(key, hashField, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HINCRBY to hash {} and field {} error message {}",
                    key, hashField, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hincrbyfloat(String key, String hashField, String value) {
        try {
            jedisCluster.hincrByFloat(key, hashField, Double.parseDouble(value));
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Cannot send Redis message with command HINCRBYFLOAT to hash {} ,field {} and value {} error message {}",
                    key, hashField, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void hincrbyfloat(byte[] key, byte[] hashField, double value) {
        try {
            jedisCluster.hincrByFloat(key, hashField, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command HINCRBYFLOAT to hash {} fied {} and value {} error message {}",
                    key, hashField, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void rpush(final String listName, final String value) {
        try {
            jedisCluster.rpush(listName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command RPUSH to list {} error message: {}",
                    listName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void lpush(String listName, String value) {
        try {
            jedisCluster.lpush(listName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command LPUSH to list {} error message: {}",
                    listName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void sadd(final String setName, final String value) {
        try {
            jedisCluster.sadd(setName, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command RPUSH to set {} error message {}",
                    setName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void publish(final String channelName, final String message) {
        try {
            jedisCluster.publish(channelName, message);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command PUBLISH to channel {} error message {}",
                    channelName, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void set(final String key, final String value) {
        try {
            jedisCluster.set(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command SET to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void set(final byte[] key, final byte[] value, int expire) {
        try {
            jedisCluster.set(key, value);
            if (expire > 0) {
                jedisCluster.expire(key, expire);
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command SET to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incr(String key) {
        try {
            jedisCluster.incr(key);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCR to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incr(byte[] key) {
        try {
            jedisCluster.incr(key);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCR to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrby(String key, String value) {
        try {
            jedisCluster.incrBy(key, Integer.parseInt(value));
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCRBY to key {} and value {} error message {}",
                    key, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrby(byte[] key, int value) {
        try {
            jedisCluster.incrBy(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCRBY to key {} and value {} error message {}",
                    key, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrbyfloat(String key, String value) {
        try {
            jedisCluster.incrByFloat(key, Double.parseDouble(value));
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCRBYFLOAT to key {} and value {} error message {}",
                    key, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void incrbyfloat(byte[] key, double value) {
        try {
            jedisCluster.incrByFloat(key, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command INCRBYFLOAT to key {} and value {} error message {}",
                    key, value, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void setbit(final byte[] key, final long offset, final byte[] value) {
        try {
            jedisCluster.setbit(key, offset, value);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command SET to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void pfadd(final String key, final String element) {
        try {
            jedisCluster.pfadd(key, element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command PFADD to key {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void zadd(final String key, final String score, final String element) {
        try {
            jedisCluster.zadd(key, Double.valueOf(score), element);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Cannot send Redis message with command ZADD to set {} error message {}",
                    key, e.getMessage());
            }
            throw e;
        }
    }

    @Override
    public void zrem(final String key, final String element) {
        try {
            jedisCluster.zrem(key, element);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("Cannot send Redis message with command ZREM to set {} error message {}",
                    key, e.getMessage());
            }
        }
    }

    /**
     * Closes the {@link JedisCluster}.
     */
    @Override
    public void close() throws IOException {
        this.jedisCluster.close();
    }

}
