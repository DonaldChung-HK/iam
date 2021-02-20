package it.infn.mw.iam.persistence.model;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "iam_account_group")
public class IamAccountGroupMembership {

  @EmbeddedId
  private IamAccountGroupKey id;

  @ManyToOne
  @MapsId("accountId")
  @JoinColumn(name = "account_id")
  IamAccount account;

  @ManyToOne
  @MapsId("groupId")
  @JoinColumn(name = "group_id")
  IamGroup group;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_time", nullable = true)
  Date creationTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end_time", nullable = true)
  Date endTime;

  public IamAccountGroupKey getId() {
    return id;
  }

  public void setId(IamAccountGroupKey id) {
    this.id = id;
  }

  public IamAccount getAccount() {
    return account;
  }

  public void setAccount(IamAccount account) {
    this.account = account;
  }

  public IamGroup getGroup() {
    return group;
  }

  public void setGroup(IamGroup group) {
    this.group = group;
  }


  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  @Override
  @Generated("eclipse")
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((account == null) ? 0 : account.hashCode());
    result = prime * result + ((group == null) ? 0 : group.hashCode());
    return result;
  }

  @Override
  @Generated("eclipse")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IamAccountGroupMembership other = (IamAccountGroupMembership) obj;
    if (account == null) {
      if (other.account != null)
        return false;
    } else if (!account.equals(other.account))
      return false;
    if (group == null) {
      if (other.group != null)
        return false;
    } else if (!group.equals(other.group))
      return false;
    return true;
  }

  public static IamAccountGroupMembership forAccountAndGroup(Instant creationInstant, IamAccount a,
      IamGroup g) {
    IamAccountGroupMembership ag = new IamAccountGroupMembership();
    ag.setAccount(a);
    ag.setGroup(g);
    if (creationInstant != null) {
      ag.setCreationTime(Date.from(creationInstant));
    }
    return ag;
  }

  public static IamAccountGroupMembership forAccountAndGroup(IamAccount a, IamGroup g) {
    return forAccountAndGroup(null, a, g);
  }

}
