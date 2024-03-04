package com.ou.accountservice.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author PHONG
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    // @NotNull
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "{user.firstName.notBlank}")
    @NotNull
    @Size(min = 1, max = 45, message = "{user.firstName.invalidSize}")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "{user.lastName.notBlank}")
    @NotNull
    @Size(min = 1, max = 45, message = "{user.lastName.invalidSize}")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Size(max = 300)
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 300)
    @Column(name = "cover_avatar")
    private String coverAvatar;

    @JsonIgnore
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @MapsId
    @OneToOne(optional = false)
    private Account account;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.LAZY)
    private UserStudent userStudent;

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", avatar="
                + avatar + ", coverAvatar=" + coverAvatar + ", userStudent=" + userStudent + "]";
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
