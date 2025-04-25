package com.vingcard.athos.interview.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lock")
public class Lock {
    @Id
    @Column(length = 16, nullable = false, unique = true)
    private String serial;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private String macAddress;

    @Column(nullable = false)
    private boolean online = false;

    @Column(length = 20, nullable = false)
    private String version = "";

    public Lock() {}

    public Lock(String serial, String name, String macAddress, boolean online, String version) {
        this.serial = serial;
        this.name = name;
        this.macAddress = macAddress;
        this.online = online;
        this.version = version;
    }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}
