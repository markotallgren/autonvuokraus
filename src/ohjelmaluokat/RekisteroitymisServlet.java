/*
 * Marko Tallgren
 * a1600545
 */

package ohjelmaluokat;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kohdeluokat.InvalidWebUserPoikkeus;
import kohdeluokat.WebUser;
import tietokanta.Dao;
import tietokanta.DaoPoikkeus;
import tietokanta.UsernameVarattuPoikkeus;


/**
 * Servlet implementation class RekisteroitymisServlet
 */
@WebServlet("/rekisteroidy")
public class RekisteroitymisServlet extends HttpServlet {
	
	public static final String FRONT_PAGE = "index.jsp";
	private static final long serialVersionUID = 1L;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public RekisteroitymisServlet() {
        super();
    }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		
		try {

			WebUser kayttaja = new WebUser(username, password, password2);

			Dao dao = new Dao();
			dao.lisaa(kayttaja);
			
			response.sendRedirect("rekisteroidy?onnistui=y");
			
		} catch(InvalidWebUserPoikkeus e) {
			takaisinVirheviestilla(e.getMessage(), username, request, response);
		} catch(UsernameVarattuPoikkeus e) {
			String virheviesti = "Käyttäjätunnus " + username + " on jo varattu, valitse toinen käyttäjätunnus!";
			takaisinVirheviestilla(virheviesti, username, request, response);
		} catch(DaoPoikkeus e) {
			String virheviesti = "Tietokantaan ei nyt saada yhteyttä. Korjaamme vian tuotapikaa!";
			takaisinVirheviestilla(virheviesti, username, request, response);
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException("Salausalgoritmia ei löydy.", e);
		}
	
		
	}
	
	private void takaisinVirheviestilla(String viesti, String username, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("error", viesti);
		request.setAttribute("prev_reg_username", username);
		request.getRequestDispatcher(FRONT_PAGE).forward(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(FRONT_PAGE).forward(request, response);
	}
}

