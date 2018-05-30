/*
 * Marko Tallgren
 * a1600545
 */

package kohdeluokat;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import security.Salaaja;

public class WebUser {

	private int id;
	private String username;
	private String salt;
	private String passwordHash;

	private static final String SALAUS_ALGORITMI = Salaaja.SHA512;
	private static final int SALAUS_KIERROKSIA = 100;

	public WebUser(String username, String password, String password2)
			throws NoSuchAlgorithmException, UnsupportedEncodingException,
			InvalidWebUserPoikkeus {
		super();

		validoi(username, password, password2);

		this.username = username;


		String suola = Salaaja.generoiSuola();
		setSalt(suola);

		String kryptattuSalasana = Salaaja.salaa(password, getSalt(),
				SALAUS_ALGORITMI, SALAUS_KIERROKSIA);
		setPasswordHash(kryptattuSalasana);
	}

	public WebUser(int id, String username, String salt, String passwordHash) {
		super();
		this.id = id;
		this.username = username;
		this.salt = salt;
		this.passwordHash = passwordHash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}


	public String toString() {
		return "WebUser [id = " + id + ", username=" + username + ", salt = "
				+ salt + ", passwordHash = " + passwordHash + "]";
	}

	private void validoi(String username, String password, String password2)
			throws InvalidWebUserPoikkeus {
		
		if (username == null || username.trim().length() < 3)
			throw new InvalidWebUserPoikkeus(
					"K�ytt�j�tunnuksen t�ytyy olla v�hint��n 3 merkki� pitk�");
		else if (password == null || password.trim().length() < 6)
			throw new InvalidWebUserPoikkeus(
					"Salasanan t�ytyy olla v�hint��n 6 merkki� pitk�");
		else if (!password.equals(password2))
			throw new InvalidWebUserPoikkeus("Salasanat eiv�t t�sm��");
	}


	public boolean vertaaSalasanaa(String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		String kryptattuSalasana = Salaaja.salaa(password, getSalt(),
				SALAUS_ALGORITMI, SALAUS_KIERROKSIA);

		return kryptattuSalasana.equals(this.passwordHash);
	}

}
