/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearch;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ben
 */
@Entity
@Table(name = "campaign")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c"),
    @NamedQuery(name = "Campaign.findById", query = "SELECT c FROM Campaign c WHERE c.id = :id"),
    @NamedQuery(name = "Campaign.findByKeyword", query = "SELECT c FROM Campaign c WHERE c.keyword = :keyword"),
    @NamedQuery(name = "Campaign.findByStatus", query = "SELECT c FROM Campaign c WHERE c.status = :status"),
    @NamedQuery(name = "Campaign.findByMailObject", query = "SELECT c FROM Campaign c WHERE c.mailObject = :mailObject"),
    @NamedQuery(name = "Campaign.findByMailContent", query = "SELECT c FROM Campaign c WHERE c.mailContent = :mailContent"),
    @NamedQuery(name = "Campaign.findByMailFileName", query = "SELECT c FROM Campaign c WHERE c.mailFileName = :mailFileName")})
public class Campaign implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "status")
    private String status;
    @Size(max = 300)
    @Column(name = "mailObject")
    private String mailObject;
    @Size(max = 1000)
    @Column(name = "mailContent")
    private String mailContent;
    @Size(max = 300)
    @Column(name = "mailFileName")
    private String mailFileName;
    @Lob
    @Column(name = "mailFileContent")
    private byte[] mailFileContent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaignId")
    private Collection<Email> emailCollection;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public static final String SCRAPPING_PENDING = "SCRAPPING_PENDING";
    public static final String SCRAPPING_STARTED = "SCRAPPING_STARTED";
    public static final String SCRAPPING_DONE = "SCRAPPING_DONE";
    public static final String MAILING_PENDING = "MAILING_PENDING";
    public static final String MAILING_STARTED = "MAILING_STARTED";
    public static final String MAILING_DONE = "MAILING_DONE";

    public Campaign() {
    }

    public Campaign(Integer id) {
        this.id = id;
    }

    public Campaign(Integer id, String keyword, String status) {
        this.id = id;
        this.keyword = keyword;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMailObject() {
        return mailObject;
    }

    public void setMailObject(String mailObject) {
        this.mailObject = mailObject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getMailFileName() {
        return mailFileName;
    }

    public void setMailFileName(String mailFileName) {
        this.mailFileName = mailFileName;
    }

    public byte[] getMailFileContent() {
        return mailFileContent;
    }

    public void setMailFileContent(byte[] mailFileContent) {
        this.mailFileContent = mailFileContent;
    }

    @XmlTransient
    public Collection<Email> getEmailCollection() {
        return emailCollection;
    }

    public void setEmailCollection(Collection<Email> emailCollection) {
        this.emailCollection = emailCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getEmailCollectionToString() {
        if (this.emailCollection.isEmpty())
            return "";

        String res = "Adresses : ";
        for (Email email : this.emailCollection) {
            res += email.getEmail() + ",";
        }

        return res.substring(0, res.length()-1);
    }
    
    public boolean getSendable() {
        return this.getStatus().equals(SCRAPPING_DONE);
    }

    public String getNiceStatus() {
        String res = "Statut : ";

        switch (this.getStatus()) {
            case SCRAPPING_PENDING :
                res += "En attente de récupération";
                break;
            case SCRAPPING_STARTED :
                res += "Récupération en cours";
                break;
            case SCRAPPING_DONE :
                res += "Récupération terminée";
                break;
            case MAILING_PENDING :
                res += "En attente d'envoi";
                break;
            case MAILING_STARTED :
                res += "Envoi en cours";
                break;
            case MAILING_DONE :
                res += "Envoi terminé";
                break;
            default:
                res += this.getStatus();
                break;
        }

        return res;
    }

    public boolean hasDisplayableAddresses() {
        return this.getStatus().equals(SCRAPPING_DONE)
            || this.getStatus().equals(MAILING_PENDING)
            || this.getStatus().equals(MAILING_STARTED)
            || this.getStatus().equals(MAILING_DONE)
        ;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mailsearch.Campaign[ id=" + id + " ]";
    }

}
