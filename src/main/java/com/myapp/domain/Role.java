package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="roles", uniqueConstraints = @UniqueConstraint(columnNames = {"role"}))
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "role")
    @Unique
    private String role;

    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")},
            inverseJoinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")}
    )
    @JsonIgnore
    private Set<User> userRoles;

    public Role(String role) {
        this.role=role;
    }


}
