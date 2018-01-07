package jsf.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import jsf.util.DataConnect;
import jsf.util.SessionUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author it353f713
 */
@ManagedBean
@SessionScoped
public class Customer implements Serializable {

    private StreamedContent file;

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    /**
     * Creates a new instance of Customer
     */
    public Customer() {
    }

    public void downloadFile() {
        HttpSession session = SessionUtils.getSession();
        String name = session.getAttribute("username").toString();
        Connection con = null;
        PreparedStatement ps = null;
        byte[] fileBytes = null;
        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("select * from photo where username = '" + name + "'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                fileBytes = rs.getBytes("file");
            }
            InputStream stream = new ByteArrayInputStream(fileBytes);
            file = new DefaultStreamedContent(stream, "image/jpg", "download.jpeg");

        } catch (SQLException ex) {

        } finally {
            DataConnect.close(con);
        }
    }

}
