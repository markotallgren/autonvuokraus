/*
 * Marko Tallgren
 * a1600545
 */

package tietokanta;

public class UsernameVarattuPoikkeus extends Exception {

	private static final long serialVersionUID = 1L;

	public UsernameVarattuPoikkeus() {
		
		super("Username on jo varattuna jollain toisella k�ytt�j�ll� tietokannassa.");
		System.out.println("username varattu");
	}
}
