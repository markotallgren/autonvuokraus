/*
 * Marko Tallgren
 * a1600545
 */

package ohjelmaluokat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UloskirjautumisServlet
 */
@WebServlet("/kirjaudu_ulos")
public class UloskirjautumisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UloskirjautumisServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute(VuokrausOhjelma.SESSION_ATTR_WEBUSER);
		request.getSession().invalidate();
		request.getRequestDispatcher(VuokrausOhjelma.FRONT_PAGE).forward(request, response);
		
	}

}

