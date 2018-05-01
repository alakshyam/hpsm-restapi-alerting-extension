package com.appdynamics.extensions.hpsm.common;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

	private String url;
	private String username;
	private String password;
	private String passwordEncrypted;
	private String encryptionKey;
	private String hpsmVersion;
	private String proxyUri;
	private String proxyUser;
	private String proxyPassword;
	private String closeNotesText;

    private List<Field> fields = new ArrayList<Field>();
    
    private List<Service> services = new ArrayList<Service>();
    
    public String getUrl() {
		return url;
	}

    public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    public void setPasswordEncrypted(String passwordEncrypted) {
        this.passwordEncrypted = passwordEncrypted;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
    
    public String getHpsmVersion() {
		return hpsmVersion;
	}

    public void setHpsmVersion(String hpsmVersion) {
		this.hpsmVersion = hpsmVersion;
	}

    public String getProxyUri() {
        return proxyUri;
    }

    public void setProxyUri(String proxyUri) {
        this.proxyUri = proxyUri;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getCloseNotesText() {
        return closeNotesText;
    }

    public void setCloseNotesText(String closeNotesText) {
        this.closeNotesText = closeNotesText;
    }

    public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
    
    
}
