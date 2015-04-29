
package cz.muni.fi.pv168.web;

import org.library.entity.Book;
import org.library.managers.BookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LibraryServlet", urlPatterns = {"/library/*", "/index.jsp"})
public class LibraryServlet extends HttpServlet {
    public static final String URL_MAPPING = "/library";

    private final static Logger log = LoggerFactory.getLogger(LibraryServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showBooksList(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                //načtení POST parametrů z formuláře
                String iso = request.getParameter("iso");
                String edition = request.getParameter("edition");
                //kontrola vyplnění hodnot
                if (iso == null || iso.length() == 0) {
                    request.setAttribute("chyba", "Je nutné vyplnit ISBN !");
                    showBooksList(request, response);
                    return;
                }
                //zpracování dat - vytvoření záznamu v databázi
                try {
                    if(edition == null || edition.length() == 0) edition = "0";
                    Book book = new Book();
                    book.setEdition(Integer.parseInt(edition));
                    book.setIso(iso);
                    getBookManager().addBook(book);
                        log.debug("created {}",book);
                    //redirect-after-POST je ochrana před vícenásobným odesláním formuláře
                        response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot add book", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Book book = new Book();
                    book.setIdBook(id);
                    getBookManager().deleteBook(book);
                    log.debug("deleted book {}",id);
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot delete book", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/edit":
                String id = request.getParameter("id");
                Book book = getBookManager().findBookById(Long.valueOf(id));
                request.setAttribute("update", id);
                request.setAttribute("iso", book.getIso());
                request.setAttribute("edition", Integer.toString(book.getEdition()));
                showBooksList(request, response);
                return;
            case "/update":
                //načtení POST parametrů z formuláře
                id = request.getParameter("id");
                iso = request.getParameter("iso");
                edition = request.getParameter("edition");
                //kontrola vyplnění hodnot
                if (id == null || id.length() == 0) {
                    request.setAttribute("chyba", "Je nutné vyplnit id !");
                    showBooksList(request, response);
                    return;
                }
                if (iso == null || iso.length() == 0) {
                    request.setAttribute("chyba", "Je nutné vyplnit ISBN !");
                    showBooksList(request, response);
                    return;
                }
                //zpracování dat - vytvoření záznamu v databázi
                try {
                    if(edition == null || edition.length() == 0) edition = "0";
                    book = new Book();
                    book.setIdBook(Long.valueOf(id));
                    book.setEdition(Integer.parseInt(edition));
                    book.setIso(iso);
                    if(!getBookManager().updateBook(book)) throw new IllegalArgumentException("Id not found");
                    log.debug("updateded {}",book);
                    //redirect-after-POST je ochrana před vícenásobným odesláním formuláře
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot add book", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets BookManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return BookManager instance
     */
    private BookManager getBookManager() {
        return (BookManager) getServletContext().getAttribute("bookManager");
    }

    /**
     * Stores the list of books to request attribute "books" and forwards to the JSP to display it.
     */
    private void showBooksList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("books", getBookManager().findAllBooks());
            request.getRequestDispatcher("/list.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}