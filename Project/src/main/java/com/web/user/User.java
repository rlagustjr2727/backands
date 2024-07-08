package com.web.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "USER_ID")
    @JsonProperty("id")
    private String userId;

    @Column(name = "USER_NAME", nullable = false)
    @JsonProperty("name")
    private String userName;

    @Column(name = "USER_NICKNAME", nullable = false)
    @JsonProperty("nickname")
    private String userNickName;

    @Column(name = "USER_PASSWORD", nullable = false)
    @JsonProperty("password")
    private String userPassword;

    @Column(name = "USER_EMAIL", nullable = false)
    @JsonProperty("email")
    private String userEmail;

    @Column(name = "USER_DOMAIN", nullable = false)
    @JsonProperty("domain")
    private String userDomain;

    @Temporal(TemporalType.DATE)
    @Column(name = "USER_BIRTH", nullable = false)
    @JsonProperty("birth")
    private Date userBirth;

    @Column(name = "USER_PHONE_NUM", nullable = false)
    @JsonProperty("telNumber")
    private String userPhoneNum;

    @Column(name = "USER_PROFILE_IMAGE")
    private String userProfileImage;
}
