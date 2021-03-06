package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User implements SocialUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Size(min = 4, max = 30)
    @Setter
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 4, max = 30)
    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Micropost> microposts;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Relationship> followerRelations;

    @OneToMany(mappedBy = "followed", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Relationship> followedRelations;

    @OneToOne(cascade={CascadeType.DETACH, CascadeType.REFRESH}, orphanRemoval=true)
    @JoinTable(name="user_roles",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}
    )
    @Getter
    @Setter
    private Role role;

    @Override
    @JsonProperty("email")
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
      //  return Collections.singleton(() -> "ROLE_USER");
        return Collections.singleton(() -> role.getRole());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public String getUserId() {
        return getUsername();
    }
}
