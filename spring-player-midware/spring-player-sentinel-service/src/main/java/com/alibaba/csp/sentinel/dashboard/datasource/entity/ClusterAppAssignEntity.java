package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.slots.block.Rule;


@SuppressWarnings("all")
public class ClusterAppAssignEntity implements RuleEntity {

    private Long id;
    private String app;
    private String machineId;
    private String ip;
    private Integer port;

    private Boolean belongToApp;

    private Set<String> clientSet;

    private Set<String> namespaceSet;
    private Double maxAllowedQps;

    private Date gmtCreate;

    public String getMachineId() {
        return machineId;
    }

    public ClusterAppAssignEntity setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public ClusterAppAssignEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    public ClusterAppAssignEntity setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Set<String> getClientSet() {
        return clientSet;
    }

    public ClusterAppAssignEntity setClientSet(Set<String> clientSet) {
        this.clientSet = clientSet;
        return this;
    }

    public Set<String> getNamespaceSet() {
        return namespaceSet;
    }

    public ClusterAppAssignEntity setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }

    public Boolean getBelongToApp() {
        return belongToApp;
    }

    public ClusterAppAssignEntity setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }

    public Double getMaxAllowedQps() {
        return maxAllowedQps;
    }

    public ClusterAppAssignEntity setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }

    @Override
    public String toString() {
        return "ClusterAppAssignEntity{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", machineId='" + machineId + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", belongToApp=" + belongToApp +
                ", clientSet=" + clientSet +
                ", namespaceSet=" + namespaceSet +
                ", maxAllowedQps=" + maxAllowedQps +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

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

    @Override
    public Date getGmtCreate() {
        return gmtCreate;
    }

    @Override
    public Rule toRule() {
        return null;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ClusterAppAssignEntity)) { return false; }
        final ClusterAppAssignEntity that = (ClusterAppAssignEntity)o;
        return Objects.equals(app, that.app) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(machineId, that.machineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, ip, port, machineId);
    }

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        ClusterAppAssignEntity clusterAppAssignEntity1 = new ClusterAppAssignEntity();
        clusterAppAssignEntity1.setApp("pegasus-gw-wms");
        clusterAppAssignEntity1.setIp("127.0.0.1");
        clusterAppAssignEntity1.setPort(18310);
        clusterAppAssignEntity1.setMachineId("000000");
        System.out.println(clusterAppAssignEntity1.hashCode());

        ClusterAppAssignEntity clusterAppAssignEntity2 = new ClusterAppAssignEntity();
        clusterAppAssignEntity2.setApp("pegasus-gw-wms");
        clusterAppAssignEntity2.setIp("127.0.0.1");
        clusterAppAssignEntity2.setPort(18310);
        clusterAppAssignEntity2.setMachineId("000000");
        clusterAppAssignEntity2.setId(123L);
        System.out.println(clusterAppAssignEntity2.hashCode());

        Set<ClusterAppAssignEntity> clusterAppAssignEntitySet = new HashSet<>();
        clusterAppAssignEntitySet.add(clusterAppAssignEntity1);
        clusterAppAssignEntitySet.add(clusterAppAssignEntity2);
        System.out.println(clusterAppAssignEntitySet.size());
        System.out.println(clusterAppAssignEntitySet);

        if (clusterAppAssignEntitySet.contains(clusterAppAssignEntity2)) {
            clusterAppAssignEntitySet.remove(clusterAppAssignEntity2);
            clusterAppAssignEntitySet.add(clusterAppAssignEntity2);
        }
        System.out.println(clusterAppAssignEntitySet.size());
        System.out.println(clusterAppAssignEntitySet);


        Map<ClusterAppAssignEntity, String> map = new ConcurrentHashMap<>(16);
        map.put(clusterAppAssignEntity1, "1");
        map.put(clusterAppAssignEntity2, "2");
        System.out.println(map);

        if (map.get(clusterAppAssignEntity2) != null) {
            map.remove(clusterAppAssignEntity2);
            map.put(clusterAppAssignEntity2, "2");
        }
        System.out.println(map);


        ClusterClientEntity clusterClientEntity = new ClusterClientEntity();
        clusterClientEntity.setApp("pegasus-gw-wms");
        clusterClientEntity.setIp("127.0.0.1");
        clusterClientEntity.setPort(8720);
        clusterClientEntity.setMode(1);

        ClusterClientConfig clusterClientConfig = new ClusterClientConfig();
        clusterClientConfig.setConnectTimeout(300);
        clusterClientConfig.setRequestTimeout(300);
        clusterClientConfig.setServerHost("127.0.0.1");
        clusterClientConfig.setServerPort(18310);
        clusterClientEntity.setClientConfig(clusterClientConfig);

        System.out.println(clusterClientEntity);
    }
}
