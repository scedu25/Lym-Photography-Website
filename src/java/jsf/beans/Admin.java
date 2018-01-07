/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.beans;

import jsf.util.DataConnect;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author IT353F713
 */
@Named(value = "admin")
@SessionScoped
public class Admin implements Serializable {

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
     * Creates a new instance of Admin
     */
    public Admin() {
    }

    public void storeFile() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("INSERT INTO photo (username, file) VALUES (?, ?)");
            ps.setString(1, userName);
            ps.setBinaryStream(2, file.getInputstream());
            ps.executeUpdate();

            FacesContext.getCurrentInstance().addMessage(
                    "adminform:submit",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "File successfully submitted to " + userName, "."));

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
    
    public void deleteFile() {
                Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("DELETE FROM photo WHERE photo_id = (SELECT MAX(photo_id) FROM photo)");

            ps.executeUpdate();

            FacesContext.getCurrentInstance().addMessage(
                    "adminform:delete",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "File successfully deleted.", ""));

        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "adminform:delete",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "File deletion failed",
                            ""));
        } finally {
            DataConnect.close(con);
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        
        UploadedFile file = event.getFile();
        
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DataConnect.getConnection();
            ps = con.prepareStatement("INSERT INTO photo (username, file) VALUES (?, ?)");
            ps.setString(1, userName);
            ps.setBinaryStream(2, file.getInputstream());
            ps.executeUpdate();

            FacesContext.getCurrentInstance().addMessage(
                    "multiform:submit",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "A file has been successfully submitted to " + userName, " "));

        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "multiform:submit",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "uploading failed",
                            ""));
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DataConnect.close(con);
        }
        
        FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
}
