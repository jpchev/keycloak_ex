package exkc.shared.api.keycloak.entities.dtos;

public class CreateUtilisateurDto {
    private String email;
    private String prenom;
    private String nom;

    public String getEmail() {
        return email;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }
}
