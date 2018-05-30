/*
 * Marko Tallgren
 * a1600545
 */

package tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import kohdeluokat.WebUser;
import tietokanta.DaoPoikkeus;
import tietokanta.UsernameVarattuPoikkeus;
import kohdeluokat.*;

public class Dao {

	private Connection yhdista() throws SQLException {
		Connection tietokantayhteys = null;

		String JDBCAjuri = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost:3306/username";

		try {
			Class.forName(JDBCAjuri).newInstance();

			tietokantayhteys = DriverManager.getConnection(url, "username",
					"password");

		} catch (SQLException sqlE) {
			System.err.println("Tietokantayhteyden avaaminen ei onnistunut. "
					+ url + "\n" + sqlE.getMessage() + " " + sqlE.toString()
					+ "\n");
			sqlE.printStackTrace();
			throw (sqlE);
		} catch (Exception e) {
			System.err.println("TIETOKANTALIITTYN VIRHETILANNE: "
					+ "JDBC:n omaa tietokanta-ajuria ei loydy.\n\n"
					+ e.getMessage() + " " + e.toString() + "\n");
			e.printStackTrace();
			System.out.print("\n");
			throw new SQLException();
		}
		return tietokantayhteys;
	}

	protected void suljeYhteys(Connection conn) throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				conn.close();
			}
		} catch (Exception e) {
			throw new SQLException();
		}
	}

	public List<Vuokraus> haeVuokraukset() throws SQLException {
		Vuokraus vuokraus = null;
		Asiakas asiakas;
		Auto auto;
		List<Vuokraus> lista = null;

		String sql = "SELECT vuokraus.numero AS vuokrausnro, vuokrauspvm, paattymispvm, kokonaishinta, "
				+ " maksupvm, asiakas.id AS id, etunimi, sukunimi, osoite, asiakas.postinro AS postinro, postitmp,"
				+ " rekno, merkki, malli, huoltopvm, vrkhinta "
				+ " FROM vuokraus JOIN asiakas ON vuokraaja = asiakas.id JOIN posti p ON "
				+ " asiakas.postinro = p.postinro JOIN auto ON rekno = vuokrakohde "
				+ " ORDER BY vuokrausnro ";

		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);
				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko != null) {
					while (tulosjoukko.next()) {

						vuokraus = teeVuokraus(tulosjoukko);
						asiakas = teeAsiakas(tulosjoukko);
						vuokraus.setVuokraaja(asiakas);
						auto = teeAuto(tulosjoukko);
						vuokraus.setVuokrakohde(auto);

						if (lista == null)
							lista = new ArrayList<Vuokraus>();

						lista.add(vuokraus);
					}

					tulosjoukko.close();
				} else {
					lista = null;
				}
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			suljeYhteys(conn);
		}

		return lista;
	}

	public List<Vuokraus> haeVuokraukset(String pvm) throws SQLException {
		Vuokraus vuokraus = null;
		Asiakas asiakas;
		Auto auto;
		List<Vuokraus> lista = null;

		String sql = "SELECT vuokraus.numero AS vuokrausnro, vuokrauspvm, paattymispvm, kokonaishinta,"
				+ " maksupvm, asiakas.id AS id, etunimi, sukunimi, osoite, asiakas.postinro AS postinro, postitmp,"
				+ " rekno, merkki, malli, huoltopvm, vrkhinta"
				+ " FROM vuokraus JOIN asiakas ON vuokraaja = asiakas.id JOIN posti p ON"
				+ " asiakas.postinro = p.postinro JOIN auto ON rekno = vuokrakohde"
				+ " WHERE vuokrauspvm = ?" + " ORDER BY vuokrausnro";

		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, pvm);
				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko != null) {
					while (tulosjoukko.next()) {

						vuokraus = teeVuokraus(tulosjoukko);
						asiakas = teeAsiakas(tulosjoukko);
						vuokraus.setVuokraaja(asiakas);
						auto = teeAuto(tulosjoukko);
						vuokraus.setVuokrakohde(auto);

						if (lista == null)
							lista = new ArrayList<Vuokraus>();

						lista.add(vuokraus);
					}

					tulosjoukko.close();
				} else {
					lista = null;
				}
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			suljeYhteys(conn);
		}

		return lista;
	}

	private Vuokraus teeVuokraus(ResultSet tulosjoukko) throws SQLException {
		Vuokraus vuokraus = null;
		int numero;
		double kokonaishinta;
		Date vuokrauspvm, paattymispvm, maksupvm;

		if (tulosjoukko != null) {
			try {
				numero = tulosjoukko.getInt("vuokrausnro");
				vuokrauspvm = tulosjoukko.getDate("vuokrauspvm");
				paattymispvm = tulosjoukko.getDate("paattymispvm");
				maksupvm = tulosjoukko.getDate("maksupvm");
				kokonaishinta = tulosjoukko.getDouble("kokonaishinta");

				vuokraus = new Vuokraus(numero, vuokrauspvm, paattymispvm,
						kokonaishinta, maksupvm, null, null);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return vuokraus;
	}

	private Asiakas teeAsiakas(ResultSet tulosjoukko) throws SQLException {
		Asiakas asiakas = null;
		int id;
		String etunimi, sukunimi, osoite, puhelin;
		String postinro, postitmp;
		Posti posti;

		if (tulosjoukko != null) {
			try {
				id = tulosjoukko.getInt("id");
				etunimi = tulosjoukko.getString("etunimi");
				sukunimi = tulosjoukko.getString("sukunimi");
				osoite = tulosjoukko.getString("osoite");
				// puhelin = tulosjoukko.getString("puhelin");
				postinro = tulosjoukko.getString("postinro");
				postitmp = tulosjoukko.getString("postitmp");

				posti = new Posti(postinro, postitmp);

				asiakas = new Asiakas(id, etunimi, sukunimi, osoite, null,
						posti);

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return asiakas;
	}

	private Auto teeAuto(ResultSet tulosjoukko) throws SQLException {
		Auto auto = null;
		String rekno, malli, merkki;
		double vrkhinta;
		Date huoltopvm;

		if (tulosjoukko != null) {
			try {
				rekno = tulosjoukko.getString("rekno");
				malli = tulosjoukko.getString("malli");
				merkki = tulosjoukko.getString("merkki");
				vrkhinta = tulosjoukko.getDouble("vrkhinta");
				huoltopvm = tulosjoukko.getDate("huoltopvm");

				auto = new Auto(rekno, malli, merkki, vrkhinta, huoltopvm);

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return auto;
	}

	public Auto haeAuto(String rekno) throws SQLException {
		String sql = "SELECT rekno, malli, merkki, vrkhinta, huoltopvm"
				+ "	FROM auto WHERE rekno = ?";
		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;
		List<Auto> lista = null;
		Auto auto = null;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, rekno);

				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko.next()) {
					auto = teeAuto(tulosjoukko);
					tulosjoukko.close();

				} else {
					auto = null;
				}
			}

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		}

		finally {
			suljeYhteys(conn);
		}

		return auto;

	}

	public List<Auto> haeAutot(String pvm) throws SQLException {
		String sql = "SELECT rekno, malli, merkki, vrkhinta, huoltopvm"
				+ " FROM auto" + " WHERE NOT EXISTS" + " (SELECT *"
				+ " FROM vuokraus"
				+ " WHERE vuokrakohde = rekno AND paattymispvm >= ?" + " )";
		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;
		List<Auto> lista = null;
		Auto auto;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, pvm);

				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko != null) {
					while (tulosjoukko.next()) {

						auto = teeAuto(tulosjoukko);

						if (lista == null)
							lista = new ArrayList<Auto>();

						lista.add(auto);

					}
					tulosjoukko.close();

				} else {
					lista = null;
				}
			}

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		}

		finally {
			suljeYhteys(conn);
		}

		return lista;

	}

	public Asiakas haeAsiakas(int id) throws SQLException {
		Asiakas asiakas = null;
		String sql = "SELECT id, etunimi, sukunimi, osoite, postinro, postitmp"
				+ " FROM asiakas NATURAL JOIN posti" + " WHERE id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);

				preparedStatement.setInt(1, id);

				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko != null && tulosjoukko.next()) {
					asiakas = teeAsiakas(tulosjoukko);

					tulosjoukko.close();

				} else {
					asiakas = null;

				}
			}

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			suljeYhteys(conn);
		}

		return asiakas;
	}

	public List<Asiakas> haeAsiakkaat() throws SQLException {
		Asiakas asiakas = null;
		List<Asiakas> lista = null;
		String sql = "SELECT id, etunimi, sukunimi, osoite, postinro, postitmp"
				+ " FROM asiakas NATURAL JOIN posti" + " ORDER BY id";
		PreparedStatement preparedStatement = null;
		ResultSet tulosjoukko = null;
		Connection conn = null;

		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

				preparedStatement = conn.prepareStatement(sql);

				tulosjoukko = preparedStatement.executeQuery();

				preparedStatement.close();
				conn.commit();
				conn.close();

				if (tulosjoukko != null) {
					while (tulosjoukko.next()) {

						asiakas = teeAsiakas(tulosjoukko);

						if (lista == null)
							lista = new ArrayList<Asiakas>();

						lista.add(asiakas);

					}
					tulosjoukko.close();

				} else {
					lista = null;
				}
			}

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		}

		finally {
			suljeYhteys(conn);
		}

		return lista;
	}

	public WebUser hae(String username) throws SQLException {
		WebUser kayttaja;
		Connection conn = null;
		String sql = "select id, username, salt, password_hash from webuser where username = ?";

		try {
			conn = this.yhdista();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			PreparedStatement usernameHaku = conn.prepareStatement(sql);
			usernameHaku.setString(1, username);
			ResultSet rs = usernameHaku.executeQuery();

			usernameHaku.close();
			conn.commit();
			conn.close();

			if (rs.next()) {
				kayttaja = new WebUser(rs.getInt("id"),
						rs.getString("username"), rs.getString("salt"),
						rs.getString("password_hash"));

			} else {
				kayttaja = new WebUser(-1, "-", "-", "-");
			}
			rs.close();

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			suljeYhteys(conn);
		}
		return kayttaja;
	}

	public boolean lisaa(WebUser kayttaja) throws UsernameVarattuPoikkeus,
			DaoPoikkeus {
		Connection conn = null;
		boolean ok = false;
		String sql1 = "select id from webuser where username = ?";
		String sql2 = "insert into webuser(username, password_hash, salt) values(?,?,?)";

		try {
			conn = yhdista();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			PreparedStatement usernameHaku = conn.prepareStatement(sql1);
			usernameHaku.setString(1, kayttaja.getUsername());
			ResultSet rs = usernameHaku.executeQuery();

			if (rs.next()) {
				usernameHaku.close();
				rs.close();
				throw new UsernameVarattuPoikkeus();
			}
			usernameHaku.close();
			rs.close();

			PreparedStatement insertLause = conn.prepareStatement(sql2);
			insertLause.setString(1, kayttaja.getUsername());
			insertLause.setString(2, kayttaja.getPasswordHash());
			insertLause.setString(3, kayttaja.getSalt());

			int lkm = insertLause.executeUpdate();
			insertLause.close();

			if (lkm == 1) {
				conn.commit();
				conn.close();
				ok = true;
			} else {
				conn.rollback();
				conn.close();
				ok = false;
			}

		} catch (UsernameVarattuPoikkeus e) {
			throw e;
		} catch (SQLException e) {
			System.out.println("::::SQLException");
			ok = false;
			throw new DaoPoikkeus("Tietokantahaku aiheutti virheen", e);
		} catch (Exception e) {
			System.out.println("::::Exception");
			ok = false;
			throw new DaoPoikkeus("Tietokannan ajuria ei loydy", e);
		} finally {
			try {
				suljeYhteys(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ok;

	}

	public boolean lisaaVuokraus(Vuokraus vuokraus) throws SQLException {
		boolean ok = false;
		String sql = "INSERT INTO vuokraus (vuokrauspvm, paattymispvm, kokonaishinta,"
				+ " maksupvm, vuokraaja, vuokrakohde) VALUES (?, ?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement lause;
		int lkm;

		if (vuokraus != null) {
			try {
				conn = yhdista();
				if (conn != null) {
					conn.setAutoCommit(false);
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

					lause = conn.prepareStatement(sql);
					lause.setString(1, vuokraus.getVuokrauspvmTK());
					lause.setString(2, vuokraus.getPaattymispvmTK());
					lause.setDouble(3, vuokraus.getKokonaishinta());
					lause.setString(4, vuokraus.getMaksupvmTK());
					lause.setInt(5, vuokraus.getVuokraaja().getId());
					lause.setString(6, vuokraus.getVuokrakohde().getRekno());

					lkm = lause.executeUpdate();
					lause.close();
					if (lkm == 1) {
						conn.commit();
						conn.close();
						ok = true;
					} else {
						conn.rollback();
						conn.close();
						ok = false;
					}

				}

			} catch (SQLException e) {
				throw e;
			} catch (Exception e) {
				throw new SQLException();
			} finally {
				suljeYhteys(conn);
			}

		}

		return ok;
	}

}
