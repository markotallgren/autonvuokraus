/*
 * Marko Tallgren
 * a1600545
 */

package ohjelmaluokat;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kohdeluokat.Asiakas;
import kohdeluokat.Auto;
import kohdeluokat.Vuokraus;
import tietokanta.Dao;

/**
 * Servlet implementation class UudenVuokrauksenKasittely
 */
@WebServlet("/UusiVuokraus")
public class UudenVuokrauksenKasittely extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UudenVuokrauksenKasittely() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		RequestDispatcher disp;

		if (action != null && action.equalsIgnoreCase("tee uusi vuokraus")) {
			System.out.println("VUOKRAUS");
			disp = request.getRequestDispatcher("uusiVuokraus.jsp");
			disp.forward(request, response);
		} else if (action != null
				&& action.equalsIgnoreCase("hae asiakkaat ja autot")) {
			System.out.println("HAE AUTOT");
			valmistaNaytto(request, response);
		} else if (action != null && action.equalsIgnoreCase("jatka")) {
			System.out.println("JATKA");
			jatka(request, response);
		}
		else if (action != null && action.equalsIgnoreCase("talleta vuokraus")) {
			System.out.println("TALLETA");
			talleta(request, response);
		}
		else if (action != null && action.equalsIgnoreCase("peruuta")) {
			System.out.println("PERUUTA");
			disp = request.getRequestDispatcher(VuokrausOhjelma.SECOND_PAGE);
			disp.forward(request, response);
		}

	}

	private void jatka(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		Dao dao = new Dao();
		RequestDispatcher disp;
		String date = req.getParameter("date");
		String date2 = req.getParameter("date2");
		int vuokraaja = Integer.parseInt(req.getParameter("vuokraaja"));
		String rekno = req.getParameter("valittuauto");
		Asiakas asiakas;
		Auto auto;
		Vuokraus vuokraus;
		String maksettu = req.getParameter("maksettu");
		Date maksupvm = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		HttpSession session = req.getSession();

		if (rekno == null) {
			req.setAttribute("autoPuuttuu", true);
			req.setAttribute("vuokraaja", vuokraaja);
			
			valmistaNaytto(req, res);
		} 
		else {
		try {

				asiakas = dao.haeAsiakas(vuokraaja);
				auto = dao.haeAuto(rekno);

				Date alku = f.parse(date);
				Date loppu = f.parse(date2);

				long vali = (loppu.getTime() - alku.getTime())
						/ (24 * 60 * 60 * 1000);
				if (alku.equals(loppu))
					vali = 1;

				System.out.println(vali);
				req.setAttribute("vali", vali);
				double kokonaishinta = auto.getVrkhinta() * vali;
				System.out.println(kokonaishinta);

				vuokraus = new Vuokraus(alku, loppu, kokonaishinta, maksupvm,
						asiakas, auto);

				session.setAttribute("vuokraus", vuokraus);
				disp = req.getRequestDispatcher("vuokraus.jsp");
				disp.forward(req, res);
			} catch (ParseException e) {

			} catch (SQLException e) {
				req.setAttribute("TK_VIRHE", true);
				disp = req.getRequestDispatcher(VuokrausOhjelma.FRONT_PAGE);
				disp.forward(req, res);
			}
		}

	}

	private void valmistaNaytto(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		Dao dao = new Dao();
		RequestDispatcher disp;
		String date = req.getParameter("date");
		String date2 = req.getParameter("date2");
		if (date.length() == 0 || date2.length() == 0) {

			req.setAttribute("pvmPuuttuu", true);
			disp = req.getRequestDispatcher("uusiVuokraus.jsp");
			disp.forward(req, res);
		} else {
			if (date.compareTo(date2) <= 0) {
				try {
					List<Asiakas> lista = dao.haeAsiakkaat();
					System.out.println(lista);
					List<Auto> lista2 = dao.haeAutot(date);

					if (lista != null && lista2 != null) {
						req.setAttribute("autothaettu", " on ");
						req.setAttribute("date", date);
						req.setAttribute("date2", date2);
						req.setAttribute("asiakkaat", lista);
						req.setAttribute("autot", lista2);
						disp = req.getRequestDispatcher("uusiVuokraus.jsp");
						disp.forward(req, res);
					} else {
						req.setAttribute("TK_VIRHE", true);
						disp = req
								.getRequestDispatcher(VuokrausOhjelma.FRONT_PAGE);
						disp.forward(req, res);
					}
				} catch (SQLException e) {
					req.setAttribute("TK_VIRHE", true);
					disp = req.getRequestDispatcher(VuokrausOhjelma.FRONT_PAGE);
					disp.forward(req, res);
				}
			} else {
				req.setAttribute("pvmVirhe", true);
				disp = req.getRequestDispatcher("uusiVuokraus.jsp");
				disp.forward(req, res);
			}
		}
	}
	private void talleta (HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher disp;
		HttpSession session = request.getSession();
		Vuokraus vuokraus = (Vuokraus)  session.getAttribute("vuokraus");
		String maksupvm = request.getParameter("maksettu");
		Date pvm = null;
		if ( maksupvm.equals("ON") ) {
			pvm = new Date();
			vuokraus.setMaksupvm(pvm);
		}
		Dao dao = new Dao();
		try {
			if ( dao.lisaaVuokraus(vuokraus) == true) {
				request.setAttribute("LISAYSONNISTUI", true);
				session.removeAttribute("vuokraus");
				disp = request.getRequestDispatcher(VuokrausOhjelma.SECOND_PAGE);
				disp.forward(request, response);
			}
			else {
				request.setAttribute("LISAYSEPAONNISTUI", true);
				session.removeAttribute("vuokraus");
				disp = request.getRequestDispatcher(VuokrausOhjelma.SECOND_PAGE);
				disp.forward(request, response);
			}
		}
		catch (SQLException e) {
			request.setAttribute("TK_VIRHE", true);
			disp = request.getRequestDispatcher(VuokrausOhjelma.FRONT_PAGE);
			disp.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
