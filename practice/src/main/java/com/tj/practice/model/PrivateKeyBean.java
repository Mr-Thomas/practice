package com.tj.practice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrivateKeyBean implements Serializable {
    private String privateKeyObject ;

    private String publicKeyOjbect;

    public PrivateKeyBean() {}

    public PrivateKeyBean(String privateKeyObject, String publicKeyOjbect) {
        this.privateKeyObject = privateKeyObject ;
        this.publicKeyOjbect = publicKeyOjbect;
    }
}
