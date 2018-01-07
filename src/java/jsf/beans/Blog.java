/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.beans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import jsf.util.DataConnect;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author EDU
 */
@Named(value = "blog")
@SessionScoped
public class Blog implements Serializable {

    private String userName;
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Creates a new instance of Blog
     */
    public Blog() {
    }
    
    public void storeFile() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("INSERT INTO photofiles (username, file) VALUES (?, ?)");
            ps.setString(1, userName);
            ps.setBinaryStream(2, file.getInputstream());
            ps.executeUpdate();

            FacesContext.getCurrentInstance().addMessage(
                    "adminform:submit",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "A file has been successfully submitted to " + userName, " "));

        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "adminform:submit",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "uploading failed",
                            ""));
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DataConnect.close(con);
        }

    }
    
}
