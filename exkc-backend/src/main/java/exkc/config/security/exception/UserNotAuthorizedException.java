package exkc.config.security.exception;

public class UserNotAuthorizedException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserNotAuthorizedException() {
		super();
	}


	public UserNotAuthorizedException(String permission) {
		super(String.format("Utilisateur doit avoir la permission '%s'", permission));
	}

	public UserNotAuthorizedException(long idDemandeInscription) {
		this("Utilisateur n'a pas de droit sur la structure pour la demande d'inscription : " + idDemandeInscription);
	}
}
