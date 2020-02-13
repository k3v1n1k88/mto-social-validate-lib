/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.profile;

import java.util.Set;
import mto.social.validate.lib.type.LoginType;
import org.json.simple.JSONObject;

/**
 *
 * @author huuloc.tran89
 */
public class AppleProfile extends AbstractProfile {

    public JSONObject publicProfile;
    public String accessToken;
    public String refreshToken;

    public AppleProfile(long userID, String gameID, String socialID) {
        super(userID, gameID, socialID, LoginType.APPLE_LOGIN_TYPE);
    }

    @Override
    protected void setCustomInfo() {
        if (publicProfile != null && !publicProfile.isEmpty()) {
            Set keySet = publicProfile.keySet();
            for (Object k : keySet) {
                if (k != null && publicProfile.get(k) != null) {
                    info.put((String) k, publicProfile.get(k));
                }
            }
        }
    }

    /**
     * @return the publicProfile
     */
    public JSONObject getPublicProfile() {
        return publicProfile;
    }

    /**
     * @param publicProfile the publicProfile to set
     */
    public void setPublicProfile(JSONObject publicProfile) {
        this.publicProfile = publicProfile;
    }

}
