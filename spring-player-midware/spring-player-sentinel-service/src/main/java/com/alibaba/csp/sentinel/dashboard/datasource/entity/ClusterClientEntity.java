package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import java.util.Date;
import java.util.Objects;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.slots.block.Rule;

@SuppressWarnings("all")
public class ClusterClientEntity implements RuleEntity {

    private Long id;
    private String app;
    private String ip;
    private Integer port;

    private Integer mode;
    private ClusterClientConfig clientConfig;

    private Date gmtCreate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getApp() {
        return app;
    }

    public ClusterClientEntity setApp(String app) {
        this.app = app;
        return this;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public ClusterClientEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    @Override
    public Rule toRule() {
        return null;
    }

    public ClusterClientEntity setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getMode() {
        return mode;
    }

    public ClusterClientEntity setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    public ClusterClientConfig getClientConfig() {
        return clientConfig;
    }

    public ClusterClientEntity setClientConfig(ClusterClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ClusterClientEntity)) { return false; }
        final ClusterClientEntity that = (ClusterClientEntity)o;
        return Objects.equals(app, that.app) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, ip, port);
    }

    @Override
    public String toString() {
        return "ClusterClientEntity{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", mode=" + mode +
                ", clientConfig='" + clientConfig + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

}
