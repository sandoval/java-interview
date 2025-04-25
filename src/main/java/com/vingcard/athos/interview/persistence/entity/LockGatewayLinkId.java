package com.vingcard.athos.interview.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class LockGatewayLinkId implements Serializable {
    private String lockSerial;
    private String gatewaySerial;

    public LockGatewayLinkId() {}
    public LockGatewayLinkId(String lockSerial, String gatewaySerial) {
        this.lockSerial = lockSerial;
        this.gatewaySerial = gatewaySerial;
    }

    public String getLockSerial() { return lockSerial; }
    public void setLockSerial(String lockSerial) { this.lockSerial = lockSerial; }
    public String getGatewaySerial() { return gatewaySerial; }
    public void setGatewaySerial(String gatewaySerial) { this.gatewaySerial = gatewaySerial; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockGatewayLinkId that = (LockGatewayLinkId) o;
        return Objects.equals(lockSerial, that.lockSerial) && Objects.equals(gatewaySerial, that.gatewaySerial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lockSerial, gatewaySerial);
    }
}
