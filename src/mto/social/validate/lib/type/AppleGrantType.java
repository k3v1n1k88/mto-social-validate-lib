/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.type;

/**
 *
 * @author vanntl
 */
public enum AppleGrantType {
    
    AUTHORIZATION("authorization_code"),
    VALIDATION("refresh_token");
    private String type;

    private AppleGrantType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
