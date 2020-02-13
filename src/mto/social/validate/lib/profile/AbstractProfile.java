/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.profile;

import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author huuloc.tran89
 */
public abstract class AbstractProfile {

    private static final Logger logger = Logger.getLogger(AbstractProfile.class);

    protected int loginType;
    protected long userID;
    protected String gameID;
    protected String socialID;
    protected JSONObject info;

    protected AbstractProfile(long userID, String gameID, String socialID, int loginType) {
        this.userID = userID;
        this.gameID = gameID;
        this.socialID = socialID;
        this.loginType = loginType;
        this.info = new JSONObject();
    }

    protected abstract void setCustomInfo();

    /**
     * @return the loginType
     */
    public int getLoginType() {
        return loginType;
    }

    /**
     * @param loginType the loginType to set
     */
    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    /**
     * @return the userID
     */
    public long getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(long userID) {
        this.userID = userID;
    }

    /**
     * @return the gameID
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * @param gameID the gameID to set
     */
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * @return the socialID
     */
    public String getSocialID() {
        return socialID;
    }

    /**
     * @param socialID the socialID to set
     */
    public void setSocialID(String socialID) {
        this.socialID = socialID;
    }

    /**
     * @return the info
     */
    public Map<String, String> getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(JSONObject info) {
        this.info = info;
    }
}
