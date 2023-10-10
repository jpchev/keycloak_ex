package exkc.shared.api.keycloak.service;

import exkc.shared.api.keycloak.entities.dtos.CreateUtilisateurDto;
import exkc.shared.api.keycloak.entities.dtos.UpdateUtilisateurDto;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Service
public class KeycloakClientService {

    @Value("${api.keycloak.clientSecret}")
    private String clientSecret;

    @Value("${api.keycloak.clientId}")
    private String clientId;

    @Value("${api.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${api.keycloak.realm}")
    private String realm;

    @Value("${api.keycloak.grantType}")
    private String grantType;

    @Value("${api.keycloak.tmpPassword}")
    private String tmpPassword;

    @Value("${api.keycloak.defaultRole}")
    private String defaultRole;

    private Keycloak keycloak;

    public KeycloakClientService() {
    }

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .grantType(grantType)
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    public Keycloak getKeycloakInstance() {
        return keycloak;
    }

    public void createRole(String roleName) {
        var role = new RoleRepresentation();
        role.setName(roleName);

        keycloak.realm(realm)
                .roles()
                .create(role);
    }

    public List<RoleRepresentation> findAllRoles() {
        return keycloak.realm(realm).roles().list();
    }

    public RoleRepresentation findRoleByName(String roleName) {
        return keycloak.realm(realm).roles().get(roleName).toRepresentation();
    }

    public List<UserRepresentation> findAllUsers() {
        return keycloak.realm(realm).users().list();
    }

    public List<UserRepresentation> findUserByUsername(String userName) {
        return keycloak.realm(realm).users().search(userName);
    }

    public List<UserRepresentation> findUserByEmail(String email) {
        return keycloak.realm(realm).users().search(null, null, null, email, null, null);
    }

    public UserRepresentation findUserById(String id) {
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }

    public void deleteUser(String id) {
        keycloak.realm(realm).users().delete(id);
    }

    public void enableDisableUser(String email, boolean enable) {
        UserRepresentation user = findUserByEmail(email).get(0);
        user.setEnabled(enable);
        keycloak.realm(realm).users().get(user.getId()).update(user);
    }

    public void assignRole(String userId, RoleRepresentation roleRepresentation) {
        keycloak
                .realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add((List<RoleRepresentation>) roleRepresentation);
    }

    public String createUser(CreateUtilisateurDto requestDto) throws UtilisateurUserNameAlreadyExistsException {
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(requestDto.getEmail());
        user.setFirstName(requestDto.getPrenom());
        user.setLastName(requestDto.getNom());
        user.setEmail(requestDto.getEmail());
        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersRessource = realmResource.users();

        // Create user (requires manage-users role)

        Response response = usersRessource.create(user);
        if (response.getStatus() == 409) {
            throw new UtilisateurUserNameAlreadyExistsException(user.getEmail());
        }
        String userId = CreatedResponseUtil.getCreatedId(response);

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(true);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(tmpPassword);

        UserResource userResource = usersRessource.get(userId);

        // Set password credential
        userResource.resetPassword(passwordCred);


        RoleRepresentation role = realmResource.roles()//
                .get(defaultRole).toRepresentation();

//        // Assign default realm role to user
        userResource.roles().realmLevel() //
                .add(Arrays.asList(role));
        return userId;
    }

    public void updateUser(String email, UpdateUtilisateurDto updateUtilisateurDto) {
        UserRepresentation userRepresentation = this.findUserByEmail(email).get(0);
        userRepresentation.setFirstName(updateUtilisateurDto.getPrenom());
        userRepresentation.setLastName(updateUtilisateurDto.getNom());
        userRepresentation.setEmail(updateUtilisateurDto.getEmail());
        userRepresentation.setUsername(updateUtilisateurDto.getEmail());
        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .update(userRepresentation);
    }

    public void updateUserPassword(String email, String newPassword, String oldpassword) throws NotAuthorizedException {
        UserRepresentation userRepresentation = this.findUserByEmail(email).get(0);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        byte[] decodedNew = java.util.Base64.getDecoder().decode(newPassword);
        credentialRepresentation.setValue(new String(decodedNew, Charset.forName("UTF-8")));

        byte[] decodedOld = java.util.Base64.getDecoder().decode(oldpassword);

        validateCurrentPassword(userRepresentation.getUsername(), new String(decodedOld, Charset.forName("UTF-8")));
        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credentialRepresentation);
    }

    private void validateCurrentPassword(String username, String password) {
        Keycloak keycloakCheck = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        keycloakCheck.tokenManager().getAccessToken();
    }
}
