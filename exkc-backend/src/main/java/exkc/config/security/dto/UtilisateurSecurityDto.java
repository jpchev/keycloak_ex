package exkc.config.security.dto;

import exkc.config.security.EndUserDetails;

import java.util.Map;
import java.util.Set;

public class UtilisateurSecurityDto implements EndUserDetails {

    private long idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String numeroTelephone;
    private Set<String> permissions;
    private Set<String> roles;

    private boolean actif;

    public UtilisateurSecurityDto(Map<String, Object> claims) {
        this.nom = ((String) claims.get("family_name"));
        this.prenom = ((String) claims.get("given_name"));
        this.email = ((String) claims.get("email"));
        this.numeroTelephone = ((String) claims.get("phone_number"));
    }

    @Override
    public String getUniqueName() {
        return nom;
    }

}
