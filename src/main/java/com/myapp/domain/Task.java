package com.myapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

//@Entity
public class Task {

   /** @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User acceptedBy;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "task_bidder", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> bidBy;

    private TaskCategory category;



    private String name;

    private String description;

    private Date date;

    private String location;

    private String currency;

    private BigDecimal amount;

    private TaskState state;

    @NotNull
    @Column(name = "created_at")
    private Date createdAt;**/

}