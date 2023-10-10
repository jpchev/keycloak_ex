package exkc.api.config.security.dto;

import io.jsonwebtoken.Claims;

public class UserDto implements EndUserDetails {

    private String clientId;
    private String[] roles;

    public UserDto(){}

    public UserDto(Claims claims) {
        this.clientId = ((String) claims.get("sub"));
    }

    @Override
    public String getUniqueName() {
        return clientId;
    }

    public String getClientId(){return clientId;}

    public void setClientId(String clientId){ this.clientId = clientId; }
}
